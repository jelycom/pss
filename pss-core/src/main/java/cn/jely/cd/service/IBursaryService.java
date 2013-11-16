/*
 * 捷利商业进销存管理系统
 * @(#)Bursary.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.Bursary;

/**
 * @ClassName:BursaryService
 * @Description:Service
 * @author
 * @version 2013-08-05 10:50:45 
 *
 */
public interface IBursaryService extends IBaseTreeService<Bursary> {

	/**
	 * 查询所有其它收入项目
	 * @return List<Bursary>
	 */
	public List<Bursary> findOtherInCome();
	/**
	 * 查询所有其它支出项目
	 * @return List<Bursary>
	 */
	public List<Bursary> findOtherOutCome();
	/**
	 * 查询现金及银行帐户
	 * @return List<Bursary>
	 */
	public List<Bursary> findAccount();
}
