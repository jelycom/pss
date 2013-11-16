/*
 * 捷利商业进销存管理系统
 * @(#)State.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.service;

import cn.jely.cd.service.IBaseService;
import cn.jely.cd.sys.domain.State;

/**
 * @ClassName:StateService
 * @Description:Service
 * @author
 * @version 2013-03-28 17:22:07 
 *
 */
public interface IStateService extends IBaseService<State> {
	State getByCode(String id);
}
