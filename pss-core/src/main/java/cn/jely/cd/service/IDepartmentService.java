package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.Department;

public interface IDepartmentService extends IBaseService<Department> {
	List findChild(String string, String id);
	List<Department> find(String values);
}
