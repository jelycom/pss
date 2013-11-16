package cn.jely.cd.domain;

import cn.jely.cd.util.PinYinUtils;

public class BaseDataType extends LftRgtTreeNode implements java.io.Serializable {

	private Long id;
	private String name;
	private String py;
	private String sn;
	private String descs;
	private String memo;

	public BaseDataType() {
	}

	public BaseDataType(Long id) {
		this.id = id;
	}

	public BaseDataType(String name, String sn) {
		this.name = name;
		this.py=PinYinUtils.getPinYinShengMu(name);
		this.sn = sn;
	}

	public BaseDataType(String name, String py, String sn) {
		this.name = name;
		this.py = py;
		this.sn = sn;
	}

	public BaseDataType(String descs, String name, String sn, String memo) {
		this.descs = descs;
		this.name = name;
		this.sn = sn;
		this.memo = memo;
	}

	public BaseDataType(Long id, String descs, String name, String sn, String memo, Long lft, Long rgt) {
		this.id = id;
		this.descs = descs;
		this.name = name;
		this.sn = sn;
		this.memo = memo;
	}

	public BaseDataType(Long id, String descs, String name, String sn, String memo, Long lft, Long rgt, Long depth) {
		this.id = id;
		this.descs = descs;
		this.name = name;
		this.sn = sn;
		this.memo = memo;
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

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	@Override
	public String toString() {
		return "BaseDataType [id=" + id + ", name=" + name + ", descs=" + descs + ",sn=" + sn + ", memo=" + memo + "]";
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
		BaseDataType other = (BaseDataType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
