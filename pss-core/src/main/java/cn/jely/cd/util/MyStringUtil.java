/*
 * 捷利商业进销存管理系统
 * @(#)MyStringUtil.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-3
 */
package cn.jely.cd.util;

import java.util.Random;

/**
 * 
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-9-3 上午11:00:43
 */
public class MyStringUtil {
	public static String genRandomString(int length) {
		if (length < 1) {
			return null;
		}
		char[] chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
				'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int pos=new Random().nextInt(chars.length);
			sb.append(chars[pos]);
		}
		return sb.toString();
	}
}
