/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockDetailServiceTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-1
 */
package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import org.testng.annotations.Test;

import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.vo.RealStockVO;

/**
 * @ClassName:ProductStockDetailServiceTest
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-1 下午4:10:59
 *
 */
public class ProductStockDetailServiceTest extends BaseServiceTest {

	private IProductStockDetailService productStockDetailService;
	private IWarehouseService warehouseService;
	private IProductService productService;
	
	@Resource(name="productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	@Resource(name="productStockDetailService")
	public void setProductStockDetailService(IProductStockDetailService productStockDetailService) {
		this.productStockDetailService = productStockDetailService;
	}
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@Test
	public void testgetRealStockQuanlity(){
		List<ProductStockDetail> details=productStockDetailService.getAll();
		ProductStockDetail detail=(ProductStockDetail) getRandomObject(details);
		Long result=productStockDetailService.findRealStockQuantity(detail.getWarehouse(), detail.getProduct());
		System.out.println(result);
	}
	
	@Test
	public void testgetRealStock(){
//		List<Product> products=productService.getAll();
		List<RealStockVO> stocks=productStockDetailService.findRealStock(warehouseService.getAll());
		printList(stocks);		
	}
	
	@Test
	public void testgetRealStockByWarehouse(){
		
	}
	
	@Test
	public void testSumRealStockQuantity(){
		Warehouse warehouse =(Warehouse) getRandomObject(warehouseService.getAll());
		ProductType productType = null;
		List<RealStockVO> realStocks = productStockDetailService.findRealStock(warehouse, productType);
		printList(realStocks);
	}
}
