/*
 * 捷利商业进销存管理系统
 * @(#)PeriodAccount.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-7
 */
package cn.jely.cd.sys.domain;

import java.math.BigDecimal;

import cn.jely.cd.domain.FundAccount;

/**
 * 期间帐户表:用于记录每个结帐期间的帐户期初数,实时数.
 * 
 * @ClassName:PeriodAccount Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-7 下午6:25:57
 * 
 */
public class PeriodAccount {

	private Long id;
	/** @Fields accountingPeriod:所属期间 */
	private AccountingPeriod accountingPeriod;
	/** @Fields fundAccount:帐户 */
	private FundAccount fundAccount;
	/** @Fields begin:期初金额 */
	private BigDecimal begin;
	/** @Fields begin:实时金额 */
	private BigDecimal current;

	public PeriodAccount() {
	}

	public PeriodAccount(FundAccount fundAccount, BigDecimal begin) {
		this.fundAccount = fundAccount;
		this.begin = begin;
		this.current = begin;
	}

	
	public PeriodAccount(AccountingPeriod accountingPeriod, FundAccount fundAccount, BigDecimal begin) {
		this.accountingPeriod = accountingPeriod;
		this.fundAccount = fundAccount;
		this.begin = begin;
		this.current = begin;
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

	public FundAccount getFundAccount() {
		return fundAccount;
	}

	public void setFundAccount(FundAccount fundAccount) {
		this.fundAccount = fundAccount;
	}

	public BigDecimal getBegin() {
		return begin;
	}

	public void setBegin(BigDecimal begin) {
		this.begin = begin;
	}

	public BigDecimal getCurrent() {
		return current;
	}

	public void setCurrent(BigDecimal current) {
		this.current = current;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodAccount other = (PeriodAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
