/*
 * 捷利商业进销存管理系统
 * @(#)MyClassLoader.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-11-13
 */
package cn.jely.cd.util;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-11-13 下午9:26:09
 */
public class MyClassLoader extends ClassLoader {

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("我的自定义查找类方法!");
		return super.findClass(name);
	}

}
