/*
 * 捷利商业进销存管理系统
 * @(#)SystemSettingServiceTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-6
 */
package cn.jely.cd.sys;

import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Test;

import cn.jely.cd.BaseServiceTest;
import cn.jely.cd.sys.domain.CompanyInfo;
import cn.jely.cd.sys.domain.SysEditOption;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.service.ISystemSettingService;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.DateUtils;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-6 上午11:12:01
 */
public class SystemSettingServiceTest extends BaseServiceTest {

	private ISystemSettingService systemSettingService;

	@Resource(name = "systemSettingService")
	public void setSystemSettingService(ISystemSettingService systemSettingService) {
		this.systemSettingService = systemSettingService;
	}

	@Test
	public void testGetSetting() {
		SystemSetting setting = systemSettingService.getSetting();
		System.out.println(setting);
	}

	@Test
	public void testSave() {
		SystemSetting systemSetting = SystemSetting.getInstance();

		CompanyInfo companyInfo = systemSetting.getCompanyInfo();
		companyInfo.setCompanyName("成都捷利电脑通讯有限公司");
		companyInfo.setRegisteredAddress("单位地址");
		companyInfo.setBusinessAddress("经营地址");
		companyInfo.setCorporationTax("税号:13256456");
		companyInfo.setTelephoneNumber("TEL:12345678921");
		companyInfo.setFaxNumber("FAX:123456798523");
		companyInfo.setLegalRepresentative("法人代表：常胜将军");
		companyInfo.setLocale(Locale.CHINA);
		companyInfo.setOpenningDate(DateUtils.getDayBegin(new Date()));
		SysEditOption syEditOption = systemSetting.getEditOption();
		syEditOption.setPriceScale(6);
		syEditOption.setAmountScale(3);
		syEditOption.setUniteCostMethod(false);
		syEditOption.setDefaultCostMethod(CostMethod.FIFO);
		syEditOption.setCostUnusualWarn(true);
		syEditOption.setUnusualPercent(20);
		syEditOption.setSaveBeforePrint(true);
		syEditOption.setAutoGenerateBillItem(true);
		syEditOption.setBillChangeOrderPrice(true);
		syEditOption.setPurchasePriceUnusualwarn(true);
		syEditOption.setRememberwar_emp(false);
		syEditOption.setChangeBillDate(true);
		syEditOption.setBillDateMustToday(false);
		syEditOption.setDepartmentBeNull(true);
		syEditOption.setEmployeeBeNull(false);
		syEditOption.setChangeEmployee(true);
		syEditOption.setInventoryWarn(true);
		syEditOption.setPriceBelowCostWarn(true);
		syEditOption.setPriceBelowBidWarn(true);
		syEditOption.setPriceBelowAlowWarn(true);
		syEditOption.setAutoSaveNestPrice(true);
		syEditOption.setDefaultACC_AMT(false);
		syEditOption.setDispRealStock(true);
		syEditOption.setDispARAP(true);
		syEditOption.setAutoGenSummary(true);
		syEditOption.setPerformWhenARExceed(false);
		syEditOption.setAlowNegativeStock(true);
		syEditOption.setMonthCloseDay(27);
		syEditOption.setAutoMonthClose(true);
		systemSettingService.save(systemSetting);
	}
}
