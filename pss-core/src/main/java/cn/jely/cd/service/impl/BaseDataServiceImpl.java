package cn.jely.cd.service.impl;

import static cn.jely.cd.util.ConstValue.BASEDATA_APPLICATIONMODEL;
import static cn.jely.cd.util.ConstValue.BASEDATA_OPERATION;
import static cn.jely.cd.util.ConstValue.BASE_CUSTOMER_LEVEL;
import static cn.jely.cd.util.ConstValue.BASE_CUSTOMER_TYPE;
import static cn.jely.cd.util.ConstValue.BASE_INVOICE_TYPE;
import static cn.jely.cd.util.ConstValue.BASE_PRODUCT_BRAND;
import static cn.jely.cd.util.ConstValue.BASE_SUPPLIER_LEVEL;
import static cn.jely.cd.util.ConstValue.BASE_SUPPLIER_TYPE;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBaseDataDao;
import cn.jely.cd.domain.BaseData;
import cn.jely.cd.service.IBaseDataService;

@Service("baseDataService")
public class BaseDataServiceImpl extends BaseServiceImpl<BaseData> implements
		IBaseDataService {

	private IBaseDataDao baseDataDao;
	@Resource(name="baseDataDao")
	public void setBaseDataDao(IBaseDataDao baseDataDao) {
		this.baseDataDao = baseDataDao;
	}

	@Override
	public IBaseDao<BaseData> getBaseDao() {
		return baseDataDao;
	}

	@Override
	public List<BaseData> getAllModels() {
		return findBySN(BASEDATA_APPLICATIONMODEL);
	}
	@Override
	public List<BaseData> getAllOperations(){
		return findBySN(BASEDATA_OPERATION);
	}
	
	public List<BaseData> getAllSupplierType(){
		return findBySN(BASE_SUPPLIER_TYPE);
	}
	
	public List<BaseData> getAllSupplierLevel(){
		return findBySN(BASE_SUPPLIER_LEVEL);
	}
	public List<BaseData> getAllCustomerType(){
		return findBySN(BASE_CUSTOMER_TYPE);
	}
	
	public List<BaseData> getAllCustomerLevel(){
		return findBySN(BASE_CUSTOMER_LEVEL);
	}
	/* (non-Javadoc)
	 * @see cn.jely.cd.service.IBaseDataService#getAllProductUnits()
	 */
	@Override
	public List<BaseData> getAllInvoiceType() {
		return findBySN(BASE_INVOICE_TYPE);
	}
	
	@Override
	public List<BaseData> getAllProductBrand() {
		return findBySN(BASE_PRODUCT_BRAND);
	}
	
	
	private List<BaseData> findBySN(String sn){
		String hql="select o from BaseData o where o.type.sn=?";
		return baseDataDao.findByHql(hql,sn);
	}


}
