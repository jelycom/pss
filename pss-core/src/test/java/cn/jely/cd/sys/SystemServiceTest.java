/*
 * 捷利商业进销存管理系统
 * @(#)SystemServiceTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-1
 */
package cn.jely.cd.sys;

import javax.annotation.Resource;

import org.testng.annotations.Test;

import cn.jely.cd.BaseServiceTest;
import cn.jely.cd.sys.service.ISystemService;

/**
 * @ClassName:SystemServiceTest
 * @author 周义礼
 * @version 2013-4-1 下午5:08:25
 *
 */
public class SystemServiceTest extends BaseServiceTest{

	private ISystemService systemService;
	@Resource
	public void setSystemService(ISystemService systemService) {
		this.systemService = systemService;
	}

	@Test
	public void testInit() {
		systemService.saveinitData();
	}

	@Test
	public void testRepair() {
		systemService.updaterepair();
	}

}
