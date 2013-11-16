/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockingMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-16
 */
package cn.jely.cd.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * 库存盘点主表
 * 可加入一个属性表示需盘点总数,用盘点明细中完成数除以总数可得到盘点百分比.
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-16 上午9:43:53
 */
public class ProductStockingMaster implements Serializable,IStateAble,IItemAble,Cloneable {
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private Long id;
	/**String:item:单据号*/
	private String item;
	/**Date:billDate:单据日期*/
	private Date billDate;
	/**Warehouse:warehouse:盘点仓库*/
	private Warehouse warehouse;
	/**Employee:employee:经手人*/
	private Employee employee;
	/** 状态*/
	protected State state;
	/**单据信息*/
	protected InfoComponent info=new InfoComponent();
	/**String:memos:备注*/
	private String memos;
	/**List<ProductStockingDetail>:details:盘点表明细数据*/
	private List<ProductStockingDetail> details=new ArrayList<ProductStockingDetail>();
	
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
	public String getMemos() {
		return memos;
	}
	public void setMemos(String memos) {
		this.memos = memos;
	}
	public List<ProductStockingDetail> getDetails() {
		return details;
	}
	public void setDetails(List<ProductStockingDetail> details) {
		this.details = details;
	}
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public InfoComponent getInfo() {
		return info;
	}
	public void setInfo(InfoComponent info) {
		this.info = info;
	}


	public ProductStockingMaster toDisp(boolean withDetail) {
		ProductStockingMaster master = null;
		try {
			master = (ProductStockingMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setWarehouse(new Warehouse(this.getWarehouse().getId(),this.getWarehouse().getName()));
		master.setEmployee(new Employee(this.getEmployee().getId(),this.getEmployee().getName()));
		if(withDetail){
			List<ProductStockingDetail> details=this.getDetails();
			int size = details.size();
			for(int i=0;i<size;i++){
				ProductStockingDetail masterdetail=details.get(i);
				Product product = masterdetail.getProduct();
				masterdetail.setProduct(new Product(product.getId(),product.getFullName(),product.getItem(),product.getSpecification(),product.getUnit(),product.getColor()));
			}
		}else{
			master.getDetails().clear();
		}
		return master;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductStockingMaster other = (ProductStockingMaster) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
