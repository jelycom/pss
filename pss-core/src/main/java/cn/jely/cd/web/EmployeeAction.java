package cn.jely.cd.web;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.LftRgtTreeNodeComparator;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.Role;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ArrayConverter;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.vo.MenuTree;

/**
 * @ClassName:EmployeeAction
 * @Description:用户管理Action
 * @author {周义礼}
 * @date 2012-11-9 上午9:49:30
 * 
 */
@Controller("employeeAction")
@Scope("prototype")
public class EmployeeAction extends JQGridAction<Employee> {
	private static final long serialVersionUID = 1L;
	private Employee employee = new Employee();
	private IEmployeeService employeeService;
//	private IDepartmentService departmentService;
	// private IRoleService roleService;
	private Pager<Employee> pager;
	private String ids;

	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

//	@Resource(name = "departmentService")
//	public void setDepartmentService(IDepartmentService departmentService) {
//		this.departmentService = departmentService;
//	}

	// @Resource(name = "roleService")
	// public void setRoleService(IRoleService roleService) {
	// this.roleService = roleService;
	// }

	public Pager<Employee> getPager() {
		return pager;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Override
	public Employee getModel() {
		return employee;
	}

	@Override
	public String list() {
		logger.debug("Employee list.....");
		// putContext(CONTEXT_DEPTS, departmentService.getAll());
		// putContext(CONTEXT_ROLES, roleService.getAll());
		// pager = employeeService.findPager(objectQuery);
		return SUCCESS;
	}

	@Override
	public String listjson() {
		parseCondition();
		pager = employeeService.findPager(objectQuery);
		actionJsonResult = new ActionJsonResult(pager);
		return JSONALL;
	}

	@Override
	public String listall() {
		actionJsonResult = new ActionJsonResult(employeeService.getAll());
		return JSONALL;
	}

	@Override
	public String save() {
		if (employee.getDepartment() == null || employee.getDepartment().getId() == null
				|| employee.getDepartment().getId() == -1) {
			employee.setDepartment(null);
		}
		// 修改后传过来的ids数组
		if (StringUtils.isNotBlank(ids)) {
			String[] idsStr = ids.split(",");
			Long[] idsLong = ArrayConverter.Strings2Longs(idsStr);
			for (Long idL : idsLong) {
				employee.getUser().getRoles().add(new Role(idL));
			}
		}
		if (StringUtils.isNotBlank(id)) {
			employeeService.update(employee);
		} else {
			employeeService.save(employee);
		}
		actionJsonResult = new ActionJsonResult(employee);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("Employee delete.....");
		// id对应的记录不存在已经在Dao作了处理
		if (id != null) {
			employeeService.delete(id);
		}
		actionJsonResult = new ActionJsonResult(true, null);
		return JSONLIST;// 删除和保存都不需要跳转页面,如需跳转用RELOAD
	}

	/**
	 * 取得用户的资源（包含菜单）
	 * 
	 * @return String
	 */
	public String findemployeeresources() {
		List<ActionResource> resources = findResources();
		actionJsonResult = new ActionJsonResult(resources);
		return JSONLIST;
	}

	private List<ActionResource> findResources() {
		Employee employee = (Employee) getSession().get(LOGIN_USER);
		SystemSetting setting = (SystemSetting) getApplication().get(ConstValue.SYS_SETTING);
		Boolean opened = setting.getCompanyInfo().getOpened();
		List<ActionResource> resources = employeeService.findUserResources(employee.getId(), !opened);
		Collections.sort(resources, new LftRgtTreeNodeComparator());// 按左值排序。
		return resources;
	}

	/**
	 * 取得用户拥有的菜单
	 * 
	 * @return String
	 */
	public String findemployeemenus() {
		List<ActionResource> resources = findResources();
		// List<LftRgtTreeMenu> userMenus = new ArrayList<LftRgtTreeMenu>();
		// for (ActionResource resource : resources) {
		// if (resource.getMenu() != null) {
		// LftRgtTreeMenu userMenu = new LftRgtTreeMenu(resource.getLft(),
		// resource.getRgt(), resource.getDepth(),
		// resource.getMenu().getName(), resource.getLinkAddress());
		// userMenus.add(userMenu);
		// }
		// }
		// Pager<LftRgtTreeMenu> menuPg = new Pager<LftRgtTreeMenu>(1,
		// userMenus.size(), userMenus.size(), 1, userMenus);
		//
		Stack<ActionResource> resourceStack = new Stack<ActionResource>();
		Stack<MenuTree> menuStack = new Stack<MenuTree>();
		ActionResource nestParent = null;
		MenuTree nestMenu = null;
		MenuTree root = null;
		for (ActionResource resource : resources) {
			if (resource.getMenu() != null) {
				MenuTree menu = new MenuTree(resource.getMenu().getName(), resource.getMenu().getUrl());
				if (nestParent == null) {
					nestParent = resource;
					root = menu;
					nestMenu = menu;
				} else {
					nestParent = resourceStack.peek();
					nestMenu = menuStack.peek();
					while (resource.getLft() < nestParent.getLft() || resource.getRgt() > nestParent.getRgt()) {
						resourceStack.pop();
						menuStack.pop();
						nestParent = resourceStack.peek();
						nestMenu = menuStack.peek();
					}
					nestMenu.getChildren().add(menu);
				}
				resourceStack.push(resource);
				menuStack.push(menu);
			}
		}
		// while(!menuStack.empty()){
		// nestParent=menuStack.pop();
		// }
		writejson(root);
		return null;
//		return NONE;
	}

	@Override
	protected void beforInputSave() {
		logger.debug("beforInputSave...");
		if (StringUtils.isNotBlank(id)) {
			employee = employeeService.getById(id);
			employee.setDepartment(null);
			employee.getUser().getRoles().clear();
			// Set<Role> roles = employee.getRoles();
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			} else {
				// 修改前的数据回显准备
				// ids = new Long[roles.size()];
				// int i = 0;
				// for (Role role : roles) {
				// ids[i++] = role.getId();
				// }
			}
		}
	}

	// public String query() throws IOException {
	// List<Employee> employees = employeeService.find(value);
	// for (Employee employee : employees) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("name", employee.getName());
	// map.put("id", employee.getId().toString());
	// list.add(map);
	// }
	// return JSONfUZZY;
	// }

}
