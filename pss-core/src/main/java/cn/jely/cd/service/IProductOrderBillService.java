/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.ProductOrderBillMaster;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.State;

/**
 * 产品订单(进货和出货)服务
 * @ClassName:ProductplanmasterService
 * @Description:Service
 * @author
 * @version 2012-12-05 10:34:59 
 *
 */
public interface IProductOrderBillService<T extends ProductOrderBillMaster> extends IBaseService<T> {

	/**查询所有未完成的计划
	 * @param objectQuery 封装的查询条件,如某个往来单位,某段时间等
	 * @return
	 */
	public List<T> findAllUnFinished(ObjectQuery objectQuery);
	
	/**查询所有已经完成的计划
	 * @param objectQuery 封装的查询条件,如某个往来单位,某段时间等
	 * @return
	 */
	public List<T> findAllFinished(ObjectQuery objectQuery);
//	/**
//	 * 更改计划的状态
//	 */
//	public T changeState(T master,State newState);
}
