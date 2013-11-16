/*
 * 捷利商业进销存管理系统
 * @(#)TimerJob.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.service;

import java.util.List;

import org.quartz.SchedulerException;

import cn.jely.cd.service.IBaseService;
import cn.jely.cd.sys.domain.TimerJob;

/**
 * @ClassName:TimerJobService
 * @Description:Service
 * @author
 * @version 2013-01-13 15:24:29 
 *
 */
public interface ITimerJobService extends IBaseService<TimerJob> {
	/**
	 * 取得所有有效的任务列表
	 * @Title:getAllAvaliableJob
	 * @Description:
	 * @return List<TimerJob>
	 */
	public List<TimerJob> getAllAvaliableJob();

	/**
	 * 开始运行所有可用的定时任务
	 * @Title:startAllAvaliableJob
	 * @param reloadAll 是否重新加载所有.
	 * @throws ClassNotFoundException
	 * @throws SchedulerException void
	 */
	public void startAllAvaliableJob(boolean reloadAll) throws ClassNotFoundException, SchedulerException;
}
