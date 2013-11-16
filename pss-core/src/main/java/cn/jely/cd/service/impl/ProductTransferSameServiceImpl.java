/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferSameServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductTransferSameDao;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductTransferDetail;
import cn.jely.cd.domain.ProductTransferSameMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IProductTransferSameService;

/**
 * @ClassName:ProductTransferSameServiceImpl
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-17 下午5:23:23
 *
 */
public class ProductTransferSameServiceImpl extends ProductTransferServiceAbstractImpl<ProductTransferSameMaster> implements
		IProductTransferSameService {
	
	private IProductTransferSameDao productTransferSameDao;
	
	public void setProductTransferSameDao(IProductTransferSameDao productTransferSameDao) {
		this.productTransferSameDao = productTransferSameDao;
	}

	@Override
	protected IBaseDao<ProductTransferSameMaster> getBaseDao() {
		return productTransferSameDao;
	}

	@Override
	protected void performChange(ProductTransferSameMaster master) {
		Warehouse outWarehouse = master.getOutWarehouse();
		Warehouse inWarehouse = master.getInWarehouse();
//		Employee outEmployee = master.getOutEmployee();
		for(ProductTransferDetail detail:master.getDetails()){
			Product product = detail.getProduct();
			product=productDao.getById(product.getId());
			BigDecimal cost=productStockDetailDao.updateOut(outWarehouse, product, detail.getQuantity(), detail.getStockDetailID());
			detail.setCostAmount(cost);//同价调拨的成本合计是一样的
			detail.setAmount(cost);
			ProductStockDetail psdetail=new ProductStockDetail(inWarehouse,product,detail.getQuantity(),cost);
			productStockDetailDao.save(psdetail);
		}
	}

}
