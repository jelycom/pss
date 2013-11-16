package cn.jely.cd.domain;

import java.io.Serializable;

/**
 * Warehouseallocation entity. @author MyEclipse Persistence Tools
 */

public class WarehouseAllocation implements java.io.Serializable {

	// Fields

	private Long id;
	private Warehouse warehouse;
	private String item;
	private String name;
	private String py;
	private String memos;
	private boolean invalid;

	// Constructors

	/** default constructor */
	public WarehouseAllocation() {
	}

	/** full constructor */
	public WarehouseAllocation(Warehouse warehouse, String name,String item,
			String py, String memos, boolean invalid) {
		this.warehouse = warehouse;
		this.item = item;
		this.name = name;
		this.py = py;
		this.memos = memos;
		this.invalid = invalid;
	}

	// Property accessors

	public WarehouseAllocation(String name) {
		this.name = name;
	}

	public WarehouseAllocation(Warehouse warehouse,String name, String item ) {
		this.name = name;
		this.item = item;
		this.warehouse = warehouse;
	}

	
	public WarehouseAllocation(Warehouse warehouse, String item, String name, boolean invalid) {
		this.warehouse = warehouse;
		this.item = item;
		this.name = name;
		this.invalid = invalid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

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

	public String getMemos() {
		return this.memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

}