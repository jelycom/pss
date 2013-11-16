/*
 * 捷利商业进销存管理系统
 * @(#)IBillStateDao.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-15
 */
package cn.jely.cd.dao;

import java.util.Date;
import java.util.List;

import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:IBillStateDao
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-15 上午9:46:05
 *
 */
public interface IBillStateDao<T> extends IBaseDao<T>{
	/**查询所有未完成的计划
	 * @param objectQuery 
	 * @return 未完成的计划列表
	 */
	public List<T> findAllUnFinished(ObjectQuery objectQuery);
	
	/**查询所有已完成的计划
	 * @param objectQuery 
	 * @return 已完成的计划列表
	 */
	public List<T> findAllFinished(ObjectQuery objectQuery);
	
	/**统计指定状态的计划单据数
	 * @Title:countFinished
	 * @return Long 返回的统计计划数量
	 */
	public Long countByStates(State...states);
	/**
	 * 统计所有已经完成的数量 
	 * @Title:countAllFinished
	 * @return Long
	 */
	public Long countAllFinished();
	
	/**
	 * 统计所有未完成数
	 * @return Long
	 */
	Long countAllUnFinished();
	/**
	 * 根据日期取得最后一个编号字段
	 * @param billDate
	 * @return String
	 */
	public String findLasteItem(ObjectQuery objectQuery);

	/**
	 * 根据单据日期生成编号
	 * @param billDate
	 * @return String
	 */
	String generateItem(Date billDate);
}
