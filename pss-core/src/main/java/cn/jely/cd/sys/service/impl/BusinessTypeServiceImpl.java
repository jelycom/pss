/*
 * 捷利商业进销存管理系统
 * @(#)BusinessType.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IBusinessTypeDao;
import cn.jely.cd.sys.domain.BusinessType;
import cn.jely.cd.sys.service.IBusinessTypeService;

/**
 * @ClassName:BusinessTypeServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-06-18 13:25:35 
 *
 */
public class BusinessTypeServiceImpl extends BaseServiceImpl<BusinessType> implements
		IBusinessTypeService {

	private IBusinessTypeDao businessTypeDao;

	public void setBusinessTypeDao(IBusinessTypeDao businessTypeDao) {
		this.businessTypeDao = businessTypeDao;
	}

	@Override
	public IBaseDao<BusinessType> getBaseDao() {
		return businessTypeDao;
	}



}
