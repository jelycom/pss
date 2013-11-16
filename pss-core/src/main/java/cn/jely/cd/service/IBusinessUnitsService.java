/*
 * 捷利商业进销存管理系统
 * @(#)Businessunits.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.BusinessUnits;

/**
 * @ClassName:BusinessunitsService
 * @Description:Service
 * @author
 * @version 2012-11-14 14:35:42 
 *
 */
public interface IBusinessUnitsService extends IBaseService<BusinessUnits> {
	/**根据此类的快速搜索属性进行搜索
	 * @Title:findByQuickProp
	 * @param values
	 * @return List<Businessunits>
	 */
	List<BusinessUnits> findByQuickProp(String values);
	/**
	 * 生成往来单位的编号
	 * @return
	 */
	String generateItem();
	
	
}
