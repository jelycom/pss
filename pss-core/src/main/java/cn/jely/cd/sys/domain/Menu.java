/*
 * 捷利商业进销存管理系统
 * @(#)Menu.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-7
 */
package cn.jely.cd.sys.domain;

/**
 * 菜单类实体
 * @ClassName:Menu
 * @author 周义礼
 * @version 2013-4-7 上午8:47:14
 *
 */
public class Menu {
//	private Long id;
	private String name;
	private String url;
	private String icons;
//	private Menu parent;
	
	
	public Menu() {
	}
	
	public Menu(String name,String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcons() {
		return icons;
	}
	
	public void setIcons(String icons) {
		this.icons = icons;
	}
	
}
