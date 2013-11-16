/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockingMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service;

import cn.jely.cd.service.IBaseService;
import cn.jely.cd.domain.ProductStockingMaster;

/**
 * @ClassName:ProductStockingMasterService
 * @Description:Service
 * @author
 * @version 2013-08-16 10:54:17 
 *
 */
public interface IProductStockingService extends IBaseService<ProductStockingMaster> {
	/**
	 * 根据id字符串中的盘点表号继续库存的盘点.
	 * @param id
	 * @return ProductStockingMaster 剩余未盘点的产品组成的盘点单
	 */
	public ProductStockingMaster continueStocking(String id);
	
	/**
	 * 根据id字符串中的盘点表号进行单据生效的保存
	 * @param id void
	 */
	public void audit(String id);
}
