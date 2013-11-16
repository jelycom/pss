/*
 * 捷利商业进销存管理系统
 * @(#)BusinessType.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.sys.domain.BusinessType;
import cn.jely.cd.sys.service.IBusinessTypeService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:BusinessTypeServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-06-18 13:25:35 
 *
 */
public class BusinessTypeServiceTest extends BaseServiceTest{

	private IBusinessTypeService businessTypeService;

	@Resource(name = "businessTypeService")
	public void setBusinessTypeService(IBusinessTypeService businessTypeService) {
		this.businessTypeService = businessTypeService;
	}

	@Test
	public void testSave() {
		BusinessType businessType = new BusinessType("赠送",BusinessType.BUSINESSUNITTARGET,BusinessType.DEC,BusinessType.NO,BusinessType.NO,BusinessType.NO); //如果出错请把字符串长度改短
		businessTypeService.save(businessType);
		businessType=new BusinessType("受赠",BusinessType.BUSINESSUNITTARGET,BusinessType.ADD, BusinessType.NO,BusinessType.NO,BusinessType.NO);
		businessTypeService.save(businessType);
		businessType=new BusinessType("现款采购",BusinessType.BUSINESSUNITTARGET,BusinessType.ADD, BusinessType.DEC,BusinessType.NO,BusinessType.NO);
		businessTypeService.save(businessType);
		businessType=new BusinessType("现款销售",BusinessType.BUSINESSUNITTARGET,BusinessType.DEC, BusinessType.ADD,BusinessType.NO,BusinessType.NO);
		businessTypeService.save(businessType);
		businessType=new BusinessType("采购",BusinessType.BUSINESSUNITTARGET,BusinessType.ADD, BusinessType.DEC,BusinessType.NO,BusinessType.ADD);
		businessTypeService.save(businessType);
		businessType=new BusinessType("销售",BusinessType.BUSINESSUNITTARGET,BusinessType.DEC, BusinessType.ADD,BusinessType.ADD,BusinessType.NO);
		businessTypeService.save(businessType);
		businessType=new BusinessType("采购付款",BusinessType.BUSINESSUNITTARGET,BusinessType.NO, BusinessType.DEC,BusinessType.NO,BusinessType.DEC);
		businessTypeService.save(businessType);
		businessType=new BusinessType("销售收款",BusinessType.BUSINESSUNITTARGET,BusinessType.NO, BusinessType.ADD,BusinessType.DEC,BusinessType.NO);
		businessTypeService.save(businessType);
	}

	@Test
	public void testUpdate() {
		BusinessType businessType = (BusinessType) getRandomObject(businessTypeService.getAll());
		String oldstr=businessType.getName();
		businessType.setName("u"+oldstr);
		businessTypeService.update(businessType);
		BusinessType businessType2 = businessTypeService.getById(businessType.getId());
		String newstr=businessType2.getName();
		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testFindPager() {
		
		Pager<BusinessType> pager=businessTypeService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	


}