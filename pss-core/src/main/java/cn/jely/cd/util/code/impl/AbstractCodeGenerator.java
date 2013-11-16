/*
 * 捷利商业进销存管理系统
 * @(#)AbstractCodeGenerator.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-3-27
 */
package cn.jely.cd.util.code.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.code.DateCoder;
import cn.jely.cd.util.code.ICodeGenerator;

/**
 * @ClassName:AbstractCodeGenerator
 * @author 周义礼
 * @version 2013-3-27 下午10:58:26
 * 
 */
public abstract class AbstractCodeGenerator implements ICodeGenerator {

//	private DateCoder dateCoder;
//	
//	
//	public AbstractCodeGenerator(DateCoder dateCoder) {
//		this.dateCoder = dateCoder;
//	}
//	
//
//	public AbstractCodeGenerator(String format, String value, Date date) {
//		this.dateCoder = new DateCoder(format,value,date);
//	}
	@Override
	public String Generate(DateCoder dateCoder){
		if (dateCoder == null || StringUtils.isBlank(dateCoder.getFormat())) {
			return null;// 如果没有传入格式
		}
		String format = dateCoder.getFormat();
		String value = dateCoder.getValue();
		Date date = dateCoder.getDate();
		if(date==null){
			throw new RuntimeException("必须提供日期!");
		}
		String rightFormat = format.replaceAll("\\s", "");
		int lftBracePos = rightFormat.indexOf("{");
		int rgtBracePos = rightFormat.indexOf("}");
		int lftSquareBracketsPos = rightFormat.indexOf("[");
		int rgtSquareBracketspos = rightFormat.indexOf("]");
		int lftParenthesisPos = rightFormat.indexOf("(");
		int rgtParenthesisPos = rightFormat.indexOf(")");
		Date oldDate = null;
		StringBuffer codeBuffer = new StringBuffer();
		int excludePos = 0;//排除的位置索引
		/*前缀部分*/
		if (lftBracePos >= 0 && rgtBracePos > lftBracePos) {
			codeBuffer.append(rightFormat.substring(lftBracePos + 1, rgtBracePos));
			excludePos = excludePos + 2;
		}
		/*日期部分*/
		if (lftSquareBracketsPos >= 0 && rgtSquareBracketspos > lftSquareBracketsPos && date != null) {
			String datePattern = rightFormat.substring(lftSquareBracketsPos + 1, rgtSquareBracketspos);
			datePattern = datePattern.replaceAll("Y", "y");
			datePattern = datePattern.replaceAll("m", "M");
			datePattern = datePattern.replaceAll("D", "d");
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			codeBuffer.append(dateFormat.format(date));
			try {
				if (StringUtils.isNotBlank(value)) {
					oldDate = dateFormat.parse(value.substring(lftSquareBracketsPos - excludePos, rgtSquareBracketspos
							- excludePos - 1));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			excludePos = excludePos + 2;
		}
		/*序号部分,是否重新排号让checkDate检查确定*/
		if (lftParenthesisPos >= 0 && rgtParenthesisPos > lftParenthesisPos) {
			int sequenceLen = rgtParenthesisPos - lftParenthesisPos - 1;
			String regx = "%0" + sequenceLen + "d";
			if (StringUtils.isBlank(value) || checkDate(date, oldDate)) {// 如果是没有值或者检查两个日期不同,则需要进行重新排号
				codeBuffer.append(String.format(regx, 1));
			} else {
				String oldValueString = value.substring(lftParenthesisPos - excludePos);
				codeBuffer.append(String.format(regx, Integer.parseInt(oldValueString) + 1));
			}
		}
		return codeBuffer.toString();
	}

	/**
	 * 根据不同的检查策略来决定是否重新计数
	 * 
	 * @Title:checkDate
	 * @Description:TODO 判断是否超过设定的值.
	 * @param date
	 * @param oldDate
	 * @return boolean true:日期不同,需要重新计数,false:不需要重新计数,只需+1即可
	 */
	protected abstract boolean checkDate(Date date, Date oldDate);
}
