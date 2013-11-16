/*
 * 捷利商业进销存管理系统
 * @(#)CollectionChecker.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-12
 */
package cn.jely.cd.util.logic.impl;

import java.util.Collection;

/**
 * 集合检查器，不符合逻辑抛错。
 * @ClassName:CollectionChecker
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-12 下午2:54:53
 *
 */
public class CollectionChecker {
	
	/**
	 * 检查集合是否为空或空集合,
	 * @param c
	 * @return boolean 检查通过true,未通过false,逻辑错误则throw 错误
	 */
	public static boolean NotEmpty(Collection<?> c){
		if(c==null||c.isEmpty()){
			throw new RuntimeException("不能为空");
		}else{
			return true;
		}
		
	}
}
