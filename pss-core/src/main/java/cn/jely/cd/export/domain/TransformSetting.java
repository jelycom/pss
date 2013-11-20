/*
 * 捷利商业进销存管理系统
 * @(#)TransformSetting.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-11-13
 */
package cn.jely.cd.export.domain;

import java.io.Serializable;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-11-13 下午2:59:35
 */
public class TransformSetting implements Serializable {
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private Long id;
	/**String:name:所属实体名*/
	private String name;
	/**String:propertyName:属性名称*/
	private String propertyName;
	/**String:dispName:显示名称*/
	private String dispName;
	/**Class<?>:propertyType:属性类型*/
	private Class<?> propertyType;
	
	public TransformSetting(Long id, String name, String propertyName, String dispName,Class<?> propertyType) {
		this.id = id;
		this.name = name;
		this.propertyName = propertyName;
		this.dispName = dispName;
		this.propertyType = propertyType;
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
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Class<?> getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}
	public String getDispName() {
		return dispName;
	}
	public void setDispName(String dispName) {
		this.dispName = dispName;
	}
	
}
