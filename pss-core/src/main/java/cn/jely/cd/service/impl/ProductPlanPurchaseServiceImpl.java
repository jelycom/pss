/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductPlanDao;
import cn.jely.cd.dao.IProductPlanPurchaseDao;
import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.service.IProductPlanPurchaseService;
import cn.jely.cd.util.code.DateCoder;
import cn.jely.cd.util.code.ICodeGenerator;
import cn.jely.cd.util.code.impl.MonthGenerator;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.State;

/**
 * 产品采购计划
 * @ClassName:ProductplanmasterServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2012-12-05 10:34:59
 * 
 */
public class ProductPlanPurchaseServiceImpl extends	ProductPlanServiceAbstractImpl<ProductPlanPurchaseMaster> implements IProductPlanPurchaseService{

	private IProductPlanPurchaseDao productPlanPurchaseDao;

	public void setProductPlanPurchaseDao(
			IProductPlanPurchaseDao productPlanPurchaseDao) {
		this.productPlanPurchaseDao = productPlanPurchaseDao;
	}

	@Override
	public IBaseDao<ProductPlanPurchaseMaster> getBaseDao() {
		return productPlanPurchaseDao;
	}


	@Override
	protected IProductPlanDao<ProductPlanPurchaseMaster> getProductPlanDao() {
		return productPlanPurchaseDao;
	}

	@Override
	protected ProductPlanPurchaseMaster getOldDomain(ProductPlanPurchaseMaster master) {
		return getById(master.getId());
	}

	@Override
	protected void performChange(ProductPlanPurchaseMaster master) {
				
	}

}
