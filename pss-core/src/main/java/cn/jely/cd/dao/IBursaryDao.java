/*
 * 捷利商业进销存管理系统
 * @(#)Bursary.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.dao;

import cn.jely.cd.domain.Bursary;

/**
 * @ClassName:BursaryAction
 * @Description:Dao
 * @version 2013-08-05 10:50:45 
 *
 */
public interface IBursaryDao extends IBaseTreeDao<Bursary> {

	/**
	 * 检查实体是否已经存在
	 * @param bursary
	 * @return boolean
	 */
	public boolean checkExist(Bursary bursary);
	/**
	 * 根据会计科目编号查询会计科目
	 * @param item
	 * @return Bursary
	 */
	public Bursary findByItem(String item);
}
