/*
 * 捷利商业进销存管理系统
 * @(#)PeriodStock.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.BaseServiceTest;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.sys.domain.PeriodStock;
import cn.jely.cd.sys.service.IPeriodStockService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryGroup;
import cn.jely.cd.util.query.QueryRule;

/**
 * @ClassName:PeriodStockServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-06-08 15:45:12
 * 
 */
public class PeriodStockServiceTest extends BaseServiceTest {

	private IPeriodStockService periodStockService;
	private IProductService productService;
	private IWarehouseService warehouseService;
	private IEmployeeService employeeService;

	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Resource(name = "warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@Resource(name = "productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	@Resource(name = "periodStockService")
	public void setPeriodStockService(IPeriodStockService periodStockService) {
		this.periodStockService = periodStockService;
	}

	@Test
	public void testSave() {
		List<Product> allProducts = productService.getAll();
		List<Warehouse> allHouse = warehouseService.getAll();
		List<Employee> allemps = employeeService.getAll();
		// periodStock.setItem(periodStockService.)
		for (int i = 0; i < allProducts.size(); i++) {
			PeriodStock periodStock = new PeriodStock(); // 如果出错请把字符串长度改短
			periodStock.setWarehouse((Warehouse) getRandomObject(allHouse));
			periodStock.setProduct(allProducts.get(i));
			periodStock.setQuantity(new Random().nextInt(20) + 1);
			periodStock.setAmount(new BigDecimal(new Random().nextInt(400)));
			periodStockService.save(periodStock);
		}
	}

//	@Test
//	public void testUpdate() {
//		PeriodStockMaster periodStock = (PeriodStockMaster) getRandomObject(periodStockService.getAll());
//		List<PeriodStockDetail> details = periodStock.getDetails();
//		BigDecimal oldvalue=periodStock.getAmount();
//		periodStock.setAmount(null);
//		for (PeriodStockDetail detail : details) {
//			detail.setAmount(detail.getAmount().subtract(BigDecimal.ONE));
//			detail.setMemos("U"+detail.getMemos());
//		}
//		periodStockService.update(periodStock);
//		PeriodStock periodStock2 = periodStockService.getById(periodStock.getId());
//		BigDecimal newvalue = periodStock2.getAmount();
//		Assert.assertTrue(!oldvalue.equals(newvalue));
//	}

	@Test
	public void testFindPager() {
		Pager<PeriodStock> pager = periodStockService.findPager(new ObjectQuery());
		System.out.println("pageNo:" + pager.getPageNo());
		System.out.println("pageSize:" + pager.getPageSize());
		System.out.println("datas:" + pager.getRows());
		System.out.println("totalPages:" + pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size() > 0);
	}
	@Test
	public void testFindStockPager() {
//		List<Object[]> pss = periodStockService.findAllStock(new ObjectQuery(), null);
//		for (Object[] objects : pss) {
//			for (Object object : objects) {
//				System.out.println(object);
//			}
//		}
//		Assert.assertNotNull(pss);
//		Assert.assertTrue(pss.size() > 0);
	}
	@Test
	public void testFindAllStock() {
		ObjectQuery objectQuery = new ObjectQuery();
		QueryGroup qg=new QueryGroup();
		objectQuery.setQueryGroup(qg);
		QueryRule queryRule = new QueryRule("productType.lft","gt", "0");
//		queryRule.setRealType(true);
		queryRule.setRootClass(Product.class);
		queryRule.setRootAlia("o");
		objectQuery.getQueryGroup().getRules().add(queryRule);
		objectQuery.setOrderField("quantity");
		objectQuery.setOrderType(ObjectQuery.ORDERDESC);
		List<PeriodStock> pss = periodStockService.findAllStock(objectQuery, null);
		System.out.println("总记录数:" + pss.size());
//		for (PeriodStock periodStock : pss) {
//			System.out.println(periodStock);
//		}
		Assert.assertNotNull(pss);
//		Assert.assertTrue(pss.size() > 0);
	}

}