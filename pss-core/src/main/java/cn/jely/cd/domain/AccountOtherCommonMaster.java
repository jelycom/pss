/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherInMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-20
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jely.cd.pagemodel.AccountOtherCommonDetailPM;
import cn.jely.cd.pagemodel.AccountOtherCommonMasterPM;
import cn.jely.cd.pagemodel.ToPageModel;
import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * 帐户其它收入/支出
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-20 下午12:11:31
 */
public class AccountOtherCommonMaster implements IStateAble,IItemAble,Cloneable,ToPageModel<AccountOtherCommonMasterPM>{

	private Long id;
	/** String:item:单据编号 */
	private String item;
	/**Date:billdate:单据日期*/
	private Date billDate;
	/** BusinessUnits:businessUnit:往来单位 */
	private BusinessUnits businessUnit;
	/**Employee:employee:经手人*/
	private Employee employee;
	/** FundAccount:account:收/付款帐户 */
	private FundAccount fundAccount;
	/** State:state:单据状态 */
	private State state;
	/** String:memos:单据备注 */
	private String memos;
	/** BigDecimal:amount:收/付款金额 */
	private BigDecimal amount;
	/** BigDecimal:discount:优惠/折扣 */
	private BigDecimal discount;
	/**InfoComponent:info:记录信息*/
	private InfoComponent info=new InfoComponent();
	/**List<AccountOtherCommonDetail>:details:明细数据*/
	private List<AccountOtherCommonDetail> details=new ArrayList<AccountOtherCommonDetail>();
	
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

	public FundAccount getFundAccount() {
		return fundAccount;
	}

	public void setFundAccount(FundAccount fundAccount) {
		this.fundAccount = fundAccount;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
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

	public InfoComponent getInfo() {
		return info;
	}

	public void setInfo(InfoComponent info) {
		this.info = info;
	}
	
	public List<AccountOtherCommonDetail> getDetails() {
		return details;
	}

	public void setDetails(List<AccountOtherCommonDetail> details) {
		this.details = details;
	}

	public AccountOtherCommonMaster toDisp(boolean withDetail) {
		AccountOtherCommonMaster master = null;
		try {
			master = (AccountOtherCommonMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setBusinessUnit(new BusinessUnits(this.getBusinessUnit().getId(), this.getBusinessUnit().getName()));
		master.setEmployee(new Employee(this.getEmployee().getId(), this.getEmployee().getName()));
		if (this.fundAccount != null) {
			master.setFundAccount(new FundAccount(this.getFundAccount().getId(), this.getFundAccount().getName()));
		}
		if (withDetail) {
			List<AccountOtherCommonDetail> details = master.getDetails();
			int size = details.size();
			for (int i = 0; i < size; i++) {
				AccountOtherCommonDetail masterdetail = details.get(i);
				masterdetail.setMaster(null);
				Bursary bursary=new Bursary(masterdetail.getBursary().getId(),masterdetail.getBursary().getFullName());
				masterdetail.setBursary(bursary);
			}
		} else {
			master.getDetails().clear();
		}
		return master;
	}
	
	@Override
	public AccountOtherCommonMasterPM convert(boolean withDetail) {
		AccountOtherCommonMasterPM masterPM=new AccountOtherCommonMasterPM();
		org.springframework.beans.BeanUtils.copyProperties(this,masterPM,new String[]{"details"});
		BusinessUnits businessUnit = this.getBusinessUnit();
		FundAccount fd=this.getFundAccount();
		Employee employee = this.getEmployee();
		masterPM.setBusinessUnitId(businessUnit.getId());
		masterPM.setBusinessUnitName(businessUnit.getName());
		masterPM.setEmployeeId(employee.getId());
		masterPM.setEmployeeName(employee.getName());
		if(fd!=null){
			masterPM.setFundAccountId(fd.getId());
			masterPM.setFundAccountName(fd.getName());
		}
		if(withDetail){
			List<AccountOtherCommonDetail> details = this.getDetails();
			int size = details.size();
			for(int i=0;i<size;i++){
				AccountOtherCommonDetailPM detailpm=new AccountOtherCommonDetailPM();
				AccountOtherCommonDetail detail=details.get(i);
				org.springframework.beans.BeanUtils.copyProperties(detail, detailpm);
				detailpm.setBursaryId(detail.getBursary().getId());
				detailpm.setBursaryName(detail.getBursary().getFullName());
				masterPM.getDetails().add(detailpm);
			}
		}
		return masterPM;
	}

}
