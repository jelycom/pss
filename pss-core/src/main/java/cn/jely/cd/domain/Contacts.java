package cn.jely.cd.domain;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import cn.jely.cd.util.PinYinUtils;
import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * 住来单位对应的联系人 Contactor entity. @author MyEclipse Persistence Tools
 */

public class Contacts implements java.io.Serializable, IPinYinLogic {

	// Fields

	private Long id;
	private BusinessUnits businessUnit;
	private Integer orders;
	private String name;
	private String py;
	private String englishName;
	private String title;
	private String phone;
	private String mobilePhone;
	private String email;
	private Date birthday;
	private String dateDescription;

	// Constructors

	/** default constructor */
	public Contacts() {
	}

	public Contacts(BusinessUnits businessUnit, String name) {
		this.businessUnit = businessUnit;
		this.name = name;
		this.py = PinYinUtils.getPinYinShengMu(name);
	}

	/** minimal constructor */
	public Contacts(BusinessUnits businessunit, String name, String py, String englishName, String title, String phone,
			String mobilePhone, String email, String dateDescription) {
		this.businessUnit = businessunit;
		this.name = name;
		this.py = py;
		this.englishName = englishName;
		this.title = title;
		this.phone = phone;
		this.mobilePhone = mobilePhone;
		this.email = email;
		this.dateDescription = dateDescription;
	}

	/** full constructor */
	public Contacts(BusinessUnits businessunit, String name, String py, String englishName, String title, String phone,
			String mobilePhone, String email, Date redLetterDate, String dateDescription) {
		this.businessUnit = businessunit;
		this.name = name;
		this.py = py;
		this.englishName = englishName;
		this.title = title;
		this.phone = phone;
		this.mobilePhone = mobilePhone;
		this.email = email;
		this.birthday = redLetterDate;
		this.dateDescription = dateDescription;
	}

	// Property accessors
	public Contacts(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public BusinessUnits getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(BusinessUnits businessUnit) {
		this.businessUnit = businessUnit;
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

	public String getEnglishName() {
		return this.englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getDateDescription() {
		return this.dateDescription;
	}

	public void setDateDescription(String dateDescription) {
		this.dateDescription = dateDescription;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String pySource() {
		return this.name;
	}

	@Override
	public boolean checkBlank() {
		return StringUtils.isBlank(py);
	}

}