/*
 * 捷利商业进销存管理系统
 * @(#)ISystemService.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-1
 */
package cn.jely.cd.sys.service;

/**
 * @ClassName:ISystemService
 * @author 周义礼
 * @version 2013-4-1 下午3:04:46
 *
 */
public interface ISystemService {
	/**初始化测试数据
	 * @return Boolean
	 */
	Boolean saveinitData();
	/**
	 * 修复初始化测试数据
	 * @return Boolean
	 */
	Boolean updaterepair();
}
