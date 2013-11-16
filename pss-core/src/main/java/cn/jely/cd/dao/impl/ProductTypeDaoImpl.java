/*
 * 捷利商业进销存管理系统
 * @(#)Producttype.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductTypeDao;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;

/**
 * @ClassName:ProducttypeDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2012-11-30 14:56:06 
 *
 */
@Repository("producttypeDao")
public class ProductTypeDaoImpl extends NestedTreeDaoImpl<ProductType> implements IProductTypeDao {

	@Override
	public boolean checkExist(ProductType productType) {
		String hql = "select count(o) from " + entityClass.getName()
				+ " o where  o.name = :name or o.item = :item";
		Map<String, Object> paramValue = new HashMap<String, Object>();
		paramValue.put("name", productType.getName());
		paramValue.put("item", productType.getItem());
		return countByHql(hql, paramValue) > 0L;
	}

	@Override
	public List<Serializable> findChildIds(Serializable productTypeId,boolean includeself) {
		List<Serializable> typeIds = new ArrayList<Serializable>();
		if (productTypeId != null) {
			List<ProductType> types = new ArrayList<ProductType>();
			types = findTreeNodes(productTypeId, true);
			for (ProductType type : types) {
				typeIds.add(type.getId());
			}
		} 
		return typeIds;
	}
	
	
}
