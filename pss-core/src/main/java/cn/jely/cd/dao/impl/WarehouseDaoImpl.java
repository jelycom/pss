package cn.jely.cd.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IWarehouseDao;
import cn.jely.cd.domain.Warehouse;

@Repository("warehouseDao")
public class WarehouseDaoImpl extends BaseDaoImpl<Warehouse> implements IWarehouseDao {

}
