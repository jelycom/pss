package cn.jely.cd.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * Warehouse entity. @author MyEclipse Persistence Tools
 */

public class Warehouse implements java.io.Serializable ,IPinYinLogic{

	// Fields

	private Long id;
	private Region region;
	private String item;
	private String name;
	private String py;
	private String address;
	private String memos;

	// Constructors

	/** default constructor */
	public Warehouse() {
	}

	
	public Warehouse(Long id) {
		this.id = id;
	}


	public Warehouse(String name) {
		this.name = name;
	}


	/** full constructor */
	public Warehouse(Region region, String item, String name, String py,
			String address, String memos) {
		this.region = region;
		this.item = item;
		this.name = name;
		this.py = py;
		this.address = address;
		this.memos = memos;
	}
	public Warehouse(Long id, String name) {
		this.id=id;
		this.name=name;
	}

	// Property accessors


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

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMemos() {
		return this.memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
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
		return StringUtils.isBlank(this.py);
	}


}