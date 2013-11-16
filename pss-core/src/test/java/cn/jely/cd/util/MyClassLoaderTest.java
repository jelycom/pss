/*
 * 捷利商业进销存管理系统
 * @(#)MyClassLoaderTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-11-13
 */
package cn.jely.cd.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;

import org.testng.annotations.Test;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-11-13 下午9:33:56
 */
public class MyClassLoaderTest {

	@Test
	public void testLoad() throws ClassNotFoundException, Throwable, NoSuchMethodException{
		MyClassLoader loader = new MyClassLoader();
		Class<?> loadClass = loader.loadClass(MyClass.class.getName());
		System.out.println(loadClass.getClassLoader());
		System.out.println(loadClass.getClassLoader().getParent());
		Method method = loadClass.getMethod("main",(new String[0]).getClass());
		String [] argsarray=new String[]{"123"};
		Object transarray[] ={argsarray}; 
		method.invoke(null,transarray);
	}
	
	@Test
	public void testSystemLoader() throws IOException{
		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println(classLoader);
		ClassLoader parentLoader = classLoader.getParent();
		System.out.println(parentLoader);
		Enumeration<URL> resources = parentLoader.getResources("MyClass.class");
		while (resources.hasMoreElements()) {
			URL url = (URL) resources.nextElement();
			System.out.println(url);
		}
		System.out.println(parentLoader.getParent());
	}
}
