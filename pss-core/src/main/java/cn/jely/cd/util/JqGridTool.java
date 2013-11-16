package cn.jely.cd.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.query.ObjectQuery;

/**
 * 对前端发到后台的json格式进行解析.
 * 
 * @author {周义礼}
 * 
 */
public class JqGridTool {
	// private static JqGridTool instance = new JqGridTool();
	private static final String SUBGROUPS = "groups";
	private static final String RULE_DATA = "data";
	private static final String RULE_OP = "op";
	private static final String RULE_FIELD = "field";
	private static final String GROUP_OPERATION = "groupOp";
	private static final String RULES_KEY = "rules";
	// private static StringBuilder filterBuilder = null;
	// private static List<JqGridFieldValue> values = null;// 因为有不同的组,所以不能放在方法里.
	private static boolean parseFlag;// init value is false;

	// private JqGridTool() {
	//
	// }
	//
	// public static JqGridTool getInstance() {
	// return instance;
	// }

	/**
	 * 解析单个查询条件,(当前端jqgrid multiselect设置为false时用此解析)
	 * 
	 * @param sField
	 * @param sOper
	 * @param sString
	 * @return 查询条件的封装对象
	 */
	public static ObjectQuery parseSingle(String sField, String sOper, String sString) {
		ObjectQuery objectQuery = new ObjectQuery();
//		StringBuilder whereString = new StringBuilder().append("o.").append(sField);
		StringBuilder whereString = new StringBuilder().append(sField);
		FieldOperation fo = FieldOperation.valueOf(FieldOperation.class, sOper);
		if (StringUtils.isBlank(sString)) { // 如果为空则查询是否为Null,就没有字段值了
			if (FieldOperation.eq.toString().equals(sOper)) {// 如果="" 那就是is null
				sOper = FieldOperation.n.toString();
			} else if (FieldOperation.ne.toString().equals(sOper)) {// 如果!=""
																	// 那就是 is
																	// not null
				sOper = FieldOperation.nn.toString();
			}
			whereString.append(fo.getOperate());
			objectQuery.addWhere(whereString.toString(), null, null);
		} else { // 如果不为空则表示需要为参数设置值.
			if (sField.contains(".")) {// 用点作为命名参数会出错
				sField.replaceAll(".", "_");
			}
			whereString.append(fo.getOperate()).append(":").append(sField);
			objectQuery.addWhere(whereString.toString(), sField, prepareValue(fo, sString));
		}
		return objectQuery;
	}

	/**
	 * 根据前台filters的内容修改查询对象
	 * 
	 * @param jsonString
	 * @param objectQuery
	 * @return 修改后的查询对象
	 */
	public static ObjectQuery parseJqgridJson(String jsonString) {
		ObjectQuery objectQuery = new ObjectQuery();
		if (StringUtils.isBlank(jsonString))
			return objectQuery;
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			throw new RuntimeException("Parse JSONString error!");
		}
		return parseGroupJson(jsonObject, objectQuery);
	}

	public static ObjectQuery parseJqgridJson(JSONObject jsonObject) {
		// filterBuilder = new StringBuilder(100);
		// values = new ArrayList();// 因为有不同的组,所以不能放在方法里.
		ObjectQuery objectQuery = new ObjectQuery();
		parseGroupJson(jsonObject, objectQuery);
		// objectQuery.addWhere(filterBuilder.toString(), values);
		return objectQuery;
	}

	/**
	 * 解析JSON多条件查询对象为hql
	 * 
	 * @param jsonObj
	 *            传入JqGrid的filter的json对象
	 * @return
	 */
	private static ObjectQuery parseGroupJson(JSONObject jsonObj, ObjectQuery objectQuery) {
		// StringBuilder whereBuilder=new StringBuilder();
		if (jsonObj.containsKey(GROUP_OPERATION)) {
			objectQuery.addWhere("(", null, null);
			// whereBuilder.append("(");
			String groupOp = jsonObj.getString(GROUP_OPERATION);
			if (jsonObj.containsKey(RULES_KEY)) {
				JSONArray jsonRules = jsonObj.getJSONArray(RULES_KEY);
				parseRule(groupOp, jsonRules, objectQuery);
				// if (tmpList != null) {
				// values.addAll(tmpList);
				// }
				// 如果有Rule选项并且子选项中也有值 ，则在它们中添加条件
				if (jsonObj.containsKey(SUBGROUPS) && jsonObj.getJSONArray(SUBGROUPS) != null
						&& jsonObj.getJSONArray(SUBGROUPS).size() > 0 && jsonRules != null && jsonRules.size() > 0) {
					// whereBuilder.append(" ").append(groupOp).append(" ");
					objectQuery.addWhere(groupOp, null, null);
				}
			}
			if (jsonObj.containsKey(SUBGROUPS)) {
				JSONArray jsonSubGroups = jsonObj.getJSONArray(SUBGROUPS);
				if (jsonSubGroups != null && jsonSubGroups.size() > 0) {
					for (int i = 0; i < jsonSubGroups.size(); i++) {
						if (i > 0) {
							// whereBuilder.append(" ").append(groupOp).append(" ");
							objectQuery.addWhere(groupOp, null, null);
						}
						parseGroupJson((JSONObject) jsonSubGroups.get(i), objectQuery);
					}
				}
			}
			objectQuery.addWhere(")", null, null);
		}

		return objectQuery;
	}

	/**
	 * 根据json数组(条件组)解析查询条件
	 * 
	 * @param groupOp
	 * @param jsonArray
	 * @return List 返回
	 */
	private static ObjectQuery parseRule(String groupOp, JSONArray jsonArray, ObjectQuery objectQuery) {
		List<JqGridFieldValue> localvalues = null;
		if (jsonArray != null) {
			localvalues = new ArrayList<JqGridFieldValue>();
			// Map<String, Object> paramValueMap =
			// objectQuery.getParamValueMap();
			for (int i = 0; i < jsonArray.size(); i++) { // 解析每个条件
				JSONObject jsonrule = (JSONObject) jsonArray.get(i);
				String ruleData = jsonrule.getString(RULE_DATA);
				String ruleField = jsonrule.getString(RULE_FIELD);
				if (StringUtils.isBlank(ruleField) && StringUtils.isBlank(ruleData)) { // 如果条件为空,则忽略此条件
					continue;
				}
				StringBuilder whereBuilder = new StringBuilder();
				if (i > 0) {
					whereBuilder.append(groupOp);
				}
				String oper = jsonrule.getString(RULE_OP);
				FieldOperation fo = FieldOperation.valueOf(FieldOperation.class, oper);
				String resultValue = prepareValue(fo, ruleData);
				// JqGridFieldValue jqGridValue = new
				// JqGridFieldValue(ruleField, resultValue);
				// localvalues.add(jqGridValue);
				whereBuilder.append(" ").append(ruleField);
				if (ruleField.contains(".")) {// 用点作为命名参数会出错
					ruleField = ruleField.replaceAll("\\.", "_");
				}
				while (objectQuery.getParamValueMap().containsKey(ruleField)) {// 如果已经重复,比如日期范围查询时
					ruleField = ruleField + "$";
				}
				whereBuilder.append(fo.getOperate()).append(":").append(ruleField);
				objectQuery.addWhere(whereBuilder.toString(), ruleField, resultValue);
				// paramValueMap.put(ruleField, resultValue);
			}
		}
		return objectQuery;
	}

	/**
	 * 
	 * @param oper
	 * @param ruleData
	 * @return String
	 */
	private static String prepareValue(FieldOperation fo, String ruleData) {
		StringBuilder value = new StringBuilder();
		switch (fo) {
		case ew:// %xx
		case en:
			value.append("%").append(ruleData);
			break;
		case cn:// %xx%
		case nc:
			value.append("%").append(ruleData).append("%");
			break;
		case bw:// xx%
		case bn:
			value.append(ruleData).append("%");
			break;
		default:
			value.append(ruleData); // value是list不要加空格
			break;
		}
		return value.toString();
	}
}
