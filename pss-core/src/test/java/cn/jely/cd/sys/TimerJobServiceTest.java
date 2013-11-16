/*
 * 捷利商业进销存管理系统
 * @(#)TimerJob.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import cn.jely.cd.sys.domain.TimerJob;
import cn.jely.cd.sys.service.ITimerJobService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:TimerJobServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-01-13 15:24:29 
 *
 */
public class TimerJobServiceTest extends cn.jely.cd.BaseServiceTest{

	private ITimerJobService timerJobService;

	@Resource(name = "timerJobService")
	public void setTimerJobService(ITimerJobService timerJobService) {
		this.timerJobService = timerJobService;
	}


	@Test
	public void testSave() {
		for (int i = 0; i < 5; i++) {
			TimerJob timerJob = new TimerJob("TJob"+i,"cn.jely.cd.sys.job.CronJob","0/1 * * * * ?", "1"); //如果出错请把字符串长度改短
			timerJobService.save(timerJob);
		}
		Assert.assertTrue(timerJobService.getAll().size()==5);
	}

	@Test
	public void testUpdate() {
		ObjectQuery objectQuery = new ObjectQuery().addWhere("name=:name", "name", "TJob2");
		TimerJob timerJob = timerJobService.findQueryObject(objectQuery);
		String oldstr=timerJob.getName();
		timerJob.setName("UJob");
		timerJobService.update(timerJob);
		TimerJob timerJob2 = timerJobService.findQueryObject(objectQuery);
		Assert.assertNull(timerJob2);
	}

	@Test
	public void testDelete() {
		TimerJob job=timerJobService.findQueryObject(new ObjectQuery().addWhere("name like :name", "name", "%TJob%"));
		timerJobService.delete(job.getId());
		TimerJob timerJob=timerJobService.getById(job.getId());
		Assert.assertNull(timerJob);
	}

	@Test
	public void testFindPager() {
		Pager<TimerJob> pager=timerJobService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

	@Test
	public void testStartAllAvaliableJob(){
		try {
			timerJobService.startAllAvaliableJob(false);
			Thread.sleep(6*1000);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}