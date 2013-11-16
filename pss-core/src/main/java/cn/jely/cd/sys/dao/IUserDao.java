/*
 * 捷利商业进销存管理系统
 * @(#)User.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.dao;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.sys.domain.User;

/**
 * @ClassName:UserAction
 * @Description:Dao
 * @author
 * @version 2013-10-08 21:14:26 
 *
 */
public interface IUserDao extends IBaseDao<User> {

	public User checkUser(String userName,String password);
	
	/**
	 * 将ids列表中的User实体的状态置为删除状态
	 * @param ids
	 */
	public void setState(String ids,Integer StateId);
}
