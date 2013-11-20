/*
 * 捷利商业进销存管理系统
 * @(#)RealStock.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-1
 */
package cn.jely.cd.vo;

import java.math.BigDecimal;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Warehouse;

/**
 * 实时库存显示类
 * 
 * @ClassName:RealStock Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-1 下午3:21:46
 * 
 */
public class RealStockVO implements Cloneable {
	private Warehouse warehouse;
	private Product product;
	private Integer quantity;
	private BigDecimal amount;

	public RealStockVO(Warehouse warehouse, Product product, Long quanlity, BigDecimal amount) {
		this.warehouse = warehouse;
		this.product = product;
		if (quanlity != null) {
			this.quantity = quanlity.intValue();
		} else {
			this.quantity = 0;
		}
		this.amount = amount;
	}

	public RealStockVO(Warehouse warehouse, Product product, Long quantity) {
		this.warehouse = warehouse;
		this.product = product;
		if (quantity != null) {
			this.quantity = quantity.intValue();
		} else {
			this.quantity = 0;
		}
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
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

	public void setQuantity(int quanlity) {
		this.quantity = quanlity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public RealStockVO toDisp(boolean withDetail) {
		RealStockVO master = null;
		try {
			master = (RealStockVO) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		Product productTmp = master.getProduct();
		Product newProduct = new Product(productTmp.getId(), productTmp.getItem(), productTmp.getFullName(),
				productTmp.getSpecification(), productTmp.getUnit(), productTmp.getColor(), productTmp.getMarque());
		newProduct.setProductType(new ProductType(productTmp.getProductType().getName(), productTmp.getProductType()
				.getItem(), productTmp.getProductType().getPy()));
		master.setProduct(newProduct);
		master.setWarehouse(new Warehouse(master.getWarehouse().getId(), master.getWarehouse().getName()));
		if (withDetail) {

		} else {

		}
		return master;
	}

	@Override
	public String toString() {
		return "RealStockVO [warehouse=" + warehouse + ", product=" + product + ", quanlity=" + quantity + ", amount="
				+ amount + "]";
	}

}
