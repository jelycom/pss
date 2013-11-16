/*
 * 捷利商业进销存管理系统
 * @(#)AbstractCodeGeneratorTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-19
 */
package cn.jely.cd.util.code;

import java.util.Date;

import org.junit.Test;

import cn.jely.cd.util.code.impl.AbstractCodeGenerator;
import cn.jely.cd.util.code.impl.DayGenerator;
import cn.jely.cd.util.code.impl.MonthGenerator;
import cn.jely.cd.util.code.impl.NoDateGenerator;
import cn.jely.cd.util.code.impl.YearGenerator;

/**
 * @ClassName:AbstractCodeGeneratorTest
 * @author 周义礼
 * @version 2013-4-19 下午3:02:36
 *
 */
public class AbstractCodeGeneratorTest {

	@Test
	public void testDayGenerator1(){
		AbstractCodeGenerator dayGenerator=new DayGenerator();
		String result=dayGenerator.Generate(new DateCoder("{CP}[yyyy-mm-dd](00000)","CP2013-04-1900001",new Date()));
		System.out.println(result);
	}
	@Test
	public void testDayGenerator2(){
		AbstractCodeGenerator dayGenerator=new DayGenerator();
		String result=dayGenerator.Generate(new DateCoder("{CP}[yyyy-mm-dd](00000)","CP2013-04-1300001",new Date()));
		System.out.println(result);
	}
	@Test
	public void testMonthGenerator1(){
		AbstractCodeGenerator dayGenerator=new MonthGenerator();
		String result=dayGenerator.Generate(new DateCoder("{CP}[yyyy-mm-dd](00000)","CP2013-03-1200001",new Date()));
		System.out.println(result);
	}
	@Test
	public void testMonthGenerator2(){
		AbstractCodeGenerator dayGenerator=new MonthGenerator();
		String result=dayGenerator.Generate(new DateCoder("{CP}[yyyy-mm-dd](00000)","CP2013-04-1200001",new Date()));
		System.out.println(result);
	}
	@Test
	public void testYearGenerator(){
		AbstractCodeGenerator dayGenerator=new YearGenerator();
		String result=dayGenerator.Generate(new DateCoder("{CP}[yyyy-mm-dd](00000)","CP2012-02-1200001",new Date()));
		System.out.println(result);
	}
	@Test
	public void testYearGenerator2(){
		AbstractCodeGenerator dayGenerator=new YearGenerator();
		String result=dayGenerator.Generate(new DateCoder("{CP}[yyyy-mm-dd](00000)","CP2013-02-1200001",new Date()));
		System.out.println(result);
	}
	@Test
	public void testNoDateGenerator(){
		AbstractCodeGenerator dayGenerator=new NoDateGenerator();
		String result=dayGenerator.Generate(new DateCoder("(00)","99",new Date()));
		System.out.println(result);
	}
}
