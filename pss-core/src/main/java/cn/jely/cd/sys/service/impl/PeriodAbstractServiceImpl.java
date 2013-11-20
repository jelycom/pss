/*
 * 捷利商业进销存管理系统
 * @(#)PeriodAbstractServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-20
 */
package cn.jely.cd.sys.service.impl;

import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IAccountingPeriodDao;
import cn.jely.cd.util.exception.PeriodOpendedException;

/**
 * @ClassName:PeriodAbstractServiceImpl
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-20 下午4:59:03
 *
 */
public abstract class PeriodAbstractServiceImpl<T> extends BaseServiceImpl<T> {

	private IAccountingPeriodDao accountingPeriodDao;
	
	
	public void setAccountingPeriodDao(IAccountingPeriodDao accountingPeriodDao) {
		this.accountingPeriodDao = accountingPeriodDao;
	}
	
	@Override
	protected Boolean beforeSaveCheck(T t) {
		if(accountingPeriodDao.findValidPeriod()==null){
			return true;
		}else{
			throw new PeriodOpendedException();
		}
	}

	@Override
	protected Boolean beforeUpdateCheck(T t) {
		if(accountingPeriodDao.findValidPeriod()==null){
			return super.beforeUpdateCheck(t);
		}else{
			throw new PeriodOpendedException();
		}
	}
	
}
