/*
 * 捷利商业进销存管理系统
 * @(#)IPinYinLogic.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-2
 */
package cn.jely.cd.util.logic;

import org.apache.struts2.json.annotations.JSON;

/**
 * 需要对拼音进行检查的类,需要实现此接口
 * @ClassName:IPinYinLogic
 * @author 周义礼
 * @version 2013-4-2 下午10:29:37
 *
 */
public interface IPinYinLogic extends ILogic{
	/**
	 * 对实体进行拼音码的设置方法
	 * @Title:setPy
	 * @param py void
	 */
	void setPy(String py);
	/**
	 * 需要生成拼音码的对象
	 * @Title:getValue
	 * @return String
	 */
	String pySource();
	/**
	 * 检查值,返回需要检查的结果,
	 * @Title:checkBlank
	 * @return boolean true:表示为空,则需要进行从pySource得到值进行相应运算后并setPy<br/>
	 * 					false:表示不为空,则不需要重新设置值
	 */
	boolean checkBlank();
}
