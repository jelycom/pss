package cn.jely.cd.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.sys.domain.ActionResource;

public interface IEmployeeService extends IBaseService<Employee> {

	/**
	 * 登录检查如果用户名和密码正确则返回此用户，否则返回null。
	 * @param name
	 * @param password
	 * @return Employee
	 */
	Employee findEmployee(String name, String password);
	/**
	 * 查询用户所拥有的资源地址，
	 * @param id
	 * @param isInit 是否期初状态，true:只返回此用户期初开帐相关的资源，false:返回开帐后的资源地址。
	 * @return Set<String>
	 */
	Set<String> findUserResourceAddresses(Serializable id, Boolean isInit);
	/**
	 * 根据用户名或拼音进行搜索。
	 * @param value 
	 * @return List<Employee>
	 */
	List<Employee> findEmpByNamePy(String value);
	/**
	 * 查询此用户拥有的所有资源，包括公共资源
	 * @param id
	 * @return Set<ActionResource>
	 */
	List<ActionResource> findUserResources(Serializable id,Boolean isInit);
//	/**
//	 * 根据用户id查询所有可访问的资源
//	 * @param id 用户id
//	 * @return Set<ActionResource>
//	 */
//	Set<ActionResource> findUserInitResources(Serializable id);
}
