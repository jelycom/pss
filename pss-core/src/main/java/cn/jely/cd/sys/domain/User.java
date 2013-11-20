/*
 * 捷利商业进销存管理系统
 * @(#)User.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-8
 */
package cn.jely.cd.sys.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.struts2.json.annotations.JSON;

import cn.jely.cd.domain.Department;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-8 上午9:45:39
 */
public class User {
	public static final int DELETE = -1;
	public static final int DISABLE = 0;
	public static final int ENABLE = 1;
//	public static final int LEAVE = 2;

	/**Long:id:主键*/
	private Long id;
	/**String:name:用户名*/
	private String name;
	/**String:password:密码*/
	private String password;
	/**Integer:stateId:状态编号*/
	private Integer stateId;
	/**String:stateName:状态名称*/
	private String stateName;
	/**String:skin:用户主题 */
	private String skin;
	/**Date:expireDate:帐户失效日期*/
	private Date expireDate;
	private String memos;
	/**Set<Role>:roles:用户拥有的角色*/
	private Set<Role> roles = new TreeSet<Role>();
	/**List<Department>:scope:查询范围可用列表*/
	private List<Department> scope = new ArrayList<Department>();

	
	public User() {
	}

	public User(String name, String password) {
		this.name = name;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		if (null != stateId) {
			switch (stateId) {
			case DELETE:
				this.stateName = "删除";
				break;
			case DISABLE:
				this.stateName = "禁用";
				break;
			case ENABLE:
				this.stateName = "正常";
				break;
			default:
				break;
			}
		}
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}
	@JSON(format = "yyyy-MM-dd")
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	public String getMemos() {
		return memos;
	}
	
	public void setMemos(String memos) {
		this.memos = memos;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public List<Department> getScope() {
		return scope;
	}

	public void setScope(List<Department> scope) {
		this.scope = scope;
	}
	
}
