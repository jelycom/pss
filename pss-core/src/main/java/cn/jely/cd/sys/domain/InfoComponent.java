/*
 * 捷利商业进销存管理系统
 * @(#)InfoCompnent.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-2
 */
package cn.jely.cd.sys.domain;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * 表操作信息组件,通过Interceptor来进行值的修改
 * @ClassName:InfoCompnent
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-2 上午11:21:25
 *
 */
public class InfoComponent {
	protected Date createTime;
	protected String createMan;
	protected Date modifyTime;
	protected String modifyMan;
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateMan() {
		return createMan;
	}
	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyMan() {
		return modifyMan;
	}
	public void setModifyMan(String modifyMan) {
		this.modifyMan = modifyMan;
	}
	
}
