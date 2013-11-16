/*
 * 捷利商业进销存管理系统
 * @(#)ProductPurchaseReturnServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-16
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductPurchaseReturnDao;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonDetail;
import cn.jely.cd.domain.ProductPurchaseReturnMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.service.IProductPurchaseReturnService;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * 进货退货服务层
 * @ClassName:ProductPurchaseReturnServiceImpl
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-16 下午1:50:09
 *
 */
public class ProductPurchaseReturnServiceImpl extends ProductPurchaseServiceAbstractImpl<ProductPurchaseReturnMaster> implements
		IProductPurchaseReturnService {

	private IProductPurchaseReturnDao productPurchaseReturnDao;
	
	public void setProductPurchaseReturnDao(IProductPurchaseReturnDao productPurchaseReturnDao) {
		this.productPurchaseReturnDao = productPurchaseReturnDao;
	}


	@Override
	protected Set<String> updatePlan(Map<Long, List<ProductQuantity>> planMap) {
		return null;
	}

	@Override
	protected Set<String> updateOrderBill(Map<Long, List<ProductQuantity>> orderMap, BigDecimal paid) {
		return null;
	}

	@Override
	protected BigDecimal getDbPrepare(ProductPurchaseReturnMaster t) {
		return BigDecimal.ZERO;
	}

	@Override
	protected void updateFundAccount(Serializable id, BigDecimal paid) {
		fundAccountDao.addCurrent(id, paid);
	}

	@Override
	protected void updateBusinessUnit(Serializable id, BigDecimal ARAP, Date lastDate) {
		businessUnitsDao.updateSubtractPayAble(id, ARAP, lastDate);
	}

	@Override
	protected Boolean PersistProductStockDetail(ProductPurchaseReturnMaster t) {
		for (ProductCommonDetail detail : t.getDetails()) {
			Product product = productDao.getById(detail.getProduct().getId());
			BigDecimal cost = productStockDetailDao.updateOut(t.getWarehouse(), product,
					detail.getQuantity(), detail.getStockDetailID());
			detail.setCostAmount(cost);
		}
		return true;
	}

	@Override
	protected IBaseDao<ProductPurchaseReturnMaster> getBaseDao() {
		return productPurchaseReturnDao;
	}

	@Override
	public List<ProductPurchaseReturnMaster> findAllUnFinished(ObjectQuery objectQuery) {
		return productPurchaseReturnDao.findAllUnFinished(objectQuery);
	}



//	@Override
//	public List<ProductPurchaseReturnMaster> findUnComplete(BusinessUnits businessUnit) {
//		return productPurchaseReturnDao.findUnComplete(businessUnit);
//	}



}
