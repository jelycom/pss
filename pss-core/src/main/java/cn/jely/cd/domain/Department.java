package cn.jely.cd.domain;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * Department entity. @author MyEclipse Persistence Tools
 */

public class Department implements java.io.Serializable,IPinYinLogic {

	// Fields

	private Long id;
	private String name;
	private String py;
	private Department parent;
	private Employee manager;
	private String memo;

	// Constructors

	/** default constructor */
	public Department() {
	}

	/** full constructor */
	public Department(String name, String py, String memo) {
		this.name = name;
		this.py = py;
		this.memo = memo;
	}

	// Property accessors


	public Department(String name) {
		this.name=name;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
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