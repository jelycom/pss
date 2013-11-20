/*
 * 捷利商业进销存管理系统
 * @(#)productCommonMasterPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-10
 */
package cn.jely.cd.pagemodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:productCommonMasterPM
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-10 下午4:21:09
 *
 */
public class ProductCommonMasterPM implements PageModel{
	// Fields
	private Long id;
	/** 往来单位*/
	private Long businessUnitId;
	private String businessUnitName;
	/** 单位联系人*/
	private Long contactorId;
	private String contactorName;
	/** 所影响的仓库*/
	private Long warehouseId;
	private String warehouseName;
	/** 此单经手人Id*/
	private Long employeeId;
	private String employeeName;
	/** 资金帐户*/
	private Long fundAccountId;
	private String fundAccountName;
	/** 发票类型*/
	private Long invoiceTypeId;
	private String invoiceTypeName;
	/** 发票号码*/
	private String invoiceNo;
	/** 单据编号 */
	private String item;
	/** 业务单据发生日期*/
	private Date billDate;
	/** 小计(不含税)*/
	private BigDecimal subTotal;
	/** 税费*/
	private BigDecimal valueAddTax;
	/** 预付款*/
	private BigDecimal prepare;
	/** 总计(小计+税费)*/
	private BigDecimal amount;
	/** 已付,在以下情况amount将不等于arap:一单总金额为100元,先付了20元,那么应收应付就有80元
	 * 则此时:amount:100,paid:20,arap:80
	 * 如果在之前还有订单,并且订单也付了20元订金,则选择此单据后的各属性为:amount:100,paid:20,
	 * ,下次冲销的时候将此单的arap及paidArap进行运算即可得出还有多少未付*/
	private BigDecimal paid;
	/** 总计(应收应付)*/
	protected BigDecimal arap;
	/** 已收/付(应收应付)*/
	private BigDecimal paidArap;
	private BigDecimal remainArap;
	/** 商品明细*/
	private List<ProductCommonDetailPM> details=new ArrayList<ProductCommonDetailPM>();
	/** 备注*/
	private String memos;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public void setContactorName(String cotactorName) {
		this.contactorName = cotactorName;
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
	public Long getInvoiceTypeId() {
		return invoiceTypeId;
	}
	public void setInvoiceTypeId(Long invoicTypeId) {
		this.invoiceTypeId = invoicTypeId;
	}
	public String getInvoceTypeName() {
		return invoiceTypeName;
	}
	public void setInvoiceTypeName(String invocTypeName) {
		this.invoiceTypeName = invocTypeName;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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
	public BigDecimal getPaidArap() {
		return paidArap;
	}
	public void setPaidArap(BigDecimal paidArap) {
		this.paidArap = paidArap;
	}
	public List<ProductCommonDetailPM> getDetails() {
		return details;
	}
	public void setDetails(List<ProductCommonDetailPM> details) {
		this.details = details;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	public String getFundAccountName() {
		return fundAccountName;
	}
	public void setFundAccountName(String fundAccountName) {
		this.fundAccountName = fundAccountName;
	}
	public BigDecimal getPrepare() {
		return prepare;
	}
	public void setPrepare(BigDecimal prepare) {
		this.prepare = prepare;
	}
	public BigDecimal getArap() {
		return arap;
	}
	public void setArap(BigDecimal arap) {
		this.arap = arap;
	}
	public BigDecimal getRemainArap() {
		return remainArap;
	}
	public void setRemainArap(BigDecimal remainArap) {
		this.remainArap = remainArap;
	}
}
