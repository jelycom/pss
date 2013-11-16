/*
 * 捷利商业进销存管理系统
 * @(#)ObjectQueryTool.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-31
 */
package cn.jely.cd.util.query;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.FieldOperation;

/**
 * 
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-31 下午10:23:38
 */
public class ObjectQueryTool {

	/**
	 * 将jqGrid的条件值转换为真正对应的类型值
	 * 
	 * @param queryRule
	 * @param rootClass
	 *            TODO
	 * @return
	 * @throws IntrospectionException
	 *             QueryRule
	 */
	public static QueryRule toRealQueryRule(QueryRule queryRule, Class<?> rootClass) throws IntrospectionException {
		if (queryRule.getRealOp() != null) {
			return queryRule;
		} else {
			Class<?> ruleClass = queryRule.getRootClass();
			if (ruleClass == null) {
				ruleClass = rootClass;
			}
			Class<?> fieldType = findFieldType(rootClass, queryRule.getField());
			String dataString = queryRule.getData().toString();
			FieldOperation realOP = getRealOperate(queryRule.getOp(), dataString);
			queryRule.setRealOp(realOP);
			dataString = prepareValue(realOP, dataString);
			Object realData = null;
			if (StringUtils.isNotBlank(dataString)) {
				realData = getRealData(fieldType, dataString);
			}
			if (StringUtils.isNotBlank(queryRule.getRootAlia())) {
				queryRule.setField(queryRule.getRootAlia() + "." + queryRule.getField());
			}
			queryRule.setData(realData);
			// QueryRule realRule=new QueryRule(queryRule.getField(),
			// realOP.getOperate(), realData);
			return queryRule;
		}
	}

	/**
	 * 将页面传递的操作符转为真实的条件操作符
	 * 
	 * @param op
	 * @param b
	 * @return String
	 */
	private static FieldOperation getRealOperate(String op, String dataString) {
		FieldOperation operation = null;
		if (StringUtils.isBlank(dataString)) {
			if (FieldOperation.eq.toString().equals(op)) {// 如果="" 那就是is null
				operation = FieldOperation.n;
			} else if (FieldOperation.ne.toString().equals(op)) {// 如果!="" 那就是is not null
				operation = FieldOperation.nn;
			}
		} else {
			operation = FieldOperation.valueOf(FieldOperation.class, op);
		}
		return operation;
	}

	public static Object getRealData(Class<?> realType, Object orgValue) {
		if (orgValue != null) {
			DateConverter dateconverter = new DateConverter();
			dateconverter.setPatterns(new String[] { "yyyy-MM-dd", "yyyy/MM/dd", "MM/dd/yyyy" });
			ConvertUtils.register(dateconverter, java.util.Date.class);
			return ConvertUtils.convert(orgValue, realType);
		} else {
			return null;
		}
	}

	/**
	 * 根据实体类和属性名,得到属性的类型
	 * 
	 * @param entityClass
	 * @param fieldName
	 * @return the last fieldName's type
	 * @throws IntrospectionException
	 */
	private static Class<?> findFieldType(Class<?> entityClass, String fieldName) throws IntrospectionException {
		if (entityClass == null || fieldName == null) {
			throw new RuntimeException("Entity Class and Field can't be null!");
		}
		int pos = fieldName.indexOf(".");// 如果有.号就一个一个查询它的类别
		String firstName = fieldName;
		Class<?> tmpClass = entityClass;
		if (pos > 0) {
			firstName = fieldName.substring(0, pos);
			fieldName = fieldName.substring(pos + 1);
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(entityClass, Object.class);
		PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptor propertyDescriptor = properties[i];
			if (firstName.equals(propertyDescriptor.getName())) {
				tmpClass = (Class<?>) propertyDescriptor.getPropertyType();
				if (pos > 0) {
					tmpClass = findFieldType(tmpClass, fieldName);
				}
				return tmpClass;
			}
		}
		throw new RuntimeException("Get " + fieldName + "'s Type Error! Please make sure the " + entityClass
				+ " has this field");
	}

	/**
	 * 根据条件标识符(指Like查询)来取得参数的样式
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

	public static List<QueryRule> getAllRule(QueryGroup group) {
		if (group == null) {
			return new ArrayList<QueryRule>();
		}
		List<QueryRule> allRule = group.getRules();
		List<QueryGroup> groups = group.getGroups();
		for (QueryGroup queryGroup : groups) {
			allRule.addAll(getAllRule(queryGroup));
		}
		return allRule;
	}

	// public String queryGroup2Where(QueryGroup group) {
	// if (group != null) {
	// StringBuilder whereBuilder = new StringBuilder();
	// int i = 0;
	// List<QueryRule> rules = group.getRules();
	// for (QueryRule queryRule : rules) {
	// if (i > 0) {
	// whereBuilder.append(" " + group.getOp() + " ");
	// }
	// whereBuilder.append(queryRule.getField()).append(queryRule.getOp());
	// if (queryRule.getData() != null) {
	// String key = UUID.randomUUID().toString();
	// whereBuilder.append(":").append(key);
	// paramValueMap.put(key, queryRule.getData());
	// }
	// i++;
	// }
	// List<QueryGroup> subGroups = queryGroup.getGroups();
	// for (QueryGroup queryGroup : subGroups) {
	// if (i > 0) {
	// whereBuilder.append(" " + queryGroup.getOp() + " (");
	// }
	// whereBuilder.append(queryGroup2Where(queryGroup)).append(")");
	// i++;
	// }
	// return whereBuilder.toString();
	// } else {
	// return "";
	// }
	// }

	/**
	 * 将条件组转换成可识别的hql条件组
	 * 
	 * @param queryGroup
	 * @param rootClass TODO
	 * @return QueryGroup
	 * @throws IntrospectionException
	 */
	public static QueryGroup toRealQueryGroup(QueryGroup queryGroup, Class<?> rootClass) throws IntrospectionException {
		if (queryGroup != null) {
			List<QueryRule> rules = queryGroup.getRules();
			for (QueryRule rule : rules) {
				rule = toRealQueryRule(rule, rootClass);
			}
			List<QueryGroup> subGroups = queryGroup.getGroups();
			for (QueryGroup subGroup : subGroups) {
				subGroup = toRealQueryGroup(subGroup, rootClass);
			}
			return queryGroup;
		} else {
			return null;
		}
	}
	
	
	public static class QueryGroupPaser{
		private static final String SUBGROUPS = "groups";
		private static final String RULE_DATA = "data";
		private static final String RULE_OP = "op";
		private static final String RULE_FIELD = "field";
		private static final String GROUP_OPERATION = "groupOp";
		private static final String RULES_KEY = "rules";
		
		public static QueryGroup parseJqgridSingle(String sField, String sOper, String sString) {
			QueryGroup qg = new QueryGroup();
			qg.getRules().add(new QueryRule(sField, sOper, sString));//解析放到 工具类中，因为除了操作符外还有对参数按字段类型的转换
			return qg;
		}

		/**
		 * 根据前台filters的内容修改查询对象
		 * @param jsonString
		 * @return 修改后的查询组对象
		 */
		public static QueryGroup parseJqgridJson(String jsonString) {
			JSONObject jsonObject = null;
			try {
				if (StringUtils.isBlank(jsonString)){
					return null;
				}
				jsonObject = JSONObject.fromObject(jsonString);
			} catch (Exception e) {
				throw new RuntimeException("Parse JSONString error!");
			}
			return parseGroupJson(jsonObject);
		}
		
		/**
		 *	解析由Filters转化成QUERYGROUP后的对象
		 * @param jsonObject
		 * @return QueryGroup
		 */
		private static QueryGroup parseGroupJson(JSONObject jsonObj) {
			QueryGroup qg=new QueryGroup();
			if (jsonObj.containsKey(GROUP_OPERATION)) {
				String groupOp = jsonObj.getString(GROUP_OPERATION);
				qg.setGroupOp(groupOp);
				if (jsonObj.containsKey(RULES_KEY)) {
					JSONArray jsonRules = jsonObj.getJSONArray(RULES_KEY);
					if(jsonRules!=null &&jsonRules.size()>0){
						for (Object object : jsonRules) {
							JSONObject jsonRule=(JSONObject) object;
							qg.getRules().add(parseRule(jsonRule));//解析条件组
						}
					}
				}
				if (jsonObj.containsKey(SUBGROUPS)) {//解析子组
					JSONArray jsonSubGroups = jsonObj.getJSONArray(SUBGROUPS);
					if (jsonSubGroups != null && jsonSubGroups.size() > 0) {
						for (int i = 0; i < jsonSubGroups.size(); i++) {
							qg.getGroups().add(parseGroupJson((JSONObject) jsonSubGroups.get(i)));
						}
					}
				}
			}
			return qg;
		}

		/**
		 * 解析rules部分
		 * @param jsonRule
		 * @return QueryRule
		 */
		private static QueryRule parseRule(JSONObject jsonRule) {
			String ruleData = jsonRule.getString(RULE_DATA);
			String ruleField = jsonRule.getString(RULE_FIELD);
			String oper = jsonRule.getString(RULE_OP);
			return new QueryRule(ruleField, oper, ruleData);
		}
	}
}
