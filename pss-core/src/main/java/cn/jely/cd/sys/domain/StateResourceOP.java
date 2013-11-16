/*
 * 捷利商业进销存管理系统
 * @(#)StateResourceOP.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-5-31
 */
package cn.jely.cd.sys.domain;

import cn.jely.cd.sys.domain.ActionResource;

/**
 * 状态资源地址操作类
 * @ClassName:StateResourceOP
 * Description:根据资源地址及状态确定相应的操作资源
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-5-31 下午2:01:33
 *
 */
public class StateResourceOP {
	private Long id;
	/**@Fields actionResource:访问的资源地址*/
	private ActionResource actionResource;
	/**@Fields state:状态*/
	private State state;
	/**@Fields opResource:此状态相应的操作的地址*/
	private ActionResource opResource; 
	/**@Fields name:此操作显示的名称,不超过8个字符长.避免显示不完整*/
	private String opName;
	/**@Fields memo:此操作的说明,可以作为前台tooltip显示的文本*/
	private String memo;
	
	
	public StateResourceOP() {
		super();
	}
	
	public StateResourceOP(ActionResource actionResource, State state, ActionResource opResource, String opName) {
		super();
		this.actionResource = actionResource;
		this.state = state;
		this.opResource = opResource;
		this.opName = opName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ActionResource getActionResource() {
		return actionResource;
	}
	public void setActionResource(ActionResource actionResource) {
		this.actionResource = actionResource;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public ActionResource getOpResource() {
		return opResource;
	}
	public void setOpResource(ActionResource opResource) {
		this.opResource = opResource;
	}

	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Override
	public String toString() {
		return "StateResourceOP [id=" + id + ", opName=" + opName + ", memo=" + memo + "]";
	}
	
}