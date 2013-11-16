/*
 * 捷利商业进销存管理系统
 * @(#)User.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IUserDao;
import cn.jely.cd.sys.domain.User;
import cn.jely.cd.sys.service.IUserService;
import cn.jely.cd.util.ArrayConverter;

/**
 * @ClassName:UserServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-10-08 21:14:26 
 *
 */
public class UserServiceImpl extends BaseServiceImpl<User> implements
		IUserService {

	private IUserDao userDao;

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public IBaseDao<User> getBaseDao() {
		return userDao;
	}

	@Override
	public void setDeleteState(String ids) {
		userDao.setState(ids, User.DELETE);
	}

}
