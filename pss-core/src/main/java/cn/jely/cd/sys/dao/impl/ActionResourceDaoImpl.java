/*
 * 捷利商业进销存管理系统
 * @(#)ActionResource.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jely.cd.dao.impl.BaseDaoImpl;
import cn.jely.cd.dao.impl.NestedTreeDaoImpl;
import cn.jely.cd.sys.dao.IActionResourceDao;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryGroup;
import cn.jely.cd.util.query.QueryRule;

/**
 * @ClassName:ActionResourceDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2013-04-04 22:57:05
 * 
 */

public class ActionResourceDaoImpl extends NestedTreeDaoImpl<ActionResource> implements IActionResourceDao {

	@Override
	public boolean checkExist(ActionResource actionResource) {
		String hql = "select count(o) from " + entityClass.getName()
				+ " o where o.name = :name and o.linkAddress=:linkAddress";// 原来为or,导致空linkAddres会被查出来
		Map<String, Object> paramValueMap = new HashMap<String, Object>();
		paramValueMap.put("name", actionResource.getName());
		paramValueMap.put("linkAddress", actionResource.getLinkAddress());
		Long countByHql = countByHql(hql, paramValueMap);
		return countByHql > 0L;
	}

	@Override
	public ActionResource findByLinkAddress(String linkAddress) {
		String hql = "select o from " + entityClass.getName() + " o where o.linkAddress=:linkAddress";
		List<ActionResource> actionResources = findByNamedParam(hql, "linkAddress", linkAddress);
		if (actionResources != null && actionResources.size() > 0) {
			return actionResources.get(0);
		}
		return null;
	}

	@Override
	public List<ActionResource> findActionResource(Boolean init, Boolean community) {
		ObjectQuery objectQuery = new ObjectQuery();
		QueryGroup queryGroup = objectQuery.getQueryGroup();
		queryGroup.setGroupOp(QueryGroup.OR);
		if ( null != init && true == init ) {
			queryGroup.getRules().add(new QueryRule("init is not null and init", FieldOperation.eq, init));
		} else { //因为是OR条件，所以不能够省略这一个，因为省略后就只按community来查询了 //TODO:对资源的查询不关心的部分能否优化？
			queryGroup.getRules().add(new QueryRule("init",FieldOperation.eq,true));
			queryGroup.getRules().add(new QueryRule("init",FieldOperation.eq,false));
		}
		if (community != null) {
			queryGroup.getRules().add(new QueryRule("community is not null and community", FieldOperation.eq, community));
		}
		return findAll(objectQuery);
	}

}
