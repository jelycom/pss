/*
 * 捷利商业进销存管理系统
 * @(#)DayGenerator.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-3-28
 */
package cn.jely.cd.util.code.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import cn.jely.cd.util.code.DateCoder;

/**
 * @ClassName:DayGenerator
 * @Description:按天计算编号
 * @author 周义礼
 * @version 2013-3-28 下午4:28:58
 * 
 */
public class DayGenerator extends AbstractCodeGenerator {

	@Override
	protected boolean checkDate(Date date, Date oldDate) {
		// Calendar oldtime=new GregorianCalendar();
		// oldtime.setTime(oldDate);
		// Calendar newtime=new GregorianCalendar();
		// newtime.setTime(date);
		return (date == null || oldDate == null || !DateUtils.isSameDay(date, oldDate));
	}

}
