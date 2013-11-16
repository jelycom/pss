/*
 * 捷利商业进销存管理系统
 * @(#)DateUtil.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-27
 */
package cn.jely.cd.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-27 下午2:05:21
 */
public class DateUtils {

	/**
	 * 取得月初日期
	 * @param date
	 * void
	 */
	public static Date getMonthBegin(Date date) {
		Calendar calendar = setBeginOfDay(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	public static Date getDayBegin(Date date){
		Calendar calendar = setEndOfDay(date);
		return calendar.getTime();
	}
	
	public static Date getNextMonthBegin(Date date) {
		Calendar calendar = setBeginOfDay(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}

	public static Date getMonthEnd(Date date) {
		Calendar calendar = setEndOfDay(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	private static Calendar setBeginOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	private static Calendar setEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar;
	}
}
