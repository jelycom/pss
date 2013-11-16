/*
 * 捷利商业进销存管理系统
 * @(#)StateResourceOP.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.State;
import cn.jely.cd.sys.domain.StateResourceOP;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.sys.service.IStateService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:StateResourceOPServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-05-31 17:20:10 
 *
 */
public class StateResourceOPServiceTest extends BaseServiceTest{

	private IStateResourceOPService stateResourceOPService;
	private IActionResourceService actionResourceService;
	private IStateService stateService;
	
	@Resource(name = "stateResourceOPService")
	public void setStateResourceOPService(IStateResourceOPService stateResourceOPService) {
		this.stateResourceOPService = stateResourceOPService;
	}
	
	@Resource(name="actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}

	@Resource(name="stateService")
	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}


	@Test
	public void testSave() {
		List<ActionResource> resources=actionResourceService.getAll();
		List<State> states=stateService.getAll();
		ActionResource userResource = null;
		for(ActionResource resource:resources){
			if("cn.jely.cd.web.ProductOrderBillPurchaseAction.list".equalsIgnoreCase(resource.getLinkAddress())){
				userResource=resource;
				break;
			}
		}
		for (int i = 0; i < 10; i++) {
			StateResourceOP stateResourceOP = new StateResourceOP(userResource,(State)getRandomObject(states),(ActionResource) getRandomObject(resources),"OP"+i); //如果出错请把字符串长度改短
			stateResourceOPService.save(stateResourceOP);
		}
	}

	@Test
	public void testUpdate() {
		StateResourceOP stateResourceOP = (StateResourceOP) getRandomObject(stateResourceOPService.getAll());
		String oldstr=stateResourceOP.getOpName();
		stateResourceOP.setOpName("u"+oldstr);
		stateResourceOPService.update(stateResourceOP);
		StateResourceOP stateResourceOP2 = stateResourceOPService.getById(stateResourceOP.getId());
		String newstr=stateResourceOP2.getOpName();
		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testFindPager() {
		Pager<StateResourceOP> pager=stateResourceOPService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}


}