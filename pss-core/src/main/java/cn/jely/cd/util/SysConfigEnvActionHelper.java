/*
 * 捷利商业进销存管理系统
 * @(#)SysConfigEnvHelper.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-17
 */
package cn.jely.cd.util;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.jely.cd.export.service.IExporterService;
import cn.jely.cd.sys.dao.ISystemSettingDao;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.service.ITimerJobService;
/**
 * 系统配置环境工具,将需要进行配置的属性放入此类中统一进行管理
 * @ClassName:SysConfigEnvHelper
 * @author 周义礼
 * @version 2013-4-17 上午9:16:15
 * 
 */
public class SysConfigEnvActionHelper implements ApplicationContextAware{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ISystemSettingDao	systemSettingDao;
	private ITimerJobService timerJobService;
	private ApplicationContext context;

	public void setSystemSettingDao(ISystemSettingDao systemSettingDao) {
		this.systemSettingDao = systemSettingDao;
	}

	public void setTimerJobService(ITimerJobService timerJobService) {
		this.timerJobService = timerJobService;
	}

	public void init(ServletContext sc) {
		SystemSetting setting=systemSettingDao.getSetting();
		if(setting!=null&&setting.getCompanyInfo().getOpened()){//有配置并且是开帐状态才初始化定时任务
			initJob();
		}
		sc.setAttribute(ConstValue.SYS_SETTING, setting);
		sc.setAttribute(ConstValue.CONTEXTPATH, sc.getContextPath());
		IExporterService exporterService = context.getBean(IExporterService.class);
		if(exporterService!=null){
			String realPath = sc.getRealPath("/");
			exporterService.setBasePath(realPath);
//			sc.setAttribute("basePath", realPath);
		}
		String defaultSkin = setting.getEditOption().getDefaultSkin();
		sc.setAttribute(ConstValue.USERSKIN, defaultSkin);
	}

	public void reload(ServletContext sc) {
		init(sc);
	}
//TODO:注释的部分是由于系统启动时即要去连接相应的数据，而此时相应的表并没有创建出来，导致系统出错。应将这部分代码实现换个地方来进行
	private void initJob() {
		/*try {
			timerJobService.startAllAvaliableJob(true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}*/
	}

@Override
public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	this.context = applicationContext;
}
}
