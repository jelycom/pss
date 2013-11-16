/*
 * 捷利商业进销存管理系统
 * @(#)Product.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2012-11-30 16:26:19
 * 
 */
@Repository("productDao")
public class ProductDaoImpl extends BaseDaoImpl<Product> implements IProductDao {

	@Override
	public Product getLastProduct(ProductType productType) {
		// StringBuilder hql=new StringBuilder();
		// hql.append("o.productType=? order by o.item desc");
		ObjectQuery objectQuery = new ObjectQuery();
		objectQuery.addWhere("productType=:type order by item desc", "type", productType);
		return findObject(objectQuery);
	}

	@Override
	public boolean checkExist(Product product) {
		//TODO:根据系统设置来确定允许什么不能重复，如有可能同一个产品有多个型号，每个型号有多种颜色等。
		String hql = "select count(o) from " + entityClass.getName()
				+ " o where  o.fullName = :fullName or o.item = :item";
		Map<String, Object> paramValue = new HashMap<String, Object>();
		paramValue.put("fullName", product.getFullName());
		paramValue.put("item", product.getItem());
		return countByHql(hql, paramValue) > 0L;
	}

}
