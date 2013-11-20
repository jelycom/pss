/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferDiffServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductTransferDiffDao;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductTransferDetail;
import cn.jely.cd.domain.ProductTransferDiffMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IProductTransferDiffService;

/**
 * @ClassName:ProductTransferDiffServiceImpl
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-17 下午7:22:39
 *
 */
public class ProductTransferDiffServiceImpl extends ProductTransferServiceAbstractImpl<ProductTransferDiffMaster> implements IProductTransferDiffService {

	private IProductTransferDiffDao productTransferDiffDao;
	
	public void setProductTransferDiffDao(IProductTransferDiffDao productTransferDiffDao) {
		this.productTransferDiffDao = productTransferDiffDao;
	}

	@Override
	protected void performChange(ProductTransferDiffMaster master) {
		Warehouse outWarehouse = master.getOutWarehouse();
		Warehouse inWarehouse = master.getInWarehouse();
		for(ProductTransferDetail detail:master.getDetails()){
			Product product = detail.getProduct();
			product=productDao.getById(product.getId());
			BigDecimal cost=productStockDetailDao.updateOut(outWarehouse, product, detail.getQuantity(), detail.getStockDetailID());
			detail.setCostAmount(cost);//异价调拨的成本合计不是一样的.相当于调高了成本价格.而出的仓库可看作是盈利.
			ProductStockDetail psdetail=new ProductStockDetail(inWarehouse,product,detail.getQuantity(),detail.getAmount());
			productStockDetailDao.save(psdetail);
		}
	}

	@Override
	protected IBaseDao<ProductTransferDiffMaster> getBaseDao() {
		return productTransferDiffDao;
	}

}
