/*
 * 捷利商业进销存管理系统
 * @(#)InventoryCommonDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-4
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

/**
 * 盘点明细表
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-4 上午9:35:13
 */
public class InventoryCommonDetail {

	private Long id;
	private InventoryCommonMaster master;
	private int orders;
	/**Product:product:盘点产品*/
	private Product product;
	/**int:addQuantity:库面与实际差额的数量,盘盈为正,盘亏为负*/
	private Integer quantity;
	/**BigDecimal:amount:增加的金额*/
	private BigDecimal amount;
	/**Long:StockDetailID:如果产品是批次指定法计算成本,则需要提供批次id*/
	private Long StockDetailID;
	/**String:memos:备注*/
	private String memos;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public InventoryCommonMaster getMaster() {
		return master;
	}
	public void setMaster(InventoryCommonMaster master) {
		this.master = master;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer addQuantity) {
		this.quantity = addQuantity;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Long getStockDetailID() {
		return StockDetailID;
	}
	public void setStockDetailID(Long stockDetailID) {
		StockDetailID = stockDetailID;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	
	
}
