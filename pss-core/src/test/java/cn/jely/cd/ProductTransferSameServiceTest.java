/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferSameMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.ProductTransferDetail;
import cn.jely.cd.domain.ProductTransferSameMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.service.IProductTransferSameService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.vo.RealStockVO;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassInWarehouse:ProductTransferSameServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-07-17 15:59:28 
 *
 */
public class ProductTransferSameServiceTest extends BaseServiceTest{

	private IProductTransferSameService productTransferSameService;
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

	@Resource(name = "productTransferSameService")
	public void setProductTransferSameService(IProductTransferSameService productTransferSameService) {
		this.productTransferSameService = productTransferSameService;
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
			String item=((ItemGenAble)productTransferSameService).generateItem(new Date());
			ProductTransferSameMaster productTransferSameMaster = new ProductTransferSameMaster(item,subHous.get(0),outEmployee,inhouse,inEmployee,new Date()); //如果出错请把字符串长度改短
			ProductTransferDetail detail=new ProductTransferDetail();
			detail.setProduct(stocks.get(0).getProduct());
			detail.setQuantity((new Random()).nextInt(stocks.get(0).getQuantity()+1));
			productTransferSameMaster.getDetails().add(detail);
			productTransferSameService.save(productTransferSameMaster);
	}

	@Test
	public void testUpdate() {
		ProductTransferSameMaster productTransferSameMaster = (ProductTransferSameMaster) getRandomObject(productTransferSameService.getAll());
		Warehouse oldstr=productTransferSameMaster.getInWarehouse();
		Warehouse tmpWarehouse=null;
		productTransferSameMaster.setInWarehouse(tmpWarehouse);
		productTransferSameService.update(productTransferSameMaster);
		ProductTransferSameMaster productTransferSameMaster2 = productTransferSameService.getById(productTransferSameMaster.getId());
		Warehouse newstr=productTransferSameMaster2.getInWarehouse();
		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testFindPager() {
		
		Pager<ProductTransferSameMaster> pager=productTransferSameService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	

	@Test
	public void testDeleteAllProductTransferSameMaster() {
		List<ProductTransferSameMaster> productTransferSameMasters=productTransferSameService.getAll();
		for(ProductTransferSameMaster productTransferSameMaster:productTransferSameMasters){
			productTransferSameService.delete(productTransferSameMaster.getId());
			
		}
		Assert.assertTrue(productTransferSameService.getAll().size()==0);
	}

}