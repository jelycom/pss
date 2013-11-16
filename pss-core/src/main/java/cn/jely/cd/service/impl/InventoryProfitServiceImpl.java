/*
 * 捷利商业进销存管理系统
 * @(#)InventoryProfitMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IInventoryProfitDao;
import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.domain.InventoryCommonDetail;
import cn.jely.cd.domain.InventoryProfitMaster;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.service.IInventoryProfitService;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:InventoryProfitMasterServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-09-04 10:41:32
 * 
 */
public class InventoryProfitServiceImpl extends BillStateServiceImpl<InventoryProfitMaster> implements
		IInventoryProfitService {

	private IInventoryProfitDao inventoryProfitDao;
	private IProductDao productDao;
	private IProductStockDetailDao productStockDetailDao;

	public void setProductStockDetailDao(IProductStockDetailDao productStockDetailDao) {
		this.productStockDetailDao = productStockDetailDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public void setInventoryProfitDao(IInventoryProfitDao inventoryProfitDao) {
		this.inventoryProfitDao = inventoryProfitDao;
	}

	@Override
	public IBaseDao<InventoryProfitMaster> getBaseDao() {
		return inventoryProfitDao;
	}

	@Override
	protected InventoryProfitMaster getOldDomain(InventoryProfitMaster master) {
		return inventoryProfitDao.getById(master.getId());
	}

	@Override
	protected void prepareModel(InventoryProfitMaster master) {
		if (master.getWarehouse() == null || master.getWarehouse().getId() == null
				|| master.getWarehouse().getId() == -1L) {
			throw new EmptyException();
		}
		if (master.getEmployee() == null || master.getEmployee().getId() == null) {
			throw new EmptyException();
		}
		master.setAmount(SystemCalUtil.checkValue(master.getAmount()));
		BigDecimal detailTotal = checkDetails(master);// 先检查明细
		if (master.getAmount() != null) {
			if (detailTotal.compareTo(master.getAmount()) != 0) {
				throw new RuntimeException("明细值合计与合计值不相等");
			}
		} else {
			master.setAmount(detailTotal);
		}
		if (master.getState() == null) {
			master.setState(StateManager.getDefaultState());
		}
	}

	/**
	 * 检查明细数据
	 * 
	 * @param master
	 * @return BigDecimal
	 */
	private BigDecimal checkDetails(InventoryProfitMaster master) {
		List<InventoryCommonDetail> details = master.getDetails();
		if (details != null) {
			BigDecimal total = BigDecimal.ZERO;
			for (int i = 0; i < details.size(); i++) {
				InventoryCommonDetail detail1 = details.get(i);
				if (detail1.getQuantity() == null) {
					details.remove(i);
				} else if (detail1.getAmount().compareTo(BigDecimal.ZERO) <= 0) {// 如果金额小于等于0
					throw new AttrConflictException("金额不能为0，请输入金额或单价");
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (InventoryCommonDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(master);// 维护主从关系
					total =total.add(detail.getAmount());
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
	protected void performChange(InventoryProfitMaster master) {
		List<InventoryCommonDetail> details = master.getDetails();
		for (InventoryCommonDetail detail : details) {
			ProductStockDetail psdetail = new ProductStockDetail();
			psdetail.setWarehouse(master.getWarehouse());
			psdetail.setProduct(detail.getProduct());
			psdetail.setInquantity(detail.getQuantity());
			psdetail.setOutquantity(0);// 出货记录不允许为空,置0
			psdetail.setAmount(detail.getAmount());
			psdetail.setMemos(master.getItem());
			productStockDetailDao.save(psdetail);
		}
	}

}
