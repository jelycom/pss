/*
 * 捷利商业进销存管理系统
 * @(#)Role.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.Role;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IRoleService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:RoleServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-04-05 09:52:39 
 *
 */
public class RoleServiceTest extends BaseServiceTest{

	private IRoleService roleService;
	private IActionResourceService actionResourceService;
	@Resource(name = "roleService")
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	@Resource(name="actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 5; i++) {
			Role role = new Role("role"+i,"角色"+i); //如果出错请把字符串长度改短
			List<ActionResource> resources=actionResourceService.getAll();
			List<ActionResource> userResources=getRandomSubList(resources);
			role.getResources().addAll(userResources);
			roleService.save(role);
		}
	}

	@Test
	public void testUpdate() {
		Role role=(Role) getRandomObject(roleService.getAll());
		String oldstr=role.getName();
		role.setName("u"+role.getName());
		roleService.update(role);
		Role role2 = roleService.findQueryObject(new ObjectQuery().addWhere("name=:name", "name", oldstr));
		Assert.assertNull(role2);
	}

	@Test
	public void testFindPager() {
		Pager<Role> pager=roleService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}