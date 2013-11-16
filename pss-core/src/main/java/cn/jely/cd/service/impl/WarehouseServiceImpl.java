package cn.jely.cd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IWarehouseDao;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

@Service("warehouseService")
public class WarehouseServiceImpl extends BaseServiceImpl<Warehouse> implements
		IWarehouseService {

	private IWarehouseDao warehouseDao;
	@Resource(name="warehouseDao")
	public void setWarehouseDao(IWarehouseDao warehouseDao) {
		this.warehouseDao = warehouseDao;
	}

	@Override
	public IBaseDao<Warehouse> getBaseDao() {

		return warehouseDao;
	}

	@Override
	public List findChild(String string, StringBuffer id) {
		String value = id.substring(0, id.length() - 1);
		String hql = "from Warehouse w where w."+string+" in("+value+")";
		return super.findQuery(hql);
	}

	@Override
	public List<Warehouse> find(String values) {
		String hql = "from Warehouse w where w.name like ? or w.py like ?";
		return super.findQuery(hql,null, new Object[] {
				"%" + values + "%", "%" + values + "%" });
	}


}
