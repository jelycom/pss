/*
 * 捷利商业进销存管理系统
 * @(#)SystemSetting.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-19
 */
package cn.jely.cd.sys.domain;

import java.util.Calendar;
import java.util.Date;

/**
 * 系统参数的设置
 * 
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-6-19 上午11:25:54
 * 
 */
public class SystemSetting {

	private static SystemSetting instance = new SystemSetting();
	private CompanyInfo companyInfo = new CompanyInfo();
	private SysEditOption editOption = new SysEditOption();
//	/** @Fields roundingMode:舍入的方式 */
//	private RoundingMode roundingMode;
//	/** @Fields amountScale:合计保留小数位 */
//	private int amountScale;
//	/** @Fields priceScale:单价保留小数位 */
//	private int priceScale;
//	/** @Fields locale:本地区域设置 */
//	private Locale locale;
	/** Date:currentDay:只保留年月日部分,应放入每个客户端 的Session中 */
	private static Date currentDay;
	
	//系统名称
	private String systemName;
	//系统版本
	private String systemVersion;
	

	// --------EDITOPTION-----录帐

	// -----------------查帐----
	// 查询条件允许保存查询时间到数据库
	// 发票管理按单处理
	// 客户价格跟踪
	// 启用会员生日管理功能
	// 生成红冲单据日期与原单相同
	// --------------其它
	// 过帐后单据显示打印次数并提示重复打印
	// 选择库存商品时默认收款日期到每月指定日期

	private SystemSetting() {

	}

	public static SystemSetting getInstance() {
		return instance;
	}

	public Date getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public void setCurrentDay(Date currentDay) {
		this.currentDay = currentDay;
	}

	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	public SysEditOption getEditOption() {
		return editOption;
	}

	public void setEditOption(SysEditOption editOption) {
		this.editOption = editOption;
	}
	
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
}
