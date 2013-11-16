/*
 * 捷利商业进销存管理系统
 * @(#)Querydata.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IQuerydataDao;
import cn.jely.cd.domain.Querydata;
import cn.jely.cd.service.IQuerydataService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:QuerydataServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-03-01 15:09:02 
 *
 */
@Service("querydataService")
public class QuerydataServiceImpl extends BaseServiceImpl<Querydata> implements
		IQuerydataService {

	private IQuerydataDao querydataDao;
	@Resource(name="querydataDao")
	public void setQuerydataDao(IQuerydataDao querydataDao) {
		this.querydataDao = querydataDao;
	}

	@Override
	public IBaseDao<Querydata> getBaseDao() {

		return querydataDao;
	}

	@Override
	public List<Querydata> loadFilter(String app) {
		return querydataDao.findByActionName(app);
	}


}
