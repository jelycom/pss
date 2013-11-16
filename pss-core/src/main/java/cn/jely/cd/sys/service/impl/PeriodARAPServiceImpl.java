/*
 * 捷利商业进销存管理系统
 * @(#)PeriodARAP.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IPeriodARAPDao;
import cn.jely.cd.sys.domain.PeriodARAP;
import cn.jely.cd.sys.service.IPeriodARAPService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:PeriodARAPServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-06-08 15:45:12 
 *
 */
public class PeriodARAPServiceImpl extends PeriodAbstractServiceImpl<PeriodARAP> implements
		IPeriodARAPService {

	private IPeriodARAPDao periodARAPDao;

	public void setPeriodARAPDao(IPeriodARAPDao periodARAPDao) {
		this.periodARAPDao = periodARAPDao;
	}

	@Override
	public IBaseDao<PeriodARAP> getBaseDao() {
		return periodARAPDao;
	}


}
