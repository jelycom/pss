/*
 * 捷利商业进销存管理系统
 * @(#)ProductPlanMasterPM.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-16
 */
package cn.jely.cd.pagemodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductPlanMasterPM
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-16 下午5:29:52
 *
 */
public class ProductPlanMasterPM implements PageModel{
	private Long id;
	private String item;
	/**Employee:planEmployee:经手人*/
	private Long planEmployeeId;
	private String planEmployeeName;
	/**Long:executeEmployeeId:执行人*/
	private Long executeEmployeeId;
	private String executeEmployeeName;
	private Date startDate;
	private Date endDate;
	private State state;
	private List<ProductPlanDetailPM> details = new ArrayList<ProductPlanDetailPM>();
	
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
	public Long getPlanEmployeeId() {
		return planEmployeeId;
	}
	public void setPlanEmployeeId(Long planEmployeeId) {
		this.planEmployeeId = planEmployeeId;
	}
	public String getPlanEmployeeName() {
		return planEmployeeName;
	}
	public void setPlanEmployeeName(String planEmployeeName) {
		this.planEmployeeName = planEmployeeName;
	}
	public Long getExecuteEmployeeId() {
		return executeEmployeeId;
	}
	public void setExecuteEmployeeId(Long executeEmployeeId) {
		this.executeEmployeeId = executeEmployeeId;
	}
	public String getExecuteEmployeeName() {
		return executeEmployeeName;
	}
	public void setExecuteEmployeeName(String executeEmployeeName) {
		this.executeEmployeeName = executeEmployeeName;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@JSON(format="yyyy-MM-dd")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public List<ProductPlanDetailPM> getDetails() {
		return details;
	}
	public void setDetails(List<ProductPlanDetailPM> details) {
		this.details = details;
	}
}
