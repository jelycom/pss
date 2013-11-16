/*
 * 捷利商业进销存管理系统
 * @(#)Querydata.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IQuerydataDao;
import cn.jely.cd.domain.Querydata;

/**
 * @ClassName:QuerydataDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2013-03-01 15:09:02 
 *
 */
@Repository("querydataDao")
public class QuerydataDaoImpl extends BaseDaoImpl<Querydata> implements IQuerydataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Querydata> findByActionName(String actionName) {
		String hql="from "+getEntityClass().getName()+" o where o.actionName=:actionName";
		return findByNamedParam(hql, "actionName", actionName);
	}

}
