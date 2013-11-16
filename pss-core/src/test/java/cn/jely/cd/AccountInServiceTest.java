/*
 * 捷利商业进销存管理系统
 * @(#)AccountInMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.AccountInMaster;
import cn.jely.cd.service.IAccountInService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:AccountInMasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountInServiceTest extends BaseServiceTest{

	private IAccountInService accountInService;

	@Resource(name = "accountInService")
	public void setAccountInService(IAccountInService accountInService) {
		this.accountInService = accountInService;
	}

	@Test
	public void testGenItem(){
		System.out.println(((ItemGenAble)accountInService).generateItem(new Date()));
	}
	@Test
	public void testSave() {
		for (int i = 0; i < 10; i++) {
			AccountInMaster accountInMaster = new AccountInMaster(); //如果出错请把字符串长度改短
//			AccountInMaster accountInMaster2 = new AccountInMaster();
//			accountInMaster2.setId(1l);
//			accountInMaster.setParent(accountInMaster2);
			accountInService.save(accountInMaster);
		}
	}

	@Test
	public void testUpdate() {
		AccountInMaster accountInMaster = (AccountInMaster) getRandomObject(accountInService.getAll());

		accountInService.update(accountInMaster);
		AccountInMaster accountInMaster2 = accountInService.getById(accountInMaster.getId());

	}

	@Test
	public void testFindPager() {
		
		Pager<AccountInMaster> pager=accountInService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}