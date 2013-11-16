/*
 * 捷利商业进销存管理系统
 * @(#)Productmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBusinessUnitsDao;
import cn.jely.cd.dao.IProductPlanDao;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.ProductOrderBillDetail;
import cn.jely.cd.domain.ProductOrderBillMaster;
import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.service.IProductOrderBillService;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.code.impl.CodeGeneratorUtil;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.IDoWithState;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:ProductmasterServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-01-08 15:45:02
 * 
 */

public abstract class ProductOrderBillServiceAbstractImpl<T extends ProductOrderBillMaster> extends BillStateServiceImpl<T>
		implements IProductOrderBillService<T>,IDoWithState<T> {

	protected IBusinessUnitsDao businessUnitsDao;
//	protected abstract IProductOrderBillDao<T> getProductOrderBillDao();
	protected abstract IProductPlanDao<? extends ProductPlanMaster> getProductPlanDao();
	/**
	 * 更新预收预付,注意:不需要更新最近出货及进货日,因为这不是进/出货单
	 * @param master void
	 */
	protected abstract void updateARAP(T master);
	
	
	public void setBusinessUnitsDao(IBusinessUnitsDao businessUnitsDao) {
		this.businessUnitsDao = businessUnitsDao;
	}
	
	private BigDecimal checkDetails(T master) {
		List<ProductOrderBillDetail> details = master.getDetails();
		if(details!=null){
			BigDecimal total = BigDecimal.ZERO;
			for(int i=0;i<details.size();i++){
				ProductOrderBillDetail detail1=details.get(i);
				if(detail1.getQuantity()==null||detail1.getQuantity()<1){
					details.remove(i);
				}else{
					detail1.setAmount(SystemCalUtil.checkValue(detail1.getAmount()));
					detail1.setPrice(SystemCalUtil.checkValue(detail1.getPrice()));
					detail1.setTax(SystemCalUtil.checkValue(detail1.getTax()));
					detail1.setTaxPrice(SystemCalUtil.checkValue(detail1.getTaxPrice()));
					detail1.setSubTotal(SystemCalUtil.checkValue(detail1.getSubTotal()));
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (ProductOrderBillDetail detail : details) {
					detail.setOrders(order++);
					detail.setOrderBillMaster(master);//维护主从关系
					if(null!=detail.getPlanMaster()&&null == detail.getPlanMaster().getId()){
						detail.setPlanMaster(null);
					}
					BigDecimal checkAmount=SystemCalUtil.add(detail.getSubTotal(), detail.getTax());
					if(SystemCalUtil.checkValue(detail.getAmount()).compareTo(checkAmount)!=0){
						throw new AttrConflictException("明细的单项合计不等于小计+税金");
					}
					total=SystemCalUtil.add(total,detail.getAmount());
				}
				return total;
			}else {
				throw new EmptyException("无有效的明细.");
			}
		}else{
			throw new EmptyException();
		}
	}
	
	@Override
	protected void prepareModel(T master) {
		BigDecimal detailTotal=checkDetails(master);
		FundAccount fundAccount = master.getFundAccount();
		if(fundAccount!=null&&(fundAccount.getId()==-1||fundAccount.getId()==null)){
			master.setFundAccount(null);
		}
		if(master.getInvoiceType()!=null&&(master.getInvoiceType().getId()==null||master.getInvoiceType().getId()==-1L)){
			master.setInvoiceType(null);
		}
		if (master.getContactor() != null && (master.getContactor().getId() == null)) {
			master.setContactor(null);
		}
		if(master.getWarehouse()!=null&&(master.getWarehouse().getId()==null||master.getWarehouse().getId()==-1L)){
			master.setWarehouse(null);
		}
		if(master.getBusinessUnit()!=null&&(master.getBusinessUnit().getId()==null||master.getBusinessUnit().getId()==-1L)){
			throw new EmptyException();
		}
		if(master.getEmployee()!=null&&(master.getEmployee().getId()==null||master.getEmployee().getId()==-1L)){
			throw new EmptyException();
		}
		master.setAmount(SystemCalUtil.checkValue(master.getAmount()));
		master.setPaid(SystemCalUtil.checkValue(master.getPaid()));
		master.setSubTotal(SystemCalUtil.checkValue(master.getSubTotal()));
		master.setValueAddTax(SystemCalUtil.checkValue(master.getValueAddTax()));
		master.setPaidArap(SystemCalUtil.checkValue(master.getPaidArap()));
		master.setArap(master.getPaid());
		if(detailTotal.compareTo(master.getAmount())!=0){
			throw new RuntimeException("明细值合计与合计值不相等");
		}
		if(master.getState()==null){
			master.setState(StateManager.getDefaultState());
		}
	}
	
	@Override
	protected void performChange(T master) {
		updatePlanState(master);
		if(master.getFundAccount()!=null&&SystemCalUtil.checkValue(master.getArap()).compareTo(BigDecimal.ZERO)>0){
			updateARAP(master);
		}
	}

	/**
	 * 更新相关联的产品计划
	 * @Title:updatePlanState
	 * @param master
	 * @param detail void
	 */
	private void updatePlanState(T master) {
		List<ProductOrderBillDetail> details=master.getDetails();
		Map<Long, List<ProductQuantity>> planMap = new HashMap<Long, List<ProductQuantity>>();
		for(ProductOrderBillDetail detail:details){
			ProductPlanMaster planMaster = detail.getPlanMaster();
			if(planMaster!=null&&planMaster.getId()!=null){
				List<ProductQuantity> pqs=planMap.get(planMaster.getId());
				if(pqs!=null){
					pqs.add(new ProductQuantity(detail.getProduct(), detail.getQuantity()));
				}else{
					pqs=new ArrayList<ProductQuantity>();
					pqs.add(new ProductQuantity(detail.getProduct(), detail.getQuantity()));
					planMap.put(planMaster.getId(), pqs);
				}
			}
		}
		Set<String> planItems = getProductPlanDao().update(planMap);
		StringBuffer memos=new StringBuffer(master.getMemos());
		if(planItems!=null){
			memos.append(planItems.toString());
		}
		master.setMemos(memos.toString());
//		if(assoPlans!=null&&assoPlans.size()>0){
//			for (ProductPlanMaster plan : assoPlans) {
//				plan= getProductPlanDao().getById(plan.getId());//TODO:取得对应的实体对象是否可取消,并将此功能放入plan中进行操作
//				boolean complete=true;//默认完成状态,如果有数量小于计划数量,则表示未完成计划.
//				for(ProductOrderBillDetail detail:details){//循环订单明细,找出对应的计划明细并更新明细及计划的状态
//					if (detail.getMemos().trim().equals(plan.getItem())&&(cn.jely.cd.util.state.State.NEW.equals(plan.getState())||cn.jely.cd.util.state.State.PROCESS.equals(plan.getState()))) {
//						List<ProductPlanDetail> pdetails = plan.getDetails();
//						for(int i=0;i<pdetails.size();i++){ //循环计划明细
//							ProductPlanDetail pdetail=pdetails.get(i);
//							if(detail.getProduct().equals(pdetail.getProduct())&&!pdetail.isComplete()){//如果订单的产品和计划的产品一致
//								Integer completeQuantity = (pdetail.getCompleteQuantity()==null?0:pdetail.getCompleteQuantity())+detail.getQuantity();//已完成的数量
//								pdetail.setCompleteQuantity(completeQuantity);//设置计划单实际完成的数量
//								if(completeQuantity<pdetail.getQuantity()){
//									if(complete){
//										complete=false;//如果有一个明细没有完成,表示这张计划没有完成
//									}
//								}else{
//									pdetail.setComplete(true);
//									break;
//								}
//							}
//						}
//					}
//				}
//				if(complete){//如果单据已经完成则更新其状态
//					plan.setState(cn.jely.cd.util.state.State.COMPLETE);
//				}else{
//					plan.setState(cn.jely.cd.util.state.State.PROCESS);//在执行过程中
//				}
//				getProductPlanDao().update(plan);
//			}
//		}
	}

//	@Override
//	public T changeState(T master, State newState) {
//		if(StateManager.canChangeTo(master,newState)){//如果是未完成的状态
//			master=getProductOrderBillDao().getById(master.getId());//只更新状态,避免更新其它数据.
//			master.setState(newState);
//			update(master);
//			doWithState(master);
//			return master;
//		}
//		return null;
//	}
}
