/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferSameServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.domain.ProductTransferDetail;
import cn.jely.cd.domain.ProductTransferMaster;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.IDoWithState;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:ProductTransferSameServiceImpl
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-17 下午5:06:30
 *
 */
public abstract  class ProductTransferServiceAbstractImpl<T extends ProductTransferMaster> extends BillStateServiceImpl<T> implements IDoWithState<T>{

	protected IProductStockDetailDao productStockDetailDao;
	protected IProductDao productDao;
	
	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public void setProductStockDetailDao(IProductStockDetailDao productStockDetailDao) {
		this.productStockDetailDao = productStockDetailDao;
	}

	@Override
	protected void prepareModel(T master) {
		if(master==null){
			throw new EmptyException();
		}
		if(master.getInWarehouse()==null||master.getInWarehouse().getId()==null){
			throw new EmptyException("调入仓");
		}
		if(master.getOutWarehouse()==null||master.getOutWarehouse().getId()==null){
			throw new EmptyException("调出仓");
		}
		if(master.getOutEmployee()==null||master.getOutEmployee().getId()==null){
			throw new EmptyException("调出仓经手人");
		}
		if(master.getInEmployee()==null||master.getInEmployee().getId()==null){
			throw new EmptyException("调入仓经手人");
		}
		if(master.getOutWarehouse().equals(master.getInWarehouse())){
			throw new AttrConflictException("出库仓库不能和入库仓库相同");
		}
		if(master.getState()==null){
			master.setState(StateManager.getDefaultState());
		}
		checkDetails(master);
	}
	
	/**
	 * 检查明细数据
	 * @param master void
	 */
	private BigDecimal checkDetails(T master) {
		List<ProductTransferDetail> details = master.getDetails();
		if(details!=null){
			BigDecimal total = BigDecimal.ZERO;
			for(int i=0;i<details.size();i++){
				ProductTransferDetail detail1=details.get(i);
				if(detail1.getQuantity()==null||detail1.getQuantity()<1){
					details.remove(i);
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (ProductTransferDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(master);//维护主从关系
					detail.setAmount(SystemCalUtil.checkValue(detail.getAmount()));
					total=total.add(detail.getAmount());
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
	protected T getOldDomain(T master) {
		return getById(master.getId());
	}
}
