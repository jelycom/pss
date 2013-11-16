/*
 * 捷利商业进销存管理系统
 * @(#)IProductCommonDao.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-15
 */
package cn.jely.cd.dao;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:IProductCommonDao
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-15 上午10:37:07
 *
 */
public interface IProductCommonDao<T> extends IBillStateDao<T> {

	public ProductCommonMaster getAllMasterById(Long id);
	/**
	 * 更新已付的应收应付款
	 * @param master
	 * @param paid void
	 */
	public void updatePaidARAP(ProductCommonMaster master,BigDecimal paid);
//	List<T> findUnComplete(BusinessUnits unit);

	/**
	 * 查询指定往来单位未完成的单据,状态为未完成,paidArap不为空
	 * @param objectQuery
	 * @return List<T>
	 */
	public List<T> findUnComplete(ObjectQuery objectQuery);
//	public void updateMaster(ProductCommonMaster master);
}
