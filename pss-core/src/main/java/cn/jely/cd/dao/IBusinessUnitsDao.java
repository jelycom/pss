/*
 * 捷利商业进销存管理系统
 * @(#)Businessunits.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.jely.cd.domain.BusinessUnits;

/**
 * @ClassName:BusinessunitsAction
 * @Description:Dao
 * @author
 * @version 2012-11-14 14:35:42 
 *
 */
public interface IBusinessUnitsDao extends IBaseDao<BusinessUnits> {
	
	public boolean checkExist(BusinessUnits unit);
	/**
	 * 减少应付款
	 * @param id
	 * @param paid void
	 * @param date 
	 */
	public void updateSubtractPayAble(Serializable id,BigDecimal paid, Date date);
	/**
	 * 增加应付款
	 * @param t
	 * @param paid void
	 */
	public void updateAddPayAble(Serializable id,BigDecimal paid,Date date);
	/**
	 * 减少应收款
	 * @param paid void
	 * @param date 
	 * @param t
	 */
	public void updateSubtractReceiveAble(Serializable id,BigDecimal paid, Date date);
	/**
	 * 增加应收款
	 * @param t
	 * @param paid void
	 */
	public void updateAddReceiveAble(Serializable id,BigDecimal paid,Date date);
	/**
	 * 减少预付款
	 * @param t
	 * @param paid void
	 */
	public void updateSubtractPrepaid(Serializable id,BigDecimal paid);
	/**
	 * 增加预付款
	 * @param t
	 * @param paid void
	 */
	public void updateAddPrepaid(Serializable id,BigDecimal paid);
	/**
	 * 减少预收款
	 * @param t
	 * @param paid void
	 */
	public void updateSubtractAdvance(Serializable id,BigDecimal paid);
	/**
	 * 增加预收款
	 * @param t
	 * @param paid void
	 */
	public void updateAddAdvance(Serializable id,BigDecimal paid);
}
