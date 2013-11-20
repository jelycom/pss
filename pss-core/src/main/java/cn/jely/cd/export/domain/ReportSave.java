/*
 * 捷利商业进销存管理系统
 * @(#)ReportSave.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-11-4
 */
package cn.jely.cd.export.domain;

import java.util.Date;

/**
 * 生成已经填充了数据的报表文件
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-11-4 上午10:50:13
 */
public class ReportSave {
	private Long id;
	/**String:name:保存名称*/
	private String name;
	/**Date:saveDate:保存时间*/
	private Date saveDate;
	/**ReportSetting:reportSetting:对应的模板*/
	private ReportSetting reportSetting;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}
	public ReportSetting getReportSetting() {
		return reportSetting;
	}
	public void setReportSetting(ReportSetting reportSetting) {
		this.reportSetting = reportSetting;
	}
	
}
