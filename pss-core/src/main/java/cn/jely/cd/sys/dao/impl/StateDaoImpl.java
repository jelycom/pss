/*
 * 捷利商业进销存管理系统
 * @(#)State.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.impl.BaseDaoImpl;
import cn.jely.cd.sys.dao.IStateDao;
import cn.jely.cd.sys.domain.State;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:StateDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2013-03-28 17:22:07 
 *
 */
@Repository("stateDao")
public class StateDaoImpl extends BaseDaoImpl<State> implements IStateDao {

	@Override
	public List<State> findByCompleteState(boolean completeState) {
		return findByNamedParam("from State where completed=:complete", "complete", completeState);
	}
	
}
