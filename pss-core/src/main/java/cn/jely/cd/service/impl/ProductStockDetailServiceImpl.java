/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockDetailServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-1
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.dao.IProductTypeDao;
import cn.jely.cd.dao.IWarehouseDao;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Region;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryRule;
import cn.jely.cd.vo.RealStockVO;

/**
 * @ClassName:ProductStockDetailServiceImpl Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-1 下午3:51:42
 * 
 */
public class ProductStockDetailServiceImpl extends BaseServiceImpl<ProductStockDetail> implements
		IProductStockDetailService {

	private IProductStockDetailDao productStockDetailDao;
	private IWarehouseDao warehouseDao;
	private IProductTypeDao productTypeDao;

	public void setProductTypeDao(IProductTypeDao productTypeDao) {
		this.productTypeDao = productTypeDao;
	}

	public void setWarehouseDao(IWarehouseDao warehouseDao) {
		this.warehouseDao = warehouseDao;
	}

	public void setProductStockDetailDao(IProductStockDetailDao productStockDetailDao) {
		this.productStockDetailDao = productStockDetailDao;
	}

	@Override
	public List<RealStockVO> findRealStock(List<Warehouse> warehouses) {
		if (warehouses == null || warehouses.size() < 1) {
			warehouses = warehouseDao.getAll();
		}
		return productStockDetailDao.sumRealStock(warehouses);
	}

	@Override
	public Long findRealStockQuantity(Warehouse warehouse, Product product) {
		return productStockDetailDao.sumRealStockQuantity(warehouse, product);
	}

	@Override
	protected IBaseDao<ProductStockDetail> getBaseDao() {
		return productStockDetailDao;
	}

	@Override
	public List<RealStockVO> findRealStock(Serializable warehouseId, Serializable productTypeId, ObjectQuery objectQuery) {
		List<Serializable> typeIds = productTypeDao.findChildIds(productTypeId, true);
//		objectQuery.getQueryGroup().getRules().add(new QueryRule("o.product.productType", FieldOperation.in, types));
//		if (warehouseId != null) {
//			objectQuery.getQueryGroup().getRules().add(new QueryRule("o.warehouse.id", FieldOperation.eq, warehouseId));
//		}
		return productStockDetailDao.sumRealStock(warehouseId,typeIds,objectQuery);
	}

	@Override
	public List<RealStockVO> findRealStock(Warehouse warehouse, ProductType productType) {
		List<ProductType> types = null;
		if (productType != null) {
			types = productTypeDao.findTreeNodes(productType.getId(), true);
		} else {
			types = productTypeDao.findTreeNodes(null, true);
		}
		return productStockDetailDao.sumRealStockQuantity(warehouse, types);
	}

}
