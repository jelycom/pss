/*
 * 捷利商业进销存管理系统
 * @(#)ProductQuantity.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-11
 */
package cn.jely.cd.domain;

/**
 *  与计划/订单进行状态关联更新计算时传递用
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-11 下午5:12:52
 *
 */
public class ProductQuantity {
	private Product product;
	private int quantity;
	private boolean complete; 
	
	public ProductQuantity(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
}
