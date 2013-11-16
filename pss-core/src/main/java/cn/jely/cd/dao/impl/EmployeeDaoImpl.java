package cn.jely.cd.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IEmployeeDao;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.User;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryGroup;
import cn.jely.cd.util.query.QueryRule;

@Repository("employeeDao")
public class EmployeeDaoImpl extends BaseDaoImpl<Employee> implements IEmployeeDao {
	
	@Override
	public Employee findEmployee(User user) {
		ObjectQuery objectQuery=new ObjectQuery(entityClass);
		List<QueryRule> rules = objectQuery.getQueryGroup().getRules();
		rules.add(new QueryRule("user",FieldOperation.eq,user));
		return findObject(objectQuery);
	}

	@Override
	public List<Employee> findByNamePY(String values) {
		ObjectQuery objectQuery=new ObjectQuery();
		QueryGroup queryGroup = objectQuery.getQueryGroup();
		queryGroup.setGroupOp(QueryGroup.OR);
		List<QueryRule> rules = queryGroup.getRules();
		rules.add(new QueryRule("name", FieldOperation.cn, "%"+values+"%"));
		rules.add(new QueryRule("py", FieldOperation.cn, "%"+values+"%"));
//		objectQuery.addWhere("name like :value or py like :value", "value", values);
		return findAll(objectQuery);
	}

	@Override
	public List<ActionResource> findUserActionResources(Employee employee) {
		String hql="";
		return null;
	}
}
