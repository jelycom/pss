/*
 * 捷利商业进销存管理系统
 * @(#)ProductTrasferMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.jely.cd.pagemodel.ProductCommonDetailPM;
import cn.jely.cd.pagemodel.ProductTransferMasterPM;
import cn.jely.cd.pagemodel.ToPageModel;
import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductTrasferMaster
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-17 下午2:30:56
 *
 */
public class ProductTransferMaster implements Serializable,IStateAble,IItemAble,Cloneable,ToPageModel<ProductTransferMasterPM>{
	// Fields
	protected Long id;
	/** 单据编号 */
	protected String item;
	/** 调出仓库*/
	protected Warehouse outWarehouse;
	/** 调出仓经手人*/
	protected Employee outEmployee;
	/** 调入仓库*/
	protected Warehouse inWarehouse;
	/** 调入仓经手人*/
	protected Employee inEmployee;
	/**单据信息*/
	protected InfoComponent info=new InfoComponent();
	/** 业务单据发生日期*/
	protected Date billDate;
	/** 状态*/
	protected State state;
	/**List<ProductPlanDeliveryMaster>:productPlans:关联计划*/
	private List<ProductTransferDetail> details=new ArrayList<ProductTransferDetail>();
	/**String:memos:备注*/
	private String memos;
	
	
	public ProductTransferMaster() {
		super();
	}
	
	
	public ProductTransferMaster(String item, Warehouse outWarehouse, Employee outEmployee, Warehouse inWarehouse,
			Employee inEmployee, Date billDate) {
		super();
		this.item = item;
		this.outWarehouse = outWarehouse;
		this.outEmployee = outEmployee;
		this.inWarehouse = inWarehouse;
		this.inEmployee = inEmployee;
		this.billDate = billDate;
	}


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
	public Warehouse getOutWarehouse() {
		return outWarehouse;
	}
	public void setOutWarehouse(Warehouse outWarehouse) {
		this.outWarehouse = outWarehouse;
	}
	public Employee getOutEmployee() {
		return outEmployee;
	}
	public void setOutEmployee(Employee outEmployee) {
		this.outEmployee = outEmployee;
	}
	public Warehouse getInWarehouse() {
		return inWarehouse;
	}
	public void setInWarehouse(Warehouse inWarehouse) {
		this.inWarehouse = inWarehouse;
	}
	public Employee getInEmployee() {
		return inEmployee;
	}
	public void setInEmployee(Employee inEmployee) {
		this.inEmployee = inEmployee;
	}
	public InfoComponent getInfo() {
		return info;
	}
	public void setInfo(InfoComponent info) {
		this.info = info;
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

	public List<ProductTransferDetail> getDetails() {
		return details;
	}
	public void setDetails(List<ProductTransferDetail> details) {
		this.details = details;
	}
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	
	public ProductTransferMaster toDisp(boolean withDetail){
		ProductTransferMaster master=null;
		try {
			master = (ProductTransferMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setInWarehouse(new Warehouse(master.getInWarehouse().getId(), master.getInWarehouse().getName()));
		master.setOutWarehouse(new Warehouse(master.getOutWarehouse().getId(), master.getOutWarehouse().getName()));
		master.setInEmployee(new Employee(master.getInEmployee().getId(), this.getInEmployee().getName()));
		master.setOutEmployee(new Employee(this.getOutEmployee().getId(), this.getOutEmployee().getName()));
		if (withDetail) {
			List<ProductTransferDetail> details = master.getDetails();
			int size = details.size();
			for (int i = 0; i < size; i++) {
				ProductTransferDetail masterdetail = details.get(i);
				masterdetail.setMaster(null);
				Product product = masterdetail.getProduct();
				masterdetail.setProduct(new Product(product.getId(), product.getFullName(), product.getItem(),product.getSpecification(), product.getUnit(), product.getColor()));
			}
		} else {
			master.getDetails().clear();
		}
		return master;
	}
	
	@Override
	public ProductTransferMasterPM convert(boolean withDetail) {
		ProductTransferMasterPM masterPM=new ProductTransferMasterPM();
		BeanUtils.copyProperties(this, masterPM, new String[]{"details"});
		masterPM.setOutWarehouseId(this.outWarehouse.getId());
		masterPM.setOutWarehouseName(this.getOutWarehouse().getName());
		masterPM.setOutEmployeeId(this.outEmployee.getId());
		masterPM.setOutEmployeeName(this.getOutEmployee().getName());
		masterPM.setInWarehouseId(this.inWarehouse.getId());
		masterPM.setInWarehouseName(this.getInWarehouse().getName());
		masterPM.setInEmployeeId(this.inEmployee.getId());
		masterPM.setInEmployeeName(this.getInEmployee().getName());
		if(withDetail){
			List<ProductCommonDetailPM> PMdetails=masterPM.getDetails();
			List<ProductTransferDetail> details=this.getDetails();
			for (ProductTransferDetail detail : details) {
				ProductCommonDetailPM PMdetail=new ProductCommonDetailPM();
				BeanUtils.copyProperties(detail, PMdetail);
				PMdetail.setProductId(detail.getProduct().getId());
				PMdetail.setFullName(detail.getProduct().getFullName());
				PMdetail.setSpecification(detail.getProduct().getSpecification());
				PMdetail.setColor(detail.getProduct().getColor());
				PMdetail.setUnit(detail.getProduct().getUnit());
				PMdetails.add(PMdetail);
			}
		}
		return masterPM;
	} 
	
}
