package cn.jely.cd.domain;

import cn.jely.cd.util.PinYinUtils;

public class BaseData {
	private Long id;
	/** @Fields name:基础数据名称 */
	private String name;
	/** @Fields name:基础数据拼音码 */
	private String py;
	/** @Fields name:基础数据值 */
	private String value;
	/** @Fields name:值类型 */
	private String valueType;
	/** @Fields name:描述 */
	private String descs;
	/** @Fields name:排序 */
	private Integer sequence;
	/** @Fields name:系统属性标记,true:是系统内置,不可修改 */
	private Boolean systemAttr;
	/** @Fields name:备注 */
	private String memo;
	/** @Fields name:所属类型 */
	private BaseDataType dataType;

	public BaseData() {
	}

	public BaseData(Long id) {
		this.id = id;
	}

	/**
	 * 自动生成py
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param name
	 */
	public BaseData(String name) {
		this.name = name;
		this.py = PinYinUtils.getPinYinShengMu(name);
	}

	public BaseData(BaseDataType dataType, String name, String dataValue, String descs, Integer sequence, String memo) {
		this.dataType = dataType;
		this.descs = descs;
		this.name = name;
		this.py = PinYinUtils.getPinYinShengMu(name);
		this.value = dataValue;
		this.sequence = sequence;
		this.memo = memo;
	}

	public BaseData(String name, String value, String valueType, boolean systemAttr, BaseDataType dataType) {
		this.name = name;
		this.py = PinYinUtils.getPinYinShengMu(name);
		this.value = value;
		this.valueType = valueType;
		this.systemAttr = systemAttr;
		this.dataType = dataType;
	}

	public BaseData(Long id, String name) {
		this.id = id;
		this.name = name;
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

	public String getValue() {
		return value;
	}

	public void setValue(String dataValue) {
		this.value = dataValue;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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

	public BaseDataType getDataType() {
		return dataType;
	}

	public void setDataType(BaseDataType dataType) {
		this.dataType = dataType;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public Boolean getSystemAttr() {
		return systemAttr;
	}

	public void setSystemAttr(Boolean systemAttr) {
		this.systemAttr = systemAttr;
	}

	@Override
	public String toString() {
		return "BaseData [id=" + id + ", name=" + name + ", py=" + py + ", dataValue=" + value + ", descs=" + descs
				+ ", sequence=" + sequence + ", memo=" + memo + "]";
	}

}
