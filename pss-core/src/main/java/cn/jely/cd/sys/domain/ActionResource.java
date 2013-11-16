/*
 * 捷利商业进销存管理系统
 * @(#)ActionResource.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-3-29
 */
package cn.jely.cd.sys.domain;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.LftRgtTreeNode;
import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * 系统中需要进行管理的URL类
 * TODO:有些资源只能在期初时用（期初库存添加，开帐后就不能再用了），有些资源只能期初后用（业务单据,期初不能使用），而有些资源期初，开除后都 能用（产品资料）。
 * @ClassName:ActionUrl
 * @author 周义礼
 * @version 2013-3-29 下午5:55:44
 * 
 */
public class ActionResource extends LftRgtTreeNode implements java.io.Serializable,IPinYinLogic{
	
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private Long id;
	/**资源名称*/
	private String name;
	/**资源拼音*/
	private String py;
	/**资源地址*/
	private String linkAddress;
	/**资源备注*/
	private String memos;
	/**资源角色*/
//	private Role role;
	/**菜单信息*/
	private Menu menu;
	
	/**boolean:sys:是否初始化使用的系统资源：true:是，则*/
	private boolean init;
	/**boolean:controlAble:是否公共资源，true则表示是，不需要进行权限控制，false：不是公共资源，表示需要进行权限控制*/
	private boolean community;

	public ActionResource() {
		super();
	}

	public ActionResource(Long id) {
		this.id = id;
	}
	
	
	public ActionResource(String name) {
		super();
		this.name = name;
	}

	public ActionResource(String name, String linkAddress) {
		super();
		this.name = name;
		this.linkAddress = linkAddress;
	}

	public ActionResource(String name, String linkAddress,String menuName) {
		this.name = name;
		this.linkAddress = linkAddress;
		if(menuName!=null&&!"".equals(menuName.trim())){
			this.menu=new Menu(menuName,linkAddress);
		}
	}
	public ActionResource(String name, String linkAddress,String menuName,String menuUrl,boolean isInit) {
		this.name = name;
		this.linkAddress = linkAddress;
		if(menuName!=null&&!"".equals(menuName.trim())){
			this.menu=new Menu(menuName,menuUrl);
		}
		this.init=isInit;
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

	public void setPy(String py) {
		this.py = py;
	}

	public String getPy() {
		return py;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}


	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionResource other = (ActionResource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActionResource [id=" + id + ", name=" + name + ", linkAddress=" + linkAddress + ", memos=" + memos
				+ "]";
	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}
	

	public boolean isCommunity() {
		return community;
	}

	public void setCommunity(boolean community) {
		this.community = community;
	}

	@Override
	public String pySource() {
		return name;
	}

	@Override
	public boolean checkBlank() {
		return StringUtils.isBlank(py);
	}

}
