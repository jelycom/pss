package cn.jely.cd.domain;

/**
 * Querydata entity. @author MyEclipse Persistence Tools
 */

public class Querydata implements java.io.Serializable {

	// Fields

	private Long id;
	
	/**保存的过滤名*/
	private String name;
	/**	过滤条件 */
	private String value;
	/** 过滤器所属的用户 */
	private Employee employee;
	/** 过滤器的类型 可为私有(自己用),组(部门可用),公共(全体可用) */
	private String dataType;
	/**	过滤所属的action名称 */
	private String actionName; //条件所属的Action名称

	// Constructors

	/** default constructor */
	public Querydata() {
	}

	/** full constructor */


	// Property accessors

	public Long getId() {
		return this.id;
	}

	public Querydata(Long id, String name, String value, String pageName) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.actionName = pageName;
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

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}