/*
 * 捷利商业进销存管理系统
 * @(#)YearCodeGenerator.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-3-25
 */
package cn.jely.cd.util.code.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cn.jely.cd.util.code.DateCoder;

/**
 * 编号以年为周期生成器
 * 
 * @ClassName:YearCodeGenerator
 * @author 周义礼
 * @version 2013-3-25 下午5:43:02
 * 
 */
public class YearGenerator extends AbstractCodeGenerator {


	@Override
	protected boolean checkDate(Date date, Date oldDate) {
		Calendar oldtime = new GregorianCalendar();
		oldtime.setTime(oldDate);
		Calendar newtime = new GregorianCalendar();
		newtime.setTime(date);
		if (oldtime.get(Calendar.YEAR) == newtime.get(Calendar.YEAR)) {
			return false;
		}
		return true;
	}

}
