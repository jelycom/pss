/*
 * 进销存系统
 * @(#)CodeBuilder.java
 * Copyright (c) 2011-2012 Jely Corporation
 * author : jely
 * @version 2012/6/15
 */
package cn.jely.cd.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author zkw
 * @modified by zkw
 * @version 1.00 2012/6/15
 * @modified version 1.00 2012/6/15 
 * @note 代码编辑器
 * @type java
 */
public final class CodeBuilder {

//	public static String builder(String schoolCode, String cardId) {
//		Calendar c = Calendar.getInstance();
//		String d = DateFormatter.getSimpleDate(c.getTime());
//		return schoolCode + "-" + d + "-" + cardId;
//	}
	
	public static String builderPurchaseListCode(String fomart,Long code){
		String head = fomart.substring(fomart.indexOf("\"")+1,fomart.lastIndexOf("\""));
		String pattern = fomart.substring(fomart.lastIndexOf("\"")+1,fomart.indexOf("["));
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
		Matcher m = p.matcher(pattern); 
		pattern = m.replaceAll("");
		pattern = pattern.replaceAll("Y", "y");
		pattern = pattern.replaceAll("D", "d");
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String time = sdf.format(new Date());
		Integer number = fomart.length()-1-(fomart.indexOf("[")+2);
		String last = String.format("%0"+number+"d", code);
		StringBuffer sb = new StringBuffer();
		sb.append(head);
		sb.append(time);
		sb.append(last);
		return sb.toString();
	}
	
	public static String generateCode(String format,String value){
		
		return null;
	}
}
