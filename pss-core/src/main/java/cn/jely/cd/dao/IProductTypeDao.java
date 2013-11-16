/*
 * 捷利商业进销存管理系统
 * @(#)Producttype.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao;

import java.io.Serializable;
import java.util.List;

import cn.jely.cd.domain.ProductType;

/**
 * @ClassName:ProducttypeAction
 * @Description:Dao
 * @author
 * @version 2012-11-30 14:56:06 
 *
 */
public interface IProductTypeDao extends IBaseTreeDao<ProductType> {
	/**
	 * 检查产品类别是否已经存在
	 * @param productType
	 * @return boolean true:已经存在
	 */
	public boolean checkExist(ProductType productType);
	public List<Serializable> findChildIds(Serializable productTypeId,boolean includeself);
}
