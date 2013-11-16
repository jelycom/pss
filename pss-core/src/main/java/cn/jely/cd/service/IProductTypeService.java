/*
 * 捷利商业进销存管理系统
 * @(#)Producttype.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import cn.jely.cd.domain.ProductType;

/**
 * @ClassName:ProducttypeService
 * @Description:Service
 * @author
 * @version 2012-11-30 14:56:06 
 *
 */
public interface IProductTypeService extends IBaseTreeService<ProductType> {
	/**
	 * @Title:generateItem
	 * @param productType 根据父类生成子类的编号
	 * @return String 生成的子类编号
	 */
	public String generateItem(ProductType productType);
//	public List findChild(String string, StringBuffer id);
}
