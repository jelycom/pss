/*
 * 捷利商业进销存管理系统
 * @(#)ActionResource.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.service;

import java.util.List;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.service.IBaseTreeService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.util.TreeNode;

/**
 * @ClassName:ActionResourceService
 * @Description:Service
 * @author
 * @version 2013-04-04 22:57:05 
 *
 */
public interface IActionResourceService extends IBaseTreeService<ActionResource> {
	/**
	 * 查询所有资源地址
	 * @return List<String>
	 */
	List<String> findControlActionResourceLinkAddresses();
	/**
	 * 查找所有初始化所需要的资源
	 * @return List<String>
	 */
	List<String> findInitActionResourceLinkAddresses();
	/**
	 * 根据地址查询系统资源
	 * @Title:findByLinkAddress
	 * @param linkAddress
	 * @return ActionResource
	 */
	ActionResource findByLinkAddress(String linkAddress); 
	/**
	 * 查询系统管理下的所有资源
	 * @return List<ActionResource>
	 */
	List<ActionResource> findSysResources();
	
	/**
	 * 查询系统开帐、初始化所需的资源
	 * @return List<ActionResource>
	 */
	List<ActionResource> findInitActionResources();
	
//	/**
//	 * 根据是否已经开帐返回用户所拥有的资源列表
//	 * @param employee 查询的用户
//	 * @param isOpen 是否开帐
//	 * @return List<ActionResource>
//	 */
//	List<ActionResource> findUserActionResources(Employee employee,Boolean isOpen);
//	
	}
