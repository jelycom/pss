package cn.jely.cd.domain;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * 资金帐户实体类,包括现金帐户以及银行帐户,现金以及银行帐户中只能各设置一个默认帐户 默认的帐户不能停用. FundAccount entity. @author
 * MyEclipse Persistence Tools
 */

public class FundAccount implements java.io.Serializable, IPinYinLogic {

	// Fields

	private Long id;
	/** @Fields item:帐户编号 */
	private String item;
	/** @Fields item:名称 */
	private String name;
	/** @Fields item:拼音码 */
	private String py;
	/** @Fields item:帐户号码 */
	private String accountNo;
	/** @Fields item:银行名称 */
	private String bankName;
	/**
	 * @Fields 
	 *         item:是否银行帐户,mask:已解决:用String是因为Struts2不能将1,0转换为true,false,又不想自己定义转换器
	 */
	private Boolean isBank = false;
	/** @Fields item:是否默认帐户 */
	private Boolean isDefault = false;
	/** @Fields item:是否失效 */
	private Boolean invalid = false;
	/** 帐户当前金额 */
	private BigDecimal current;

	// Constructors

	/** default constructor */
	public FundAccount() {
	}

	/** minimal constructor */
	public FundAccount(String name, String py) {
		this.name = name;
		this.py = py;
	}

	public FundAccount(String item, String name, String py, String accountNo, String bankName) {
		super();
		this.item = item;
		this.name = name;
		this.py = py;
		this.accountNo = accountNo;
		this.bankName = bankName;
	}

	/** 构造现金帐户 */
	public FundAccount(String name) {
		super();
		this.name = name;
	}

	// Property accessors

	public FundAccount(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getItem() {
		return this.item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPy() {
		return this.py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getIsBank() {
		return isBank;
	}

	public void setIsBank(boolean isBank) {
		this.isBank = isBank;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

	public BigDecimal getCurrent() {
		return current;
	}

	public void setCurrent(BigDecimal current) {
		this.current = current;
	}

	@Override
	public String pySource() {
		return this.name;
	}

	@Override
	public boolean checkBlank() {
		return StringUtils.isBlank(this.py);
	}

}