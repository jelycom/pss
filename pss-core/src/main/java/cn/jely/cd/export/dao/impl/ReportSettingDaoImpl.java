/*
 * 捷利商业进销存管理系统
 * @(#)ReportSettingDaoImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-28
 */
package cn.jely.cd.export.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.impl.BaseDaoImpl;
import cn.jely.cd.export.dao.IReportSettingDao;
import cn.jely.cd.export.domain.ReportSetting;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 报表配置参数表
 * 
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-10-28 下午1:49:51
 */
public class ReportSettingDaoImpl extends BaseDaoImpl<ReportSetting> implements IReportSettingDao {
	static {
	}
	List<ReportSetting> settings = Arrays.asList(new ReportSetting[] {
			new ReportSetting(1l, "产品进货统计表", "jhtj", "report/design/", ""),
			new ReportSetting(2l, "产品库存分布表", "cpkcfb", "realstock.jrxml", ""),
			new ReportSetting(3l, "产品按仓分布表", "cpacfb", "realstockbypw.jrxml", "")

	});

	@Override
	public ReportSetting getById(Serializable id) {
		Long lid = (Long) id;
		// ReportSetting reportSetting = null;
		// switch (lid.intValue()) {
		// case 1:
		// reportSetting = new
		// ReportSetting(1l,"产品进货统计表","jhtj","report/design/","");
		// break;
		// case 2:
		// // reportSetting = new
		// ReportSetting(2l,"产品库存分布表","report/design/realstock.jrxml","report/report/realstock.jasper");
		// reportSetting = new
		// ReportSetting(2l,"产品库存分布表","cpkcfb","realstock.jrxml","");
		// break;
		// case 3:
		// reportSetting = new
		// ReportSetting(3l,"产品库存按仓分布","cpacfb","realstockbypw.jrxml","realstockbypw.jasper");
		// default:
		// break;
		// }
		for (ReportSetting reportSetting : settings) {
			if (reportSetting.getId().equals(lid)) {
				return reportSetting;
			}
		}
		return null;
	}

	@Override
	public ReportSetting findBySn(String sn) {
		if (StringUtils.isNotBlank(sn)) {
			for (ReportSetting reportSetting : settings) {
				if (sn.equals(reportSetting.getSn())) {
					return reportSetting;
				}
			}
		}
		return null;
	}

}
