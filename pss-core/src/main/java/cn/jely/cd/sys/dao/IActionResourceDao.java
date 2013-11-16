/*
 * 捷利商业进销存管理系统
 * @(#)ActionResource.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.dao;

import java.util.List;

import cn.jely.cd.dao.IBaseTreeDao;
import cn.jely.cd.sys.domain.ActionResource;

/**
 * @ClassName:ActionResourceAction
 * @Description:Dao
 * @author
 * @version 2013-04-04 22:57:05 
 *
 */
public interface IActionResourceDao extends IBaseTreeDao<ActionResource> {
	/**
	 * 检查是否存在相同的资源地址
	 * @param actionResource
	 * @return boolean true:已经存在
	 */
	public boolean checkExist(ActionResource actionResource);
	
	/**
	 * 根据链接地址查询资源
	 * @param linkAddress
	 * @return ActionResource
	 */
	public ActionResource findByLinkAddress(String linkAddress);
	
	/**
	 * 取得所有的资源地址，
	 * @param init 是否只包含初始化地址,true:只包含初始化地址，false:只查询非初始化地址，null:不关心是否初始化
	 * @param community 是否查询公共资源,true:查询是公共资源的，false：查询不是公共资源的,null:不以是否公共资源为条件
	 * @return List<String>
	 */
	public List<ActionResource> findActionResource(Boolean init, Boolean community);
}
