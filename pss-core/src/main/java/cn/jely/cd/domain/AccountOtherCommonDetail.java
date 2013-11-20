/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherCommonDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-5
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-8-5 下午4:40:04
 */
public class AccountOtherCommonDetail {
	/**Long:id:主键*/
	private Long id;
	/**Integer:orders:排序号*/
	private Integer orders;
	/**AccountOtherCommonMaster:master:主表*/
	private AccountOtherCommonMaster master;
	/**Bursary:bursary:收/支科目*/
	private Bursary bursary;
	/**BigDecimal:pay:本次收/支的金额*/
	private BigDecimal pay;
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
	public AccountOtherCommonMaster getMaster() {
		return master;
	}
	public void setMaster(AccountOtherCommonMaster master) {
		this.master = master;
	}
	public Bursary getBursary() {
		return bursary;
	}
	public void setBursary(Bursary bursary) {
		this.bursary = bursary;
	}
	
	public BigDecimal getPay() {
		return pay;
	}
	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	
}
