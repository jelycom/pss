/*
 * 捷利商业进销存管理系统
 * @(#)CompanyInfo.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-15
 */
package cn.jely.cd.sys.domain;

import java.util.Date;
import java.util.Locale;

import cn.jely.cd.util.CostMethod;

/**
 * @ClassName:CompanyInfo
 * @author 周义礼
 * @version 2013-4-15 上午9:24:08
 *
 */
public class CompanyInfo {
	/**@Fields name:公司名称(全)*/
	private String companyName;
	/**@Fields RegisteredAddress:注册地址*/
	private String registeredAddress;
	/**@Fields BusinessAddress:经营地址*/
	private String businessAddress;
	/**@Fields organizationCode:机构代码*/
	private String organizationCode; 
	/**@Fields corporationTax:公司税号*/
	private String corporationTax;
	/**@Fields legalRepresentative:法人代表*/
	private String legalRepresentative;
	/**@Fields businessScope:业务范围*/
	private String businessScope;
	/**String:telephoneNumber:电话号码*/
	private String telephoneNumber;
	/**String:faxNumber:传真号码*/
	private String faxNumber;
//	/**@Fields productionCostMethod:产品成本计算方式*/
//	private CostMethod defaultProductionCostMethod;
//	/**@Fields serviceCostMethod:服务成本计算方式*/
//	private CostMethod defaultServiceCostMethod;
//	/**@Fields autoClear:是否自动结算*/
//	private boolean autoClear;
//	/**@Fields clearDay:结算日*/
//	private Integer clearDay;
	/**@Fields openningDate:开帐日期 yyyy-MM*/
	/**Boolean:opened:是否已经开帐*/
	private Boolean opened = false;
	/**Date:openningDate:如果已经开帐，则有开帐日期*/
	private Date openningDate;
	//本地信息
	private Locale locale;
	/**@Fields accountingPeriod:当前会计期间*/
	private AccountingPeriod currentPeriod;
	/**@Fields memos:备注*/
	private String memos;
	
	
	public CompanyInfo() {
	}
	
	public CompanyInfo(String companyName) {
		this.companyName = companyName;
	}

	public CompanyInfo(String companyName, String registeredAddress, String businessAddress,
			String organizationCode, String corporationTax, String legalRepresentative, String businessScope,
			CostMethod defaultProductionCostMethod, CostMethod defaultServiceCostMethod, boolean autoClear,
			Integer clearDay, Date openningDate, String memos) {
		this.companyName = companyName;
		this.registeredAddress = registeredAddress;
		this.businessAddress = businessAddress;
		this.organizationCode = organizationCode;
		this.corporationTax = corporationTax;
		this.legalRepresentative = legalRepresentative;
		this.businessScope = businessScope;
//		this.defaultProductionCostMethod = defaultProductionCostMethod;
//		this.defaultServiceCostMethod = defaultServiceCostMethod;
//		this.autoClear = autoClear;
//		this.clearDay = clearDay;
		this.openningDate = openningDate;
		this.memos = memos;
	}
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getCorporationTax() {
		return corporationTax;
	}
	public void setCorporationTax(String corporationTax) {
		this.corporationTax = corporationTax;
	}
	public String getLegalRepresentative() {
		return legalRepresentative;
	}
	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public Boolean getOpened() {
		return opened;
	}

	public void setOpened(Boolean opened) {
		this.opened = opened;
	}

	public Date getOpenningDate() {
		return openningDate;
	}

	public void setOpenningDate(Date openningDate) {
		this.openningDate = openningDate;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public AccountingPeriod getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(AccountingPeriod currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
}
