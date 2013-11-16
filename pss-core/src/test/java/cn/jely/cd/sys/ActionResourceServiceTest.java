/*
 * 捷利商业进销存管理系统
 * @(#)ActionResource.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.springframework.test.annotation.NotTransactional;
import org.testng.annotations.Test;

import cn.jely.cd.BaseServiceTest;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.Menu;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.TreeNode;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ActionResourceServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-04-04 22:57:05 
 *
 */
public class ActionResourceServiceTest extends BaseServiceTest{

	private IActionResourceService actionResourceService;

	@Resource(name = "actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}
	
	@Test(groups="save")
	@NotTransactional
	public void testSave() {
		ActionResource root=new ActionResource("系统资源", "","系统菜单");
		actionResourceService.save(root,null);
		for(int j=0;j<2;j++){
			ActionResource child=new ActionResource("子功能"+j, "","功能1");
			actionResourceService.save(child, root.getId());
			for (int i = 0; i < 1; i++) {
			ActionResource actionResource = new ActionResource("功能"+j+"查看"+i,"cn.jely.cd.sys.web.ActionResourceAction.list"+j+"_"+i); //如果出错请把字符串长度改短
			Menu viewmenu=new Menu("菜单查看"+j+"_"+i,"");
			actionResource.setMenu(viewmenu);
			actionResourceService.save(actionResource,child.getId());
			actionResource = new ActionResource("功能"+j+"保存"+i,"cn.jely.cd.sys.web.ActionResourceAction.save"+j+"_"+i);
			Menu savemenu=new Menu("菜单保存"+j+"_"+i,"");
			actionResource.setMenu(savemenu);
			actionResourceService.save(actionResource,child.getId());
			Menu deletemenu=new Menu("菜单删除"+j+"_"+i,"");
			actionResource.setMenu(deletemenu);
			actionResource = new ActionResource("功能"+j+"删除"+i,"cn.jely.cd.sys.web.ActionResourceAction.delete"+j+"_"+i);
			actionResourceService.save(actionResource,child.getId());
			}
		}
	}

	@Test
	public void testSaveOBP(){
		ActionResource resource = (ActionResource) getRandomObject(actionResourceService.getAll());
		ActionResource child=new ActionResource("人工添加", "cn.jely.cd.web.ProductOrderBillPurchaseAction.list");
		actionResourceService.save(child, resource.getId());
	}
	
	
	@Test(groups="update")
	public void testUpdate() {
		ActionResource resource = (ActionResource) getRandomObject(actionResourceService.getAll());
		String oldstr=resource.getName();
		resource.setName("u"+oldstr);
		actionResourceService.update(resource);
		ActionResource resource2 = actionResourceService.findQueryObject(new ObjectQuery().addWhere("name=:name", "name", oldstr));
		Assert.assertNull(resource2);
	}


	@Test
	public void testGetAllMenu() {
		List<ActionResource> actionResources=actionResourceService.getAll();
		List<ActionResource> menuResources=new ArrayList<ActionResource>();
		for(ActionResource menuResource:actionResources){
			if(menuResource.getMenu()!=null){
				menuResources.add(menuResource);
			}
		}
		Assert.assertTrue(menuResources.size()>0);
		System.out.println(menuResources);
	}
	
	@Test
	public void testFindPager() {
		Pager<ActionResource> pager=actionResourceService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	
	@Test
	public void testFindinitLinkedAddress(){
		List<String> addresses = actionResourceService.findInitActionResourceLinkAddresses();
		printList(addresses);
	}
	
	@Test
	public void testfindInitActionResources(){
		List<ActionResource> resources=actionResourceService.findInitActionResources();
		printList(resources);
	}
	
	@Test
	public void testfindActionResources(){
		List<ActionResource> resources=actionResourceService.findInitActionResources();
		printList(resources);
	}
	@Test
	public void testControlAddress(){
		List<String> addresses = actionResourceService.findControlActionResourceLinkAddresses();
		printList(addresses);
	}
	
	@Test
	public void testfindAllNotCommunityAddress(){
		List<String> addresses = actionResourceService.findControlActionResourceLinkAddresses();
	}
	
	@Test
	public void testFindSysResources(){
		List<ActionResource> resources=actionResourceService.findSysResources();
		printList(resources);
	}
	
	@Test
	public void testToChildModel(){
		List<ActionResource> actionResources=actionResourceService.findInitActionResources();
		TreeNode<ActionResource> resNode=actionResourceService.toChildModel(actionResources);
		System.out.println(resNode);
	}
}