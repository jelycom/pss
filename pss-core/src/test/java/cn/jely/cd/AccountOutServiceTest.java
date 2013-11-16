/*
 * 捷利商业进销存管理系统
 * @(#)AccountOutMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.AccountCommonDetail;
import cn.jely.cd.domain.AccountOutMaster;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IAccountOutService;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.service.IProductOrderBillPurchaseService;
import cn.jely.cd.service.IProductPlanPurchaseService;
import cn.jely.cd.service.IProductPurchaseService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:AccountOutServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountOutServiceTest extends BaseServiceTest{

	private IAccountOutService accountOutService;
	private IProductPurchaseService productPurchaseService;
	private IProductOrderBillPurchaseService productOrderBillPurchaseService;
	private IEmployeeService employeeService;
	private IProductService productService;
	private IBusinessUnitsService businessUnitsService;
	private IFundAccountService fundAccountService;
	private IProductPlanPurchaseService productPlanPurchaseService;
	private IWarehouseService warehouseService;
	private IStateResourceOPService stateResourceOPService;
	private IActionResourceService actionResourceService;
	
	
	@Resource(name = "actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}
	@Resource(name = "productOrderBillPurchaseService")
	public void setProductOrderBillPurchaseService(IProductOrderBillPurchaseService productOrderBillPurchaseService) {
		this.productOrderBillPurchaseService = productOrderBillPurchaseService;
	}
	@Resource(name="stateResourceOPService")
	public void setStateResourceOPService(IStateResourceOPService stateResourceOPService) {
		this.stateResourceOPService = stateResourceOPService;
	}
	
	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@Resource(name = "productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	@Resource(name="businessUnitsService")
	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}
	@Resource(name="fundAccountService")
	public void setFundAccountService(IFundAccountService fundAccountService) {
		this.fundAccountService = fundAccountService;
	}
	@Resource(name="productPlanPurchaseService")
	public void setProductPlanPurchaseService(IProductPlanPurchaseService productPlanPurchaseService) {
		this.productPlanPurchaseService = productPlanPurchaseService;
	}
	
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	@Resource(name = "productPurchaseService")
	public void setProductPurchaseService(IProductPurchaseService productPurchaseMasterService) {
		this.productPurchaseService = productPurchaseMasterService;
	}
	@Resource(name = "accountOutService")
	public void setAccountOutService(IAccountOutService accountOutService) {
		this.accountOutService = accountOutService;
	}

	@Test
	public void testSave() {
		List<Employee> allemps = employeeService.getAll();
		List<BusinessUnits> allUnits= businessUnitsService.getAll();
		List<FundAccount> allfas=fundAccountService.getAll();
		List<Product> allProducts=productService.getAll();
		Employee employee=allemps.get(0);
		BusinessUnits unit=allUnits.get(0);
		FundAccount fundAccount=allfas.get(0);
		Date date=new Date(System.currentTimeMillis());
//		List<ProductPlanPurchaseMaster> subPlans=new ArrayList<ProductPlanPurchaseMaster>(getRandomSubList(allPlans));
		BigDecimal paid=new BigDecimal("800.00");
		BigDecimal discount=new BigDecimal("20.1");
		AccountOutMaster master = new AccountOutMaster(unit,employee,fundAccount,date,paid,discount); //如果出错请把字符串长度改短
		master.setItem(((ItemGenAble)accountOutService).generateItem(date));
		List<AccountCommonDetail> acdetails=master.getDetails();
		productPurchaseService.getAll();
		for (int i = 0; i < 2; i++) {
			
//			AccountOutMaster accountOutMaster2 = new AccountOutMaster();
//			accountOutMaster2.setId(1l);
//			accountOutMaster.setParent(accountOutMaster2);
			accountOutService.save(master);
		}
	}

	@Test
	public void testUpdate() {
		AccountOutMaster accountOutMaster = (AccountOutMaster) getRandomObject(accountOutService.getAll());
		accountOutService.update(accountOutMaster);
		AccountOutMaster accountOutMaster2 = accountOutService.getById(accountOutMaster.getId());
	}

	@Test
	public void testFindPager() {
		
		Pager<AccountOutMaster> pager=accountOutService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	
}