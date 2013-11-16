/*
 * 捷利商业进销存管理系统
 * @(#)User.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jely.cd.dao.impl.BaseDaoImpl;
import cn.jely.cd.sys.dao.IUserDao;
import cn.jely.cd.sys.domain.User;
import cn.jely.cd.util.ArrayConverter;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryRule;

/**
 * @ClassName:UserDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2013-10-08 21:14:26
 * 
 */

public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao {

	@Override
	public User checkUser(String userName, String password) {
		ObjectQuery objectQuery = new ObjectQuery(entityClass);
		List<QueryRule> rules = objectQuery.getQueryGroup().getRules();
		rules.add(new QueryRule("name", FieldOperation.eq, userName));
		rules.add(new QueryRule("password", FieldOperation.eq, password));
		return findObject(objectQuery);
	}

	@Override
	public void setState(String ids, Integer StateId) {
		Map<String,Object> param =  new HashMap<String, Object>();
		param.put("stateId", StateId);
		param.put("ids", ArrayConverter.Strings2Longs(ids.split(",")));
		executeHql("update User u set u.stateId = :stateId where id in(:ids)",param);
	}

}
