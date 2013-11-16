/*
 * 捷利商业进销存管理系统
 * @(#)QuerydataServiceTest.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.Querydata;
import cn.jely.cd.service.IQuerydataService;

public class QuerydataServiceTest extends BaseServiceTest {

	private IQuerydataService querydataService;
	
	@Test
	public void testSave(){
		Querydata querydata=new Querydata(null, "测试条件名", "测试条件对应的值", "EmployeeAction");
		querydataService.save(querydata);
	}
	
	@Test
	public void testLoadFilter(){
		List<Querydata> searchDatas=querydataService.loadFilter("EmployeeAction");
		Assert.assertNotNull(searchDatas);
		Assert.assertNotNull(searchDatas.get(0));
		Assert.assertEquals("测试条件名", searchDatas.get(0).getName());
	}
	@Resource(name="querydataService")
	public void setQuerydataService(IQuerydataService querydataService) {
		this.querydataService = querydataService;
	}
	
	
}
