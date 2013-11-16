package cn.jely.cd.domain;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * Region entity. @author MyEclipse Persistence Tools
 */

public class Region extends LftRgtTreeNode implements java.io.Serializable,IPinYinLogic {

	// Fields

	private Long id;
	private String name;
	private String py;
	private String item;
	private String memos;

	// private Long lft;
	// private Long rgt;
	// private Integer depth;
	// Constructors

	/** default constructor */
	public Region() {
	}

	public Region(Long id) {
 		this.id = id;
	}

	public Region(String name) {
		this.name = name;
	}

	/** minimal constructor */
	public Region(String item, String name) {
		this.item = item;
		this.name = name;
	}

	public Region(Long id, String name, String item, Long lft, Long rgt) {
		this.id = id;
		this.name = name;
		this.item = item;
		// this.lft = lft;
		// this.rgt = rgt;
	}

	public Region(Long id, String name, String item, Long lft, Long rgt, Long depth) {
		this.id = id;
		this.name = name;
		this.item = item;
		// this.lft = lft;
		// this.rgt = rgt;
		// this.depth = depth.intValue();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	@Override
	public String toString() {
		return "Region [id=" + id + ", name=" + name + ", item=" + item + "]";
		// + ", lft=" + lft + ", rgt=" + rgt + ", depth=" + depth
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
		Region other = (Region) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String pySource() {
		return this.name;
	}

	@Override
	public boolean checkBlank() {
		return StringUtils.isBlank(py);
	}

}