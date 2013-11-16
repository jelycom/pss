/*
 * 捷利商业进销存管理系统
 * @(#)TimerJob.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.domain;

/**
 * @ClassName:TimerJob
 * @Description:定时任务的类,保存诸如需要定时执行的任务的类,可用于定时结帐,生成报表等功能.
 * @author 周义礼
 * @version 2013-1-12 下午10:05:21
 * 
 */
public class TimerJob {
	public static final String VALID="1";
	public static final String INVALID="0";
	private Long id;
	/** @Fields className:任务类名 */
	private String className;
	/** @Fields code:任务名称 */
	private String name;
	/** @Fields expression:任务的Cron表达式 */
	private String expression;
	/** @Fields status:任务的状态,是否可用:"1"可用,"0"不可用 */
	private String status;

	public TimerJob() {
	}

	public TimerJob(String name, String className, String expression,String status) {
		this.name = name;
		this.className = className;
		this.expression = expression;
		this.status=status;
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

	public String getClassName() {
		return className;
	}

	public String getExpression() {
		return expression;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
