/*
 * 捷利商业进销存管理系统
 * @(#)IExporterService.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-24
 */
package cn.jely.cd.export.service;

import java.io.IOException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import cn.jely.cd.util.query.ObjectQuery;

import com.lowagie.text.pdf.codec.Base64.OutputStream;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-10-24 下午6:04:29
 */
public interface IExporterService {
	
	/**
	 * 将符合条件的数据导出到指定的流中。
	 * @param objectQuery
	 * @param ReportSettingId TODO
	 * @param outStream void
	 * @throws JRException 
	 * @throws IOException 
	 */
	void ExportPdf(ObjectQuery objectQuery,Long ReportSettingId, OutputStream outStream) throws JRException, IOException;
	void ExportXls(ObjectQuery objectQuery,Long ReportSettingId, OutputStream outStream,boolean isonepagepersheet) throws JRException, IOException;
	/**
	 *
	 * @param warehouseId
	 * @param productTypeId
	 * @param productId
	 * @param reportSettingId
	 * @return JasperPrint
	 * @throws IOException 
	 * @throws JRException 
	 */
	public JasperPrint findRealStock(Long warehouseId, Long productTypeId, Long reportSettingId) throws JRException, IOException;
	/**
	 * 设置项目地址
	 * @param basePath void
	 */
	void setBasePath(String basePath);
}
