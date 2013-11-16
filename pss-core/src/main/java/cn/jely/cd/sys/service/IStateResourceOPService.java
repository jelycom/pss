/*
 * 捷利商业进销存管理系统
 * @(#)StateResourceOP.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service;

import java.util.List;

import cn.jely.cd.service.IBaseService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.State;
import cn.jely.cd.sys.domain.StateResourceOP;

/**
 * @ClassName:StateResourceOPService
 * @Description:Service
 * @author
 * @version 2013-05-31 17:20:10 
 *
 */
public interface IStateResourceOPService extends IBaseService<StateResourceOP> {
	/**
	 * 根据State查询资源的可用操作
	 * @Title:getStateResourceOPs
	 * @param state
	 * @return List<StateResourceOP>
	 */
	List<StateResourceOP> getStateResourceOPs(ActionResource resource,State state); 
	
	/**
	 * 根据State状态查询可用的操作
	 * @Title:getStateSequence
	 * @param state
	 * @return List<State>
	 */
	List<ActionResource> getOPList(ActionResource resource,State state);
	
	/**根据资源实体查询出对应的状态列表.
	 * @Title:getStateSequence
	 * @param srop
	 * @return List<State>
	 */
	List<State> getStateSequence(ActionResource resource);
}
