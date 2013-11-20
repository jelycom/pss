/*
 * 捷利商业进销存管理系统
 * @(#)IProductStockDetailService.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-1
 */
package cn.jely.cd.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.vo.RealStockVO;

/**
 * 库存相关服务
 * @ClassName:IProductStockDetailService
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-1 下午3:47:50
 *
 */
public interface IProductStockDetailService extends IBaseService<ProductStockDetail> {
	/**
	 * 查询指定的库房的实时产品库存数量及金额
	 * @param warehouses
	 * @return List<ProductStockDetail>
	 */
	public List<RealStockVO> findRealStock(List<Warehouse> warehouses);
	
	public List<RealStockVO> findRealStock(Warehouse warehouses,ProductType productType);
	
	/**
	 * 查询指定的库房的实时产品库存数量及金额
	 * @param objectQuery 查询条件
	 * @return List<ProductStockDetail>
	 */
	public List<RealStockVO> findRealStock(Serializable	warehouseId,Serializable productTypeId,ObjectQuery objectQuery);
	/**
	 * 查询指定库房指定产品的库存量
	 * @param warehouse
	 * @param product
	 * @return List<RealStockVO>
	 */
	public Long findRealStockQuantity(Warehouse warehouse,Product product);
	
	/**
	 * 按库房查询产品最近的售价
	 * @param warehouse
	 * @param product
	 * @return List<BigDecimal>
	 */
//	public List<BigDecimal> findPriceHistory(Warehouse warehouse,Product product);
	
//	public List<Warehouse> findWarehouseHave();
	
}
