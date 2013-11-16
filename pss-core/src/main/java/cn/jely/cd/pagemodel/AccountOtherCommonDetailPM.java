/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherCommonDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-5
 */
package cn.jely.cd.pagemodel;

import java.math.BigDecimal;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-5 下午4:40:04
 */
public class AccountOtherCommonDetailPM {
	/**Long:id:主键*/
	private Long id;
	/**Integer:orders:排序号*/
	private Integer orders;
	/**Long:bursaryId:科目编号*/
	private Long bursaryId;
	/**String:bursaryName:科目名称*/
	private String bursaryName;
	/**Bursary:bursary:收/支科目*/
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
	
	public Long getBursaryId() {
		return bursaryId;
	}
	public void setBursaryId(Long bursaryId) {
		this.bursaryId = bursaryId;
	}
	public String getBursaryName() {
		return bursaryName;
	}
	public void setBursaryName(String bursaryName) {
		this.bursaryName = bursaryName;
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
