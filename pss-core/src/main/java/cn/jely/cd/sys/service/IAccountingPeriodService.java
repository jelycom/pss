/*
 * 捷利商业进销存管理系统
 * @(#)AccountingPeriod.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service;

import cn.jely.cd.service.IBaseService;
import cn.jely.cd.sys.domain.AccountingPeriod;

/**
 * @ClassName:AccountingPeriodService
 * @Description:Service
 * @version 2013-04-11 17:30:51 
 *
 */
public interface IAccountingPeriodService extends IBaseService<AccountingPeriod> {

	/**期初开帐
	 * @Title:StartInitPeriod
	 * @return Boolean
	 */
	public Boolean saveInitPeriod();
	/**
	 * 反期初开帐
	 * @Title:unStartInitPeriod
	 * @return Boolean
	 */
	public Boolean saveUnStartInitPeriod();
	/**
	 * 期间结帐
	 * @Title:ClosePeriod
	 * @return Boolean
	 */
	public Boolean ClosePeriod();
	/**反期间结帐
	 * @Title:unClosePeriod
	 * @return Boolean
	 */
	public Boolean unClosePeriod();
}
