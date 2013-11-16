/*
 * 捷利商业进销存管理系统
 * @(#)State.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IStateDao;
import cn.jely.cd.sys.domain.State;
import cn.jely.cd.sys.service.IStateService;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:StateServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-03-28 17:22:07 
 *
 */
@Service("stateService")
public class StateServiceImpl extends BaseServiceImpl<State> implements
		IStateService {

	private IStateDao stateDao;
	@Resource(name="stateDao")
	public void setStateDao(IStateDao stateDao) {
		this.stateDao = stateDao;
	}

	@Override
	public IBaseDao<State> getBaseDao() {

		return stateDao;
	}

	@Override
	public State getByCode(String id) {
		return null;//findQueryObject(new ObjectQuery(" o.stateCode = :code ", id));
	}


}
