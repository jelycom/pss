/*
 * 捷利商业进销存管理系统
 * @(#)ActionResource.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IEmployeeDao;
import cn.jely.cd.service.NestedTreeServiceImpl;
import cn.jely.cd.sys.dao.IActionResourceDao;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryGroup;
import cn.jely.cd.util.query.QueryRule;

/**
 * @ClassName:ActionResourceServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-04-04 22:57:05
 * 
 */
public class ActionResourceServiceImpl extends NestedTreeServiceImpl<ActionResource> implements IActionResourceService {

	private IActionResourceDao actionResourceDao;
	private IEmployeeDao employeeDao;

	public void setEmployeeDao(IEmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	public void setActionResourceDao(IActionResourceDao actionResourceDao) {
		this.actionResourceDao = actionResourceDao;
	}

	@Override
	public IBaseDao<ActionResource> getBaseDao() {
		return actionResourceDao;
	}

	@Override
	public List<String> findControlActionResourceLinkAddresses() {
		List<ActionResource> allActionResources = actionResourceDao.findActionResource(null, false);
		List<String> allAddresses = getAllAddresses(allActionResources);
		return allAddresses;
	}

	@Override
	public List<String> findInitActionResourceLinkAddresses() {
		List<ActionResource> initActionResources = actionResourceDao.findActionResource(true, null);
		List<String> initAddresses = getAllAddresses(initActionResources);
		return initAddresses;
	}

	@Override
	public List<ActionResource> findInitActionResources() {
		return actionResourceDao.findActionResource(true, null);
	}

	private List<String> getAllAddresses(List<ActionResource> allActionResources) {
		List<String> allAddresses = new ArrayList<String>();
		for (ActionResource actionResource : allActionResources) {
			String linkAddress = actionResource.getLinkAddress();
			if (StringUtils.isNotBlank(linkAddress)) {
				allAddresses.add(linkAddress);
			}
		}
		return allAddresses;
	}

	@Override
	protected Boolean beforeSaveCheck(ActionResource t) {
		return !actionResourceDao.checkExist(t);
	}

	@Override
	public ActionResource findByLinkAddress(String linkAddress) {
		if (linkAddress != null) {
			actionResourceDao.findByLinkAddress(linkAddress);
		}
		return null;
	}

	@Override
	public Pager<ActionResource> findPager(ObjectQuery objectQuery) {
		if (StringUtils.isBlank(objectQuery.getOrderField())) {
			objectQuery.setOrderType(ObjectQuery.ORDERASC);
			objectQuery.setOrderField("lft");
		}
		return super.findPager(objectQuery);
	}

	@Override
	public List<ActionResource> findSysResources() {
		ObjectQuery objectQuery = new ObjectQuery();
		QueryGroup queryGroup = new QueryGroup();
		queryGroup.getRules().add(new QueryRule("linkAddress", FieldOperation.cn, "%.sys.%"));
		objectQuery.setQueryGroup(queryGroup);
		return actionResourceDao.findAll(objectQuery);
	}

}
