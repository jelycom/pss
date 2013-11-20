/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockingServiceTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-4
 */
package cn.jely.cd;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Test;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockingDetail;
import cn.jely.cd.domain.ProductStockingMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IProductStockingService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.DateUtils;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-4 下午3:58:48
 */
public class ProductStockingServiceTest  extends BaseServiceTest{

	private IProductStockingService productStockingService;
	private IWarehouseService warehouseService;
	private IEmployeeService employeeService;
	private IProductService productService;
	
	@Resource(name="productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	@Resource(name="employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@Resource(name="productStockingService")
	public void setProductStockingService(IProductStockingService productStockingService) {
		this.productStockingService = productStockingService;
	}
	
	@Test
	public void testSave(){
		List<Employee> allemps=employeeService.getAll();
		List<Warehouse> allwhs = warehouseService.getAll();
		List<Product> allpdts = productService.getAll();
		int size=allpdts.size();
		ProductStockingMaster master=new ProductStockingMaster();
		master.setBillDate(DateUtils.getDayBegin(new Date()));
		master.setEmployee((Employee) getRandomObject(allemps));
		master.setWarehouse((Warehouse) getRandomObject(allwhs));
		List<ProductStockingDetail> details = master.getDetails();
		for(int i=0 ;i<(size/2);i++){
			ProductStockingDetail detail =new ProductStockingDetail();
			detail.setProduct(allpdts.get(i));
			detail.setQuantity(new Random().nextInt(5)+1);
			details.add(detail);
		}
		productStockingService.save(master);
	}
	
	@Test
	public void testUpdate(){
		List<ProductStockingMaster> allps = productStockingService.getAll();
		ProductStockingMaster productStockingMaster = allps.get(0);
		List<ProductStockingDetail> details = productStockingMaster.getDetails();
		for (ProductStockingDetail detail : details) {
			detail.setComplete(true);
		}
		productStockingService.update(productStockingMaster);
		
	}
	@Test
	public void testAudit(){
		productStockingService.audit("1");
	}
	
	@Test
	public void testContinueStocking(){
		ProductStockingMaster master=productStockingService.continueStocking("1");
		System.out.println(master);
	}
	
	@Test
	public void testPerform(){
		
	}
}
