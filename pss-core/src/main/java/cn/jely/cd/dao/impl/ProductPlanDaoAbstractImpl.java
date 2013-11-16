/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cn.jely.cd.dao.IProductPlanDao;
import cn.jely.cd.domain.ProductPlanDetail;
import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.util.state.State;

/**
 * 产品计划(采购和销售)通用功能实现
 * 
 * @ClassName:ProductPlanDaoAbstractImpl
 * @Description:DaoImpl
 * @author
 * @version 2012-12-05 10:34:59
 * 
 */

public abstract class ProductPlanDaoAbstractImpl<T extends ProductPlanMaster> extends BillStateDaoImpl<T> implements
		IProductPlanDao<T> {

	@Override
	public boolean checkExist(T t) {
		String hql = "select count(o) from " + entityClass.getName() + " o where o.item=:item";
		return countByHql(hql, "item", t.getItem()) > 0L;
	}

	@Override
	public Set<String> update(Map<Long, List<ProductQuantity>> planMap) {
		if (!planMap.isEmpty()) {
			Set<String> planItems = new TreeSet<String>();
			for (Long planId : planMap.keySet()) {
				List<ProductQuantity> assoPQs = planMap.get(planId);
				T master = getById(planId);
				planItems.add(master.getItem());
				boolean complete = true;
				for (ProductPlanDetail detail : master.getDetails()) {
					for (ProductQuantity pq : assoPQs) {
						if (detail.getProduct().equals(pq.getProduct()) && !pq.isComplete()) {// 如果针对同一产品并且明细是没有完成的状态
							int oldComplete = detail.getCompleteQuantity() == null ? 0 : detail.getCompleteQuantity();
							int completeQuantity = oldComplete + pq.getQuantity();
							detail.setCompleteQuantity(completeQuantity);
							if (detail.getQuantity() > completeQuantity) {
								if (complete) {
									complete = false;
								}
							} else {// 如果完成数量大于等于计划数,表示完成
								detail.setComplete(true);
								break;
							}
						}
					}
					if (!detail.isComplete()) {
						complete = false;
					}
				}
				if (complete) {// 如果遍历全都完成,则
					master.setState(State.COMPLETE);
				} else {
					master.setState(State.PROCESS);
				}
				update(master);
			}
			return planItems;
		}
		return null;
	}
}
