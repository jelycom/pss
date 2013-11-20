/*
 * 捷利商业进销存管理系统
 * @(#)ExportUtil.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-24
 */
package cn.jely.cd.export.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

import org.apache.commons.lang3.StringUtils;



/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-24 下午10:05:16
 */
public class ExportUtil {

	public static JasperPrint fillData(List<?> datas, JasperDesign design, Map<String, Object> reportParams)
			throws JRException, IOException {
		if (null == datas || datas.size() <= 0 || null == design) {
			return null;
		} else {
			JasperReport compileReport = JasperCompileManager.compileReport(design);
			return fillData(datas, compileReport, reportParams);
		}
	}

	public static JasperPrint fillData(List<?> datas, JasperReport report, Map<String, Object> reportParams)
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
	public static void downFile(HttpServletResponse response,String fileName,byte [] content) throws IOException{
		BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(content));
		downFile(response, fileName, bis);
	}
	public static void downFile(HttpServletResponse response,String fileName,InputStream is) throws IOException{
		response.setHeader("Content-disposition", "attachment;filename="+fileName);
		response.setContentType("application/x-download");
		ServletOutputStream outputStream = response.getOutputStream();
		int idx = 0;
		byte [] buf = new byte[1024];
		while((idx = is.read(buf))!=-1){
			outputStream.write(buf,0,idx);
		}
		outputStream.flush();
		outputStream.close();
	}
	
	public static void toPdf(HttpServletResponse response, JasperPrint jrprint) throws IOException, JRException{
		response.setHeader("Content-disposition", "attachment;filename="+jrprint.getName()+".pdf");
		response.setContentType("application/pdf");
		toPdf(response.getOutputStream(), jrprint);
	}
	public static void toXls(HttpServletResponse response, JasperPrint jrprint) throws IOException, JRException{
		response.setHeader("Content-disposition", "attachment;filename="+jrprint.getName()+".xls");
		response.setContentType("application/x-download");
		HashMap<JRExporterParameter, Object> setting = new HashMap<JRExporterParameter, Object>();
		setting.put(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, false);
		toXls(response.getOutputStream(), jrprint,setting);
	}
	
	public static void toPdf(OutputStream outStream, JasperPrint jrprint) throws IOException, JRException {
		BufferedOutputStream bos = new BufferedOutputStream(outStream);
		byte[] pdfBytes = JasperExportManager.exportReportToPdf(jrprint);
		bos.write(pdfBytes);
//		ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
//		byte[] buf = new byte[1024];
//		int len = 0;
//		while( (len = bis.read(buf, 0, 1024))!=-1){
//			bos.write(buf);
//		}
	}

	public static void toXls(OutputStream outStream, JasperPrint jrprint, Map<JRExporterParameter, Object> setting)
			throws JRException {
		JRXlsExporter exporter = new JRXlsExporter();
//		BufferedOutputStream bos = new BufferedOutputStream(outStream);
		if (setting == null || setting.isEmpty()) {
			setting = new HashMap<JRExporterParameter, Object>();
		}
		if (!setting.containsKey(JRExporterParameter.JASPER_PRINT)) {
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrprint);
		}
		if(!setting.containsKey(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET)){
			setting.put(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		}
		for (JRExporterParameter paramName : setting.keySet()) {
			exporter.setParameter(paramName, setting.get(paramName));
		}
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.exportReport();
	}

	/**
	 *
	 * @param fos
	 * @param jrPrint void
	 * @throws JRException 
	 */
	public static void toDoc(FileOutputStream fos, JasperPrint jrPrint) throws JRException {
		JRExporter exporter =  new JRDocxExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fos);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrPrint);
		exporter.exportReport();
	}
	public static void print(JasperPrint jrprint,String printerName , boolean withPrintDialog) throws JRException {
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
		// printServiceAttributeSet.add(new
		// PrinterName("Epson Stylus 820 ESC/P 2", null));
		// printServiceAttributeSet.add(new
		// PrinterName("hp LaserJet 1320 PCL 6", null));
		// printServiceAttributeSet.add(new PrinterName("PDFCreator", null));

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

	private static List<String> getPrinterList() {
		PrintService[] printers = PrintServiceLookup.lookupPrintServices(null, null);
		List<String> printerNames = new ArrayList<String>();
		for (PrintService printService : printers) {
			printerNames.add(printService.getName());
		}
		return printerNames;
	}
	
	public static JRDesignStyle getDefaultStyle(Locale locale){
		JRDesignStyle chartStyle = new JRDesignStyle();
		if(Locale.CHINA.equals(locale)){
			chartStyle.setName("Title");
			chartStyle.setDefault(true);
			chartStyle.setFontSize(10);
			chartStyle.setFontName("楷体_GB2312");
			chartStyle.setPdfFontName("楷体_GB2312");
			chartStyle.setPdfEncoding("Identity-H"); 
//			chartStyle.setFontName("宋体");
//			chartStyle.setPdfFontName("宋体");
//			chartStyle.setPdfEncoding("Identity-H");
//			chartStyle.setFontName("宋体");
//			chartStyle.setPdfFontName("STSong-Light");
//			chartStyle.setPdfEncoding("UniGB-UCS2-H");
			chartStyle.setPdfEmbedded(true);
			chartStyle.setBold(false);
		}
		return chartStyle;
	}


}
