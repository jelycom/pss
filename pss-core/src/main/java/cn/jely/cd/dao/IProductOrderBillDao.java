/*
 * 捷利商业进销存管理系统
 * @(#)Productmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.ProductOrderBillMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.util.state.State;


/**
 * @ClassName:ProductmasterAction
 * @Description:Dao
 * @author
 * @version 2013-01-08 15:45:02 
 *
 */
public interface IProductOrderBillDao<T extends ProductOrderBillMaster> extends IBillStateDao<T> {

	/**
	 *	用List中的项目更新id=键值的记录
	 * @param planMap,paid 需要被更新(paidArap)的总金额 void
	 * @return Set<String> 所更新的计划编号集合,如果没有则返回null.
	 */
	public Set<String> update(Map<Long, List<ProductQuantity>> planMap,BigDecimal paid);
	/**
	 * 计算列表中所有预付款余额
	 * @param productOrderBills
	 * @return BigDecimal
	 */
	public BigDecimal calPrepaid(Set<? extends ProductOrderBillMaster> productOrderBills);
	/**
	 * 查询指定单位未完成的单据
	 * @param unit
	 * @return List<T>
	 */
	List<T> findUnComplete(BusinessUnits unit);

}
