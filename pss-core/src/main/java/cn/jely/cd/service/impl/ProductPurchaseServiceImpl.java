/*
 * 捷利商业进销存管理系统
 * @(#)ProductPurchaseMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductOrderBillPurchaseDao;
import cn.jely.cd.dao.IProductPlanPurchaseDao;
import cn.jely.cd.dao.IProductPurchaseDao;
import cn.jely.cd.dao.IProductPurchaseReturnDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonDetail;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductOrderBillMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.domain.ProductPurchaseReturnMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IProductPurchaseService;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductPurchaseMasterServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-06-21 14:54:28 
 *
 */
public class ProductPurchaseServiceImpl extends ProductPurchaseServiceAbstractImpl<ProductPurchaseMaster> implements
		IProductPurchaseService {

	private IProductPurchaseDao productPurchaseDao;
	private IProductOrderBillPurchaseDao productOrderBillPurchaseDao;
	private IProductPlanPurchaseDao productPlanPurchaseDao;
	private IProductPurchaseReturnDao productPurchaseReturnDao;

	
	public void setProductPurchaseReturnDao(IProductPurchaseReturnDao productPurchaseReturnDao) {
		this.productPurchaseReturnDao = productPurchaseReturnDao;
	}

	public void setProductOrderBillPurchaseDao(IProductOrderBillPurchaseDao productOrderBillPurchaseDao) {
		this.productOrderBillPurchaseDao = productOrderBillPurchaseDao;
	}

	public void setProductPlanPurchaseDao(IProductPlanPurchaseDao productPlanPurchaseDao) {
		this.productPlanPurchaseDao = productPlanPurchaseDao;
	}

	public void setProductPurchaseDao(IProductPurchaseDao productPurchaseMasterDao) {
		this.productPurchaseDao = productPurchaseMasterDao;
	}

	@Override
	public IBaseDao<ProductPurchaseMaster> getBaseDao() {
		return productPurchaseDao;
	}

	
	@Override
	protected Boolean PersistProductStockDetail(ProductPurchaseMaster t) {
		for(ProductCommonDetail detail:t.getDetails()){
			ProductStockDetail psdetail=new ProductStockDetail();
			psdetail.setWarehouse(t.getWarehouse());
			psdetail.setProduct(detail.getProduct());
			psdetail.setInquantity(detail.getQuantity());
			psdetail.setOutquantity(0);//出货记录不允许为空,置0
			psdetail.setAmount(detail.getAmount());
			psdetail.setMemos(t.getItem());
			psdetail.setProductCommonDetail(detail);//设置进货时相应的单据关系
			productStockDetailDao.save(psdetail);
		}
		return true;
	}

//	@Override
//	public List<ProductPurchaseMaster> findUnComplete(BusinessUnits businessUnit) {
//		return productPurchaseDao.findUnComplete(businessUnit);
//	}

	@Override
	protected Set<String> updatePlan(Map<Long, List<ProductQuantity>> planMap) {
		return productPlanPurchaseDao.update(planMap);
	}

	@Override
	protected Set<String> updateOrderBill(Map<Long, List<ProductQuantity>> orderMap, BigDecimal paid) {
		return productOrderBillPurchaseDao.update(orderMap,paid);
	}

	@Override
	protected BigDecimal getDbPrepare(ProductPurchaseMaster t) {
		List<ProductCommonDetail> details=t.getDetails();
		Set<ProductOrderBillMaster> productOrderBills = new HashSet<ProductOrderBillMaster>();
		for(ProductCommonDetail detail:details){
			ProductOrderBillMaster orderBillMaster = detail.getOrderBillMaster();
			if(orderBillMaster!=null&&!productOrderBills.contains(orderBillMaster)){
				productOrderBills.add(orderBillMaster);
			}
		}
		return productOrderBillPurchaseDao.calPrepaid(productOrderBills);
//		return BigDecimal.ZERO;
	}

	@Override
	protected void updateFundAccount(Serializable id, BigDecimal paid) {
		fundAccountDao.subCurrent(id, paid);
	}

	@Override
	protected void updateBusinessUnit(Serializable id, BigDecimal ARAP, Date lastDate) {
		businessUnitsDao.updateAddPayAble(id,ARAP,lastDate);
	}

	@Override
	public List<BigDecimal> findPriceHistory(BusinessUnits unit, Warehouse warehouse, Product product) {
		
		return null;
	}

	@Override
	public List<ProductPurchaseMaster> findAllUnFinished(ObjectQuery objectQuery) {
		return productPurchaseDao.findAllUnFinished(objectQuery);
	}

	@Override
	public List<ProductCommonMaster> findUnFinishedWithReturn(ObjectQuery objectQuery) {
		return productPurchaseDao.findUnCompleteWithReturn(objectQuery);
	}


}
