/*
 * 捷利商业进销存管理系统
 * @(#)Querydata.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao;

import java.util.List;

import cn.jely.cd.domain.Querydata;

/**
 * @ClassName:QuerydataAction
 * @Description:Dao
 * @author
 * @version 2013-03-01 15:09:02 
 *
 */
public interface IQuerydataDao extends IBaseDao<Querydata> {
	/**
	 * 根据模块名称取得保存的查询条件
	 * @param actionName
	 * @return List<Querydata>
	 */
	public List<Querydata> findByActionName(String actionName); 
}
