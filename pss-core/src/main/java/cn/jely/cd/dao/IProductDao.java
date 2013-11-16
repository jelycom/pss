/*
 * 捷利商业进销存管理系统
 * @(#)Product.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;

/**
 * @ClassName:ProductAction
 * @Description:Dao
 * @author
 * @version 2012-11-30 16:26:19 
 *
 */
public interface IProductDao extends IBaseDao<Product> {
	
	/**
	 * 检查产品是否已经存在
	 * @param product
	 * @return boolean true:已经存在
	 */
	public boolean checkExist(Product product);
	/**根据产品类型取出最后一个编号的产品
	 * @Title:getLastProduct
	 * @param productType
	 * @return Product
	 */
	public Product getLastProduct(ProductType productType);

}
