package cn.jely.cd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IWarehouseAllocationDao;
import cn.jely.cd.domain.WarehouseAllocation;
import cn.jely.cd.service.IWarehouseAllocationService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

@Service("warehouseAllocationService")
public class WarehouseAllocationServiceImpl extends BaseServiceImpl<WarehouseAllocation> implements
		IWarehouseAllocationService {

	private IWarehouseAllocationDao warehouseAllocationDao;
	@Resource(name="warehouseAllocationDao")
	public void setWarehouseAllocationDao(IWarehouseAllocationDao warehouseAllocationDao) {
		this.warehouseAllocationDao = warehouseAllocationDao;
	}

	@Override
	public IBaseDao<WarehouseAllocation> getBaseDao() {

		return warehouseAllocationDao;
	}

	@Override
	public List findChild(String id) {
		String hql = "from WarehouseAllocation w where w.warehouse.id = " + id;
		return super.findQuery(hql);
	}


}
