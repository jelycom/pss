/*
 * 捷利商业进销存管理系统
 * @(#)ProductCommonDaoAbstractImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-3
 */
package cn.jely.cd.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.IProductCommonDao;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:ProductCommonDaoAbstractImpl
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-3 下午3:57:24
 *
 */
public abstract class ProductCommonDaoAbstractImpl<T extends ProductCommonMaster> extends BillStateDaoImpl<T> implements IProductCommonDao<T> {
	
	@Override
	public void updatePaidARAP(ProductCommonMaster t,BigDecimal paid){
		if(t!=null&&t.getId()!=null){
			t=getById(t.getId());
			if(t.getArap()!=null){
				BigDecimal receivedArap = SystemCalUtil.checkValue(t.getPaidArap());
				BigDecimal newPaid = SystemCalUtil.add(receivedArap, paid);
				t.setPaidArap(newPaid);
				int compareRet = newPaid.compareTo(t.getArap());
				if(compareRet==0){
					t.setState(State.COMPLETE);
				}else if(compareRet<0){
					t.setState(State.PROCESS);
				}else{
					throw new RuntimeException("收/付款金额大于需收/付的金额");
				}
			}
		}
		getHibernateTemplate().update(t);
//		updateMaster(t);	
	}

	@Override
	public ProductCommonMaster getAllMasterById(Long id) {
		return getHibernateTemplate().get(ProductCommonMaster.class, id);
	}
	
	
//	public List<T> findUnComplete(ObjectQuery objectQuery){
//		return (List<T>) findUnComplete(objectQuery);
//	}
//
//
	@Override
	public List<T> findUnComplete(ObjectQuery objectQuery) {
//		Map<String, Object> param=new HashMap<String, Object>();
//		StringBuilder hqlBuilder=new StringBuilder();
//		hqlBuilder.append("FROM ProductCommonMaster o WHERE o.paidArap is not null and o.state in(:states) ");
//		param.put("states", );
//		objectQuery=prepareObjectQuery(objectQuery);
//		objectQuery.setQueryClass(ProductCommonMaster.class);
		if(StringUtils.isNotBlank(objectQuery.getWhere())){
			objectQuery.addWhere("and paidArap is not null ", null, null);
		}else{
			objectQuery.addWhere("paidArap is not null ", null, null);
		}
//		if(types!=null&&types.length>0){
//			objectQuery.addWhere(" and type(o) in (:types) ","types",ProductPurchaseMaster.class);
////			objectQuery.addWhere(" and type(o) in (ProductPurchaseMaster) ",null,null);
//		}
//		findByNamedParam(objectQuery.getFullHql(), objectQuery.getParamValueMap());
		return findAllUnFinished(objectQuery);
	}

}
