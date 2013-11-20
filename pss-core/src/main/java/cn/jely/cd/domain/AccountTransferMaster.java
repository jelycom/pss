/*
 * 捷利商业进销存管理系统
 * @(#)AccountTransferMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-19
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jely.cd.pagemodel.AccountTransferMasterPM;
import cn.jely.cd.pagemodel.ToPageModel;
import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * 帐户互转
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-19 下午11:34:51
 * 
 */
public class AccountTransferMaster implements Cloneable,IStateAble,IItemAble,ToPageModel<AccountTransferMasterPM>{
	private Long id;
	private String item;
	private Date billDate;
	private FundAccount fundAccount;
	private BigDecimal amount;
	private Employee employee;
	/**BigDecimal:cost:手续费用*/
	private BigDecimal cost;
	private List<AccountTransferDetail> details = new ArrayList<AccountTransferDetail>();
	private State state;
	private InfoComponent info=new InfoComponent();
	private String memos;

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

	public FundAccount getFundAccount() {
		return fundAccount;
	}

	public void setFundAccount(FundAccount fundAccount) {
		this.fundAccount = fundAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
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

	public List<AccountTransferDetail> getDetails() {
		return details;
	}

	public void setDetails(List<AccountTransferDetail> details) {
		this.details = details;
	}

	public InfoComponent getInfo() {
		return info;
	}

	public void setInfo(InfoComponent info) {
		this.info = info;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public AccountTransferMasterPM convert(boolean withDetail) {
		return null;
	}

	/**
	 *
	 * @param b
	 * @return String
	 */
	public AccountTransferMaster toDisp(boolean withDetail) {
		AccountTransferMaster master = null;
		try {
			master = (AccountTransferMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setEmployee(new Employee(this.getEmployee().getId(), this.getEmployee().getName()));
		master.setFundAccount(new FundAccount(this.getFundAccount().getId(), this.getFundAccount().getName()));
		if (withDetail) {
			List<AccountTransferDetail> details = master.getDetails();
			int size = details.size();
			for (int i = 0; i < size; i++) {
				AccountTransferDetail masterdetail = details.get(i);
				masterdetail.setMaster(null);
				FundAccount inAccount = new FundAccount(masterdetail.getInAccount().getId(), masterdetail.getInAccount().getName());
				masterdetail.setInAccount(inAccount);
			}
		} else {
			master.getDetails().clear();
		}
		return master;
	}
	
}
