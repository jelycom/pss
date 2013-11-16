/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockingDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-16
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

/**
 * 产品盘点明细表
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-16 上午9:49:39
 */
public class ProductStockingDetail {
	
	private Long id;
	private ProductStockingMaster master;
	private int orders;
	/**Product:product:盘点产品*/
	private Product product;
	/**Integer:oldQuantity:盘点时帐面数量*/
	private Integer oldQuantity;
	/**Integer:newQuantity:盘点时实际数量*/
	private Integer newQuantity;
	/**int:quantity:增加的数量,盘盈为正,盘亏为负,相符为0*/
	private Integer quantity;
	/**BigDecimal:amount:增加的金额,可不用，这样只需要关心数量即可*/
	private BigDecimal amount;
	/**Long:StockDetailID:如果产品是批次指定法计算成本,则需要提供批次id*/
	private Long StockDetailID;
	/**boolean:complete:是否已盘,用于盘点过程中途退出时保存盘点进度用*/
	private boolean complete;
	/**String:memos:备注*/
	private String memos;

	
	public ProductStockingDetail() {
		super();
	}
	public ProductStockingDetail(Product product) {
		super();
		this.product = product;
	}	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ProductStockingMaster getMaster() {
		return master;
	}
	public void setMaster(ProductStockingMaster master) {
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
	
	public Integer getOldQuantity() {
		return oldQuantity;
	}
	public void setOldQuantity(Integer oldQuantity) {
		this.oldQuantity = oldQuantity;
	}
	public Integer getNewQuantity() {
		return newQuantity;
	}
	public void setNewQuantity(Integer newQuantity) {
		this.newQuantity = newQuantity;
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
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	
}
