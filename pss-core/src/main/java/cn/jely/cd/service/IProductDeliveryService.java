/*
 * 捷利商业进销存管理系统
 * @(#)ProductDeliveryMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductDeliveryMasterService
 * @Description:Service
 * @author
 * @version 2013-06-21 14:54:28 
 *
 */
public interface IProductDeliveryService extends IProductCommonService<ProductDeliveryMaster> {

	/**
	 * 查询产品的历史记录,未指定往来单位/仓库则表示查询所有往来单位及所有仓库
	 * @param unit 指定的往来单位记录
	 * @param warehouse 指定的仓库出库的产品
	 * @param product
	 * @return List<BigDecimal> 产品历史价格列表
	 */
	public List<BigDecimal> findPriceHistory(BusinessUnits unit,Warehouse warehouse,Product product);
	
	/**
	 * 查询出货及出货退货的所有未完成记录
	 * @param objectQuery
	 * @return List<ProductCommonMaster>
	 */
	public List<ProductCommonMaster> findUnFinishedWithReturn(ObjectQuery objectQuery);
}
