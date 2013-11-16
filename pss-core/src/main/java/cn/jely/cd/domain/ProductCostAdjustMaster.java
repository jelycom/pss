/*
 * 捷利商业进销存管理系统
 * @(#)CostAdjust.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-19
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jely.cd.pagemodel.ProductCommonDetailPM;
import cn.jely.cd.pagemodel.ProductCommonMasterPM;
import cn.jely.cd.pagemodel.ToPageModel;
import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:CostAdjust Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-19 下午5:10:43
 * 
 */
public class ProductCostAdjustMaster implements IStateAble, IItemAble, ToPageModel<ProductCommonMasterPM> {
	private Long id;
	private Date billDate;
	private String item;
	private Warehouse warehouse;
	private Employee employee;
	/**BigDecimal:newAmount:调价后成本*/
	private BigDecimal newAmount;
	/**BigDecimal:oldAmount:调价前成本*/
	private BigDecimal oldAmount;
	private InfoComponent info = new InfoComponent();
	private String memos;
	private List<ProductCostAdjustDetail> details = new ArrayList<ProductCostAdjustDetail>();
	private State state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
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

	public BigDecimal getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(BigDecimal newAmount) {
		this.newAmount = newAmount;
	}

	public BigDecimal getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(BigDecimal oldAmount) {
		this.oldAmount = oldAmount;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public InfoComponent getInfo() {
		return info;
	}

	public void setInfo(InfoComponent info) {
		this.info = info;
	}

	public List<ProductCostAdjustDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductCostAdjustDetail> details) {
		this.details = details;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public ProductCommonMasterPM convert(boolean withDetail) {
		ProductCommonMasterPM masterPM=new ProductCommonMasterPM();
		org.springframework.beans.BeanUtils.copyProperties(this,masterPM,new String[]{"details"});
		Warehouse warehouse = this.getWarehouse();
		Employee employee = this.getEmployee();
		masterPM.setWarehouseId(warehouse.getId());
		masterPM.setWarehouseName(warehouse.getName());
		masterPM.setEmployeeId(employee.getId());
		masterPM.setEmployeeName(employee.getName());
		if(withDetail){
			List<ProductCostAdjustDetail> details = this.getDetails();
			int size = details.size();
			for(int i=0;i<size;i++){
				ProductCommonDetailPM detailpm=new ProductCommonDetailPM();
				ProductCostAdjustDetail detail=details.get(i);
				org.springframework.beans.BeanUtils.copyProperties(detail, detailpm);
				Product product = detail.getProduct();
				detailpm.setProductId(product.getId());
				detailpm.setFullName(product.getFullName());
				detailpm.setSpecification(product.getSpecification());
				detailpm.setColor(product.getColor());
				detailpm.setUnit(product.getUnit());
				masterPM.getDetails().add(detailpm);
			}
		}
		return masterPM;
	}

}
