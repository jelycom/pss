package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.BaseData;

public interface IBaseDataService extends IBaseService<BaseData> {

	List<BaseData> getAllModels();
	List<BaseData> getAllOperations();
	List<BaseData> getAllSupplierType();
	List<BaseData> getAllSupplierLevel();
	List<BaseData> getAllCustomerType();
	List<BaseData> getAllCustomerLevel();
	List<BaseData> getAllInvoiceType();
	List<BaseData> getAllProductBrand();
}
