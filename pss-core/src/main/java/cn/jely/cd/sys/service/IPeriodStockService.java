/*
 * 捷利商业进销存管理系统
 * @(#)PeriodStock.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service;


import java.util.List;

import cn.jely.cd.service.IBaseService;
import cn.jely.cd.sys.domain.PeriodStock;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:PeriodStockService
 * @Description:Service
 * @author
 * @version 2013-06-08 15:45:12 
 *
 */
public interface IPeriodStockService extends IBaseService<PeriodStock> {
	
	public List<PeriodStock> findAllStock(ObjectQuery objectQuery, Long warehouseId);
	
}
