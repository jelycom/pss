/*
 * 捷利商业进销存管理系统
 * @(#)Querydata.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.Querydata;

/**
 * @ClassName:QuerydataService
 * @Description:Service
 * @author
 * @version 2013-03-01 15:09:02 
 *
 */
public interface IQuerydataService extends IBaseService<Querydata> {
	/**加载app所属的过滤条件,app为每个页面的自定义Id
	 * @param app
	 * @return 此页面保存的所有过滤条件
	 */
	List<Querydata> loadFilter(String app);
}
