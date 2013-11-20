/*
 * 捷利商业进销存管理系统
 * @(#)UrlUtil.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-11-12
 */
package cn.jely.cd.util;

import java.io.File;
import java.net.URL;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-11-12 下午10:44:10
 */
public class UrlUtil {
	/**
	 * 取得当前类所在的文件
	 * 
	 * @param clazz
	 * @return
	 */
	public static File getClassFile(Class clazz) {
		URL path = clazz.getResource(clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1) + ".classs");
		if (path == null) {
			String name = clazz.getName().replaceAll("[.]", "/");
			path = clazz.getResource("/" + name + ".class");
		}
		return new File(path.getFile());
	}

	/**
	 * 得到当前类的路径
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getClassFilePath(Class clazz) {
		try {
			return java.net.URLDecoder.decode(getClassFile(clazz).getAbsolutePath(), "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 取得当前类所在的ClassPath目录，比如tomcat下的classes路径
	 * 
	 * @param clazz
	 * @return
	 */
	public static File getClassPathFile(Class clazz) {
		File file = getClassFile(clazz);
		for (int i = 0, count = clazz.getName().split("[.]").length; i < count; i++)
			file = file.getParentFile();
		if (file.getName().toUpperCase().endsWith(".JAR!")) {
			file = file.getParentFile();
		}
		return file;
	}

	/**
	 * 取得当前类所在的ClassPath路径
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getClassPath(Class clazz) {
		try {
			return java.net.URLDecoder.decode(getClassPathFile(clazz).getAbsolutePath(), "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {
		System.out.println(getClassFilePath(UrlUtil.class));
		System.out.println(getClassPath(UrlUtil.class));
	}

}