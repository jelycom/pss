/*
 * 捷利商业进销存管理系统
 * @(#)DateUtilsTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-27
 */
package cn.jely.cd.util;

import java.util.Date;

import org.junit.Test;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-27 下午2:28:24
 */
public class DateUtilsTest {

	@Test
	public void testGetMonthBegin(){
		System.out.println(DateUtils.getMonthBegin(new Date()));
	}
	
	@Test
	public void testGetMonthEnd(){
		System.out.println(DateUtils.getMonthEnd(new Date()));
	}
	
	@Test
	public void testGetNextMonthBegin(){
		System.out.println(DateUtils.getNextMonthBegin(new Date()));
	}
	
}
