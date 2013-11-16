/*
 * 捷利商业进销存管理系统
 * @(#)State.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-11
 */
package cn.jely.cd.util.state;

/**
 * 单据能修改的状态:NEW.
 * 单据完成状态:AUDITED.
 * 业务完成状态分为三种:STOP:手动停止的,表示此单已经执行部分,但不准备再执行下去
 * 					COMPLETE:满足条件完成的,比如:单据数量,金额入出方向都完全一致
 * 					DISCARD:作废的,只有在新建的/审核中的没有参与执行的才可以作废.
 * Description:新建状态可以
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-11 下午11:21:49
 *
 */
public enum State {
	NEW("新建"),AUDITING("审核中"),AUDITFAIL("审核失败"),AUDITED("审核通过"),PROCESS("处理中"),SUSPEND("暂停"),	STOP("手动停止"),COMPLETE("完成"),DISCARD("作废");
	private String msg;
	State(String name){
		this.msg=name;
	}
	public String getMsg() {
		return msg;
	}
//	public static final String COMPLETE = "C";
//	public static final String DISCARD = "D";
//	// 状态应有:新建,执行中,完成,中止,作废:N,P,C,S,D
//	// Fields
//	public static final String NEW = "N";
//	public static final String PROCESS = "P";
//	public static final String SUSPEND = "S";
}
