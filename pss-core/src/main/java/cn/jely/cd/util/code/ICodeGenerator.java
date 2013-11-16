/*
 * 捷利商业进销存管理系统
 * @(#)ICodeGenerator.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-3-25
 */
package cn.jely.cd.util.code;


/**
 * 生成编号的接口,用于生成相应的编号
 * @ClassName:ICodeGenerator
 * @Description:可以使用年生成器,月生成器,日生成器.
 * @author 周义礼
 * @version 2013-3-25 下午5:36:11
 *
 */
public interface ICodeGenerator {

	/**
	 * 生成编号,如果传入的value为空则生成第一个,否则按传入的时间来计算如何生成编号
	 * @Title:Generate
	 * @Description:{}中表示不需要进行改变的部分,[]存放时间的部分,()存放序列部分,有多少位就需要放多少个0;
	 * @param format
	 * @param value
	 * @param date
	 * @return String
	 */
	public String Generate(DateCoder dateCoder);
}
