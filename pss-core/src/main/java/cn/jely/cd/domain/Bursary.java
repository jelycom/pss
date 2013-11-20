/*
 * 捷利商业进销存管理系统
 * @(#)Bursary.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-31
 */
package cn.jely.cd.domain;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.PinYinUtils;
import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * 会计科目表
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-31 下午2:09:32
 */
public class Bursary extends LftRgtTreeNode implements IPinYinLogic {

	public static final String CASH_ITEM="1001";
	public static final String BANK_ITEM="1002";
	public static final String OTHERINCOME_ITEM="6051";
	public static final String OTHEROUTCOME_ITEM="6402";
	
	private Long id;
	private String item;
	private String fullName;
	private String shortName;
	private String py;
	private String memos;
	/**boolean:sys:是否系统科目*/
	private boolean sys;

	public Bursary() {
	}

	public Bursary(String item, String fullName, String shortName) {
		this.item = item;
		this.fullName = fullName;
		this.shortName = shortName;
		this.py=PinYinUtils.getPinYinShengMu(fullName);
	}

	public Bursary(String item, String fullName, String shortName, boolean sys) {
		this.item = item;
		this.fullName = fullName;
		this.shortName = shortName;
		this.sys = sys;
		this.py=PinYinUtils.getPinYinShengMu(fullName);
	}

	public Bursary(Long id, String fullName) {
		this.id=id;
		this.fullName=fullName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
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

	public boolean isSys() {
		return sys;
	}

	public void setSys(boolean sys) {
		this.sys = sys;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	@Override
	public String pySource() {
		return shortName;
	}

	@Override
	public boolean checkBlank() {
		return StringUtils.isBlank(py);
	}

	@Override
	public String toString() {
		return "Bursary [id=" + id + ", item=" + item + ", fullName=" + fullName + ", shortName=" + shortName + ", py="
				+ py + ", memos=" + memos + ", sys=" + sys + "]";
	}

	
}
