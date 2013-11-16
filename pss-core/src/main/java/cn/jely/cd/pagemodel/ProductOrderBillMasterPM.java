/*
 * 捷利商业进销存管理系统
 * @(#)ProductOrderBillMasterPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-16
 */
package cn.jely.cd.pagemodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import cn.jely.cd.domain.BaseData;
import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductOrderBillMasterPM
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-16 下午4:46:28
 *
 */
public class ProductOrderBillMasterPM implements PageModel{
	// Fields
	private Long id;
	/** 单据编号 */
	private String item;
	/** 往来单位*/
	private Long businessUnitId;
	private String businessUnitName;
	/** 单位联系人*/
	private Long contactorId;
	private String contactorName;
	/** 所属进货仓库*/
	private Long warehouseId;
	private String warehouseName;
	/** 此单经手人*/
	private Long employeeId;
	private String employeeName;
	/** 资金帐户*/
	private Long fundAccountId;
	private String fundAccountName;
	/** 业务类型*/
	private BaseData businessType;
	/** 发票类型*/
	private Long invoiceTypeId;
	private String invoiceTypeName;
	/** 发票号码*/
	private String invoiceNo;
	/** 单据所属会计期间*/
	private AccountingPeriod accountingPeriodId;
	/** 到货单号*/
	private String deliveryItem;
	/** 定货时间 */
	private Date billDate;
	/** 到(交)货时间*/
	private Date deliveryDate;
	/** 到(交)货地址*/
	private String deliveryAddress;
	/** 小计(不含税)*/
	private BigDecimal subTotal;
	/** 税费*/
	private BigDecimal valueAddTax;
	/** 总计(小计+税费)*/
	private BigDecimal amount;
	/** 已付金额*/
	private BigDecimal paid;
	/** 总计(应收应付)付采购定单是应收,销售定单是应付,作为进出货时冲减总金额*/
	private BigDecimal arap;
//	/** 总计(剩余应收应付)*/
	private BigDecimal paidArap;
	/** 备注*/
	private String memos;
	/** 状态*/
	private State state;
	/** 订单明细*/
	private List<ProductOrderBillDetailPM> details=new ArrayList<ProductOrderBillDetailPM>();
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
	public Long getContactorId() {
		return contactorId;
	}
	public void setContactorId(Long contactorId) {
		this.contactorId = contactorId;
	}
	public String getContactorName() {
		return contactorName;
	}
	public void setContactorName(String contactorName) {
		this.contactorName = contactorName;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
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
	public BaseData getBusinessType() {
		return businessType;
	}
	public void setBusinessType(BaseData businessType) {
		this.businessType = businessType;
	}

	public Long getInvoiceTypeId() {
		return invoiceTypeId;
	}
	public void setInvoiceTypeId(Long invoiceTypeId) {
		this.invoiceTypeId = invoiceTypeId;
	}
	public String getInvoiceTypeName() {
		return invoiceTypeName;
	}
	public void setInvoiceTypeName(String invoiceTypeName) {
		this.invoiceTypeName = invoiceTypeName;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public AccountingPeriod getAccountingPeriodId() {
		return accountingPeriodId;
	}
	public void setAccountingPeriodId(AccountingPeriod accountingPeriodId) {
		this.accountingPeriodId = accountingPeriodId;
	}
	public String getDeliveryItem() {
		return deliveryItem;
	}
	public void setDeliveryItem(String deliveryItem) {
		this.deliveryItem = deliveryItem;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date orderDate) {
		this.billDate = orderDate;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getValueAddTax() {
		return valueAddTax;
	}
	public void setValueAddTax(BigDecimal valueAddTax) {
		this.valueAddTax = valueAddTax;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getPaid() {
		return paid;
	}
	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}
	public BigDecimal getArap() {
		return arap;
	}
	public void setArap(BigDecimal arap) {
		this.arap = arap;
	}
	public BigDecimal getPaidArap() {
		return paidArap;
	}
	public void setPaidArap(BigDecimal paidArap) {
		this.paidArap = paidArap;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public List<ProductOrderBillDetailPM> getDetails() {
		return details;
	}
	public void setDetails(List<ProductOrderBillDetailPM> details) {
		this.details = details;
	}

}
