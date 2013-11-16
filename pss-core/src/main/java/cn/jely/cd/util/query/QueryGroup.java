/*
 * 捷利商业进销存管理系统
 * @(#)QueryNode.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-30
 */
package cn.jely.cd.util.query;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件WHERE部分封装
 * 
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-30 下午3:24:39
 */
public class QueryGroup {
	public static final String AND = "and";
	public static final String OR = "or";
	/** String:op:操作符 */
	private String groupOp;
	/** List<QueryRule>:rules:并列的查询条件 */
	private List<QueryRule> rules = new ArrayList<QueryRule>();
	/** List<QueryGroup>:groups:子条件组 */
	private List<QueryGroup> groups = new ArrayList<QueryGroup>();

	public QueryGroup() {
		super();
	}

	public QueryGroup(List<QueryRule> rules, String op, List<QueryGroup> groups) {
		this.groupOp = op;
		this.rules = rules;
		this.groups = groups;
	}

	public String getGroupOp() {
//		if (null == this.groupOp || "".equals(this.groupOp)) {
//			return AND;
//		}
		return this.groupOp;
	}

	public void setGroupOp(String op) {
		this.groupOp = op;
	}

	public List<QueryRule> getRules() {
		return rules;
	}

	public void setRules(List<QueryRule> rules) {
		this.rules = rules;
	}

	public List<QueryGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<QueryGroup> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "QueryGroup [op=" + groupOp + ", rules=" + rules + ", groups=" + groups + "]";
	}

}
