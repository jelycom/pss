/*
 * 捷利商业进销存管理系统
 * @(#)CompanyInfo.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.ICompanyInfoDao;
import cn.jely.cd.sys.dao.ITimerJobDao;
import cn.jely.cd.sys.domain.CompanyInfo;
import cn.jely.cd.sys.domain.TimerJob;
import cn.jely.cd.sys.job.CronJob;
import cn.jely.cd.sys.service.ICompanyInfoService;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:CompanyInfoServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-04-15 11:23:38 
 *
 */
public class CompanyInfoServiceImpl extends BaseServiceImpl<CompanyInfo> implements
		ICompanyInfoService {
	private ICompanyInfoDao companyInfoDao;
	private ITimerJobDao timerJobDao;
	
	public void setCompanyInfoDao(ICompanyInfoDao companyInfoDao) {
		this.companyInfoDao = companyInfoDao;
	}

	
	public void setTimerJobDao(ITimerJobDao timerJobDao) {
		this.timerJobDao = timerJobDao;
	}


	@Override
	public IBaseDao<CompanyInfo> getBaseDao() {
		return companyInfoDao;
	}


	@Override
	public Long save(CompanyInfo t) {
		persistTimerJob(t);//TODO:此处应该只是将Save消息发布到spring环境中,让需要对此事件感兴趣的逻辑执行,而不是写死在此处.
		return super.save(t);
	}


	/**
	 * 对计划任务进行保存及更新
	 * @Title:persistTimerJob
	 * @param timerJob void
	 */
	private void persistTimerJob(CompanyInfo companyInfo) {
//		TimerJob timerJob=timerJobDao.findObject(new ObjectQuery().addWhere(" name=:name","name",ConstValue.JOB_AUTOCLEARJOB));
//		if(timerJob==null){
//			timerJobDao.save(new TimerJob(ConstValue.JOB_AUTOCLEARJOB, CronJob.class.getName(), "0 * * * * ?",companyInfo.getAutoClear()==true?"1":"0"));
//		}else{
//			timerJob.setExpression("0 0 * * * ?");
//			timerJob.setStatus(companyInfo.getAutoClear()==true?TimerJob.VALID:TimerJob.INVALID);
//			timerJobDao.update(timerJob);
//		}
	}


	@Override
	public void update(CompanyInfo t) {
		persistTimerJob(t);
		super.update(t);
	}

}
