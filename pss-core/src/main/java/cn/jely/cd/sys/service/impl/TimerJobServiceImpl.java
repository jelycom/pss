/*
 * 捷利商业进销存管理系统
 * @(#)TimerJob.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.JobDetailBean;
import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.ITimerJobDao;
import cn.jely.cd.sys.domain.TimerJob;
import cn.jely.cd.sys.service.ITimerJobService;

/**
 * @ClassName:TimerJobServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-01-13 15:24:29
 * 
 */
@Service("timerJobService")
public class TimerJobServiceImpl extends BaseServiceImpl<TimerJob> implements ITimerJobService {
	private Scheduler scheduler;
	private ITimerJobDao timerJobDao;

	@Resource(name = "timerJobDao")
	public void setTimerJobDao(ITimerJobDao timerJobDao) {
		this.timerJobDao = timerJobDao;
	}

	@Resource(name = "schedular")
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public IBaseDao<TimerJob> getBaseDao() {

		return timerJobDao;
	}

	@Override
	public List<TimerJob> getAllAvaliableJob() {

		return timerJobDao.findByHql("from TimerJob where status=?",TimerJob.VALID);
	}

	/**
	 * 如果是先有一些任务已经在Scheduler中,而后被删除,而不是将state置0,则会出现任务继续运行的情况.
	 */
	@Override
	public void startAllAvaliableJob(boolean reloadAll) throws ClassNotFoundException, SchedulerException {
		List<TimerJob> jobs = timerJobDao.getAll();
		for (TimerJob timerJob : jobs) {
			String key = timerJob.getName();
			if(TimerJob.VALID.equals(timerJob.getStatus())){
				if(reloadAll){
					deleteByKey(key);
				}
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity(key)
						.withSchedule(CronScheduleBuilder.cronSchedule(timerJob.getExpression())).build();// 初始化触发器
				Class clazz = Class.forName(timerJob.getClassName());
				JobDetail jd = JobBuilder.newJob(clazz).withIdentity(key).build();
				scheduler.scheduleJob(jd, trigger);
			}else if(TimerJob.INVALID.equals(timerJob.getStatus())&&scheduler.isStarted()){
				deleteByKey(key);
			}
//			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(key)
//					.withSchedule(CronScheduleBuilder.cronSchedule(timerJob.getExpression())).build();// 初始化触发器
//			Class clazz = Class.forName(timerJob.getClassName());
//			JobDetail jd = JobBuilder.newJob(clazz).withIdentity(key).build();
//			// scheduler.shutdown();
//			if (scheduler.isStarted()) {//已经运行并且如果要重新加载所有则需要先停止所有的任务.起用所有的有效任务
//				logger.debug("reschedule ...........jobkey:"+key);
//				if(reloadAll){//如果是重新加载所有
//					deleteByKey(key);
//					if (TimerJob.VALID.equals(timerJob.getStatus())) {// 如果已经设定为生效的
//						scheduler.scheduleJob(jd, trigger);
//					}
//				}else if(!scheduler.checkExists(new TriggerKey(key))){//如果不是重载所有,那就只加载不存在的任务,失效的任务并不立即失效.
//					scheduler.scheduleJob(jd, trigger);
//				}
//			}else{
//				if (TimerJob.VALID.equals(timerJob.getStatus())) {// 如果已经设定为生效的
//					logger.debug("schedule ...........jobkey:"+key);
//					scheduler.scheduleJob(jd, trigger);
//				}
//			}
		}
		scheduler.start();
	}

	/**
	 * 删除失效的项目(如果有) 如果是一个Trigger对应一个JobDetail那只进行其中一个即可.</br>
	 * TODO:多个Trigger对应一个JobDetail待测
	 * 
	 * @Title:deleteChanged
	 * @param timerJob
	 * @throws SchedulerException
	 *             void
	 */
	private void deleteByKey(String key) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(key);
		if (scheduler.checkExists(triggerKey)) { // 如果有对应的任务触发器
			logger.debug("runningclass:" + scheduler.getTrigger(triggerKey).getClass().getName() + ",key.description:"
					+ scheduler.getTrigger(triggerKey).getDescription());
			scheduler.pauseTrigger(triggerKey);// 暂停触发器
			scheduler.unscheduleJob(triggerKey);// 移除触发器
		}
		JobKey jobKey = new JobKey(key);// 如果有对应的任务
		if (scheduler.checkExists(jobKey)) {
			scheduler.pauseJob(jobKey);// 暂停任务
			scheduler.deleteJob(jobKey);// 删除任务
		}
	}

}
