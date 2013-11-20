/*
 * 捷利商业进销存管理系统
 * @(#)SystemSettingServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-6
 */
package cn.jely.cd.sys.service.impl;

import cn.jely.cd.sys.dao.ISystemSettingDao;
import cn.jely.cd.sys.domain.SysEditOption;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.service.ISystemSettingService;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-6 上午11:02:51
 */
public class SystemSettingServiceImpl implements ISystemSettingService {
	
	private ISystemSettingDao systemSettingDao;
	
	public void setSystemSettingDao(ISystemSettingDao systemSettingDao) {
		this.systemSettingDao = systemSettingDao;
	}

	@Override
	public SystemSetting getSetting() {
		return systemSettingDao.getSetting();
	}

	@Override
	public void save(SystemSetting systemSetting) {
		if(systemSetting!=null){
			SysEditOption editOption = systemSetting.getEditOption();
			if(editOption.getAmountScale()>4){
				editOption.setAmountScale(4);
			}
			if(editOption.getPriceScale()>8){
				editOption.setPriceScale(8);
			}
			systemSettingDao.save(systemSetting);
		}
	}

}
