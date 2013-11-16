/*
 * 捷利商业进销存管理系统
 * @(#)ProductDeliveryMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.dao;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductDeliveryMasterAction
 * @author
 * @version 2013-06-21 14:54:28 
 *
 */
public interface IProductDeliveryDao extends IProductCommonDao<ProductDeliveryMaster> {
//	/**
//	 * 查找往来单位应收款未完全收回的单据
//	 * @return List<ProductDeliveryMaster>
//	 */
//	public List<ProductDeliveryMaster> findUnComplete(ObjectQuery objectQuery);
	
	/**
	 * 查询产品的历史记录,未指定往来单位/仓库则表示查询所有往来单位及所有仓库
	 * @param unit 指定的往来单位记录
	 * @param warehouse 指定的仓库出库的产品
	 * @param product
	 * @return List<BigDecimal>
	 */
	public List<BigDecimal> findPriceHistory(BusinessUnits unit,Warehouse warehouse,Product product);

	/**
	 * 查询销售及销售退货单据状态为未完成的部分
	 * @param objectQuery
	 * @return List<ProductCommonMaster>
	 */
	List<ProductCommonMaster> findUnFinishedWithReturn(ObjectQuery objectQuery);
}
