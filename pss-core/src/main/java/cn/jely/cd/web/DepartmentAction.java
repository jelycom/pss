package cn.jely.cd.web;

import static cn.jely.cd.util.ConstValue.CONTEXT_DEPARTMENT_MANAGERS;
import static cn.jely.cd.util.ConstValue.CONTEXT_DEPTS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.Department;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.service.IDepartmentService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
@Controller("departmentAction")
@Scope("prototype")
public class DepartmentAction extends JQGridAction<Department> {
	private static final long serialVersionUID = 1L;
	private Department department;
	private IDepartmentService departmentService;
	private IEmployeeService employeeService;
	private Pager<Department> pager;
	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	@Resource(name="departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Resource(name="employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}
	
	public Pager<Department> getPager() {
		return pager;
	}
	
	@Override
	public Department getModel() {
		return department;
	}

	@Override
	public String list() {
		logger.debug("Department list.....");
//		putContext(CONTEXT_DEPARTMENT_MANAGERS, employeeService.getAll());
//		pager=departmentService.findPager(objectQuery);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String listjson() {
		putContext(CONTEXT_DEPARTMENT_MANAGERS, employeeService.getAll());
		pager=departmentService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}
	
	public String listall(){
		actionJsonResult=new ActionJsonResult(departmentService.getAll());
		return JSONALL;
	}

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="必须输入名称")})
	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("Department save.....");
		if (department!=null&&department.getId()!=null) {
			departmentService.update(department);
		}else{
			departmentService.save(department);
		}
		actionJsonResult=new ActionJsonResult(department);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("Department delete.....");
		//id对应的记录不存在已经在Dao作了处理
		List<Department> departments = departmentService.findChild("parent.id",id);
		if (StringUtils.isNotBlank(id) && departments.size() < 1) {
			departmentService.delete(id);
			actionJsonResult=new ActionJsonResult(true, null);
		}
		else {
			actionJsonResult=new ActionJsonResult(false, "有下级部门，不能删除");
		}
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				department =new Department();
			}
		}else{
			department=departmentService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				department.setParent(null);
				department.setManager(null);
			}
		}
	}

	@Override
	public String input() {
		logger.debug("Department input.....");
		//将需要在页面展示的关联对象放入Context；
		putContext(CONTEXT_DEPARTMENT_MANAGERS, employeeService.getAll());
		putContext(CONTEXT_DEPTS,departmentService.getAll());
		return INPUT;
	}

}
