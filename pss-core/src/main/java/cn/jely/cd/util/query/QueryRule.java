/*
 * 捷利商业进销存管理系统
 * @(#)QueryRule.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-30
 */
package cn.jely.cd.util.query;

import cn.jely.cd.util.FieldOperation;

/**
 * 单个查询条件
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-30 下午4:04:28
 */
public class QueryRule {
	
	private String field;
	private String op;
	private FieldOperation realOp;
	private Object data;
//	/**boolean:realType:是否真实类型，false需要根据实体类进行转换*/
//	private boolean realType;
	/**Class<?>:rootClass:字段对应实体类*/
	private Class<?> rootClass;
	/**String:rootAlia:根类别名*/
	private String rootAlia;
//	/**boolean:andChild:查询时是否加入子，如类别下的子类*/
//	private boolean andChild;
	
	public QueryRule() {
		super();
	}
	public QueryRule(String field, String op, Object data) {
		super();
		this.field = field;
		this.op = op;
		this.data = data;
	}
	
	public QueryRule(String field, FieldOperation op ,Object data){
		this.field = field;
		this.realOp = op;
		this.data = data;
	}

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}

	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
//	
//	public boolean isRealType() {
//		return realType;
//	}
//	public void setRealType(boolean realType) {
//		this.realType = realType;
//	}
//	public boolean isAndChild() {
//		return andChild;
//	}
//	public void setAndChild(boolean andChild) {
//		this.andChild = andChild;
//	}
	
	public Class<?> getRootClass() {
		return rootClass;
	}
	public void setRootClass(Class<?> rootClass) {
		this.rootClass = rootClass;
	}
	
	public String getRootAlia() {
		return rootAlia;
	}
	public void setRootAlia(String rootAlia) {
		this.rootAlia = rootAlia;
	}
	
	public FieldOperation getRealOp() {
		return realOp;
	}
	public void setRealOp(FieldOperation realOp) {
		this.realOp = realOp;
	}
	@Override
	public String toString() {
		return "QueryRule [field=" + field + ", op=" + op + ", data=" + data + "]";
	}
}
