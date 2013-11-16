package cn.jely.cd.sys.domain;

import java.util.List;

import cn.jely.cd.sys.domain.ActionResource;

/**
 * 状态类,所有需要状态的单据可定义出所属的状态
 */

public class State implements java.io.Serializable {

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	/** 系统主键*/
	private Long id;
	/** 状态类型(所属单据类型)*/
	/** 状态值*/
	private Long stateId;
	/** 状态编码 */
	private Integer stateCode;
	/** 状态名称*/
	private String stateName;
	/**是否可修改*/
	private Boolean editable;
	/** 是否完成状态 */
	private boolean completed;
	
	/** 此状态可用的操作*/
	private List<ActionResource> validOperates;
	
	// Constructors

	/** default constructor */
	public State() {
	}

	/** minimal constructor */
	public State(Long id) {
		this.id = id;
	}



	// Property accessors



	public Long getStateId() {
		return this.stateId;
	}

	
	public State(String stateName, boolean editable,boolean completed) {
		super();
		this.editable = editable;
		this.stateName = stateName;
		this.completed = completed;
	}

	public State(Long stateId, String stateName, Integer stateCode, List<ActionResource> validOperate) {
		this.stateId = stateId;
		this.stateName = stateName;
		this.stateCode = stateCode;
		this.validOperates = validOperate;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStateName() {
		return this.stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getStateCode() {
		return this.stateCode;
	}

	public void setStateCode(Integer stateCode) {
		this.stateCode = stateCode;
	}

	public List<ActionResource> getValidOperates() {
		return validOperates;
	}

	public void setValidOperates(List<ActionResource> validOperates) {
		this.validOperates = validOperates;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

}