/*
 * 捷利商业进销存管理系统
 * @(#)IStateAble.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-12
 */
package cn.jely.cd.util.state;

/**
 * 状态接口,实现此接口可返回状态
 * @ClassName:IStateAble
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-12 上午10:44:22
 *
 */
public interface IStateAble {
	State getState();
	void setState(State state);
}
