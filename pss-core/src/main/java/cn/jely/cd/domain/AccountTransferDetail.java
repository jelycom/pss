/*
 * 捷利商业进销存管理系统
 * @(#)AccountTransferDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-20
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

/**
 * @ClassName:AccountTransferDetail Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-20 上午11:00:57
 * 
 */
public class AccountTransferDetail {
	private Long id;
	/**AccountTransferMaster:master:转款主表*/
	private AccountTransferMaster master;
	/**Integer:orders:序号*/
	private Integer orders;
	/**FundAccount:account:转入帐户*/
	private FundAccount inAccount;
	/**BigDecimal:inAmount:转入金额*/
	private BigDecimal inAmount;
	private String memos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AccountTransferMaster getMaster() {
		return master;
	}

	public void setMaster(AccountTransferMaster master) {
		this.master = master;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public FundAccount getInAccount() {
		return inAccount;
	}

	public void setInAccount(FundAccount inAccount) {
		this.inAccount = inAccount;
	}

	public BigDecimal getInAmount() {
		return inAmount;
	}

	public void setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

}
