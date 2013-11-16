package cn.jely.cd.util.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.MyStringUtil;

/**
 * 查询的父类，查询的条件及值由子类提供 父类只提供基本数据及调用的逻辑方法 如果采用addWhere(String condition, List<?
 * extends Object> values)方法设置参数则需将jqGridCondeition设置为false.
 * 
 * @author Administrator
 * @version 1.0 2012-09-10
 */
public class ObjectQuery {
	public static final String ORDERASC = "asc";
	public static final String ORDERDESC = "desc";
	private int pageNo = 1, pageSize = 10;
	private String orderField, orderType;
	private String baseHql;
	private String fullHql;
	private String baseCountHql;
	private String countHql;
	private Class<?> queryClass;
	private String group;
	private String having;
	private QueryGroup queryGroup = new QueryGroup();
	private StringBuilder whereBuilder = new StringBuilder(100);// 初始化类就初始它，后面才好用。
	private StringBuilder orderBuilder = new StringBuilder(100);// 排序条件的生成器
	private List params = new ArrayList();
	private Map<String, Object> paramValueMap = new HashMap<String, Object>();
	private boolean whereFlag;
	private boolean orderFlag;
	private boolean jqGridCondition;

	public ObjectQuery() {
	}

	public ObjectQuery(Class<?> queryClass) {
		this.queryClass = queryClass;
	}

	public ObjectQuery(String fullHql, Map<String, Object> paramValue) {
		this.fullHql = fullHql;
		this.paramValueMap = paramValue;
	}

	public ObjectQuery(String baseHql) {
		this.baseHql = baseHql;
	}

	/**
	 * 非jqGrid的多条件查询 添加单个条件
	 * 
	 * @Title:addWhere
	 */
	public ObjectQuery addWhere(String condition, String key, Object values) {
		this.whereBuilder.append(" ").append(condition);
		if (StringUtils.isNotEmpty(key)) {
			this.paramValueMap.put(key, values);
		}
		return this;
	}

	/**
	 * 多条件查询,将条件放入一条语句中
	 */
	public ObjectQuery addWhere(String condition, Map<String, Object> paramValueMap) {
		this.whereBuilder.append(" ").append(condition).append(" ");
		this.paramValueMap.putAll(paramValueMap);
		return this;
	}

	/**
	 * 多条件查询,条件放入List中,list中的条件是and关系
	 * 
	 * @param conditions
	 * @param paramValueMap
	 * @return ObjectQuery
	 */
	public ObjectQuery addWhere(List<String> conditions, Map<String, Object> paramValueMap) {
		for (String condition : conditions) {
			this.whereBuilder.append(" ").append(condition).append(" ");
		}
		this.paramValueMap.putAll(paramValueMap);
		return this;
	}

	public String getWhere() {
		if (!whereFlag) {
			int i = 0;
			if (whereBuilder.length() > 0) {
				i++;
			}
			if (queryGroup != null && (queryGroup.getGroups().size() > 0 || queryGroup.getRules().size() > 0)) {
				if (StringUtils.isBlank(queryGroup.getGroupOp())) {
					queryGroup.setGroupOp(QueryGroup.AND);
				}
				if (i > 0) {
					whereBuilder.append(" ").append(queryGroup.getGroupOp()).append(" ");
				}
				whereBuilder.append(queryGroup2Where(queryGroup));
			}
			if (whereBuilder.toString().trim().length() > 0) {
				if (!StringUtils.containsIgnoreCase(baseHql, "where") && whereBuilder.indexOf("where") == -1) {
					whereBuilder.insert(0, " where ");
				}
			}
			whereFlag = true;
		}
		System.out.println("getWhere: " + whereBuilder.toString());
		return whereBuilder.toString();
	}

	private StringBuilder queryGroup2Where(QueryGroup qGroup) {
		StringBuilder whereBuilder = new StringBuilder();
		if (qGroup != null) {
			int i = 0;
			List<QueryRule> rules = qGroup.getRules();
			for (QueryRule queryRule : rules) {
				whereBuilder.append(" ");
				if (i > 0) {
					whereBuilder.append(qGroup.getGroupOp());
				}
				FieldOperation realOp = queryRule.getRealOp();
				whereBuilder.append(" ").append(queryRule.getField()).append(" ").append(realOp.getOperate()).append(" ");
				if (queryRule.getData() != null) {
					String key = MyStringUtil.genRandomString(10);
					while (paramValueMap.containsKey(key)) {
						key = MyStringUtil.genRandomString(10);
					}
					if (FieldOperation.in.equals(realOp)||FieldOperation.ni.equals(realOp)) {
						whereBuilder.append("(:").append(key).append(")");
					} else {
						whereBuilder.append(":").append(key);
					}
					this.paramValueMap.put(key, queryRule.getData());
				}
				i++;
			}
			List<QueryGroup> subGroups = qGroup.getGroups();
			for (QueryGroup subGroup : subGroups) {
				if (i > 0) {
					whereBuilder.append(" ").append(qGroup.getGroupOp()).append(" ");
				}
				whereBuilder.append("(").append(queryGroup2Where(subGroup)).append(")");
				i++;
			}
		}
		return whereBuilder;
	}

	public String getOrder() {
		if (StringUtils.isNotBlank(orderField) && !orderFlag) {
			orderBuilder.append(" ORDER BY ");
			String[] orderFields = orderField.split(",");
			String[] orders = null;
			if (StringUtils.isNotBlank(orderType)) {
				orders = orderType.split(",");
			} else {
				orders = new String[orderFields.length];
			}
			for (int i = 0; i < orderFields.length; i++) {
				// if(!StringUtils.containsIgnoreCase(orderFields[i], "o.")){
				// orderBuilder.append("o.");
				// }
				orderBuilder.append(orderFields[i]).append(" ");
				if (orders.length <= i
						&& (!ORDERASC.equalsIgnoreCase(orders[i]) || !ORDERDESC.equalsIgnoreCase(orders[i]))
						|| orders[i] == null) {
					orderBuilder.append(" DESC ");// 默认降序
				} else {
					orderBuilder.append(orders[i]);
				}
				if (i < orderFields.length - 1) {
					orderBuilder.append(",");
				}
			}
			orderFlag = true;
		}
		return orderBuilder.toString();
	}

	public void setBaseHql(String baseHql) {
		this.baseHql = baseHql;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getGroup() {
		if (StringUtils.isNotBlank(group) && !StringUtils.containsIgnoreCase(group, "group by")) {
			if (!StringUtils.containsIgnoreCase(group, ".")) {
				group = "o." + group;
			}// TODO:如果有分组条件但没有指定是哪个则默认为自己。
			return " group by " + group;
		}
		return group == null ? "" : group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getHaving() {
		if (StringUtils.isNotBlank(having) && !StringUtils.containsIgnoreCase(having, "having")) {
			// if(!StringUtils.containsIgnoreCase(having, "o.")){
			// having="o."+having;
			// }
			return " having " + having;
		}
		return having == null ? "" : having;
	}

	public void setHaving(String having) {
		this.having = having;
	}

	public List getParams() {
		return params;
	}

	public void setParams(List params) {
		this.params = params;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public boolean isJqGridCondition() {
		return jqGridCondition;
	}

	public void setJqGridCondition(boolean jqGridCondition) {
		this.jqGridCondition = jqGridCondition;
	}

	// public StringBuilder getWhereBuilder() {
	// return whereBuilder;
	// }
	//
	// public void setWhereBuilder(StringBuilder whereBuilder) {
	// this.whereBuilder = whereBuilder;
	// }
	//
	// public StringBuilder getOrderBuilder() {
	// return orderBuilder;
	// }
	//
	// public void setOrderBuilder(StringBuilder orderBuilder) {
	// this.orderBuilder = orderBuilder;
	// }

	public void setQueryClass(Class<?> queryClass) {
		this.queryClass = queryClass;
	}

	public Class<?> getQueryClass() {
		return queryClass;
	}

	public String getFullHql() {
		if (StringUtils.isBlank(this.fullHql)) {
			if (this.queryClass != null && StringUtils.isBlank(baseHql)) {
				this.baseHql = " FROM " + this.queryClass.getName();
			}
			this.fullHql = this.baseHql + getWhere() + getGroup() + getHaving() + getOrder();
		}
		return fullHql;
	}

	public String getCountHql() {
		if (StringUtils.isBlank(this.countHql)) {
			if (this.queryClass != null && StringUtils.isBlank(baseCountHql)) {
				String entityName = this.queryClass.getSimpleName();
				this.baseCountHql = "SELECT count(" + entityName + ") FROM " + entityName + " " + entityName;
			}
			countHql = baseCountHql + getWhere() + getGroup() + getHaving() + getOrder();
		}
		return countHql;
	}

	public Map<String, Object> getParamValueMap() {
		return paramValueMap;
	}

	public QueryGroup getQueryGroup() {
		if (this.queryGroup == null) {
			this.queryGroup = new QueryGroup();
		}
		return this.queryGroup;
	}

	public void setQueryGroup(QueryGroup queryGroup) {
		this.queryGroup = queryGroup;
	}
}
