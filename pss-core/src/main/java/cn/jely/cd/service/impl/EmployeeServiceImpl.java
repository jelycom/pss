package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IEmployeeDao;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.LftRgtTreeNodeComparator;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.sys.dao.IActionResourceDao;
import cn.jely.cd.sys.dao.IUserDao;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.Role;
import cn.jely.cd.sys.domain.User;

@Service("employeeService")
public class EmployeeServiceImpl extends BaseServiceImpl<Employee> implements IEmployeeService {

	private IEmployeeDao employeeDao;
	private IUserDao userDao;
	private IActionResourceDao actionResourceDao;

	public void setActionResourceDao(IActionResourceDao actionResourceDao) {
		this.actionResourceDao = actionResourceDao;
	}

	@Resource(name = "userDao")
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Resource(name = "employeeDao")
	public void setEmployeeDao(IEmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	@Override
	public IBaseDao<Employee> getBaseDao() {
		return employeeDao;
	}

	@Override
	public Employee findEmployee(String name, String password) {
		User user = userDao.checkUser(name, password);
		Employee employee = null;
		if (user != null) {
			employee = employeeDao.findEmployee(user);
		}
		return employee;
	}

	@Override
	public List<Employee> findEmpByNamePy(String values) {
		return employeeDao.findByNamePY(values);
	}

	@Override
	public Set<String> findUserResourceAddresses(Serializable id, Boolean isInit) {
		List<ActionResource> userResources = findUserResources(id, isInit);
		Set<String> addresses = new HashSet<String>();
		for (ActionResource actionResource : userResources) {
			String linkAddress = actionResource.getLinkAddress();
			if (StringUtils.isNotBlank(linkAddress)) {
				addresses.add(linkAddress);
			}
		}
		return addresses;
	}

	// @Override
	// public Set<ActionResource> findUserAllResources(Serializable id) {
	// TreeSet<ActionResource> userResources = findUserResource(id,null);
	// return userResources;
	// }
	@Override
	public List<ActionResource> findUserResources(Serializable id, Boolean isInit) {
		Employee employee = getById(id);
		Set<ActionResource> userResources = new HashSet<ActionResource>();
		List<ActionResource> communityResources = actionResourceDao.findActionResource(isInit, true);// 加入公共的资源
		// userResources.addAll(communityResources);
		User user = employee.getUser();
		if (employee != null && user != null) {
			Set<Role> roles = user.getRoles();
			for (Role role : roles) {
				List<ActionResource> actionResources = role.getResources();
				for (ActionResource actionResource : actionResources) {
					if (communityResources.contains(actionResource)) {// 如果允许访问
						userResources.add(actionResource);// Set用于去除重复的资源。
					}
				}
			}
		}
		List<ActionResource> userResList = new ArrayList<ActionResource>(userResources);
		// TreeSet<ActionResource> userResources = new
		// TreeSet<ActionResource>(new LftRgtTreeNodeComparator());
//		Collections.sort(userResList, new LftRgtTreeNodeComparator());// 按左值排序。
		return userResList;
	}

	// @Override
	// public Set<ActionResource> findUserInitResources(Serializable id){
	// TreeSet<ActionResource> userResources = findUserResource(id,true);
	// return userResources;
	// }
}
