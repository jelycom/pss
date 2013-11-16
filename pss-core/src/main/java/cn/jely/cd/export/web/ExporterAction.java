/*
 * 捷利商业进销存管理系统
 * @(#)ExporterAction.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-28
 */
package cn.jely.cd.export.web;

import org.apache.struts2.ServletActionContext;

import cn.jely.cd.export.service.IExporterService;
import cn.jely.cd.web.BaseAction;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-10-28 下午3:33:49
 */
public class ExporterAction extends BaseAction{
	public static final String JRXMLPATH = "/report/jrxml/";
	public static final String REPORTPATH = "/report/jasperreport/";
	public static final String PRINTPATH = "/report/print/";
	private IExporterService exporterService;
	
	public void setExporterService(IExporterService exporterService) {
		this.exporterService = exporterService;
	}

	public String uploadjrxml(){
		String realPath = ServletActionContext.getServletContext().getRealPath("/");
		
		return null;
	}
	public void exportRealStock(){
		String warehouseId = getRequest().getParameter("warehouseId");
		String productTypeId = getRequest().getParameter("productTypeId");
	}
}
