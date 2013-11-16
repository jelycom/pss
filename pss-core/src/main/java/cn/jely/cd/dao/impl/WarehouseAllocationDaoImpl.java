package cn.jely.cd.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IWarehouseAllocationDao;
import cn.jely.cd.domain.WarehouseAllocation;

@Repository("warehouseAllocationDao")
public class WarehouseAllocationDaoImpl extends BaseDaoImpl<WarehouseAllocation> implements IWarehouseAllocationDao {

}
