/*
 * 捷利商业进销存管理系统
 * @(#)ProductChangeMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-8
 */
package cn.jely.cd.domain;

import java.util.ArrayList;
import java.util.List;

import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-8-8 下午5:11:19
 */
public class ProductChangeMaster implements java.io.Serializable, IStateAble, IItemAble {

	private Long id;
	private String item;
	/** 所影响的仓库 */
	protected Warehouse warehouse;
	/** 此单经手人 */
	protected Employee employee;
	/** 单据信息 */
	protected InfoComponent info = new InfoComponent();
	/** 状态 */
	protected State state;
	/** 备注 */
	protected String memos;

	private List<ProductChangeDetail> details=new ArrayList<ProductChangeDetail>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public InfoComponent getInfo() {
		return info;
	}

	public void setInfo(InfoComponent info) {
		this.info = info;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public List<ProductChangeDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductChangeDetail> details) {
		this.details = details;
	}

	public void setItem(String item) {
		this.item = item;
	}

	@Override
	public String getItem() {
		return item;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}

}
