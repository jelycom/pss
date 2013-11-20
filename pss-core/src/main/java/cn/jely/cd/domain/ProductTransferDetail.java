/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

import cn.jely.cd.util.math.SystemCalUtil;

/**
 * @ClassName:ProductTransferDetail
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-17 下午3:11:51
 *
 */
public class ProductTransferDetail {

	private Long id;
	/**ProductTrasferMaster:master:调拨主对象*/
	private ProductTransferMaster master;
	/**Integer:orders:顺序*/
	private Integer orders;
	/**Product:product:调拨的产品*/
	private Product product;
	/**Long:stockDetailID:批次号,用于手动指定成本时的*/
	private Long stockDetailID;
	/**Integer:quantity:数量*/
	private Integer quantity;
	/**BigDecimal:price:金额合计*/
	private BigDecimal amount;
	/**@Fields costAmount:明细成本小计*/
	private BigDecimal costAmount;
	/**String:memos:备注*/
	private String memos;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ProductTransferMaster getMaster() {
		return master;
	}
	public void setMaster(ProductTransferMaster master) {
		this.master = master;
	}
	public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public Long getStockDetailID() {
		return stockDetailID;
	}
	public void setStockDetailID(Long stockDetailID) {
		this.stockDetailID = stockDetailID;
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
	public BigDecimal getPrice(){
		return SystemCalUtil.dividePrice(amount, new BigDecimal(quantity));
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
	public BigDecimal getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}
}
