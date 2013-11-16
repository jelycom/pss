/*
 * 捷利商业进销存管理系统
 * @(#)PeriodARAP.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.sys.domain.PeriodARAP;
import cn.jely.cd.sys.service.IPeriodARAPService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:PeriodARAPServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-06-08 15:45:12 
 *
 */
public class PeriodARAPServiceTest extends BaseServiceTest{

	private IPeriodARAPService periodARAPService;
	private IBusinessUnitsService businessUnitsService;
	
	@Resource(name="businessUnitsService")
	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}

	@Resource(name = "periodARAPService")
	public void setPeriodARAPService(IPeriodARAPService periodARAPService) {
		this.periodARAPService = periodARAPService;
	}

	@Test
	public void testSave() {
		List<BusinessUnits> allUnits=businessUnitsService.getAll();
		for (int i = 0; i < allUnits.size(); i++) {
			PeriodARAP periodARAP = new PeriodARAP(allUnits.get(i),200,500); 
			periodARAPService.save(periodARAP);
		}
	}

	@Test
	public void testUpdate() {
		PeriodARAP periodARAP = (PeriodARAP) getRandomObject(periodARAPService.getAll());
		BigDecimal oldvalue=periodARAP.getReceivable();
		periodARAP.setReceivable(oldvalue.add(new BigDecimal(100)));
		periodARAPService.update(periodARAP);
		PeriodARAP periodARAP2 = periodARAPService.getById(periodARAP.getId());
		BigDecimal newvalue=periodARAP2.getReceivable();
		Assert.assertTrue(newvalue.compareTo(oldvalue)>0);
	}

	@Test
	public void testFindPager() {
		
		Pager<PeriodARAP> pager=periodARAPService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}