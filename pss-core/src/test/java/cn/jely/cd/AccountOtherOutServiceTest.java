/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherOutMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.AccountOtherOutMaster;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.service.IAccountOtherOutService;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:AccountOtherOutMasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountOtherOutServiceTest extends BaseServiceTest{

	private IAccountOtherOutService accountOtherOutService;
	private IEmployeeService employeeService;
	private IBusinessUnitsService businessUnitsService;
	private IFundAccountService fundAccountService;
	
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
	@Resource(name = "accountOtherOutService")
	public void setAccountOtherOutService(IAccountOtherOutService accountOtherOutService) {
		this.accountOtherOutService = accountOtherOutService;
	}

	@Test
	public void testGenItem(){
		System.out.println(((ItemGenAble)accountOtherOutService).generateItem(new Date()));
	}
	@Test
	public void testSave() {
		List<Employee> allemps = employeeService.getAll();
		List<BusinessUnits> allUnits= businessUnitsService.getAll();
		List<FundAccount> allfas=fundAccountService.getAll();
		Employee employee=allemps.get(0);
		BusinessUnits unit=allUnits.get(0);
		FundAccount fundAccount=allfas.get(0);
		Date date=new Date(System.currentTimeMillis());
//		List<ProductPlanPurchaseMaster> subPlans=new ArrayList<ProductPlanPurchaseMaster>(getRandomSubList(allPlans));
		BigDecimal paid=new BigDecimal("800.00");
		BigDecimal discount=new BigDecimal("20.1");
		AccountOtherOutMaster master = new AccountOtherOutMaster(); //如果出错请把字符串长度改短
		master.setBillDate(date);
		master.setBusinessUnit(unit);
		master.setEmployee(employee);
		master.setFundAccount(fundAccount);
		master.setDiscount(discount);
		master.setAmount(paid);
		master.setItem(((ItemGenAble)accountOtherOutService).generateItem(master.getBillDate()));
//		List<AccountCommonDetail> acdetails=master.getDetails();
		for (int i = 0; i < 2; i++) {
		}
		accountOtherOutService.save(master);
	}

	@Test
	public void testUpdate() {
		AccountOtherOutMaster accountOtherOutMaster = (AccountOtherOutMaster) getRandomObject(accountOtherOutService.getAll());

		accountOtherOutService.update(accountOtherOutMaster);
		AccountOtherOutMaster accountOtherOutMaster2 = accountOtherOutService.getById(accountOtherOutMaster.getId());

	}

	@Test
	public void testFindPager() {
		
		Pager<AccountOtherOutMaster> pager=accountOtherOutService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}