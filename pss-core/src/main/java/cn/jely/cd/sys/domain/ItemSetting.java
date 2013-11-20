/*
 * 捷利商业进销存管理系统
 * @(#)ItemSetting.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-14
 */
package cn.jely.cd.sys.domain;

/**
 * @ClassName:ItemSetting
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-14 上午10:34:29
 *
 */
public class ItemSetting {
	private Long id;
	/**String:name:显示的名称*/
	private String name;
	/**String:className:实体类名称*/
	private String className;
	/**String:regx:item格式表达式*/
	private String regx;
	/**String:generatorName:生成器名称*/
	private String generatorName;
	
	
	public ItemSetting() {
	}
	
	
	public ItemSetting(String name, String className, String regx, String generatorName) {
		super();
		this.name = name;
		this.className = className;
		this.regx = regx;
		this.generatorName = generatorName;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getRegx() {
		return regx;
	}
	public void setRegx(String regx) {
		this.regx = regx;
	}
	public String getGeneratorName() {
		return generatorName;
	}
	public void setGeneratorName(String generatorName) {
		this.generatorName = generatorName;
	}
	
	
}
