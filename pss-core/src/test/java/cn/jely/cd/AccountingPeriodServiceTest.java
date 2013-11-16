/*
 * 捷利商业进销存管理系统
 * @(#)AccountingPeriod.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.sys.service.IAccountingPeriodService;
import cn.jely.cd.util.DateUtils;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:AccountingPeriodServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-04-11 17:30:51 
 *
 */
public class AccountingPeriodServiceTest extends BaseServiceTest{

	private IAccountingPeriodService accountingPeriodService;

	@Resource(name = "accountingPeriodService")
	public void setAccountingPeriodService(IAccountingPeriodService accountingPeriodService) {
		this.accountingPeriodService = accountingPeriodService;
	}

	@Test
	public void testSave() {
		Calendar calendar=Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH);
		for (int i = month; i < 12; i++) {
			AccountingPeriod accountingPeriod = new AccountingPeriod("2013-"+(i+1),DateUtils.getMonthBegin(calendar.getTime()),DateUtils.getMonthEnd(calendar.getTime())); //如果出错请把字符串长度改短
//			AccountingPeriod accountingPeriod2 = new AccountingPeriod();
//			accountingPeriod2.setId(1l);
//			accountingPeriod.setParent(accountingPeriod2);
			accountingPeriodService.save(accountingPeriod);
			calendar.add(Calendar.MONTH, 1);
		}
	}

	@Test
	public void testUpdate() {
		AccountingPeriod accountingPeriod = accountingPeriodService.getById(1L);
		String oldstr=accountingPeriod.getMonth();
		accountingPeriod.setMonth("aa");
		accountingPeriodService.update(accountingPeriod);
		AccountingPeriod accountingPeriod2 = accountingPeriodService.getById(1L);
		String newstr=accountingPeriod2.getMonth();
		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testDelete() {
		accountingPeriodService.delete(2l);
		AccountingPeriod accountingPeriod=accountingPeriodService.getById(2l);
		Assert.assertNull(accountingPeriod);
	}

	@Test
	public void testGetById() {
		AccountingPeriod accountingPeriod=accountingPeriodService.getById(1l);
		Assert.assertNotNull(accountingPeriod);
	}

	@Test
	public void testGetAll() {
		List<AccountingPeriod> accountingPeriods=accountingPeriodService.getAll();
		Assert.assertTrue(accountingPeriods.size()>0);
	}

	@Test
	public void testFindPager() {
		Pager<AccountingPeriod> pager=accountingPeriodService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

	@Test
	public void testInitPeriod(){
		accountingPeriodService.saveInitPeriod();
	}
	
	@Test
	public void testUnInitPeriod(){
		accountingPeriodService.saveUnStartInitPeriod();
	}
}