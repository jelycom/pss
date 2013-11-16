package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.Warehouse;

public interface IWarehouseService extends IBaseService<Warehouse> {
	public List findChild(String string, StringBuffer id);
	List<Warehouse> find(String values);
}
