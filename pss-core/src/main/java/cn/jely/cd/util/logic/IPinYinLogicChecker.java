/*
 * 捷利商业进销存管理系统
 * @(#)IPinYinLogicChecker.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-2
 */
package cn.jely.cd.util.logic;

/**
 * 对拼音进行逻辑检查的接口
 * @ClassName:IPinYinLogicChecker
 * @author 周义礼
 * @version 2013-4-2 下午10:25:11
 *
 */
public interface IPinYinLogicChecker extends ILogicChecker {
	void CheckLogic(ILogic entity);
}
