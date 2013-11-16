/*
 * 捷利商业进销存管理系统
 * @(#)Region.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Region;

/**
 * @ClassName:RegionService
 * @Description:Service
 * @author
 * @version 2012-11-09 13:48:12 
 *
 */
public interface IRegionService extends IBaseTreeService<Region> {
	/**
	 * @Title:generateItem
	 * @param productType 根据父类生成子类的编号
	 * @return String 生成的子类编号
	 */
	public String generateItem(Region region);
}
