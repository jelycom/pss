/*
 * 捷利商业进销存管理系统
 * @(#)ProductDeliveryMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductDeliveryDao;
import cn.jely.cd.dao.IProductOrderBillDao;
import cn.jely.cd.dao.IProductOrderBillDeliveryDao;
import cn.jely.cd.dao.IProductPlanDeliveryDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonDetail;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.ProductOrderBillMaster;
import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IProductDeliveryService;
import cn.jely.cd.util.code.DateCoder;
import cn.jely.cd.util.code.ICodeGenerator;
import cn.jely.cd.util.code.impl.MonthGenerator;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductDeliveryMasterServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-06-21 14:54:28
 * 
 */
public class ProductDeliveryServiceImpl extends ProductDeliveryServiceAbstractImpl<ProductDeliveryMaster> implements
		IProductDeliveryService {

	private IProductDeliveryDao productDeliveryDao;
	private IProductPlanDeliveryDao productPlanDeliveryDao;
	private IProductOrderBillDeliveryDao productOrderBillDeliveryDao;

	public void setProductPlanDeliveryDao(IProductPlanDeliveryDao productPlanDeliveryDao) {
		this.productPlanDeliveryDao = productPlanDeliveryDao;
	}

	public void setProductOrderBillDeliveryDao(IProductOrderBillDeliveryDao productOrderBillDeliveryDao) {
		this.productOrderBillDeliveryDao = productOrderBillDeliveryDao;
	}

	public void setProductDeliveryDao(IProductDeliveryDao productDeliveryDao) {
		this.productDeliveryDao = productDeliveryDao;
	}

	@Override
	public IBaseDao<ProductDeliveryMaster> getBaseDao() {
		return productDeliveryDao;
	}

	@Override
	protected Boolean PersistProductStockDetail(ProductDeliveryMaster t) {
		for (ProductCommonDetail detail : t.getDetails()) {
			Product product = productDao.getById(detail.getProduct().getId());
			BigDecimal cost = productStockDetailDao.updateOut(t.getWarehouse(), product,
					detail.getQuantity(), detail.getStockDetailID());
			detail.setCostAmount(cost);
		}
		return true;
	}


//	@Override
//	public List<ProductDeliveryMaster> findUnComplete(BusinessUnits businessUnit) {
//		return productDeliveryDao.findUnComplete(businessUnit);
//	}

 
	@Override
	protected Set<String> updatePlan(Map<Long, List<ProductQuantity>> planMap) {
		return productPlanDeliveryDao.update(planMap);
	}

	@Override
	protected Set<String> updateOrderBill(Map<Long, List<ProductQuantity>> orderMap,BigDecimal paid) {
		return productOrderBillDeliveryDao.update(orderMap,paid);
	}

	@Override
	protected BigDecimal getDbPrepare(ProductDeliveryMaster t) {
		List<ProductCommonDetail> details=t.getDetails();
		Set<ProductOrderBillMaster> productOrderBills = new HashSet<ProductOrderBillMaster>();
		for(ProductCommonDetail detail:details){
			ProductOrderBillMaster orderBillMaster = detail.getOrderBillMaster();
			if(orderBillMaster!=null&&!productOrderBills.contains(orderBillMaster)){
				productOrderBills.add(orderBillMaster);
			}
		}
		return productOrderBillDeliveryDao.calPrepaid(productOrderBills);
	}

	@Override
	protected void updateFundAccount(Serializable id, BigDecimal paid) {
		fundAccountDao.addCurrent(id, paid);
	}

	@Override
	protected void updateBusinessUnit(Serializable id, BigDecimal ARAP, Date lastDate) {
		businessUnitsDao.updateAddReceiveAble(id, ARAP,lastDate);
	}

	@Override
	public List<BigDecimal> findPriceHistory(BusinessUnits unit, Warehouse warehouse, Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDeliveryMaster> findAllUnFinished(ObjectQuery objectQuery) {
		return productDeliveryDao.findAllUnFinished(objectQuery);
	}

	@Override
	public List<ProductCommonMaster> findUnFinishedWithReturn(ObjectQuery objectQuery) {
		return productDeliveryDao.findUnFinishedWithReturn(objectQuery);
	}
	
	// private void updateStock(ProductCommonDetail detail,
	// List<ProductStockDetail> psdetails) {
	// Integer quanlity = detail.getQuanlity();
	// BigDecimal cost = BigDecimal.ZERO;
	// for (ProductStockDetail psDetail : psdetails) {
	// Integer subQuanlity = psDetail.getInquanlity() -
	// psDetail.getOutquanlity();// 这记录的可用数量
	// if (subQuanlity < quanlity) {
	// psDetail.setOutquanlity(psDetail.getInquanlity());
	// cost.add(psDetail.getAmount());
	// psDetail.setAmount(BigDecimal.ZERO);
	// } else if (subQuanlity > quanlity) {
	// psDetail.setOutquanlity(psDetail.getOutquanlity() + quanlity);
	// // 当前的库存金额=原来的库存金额-现在出货的金额
	// BigDecimal costAmount = psDetail.getPrice().multiply(new
	// BigDecimal(quanlity.toString()));// 成本单价*出货数量=出货的金额
	// psDetail.setAmount(psDetail.getAmount().subtract(costAmount));
	// detail.setCostAmount(cost.add(costAmount));
	// } else {
	// // 如果数量相等,则明细中的成本就是剩余的成本.
	// detail.setCostAmount(cost.add(psDetail.getAmount()));
	// psDetail.setOutquanlity(psDetail.getOutquanlity() + quanlity);
	// psDetail.setAmount(BigDecimal.ZERO);
	// }
	// productStockDetailDao.update(psDetail);
	// quanlity = quanlity - subQuanlity;// 减去此单扣减的数量
	// if (quanlity <= 0) {// 如果数量已经为负,则退出循环
	// break;
	// }
	// }
	// }

}
