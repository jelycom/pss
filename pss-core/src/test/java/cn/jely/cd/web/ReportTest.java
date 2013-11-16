/*
 * 捷利商业进销存管理系统
 * @(#)ReportTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-21
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.SimpleReportContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.repo.JasperDesignReportResource;
import net.sf.jasperreports.repo.JasperDesignReportResourceCache;

import org.testng.annotations.Test;

import cn.jely.cd.BaseServiceTest;
import cn.jely.cd.export.ro.RealStockRO;
import cn.jely.cd.export.util.ExportUtil;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.vo.RealStockVO;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-10-21 下午1:51:57
 */
public class ReportTest extends BaseServiceTest{

	IProductService productService;
	IProductStockDetailService productStockDetailService;
	@Resource(name="productStockDetailService")
	public void setProductStockDetailService(IProductStockDetailService productStockDetailService) {
		this.productStockDetailService = productStockDetailService;
	}

	@Resource(name = "productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	@Test
	public void testDataSource() throws JRException{
//		JasperDesign design = JRXmlLoader.load("E:/study/jelyworkspace/DynamicReport/report/templates/Java5Report.jrxml");
		JasperDesign design = JRXmlLoader.load("F:/TDDOWNLOAD/product.jrxml");
		System.out.println(design.getQuery().getLanguage());
		System.out.println(design.getQuery().getText());
		Locale locale = Locale.CHINA;
		JRDesignStyle defaultStyle = ExportUtil.getDefaultStyle(locale);
//		defaultStyle.setItalic(true);
//		defaultStyle.setBold(true);
		design.setDefaultStyle(defaultStyle);
		JasperReport jasper = JasperCompileManager.compileReport(design);
		System.out.println(jasper.getQuery().getLanguage());
//		JasperDesignCache cache = JasperDesignCache.getInstance(DefaultJasperReportsContext.getInstance(), new SimpleReportContext());
//		cache.set(UUID.randomUUID().toString(), design);
		JasperDesignReportResourceCache reportCache = JasperDesignReportResourceCache.getInstance(new SimpleReportContext());
		JasperDesignReportResource drResource = new JasperDesignReportResource();
		drResource.setJasperDesign(design);
		drResource.setReport(jasper);
		reportCache.setResource("product", drResource);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productService.findAll(null));
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(JRParameter.REPORT_LOCALE, locale);
		parameters.put(JRParameter.REPORT_DATA_SOURCE, dataSource);//"datasource"
		parameters.put("title", "my title");
		JasperPrint jasPrint = JasperFillManager.fillReport(jasper, parameters , dataSource);
		JRRtfExporter rtfExporter = new JRRtfExporter();
		rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasPrint);
		rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "F:/TDDOWNLOAD/1.rtf");
		rtfExporter.exportReport();
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "F:/TDDOWNLOAD/1.xls");
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.exportReport();
		JasperExportManager.exportReportToPdfFile(jasPrint,"F:/TDDOWNLOAD/1.pdf");
//		JasperExportManager.exportReportToPdfFile(jasPrint, new FileOutputStream(new File(design.get)));
	}
	
	@Test
	public void testRealStock() throws JRException, IOException{
		JasperDesign design = JRXmlLoader.load("F:/TDDOWNLOAD/realstock.jrxml");
		design.setDefaultStyle(ExportUtil.getDefaultStyle(Locale.CHINA));
		JasperReport compileReport = JasperCompileManager.compileReport(design);
		Map<String, Object> parameters=new HashMap<String, Object>();
		ObjectQuery objectQuery= new ObjectQuery();
//		QueryGroup queryGroup = new QueryGroup();
//		objectQuery.setQueryGroup(queryGroup);
//		QueryRule queryRule = new QueryRule("o.warehouse.id", "=", 5l);
//		queryGroup.getRules().add(queryRule);
		List<RealStockVO> realStock = productStockDetailService.findRealStock(5l,null,objectQuery);
		List<RealStockRO> realStockROs = new ArrayList<RealStockRO>();
		for (RealStockVO realStockVO : realStock) {
			RealStockRO ro = new RealStockRO();
			ro.setProductName(realStockVO.getProduct().getFullName());
			ro.setShortName(realStockVO.getProduct().getShortName());
			ro.setProductUnit(realStockVO.getProduct().getUnit());
			ro.setQuantity(realStockVO.getQuantity());
			ro.setAmount(realStockVO.getAmount());
			ro.setWarehouseName(realStockVO.getWarehouse().getName());
			realStockROs.add(ro);
		}
		JasperPrint jrprint = ExportUtil.fillData(realStockROs, design, parameters);
		ExportUtil.print(jrprint, "Fax", true);
//		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(realStockROs);
//		parameters.put(JRParameter.REPORT_DATA_SOURCE, dataSource);
//		parameters.put(JRParameter.REPORT_LOCALE, Locale.CHINA);
//		JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters,dataSource);
//		JasperExportManager.exportReportToPdfFile(jasperPrint,"F:/TDDOWNLOAD/1.pdf");
	}
}
