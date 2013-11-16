/*
 * 捷利商业进销存管理系统
 * @(#)DateCoder.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-3
 */
package cn.jely.cd.util.code;

import java.util.Date;

/**
 * 对编号进行生成时传入的参数封装
 * @ClassName:DateCoder
 * @author 周义礼
 * @version 2013-4-3 下午3:09:35
 *
 */
public class DateCoder {
	
	/**@Fields format:生成的格式*/
	private String format;
	/**@Fields value:目前的最后一个值*/
	private String value;
	/**@Fields date:*/
	private Date date;
	
	
	public DateCoder() {
	}
	
	
	public DateCoder(String format, String value, Date date) {
		this.format = format;
		this.value = value;
		this.date = date;
	}


	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "DateCoder [format=" + format + ", value=" + value + ", date=" + date + "]";
	}
	
}
