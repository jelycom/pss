/*
 * 捷利商业进销存管理系统
 * @(#)PeriodAccount.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.sys.dao.IAccountingPeriodDao;
import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.sys.domain.PeriodAccount;
import cn.jely.cd.sys.service.IAccountingPeriodService;
import cn.jely.cd.sys.service.IPeriodAccountService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:PeriodAccountServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-06-07 22:04:21 
 *
 */
public class PeriodAccountServiceTest extends BaseServiceTest{

	private IPeriodAccountService periodAccountService;
	private IAccountingPeriodService accountingPeriodService;
	private IFundAccountService fundAccountService;
	
	@Resource(name = "periodAccountService")
	public void setPeriodAccountService(IPeriodAccountService periodAccountService) {
		this.periodAccountService = periodAccountService;
	}
	@Resource(name="accountingPeriodService")
	public void setAccountingPeriodService(IAccountingPeriodService accountingPeriodService) {
		this.accountingPeriodService = accountingPeriodService;
	}

	@Resource(name="fundAccountService")
	public void setFundAccountService(IFundAccountService fundAccountService) {
		this.fundAccountService = fundAccountService;
	}

	@Test
	public void testSave() {
		List<FundAccount> accounts=fundAccountService.getAll();
		AccountingPeriod initPeriod=accountingPeriodService.findQueryObject(null);
		for(FundAccount account:accounts){
			PeriodAccount periodAccount = new PeriodAccount(account,new BigDecimal(10000)); //如果出错请把字符串长度改短
			periodAccountService.save(periodAccount);
		}
	}


	@Test
	public void testFindPager() {
		Pager<PeriodAccount> pager=periodAccountService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	

}