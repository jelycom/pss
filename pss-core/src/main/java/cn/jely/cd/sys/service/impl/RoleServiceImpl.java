/*
 * 捷利商业进销存管理系统
 * @(#)Role.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IRoleDao;
import cn.jely.cd.sys.domain.Role;
import cn.jely.cd.sys.service.IRoleService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:RoleServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-04-05 09:52:39 
 *
 */
public class RoleServiceImpl extends BaseServiceImpl<Role> implements
		IRoleService {

	private IRoleDao roleDao;

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public IBaseDao<Role> getBaseDao() {
		return roleDao;
	}


}
