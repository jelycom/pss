/*
 * 捷利商业进销存管理系统
 * @(#)SystemAction.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-12
 */
package cn.jely.cd.sys.web;

import cn.jely.cd.sys.service.ISystemService;
import cn.jely.cd.web.BaseAction;

/**
 * @ClassName:SystemAction
 * @Description:初始化数据类
 * @author 周义礼
 * @version 2013-4-12 上午10:12:02
 *
 */
public class SystemAction extends BaseAction {

	private ISystemService systemService;
	
	public void setSystemService(ISystemService systemService) {
		this.systemService = systemService;
	}
	public String init(){
		systemService.saveinitData();
		return MAIN;
	}
	public String repair(){
		systemService.updaterepair();
		return MAIN;
	}
}
