/*
 * 捷利商业进销存管理系统
 * @(#)NoDateGenerator.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-3
 */
package cn.jely.cd.util.code.impl;

import java.util.Date;

/**
 * 跟时间无关的编号生成类
 * @ClassName:NoDateGenerator
 * @author 周义礼
 * @version 2013-4-3 下午3:51:49
 * 
 */
public class NoDateGenerator extends AbstractCodeGenerator {


	@Override
	protected boolean checkDate(Date date, Date oldDate) {
		return false;
	}

}
