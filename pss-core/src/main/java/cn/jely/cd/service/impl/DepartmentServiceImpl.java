package cn.jely.cd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IDepartmentDao;
import cn.jely.cd.domain.Department;
import cn.jely.cd.service.IDepartmentService;

@Service("departmentService")
public class DepartmentServiceImpl extends BaseServiceImpl<Department>
		implements IDepartmentService {

	private IDepartmentDao departmentDao;

	@Resource(name = "departmentDao")
	public void setDepartmentDao(IDepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	@Override
	public IBaseDao<Department> getBaseDao() {
		return departmentDao;
	}

	private void prepareSaveUpdate(Department t) {
		if (t.getParent() != null && (t.getParent().getId() == null || t.getParent().getId() == -1)) {
			t.setParent(null);
		}
		if (t.getManager() != null && (t.getManager().getId() == null || t.getManager().getId() == -1)) {
			t.setManager(null);
		}
	}

	@Override
	public Long save(Department t) {
		prepareSaveUpdate(t);
		return super.save(t);
	}

	@Override
	public void update(Department t) {
		prepareSaveUpdate(t);
		super.update(t);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List findChild(String string, String id) {
		String hql = "from Department d where d." + string + " = " + id;
		return super.findQuery(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Department> find(String values) {
		String hql = "from Department d where d.name like ? or d.py like ?";
		return findQuery(hql, null, new Object[] { "%" + values + "%",
				"%" + values + "%" });
	}

}
