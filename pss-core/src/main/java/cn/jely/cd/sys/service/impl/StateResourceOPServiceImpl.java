/*
 * 捷利商业进销存管理系统
 * @(#)StateResourceOP.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IStateResourceOPDao;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.State;
import cn.jely.cd.sys.domain.StateResourceOP;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:StateResourceOPServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-05-31 17:20:10 
 *
 */
public class StateResourceOPServiceImpl extends BaseServiceImpl<StateResourceOP> implements
		IStateResourceOPService {

	private IStateResourceOPDao stateResourceOPDao;

	public void setStateResourceOPDao(IStateResourceOPDao stateResourceOPDao) {
		this.stateResourceOPDao = stateResourceOPDao;
	}

	@Override
	public IBaseDao<StateResourceOP> getBaseDao() {
		return stateResourceOPDao;
	}

	@Override
	public List<StateResourceOP> getStateResourceOPs(ActionResource resource,State state) {
//		List<StateResourceOP> srops=stateResourceOPDao.findAll(new ObjectQuery("o.actionResource=? and o.state=?", resource,state));
		return null;
	}

	@Override
	public List<ActionResource> getOPList(ActionResource resource, State state) {
		List<StateResourceOP> srops=getStateResourceOPs(resource, state);
		List<ActionResource> OPList=new ArrayList<ActionResource>();
		for(StateResourceOP srop:srops){
			ActionResource OPResource=srop.getOpResource();
			OPList.add(OPResource);
		}
		return OPList;
	}

	@Override
	public List<State> getStateSequence(ActionResource resource) {
		List<StateResourceOP> srops=new ArrayList<StateResourceOP>();//stateResourceOPDao.findAll(new ObjectQuery("o.actionResource=?", resource));
		List<State> states=new ArrayList<State>();
		for(StateResourceOP srop:srops){
			State state = srop.getState();
			if(!states.contains(state)){
				states.add(state);
			}
		}
		return states;
	}


}
