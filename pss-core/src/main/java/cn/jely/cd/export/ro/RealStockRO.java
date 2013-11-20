/*
 * 捷利商业进销存管理系统
 * @(#)RealStockRO.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-22
 */
package cn.jely.cd.export.ro;

import java.math.BigDecimal;

import cn.jely.cd.domain.Warehouse;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-22 下午4:52:38
 */
public class RealStockRO {
	private String productName;
	private String shortName;
	private String productUnit;
	private String productMarque;
	private String productSpecification;
	private String productColor;
	private String warehouseName;
	private Integer quantity;
	private BigDecimal amount;
	private String warehouseRegion;

	public RealStockRO() {
	}

	public RealStockRO(String productName, String shortName, String productUnit, String productMarque,
			String productSpecification, String productColor, String warehouseName, Long quantity, BigDecimal amount) {
		this.productName = productName;
		this.shortName = shortName;
		this.productUnit = productUnit;
		this.productMarque = productMarque;
		this.productSpecification = productSpecification;
		this.productColor = productColor;
		this.warehouseName = warehouseName;
		if (quantity != null) {
			this.quantity = quantity.intValue();
		} else {
			this.quantity = 0;
		}
		this.amount = amount;
	}
	public RealStockRO(String productName, String shortName, String productUnit, String productMarque,
			String productSpecification, String productColor, String warehouseName, Integer quantity, BigDecimal amount) {
		this.productName = productName;
		this.shortName = shortName;
		this.productUnit = productUnit;
		this.productMarque = productMarque;
		this.productSpecification = productSpecification;
		this.productColor = productColor;
		this.warehouseName = warehouseName;
		if (quantity != null) {
			this.quantity = quantity;
		} else {
			this.quantity = 0;
		}
		this.amount = amount;
	}
	
	public String getProductMarque() {
		return productMarque;
	}
	public void setProductMarque(String productMarque) {
		this.productMarque = productMarque;
	}
	public String getProductSpecification() {
		return productSpecification;
	}
	public void setProductSpecification(String productSpecification) {
		this.productSpecification = productSpecification;
	}
	public String getProductColor() {
		return productColor;
	}
	public void setProductColor(String productColor) {
		this.productColor = productColor;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getWarehouseRegion() {
		return warehouseRegion;
	}
	public void setWarehouseRegion(String warehouseRegion) {
		this.warehouseRegion = warehouseRegion;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductUnit() {
		return productUnit;
	}
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
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
	
}
