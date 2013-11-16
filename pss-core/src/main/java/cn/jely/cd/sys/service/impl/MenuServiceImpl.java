/*
 * 捷利商业进销存管理系统
 * @(#)Menu.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IMenuDao;
import cn.jely.cd.sys.domain.Menu;
import cn.jely.cd.sys.service.IMenuService;

/**
 * @ClassName:MenuServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-04-08 10:14:43 
 *
 */
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements
		IMenuService {

	private IMenuDao menuDao;

	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
	}

	@Override
	public IBaseDao<Menu> getBaseDao() {
		return menuDao;
	}


}
