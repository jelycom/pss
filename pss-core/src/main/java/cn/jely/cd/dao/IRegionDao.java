/*
 * 捷利商业进销存管理系统
 * @(#)Region.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao;

import cn.jely.cd.domain.Region;

/**
 * @ClassName:RegionAction
 * @Description:Dao
 * @author
 * @version 2012-11-09 13:48:12 
 *
 */
public interface IRegionDao extends IBaseTreeDao<Region> {

	/**
	 * 检查地区是否已经存在
	 * @param region
	 * @return boolean true:已经存在
	 */
	public boolean checkExist(Region region);
}
