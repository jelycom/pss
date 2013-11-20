/*
 * 捷利商业进销存管理系统
 * @(#)IDoWithState.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-12
 */
package cn.jely.cd.util.state;


/**
 * 根据泛型参数确定返回的类型,
 * @ClassName:IDoWithState
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-12 上午10:49:10
 *
 */
public interface IDoWithState<T> {
	T doWithState(IStateAble domain);
}
