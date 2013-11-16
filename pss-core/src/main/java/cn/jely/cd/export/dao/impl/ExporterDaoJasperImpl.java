/*
 * 捷利商业进销存管理系统
 * @(#)ExporterDaoImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-24
 */
package cn.jely.cd.export.dao.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.commons.lang3.StringUtils;

import cn.jely.cd.dao.impl.BaseDaoImpl;
import cn.jely.cd.export.dao.IExporterDao;
import cn.jely.cd.export.ro.RealStockRO;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryRule;


/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-10-24 下午6:15:37
 */
public class ExporterDaoJasperImpl extends BaseDaoImpl<Object> implements IExporterDao {

//	@Override
//	public void ExportPdf(ObjectQuery objectQuery, JasperDesign design, Map<String,Object> setting, OutputStream outStream) throws JRException,
//			IOException {
//		if(null == objectQuery ||null == design || null == outStream){
//			
//		}else{
//			JasperReport compileReport = JasperCompileManager.compileReport(design);
//			ExportPdf(objectQuery, compileReport,setting, outStream);
//		}
//	}
//
//	@Override
//	public void ExportPdf(ObjectQuery objectQuery, JasperReport report, Map<String,Object> setting, OutputStream outStream) throws JRException,
//			IOException {
//		if(null == objectQuery ||null == report || null == outStream){
//			
//		}else{
//			List datas = findByNamedParam(objectQuery.getFullHql(), objectQuery.getParamValueMap());
//			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
//			if(null==setting||setting.isEmpty()){
//				setting = new HashMap<String,Object>();
//				setting.put(JRParameter.REPORT_DATA_SOURCE, dataSource);
//				setting.put(JRParameter.REPORT_LOCALE, Locale.CHINA);
//			}
//			JasperPrint jasperPrint = JasperFillManager.fillReport(report, setting,dataSource);
//			BufferedOutputStream bos=new BufferedOutputStream(outStream);
//			bos.write(JasperExportManager.exportReportToPdf(jasperPrint));
//		}
//	}
//
//	@Override
//	public void ExportXls(ObjectQuery objectQuery, JasperDesign design, Map<JRExporterParameter,Object> setting, OutputStream outStream) throws JRException,
//			IOException {
//		
//	}
//
//	@Override
//	public void ExportXls(ObjectQuery objectQuery, JasperReport report,Map<JRExporterParameter,Object> setting,  OutputStream outStream) throws JRException,
//			IOException {
//		List datas = findByNamedParam(objectQuery.getFullHql(), objectQuery.getParamValueMap());
//		if (setting != null && !setting.isEmpty()) {
//			BufferedOutputStream bos = new BufferedOutputStream(outStream);
//			JRXlsExporter exporter = new JRXlsExporter();
//			for (JRExporterParameter paramName : setting.keySet()) {
//				exporter.setParameter(paramName, setting.get(paramName));
//			}
//			if (!setting.containsKey(JRExporterParameter.JASPER_PRINT)) {
//				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrprint);
//			}
//			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
//			exporter.exportReport();
//		}
//		
//	}
	@Override
	public JasperPrint fillData(List<?> datas, JasperDesign design, Map<String, Object> reportParams)
			throws JRException, IOException {
		if (null == datas || datas.size() <= 0 || null == design) {
			return null;
		} else {
			JasperReport compileReport = JasperCompileManager.compileReport(design);
			return fillData(datas, compileReport, reportParams);
		}
	}
	@Override
	public JasperPrint fillData(List<?> datas, JasperReport report, Map<String, Object> reportParams)
			throws JRException, IOException {
		if (null == datas || datas.size() <= 0 || null == report) {
			return null;
		} else {
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
			if (null == reportParams) {
				reportParams = new HashMap<String, Object>();
				reportParams.put(JRParameter.REPORT_DATA_SOURCE, dataSource);
				reportParams.put(JRParameter.REPORT_LOCALE, Locale.CHINA);
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, reportParams);
			return jasperPrint;
		}
	}
	@Override
	public void toPdf(OutputStream outStream, JasperPrint jrprint) throws IOException, JRException {
		BufferedOutputStream bos = new BufferedOutputStream(outStream);
		bos.write(JasperExportManager.exportReportToPdf(jrprint));
	}
	@Override
	public void toXls(OutputStream outStream, JasperPrint jrprint, Map<JRExporterParameter, Object> setting)
			throws JRException {
		if (setting != null && !setting.isEmpty()) {
			BufferedOutputStream bos = new BufferedOutputStream(outStream);
			JRXlsExporter exporter = new JRXlsExporter();
			for (JRExporterParameter paramName : setting.keySet()) {
				exporter.setParameter(paramName, setting.get(paramName));
			}
			if (!setting.containsKey(JRExporterParameter.JASPER_PRINT)) {
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrprint);
			}
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
			exporter.exportReport();
		}
	}

	public void print(JasperPrint jrprint,String printerName , boolean withPrintDialog) throws JRException {
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(MediaSizeName.ISO_A4);
		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		boolean printerFound = false;
		if (StringUtils.isNotBlank(printerName)) {
			List<String> printerNames = getPrinterList();
			for (String name : printerNames) {
				if (name.equals(printerName)) {
					printServiceAttributeSet.add(new PrinterName(name, null));
					printerFound = true;
					break;
				}
			}
		}
		JRPrintServiceExporter exporter = new JRPrintServiceExporter();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrprint);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
		exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
		if (printerFound) {
			exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
		}else{
			exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);
		}
		// exporter.setParameter(JRPrintServiceExporterParameter.pr, value);
		exporter.exportReport();
	}

	private List<String> getPrinterList() {
		PrintService[] printers = PrintServiceLookup.lookupPrintServices(null, null);
		List<String> printerNames = new ArrayList<String>();
		for (PrintService printService : printers) {
			printerNames.add(printService.getName());
		}
		return printerNames;
	}
	@Override
	public List<RealStockRO> findRealStock(Long warehouseId, List<Serializable> typeIds) {
		String baseHql = "select new cn.jely.cd.export.ro.RealStockRO(p.fullName,p.shortName,p.unit,p.marque,p.specification,p.color,o.warehouse.name,sum(o.inquantity-o.outquantity),sum(o.amount)) from ProductStockDetail o join o.product p ";
//		String baseHql = "select o from ProductStockDetail o join o.product p ";
		ObjectQuery objectQuery = new ObjectQuery(baseHql);
		if(null!=warehouseId){
			objectQuery.getQueryGroup().getRules().add(new QueryRule("o.warehouse.id", FieldOperation.eq, warehouseId));
		}
		if(null!=typeIds && !typeIds.isEmpty()){
			objectQuery.getQueryGroup().getRules().add(new QueryRule("p.productType.id", FieldOperation.in, typeIds));
		}
//		if(null!=productId){
//			objectQuery.getQueryGroup().getRules().add(new QueryRule("p.id", FieldOperation.eq, productId));
//		}
		objectQuery.setGroup("o.warehouse,o.product");
		String fullHql = objectQuery.getFullHql();
		Map<String, Object> paramValueMap = objectQuery.getParamValueMap();
		return findByNamedParam(fullHql, paramValueMap);
	}
}
