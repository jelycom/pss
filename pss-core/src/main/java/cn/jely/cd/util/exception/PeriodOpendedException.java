/*
 * 捷利商业进销存管理系统
 * @(#)PeriodOpendedException.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-20
 */
package cn.jely.cd.util.exception;

/**
 * @ClassName:PeriodOpendedException
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-20 下午4:54:38
 *
 */
public class PeriodOpendedException extends RuntimeException {
	private static  String message="系统已经开帐";
	public PeriodOpendedException(){
		super(message);
	}
}
