package cn.jely.cd.domain;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * Producttype entity. @author MyEclipse Persistence Tools
 */

public class ProductType extends LftRgtTreeNode implements java.io.Serializable, IPinYinLogic {

	// Fields

	private Long id;
	private String item;
	private String name;
	private String py;
	private String memos;

	// Constructors

	/** default constructor */
	public ProductType() {
	}

	
	public ProductType(Long id) {
		this.id = id;
	}


	public ProductType(String name) {
		this.name = name;
	}

	/** minimal constructor */
	public ProductType(String name, String item, String py) {
		this.item = item;
		this.name = name;
		this.py = py;
	}

	// Property accessors

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

	public String getMemos() {
		return this.memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
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
		ProductType other = (ProductType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProductType [id=" + id + ", item=" + item + ", py=" + py + ", name=" + name + ", memos=" + memos + "]";
	}

	@Override
	public boolean checkBlank() {
		return StringUtils.isBlank(py);
	}

	@Override
	public String pySource() {
		return name;
	}

}