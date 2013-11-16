/*
 * 捷利商业进销存管理系统
 * @(#)Producttype.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductTypeDao;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.service.IProductTypeService;
import cn.jely.cd.service.NestedTreeServiceImpl;

@Service("productTypeService")
public class ProductTypeServiceImpl extends NestedTreeServiceImpl<ProductType> implements IProductTypeService {

	private IProductTypeDao productTypeDao;

	@Resource(name = "productTypeDao")
	public void setProductTypeDao(IProductTypeDao productTypeDao) {
		this.productTypeDao = productTypeDao;
	}

	@Override
	public IBaseDao<ProductType> getBaseDao() {
		return productTypeDao;
	}

	
	@Override
	protected Boolean beforeSaveCheck(ProductType t) {
		return !productTypeDao.checkExist(t);
	}
	
	@Override
	public Boolean save(ProductType t, Serializable pid) {
		t.setItem(generateItem(pid == null ? null : productTypeDao.getById(pid)));
		return super.save(t, pid);
	}

	@Override
	public String generateItem(ProductType productType) {
		if (productType != null) {
			ProductType lastChild = (ProductType) productTypeDao.getLastChild(productType);
			if (lastChild == null) {
				return productType.getItem() + String.format("%02d", 1);
			} else {
				String lastItem = lastChild.getItem().substring(productType.getItem().length());
				if (StringUtils.isBlank(lastItem)) {
					lastItem = "0";
				}
				Integer newItemValue = Integer.valueOf(lastItem) + 1;
				return productType.getItem() + String.format("%02d", newItemValue);
			}
		} else if (productTypeDao.getCount(null) == 0) {
			return "01";
		}
		return null;
	}

}
