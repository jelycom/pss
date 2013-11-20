/*
 * 捷利商业进销存管理系统
 * @(#)ReportSetting.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-28
 */
package cn.jely.cd.export.domain;

import java.util.List;

import cn.jely.cd.sys.domain.ActionResource;

/**
 * 报表信息类，用于保存上传的报表设计文件，编译后的文件及相应的打印文件。
 * 设计为在页面选择相应的资源地址，然后上传相应的模板文件，同一资源地址有多个模板时可指定默认的模板文件。再通过拦截器查询相应的模板调用打印。
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-28 上午11:33:58
 */
public class ReportSetting {
	private Long id;
//	private String className;
//	private String methodName;
	private ActionResource actionResource;
	/**String:name:报表显示名称*/
	private String name;
	/**String:sn:标识号*/
	private String sn;
	/**String:designName:报表设计名.jrxml,只在有在线设计时才使用此。*/
	private String designName;
	/**String:jasperName:编译后的报表名.jasper，每次上传或预置时均产生，用于实际填充报表使用。*/
	private String jasperName;
	/**List<String>:reportNames:所生成的报表.print,需要包装为一实体，因为需要有日期来进行区分已经生成的报表让用户选择*/
	private List<ReportSave> reportSaves;
	
	
	public ReportSetting() {
	}
	
	
	public ReportSetting(Long id) {
		this.id = id;
	}


	public ReportSetting(Long id, String name,String sn, String designName, String jasperName) {
		this.id = id;
		this.name = name;
		this.sn = sn;
		this.designName = designName;
		this.jasperName = jasperName;
	}

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
	public String getDesignName() {
		return designName;
	}
	public void setDesignName(String designName) {
		this.designName = designName;
	}
	public String getJasperName() {
		return jasperName;
	}
	public void setJasperName(String jasperName) {
		this.jasperName = jasperName;
	}
	public List<ReportSave> getReportSaves() {
		return reportSaves;
	}
	public void setReportSaves(List<ReportSave> reportSaves) {
		this.reportSaves = reportSaves;
	}
	public ActionResource getActionResource() {
		return actionResource;
	}
	public void setActionResource(ActionResource actionResource) {
		this.actionResource = actionResource;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	
}
