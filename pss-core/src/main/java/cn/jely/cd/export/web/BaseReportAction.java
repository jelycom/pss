/*
 * 捷利商业进销存管理系统
 * @(#)BaseReportAction.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-29
 */
package cn.jely.cd.export.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

import cn.jely.cd.export.util.ExportUtil;
import cn.jely.cd.web.BaseAction;

/**
 * 报表Action基类。
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-29 下午3:38:30
 */
public class BaseReportAction extends BaseAction {
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	public static final int pdf = 1;
	public static final int xls = 2;
	
	/**int:exportType:输出类型*/
	protected int exportType = 1;
	protected JasperPrint jasperPrint;
	protected Map<JRExporterParameter, Object> xlsSetting;
	
	protected void export() throws IOException, JRException{
		OutputStream outputStream = getResponse().getOutputStream();
		switch(exportType){
		case 1:
			ExportUtil.toPdf(outputStream, jasperPrint);
			break;
		case 2:
			ExportUtil.toXls(outputStream, jasperPrint, xlsSetting);
			break;
		default:
			ExportUtil.toPdf(outputStream, jasperPrint);
		}

	}
	public int getExportType() {
		return exportType;
	}
	public void setExportType(int exportType) {
		this.exportType = exportType;
	}
	
}
