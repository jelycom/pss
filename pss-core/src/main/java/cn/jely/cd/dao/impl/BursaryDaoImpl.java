/*
 * 捷利商业进销存管理系统
 * @(#)Bursary.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jely.cd.dao.IBursaryDao;
import cn.jely.cd.domain.Bursary;
import cn.jely.cd.domain.Region;

/**
 * @ClassName:BursaryDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2013-08-05 10:50:45 
 *
 */

public class BursaryDaoImpl extends NestedTreeDaoImpl<Bursary> implements IBursaryDao {

	@Override
	public boolean checkExist(Bursary bursary) {
		String hql = "select count(o) from " + entityClass.getName()
				+ " o where  o.fullName = :fullName or o.shortName = :shortName or o.item = :item";
		Map<String, Object> paramValue = new HashMap<String, Object>();
		paramValue.put("fullName", bursary.getFullName());
		paramValue.put("shortName", bursary.getShortName());
		paramValue.put("item", bursary.getItem());
		return countByHql(hql, paramValue) > 0L;
	}

	@Override
	public Bursary findByItem(String item) {
		@SuppressWarnings("unchecked")
		List<Bursary> bursaries=findByNamedParam("from "+entityClass.getName()+" o where o.item=:item", "item", item);
		if(bursaries!=null&&bursaries.size()>0){
			return bursaries.get(0);
		}
		return null;
	}
	
	
}
