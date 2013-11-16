/*
 * 捷利商业进销存管理系统
 * @(#)ProductDeliveryReturnServiceImpl.java
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
import cn.jely.cd.dao.IProductDeliveryReturnDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.ProductCommonDetail;
import cn.jely.cd.domain.ProductDeliveryReturnMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.service.IProductDeliveryReturnService;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductDeliveryReturnServiceImpl
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-16 下午1:47:35
 *
 */
public class ProductDeliveryReturnServiceImpl extends ProductDeliveryServiceAbstractImpl<ProductDeliveryReturnMaster> implements
		IProductDeliveryReturnService {

	private IProductDeliveryReturnDao productDeliveryReturnDao;
	
	public void setProductDeliveryReturnDao(IProductDeliveryReturnDao productDeliveryReturnDao) {
		this.productDeliveryReturnDao = productDeliveryReturnDao;
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
	protected BigDecimal getDbPrepare(ProductDeliveryReturnMaster t) {
		return BigDecimal.ZERO;
	}

	@Override
	protected void updateFundAccount(Serializable id, BigDecimal paid) {
		fundAccountDao.subCurrent(id, paid);
	}

	@Override
	protected void updateBusinessUnit(Serializable id, BigDecimal ARAP, Date lastDate) {
		businessUnitsDao.updateSubtractReceiveAble(id, ARAP, lastDate);//减少应收款.
	}

	@Override
	protected Boolean PersistProductStockDetail(ProductDeliveryReturnMaster t) {
		for(ProductCommonDetail detail:t.getDetails()){
			ProductStockDetail psdetail=null;
			if(detail.getStockDetailID()!=null){//TODO:如果有指定批次号,并且出库时一条明细5个对应多条库存明细(1+1+3,3条明细加起来才够5个),这样很有问题
				psdetail=productStockDetailDao.getById(detail.getStockDetailID());
				psdetail.setOutquantity(psdetail.getOutquantity()-detail.getQuantity());//逆操作,将出库数量减去.
				psdetail.setAmount(psdetail.getAmount());
//				productStockDetailDao.update(psdetail);
			}else{
				psdetail=new ProductStockDetail();
				psdetail.setWarehouse(t.getWarehouse());
				psdetail.setProduct(detail.getProduct());
				psdetail.setInquantity(detail.getQuantity());
				psdetail.setOutquantity(0);//出货记录不允许为空,置0
				psdetail.setAmount(detail.getCostAmount());//销售退货的成本应该是明细出货时的成本数据.
				psdetail.setMemos(t.getItem());
				productStockDetailDao.save(psdetail);
			}
		}
		return true;
	}

	@Override
	protected IBaseDao<ProductDeliveryReturnMaster> getBaseDao() {
		return productDeliveryReturnDao;
	}

	@Override
	public List<ProductDeliveryReturnMaster> findAllUnFinished(ObjectQuery objectQuery) {
		return productDeliveryReturnDao.findAllUnFinished(objectQuery);
	}

//	@Override
//	public List<ProductDeliveryReturnMaster> findUnComplete(BusinessUnits businessUnit) {
//		return productDeliveryReturnDao.findUnComplete(businessUnit);
//	}

}
