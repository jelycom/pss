/*
 * 捷利商业进销存管理系统
 * @(#)Productmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import cn.jely.cd.dao.IProductOrderBillDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.ProductOrderBillDetail;
import cn.jely.cd.domain.ProductOrderBillMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:ProductmasterDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2013-01-08 15:45:02
 * 
 */
public class ProductOrderBillDaoAbstractImpl<T extends ProductOrderBillMaster> extends BillStateDaoImpl<T> implements
		IProductOrderBillDao<T> {

	@Override
	public Set<String> update(Map<Long, List<ProductQuantity>> orderMap, BigDecimal paid) {
		if (!orderMap.isEmpty()) {
			Set<String> orderItems = new TreeSet<String>();
			BigDecimal totalPaid = SystemCalUtil.checkValue(paid);
			for (Long planId : orderMap.keySet()) {
				List<ProductQuantity> assoPQs = orderMap.get(planId);
				T master = getById(planId);
				orderItems.add(master.getItem());
				totalPaid = updateARAP(totalPaid, master);// 冲抵预收预付款
				boolean complete = true;
				for (ProductOrderBillDetail detail : master.getDetails()) {
					for (ProductQuantity pq : assoPQs) {
						if (detail.getProduct().equals(pq.getProduct()) && !pq.isComplete()) {// 如果针对同一产品并且明细是没有完成的状态
							Integer oldComplete = detail.getCompleteQuantity() == null ? 0 : detail.getCompleteQuantity();
							int completeQuantity = oldComplete + pq.getQuantity();
							detail.setCompleteQuantity(completeQuantity);
							if (detail.getQuantity() > completeQuantity) {
								if (complete) {
									complete = false;
								}
							} else {
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
			return orderItems;
		}
		return null;
	}

	/**
	 * 更新预收/付款金额
	 * 
	 * @param totalPaid
	 *            总的冲抵金额
	 * @param master
	 *            此次冲抵的单据
	 * @return BigDecimal 冲抵后的金额(作为循环时下次使用的金额)
	 */
	private BigDecimal updateARAP(BigDecimal totalPaid, T master) {
		BigDecimal oldArap = SystemCalUtil.checkValue(master.getArap());
		int compareRet = totalPaid.compareTo(oldArap);
		if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
			if (compareRet >= 0) {
				master.setPaidArap(oldArap);
				totalPaid = totalPaid.subtract(oldArap);
			} else if (compareRet < 0) {
				master.setPaidArap(oldArap.subtract(totalPaid));
				totalPaid = BigDecimal.ZERO;
			}
		}
		return totalPaid;
	}

	@Override
	public BigDecimal calPrepaid(Set<? extends ProductOrderBillMaster> orderBills) {
		BigDecimal dbPrepare = BigDecimal.ZERO;// 数据库中记录的预付款金额(预付-已冲销部分)
		if (orderBills != null && orderBills.size() > 0) {
			for (ProductOrderBillMaster orderMaster : orderBills) {
				orderMaster = getById(orderMaster.getId());
				BigDecimal orderArap = orderMaster.getArap();
				BigDecimal remainARAP = BigDecimal.ZERO;
				if (orderArap != null) {
					BigDecimal paidArap = orderMaster.getPaidArap();
					if (paidArap != null) {
						remainARAP = orderArap.subtract(paidArap);
					} else {
						remainARAP = orderArap;
					}
					dbPrepare = dbPrepare.add(remainARAP);
				}
			}
		}
		return dbPrepare;
	}

	@Override
	public List<T> findUnComplete(BusinessUnits unit) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("states", StateManager.getUnCompleteStates());
		param.put("unit", unit);
		return findByNamedParam("from " + getEntityClass().getName()
				+ " where paidArap is not null and state in(:states) and businessUnit=:unit", param);
	}
}
