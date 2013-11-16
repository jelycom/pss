/*
 * 捷利商业进销存管理系统
 * @(#)PeriodStock.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.dao;

import java.util.List;

import cn.jely.cd.domain.Product;
import cn.jely.cd.sys.domain.PeriodStock;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:PeriodStockAction
 * @Description:Dao
 * @author
 * @version 2013-06-08 15:45:12 
 *
 */
public interface IPeriodStockDao extends IPeriodAbstratDao<PeriodStock> {
	
	/**
	 * 寻找重复的记录
	 * @Title:findDuplicate
	 * @return List<PeriodStock>
	 */
	public List<PeriodStock> findDuplicate(PeriodStock t);
	
	/**
	 * 查询所有库存,包括没有库存的部分
	 * @param objectQuery
	 * @param warehouseId TODO
	 * @return List
	 */
	public List<PeriodStock> findAllStock(ObjectQuery objectQuery, Long warehouseId);

	/**
	 * 根据产品列表及仓库查询库存数据
	 * @param products
	 * @param warehouseId
	 * @return List<PeriodStock>
	 */
	public List<PeriodStock> findAllStock(List<Product> products, Long warehouseId);
}
