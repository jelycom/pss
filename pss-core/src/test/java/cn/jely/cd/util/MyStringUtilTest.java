/*
 * 捷利商业进销存管理系统
 * @(#)MyStringUtilTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-3
 */
package cn.jely.cd.util;

import org.junit.Test;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-3 上午11:07:33
 */
public class MyStringUtilTest {

	@Test
	public void genRandomStringTest(){
		System.out.println(MyStringUtil.genRandomString(10));
	}
}
