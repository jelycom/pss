/*
 * 捷利商业进销存管理系统
 * @(#)State.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.dao;

import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.sys.domain.State;

/**
 * @ClassName:StateAction
 * @Description:Dao
 * @author
 * @version 2013-03-28 17:22:07 
 *
 */
public interface IStateDao extends IBaseDao<State> {
	List<State> findByCompleteState(boolean completeState);
}
