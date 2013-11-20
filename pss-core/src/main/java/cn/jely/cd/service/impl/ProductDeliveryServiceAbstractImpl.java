/*
 * 捷利商业进销存管理系统
 * @(#)ProductDeliveryServiceAbstractImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-25
 */
package cn.jely.cd.service.impl;

import java.util.List;

import cn.jely.cd.dao.IProductCommonDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.ProductDeliveryReturnMaster;
import cn.jely.cd.util.query.ObjectQuery;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-25 下午5:48:36
 */
public abstract class ProductDeliveryServiceAbstractImpl<T extends ProductCommonMaster> extends ProductCommonServiceAbstractImpl<T> {

//	@Override
//	public List<ProductCommonMaster> findAllUnFinishedPD(ObjectQuery objectQuery) {
//		return ((IProductCommonDao)getBaseDao()).findUnComplete(objectQuery);
//	}
}
