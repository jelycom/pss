/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductPlanDao;
import cn.jely.cd.dao.IProductPlanDeliveryDao;
import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.service.IProductPlanDeliveryService;

/**
 * 产品采购计划
 * @ClassName:ProductplanmasterServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2012-12-05 10:34:59
 * 
 */
@Service("productPlanDeliveryService")
public class ProductPlanDeliveryServiceImpl extends	ProductPlanServiceAbstractImpl<ProductPlanDeliveryMaster> implements IProductPlanDeliveryService{

	private IProductPlanDeliveryDao productPlanDeliveryDao;

	@Resource(name = "productPlanDeliveryDao")
	public void setProductPlanDeliveryDao(IProductPlanDeliveryDao productPlanDeliveryDao) {
		this.productPlanDeliveryDao = productPlanDeliveryDao;
	}


	@Override
	public IBaseDao<ProductPlanDeliveryMaster> getBaseDao() {
		return productPlanDeliveryDao;
	}


	@Override
	protected IProductPlanDao<ProductPlanDeliveryMaster> getProductPlanDao() {
		return productPlanDeliveryDao;
	}

	@Override
	protected ProductPlanDeliveryMaster getOldDomain(ProductPlanDeliveryMaster master) {
		return getById(master.getId());
	}

	@Override
	protected void performChange(ProductPlanDeliveryMaster master) {
		
	}
}
