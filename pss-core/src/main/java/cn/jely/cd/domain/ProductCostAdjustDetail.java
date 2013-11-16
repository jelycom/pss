/*
 * 捷利商业进销存管理系统
 * @(#)ProductCostAdjustDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-19
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCostAdjustMaster;

/**
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-19 下午5:29:22
 *
 */
public class ProductCostAdjustDetail {
private Long id;
private ProductCostAdjustMaster master;
/**Integer:orders:顺序*/
private Integer orders;
/**Product:product:调价的产品*/
private Product product;
/**Long:stockDetailID:批次号,用于手动指定成本时的*/
private Long stockDetailID;
/**Integer:quantity:数量*/
private Integer quantity;
/**@Fields costAmount:原始成本小计*/
private BigDecimal oldAmount;
/**BigDecimal:newAmount:调整后成本小计*/
private BigDecimal newAmount;
/**String:memos:备注*/
private String memos;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public ProductCostAdjustMaster getMaster() {
	return master;
}
public void setMaster(ProductCostAdjustMaster master) {
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

public BigDecimal getOldAmount() {
	return oldAmount;
}
public void setOldAmount(BigDecimal oldAmount) {
	this.oldAmount = oldAmount;
}
public BigDecimal getNewAmount() {
	return newAmount;
}
public void setNewAmount(BigDecimal newAmount) {
	this.newAmount = newAmount;
}
public String getMemos() {
	return memos;
}
public void setMemos(String memos) {
	this.memos = memos;
}

}
