/*
 * 捷利商业进销存管理系统
 * @(#)PeriodARAP.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-8
 */
package cn.jely.cd.sys.domain;

import java.math.BigDecimal;

import cn.jely.cd.domain.BusinessUnits;

/**
 * 期初及结帐期间应收应付款
 * @ClassName:PeriodARAP
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-6-8 下午2:58:41
 *
 */
public class PeriodARAP {
	
	private Long id;
	/**@Fields accountingPeriod:结帐期间(期初或某一期间)*/
	private AccountingPeriod accountingPeriod;
	/**@Fields businessUnits:应收应付所属单位*/
	private BusinessUnits businessUnits;
	/**@Fields receivable:应收款*/
	private BigDecimal receivable;
	/**@Fields payable:应付款*/
	private BigDecimal payable;
	/**BigDecimal:advance:预收款/暂收款*/
	private BigDecimal advance;
	/**BigDecimal:prepaid:预付款/暂付款*/
	private BigDecimal prepaid;
	
	public PeriodARAP(Long id) {
		this.id = id;
	}


	public PeriodARAP(BusinessUnits businessUnits, BigDecimal receivable, BigDecimal payable) {
		this.businessUnits = businessUnits;
		this.receivable = receivable;
		this.payable = payable;
	}
	
	public PeriodARAP(BusinessUnits businessUnits, Integer receivable, Integer payable) {
		this.businessUnits = businessUnits;
		this.receivable = new BigDecimal(receivable);
		this.payable = new BigDecimal(payable);
	}
	
	
	public PeriodARAP() {
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AccountingPeriod getAccountingPeriod() {
		return accountingPeriod;
	}
	public void setAccountingPeriod(AccountingPeriod accountingPeriod) {
		this.accountingPeriod = accountingPeriod;
	}
	public BusinessUnits getBusinessUnits() {
		return businessUnits;
	}
	public void setBusinessUnits(BusinessUnits businessUnits) {
		this.businessUnits = businessUnits;
	}
	public BigDecimal getReceivable() {
		return receivable;
	}
	public void setReceivable(BigDecimal receivable) {
		this.receivable = receivable;
	}
	public BigDecimal getPayable() {
		return payable;
	}
	public void setPayable(BigDecimal payable) {
		this.payable = payable;
	}
	public BigDecimal getAdvance() {
		return advance;
	}
	public void setAdvance(BigDecimal advance) {
		this.advance = advance;
	}
	public BigDecimal getPrepaid() {
		return prepaid;
	}
	public void setPrepaid(BigDecimal prepaid) {
		this.prepaid = prepaid;
	}
	
	
}
