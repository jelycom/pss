/*
 * 捷利商业进销存管理系统
 * @(#)SysInitParamBean.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-16
 */
package cn.jely.cd.util;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

/**
 * 载入默认的配置参数到环境中
 * @ClassName:SysInitParamBean
 * @author 周义礼
 * @version 2013-4-16 上午10:48:40
 *
 */
public class SysInitParamBean implements ServletContextAware {
	private Logger logger=LoggerFactory.getLogger(getClass());
	private SysConfigEnvActionHelper sysConfigEnvActionHelper;

	public void setSysConfigEnvActionHelper(SysConfigEnvActionHelper sysConfigEnvActionHelper) {
		this.sysConfigEnvActionHelper = sysConfigEnvActionHelper;
	}



	@Override
	public void setServletContext(ServletContext sc) {
		sysConfigEnvActionHelper.init(sc);
//		ProjectConfig.getInstance().init();
		logger.debug("-------setServletContext-------");
	}

//	@Override
//	public void afterPropertiesSet() throws Exception {
//		logger.debug("----------afterPropertiesSet0--------------");
//
//	}

}
