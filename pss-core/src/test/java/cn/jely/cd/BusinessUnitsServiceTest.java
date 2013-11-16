/*
 * 捷利商业进销存管理系统
 * @(#)BusinessUnitsServiceTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-12
 */
package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Contacts;
import cn.jely.cd.domain.Department;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:BusinessUnitsServiceTest
 * @author 周义礼
 * @version 2013-4-12 上午11:10:24
 * 
 */
public class BusinessUnitsServiceTest extends BaseServiceTest {

	private IBusinessUnitsService businessUnitsService;

	@Resource
	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}

	@Test
	public void testSave() {
		BusinessUnits businessUnits = new BusinessUnits("BU20130000003", "捷利", "成都捷利电脑");
		businessUnitsService.save(businessUnits);
	}
	
	@Test
	public void testSave2(){
		for(int i=0;i<12;i++){
			BusinessUnits businessUnits=new BusinessUnits("businesU"+i,"往来单位全称"+i);
			for(int j=0;j<3;j++){
				Contacts contactor=new Contacts(businessUnits,"U_"+i+"姓名"+j);
//				contactor.setName();
				businessUnits.getContactors().add(contactor);
			}
			businessUnitsService.save(businessUnits);
		}
		List<BusinessUnits> all = businessUnitsService.getAll();
		Assert.assertTrue(all.size()>5);
	}
	
	@Test
	public void testUpdate(){
		BusinessUnits unit=businessUnitsService.getAll().get(0);
		List<Contacts> contactors = unit.getContactors();
		for(Contacts contactor:contactors){
			contactor.setDateDescription("111111111");
		}
		contactors.add(new Contacts(unit, "testadd"));
		businessUnitsService.update(unit);
	}
	
	@Test
	public void testFindPager(){
		ObjectQuery objectQuery = new ObjectQuery();
		Pager<BusinessUnits> pager=businessUnitsService.findPager(objectQuery);
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	
	@Test
	public void testGenItem(){
		String item=businessUnitsService.generateItem();
		System.out.println(item);
	}
}
