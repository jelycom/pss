/*
 * 捷利商业进销存管理系统
 * @(#)IPeriodAbstratDao.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-17
 */
package cn.jely.cd.sys.dao;

import java.util.Date;
import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.sys.domain.AccountingPeriod;

/**
 * 期间操作公共Dao
 * @ClassName:IPeriodAbstratDao
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-17 下午2:23:15
 *
 */
public interface IPeriodAbstratDao<T> extends IBaseDao<T> {
	/**查询所有在指定时间段内未关联的数据
	 * @Title:findAllUnAsso
	 * @param startDate 开始日期
	 * @param endDate  结束日期
	 * @return List<PeriodStock>
	 */
	List<T> findAllUnAsso(Date startDate, Date endDate);
	/**
	 * 查询所有在未关联的数据
	 * @Title:findAllUnAsso
	 * @return List<PeriodStock>
	 */
	List<T> findAllUnAsso();
	
	List<T> findAllAssoTo(AccountingPeriod accountingPeriod);
}
