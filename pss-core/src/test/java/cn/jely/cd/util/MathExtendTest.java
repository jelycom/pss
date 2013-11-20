/*
 * 捷利商业进销存管理系统
 * @(#)MathExtendTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-3
 */
package cn.jely.cd.util;

import org.junit.Test;

import cn.jely.cd.util.math.MathExtend;
import cn.jely.cd.util.math.SystemCalUtil;

/**
 * @ClassName:MathExtendTest
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-3 上午10:41:16
 *
 */
public class MathExtendTest {
	
	@Test
	public void testadd(){
		String result=MathExtend.add("20.3568", ".36587");
		double d1=4.00001d;
		double d2=2.1;
		System.out.println(result);
		System.out.println(d1+d2);
		System.out.println(MathExtend.add(d1, d2));
	}
	@Test
	public void testmutiply(){
		String result=MathExtend.multiply("3.00002", "8.9", 4);
		
		System.out.println(result);
	}
	
	@Test
	public void testdividePrice(){
		System.out.println(SystemCalUtil.dividePrice("100", "7.1"));
	}
}
