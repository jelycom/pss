/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherCommonMasterPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-5
 */
package cn.jely.cd.pagemodel;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import cn.jely.cd.util.state.State;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-8-5 下午4:59:51
 */
public class AccountOtherCommonMasterPM implements PageModel {
	
	private Long id;
	/** @Fields item:编号 */
	private String item;
	/** 单据发生日期 */
	private Date billDate;
	private Long businessUnitId;
	private String businessUnitName;
	/** @Fields employee:经手人 */
	private Long employeeId;
	private String employeeName;
	/** @Fields fundAccount:收付款帐户 */
	private Long fundAccountId;
	private String fundAccountName;
	/** @Fields amount:收/付款金额 */
	private BigDecimal amount;
	/** State:state:单据的状态 */
	private State state;
	/** @Fields memos:备注 */
	private String memos;
	/** 帐户收付款明细 */
	private List<AccountOtherCommonDetailPM> details = new ArrayList<AccountOtherCommonDetailPM>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Long getBusinessUnitId() {
		return businessUnitId;
	}
	public void setBusinessUnitId(Long businessUnitId) {
		this.businessUnitId = businessUnitId;
	}
	public String getBusinessUnitName() {
		return businessUnitName;
	}
	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getFundAccountId() {
		return fundAccountId;
	}
	public void setFundAccountId(Long fundAccountId) {
		this.fundAccountId = fundAccountId;
	}
	public String getFundAccountName() {
		return fundAccountName;
	}
	public void setFundAccountName(String fundAccountName) {
		this.fundAccountName = fundAccountName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	public List<AccountOtherCommonDetailPM> getDetails() {
		return details;
	}
	public void setDetails(List<AccountOtherCommonDetailPM> details) {
		this.details = details;
	}
	
}
