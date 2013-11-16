package cn.jely.cd.dao;

import java.util.List;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.User;

public interface IEmployeeDao extends IBaseDao<Employee> {

	/**
	 * 根据用户名及密码查找用户
	 * @param user
	 * @return Employee
	 */
	public Employee findEmployee(User user);
	/**
	 *  根据姓名及拼音查找用户
	 * @param values
	 * @return List<Employee>
	 */
	public List<Employee> findByNamePY(String values);
	
	/**
	 * 查询用户所拥有的资源权限
	 * @param employee TODO
	 * @return List<ActionResource>
	 */
	public List<ActionResource> findUserActionResources(Employee employee);

}
