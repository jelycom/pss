package cn.jely.cd.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IDepartmentDao;
import cn.jely.cd.domain.Department;

@Repository("departmentDao")
public class DepartmentDaoImpl extends BaseDaoImpl<Department> implements IDepartmentDao {

}
