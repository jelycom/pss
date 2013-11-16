/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherInMaster.java
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

import cn.jely.cd.domain.AccountCommonDetail;
import cn.jely.cd.domain.AccountOtherCommonDetail;
import cn.jely.cd.domain.AccountOtherInMaster;
import cn.jely.cd.domain.AccountOutMaster;
import cn.jely.cd.domain.Bursary;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.Product;
import cn.jely.cd.service.IAccountOtherInService;
import cn.jely.cd.service.IBursaryService;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:AccountOtherInMasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountOtherInServiceTest extends BaseServiceTest{

	private IAccountOtherInService accountOtherInService;
	private IEmployeeService employeeService;
	private IBusinessUnitsService businessUnitsService;
	private IFundAccountService fundAccountService;
	private IWarehouseService warehouseService;
	private IBursaryService bursaryService;
	
	@Resource(name="bursaryService")
	public void setBursaryService(IBursaryService bursaryService) {
		this.bursaryService = bursaryService;
	}

	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Resource(name="businessUnitsService")
	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}
	@Resource(name="fundAccountService")
	public void setFundAccountService(IFundAccountService fundAccountService) {
		this.fundAccountService = fundAccountService;
	}
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	@Resource(name = "accountOtherInService")
	public void setAccountOtherInService(IAccountOtherInService accountOtherInService) {
		this.accountOtherInService = accountOtherInService;
	}

	@Test
	public void testGenItem(){
		System.out.println(((ItemGenAble)accountOtherInService).generateItem(new Date()));
	}
	@Test
	public void testSave() {
		List<Employee> allemps = employeeService.getAll();
		List<BusinessUnits> allUnits= businessUnitsService.getAll();
		List<FundAccount> allfas=fundAccountService.getAll();
		List<Bursary> allbursarys=bursaryService.getAll();
		Employee employee=allemps.get(0);
		BusinessUnits unit=allUnits.get(0);
		FundAccount fundAccount=allfas.get(0);
		Date date=new Date(System.currentTimeMillis());
//		List<ProductPlanPurchaseMaster> subPlans=new ArrayList<ProductPlanPurchaseMaster>(getRandomSubList(allPlans));
		BigDecimal paid=new BigDecimal("800.00");
		BigDecimal discount=new BigDecimal("20.1");
		AccountOtherInMaster master = new AccountOtherInMaster(); //如果出错请把字符串长度改短
		master.setBillDate(date);
		master.setBusinessUnit(unit);
		master.setEmployee(employee);
		master.setFundAccount(fundAccount);
		master.setDiscount(discount);
		master.setItem(((ItemGenAble)accountOtherInService).generateItem(master.getBillDate()));
		List<AccountOtherCommonDetail> acdetails=master.getDetails();
		BigDecimal total=BigDecimal.ZERO;
		for (int i = 0; i < 2; i++) {
			AccountOtherCommonDetail detail=new AccountOtherCommonDetail();
			detail.setBursary((Bursary) getRandomObject(allbursarys));
			BigDecimal pay = new BigDecimal(new Random().nextInt(300)+1);
			detail.setPay(pay);
			acdetails.add(detail);
			total=total.add(pay);
		}
		master.setAmount(total);
		accountOtherInService.save(master);
	}

	@Test
	public void testUpdate() {
		AccountOtherInMaster accountOtherInMaster = (AccountOtherInMaster) getRandomObject(accountOtherInService.getAll());
		accountOtherInService.update(accountOtherInMaster);
		AccountOtherInMaster accountOtherInMaster2 = accountOtherInService.getById(accountOtherInMaster.getId());

	}

	@Test
	public void testFindPager() {
		
		Pager<AccountOtherInMaster> pager=accountOtherInService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}