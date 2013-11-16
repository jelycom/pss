/*
 * 捷利商业进销存管理系统
 * @(#)Region.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IRegionDao;
import cn.jely.cd.domain.Region;

/**
 * @ClassName:RegionDaoImpl
 * @Description:DaoImpl
 * @version 2012-11-09 13:48:12 
 *
 */
@Repository("regionDao")
public class RegionDaoImpl extends NestedTreeDaoImpl<Region> implements IRegionDao {

	@Override
	public boolean checkExist(Region region) {
		String hql = "select count(o) from " + entityClass.getName()
				+ " o where  o.name = :name or o.item = :item";
		Map<String, Object> paramValue = new HashMap<String, Object>();
		paramValue.put("name", region.getName());
		paramValue.put("item", region.getItem());
		return countByHql(hql, paramValue) > 0L;
	}

}
