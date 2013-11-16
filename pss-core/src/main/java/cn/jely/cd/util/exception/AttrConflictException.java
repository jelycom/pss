/*
 * 捷利商业进销存管理系统
 * @(#)AttrConflictException.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-5-31
 */
package cn.jely.cd.util.exception;

/**
 * @ClassName:AttrConflictException
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-5-31 下午3:37:22
 *
 */
public class AttrConflictException extends RuntimeException{
	private static  String message="属性冲突/关键属性已经存在";
	
	public AttrConflictException() {
		super(message);
	}

	public AttrConflictException(String detailmessage) {
		super(message+":"+detailmessage);
	}
}
