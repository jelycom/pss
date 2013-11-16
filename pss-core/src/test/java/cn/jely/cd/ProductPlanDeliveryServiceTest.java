/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd;

import java.util.ArrayList;
import java.util.Calendar;
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
import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IProductPlanDeliveryService;
import cn.jely.cd.service.IProductPlanService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductplanmasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2012-12-05 10:34:59 
 *
 */
public class ProductPlanDeliveryServiceTest extends BaseServiceTest{

	private IProductPlanDeliveryService productPlanDeliveryService;
	private IEmployeeService employeeService;
	private IProductService productService;

	@Resource(name = "productPlanDeliveryService")
	public void setProductDeliveryPlanService(IProductPlanDeliveryService productDeliveryPlanService) {
		this.productPlanDeliveryService = productDeliveryPlanService;
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
		String item=((ItemGenAble)productPlanDeliveryService).generateItem(new Date());
		System.out.println(item);
	}
	
	@Test
	public void testSave() {
		List<Employee> allemps = employeeService.getAll();
		for(int i=0;i<10;i++){
			Employee planEmployee = (Employee) getRandomObject(allemps);
			Employee executeEmployee =(Employee) getRandomObject(allemps);
			ProductPlanDeliveryMaster master=new ProductPlanDeliveryMaster(planEmployee,executeEmployee,new Date(),DateUtils.addDays(new Date(), 20),State.NEW);
			master.setItem(((ItemGenAble)productPlanDeliveryService).generateItem(new Date()));
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
			productPlanDeliveryService.save(master);
		}
		
	}

	@Test
	public void testUpdate() {
		ProductPlanDeliveryMaster master=(ProductPlanDeliveryMaster) getRandomObject(productPlanDeliveryService.getAll());
		Long oldId=master.getId();
		Integer oldSize=master.getDetails().size();
//		productPlanDeliveryService.changeState(master, ProductPlanMaster.DISCARD); 
		master.getDetails().remove(0);
		productPlanDeliveryService.update(master);
		master=productPlanDeliveryService.getById(oldId);
		Assert.assertTrue(oldSize>master.getDetails().size());
	}

	@Test
	public void testUpdateState() {
//		Pager<ProductPlanDeliveryMaster> pager=productPlanDeliveryService.findPager(null);
		ProductPlanDeliveryMaster master=(ProductPlanDeliveryMaster) getRandomObject(productPlanDeliveryService.getAll());
		State oldState = master.getState();
//		if(ProductPlanDeliveryMaster.NEW.equals(oldState)){
//			master.setState(ProductPlanDeliveryMaster.PROCESS);
//		}else
//		if(ProductPlanDeliveryMaster.PROCESS.equals(oldState)){
//			master.setState(ProductPlanDeliveryMaster.COMPLETE);
//		}else
//		if(ProductPlanDeliveryMaster.COMPLETE.equals(oldState)){
//			master.setState(ProductPlanDeliveryMaster.DISCARD);
//		}else
//		if(ProductPlanDeliveryMaster.DISCARD.equals(oldState)){
//			master.setState(ProductPlanDeliveryMaster.SUSPEND);
//		}else
//		if(ProductPlanDeliveryMaster.SUSPEND.equals(oldState)){
//			master.setState(ProductPlanDeliveryMaster.NEW);
//		}
		productPlanDeliveryService.update(master);
		Assert.assertTrue(!oldState.equals(master.getState()));
	}

	@Test
	public void testCountByStates(){
		Long count=productPlanDeliveryService.countAllFinished();
		Assert.assertTrue(count>0);
	}
}