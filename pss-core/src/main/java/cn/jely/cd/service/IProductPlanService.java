/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.util.List;

/**
 * 产品计划(进货和出货)服务
 * @ClassName:ProductplanmasterService
 * @Description:Service
 * @author
 * @version 2012-12-05 10:34:59 
 *
 */
public interface IProductPlanService<T> extends IBaseService<T> {
//	public Long save(ProductPlanMaster productPlanMaster);
//	public void update(ProductPlanMaster productplanmaster);
//	public void deleteRelation(String id);
	/**查询所有未完成的计划
	 * @return
	 */
	public List<T> findAllUnFinished();
	
	/**查询所有已经完成的计划
	 * @return
	 */
	public List<T> findAllFinished();
//	/**
//	 * 更改计划的状态
//	 */
//	public ProductPlanMaster changeState(ProductPlanMaster master,State newState);

	/**统计所有完成状态的计划单据数量
	 * @Title:countAllFinished
	 * @return Long
	 */
	public Long countAllFinished();
	
	
}
