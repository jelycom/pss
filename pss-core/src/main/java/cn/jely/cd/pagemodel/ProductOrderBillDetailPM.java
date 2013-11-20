/*
 * 捷利商业进销存管理系统
 * @(#)ProductOrderBillDetailPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-16
 */
package cn.jely.cd.pagemodel;

import java.math.BigDecimal;

/**
 * @ClassName:ProductOrderBillDetailPM
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-16 下午5:05:09
 *
 */
public class ProductOrderBillDetailPM  implements PageModel{
	private Long id;
	private Integer orders;
	/** 订单主表 */
	private Long planMasterId;
	/** 产品*/
	private Long productId;
	private String fullName;
	private String specification;
	private String color;
	private String unit;
	/** 订单数量 */
	private Integer quantity;
	/** 产品单价 */
	private BigDecimal price;
	/** 小计 */
	private BigDecimal subTotal;
	/** 产品税金 */
	private BigDecimal taxPrice;
	/** 产品税金小计 */
	private BigDecimal tax;
	/** 合计 :tax+subtotal*/
	private BigDecimal amount;
	/** 备注(如果有计划单则自动添入计划单号) */
	private String memos;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPlanMasterId() {
		return planMasterId;
	}
	public void setPlanMasterId(Long planMasterId) {
		this.planMasterId = planMasterId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	
}
