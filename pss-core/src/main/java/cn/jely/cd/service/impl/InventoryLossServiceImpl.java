/*
 * 捷利商业进销存管理系统
 * @(#)InventoryLossMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IInventoryLossDao;
import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.domain.InventoryCommonDetail;
import cn.jely.cd.domain.InventoryLossMaster;
import cn.jely.cd.domain.Product;
import cn.jely.cd.service.IInventoryLossService;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:InventoryLossMasterServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-09-04 10:41:32 
 *
 */
public class InventoryLossServiceImpl extends BillStateServiceImpl<InventoryLossMaster> implements
		IInventoryLossService {

	private IInventoryLossDao inventoryLossDao;
	private IProductDao productDao;
	private IProductStockDetailDao productStockDetailDao;
	
	public void setProductStockDetailDao(IProductStockDetailDao productStockDetailDao) {
		this.productStockDetailDao = productStockDetailDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public void setInventoryLossDao(IInventoryLossDao inventoryLossDao) {
		this.inventoryLossDao = inventoryLossDao;
	}

	@Override
	public IBaseDao<InventoryLossMaster> getBaseDao() {
		return inventoryLossDao;
	}

	@Override
	protected InventoryLossMaster getOldDomain(InventoryLossMaster master) {
		return inventoryLossDao.getById(master.getId());
	}

	@Override
	protected void prepareModel(InventoryLossMaster master) {
		if (master.getWarehouse() == null || master.getWarehouse().getId() == null || master.getWarehouse().getId() == -1L) {
			throw new EmptyException();
		}
		if (master.getEmployee() == null || master.getEmployee().getId() == null) {
			throw new EmptyException();
		}
		master.setAmount(SystemCalUtil.checkValue(master.getAmount()));
		BigDecimal detailTotal = checkDetails(master);// 先检查明细
//		if(master.getAmount().compareTo(BigDecimal.ZERO)!=0||detailTotal.compareTo(BigDecimal.ZERO)!=0){
//			throw new RuntimeException("盘亏不能有金额。");
//		}
		if (master.getState() == null) {
			master.setState(StateManager.getDefaultState());
		}
		
	}
	/**
	 * 检查明细数据
	 * @param master
	 * @return BigDecimal
	 */
	private BigDecimal checkDetails(InventoryLossMaster master) {
		List<InventoryCommonDetail> details = master.getDetails();
		if (details != null) {
			BigDecimal total = BigDecimal.ZERO;
			for (int i = 0; i < details.size(); i++) {
				InventoryCommonDetail detail1 = details.get(i);
				if (detail1.getQuantity() == null) {
					details.remove(i);
				}
//				else if(detail1.getAmount()!=null && detail1.getAmount().compareTo(BigDecimal.ZERO)!=0){//如果金额不等于0
//					throw new AttrConflictException("盘亏只需要数量，不能输入金额");
//				} //不能只输入数量，因为如果是手工指定的成本那就必须选择相应的批次来确定盘亏的金额。
			}
			if (details.size() > 0) {
				int order = 0;
				for (InventoryCommonDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(master);// 维护主从关系
				}
			} else {
				throw new EmptyException("无有效的明细.");
			}
			return total;
		} else {
			throw new EmptyException();
		}
	}
	@Override
	protected void performChange(InventoryLossMaster master) {
		List<InventoryCommonDetail> details = master.getDetails();
		for (InventoryCommonDetail detail : details) {
			Product product = productDao.getById(detail.getProduct().getId());
			BigDecimal cost = productStockDetailDao.updateOut(master.getWarehouse(), product,
					Math.abs(detail.getQuantity()), detail.getStockDetailID());
			detail.setAmount(BigDecimal.ZERO.subtract(cost));// 盘亏的金额为损失金额
		}
	}

}
