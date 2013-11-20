/*
 * 捷利商业进销存管理系统
 * @(#)ProductOrderBillDeliveryServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-5-27
 */
package cn.jely.cd.service.impl;

import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.dao.IProductOrderBillDeliveryDao;
import cn.jely.cd.dao.IProductPlanDao;
import cn.jely.cd.dao.IProductPlanDeliveryDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.ProductOrderBillDeliveryMaster;
import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.service.IProductOrderBillDeliveryService;
import cn.jely.cd.util.query.ObjectQuery;


/**
 * @ClassName:ProductOrderBillDeliveryServiceImpl
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-5-27 下午5:25:33
 *
 */
public class ProductOrderBillDeliveryServiceImpl extends ProductOrderBillServiceAbstractImpl<ProductOrderBillDeliveryMaster> implements
		IProductOrderBillDeliveryService {

	private IProductOrderBillDeliveryDao productOrderBillDeliveryDao;
	private IFundAccountDao fundAccountDao;
	private IProductPlanDeliveryDao productPlanDeliveryDao;
	
	public void setProductOrderBillDeliveryDao(IProductOrderBillDeliveryDao productOrderBillDeliveryDao) {
		this.productOrderBillDeliveryDao = productOrderBillDeliveryDao;
	}

	public void setProductPlanDeliveryDao(IProductPlanDeliveryDao productPlanDeliveryDao) {
		this.productPlanDeliveryDao = productPlanDeliveryDao;
	}

	public void setFundAccountDao(IFundAccountDao fundAccountDao) {
		this.fundAccountDao = fundAccountDao;
	}

	@Override
	public List<ProductOrderBillDeliveryMaster> findAllUnFinished(ObjectQuery objectQuery) {
//		return findByState("ProductOrderBillDeliveryMaster", stateDao.findByCompleteState(false));
		return findAllUnFinished(objectQuery);
	}

	@Override
	public List<ProductOrderBillDeliveryMaster> findAllFinished(ObjectQuery objectQuery) {
		return findAllFinished(objectQuery);
	}

	

//	/**
//	 * 更新相关联的产品计划
//	 * @Title:updatePlanState
//	 * @param master
//	 * @param detail void
//	 */
//	private void updatePlanState(ProductOrderBillDeliveryMaster master) {
//		List<ProductOrderBillDetail> details=master.getDetails();
//		List<ProductPlanDeliveryMaster> assoPlans = master.getProductPlans();
//		for (ProductPlanDeliveryMaster plan : assoPlans) {
//			plan=productPlanDeliveryDao.getById(plan.getId());//取得对应的实体对象
//			boolean complete=true;//默认完成状态,如果有数量小于计划数量,则表示未完成计划.
//			for(ProductOrderBillDetail detail:details){//循环订单明细,找出对应的计划明细并更新明细及计划的状态
//				if (detail.getMemos().trim().equals(plan.getItem())&&(ProductPlanMaster.NEW.equals(plan.getState())||ProductPlanMaster.PROCESS.equals(plan.getState()))) {
//					List<ProductPlanDetail> pdetails = plan.getDetails();
//					for(int i=0;i<pdetails.size();i++){ //循环计划明细
//						ProductPlanDetail pdetail=pdetails.get(i);
//						if(detail.getProduct().equals(pdetail.getProduct())&&!pdetail.isComplete()){//如果订单的产品和计划的产品一致
//							Integer completeQuantity = (pdetail.getCompleteQuantity()==null?0:pdetail.getCompleteQuantity())+detail.getQuantity();//已完成的数量
//							pdetail.setCompleteQuantity(completeQuantity);//设置计划单实际完成的数量
//							if(completeQuantity<pdetail.getPlanQuantity()){
//								if(complete){
//									complete=false;//如果有一个明细没有完成,表示这张计划没有完成
//								}
//							}else{
//								pdetail.setComplete(true);
//								break;
//							}
//						}
//					}
//				}
//			}
//			if(complete){//如果单据已经完成则更新其状态
//				plan.setState(ProductPlanMaster.COMPLETE);
//			}else{
//				plan.setState(ProductPlanMaster.PROCESS);//在执行过程中
//			}
//			productPlanDeliveryDao.update(plan);
//		}
//	}
//	
	/**
	 * 更新主从表
	 */
	@Override
	public void update(ProductOrderBillDeliveryMaster master) {
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
	protected IBaseDao<ProductOrderBillDeliveryMaster> getBaseDao() {
		return productOrderBillDeliveryDao;
	}

	@Override
	protected IProductPlanDao<ProductPlanDeliveryMaster> getProductPlanDao() {
		return productPlanDeliveryDao;
	}


	@Override
	protected void updateARAP(ProductOrderBillDeliveryMaster master) {
		fundAccountDao.addCurrent(master.getFundAccount().getId(), master.getPaid());
		businessUnitsDao.updateAddAdvance(master.getBusinessUnit().getId(), master.getPaid());
	}

	@Override
	protected ProductOrderBillDeliveryMaster getOldDomain(ProductOrderBillDeliveryMaster master) {
		return productOrderBillDeliveryDao.getById(master.getId());
	}

//	@Override
//	protected IProductOrderBillDao<ProductOrderBillDeliveryMaster> getProductOrderBillDao() {
//		return productOrderBillDeliveryDao;
//	}
}
