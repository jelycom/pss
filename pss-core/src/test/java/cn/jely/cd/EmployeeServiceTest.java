package cn.jely.cd;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.jely.cd.domain.Department;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.service.IDepartmentService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.sys.domain.Role;
import cn.jely.cd.sys.domain.User;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IRoleService;
import cn.jely.cd.sys.service.IUserService;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

public class EmployeeServiceTest extends BaseServiceTest {

	private IDepartmentService departmentService;
	private IEmployeeService employeeService;
	private IRoleService roleService;
	private IUserService userService;
	private IActionResourceService actionResourceService;

	@Resource(name = "userService")
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Resource(name = "departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@Resource(name = "roleService")
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	@Resource(name = "actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}

	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Test
	public void testSaveAdmin() {
		Employee employee = new Employee("超级管理员");
		User user = new User("admin","admin");
		employee.setUser(user);
//		userService.save(user);
		employeeService.save(employee);
	}

	@Test
	public void testupdate(){
		Employee emp = employeeService.findEmployee("admin", "admin");
		emp.setUser(null);
		employeeService.update(emp);
	}
	
	@Test
	public void testSave() {
		for (int i = 0; i < 4; i++) {
			Department department = (Department) getRandomObject(departmentService.getAll());
			for (int j = 0; j < 2; j++) {
				Employee employee = new Employee(department, "D" + i + "emp" + j);
				List<Role> userroles = getRandomSubList(roleService.getAll());
				/*
				 * for(Role role:userroles){ List<ActionResource>
				 * userresources=getSubList(actionResourceService.getAll());
				 * role.getResources().addAll(userresources); }
				 */
				if(userroles!=null&&userroles.size()>0){
					employee.getUser().getRoles().addAll(userroles);
				}
				employeeService.save(employee);
			}
		}
	}

	@Test
	public void testUpdate() {
		Employee employee = (Employee) getRandomObject(employeeService.getAll());
		String oldstr = employee.getName();
		employee.setName("u" + oldstr);
		employee.getUser().setRoles(new HashSet(getRandomSubList(roleService.getAll())));
		employeeService.update(employee);
		ObjectQuery objectQuery = new ObjectQuery();
		Employee emp2 = employeeService.findQueryObject(objectQuery.addWhere("name=:name", "name", oldstr));
		Assert.assertNull(emp2);
	}

	@Test
	public void testGetAll() {
		List<Employee> employees = employeeService.getAll();
		int oldsize = employees.size();
		Assert.assertTrue(oldsize > 0);
	}

	@Test
	public void testFindPager() {
		ObjectQuery employeeQuery = new ObjectQuery();
		employeeQuery.addWhere("department.name like :dept", "dept","%dept2%");
//		employeeQuery.setSearchField("department.name");
//		employeeQuery.setSearchOper("cn");
//		employeeQuery.setSearchString("%dept2%");
		// employeeQuery.setOrderType("desc");
		// employeeQuery.setOrderField("id");
		Pager<Employee> pager = employeeService.findPager(employeeQuery);
		System.out.println("pageNo:" + pager.getPageNo());
		System.out.println("pageSize:" + pager.getPageSize());
		System.out.println("datas:" + pager.getRows());
		for (Employee employee : pager.getRows()) {
			System.out.println(employee.getId() + ":" + employee.getName());
		}
		System.out.println("totalPages:" + pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size() > 0);
	}

	@Test
	public void testDBTime() {
		System.out.println(DateFormat.getDateInstance().format(new Date(employeeService.getDBTime().getTime())));
	}

	@Test
	public void testFindEmployee() {
		Employee employee = employeeService.findEmployee("admin", "admin");
		Assert.assertNotNull(employee);
	}
	@Test(groups="find")
	public void testGetUserResources(){
		Employee employee = employeeService.findEmployee("user", "user");
		Set<String> userAddresses = employeeService.findUserResourceAddresses(employee.getId(), true);
		System.out.println(userAddresses.size());
		printList(new ArrayList<Object>(userAddresses));
	}
}