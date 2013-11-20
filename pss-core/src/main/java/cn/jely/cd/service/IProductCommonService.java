/*
 * 捷利商业进销存管理系统
 * @(#)IProductCommonService.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-25
 */
package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.util.query.ObjectQuery;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-25 下午5:30:28
 */
public interface IProductCommonService<T> extends IBaseService<T> {
	/**
	 * 查找往来单位应付款未完全付清的单据(不是完成状态,并且arap不为空)
	 * @param businessUnit 指定的供应商
	 * @return List<ProductPurchaseMaster>
	 */
//	public List<T> findUnComplete(BusinessUnits businessUnit);

	/**
	 * 查询所有不是完成状态的单据
	 * @param objectQuery
	 * @return List<T>
	 */
	public List<T> findAllUnFinished(ObjectQuery objectQuery);
}
