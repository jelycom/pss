/*
 * 捷利商业进销存管理系统
 * @(#)Product.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.io.IOException;
import java.util.List;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;

/**
 * @ClassName:ProductService
 * @Description:Service
 * @author
 * @version 2012-11-30 16:26:19 
 *
 */
public interface IProductService extends IBaseService<Product> {
	public static final String PRODUCT_SHEETNAME="product";
	public static final String PRODUCTTYPE_SHEETNAME="productType";
	public List findChild(String string, StringBuffer id);
	public Product findCode();
	List<Product> find(String values);
	/**
	 * 根据产品类型生成产品的编号
	 * @Title:generateItem
	 * @param productType 产品所属的类别
	 * @return String 生成的产品编号
	 */
	public String generateItem(ProductType productType);
	/**
	 * 取得产品导入的模板文件
	 * @param editProductType 是否同时修改产品类别
	 * @return TODO
	 * @throws IOException 
	 */
	public byte[] getImportTemplate(Boolean editProductType) throws IOException;
	
//	public OutputStream getImportTemplate(Boolean editProductType) throws IOException;
}
