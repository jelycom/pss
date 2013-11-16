/*
 * 捷利商业进销存管理系统
 * @(#)Businessunits.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBusinessUnitsDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.util.math.SystemCalUtil;

/**
 * @ClassName:BusinessunitsDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2012-11-14 14:35:42
 * 
 */
@Repository("businessunitsDao")
public class BusinessUnitsDaoImpl extends BaseDaoImpl<BusinessUnits> implements IBusinessUnitsDao {

	@Override
	public void updateSubtractPayAble(Serializable id, BigDecimal paid, Date date) {
		BusinessUnits unit = getById(id);
		BigDecimal currentPayAble = SystemCalUtil.checkValue(unit.getCurrentPayAble());
		unit.setCurrentPayAble(currentPayAble.subtract(SystemCalUtil.checkValue(paid)));
		unit.setLastDeliveryDate(date);
		update(unit);
	}

	@Override
	public void updateAddPayAble(Serializable id, BigDecimal paid, Date date) {
		BusinessUnits t = getById(id);
		BigDecimal currentPayAble = SystemCalUtil.checkValue(t.getCurrentPayAble());
		t.setCurrentPayAble(currentPayAble.add(SystemCalUtil.checkValue(paid)));
		t.setLastPurchaseDate(date);
		update(t);
	}

	@Override
	public void updateSubtractReceiveAble(Serializable id, BigDecimal paid, Date date) {
		BusinessUnits t = getById(id);
		BigDecimal currentReceiveAble = SystemCalUtil.checkValue(t.getCurrentReceiveAble());
		t.setCurrentReceiveAble(currentReceiveAble.subtract(SystemCalUtil.checkValue(paid)));
		t.setLastPurchaseDate(date);
		update(t);
	}

	@Override
	public void updateAddReceiveAble(Serializable id, BigDecimal paid, Date date) {
		BusinessUnits t = getById(id);
		BigDecimal currentReceiveAble = SystemCalUtil.checkValue(t.getCurrentReceiveAble());
		t.setCurrentReceiveAble(currentReceiveAble.add(SystemCalUtil.checkValue(paid)));
		t.setLastDeliveryDate(date);
		update(t);
	}

	@Override
	public void updateSubtractPrepaid(Serializable id, BigDecimal paid) {
		BusinessUnits t = getById(id);
		BigDecimal currentPrepaid = SystemCalUtil.checkValue(t.getCurrentPrepaid());
		t.setCurrentPrepaid(currentPrepaid.subtract(SystemCalUtil.checkValue(paid)));
		update(t);
	}

	@Override
	public void updateAddPrepaid(Serializable id, BigDecimal paid) {
		BusinessUnits t = getById(id);
		BigDecimal currentPrepaid = SystemCalUtil.checkValue(t.getCurrentPrepaid());
		t.setCurrentPrepaid(currentPrepaid.add(SystemCalUtil.checkValue(paid)));
		update(t);
	}

	@Override
	public void updateSubtractAdvance(Serializable id, BigDecimal paid) {
		BusinessUnits t = getById(id);
		BigDecimal currentAdvance = SystemCalUtil.checkValue(t.getCurrentAdvance());
		t.setCurrentAdvance(currentAdvance.subtract(SystemCalUtil.checkValue(paid)));
		update(t);
	}

	@Override
	public void updateAddAdvance(Serializable id, BigDecimal paid) {
		BusinessUnits t = getById(id);
		t.setCurrentAdvance(SystemCalUtil.checkValue(t.getCurrentAdvance()).add(SystemCalUtil.checkValue(paid)));
		update(t);
	}

	@Override
	public boolean checkExist(BusinessUnits unit) {
		String hql="select Count(o) from "+entityClass.getName()+" o where o.shortName = :shortName or o.item = :item ";
		Map<String, Object> paramValue=new HashMap<String, Object>();
		paramValue.put("shortName",unit.getShortName());
		paramValue.put("item", unit.getItem());
		return countByHql(hql, paramValue)>0L;
	}

}
