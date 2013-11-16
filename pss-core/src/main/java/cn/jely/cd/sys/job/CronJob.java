/*
 * 捷利商业进销存管理系统
 * @(#)CornJob.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @ClassName:CornJob
 * @author 周义礼
 * @version 2013-1-13 下午7:28:46
 *
 */
public class CronJob implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		
		System.out.println("Test:"+jobExecutionContext.getJobDetail().getKey()+" run count:"+jobExecutionContext.getNextFireTime());
	}

}
