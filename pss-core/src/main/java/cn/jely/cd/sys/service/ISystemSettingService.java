/*
 * 捷利商业进销存管理系统
 * @(#)ISystemSettingService.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-6
 */
package cn.jely.cd.sys.service;

import cn.jely.cd.sys.domain.SystemSetting;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-6 上午11:01:42
 */
public interface ISystemSettingService {

	public SystemSetting getSetting();
	
	public void save(SystemSetting systemSetting);
}
