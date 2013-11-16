/*
 * 捷利商业进销存管理系统
 * @(#)User.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys;

import java.util.List;

import javax.annotation.Resource;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.jely.cd.BaseServiceTest;
import cn.jely.cd.sys.domain.User;
import cn.jely.cd.sys.service.IUserService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:UserServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-10-08 21:14:26
 * 
 */
public class UserServiceTest extends BaseServiceTest {

	private IUserService userService;

	@Resource(name = "userService")
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 10; i++) {
			User user = new User(); // 如果出错请把字符串长度改短
			// User user2 = new User();
			// user2.setId(1l);
			// user.setParent(user2);
			userService.save(user);
		}
	}

	@Test
	public void testUpdate() {
		User user = (User) getRandomObject(userService.getAll());
		String oldstr = user.getName();
		user.setName("u" + oldstr);
		userService.update(user);
		User user2 = userService.getById(user.getId());
		String newstr = user2.getName();
		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testFindPager() {

		Pager<User> pager = userService.findPager(new ObjectQuery());
		System.out.println("pageNo:" + pager.getPageNo());
		System.out.println("pageSize:" + pager.getPageSize());
		System.out.println("datas:" + pager.getRows());
		System.out.println("totalPages:" + pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size() > 0);
	}

	@Test
	public void testDeleteAllUser() {
		List<User> users = userService.getAll();
		for (User user : users) {
			userService.delete(user.getId());

		}
		Assert.assertTrue(userService.getAll().size() == 0);
	}

	@Test
	public void testSetDeleteState() {
		List<User> users = userService.getAll();
		if(users.size()>0){
			String ids = "";
			for (User user : users) {
				ids += user.getId() + ",";
			}
			ids = ids.substring(0, ids.length()-1);
			userService.setDeleteState(ids);
		}
	}

}