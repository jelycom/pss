/*
 * 捷利商业进销存管理系统
 * @(#)ExporterServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-24
 */
package cn.jely.cd.export.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.IProductTypeDao;
import cn.jely.cd.export.dao.IExporterDao;
import cn.jely.cd.export.dao.IReportSettingDao;
import cn.jely.cd.export.domain.ReportSetting;
import cn.jely.cd.export.ro.RealStockRO;
import cn.jely.cd.export.service.IExporterService;
import cn.jely.cd.export.util.ExportUtil;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.query.ObjectQuery;

import com.lowagie.text.pdf.codec.Base64.OutputStream;

/**
 * 
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-10-24 下午6:11:45
 */
public class ExporterServiceImpl implements IExporterService {

	private String basePath = null;
	private IExporterDao exporterDao;
	private IReportSettingDao reportSettingDao;
	private IProductTypeDao productTypeDao;

	@Override
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Resource(name = "productTypeDao")
	public void setProductTypeDao(IProductTypeDao productTypeDao) {
		this.productTypeDao = productTypeDao;
	}

	@Resource(name = "exporterDao")
	public void setExporterDao(IExporterDao exporterDao) {
		this.exporterDao = exporterDao;
	}

	@Resource(name = "reportSettingDao")
	public void setReportSettingDao(IReportSettingDao reportSettingDao) {
		this.reportSettingDao = reportSettingDao;
	}

	@Override
	public void ExportPdf(ObjectQuery objectQuery, Long ReportSettingId, OutputStream outStream) throws JRException,
			IOException {
		if (null == objectQuery || null == outStream) {

		} else {
			List datas = exporterDao.findByNamedParam(objectQuery.getFullHql(), objectQuery.getParamValueMap());
			ReportSetting reportSetting = reportSettingDao.getById(ReportSettingId);
			JasperPrint jasperPrint = fillData(datas, reportSetting);
			outStream.write(JasperExportManager.exportReportToPdf(jasperPrint));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void ExportXls(ObjectQuery objectQuery, Long ReportSettingId, OutputStream outStream,
			boolean isonepagepersheet) throws JRException, IOException {
		if (null == objectQuery || null == outStream) {

		} else {
			List datas = exporterDao.findByNamedParam(objectQuery.getFullHql(), objectQuery.getParamValueMap());
			ReportSetting reportSetting = reportSettingDao.getById(ReportSettingId);
			JasperPrint jasperPrint = fillData(datas, reportSetting);
			BufferedOutputStream bos = new BufferedOutputStream(outStream);
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, isonepagepersheet);
			exporter.exportReport();
		}
	}

	private JasperPrint fillData(List datas, ReportSetting reportSetting) throws JRException,
			IOException {
		String jasperName = reportSetting.getJasperName();
		JasperPrint jasperPrint = null;
		File parent = null;
		if (StringUtils.isNotBlank(basePath)) {
			parent = new File(basePath);
		}
		if (StringUtils.isNotBlank(jasperName)) {
			// JasperFillManager.fillReport(jasperName, params);
			// this.getClass().getClassLoader().getResource(jasperName);
			File file = new File(parent, ConstValue.REPORT_COMPILEPATH + jasperName);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);
			jasperPrint = exporterDao.fillData(datas, jasperReport, null);
		} else if (StringUtils.isNotBlank(reportSetting.getDesignName())) {
			File exportFile = null;
			File file = new File(parent, ConstValue.REPORT_DESIGNPATH+"/"+reportSetting.getDesignName());
			JasperDesign design = JRXmlLoader.load(file);
			JRDesignStyle defaultStyle = ExportUtil.getDefaultStyle(Locale.getDefault());
			design.setDefaultStyle(defaultStyle);
			jasperPrint = exporterDao.fillData(datas, design, null);
			if(StringUtils.isNotBlank(basePath)){
				exportFile = new File(parent,ConstValue.REPORT_COMPILEPATH + "/" + design.getName() + ".jasper");
			}else{
				exportFile = new File(file.getParentFile(),design.getName() + ".jasper");
			}
			if(!exportFile.getParentFile().exists()){
				exportFile.getParentFile().mkdirs();
			}
			if(StringUtils.isNotBlank(basePath)){
				JasperCompileManager.compileReportToFile(design,exportFile.toString());
			}
		}
		return jasperPrint;
	}

	@Override
	public JasperPrint findRealStock(Long warehouseId, Long productTypeId, Long reportSettingId)
			throws JRException, IOException {
		JasperPrint jp = null;
		ReportSetting rs = null;
		if(warehouseId!=null){
			rs = reportSettingDao.findBySn("cpacfb");
		}else if (null != reportSettingId) {
			rs = reportSettingDao.getById(reportSettingId);
		}else {
			rs = reportSettingDao.findBySn("cpkcfb");
		}
		List<Serializable> typeIds = productTypeDao.findChildIds(productTypeId, true);
		List<RealStockRO> datas = exporterDao.findRealStock(warehouseId, typeIds);
		jp = fillData(datas, rs);
		return jp;
	}

}
