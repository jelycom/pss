/*
 * 捷利商业进销存管理系统
 * @(#)State.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.sys.domain.State;
import cn.jely.cd.sys.service.IStateService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:StateServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-06-03 09:12:39 
 *
 */
public class StateServiceTest extends BaseServiceTest{

	private IStateService stateService;

	@Resource(name = "stateService")
	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 10; i++) {
			State state = new State("State"+i,i%2==0?true:false,i%2==0?true:false); //如果出错请把字符串长度改短
			stateService.save(state);
		}
	}

	@Test
	public void testUpdate() {
		State state = (State) getRandomObject(stateService.getAll());
		String oldstr=state.getStateName();
		state.setStateName("u"+oldstr);
		stateService.update(state);
		State state2 = stateService.getById(state.getId());
		String newstr=state2.getStateName();
		Assert.assertTrue(!oldstr.equals(newstr));
	}


	@Test
	public void testFindPager() {
		
		Pager<State> pager=stateService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}