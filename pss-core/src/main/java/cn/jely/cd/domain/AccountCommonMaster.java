/*
 * 捷利商业进销存管理系统
 * @(#)AccountCommonMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-2
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import cn.jely.cd.pagemodel.AccountCommonDetailPM;
import cn.jely.cd.pagemodel.AccountCommonMasterPM;
import cn.jely.cd.pagemodel.ToPageModel;
import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * 帐户收付款主表类
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-2 下午4:11:19
 * 
 */
public class AccountCommonMaster implements java.io.Serializable, IStateAble, IItemAble,
		ToPageModel<AccountCommonMasterPM>, Cloneable {
	private Long id;
	private BusinessUnits businessUnit;
	/** @Fields employee:经手人 */
	private Employee employee;
	/** @Fields fundAccount:收付款帐户 */
	private FundAccount fundAccount;
	/** @Fields item:编号 */
	private String item;
	/** 单据发生日期 */
	private Date billDate;
	/** @Fields discount:优惠,折让 */
	private BigDecimal discount;
	/** @Fields amount:收/付款金额 */
	private BigDecimal amount;
	/** State:state:单据的状态 */
	private State state;
	/** @Fields memos:备注 */
	private String memos;
	/** 帐户收付款明细 */
	private List<AccountCommonDetail> details = new ArrayList<AccountCommonDetail>();

	private InfoComponent info = new InfoComponent();

	public AccountCommonMaster() {
	}

	public AccountCommonMaster(Long id) {
		this.id = id;
	}

	public AccountCommonMaster(BusinessUnits businessUnit, Employee employee, FundAccount fundAccount, Date date,
			BigDecimal amount, BigDecimal discount) {
		this.businessUnit = businessUnit;
		this.employee = employee;
		this.fundAccount = fundAccount;
		this.billDate = date;
		this.discount = discount;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BusinessUnits getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(BusinessUnits businessUnit) {
		this.businessUnit = businessUnit;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
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

	public List<AccountCommonDetail> getDetails() {
		return details;
	}

	public void setDetails(List<AccountCommonDetail> details) {
		this.details = details;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
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

	public InfoComponent getInfo() {
		return info;
	}

	public void setInfo(InfoComponent info) {
		this.info = info;
	}

	public AccountCommonMaster toDisp(boolean withDetail) {
		AccountCommonMaster master = null;
		try {
			master = (AccountCommonMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setBusinessUnit(new BusinessUnits(this.getBusinessUnit().getId(), this.getBusinessUnit().getName()));
		master.setEmployee(new Employee(this.getEmployee().getId(), this.getEmployee().getName()));
		if (this.fundAccount != null) {
			master.setFundAccount(new FundAccount(this.getFundAccount().getId(), this.getFundAccount().getName()));
		}
		if (withDetail) {
			List<AccountCommonDetail> details = master.getDetails();
			int size = details.size();
			for (int i = 0; i < size; i++) {
				AccountCommonDetail masterdetail = details.get(i);
				masterdetail.setMaster(null);
				ProductCommonMaster productMaster = masterdetail.getProductMaster();
				masterdetail.setProductMaster(productMaster.toDisp(false));
//				productMaster.getProductOrderBills().clear();
//				productMaster.getProductPlans().clear();
				// master.getDetails().add(masterdetail);
			}
		} else {
			master.getDetails().clear();
		}
		return master;
	}

	@Override
	public AccountCommonMasterPM convert(boolean withDetail) {
		AccountCommonMasterPM masterPM = new AccountCommonMasterPM();
		org.springframework.beans.BeanUtils.copyProperties(this, masterPM, new String[] { "details" });
		BusinessUnits businessUnit = this.getBusinessUnit();
		FundAccount fd = this.getFundAccount();
		Employee employee = this.getEmployee();
		masterPM.setBusinessUnitId(businessUnit.getId());
		masterPM.setBusinessUnitName(businessUnit.getName());
		masterPM.setEmployeeId(employee.getId());
		masterPM.setEmployeeName(employee.getName());
		if (fd != null) {
			masterPM.setFundAccountId(fd.getId());
			masterPM.setFundAccountName(fd.getName());
		}
		if (withDetail) {
			List<AccountCommonDetail> details = this.getDetails();
			int size = details.size();
			for (int i = 0; i < size; i++) {
				AccountCommonDetailPM detailpm = new AccountCommonDetailPM();
				AccountCommonDetail detail = details.get(i);
				org.springframework.beans.BeanUtils.copyProperties(detail, detailpm);
				detailpm.setMasterDate(detail.getProductMaster().getBillDate());
				detailpm.setMasterItem(detail.getProductMaster().getItem());
				masterPM.getDetails().add(detailpm);
			}
		}
		return masterPM;
	}

}
