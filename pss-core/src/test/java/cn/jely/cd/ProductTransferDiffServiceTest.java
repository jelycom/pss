/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferDiffMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.ProductTransferDetail;
import cn.jely.cd.domain.ProductTransferDiffMaster;
import cn.jely.cd.domain.ProductTransferDiffMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.service.IProductTransferDiffService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.vo.RealStockVO;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:ProductTransferDiffServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-07-17 15:59:28 
 *
 */
public class ProductTransferDiffServiceTest extends BaseServiceTest{

	private IProductTransferDiffService productTransferDiffService;
	private IWarehouseService warehouseService;
	private IProductStockDetailService productStockDetailService;
	private IEmployeeService employeeService;
	
	
	@Resource(name="employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	@Resource(name="productStockDetailService")
	public void setProductStockDetailService(IProductStockDetailService productStockDetailService) {
		this.productStockDetailService = productStockDetailService;
	}
	@Resource(name = "productTransferDiffService")
	public void setProductTransferDiffService(IProductTransferDiffService productTransferDiffService) {
		this.productTransferDiffService = productTransferDiffService;
	}

	@Test
	public void testSave() {
		List<Warehouse> allHouse=warehouseService.getAll();
		List<Employee> allEmployees=employeeService.getAll();
		List<RealStockVO> stocks=null;
		List<Warehouse> subHous=null;
		for(int j=0;j<allHouse.size();j++){
			subHous=allHouse.subList(j, j+1);
			stocks=productStockDetailService.findRealStock(subHous);
			if(stocks.size()>0){
				break;
			}
		}
		Employee outEmployee=(Employee) getRandomObject(allEmployees);
		Employee inEmployee=(Employee) getRandomObject(allEmployees);
		Warehouse inhouse=(Warehouse) getRandomObject(allHouse);
		String item=((ItemGenAble)productTransferDiffService).generateItem(new Date());
		ProductTransferDiffMaster productTransferDiffMaster = new ProductTransferDiffMaster(item,subHous.get(0),outEmployee,inhouse,inEmployee,new Date()); //如果出错请把字符串长度改短
		ProductTransferDetail detail=new ProductTransferDetail();
		detail.setProduct(stocks.get(0).getProduct());
//		(new Random()).nextInt(stocks.get(0).getQuanlity()+1)
		detail.setQuantity(1);
		detail.setAmount(new BigDecimal("400"));
		productTransferDiffMaster.getDetails().add(detail);
		productTransferDiffService.save(productTransferDiffMaster);
	}

	@Test
	public void testUpdate() {
		ProductTransferDiffMaster productTransferDiffMaster = (ProductTransferDiffMaster) getRandomObject(productTransferDiffService.getAll());
		Warehouse oldWareHouse=productTransferDiffMaster.getInWarehouse();
		productTransferDiffMaster.setInWarehouse(oldWareHouse);
		productTransferDiffService.update(productTransferDiffMaster);
		ProductTransferDiffMaster productTransferDiffMaster2 = productTransferDiffService.getById(productTransferDiffMaster.getId());
		Warehouse newWarehouse=productTransferDiffMaster2.getInWarehouse();
		Assert.assertTrue(!oldWareHouse.equals(newWarehouse));
	}

	@Test
	public void testFindPager() {
		
		Pager<ProductTransferDiffMaster> pager=productTransferDiffService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	

	@Test
	public void testDeleteAllProductTransferDiffMaster() {
		List<ProductTransferDiffMaster> productTransferDiffMasters=productTransferDiffService.getAll();
		for(ProductTransferDiffMaster productTransferDiffMaster:productTransferDiffMasters){
			productTransferDiffService.delete(productTransferDiffMaster.getId());
			
		}
		Assert.assertTrue(productTransferDiffService.getAll().size()==0);
	}

}