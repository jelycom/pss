/*
 * 捷利商业进销存管理系统
 * @(#)LftRgtTreeMenu.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-17
 */
package cn.jely.cd.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 左右值菜单模型
 * 
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-9-17 下午3:58:01
 */
public class MenuTree {

	private String name;
	private String linkAddress;
	private List<MenuTree> children=new ArrayList<MenuTree>(); 

	public MenuTree() {
	}

	public MenuTree(String name, String linkAddress) {
		super();
		this.name = name;
		this.linkAddress = linkAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return linkAddress;
	}

	public void setUrl(String url) {
		this.linkAddress = url;
	}

	public List<MenuTree> getChildren() {
		return children;
	}

	public void setChildren(List<MenuTree> children) {
		this.children = children;
	}
	
}
