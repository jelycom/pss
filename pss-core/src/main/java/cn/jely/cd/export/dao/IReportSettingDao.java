/*
 * 捷利商业进销存管理系统
 * @(#)IReportSettingDao.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-28
 */
package cn.jely.cd.export.dao;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.export.domain.ReportSetting;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-28 下午1:48:46
 */
public interface IReportSettingDao extends IBaseDao<ReportSetting> {
	ReportSetting findBySn(String sn);
}
