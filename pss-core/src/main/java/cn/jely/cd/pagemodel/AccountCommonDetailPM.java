/*
 * 捷利商业进销存管理系统
 * @(#)AccountCommonDetailPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-16
 */
package cn.jely.cd.pagemodel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName:AccountCommonDetailPM
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-16 下午2:35:16
 *
 */
public class AccountCommonDetailPM implements PageModel {
	private Long id;
	/**@Fields orders: 明细序号*/
	private Integer orders;
	/**@Fields productMaster:产品进/出业务主表编号*/
	private String masterItem;
	/**Date:masterDate:进/出业务日期*/
	private Date masterDate;
	/**@Fields currentPay:应收/应付金额*/
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

	public String getMasterItem() {
		return masterItem;
	}
	public void setMasterItem(String masterItem) {
		this.masterItem = masterItem;
	}
	public Date getMasterDate() {
		return masterDate;
	}
	public void setMasterDate(Date masterDate) {
		this.masterDate = masterDate;
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
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
}
