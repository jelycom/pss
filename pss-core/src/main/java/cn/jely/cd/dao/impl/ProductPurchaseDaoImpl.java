/*
 * 捷利商业进销存管理系统
 * @(#)ProductPurchaseMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.IProductPurchaseDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryGroup;
import cn.jely.cd.util.query.QueryRule;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:ProductPurchaseMasterDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2013-06-21 14:54:28 
 *
 */

public class ProductPurchaseDaoImpl extends ProductPurchaseDaoAbstractImpl<ProductPurchaseMaster> implements IProductPurchaseDao {

	@Override
	public List<BigDecimal> findPriceHistory(BusinessUnits unit, Warehouse warehouse, Product product) {
//		String hql="select "//TODO 进出货时相应的价格历史查询.
		return null;
	}

	@Override
	public List<ProductCommonMaster> findUnCompleteWithReturn(ObjectQuery objectQuery) {
		objectQuery = prepareObjectQuery(objectQuery);
//		objectQuery.setQueryClass(ProductCommonMaster.class);
		objectQuery.setBaseHql("from ProductCommonMaster o ");
		QueryGroup queryGroup = objectQuery.getQueryGroup();
		if(StringUtils.isBlank(queryGroup.getGroupOp())){
			queryGroup.setGroupOp(QueryGroup.AND);
		}
		queryGroup.getRules().add(new QueryRule("paidArap is not null and type(o) in (ProductPurchaseMaster,ProductPurchaseReturnMaster) and state", FieldOperation.in, StateManager.getUnCompleteStates()));
//		if(StringUtils.isNotBlank(objectQuery.getWhere())){
//			objectQuery.addWhere("and  in (:states)", "states", StateManager.getUnCompleteStates());
//		}else{
//			objectQuery.addWhere("paidArap is not null and type(o) in (ProductPurchaseMaster,ProductPurchaseReturnMaster) and state in (:states)", "states", StateManager.getUnCompleteStates());
//		}
		return findByNamedParam(objectQuery.getFullHql(), objectQuery.getParamValueMap());
	}
}
