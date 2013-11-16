/*
 * 捷利商业进销存管理系统
 * @(#)ProductTrasferMasterPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.pagemodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductTrasferMasterPM
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-17 下午3:04:17
 *
 */
public class ProductTransferMasterPM {
	// Fields
	protected Long id;
	/** 单据编号 */
	protected String item;
	/** 调出仓库*/
	protected Long outWarehouseId;
	protected String outWarehouseName;
	/** 调出仓经手人*/
	protected Long outEmployeeId;
	protected String outEmployeeName;
	/** 调入仓库*/
	protected Long inWarehouseId;
	protected String inWarehouseName;
	/** 调入仓经手人*/
	protected Long inEmployeeId;
	protected String inEmployeeName;
	/** 业务单据发生日期*/
	protected Date billDate;
	/** 状态*/
	protected State state;
	/**String:memos:备注*/
	private String memos;
	private List<ProductCommonDetailPM> details=new ArrayList<ProductCommonDetailPM>();
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
	public Long getOutWarehouseId() {
		return outWarehouseId;
	}
	public void setOutWarehouseId(Long outWarehouseId) {
		this.outWarehouseId = outWarehouseId;
	}
	public String getOutWarehouseName() {
		return outWarehouseName;
	}
	public void setOutWarehouseName(String outWarehouseName) {
		this.outWarehouseName = outWarehouseName;
	}
	public Long getOutEmployeeId() {
		return outEmployeeId;
	}
	public void setOutEmployeeId(Long outEmployeeId) {
		this.outEmployeeId = outEmployeeId;
	}
	public String getOutEmployeeName() {
		return outEmployeeName;
	}
	public void setOutEmployeeName(String outEmployeeName) {
		this.outEmployeeName = outEmployeeName;
	}
	public Long getInWarehouseId() {
		return inWarehouseId;
	}
	public void setInWarehouseId(Long inWarehouseId) {
		this.inWarehouseId = inWarehouseId;
	}
	public String getInWarehouseName() {
		return inWarehouseName;
	}
	public void setInWarehouseName(String inWarehouseName) {
		this.inWarehouseName = inWarehouseName;
	}
	public Long getInEmployeeId() {
		return inEmployeeId;
	}
	public void setInEmployeeId(Long inEmployeeId) {
		this.inEmployeeId = inEmployeeId;
	}
	public String getInEmployeeName() {
		return inEmployeeName;
	}
	public void setInEmployeeName(String inEmployeeName) {
		this.inEmployeeName = inEmployeeName;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	public List<ProductCommonDetailPM> getDetails() {
		return details;
	}
	public void setDetails(List<ProductCommonDetailPM> details) {
		this.details = details;
	}
	
}
