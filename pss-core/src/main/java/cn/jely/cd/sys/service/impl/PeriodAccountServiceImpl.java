/*
 * 捷利商业进销存管理系统
 * @(#)PeriodAccount.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IAccountingPeriodDao;
import cn.jely.cd.sys.dao.IPeriodAccountDao;
import cn.jely.cd.sys.domain.PeriodAccount;
import cn.jely.cd.sys.service.IPeriodAccountService;

/**
 * @ClassName:PeriodAccountServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-06-07 22:04:21 
 *
 */
public class PeriodAccountServiceImpl extends PeriodAbstractServiceImpl<PeriodAccount> implements
		IPeriodAccountService {

	private IPeriodAccountDao periodAccountDao;

	public void setPeriodAccountDao(IPeriodAccountDao periodAccountDao) {
		this.periodAccountDao = periodAccountDao;
	}

	@Override
	public IBaseDao<PeriodAccount> getBaseDao() {
		return periodAccountDao;
	}

//	@Override
//	public Long save(PeriodAccount t) {
//		t.setCurrent(t.getBegin());
//		return super.save(t);
//	}
//
//	@Override
//	public void update(PeriodAccount t) {
//		t.setCurrent(t.getBegin());
//		super.update(t);
//	}

}
