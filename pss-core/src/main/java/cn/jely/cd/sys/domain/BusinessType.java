/*
 * 捷利商业进销存管理系统
 * @(#)BusinessType.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-18
 */
package cn.jely.cd.sys.domain;

import cn.jely.cd.util.PinYinUtils;

/**
 * @ClassName:BusinessType Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-18 上午11:35:20
 * 
 */
public class BusinessType {
	public static final Character ADD = '+';
	public static final Character DEC = '-';
	public static final Character NO = '0';
	public static final Character BUSINESSUNITTARGET = 'B';
	public static final Character STOCKTARGET = 'S';
	
	/** @Fields id:主键 */
	private Long id;
	/** @Fields name:业务类型名称 */
	private String name;
	/** @Fields py:助记码 */
	private String py;
	/** @Fields item:编号 */
	private String item;
	/**@Fields target:影响的目标,可为:往来单位和内部库房*/
	private Character target;
	/**@Fields stock:对库存的影响*/
	private Character stock;
	/**@Fields fund:对帐户的影响*/
	private Character fund;
	/**@Fields receivable:对应收款的影响*/
	private Character receivable;
	/**@Fields payable:对应付款的影响*/
	private Character payable;
	private String memos;
	
	
	public BusinessType() {
	}
	public BusinessType(Long id) {
		super();
		this.id = id;
	}
	
	public BusinessType(String name, Character target,Character stock, Character fund, Character receivable, Character payable) {
		this.name = name;
		this.py=PinYinUtils.getPinYinShengMu(name);
		this.target=target;
		this.stock = stock;
		this.fund = fund;
		this.receivable = receivable;
		this.payable = payable;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	public Character getTarget() {
		return target;
	}
	public void setTarget(Character target) {
		this.target = target;
	}
	public Character getStock() {
		return stock;
	}
	public void setStock(Character stock) {
		this.stock = stock;
	}
	public Character getFund() {
		return fund;
	}
	public void setFund(Character fund) {
		this.fund = fund;
	}
	public Character getReceivable() {
		return receivable;
	}
	public void setReceivable(Character receivable) {
		this.receivable = receivable;
	}
	public Character getPayable() {
		return payable;
	}
	public void setPayable(Character payable) {
		this.payable = payable;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}


}
