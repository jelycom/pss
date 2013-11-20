/*
 * 捷利商业进销存管理系统
 * @(#)ProductOrderBillPurchaseServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-5-27
 */
package cn.jely.cd.service.impl;

import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.dao.IProductOrderBillPurchaseDao;
import cn.jely.cd.dao.IProductPlanDao;
import cn.jely.cd.dao.IProductPlanPurchaseDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.ProductOrderBillPurchaseMaster;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.service.IProductOrderBillPurchaseService;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductOrderBillPurchaseServiceImpl Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-5-27 下午4:26:27
 * 
 */
public class ProductOrderBillPurchaseServiceImpl extends
		ProductOrderBillServiceAbstractImpl<ProductOrderBillPurchaseMaster> implements IProductOrderBillPurchaseService {
	private IProductOrderBillPurchaseDao productOrderBillPurchaseDao;
	private IFundAccountDao fundAccountDao;
	private IProductPlanPurchaseDao productPlanPurchaseDao;

	
	public void setFundAccountDao(IFundAccountDao fundAccountDao) {
		this.fundAccountDao = fundAccountDao;
	}

	public void setProductPlanPurchaseDao(IProductPlanPurchaseDao productPlanPurchaseDao) {
		this.productPlanPurchaseDao = productPlanPurchaseDao;
	}

	public void setProductOrderBillPurchaseDao(IProductOrderBillPurchaseDao productOrderBillPurchaseDao) {
		this.productOrderBillPurchaseDao = productOrderBillPurchaseDao;
	}

	@Override
	public List<ProductOrderBillPurchaseMaster> findAllUnFinished(ObjectQuery objectQuery) {
		return productOrderBillPurchaseDao.findAllUnFinished(objectQuery);
	}

	@Override
	public List<ProductOrderBillPurchaseMaster> findAllFinished(ObjectQuery objectQuery) {
		return productOrderBillPurchaseDao.findAllFinished(objectQuery);
	}

//	/**
//	 * 增加主从表
//	 * 
//	 * @return
//	 */
//	@Override
//	public Long save(ProductOrderBillPurchaseMaster master) {
//		if (master != null) {
//			master.setArap(master.getPaid());//订单的付款属于应收款
//			List<ProductOrderBillDetail> details = master.getDetails();
//			if (details != null && details.size() > 0) {
//				int orders = 1;
//				for (ProductOrderBillDetail detail : details) {
//					detail.setOrderBillMaster(master);
//					detail.setOrders(orders++);
//				}
//				updatePlanState(master);
//				return super.save(master);
//			}else {
//				throw new DetailEmptyException();
//			}
//		}
//		return null;
//	}

	
	/**
	 * 更新主从表
	 */
	@Override
	public void update(ProductOrderBillPurchaseMaster master) {
		if(true){
			throw new RuntimeException("更新暂不支持");
		}
//		if (master != null && master.getDetails() != null) {
//			// 必须保证从表中的数据多于1条
//			if (master.getDetails().size() > 0) {
//				super.update(master);
//			} else {
//				throw new DetailEmptyException();
//			}
//		}
	}

	@Override
	protected IBaseDao<ProductOrderBillPurchaseMaster> getBaseDao() {
		return productOrderBillPurchaseDao;
	}


	@Override
	protected IProductPlanDao<ProductPlanPurchaseMaster> getProductPlanDao() {
		return productPlanPurchaseDao;
	}

	@Override
	protected void updateARAP(ProductOrderBillPurchaseMaster master) {
		fundAccountDao.subCurrent(master.getFundAccount().getId(), master.getPaid());
		businessUnitsDao.updateAddPrepaid(master.getBusinessUnit().getId(), master.getPaid());
	}

	@Override
	protected ProductOrderBillPurchaseMaster getOldDomain(ProductOrderBillPurchaseMaster master) {
		return productOrderBillPurchaseDao.getById(master.getId());
	}

//	@Override
//	protected IProductOrderBillDao<ProductOrderBillPurchaseMaster> getProductOrderBillDao() {
//		return productOrderBillPurchaseDao;
//	}
}
