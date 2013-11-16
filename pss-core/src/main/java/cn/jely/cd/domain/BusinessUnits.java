package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * Businessunits entity. @author MyEclipse Persistence Tools
 */

public class BusinessUnits implements java.io.Serializable, IPinYinLogic {

	// Fields

	private Long id;
	private String item;
	/**String:shortName:单位简称,必须要有值*/
	private String shortName;
	private String py;
	private String name;
	private String unitCode;
	private String owner;
	private String rocId;
	private String contactPhone1;
	private String contactPhone2;
	private String fax;
	private String address;
	private String deliveryAddress;
	private String invoiceAddress;
	private String invoiceType;
	private String bankName;
	private String paymentTerm;
	private String account;
	private String memos;
	private Date receiveDate;
	private Integer receivedays;
	private Double creditLine;
	private Double creditBalance;
	private Date payDate;
	private Integer payDays;
	private Date lastPurchaseDate;
	private Date lastDeliveryDate;
	private Region region;
	private BaseData supplierLevel;
	private BaseData supplierType;
	private BaseData customerLevel;
	private BaseData customerType;
	/**@Fields currentPayAble:当前应付款*/
	private BigDecimal currentPayAble;
	/**@Fields currentReceiveAble:当前应收款*/
	private BigDecimal currentReceiveAble;
	/**BigDecimal:currentAdvance:当前暂(预)收款*/
	private BigDecimal currentAdvance;
	/**BigDecimal:currentPrepaid:当前暂(预)付款*/
	private BigDecimal currentPrepaid;
	private List<Contacts> contactors=new ArrayList<Contacts>();
	// Constructors

	/** default constructor */
	public BusinessUnits() {
	}

	
	public BusinessUnits(Long id) {
		this.id = id;
	}
	
	public BusinessUnits(Long id,String name) {
		this.id = id;
		this.name = name;
	}

	public BusinessUnits(String shortName, String name) {
		this.shortName = shortName;
		this.name = name;
	}

	/** minimal constructor */
	public BusinessUnits(String item, String shortName, String fullName) {
		this.item = item;
		this.shortName = shortName;
		this.name = fullName;
	}

	/** full constructor */
	public BusinessUnits(Region region, String item, String shortName, String shortPy, String fullName,
			String unitCode, String owner, String rocId, String contactPhone1, String contactPhone2, String fax,
			String address, String deliveryAddress, String invoiceAddress, String invoiceType, String bankName,
			String paymentTerm, String account, String memos, Date receiveDate, Integer receivedays, Double creditLine,
			Double creditBalance, Date payDate, Integer payDays) {
		this.region = region;
		this.item = item;
		this.shortName = shortName;
		this.py = shortPy;
		this.name = fullName;
		this.unitCode = unitCode;
		this.owner = owner;
		this.rocId = rocId;
		this.contactPhone1 = contactPhone1;
		this.contactPhone2 = contactPhone2;
		this.fax = fax;
		this.address = address;
		this.deliveryAddress = deliveryAddress;
		this.invoiceAddress = invoiceAddress;
		this.invoiceType = invoiceType;
		this.bankName = bankName;
		this.paymentTerm = paymentTerm;
		this.account = account;
		this.memos = memos;
		this.receiveDate = receiveDate;
		this.receivedays = receivedays;
		this.creditLine = creditLine;
		this.creditBalance = creditBalance;
		this.payDate = payDate;
		this.payDays = payDays;
	}

	// Property accessors

	public String getItem() {
		return this.item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPy() {
		return py;
	}

	@Override
	public void setPy(String py) {
		this.py = py;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getRocId() {
		return this.rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getContactPhone1() {
		return this.contactPhone1;
	}

	public void setContactPhone1(String contactPhone1) {
		this.contactPhone1 = contactPhone1;
	}

	public String getContactPhone2() {
		return this.contactPhone2;
	}

	public void setContactPhone2(String contactPhone2) {
		this.contactPhone2 = contactPhone2;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeliveryAddress() {
		return this.deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getInvoiceAddress() {
		return this.invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getPaymentTerm() {
		return this.paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMemos() {
		return this.memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public Integer getReceivedays() {
		return this.receivedays;
	}

	public void setReceivedays(Integer receivedays) {
		this.receivedays = receivedays;
	}

	public Double getCreditLine() {
		return this.creditLine;
	}

	public void setCreditLine(Double creditLine) {
		this.creditLine = creditLine;
	}

	public Double getCreditBalance() {
		return this.creditBalance;
	}

	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}

	public Integer getPayDays() {
		return this.payDays;
	}

	public void setPayDays(Integer payDays) {
		this.payDays = payDays;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public BaseData getSupplierLevel() {
		return supplierLevel;
	}

	public void setSupplierLevel(BaseData supplierLevel) {
		this.supplierLevel = supplierLevel;
	}

	public BaseData getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(BaseData supplierType) {
		this.supplierType = supplierType;
	}

	public BaseData getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(BaseData customerLevel) {
		this.customerLevel = customerLevel;
	}

	public BaseData getCustomerType() {
		return customerType;
	}

	public void setCustomerType(BaseData customerType) {
		this.customerType = customerType;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getLastPurchaseDate() {
		return lastPurchaseDate;
	}

	public void setLastPurchaseDate(Date lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getLastDeliveryDate() {
		return lastDeliveryDate;
	}

	public void setLastDeliveryDate(Date lastDeliveryDate) {
		this.lastDeliveryDate = lastDeliveryDate;
	}

	public BigDecimal getCurrentPayAble() {
		return currentPayAble;
	}

	public void setCurrentPayAble(BigDecimal currentPayAble) {
		this.currentPayAble = currentPayAble;
	}

	public BigDecimal getCurrentReceiveAble() {
		return currentReceiveAble;
	}

	public void setCurrentReceiveAble(BigDecimal currentReceiveAble) {
		this.currentReceiveAble = currentReceiveAble;
	}

	@Override
	public String pySource() {
		return shortName;
	}

	@Override
	public boolean checkBlank() {
		return StringUtils.isBlank(py);
	}

	public List<Contacts> getContactors() {
		return contactors;
	}

	public void setContactors(List<Contacts> contactors) {
		this.contactors = contactors;
	}
	public BigDecimal getCurrentAdvance() {
		return currentAdvance;
	}
	public void setCurrentAdvance(BigDecimal currentAdvance) {
		this.currentAdvance = currentAdvance;
	}
	public BigDecimal getCurrentPrepaid() {
		return currentPrepaid;
	}
	public void setCurrentPrepaid(BigDecimal currentPrepaid) {
		this.currentPrepaid = currentPrepaid;
	}
	
}