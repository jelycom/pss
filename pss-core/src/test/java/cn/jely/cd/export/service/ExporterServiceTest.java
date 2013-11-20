/*
 * 捷利商业进销存管理系统
 * @(#)ExportServiceTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-30
 */
package cn.jely.cd.export.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import cn.jely.cd.BaseServiceTest;
import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.dao.IProductTypeDao;
import cn.jely.cd.domain.LftRgtTreeNodeComparator;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.export.domain.TransformSetting;
import cn.jely.cd.export.domain.TransformSettingFactory;
import cn.jely.cd.export.util.ExportUtil;
import cn.jely.cd.util.AdjacencyNode;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.LftRgtTreeMenuTool;
import cn.jely.cd.util.UrlUtil;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-30 上午10:58:53
 */
public class ExporterServiceTest extends BaseServiceTest {

	private IExporterService exporterService;
	private IProductDao productDao;
	private IProductTypeDao productTypeDao;

	@Resource(name = "productDao")
	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	@Resource(name = "productTypeDao")
	public void setProductTypeDao(IProductTypeDao productTypeDao) {
		this.productTypeDao = productTypeDao;
	}

	@Resource(name = "exporterService")
	public void setExporterService(IExporterService exporterService) {
		this.exporterService = exporterService;
	}

	@Test
	public void testFindRealStock() throws JRException, IOException {
		exporterService.setBasePath("f:/TDDOWNLOAD");
		JasperPrint jrPrint = exporterService.findRealStock(null, null, 2l);
		System.out.println(jrPrint.getName());
		FileOutputStream fos = new FileOutputStream(new File("f:/TDDOWNLOAD/1.xls"));
		ExportUtil.toXls(fos, jrPrint, null);
		// fos.write(JasperExportManager.exportReportToPdf(jrPrint));
		// JasperViewer view = new JasperViewer(jrPrint,true);
		// view.setVisible(true);
		// view.setAlwaysOnTop(true);
	}

	@Test
	public void testFindRealStockDoc() throws JRException, IOException {
		exporterService.setBasePath("f:/TDDOWNLOAD");
		JasperPrint jrPrint = exporterService.findRealStock(null, null, 2l);
		System.out.println(jrPrint.getName());
		FileOutputStream fos = new FileOutputStream(new File("f:/TDDOWNLOAD/1.doc"));
		ExportUtil.toDoc(fos, jrPrint);
	}

	@Test
	public void testCreateTypeXls() throws IOException {
		XSSFWorkbook workBook = new XSSFWorkbook();
		String typeSheetName = "productType";
		XSSFSheet typeSheet = workBook.createSheet(typeSheetName);
		List<ProductType> allTypes = productTypeDao.getAll();
		Collections.sort(allTypes, new LftRgtTreeNodeComparator());// 按左值排序。
		List<AdjacencyNode<ProductType>> adjacencyNodes = new LftRgtTreeMenuTool<ProductType>().toAdjacencyNode(allTypes);
		int rownum = 0;
		XSSFRow typeTitleRow = typeSheet.createRow(rownum++);
		typeTitleRow.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("类别名称");
		typeTitleRow.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("类别编号");
		typeTitleRow.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("父类编号");
		for (AdjacencyNode<ProductType> productType : adjacencyNodes) {
			XSSFRow row = typeSheet.createRow(rownum);
			XSSFCell typeCell = row.createCell(0, Cell.CELL_TYPE_STRING);
			typeCell.setCellValue(productType.getNode().getName());
			XSSFCell itemCell = row.createCell(1, Cell.CELL_TYPE_STRING);
			itemCell.setCellValue(productType.getNode().getItem());
			if (productType.getParent() != null) {
				XSSFCell parentItemCell = row.createCell(2, Cell.CELL_TYPE_STRING);
				parentItemCell.setCellValue(productType.getParent().getItem());
			}
			rownum++;
		}
		XSSFSheet productSheet = workBook.createSheet("product");
		XSSFRow titleRow = productSheet.createRow(0);
		List<TransformSetting> transSettings = new TransformSettingFactory().getProductSetting();
		for (int i = 0; i < transSettings.size(); i++) {
			TransformSetting setting = transSettings.get(i);
			XSSFCell titleCell = null;
			titleCell = titleRow.createCell(i, Cell.CELL_TYPE_STRING);
			String cellValue = setting.getDispName();
			titleCell.setCellValue(cellValue);
			productSheet.autoSizeColumn(i, true);
			int columnWidth = productSheet.getColumnWidth(i);
			int needWidth = cellValue.getBytes().length * 256;
			if (columnWidth < needWidth) {
				productSheet.setColumnWidth(i, needWidth);
			}
			if ("productTypeName".equals(setting.getPropertyName())) {
				// NameRecord type = new NameRecord();
				// type.setNameDefinition(new Ptg[]{new
				// Area3DPtg("OFFSET("+typeSheet.getSheetName()+"!$A$3,0,0,COUNTA("+typeSheet.getSheetName()+"!$A:$A)-2)",
				// 1)});
				Name type = workBook.createName();
				type.setNameName("type");
				type.setRefersToFormula("OFFSET(" + typeSheet.getSheetName() + "!$A$3,0,0,COUNTA(" + typeSheet.getSheetName() + "!$A:$A)-2)");
				XSSFDataValidationHelper helper = new XSSFDataValidationHelper(productSheet);
				// // DataValidationHelper helper =
				// // productSheet.getDataValidationHelper();
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) helper.createFormulaListConstraint("type");
				// // CTDataValidation ctDataValidation
				// // =CTDataValidation.Factory.newInstance();
				CellRangeAddressList regions = new CellRangeAddressList(1, 10, i, i);
				DataValidation dataValidation = helper.createValidation(constraint, regions);
				productSheet.addValidationData(dataValidation);
				// DataValidation dataValidation = new
				// XSSFDataValidation(constraint, regions, ctDataValidation);
				// productSheet.addValidationData(new XSSFDataValidation(new
				// CellRangeAddressList(1, 10, i,
				// i),DVConstraint.createFormulaListConstraint(typeSheet.getSheetName()
				// + "!A1:" + typeSheet.getSheetName() + "!C1")));
			} else if ("productTypeItem".equals(setting.getPropertyName())) {
				XSSFDataValidationHelper helper = new XSSFDataValidationHelper(productSheet);
				for (int j = 0; j < 10; j++) {
					XSSFRow row = productSheet.createRow(j + 1);
					XSSFCell itemCell = row.createCell(i, Cell.CELL_TYPE_STRING);
					int cellnum = j + 2;
					itemCell.setCellFormula("IF(A" + cellnum + "=\"\",\"\",VLOOKUP(A" + cellnum + ",OFFSET(productType!$A$3,,,COUNTA(productType!$A:$A),2),2,0))");
				}
			} else if ("costMethod".equals(setting.getPropertyName())) {
				int len = CostMethod.values().length;
				String[] costNames = new String[len];
				for (int ci = 0; ci < len; ci++) {
					costNames[ci] = CostMethod.values()[ci].getMethodName();
				}
				XSSFDataValidationHelper helper = new XSSFDataValidationHelper(productSheet);
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) helper.createExplicitListConstraint(costNames);
				CellRangeAddressList addressList = new CellRangeAddressList(1, 10, i, i);
				XSSFDataValidation validation = (XSSFDataValidation) helper.createValidation(constraint, addressList);
				validation.setShowErrorBox(true);
				productSheet.addValidationData(validation);
			}
		}
		// titleRow.createCell(0, type);
		OutputStream stream = new FileOutputStream("f:/tddownload/product.xlsx");
		workBook.setActiveSheet(1);
		workBook.write(stream);
	}
	@Test
	public void testFileImport() throws IOException{
		FileInputStream fis = new FileInputStream(new File("f:/tddownload/product.xlsx"));
		XSSFWorkbook workBook = new XSSFWorkbook(fis);
		XSSFSheet productSheet = workBook.getSheet("product");
		List<TransformSetting> transSettings = null;
		if(productSheet!=null){
			Iterator<Row> rowIterator = productSheet.rowIterator();
			boolean title = true;
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				if (title) {
					transSettings = new TransformSettingFactory().getProductSetting();
				}else{
					for (int i = 0; i < row.getLastCellNum(); i++) {
						Cell cell = row.getCell(i);
						TransformSetting transformSetting = transSettings.get(i);
						String propertyName = transformSetting.getPropertyName();
						Field[] declaredFields = Product.class.getDeclaredFields();
						for (Field field : declaredFields) {
							if(propertyName.equals(field.getName())){
								Object cellValue = getCellValue(cell, null);
								break;
							}
						}
					}
				}
				System.out.println("first:"+row.getFirstCellNum()+" last:"+row.getLastCellNum());
			}
		}
	}
	
	private Object getCellValue(Cell cell,Integer cellType){
		Object ret = null;
		if (cell!=null) {
			if (cellType == null) {
				cellType = cell.getCellType();
			}
			switch (cellType) {
			case Cell.CELL_TYPE_NUMERIC:
				ret = new Double(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				ret = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				ret = cell.getBooleanCellValue();
			case Cell.CELL_TYPE_FORMULA:
				int formulaType = cell.getCachedFormulaResultType();
				ret = getCellValue(cell, formulaType);
				break;
			default:
				break;
			}
		}
		System.out.print(ret);
		return ret;
	}
	@Test
	public void testPOI() throws IOException {
		HSSFWorkbook xlsWorkBook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = xlsWorkBook.createCellStyle();
		HSSFSheet sheet = xlsWorkBook.createSheet();
		HSSFRow row = sheet.createRow(0);
		HSSFCell title = row.createCell(0);
		title.setCellType(HSSFCell.CELL_TYPE_STRING);
		title.setCellValue("TEst Title");
		HSSFCellStyle titleStyle = xlsWorkBook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
		title.setCellStyle(titleStyle);
		CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0, 9);
		sheet.addMergedRegion(rangeAddress);
		int size = 10;
		for (int j = 0; j < 5; j++) {
			row = sheet.createRow(j + 1);
			for (int i = 0; i < size; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue("test" + i);
				cell.setCellStyle(cellStyle);
			}
		}
		OutputStream stream = new FileOutputStream("f:/tddownload/test.xls");
		xlsWorkBook.write(stream);
	}

	@Test
	public void createListBox() {
		String[] head = { "城市", "地区", "邮编" };
		String[] city = { "广州", "深圳", "珠海" };
		String[] Area = { "越秀区", "天河区", "白云区", "福田区", "南山区", "宝安区", "香洲区", "金湾区", "斗门区" };
		String[] mailNum = { "510001", "510002", "510003", "518001", "518002", "518003", "519101", "519102", "519103" };
		NameRecord city_GZ = new NameRecord();
		city_GZ.setNameText(city[0]);
		city_GZ.setNameDefinition(new Ptg[] { new Area3DPtg("$A2:$A4", 1) });
		NameRecord city_SZ = new NameRecord();
		city_SZ.setNameText(city[1]);
		city_SZ.setNameDefinition(new Ptg[] { new Area3DPtg("$B2:$B4", 1) });
		NameRecord city_ZH = new NameRecord();
		city_ZH.setNameText(city[2]);
		city_ZH.setNameDefinition(new Ptg[] { new Area3DPtg("$C2:$C4", 1) });
		InternalWorkbook iw = InternalWorkbook.createWorkbook();
		iw.addName(city_GZ);
		iw.addName(city_SZ);
		iw.addName(city_ZH);
		HSSFWorkbook wb = HSSFWorkbook.create(iw);
		HSSFSheet sheet1 = wb.createSheet("sheet1");
		HSSFSheet sheet2 = wb.createSheet("sheet2");
		HSSFRow row1_0 = sheet1.createRow(0);
		HSSFRow row2_0 = sheet2.createRow(0);
		wb.isSheetVeryHidden(1);
		for (int i = 0; i < city.length; i++) {
			HSSFCell hcell1_0_i = row1_0.createCell(i);
			hcell1_0_i.setCellValue(head[i]);
			HSSFCell hcell2_0_i = row2_0.createCell(i);
			hcell2_0_i.setCellValue(city[i]);
		}
		row2_0.createCell(3).setCellValue("地区");
		row2_0.createCell(4).setCellValue("邮编");
		for (int i = 0; i < 9; i++) {
			HSSFRow row2_i = sheet2.createRow(i + 1);
			HSSFCell hcell2_i_0 = row2_i.createCell(0);
			if (i < 3) {
				hcell2_i_0.setCellValue(Area[i]);
				HSSFCell hcell2_i_1 = row2_i.createCell(1);
				hcell2_i_1.setCellValue(Area[i + 3]);
				HSSFCell hcell3_i_1 = row2_i.createCell(2);
				hcell3_i_1.setCellValue(Area[i + 6]);
			}
			HSSFCell hcell4_i_1 = row2_i.createCell(3);
			hcell4_i_1.setCellValue(Area[i]);
			HSSFCell hcell5_i_1 = row2_i.createCell(4);
			hcell5_i_1.setCellValue(mailNum[i]);
		}
		HSSFRow row2_i = sheet2.createRow(10);
		HSSFCell hcell4_10_1 = row2_i.createCell(3);
		hcell4_10_1.setCellValue("");
		HSSFCell hcell5_10_1 = row2_i.createCell(4);
		hcell5_10_1.setCellValue("");
		sheet1.addValidationData(new HSSFDataValidation(new CellRangeAddressList(1, 10, 0, 0), DVConstraint.createFormulaListConstraint(sheet2.getSheetName() + "!A1:" + sheet2.getSheetName() + "!C1")));
		StringBuffer s = new StringBuffer("INDIRECT");
		s.append("(A").append(2).append(")");
		sheet1.addValidationData(new HSSFDataValidation(new CellRangeAddressList(1, 1, 1, 1), DVConstraint.createFormulaListConstraint(s.toString())));
		HSSFRow row1_i = sheet1.createRow(1);
		HSSFCell hcell1_i_1 = row1_i.createCell(2);
		HSSFCell hcell1_i_0 = row1_i.createCell(1);
		hcell1_i_1.setCellFormula("VLOOKUP(B" + (1 + 1) + ",sheet2!D2:sheet2!E11,2,0)");
		hcell1_i_0.setCellValue("");
		FileOutputStream fos;
		try {
			File file = new File(UrlUtil.getClassPath(getClass()) + "/workbook.xls");
			if (file.exists()) {
				file.delete();
			}
			fos = new FileOutputStream(UrlUtil.getClassPath(getClass()) + "/workbook.xls");
			wb.write(fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}// 结束
	}

	@Test
	public void testfind() {
		List list = productDao
				.findByNamedParam(
						"select new cn.jely.cd.export.ro.RealStockRO(p.fullName,p.shortName,p.unit,p.marque,p.specification,p.color,o.warehouse.name,sum(o.inquantity-o.outquantity),sum(o.amount)) from ProductStockDetail o join o.product p ",
						new HashMap<String, Object>());
		printList(list);
	}

}
