/*
 * 捷利商业进销存管理系统
 * @(#)CompanyInfo.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.sys.domain.CompanyInfo;
import cn.jely.cd.sys.service.ICompanyInfoService;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:CompanyInfoServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-04-15 11:23:38 
 *
 */
public class CompanyInfoServiceTest extends BaseServiceTest{

	private ICompanyInfoService companyInfoService;

	@Resource(name = "companyInfoService")
	public void setCompanyInfoService(ICompanyInfoService companyInfoService) {
		this.companyInfoService = companyInfoService;
	}

	@Test
	public void testSave() {
		CompanyInfo companyInfo = new CompanyInfo("Companfo"); //如果出错请把字符串长度改短
//		companyInfo.setDefaultServiceCostMethod(CostMethod.ASSI);
//		companyInfo.setDefaultProductionCostMethod(CostMethod.FIFO);
		companyInfoService.save(companyInfo);
	}

	@Test
	public void testUpdate() {
		CompanyInfo companyInfo = companyInfoService.getById(1L);
		String oldstr=companyInfo.getCompanyName();
		companyInfo.setCompanyName("CompanyInfo");
		companyInfoService.update(companyInfo);
		CompanyInfo companyInfo2 = companyInfoService.getById(1L);
		String newstr=companyInfo2.getCompanyName();
		Assert.assertTrue(!oldstr.equals(newstr));
	}


	@Test
	public void testGetById() {
		CompanyInfo companyInfo=companyInfoService.getById(1l);
		Assert.assertNotNull(companyInfo);
	}

	@Test
	public void testGetAll() {
		List<CompanyInfo> companyInfos=companyInfoService.getAll();
		Assert.assertTrue(companyInfos.size()>0);
	}

	@Test
	public void testFindPager() {
		
		Pager<CompanyInfo> pager=companyInfoService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}