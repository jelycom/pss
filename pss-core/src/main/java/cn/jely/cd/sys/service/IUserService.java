/*
 * 捷利商业进销存管理系统
 * @(#)User.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service;

import cn.jely.cd.service.IBaseService;
import cn.jely.cd.sys.domain.User;

/**
 * @ClassName:UserService
 * @Description:Service
 * @author
 * @version 2013-10-08 21:14:26 
 *
 */
public interface IUserService extends IBaseService<User> {

	/**
	 * 将ids列表中的User实体的状态置为删除状态
	 * @param ids
	 */
	public void setDeleteState(String ids);
}
