/*
 * 捷利商业进销存管理系统
 * @(#)PeriodAbstractDaoImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-17
 */
package cn.jely.cd.sys.dao.impl;

import java.util.Date;
import java.util.List;

import cn.jely.cd.dao.impl.BaseDaoImpl;
import cn.jely.cd.sys.dao.IPeriodAbstratDao;
import cn.jely.cd.sys.domain.AccountingPeriod;

/**
 * @ClassName:PeriodAbstractDaoImpl
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-17 下午2:21:18
 *
 */
public abstract class PeriodAbstractDaoImpl<T> extends BaseDaoImpl<T> implements IPeriodAbstratDao<T> {

	@Override
	public List<T> findAllUnAsso(Date startDate,Date endDate){
		StringBuilder hqlBuilder=new StringBuilder("from ").append(getEntityClass().getName()).append(" o where o.accountingPeriod is null");
		if(startDate!=null&&endDate!=null&&endDate.after(startDate)){
			hqlBuilder.append(" ");//TODO:时间字段
		}
		return getHibernateTemplate().find(hqlBuilder.toString());
	}

	@Override
	public List<T> findAllUnAsso() {
		return findAllUnAsso(null, null);
	}

	@Override
	public List<T> findAllAssoTo(AccountingPeriod accountingPeriod) {
		StringBuilder hqlBuilder=new StringBuilder("from ").append(getEntityClass().getName()).append(" o where o.accountingPeriod = ?");
		return getHibernateTemplate().find(hqlBuilder.toString(), accountingPeriod);
	}
	
	
}
