/*
 * 捷利商业进销存管理系统
 * @(#)AccountingPeriod.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.dao;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.sys.domain.AccountingPeriod;

/**
 * @ClassName:AccountingPeriodAction
 * @Description:Dao
 * @author
 * @version 2013-04-11 17:30:51 
 *
 */
public interface IAccountingPeriodDao extends IBaseDao<AccountingPeriod> {

	/**
	 * 查询并返回当前有效的期间,如果没有返回null,有效多于一个返回
	 * @Title:findValidPeriod
	 * @return AccountingPeriod
	 */
	AccountingPeriod findValidPeriod();

}
