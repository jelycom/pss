/*
 * 捷利商业进销存管理系统
 * @(#)ProductCommonDetailPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-10
 */
package cn.jely.cd.pagemodel;

import java.math.BigDecimal;

/**产品进出货单页面模型
 * @ClassName:ProductCommonDetailPM Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-10 下午4:27:11
 * 
 */
public class ProductCommonDetailPM implements PageModel {
	
	/**Long:id:主键*/
	private Long id;
	/** 明细的排序号 */
	private Integer orders;
	private Long planMasterId;
	private Long orderBillMasterId;
	/** 商品 */
	private Long productId;
	private String fullName;
	private String specification;
	private String color;
	private String unit;
	/** 产品数量 */
	private int quantity;
	/** 产品单价 */
	private BigDecimal price;
	private BigDecimal subTotal;
	/** @Fields costAmount:明细成本小计 */
	private BigDecimal costAmount;
	/** @Fields productStockDetailId:对应的批次号 */
	private Long stockDetailID;
	/** BigDecimal:taxPrice:单个产品税金 */
	private BigDecimal taxPrice;
	/** BigDecimal:taxAmount:税金小计 */
	private BigDecimal tax;
	/** 产品金额小计 */
	private BigDecimal amount;

	private String memos;

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

	public Long getPlanMasterId() {
		return planMasterId;
	}

	public void setPlanMasterId(Long planMasterId) {
		this.planMasterId = planMasterId;
	}

	public Long getOrderBillMasterId() {
		return orderBillMasterId;
	}

	public void setOrderBillMasterId(Long orderBillMasterId) {
		this.orderBillMasterId = orderBillMasterId;
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

	public void setFullName(String productName) {
		this.fullName = productName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String productSpec) {
		this.specification = productSpec;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String productColor) {
		this.color = productColor;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String productUnit) {
		this.unit = productUnit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public Long getStockDetailID() {
		return stockDetailID;
	}

	public void setStockDetailID(Long stockDetailID) {
		this.stockDetailID = stockDetailID;
	}
	public BigDecimal getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

}
