/*
 * 捷利商业进销存管理系统
 * @(#)ProductPlanDetailPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-16
 */
package cn.jely.cd.pagemodel;


/**
 * @ClassName:ProductPlanDetailPM
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-16 下午5:33:52
 *
 */
public class ProductPlanDetailPM {
	private Long id;
	/**	明细的排序号 */
	private Integer orders;
	private Long productId;
	private String fullName;
	private String specification;
	private String color;
	private String unit;
	/**	计划产品数量 */
	private Integer quantity;
	private String memo;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
