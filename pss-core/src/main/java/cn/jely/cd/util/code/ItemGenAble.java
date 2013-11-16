/*
 * 捷利商业进销存管理系统
 * @(#)ItemGenAble.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-14
 */
package cn.jely.cd.util.code;

import java.util.Date;

/**
 * 编号生成接口,需要生成编号的类实现此接口
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-14 上午11:11:57
 *
 */
public interface ItemGenAble {
	public String generateItem(Date billDate);
}
