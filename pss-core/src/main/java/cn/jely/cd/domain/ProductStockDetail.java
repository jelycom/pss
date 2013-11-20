/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-26
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

import cn.jely.cd.util.math.SystemCalUtil;

/**
 * 产品库存明细表,用于记录产品成本,不同的仓库可记录不同的成本
 * 如果是平均计算,则一个仓库只有一条记录.如果是有批次的(如:先进先出,后进先出,批次指定等),则每一笔进都需要记录.
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-26 下午1:55:08
 *
 */
public class ProductStockDetail {
	private Long id;
	private ProductCommonDetail productCommonDetail;
	/**@Fields warehouse:所属仓库*/
	private Warehouse warehouse;
	/**@Fields product:库存端口*/
	private Product product;
	/**@Fields inquanlity:入库数量*/
	private int inquantity;
	/**@Fields outquanlity:出库数量*/
	private int outquantity;
	/**@Fields amount:库存金额*/
	private BigDecimal amount;
	/**失效日期*/
	
	/**@Fields outAmount:出库金额*/
//	private BigDecimal outAmount;
	/**@Fields memos:备注*/
	private String memos;
	
	
	public ProductStockDetail() {
	}
	
	
	public ProductStockDetail(Warehouse warehouse, Product product, int inquanlity, BigDecimal amount) {
		this.warehouse = warehouse;
		this.product = product;
		this.inquantity = inquanlity;
		this.amount = amount;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Integer getInquantity() {
		return inquantity;
	}
	public void setInquantity(int inquanlity) {
		this.inquantity = inquanlity;
	}
	public Integer getOutquantity() {
		return outquantity;
	}
	public void setOutquantity(int outquanlity) {
		this.outquantity = outquanlity;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		if(amount==null){
			this.amount=BigDecimal.ZERO;
		}else{
			this.amount = amount;
		}
	}

	public ProductCommonDetail getProductCommonDetail() {
		return productCommonDetail;
	}
	public void setProductCommonDetail(ProductCommonDetail productCommonDetail) {
		this.productCommonDetail = productCommonDetail;
	}
	public BigDecimal getPrice(){
		BigDecimal quanlity = new BigDecimal(this.inquantity-this.outquantity);
		return SystemCalUtil.dividePrice(amount, quanlity);
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	
}
