/*
 * 捷利商业进销存管理系统
 * @(#)ToPageModel.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-16
 */
package cn.jely.cd.pagemodel;

/**
 * @ClassName:ToPageModel
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-16 下午3:33:34
 *
 */
public interface ToPageModel<T> {
	/**
	 * 转换为页面模型
	 * @param withDetail true:是,转换明细模型,false:不转换明细模型
	 * @return T
	 */
	public T convert(boolean withDetail);
}
