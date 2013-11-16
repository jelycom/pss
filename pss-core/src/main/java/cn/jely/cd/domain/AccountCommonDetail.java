/*
 * 捷利商业进销存管理系统
 * @(#)AccountCommonDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-2
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

/**
 * @ClassName:AccountCommonDetail
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-2 下午5:28:00
 *
 */
public class AccountCommonDetail {
	private Long id;
	/**@Fields orders: 明细序号*/
	private Integer orders;
	/**@Fields master:主记录*/
	private AccountCommonMaster master;
	/**@Fields productMaster:产品进出业务主表*/
	private ProductCommonMaster productMaster;
	/**BigDecimal:needPay:应收/应付金额*/
	private BigDecimal arap;
	/**@Fields currentPay:本次付款金额*/
	private BigDecimal currentPay;
	/**String:memos:备注*/
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
	public AccountCommonMaster getMaster() {
		return master;
	}
	public void setMaster(AccountCommonMaster master) {
		this.master = master;
	}
	public ProductCommonMaster getProductMaster() {
		return productMaster;
	}
	public void setProductMaster(ProductCommonMaster productMaster) {
		this.productMaster = productMaster;
	}
	
	public BigDecimal getArap() {
		return arap;
	}
	public void setArap(BigDecimal arap) {
		this.arap = arap;
	}
	public BigDecimal getCurrentPay() {
		return currentPay;
	}
	public void setCurrentPay(BigDecimal currentPay) {
		this.currentPay = currentPay;
	}
	
	/**@return the memos*/
	public String getMemos() {
		return memos;
	}
	/**@param memos the memos to set*/
	public void setMemos(String memos) {
		this.memos = memos;
	}
	
}
