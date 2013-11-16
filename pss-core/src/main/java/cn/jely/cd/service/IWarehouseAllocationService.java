package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.WarehouseAllocation;

public interface IWarehouseAllocationService extends IBaseService<WarehouseAllocation> {
	public List findChild(String id);
}
