/*
 * 捷利商业进销存管理系统
 * @(#)Product.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.dao.IProductTypeDao;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.LftRgtTreeNodeComparator;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.export.domain.TransformSetting;
import cn.jely.cd.export.domain.TransformSettingFactory;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.util.AdjacencyNode;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.LftRgtTreeMenuTool;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2012-11-30 16:26:19
 * 
 */
@Service("productService")
public class ProductServiceImpl extends BaseServiceImpl<Product> implements IProductService {

	private IProductDao productDao;
	private IProductTypeDao productTypeDao;

	@Resource(name = "productDao")
	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public void setProductTypeDao(IProductTypeDao productTypeDao) {
		this.productTypeDao = productTypeDao;
	}

	@Override
	public IBaseDao<Product> getBaseDao() {
		return productDao;
	}



	@Override
	protected Boolean beforeSaveCheck(Product t) {
		if (t != null && t.getProductType() != null && t.getProductType().getId() != null) {
			ProductType type = productTypeDao.getById(t.getProductType().getId());
			t.setItem(generateItem(type));
			if (!checkExist(t)) {
				return super.beforeSaveCheck(t);
			}
		} 
		return false;
	}

	/**
	 * @Title:检查是否已经存在此产品
	 * @param t需要进行检查的实体
	 * @return boolean
	 */
	private boolean checkExist(Product t) {
		if (t != null) {
			return productDao.checkExist(t);
		}
		return false;
	}

	@Override
	public List findChild(String string, StringBuffer id) {
		String hql = "from Product p where p." + string + " in(" + id + ")";
		return super.findQuery(hql);
	}

	@Override
	public Product findCode() {
		return (Product) super.findQuery("from Product order by id desc limit 1");
	}

	@Override
	public List<Product> find(String values) {
		String hql = "from Product p where p.fullName like ? or p.fullPy like ?";
		return findQuery(hql, null, new Object[] { "%" + values + "%", "%" + values + "%" });
	}

	@Override
	public String generateItem(ProductType productType) {
		if (productType != null) {
			Product lastProduct = productDao.getLastProduct(productType);
			if (lastProduct == null) {
				return productType.getItem() + String.format("%07d", 1);
			} else {
				String lastItem = lastProduct.getItem().substring(productType.getItem().length());
				if (StringUtils.isBlank(lastItem)) {
					lastItem = "0";
				}
				Integer newItemValue = Integer.valueOf(lastItem) + 1;
				return productType.getItem() + String.format("%07d", newItemValue);
			}
		}
		return null;
	}

	@Override
	public byte[] getImportTemplate(Boolean editProductType) throws IOException {
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet productSheet = workBook.createSheet(PRODUCT_SHEETNAME);
		XSSFSheet typeSheet = createTypeSheet(workBook);
		if(!(true == editProductType)){
			workBook.setSheetHidden(workBook.getSheetIndex(typeSheet), XSSFWorkbook.SHEET_STATE_VERY_HIDDEN);
		}
		XSSFRow titleRow = productSheet.createRow(0);
		List<TransformSetting> transSettings = new TransformSettingFactory().getProductSetting();
		XSSFDataValidationHelper helper = new XSSFDataValidationHelper(productSheet);
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
				Name type = workBook.createName();
				String name = "type";
				type.setNameName(name); //定义名称
				type.setRefersToFormula("OFFSET(" + typeSheet.getSheetName() + "!$A$3,0,0,COUNTA(" + typeSheet.getSheetName() + "!$A:$A)-2)");
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) helper.createFormulaListConstraint(name); //使用定义的名称
				CellRangeAddressList regions = new CellRangeAddressList(1, 10, i, i);
				DataValidation dataValidation = helper.createValidation(constraint, regions);
				productSheet.addValidationData(dataValidation);
			} else if ("productTypeItem".equals(setting.getPropertyName())) { //item是通过查询前面的名称来自动取得
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
//				XSSFDataValidationHelper helper = new XSSFDataValidationHelper(productSheet);
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) helper.createExplicitListConstraint(costNames);
				CellRangeAddressList addressList = new CellRangeAddressList(1, 10, i, i);
				XSSFDataValidation validation = (XSSFDataValidation) helper.createValidation(constraint, addressList);
				validation.setShowErrorBox(true);
				productSheet.addValidationData(validation);
			}
		}
		workBook.setActiveSheet(workBook.getSheetIndex(productSheet));
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		workBook.write(baos);
		return baos.toByteArray();
	}

	private XSSFSheet createTypeSheet(XSSFWorkbook workBook) {
		XSSFSheet typeSheet = workBook.createSheet(PRODUCTTYPE_SHEETNAME);
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
		return typeSheet;
	}

}
