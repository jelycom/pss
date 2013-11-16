/*
 * 捷利商业进销存管理系统
 * @(#)ArrayConverter.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.util;


/**
 * @ClassName:ArrayConverter
 * @Description:数组转换类
 * @author {周义礼}
 * @version 2012-11-13 下午2:03:28
 *
 */
public class ArrayConverter {

	public static Long[] Strings2Longs(String[] strs){
		int size=strs.length;
		Long[] longs=new Long[size];
		for (int i = 0; i < size; i++) {
			longs[i]=Long.valueOf(strs[i]);
		}
		return longs;
	}
	
	public static String[] Longs2Strings(Long[] longs){
		int size=longs.length;
		String[] strs=new String[size];
		for(int i=0;i<size;i++){
			strs[i]=longs[i].toString();
		}
		return strs;
	}
}
