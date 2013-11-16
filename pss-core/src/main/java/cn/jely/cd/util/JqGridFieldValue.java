package cn.jely.cd.util;

public class JqGridFieldValue {

	private String fieldName;
	private String fieldValue;
	private Class filedType;
	
	
	public JqGridFieldValue() {
	}
	public JqGridFieldValue(String fieldName, String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public Class getFiledType() {
		return filedType;
	}
	public void setFiledType(Class filedType) {
		this.filedType = filedType;
	}
	
}
