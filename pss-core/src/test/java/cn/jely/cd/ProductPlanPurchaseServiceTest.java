/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductPlanDetail;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IProductPlanPurchaseService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductplanmasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2012-12-05 10:34:59 
 *
 */
public class ProductPlanPurchaseServiceTest extends BaseServiceTest{

	private IProductPlanPurchaseService productPlanPurchaseService;
	private IEmployeeService employeeService;
	private IProductService productService;

	@Resource(name = "productPlanPurchaseService")
	public void setProductPurchasePlanService(IProductPlanPurchaseService productPurchasePlanService) {
		this.productPlanPurchaseService = productPurchasePlanService;
	}
	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@Resource(name = "productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	@Test
	public void testGenerateItem(){
		String item=((ItemGenAble)productPlanPurchaseService).generateItem(new Date());
		System.out.println(item);
	}
	
	@Test
	public void testSave() {
		List<Employee> allemps = employeeService.getAll();
		for(int i=0;i<10;i++){
			Employee planEmployee = (Employee) getRandomObject(allemps);
			Employee executeEmployee =(Employee) getRandomObject(allemps);
			ProductPlanPurchaseMaster master=new ProductPlanPurchaseMaster(planEmployee,executeEmployee,new Date(),DateUtils.addDays(new Date(), 20),State.NEW);
			master.setItem(((ItemGenAble)productPlanPurchaseService).generateItem(new Date()));
			List<ProductPlanDetail> details = new ArrayList<ProductPlanDetail>();
			List<Product> allProducts=productService.getAll();
			for(int j=0;j<3;j++){
				ProductPlanDetail detail=new ProductPlanDetail();
				detail.setProduct(allProducts.get(new Random().nextInt(allProducts.size())));
				detail.setQuantity(new Random().nextInt(20)+1);
				detail.setMemos("MEMOS FOR .........");
				details.add(detail);
			}
			master.getDetails().addAll(details);
			productPlanPurchaseService.save(master);
		}
		
	}
	@Test
	public void testSaveConcurrent() {
		try {
			List<Employee> allemps = employeeService.getAll();
			Employee planEmployee = (Employee) getRandomObject(allemps);
			Employee executeEmployee =(Employee) getRandomObject(allemps);
			ProductPlanPurchaseMaster master=new ProductPlanPurchaseMaster(planEmployee,executeEmployee,new Date(),DateUtils.addDays(new Date(), 20),State.NEW);
			master.setItem(((ItemGenAble)productPlanPurchaseService).generateItem(new Date()));
			List<ProductPlanDetail> details = new ArrayList<ProductPlanDetail>();
			List<Product> allProducts=productService.getAll();
			for(int j=0;j<3;j++){
				ProductPlanDetail detail=new ProductPlanDetail();
				detail.setProduct(allProducts.get(new Random().nextInt(allProducts.size())));
				detail.setQuantity(new Random().nextInt(20)+1);
				detail.setMemos("MEMOS FOR .........");
				details.add(detail);
			}
			master.getDetails().addAll(details);
			ProductPlanPurchaseMaster master2=new ProductPlanPurchaseMaster(planEmployee,executeEmployee,new Date(),DateUtils.addDays(new Date(), 20),State.NEW);
			master2.setItem(((ItemGenAble)productPlanPurchaseService).generateItem(new Date()));
			List<ProductPlanDetail> details2 = new ArrayList<ProductPlanDetail>();
			for(int j=0;j<3;j++){
				ProductPlanDetail detail=new ProductPlanDetail();
				detail.setProduct(allProducts.get(new Random().nextInt(allProducts.size())));
				detail.setQuantity(new Random().nextInt(20)+1);
				detail.setMemos("MEMOS FOR .........");
				details2.add(detail);
			}
			master2.getDetails().addAll(details2);
			productPlanPurchaseService.save(master);
			productPlanPurchaseService.save(master2);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage()!=null);
		}

	}

	@Test
	public void testUpdate() {
		ProductPlanPurchaseMaster master=(ProductPlanPurchaseMaster) getRandomObject(productPlanPurchaseService.getAll());
		Long oldId=master.getId();
		Integer oldSize=master.getDetails().size();
//		productPlanPurchaseService.changeState(master, ProductPlanMaster.DISCARD); 
		master.getDetails().remove(0);
		productPlanPurchaseService.update(master);
		master=productPlanPurchaseService.getById(oldId);
		Assert.assertTrue(oldSize>master.getDetails().size());
	}

	@Test
	public void testUpdateState() {
//		Pager<ProductPlanPurchaseMaster> pager=productPlanPurchaseService.findPager(null);
		ProductPlanPurchaseMaster master=(ProductPlanPurchaseMaster) getRandomObject(productPlanPurchaseService.getAll());
		State oldState = master.getState();
		productPlanPurchaseService.update(master);
		Assert.assertTrue(!oldState.equals(master.getState()));
	}

	@Test
	public void testCountByStates(){
		Long count=productPlanPurchaseService.countAllFinished();
		Assert.assertTrue(count>0);
	}
}