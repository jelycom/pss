/*
 * 捷利商业进销存管理系统
 * @(#)Role.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-3-29
 */
package cn.jely.cd.sys.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jely.cd.domain.BaseData;

/**
 * 角色类
 * 
 * @ClassName:Role
 * @author 周义礼
 * @version 2013-3-29 下午9:34:42
 * 
 */
public class Role {
	private Long id;
	private String name;
	private String dispName;
	private List<ActionResource> resources=new ArrayList<ActionResource>();
	/** @Fields group:角色所属的角色组 */
	private BaseData group;

	public Role() {
	}

	public Role(Long id) {
		this.id = id;
	}

	public Role(String name, String dispName) {
		this.name = name;
		this.dispName = dispName;
	}

	public Role(String name, String dispName, BaseData group) {
		this.name = name;
		this.dispName = dispName;
		this.group = group;
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

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}


	public List<ActionResource> getResources() {
		return resources;
	}

	public void setResources(List<ActionResource> resources) {
		this.resources = resources;
	}

	public BaseData getGroup() {
		return group;
	}

	public void setGroup(BaseData group) {
		this.group = group;
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
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", dispName=" + dispName + ", resources=" + resources + ", group="
				+ group + "]";
	}

}
