/*
 * 捷利商业进销存管理系统
 * @(#)InventoryCommon.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-4
 */
package cn.jely.cd.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * 盘盈Inventory Profit、盘亏表父类
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-4 上午9:30:31
 */
public class InventoryCommonMaster implements Serializable, IStateAble, IItemAble, Cloneable {
	/** long:serialVersionUID: */
	private static final long serialVersionUID = 1L;

	private Long id;
	/** String:item:单据编号 */
	private String item;
	/** Date:billDate:单据日期 */
	private Date billDate;
	/** Employee:employee:经手人 */
	private Employee employee;
	/** InfoComponent:info:单据信息 */
	private InfoComponent info;
	/** Warehouse:warehouse:盘点库房 */
	private Warehouse warehouse;
	/** BigDecimal:amount:合计值 */
	private BigDecimal amount;
	private List<InventoryCommonDetail> details = new ArrayList<InventoryCommonDetail>();
	/** 状态 */
	protected State state;
	/** String:memos:备注 */
	private String memos;

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

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
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

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public List<InventoryCommonDetail> getDetails() {
		return details;
	}

	public void setDetails(List<InventoryCommonDetail> details) {
		this.details = details;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public InventoryCommonMaster toDisp(boolean withDetail) {
		InventoryCommonMaster master = null;
		try {
			master = (InventoryCommonMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setWarehouse(new Warehouse(this.getWarehouse().getId(), this.getWarehouse().getName()));
		master.setEmployee(new Employee(this.getEmployee().getId(), this.getEmployee().getName()));
		if (withDetail) {
			List<InventoryCommonDetail> details = this.getDetails();
			int size = details.size();
			for (int i = 0; i < size; i++) {
				InventoryCommonDetail masterdetail = details.get(i);
				Product product = masterdetail.getProduct();
				masterdetail.setProduct(new Product(product.getId(), product.getFullName(), product.getItem(), product
						.getSpecification(), product.getUnit(), product.getColor()));
			}
		} else {
			master.getDetails().clear();
		}
		return master;
	}

}
