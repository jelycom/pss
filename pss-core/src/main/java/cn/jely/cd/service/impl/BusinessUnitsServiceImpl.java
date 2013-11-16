/*
 * 捷利商业进销存管理系统
 * @(#)BusinessUnits.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBusinessUnitsDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Contacts;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.DateCoder;
import cn.jely.cd.util.code.ICodeGenerator;
import cn.jely.cd.util.code.impl.CodeGeneratorUtil;
import cn.jely.cd.util.code.impl.MonthGenerator;
import cn.jely.cd.util.code.impl.NoDateGenerator;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:BusinessUnitsServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2012-11-14 14:35:42
 * 
 */
@Service("businessUnitsService")
public class BusinessUnitsServiceImpl extends BaseServiceImpl<BusinessUnits> implements IBusinessUnitsService {

	private IBusinessUnitsDao businessUnitsDao;

	@Resource(name = "businessUnitsDao")
	public void setBusinessUnitsDao(IBusinessUnitsDao businessUnitsDao) {
		this.businessUnitsDao = businessUnitsDao;
	}

	@Override
	public IBaseDao<BusinessUnits> getBaseDao() {
		return businessUnitsDao;
	}
	
	/** 检查是否无意义的实体如果无意义则设置为空(id为null或-1) */
	private void prepareSaveUpdate(BusinessUnits t) {
		if(StringUtils.isBlank(t.getShortName())){
			throw new RuntimeException("简称不能为空");
		}
		List<Contacts> contactors = t.getContactors();
		if(contactors!=null&&contactors.size()>0){
			int orders=0;
			for(Contacts contactor:contactors){
				contactor.setOrders(orders++);
				contactor.setBusinessUnit(t);
			}
		}
		if(t.getRegion()!=null&&(t.getRegion().getId()==null || t.getRegion().getLft()==null || t.getRegion().getLft()==1)){
			t.setRegion(null);
		}
		if (t.getCustomerLevel() != null
				&& (t.getCustomerLevel().getId() == null || t.getCustomerLevel().getId() == -1)) {
			t.setCustomerLevel(null);
		}
		if (t.getCustomerType() != null && (t.getCustomerType().getId() == null || t.getCustomerType().getId() == -1)) {
			t.setCustomerType(null);
		}
		if (t.getSupplierLevel() != null
				&& (t.getSupplierLevel().getId() == null || t.getSupplierLevel().getId() == -1)) {
			t.setSupplierLevel(null);
		}
		if (t.getSupplierType() != null && (t.getSupplierType().getId() == null || t.getSupplierType().getId() == -1)) {
			t.setSupplierType(null);
		}
	}

	@Override
	protected Boolean beforeSaveCheck(BusinessUnits t) {
		if(t!=null){
			if(StringUtils.isBlank(t.getItem())){
				t.setItem(generateItem());
			}
			if (!checkExist(t)) {//保存前必须保证不存在重复的编号及简称
				prepareSaveUpdate(t);
				return super.beforeSaveCheck(t);
			}
		}
		return false;
	}

	@Override
	public String generateItem() {
		BusinessUnits last = findQueryLastObject(null);
		String lastItem = last != null ? last.getItem() : null;
		CodeGeneratorUtil codeGenUtil = CodeGeneratorUtil.getInstance();
		String className = businessUnitsDao.getEntityClass().getName();
		return codeGenUtil.genItem(className, lastItem, new Date());
	}

	private boolean checkExist(BusinessUnits t) {
		if(t!=null){
			return businessUnitsDao.checkExist(t);
		}
		return false;
	}

	@Override
	public void update(BusinessUnits t) {
		if(t!=null){
			prepareSaveUpdate(t);
			super.update(t);
		}
	}


	@Override
	public List<BusinessUnits> findByQuickProp(String values) {
		String hql = "from BusinessUnits b where b.fullName like ? or b.py like ? or b.shortName like ?";
		return super.findQuery(hql, null, new Object[] { "%" + values + "%", "%" + values + "%", "%" + values + "%" });
	}

}
