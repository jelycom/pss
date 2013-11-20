/*
 * 捷利商业进销存管理系统
 * @(#)ProductCostAdjustServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-24
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductCostAdjustDao;
import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.domain.ProductCostAdjustDetail;
import cn.jely.cd.domain.ProductCostAdjustMaster;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IProductCostAdjustService;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.StateManager;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-24 上午8:55:35
 */
public class ProductCostAdjustServiceImpl extends BillStateServiceImpl<ProductCostAdjustMaster> implements
		IProductCostAdjustService {

	private IProductCostAdjustDao productCostAdjustDao;
	private IProductStockDetailDao productStockDetailDao;

	public void setProductStockDetailDao(IProductStockDetailDao productStockDetailDao) {
		this.productStockDetailDao = productStockDetailDao;
	}
	public void setProductCostAdjustDao(IProductCostAdjustDao productCostAdjustDao) {
		this.productCostAdjustDao = productCostAdjustDao;
	}
	@Override
	protected ProductCostAdjustMaster getOldDomain(ProductCostAdjustMaster master) {
		return getById(master.getId());
	}

	@Override
	protected void prepareModel(ProductCostAdjustMaster master) {
		if (master == null) {
			throw new EmptyException();
		}
		if(master.getEmployee()==null||master.getEmployee().getId()==null){
			throw new EmptyException("经手人不能为空");
		}
		if(master.getWarehouse()==null||master.getWarehouse().getId()==null){
			throw new EmptyException("仓库不能为空");
		}
		if(master.getBillDate()==null){
			throw new EmptyException("单据日期");
		}
		if (master.getState()==null) {
			master.setState(StateManager.getDefaultState());
		}
		master.setNewAmount(SystemCalUtil.checkValue(master.getNewAmount()));
		master.setOldAmount(SystemCalUtil.checkValue(master.getOldAmount()));
		BigDecimal newAmount=checkDetails(master);
		if(newAmount.compareTo(master.getNewAmount())!=0){
			throw new AttrConflictException("总金额与明细金额不相等!");
		}
	}

	/**
	 * 检查明细数据
	 * @param details void
	 */
	private BigDecimal checkDetails(ProductCostAdjustMaster master) {
		List<ProductCostAdjustDetail> details=master.getDetails();
		if (details != null) {
			BigDecimal total = BigDecimal.ZERO;
			for (int i = 0; i < details.size(); i++) {
				ProductCostAdjustDetail detail1 = details.get(i);
				if (detail1.getQuantity() == null || detail1.getQuantity() < 1) {
					details.remove(i);
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (ProductCostAdjustDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(master);// 维护主从关系
					total = total.add(SystemCalUtil.checkValue(detail.getNewAmount()));
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
	protected void performChange(ProductCostAdjustMaster master) {
		Warehouse warehouse = master.getWarehouse();
		for(ProductCostAdjustDetail detail:master.getDetails()){
			BigDecimal oldAmount=productStockDetailDao.updateOut(warehouse, detail.getProduct(), detail.getQuantity(), detail.getStockDetailID());
			detail.setOldAmount(oldAmount);//成本调整前的成本.
			ProductStockDetail psdetail=new ProductStockDetail(warehouse,detail.getProduct(),detail.getQuantity(),detail.getNewAmount());
			productStockDetailDao.save(psdetail);
		}
	}

	@Override
	protected IBaseDao<ProductCostAdjustMaster> getBaseDao() {
		return productCostAdjustDao;
	}

}
