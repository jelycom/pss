/*
 * 捷利商业进销存管理系统
 * @(#)SystemCalUtil.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-3
 */
package cn.jely.cd.util.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.ProjectConfig;

/**
 * @ClassName:SystemCalUtil Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-3 上午11:32:16
 * 
 */
public class SystemCalUtil {

	public static final int PRICESCALE = ProjectConfig.getInstance().getInt(ConstValue.SYS_PRICESCALE, 4);
	public static final int AMOUNTSCALE = ProjectConfig.getInstance().getInt(ConstValue.SYS_AMOUNTSCALE, 2);
	public static final RoundingMode ROUNDINGMODE = ProjectConfig.getInstance()
			.getProperty(ConstValue.SYS_ROUNDINGMODE) == null ? RoundingMode.HALF_UP : (RoundingMode) ProjectConfig
			.getInstance().getProperty(ConstValue.SYS_ROUNDINGMODE);
	private static MathContext amountcontext = new MathContext(AMOUNTSCALE, ROUNDINGMODE);

	/**
	 * 检查字符串是否可转换为BigDecimal,只检查null及空串,字母等未检查
	 * 
	 * @param value
	 * @return BigDecimal
	 */
	private static BigDecimal checkValue(String value) {
		if (value == null || "".equals(value)) {
			return BigDecimal.ZERO;
		} else {
			return new BigDecimal(value);
		}
	}

	public static BigDecimal checkValue(BigDecimal value){
		return value==null?BigDecimal.ZERO:value;
	}
	private static BigDecimal checkDividend(String dividend) {
		BigDecimal ret = checkValue(dividend);
		int compareRet = ret.compareTo(BigDecimal.ZERO);
		if (compareRet < 0) {
			throw new RuntimeException("数量不能为负数");
		} else if (compareRet == 0) {
			throw new RuntimeException("被除数不能为 0");
		}
		return ret;
	}

	/**
	 * 用v1/v2并按照系统设置的精度及保留方式进行小数保留
	 * 
	 * @param v1
	 * @param v2
	 * @return String
	 */
	public static String dividePrice(String v1, String v2) {
		BigDecimal b1 = checkValue(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, PRICESCALE, ROUNDINGMODE).toString();
	}

	public static BigDecimal dividePrice(BigDecimal v1, BigDecimal v2) {
		return v1.divide(v2, PRICESCALE, ROUNDINGMODE);
	}

	public static String multiplyAmount(String price, String quanlity) {
		return checkValue(price).multiply(checkValue(quanlity), amountcontext).toString();
	}

	public static BigDecimal multiplyAmount(BigDecimal price, int quanlity) {
		return price.multiply(new BigDecimal(Integer.toString(quanlity)), amountcontext);
	}

	public static BigDecimal multiplyAmount(BigDecimal price, BigDecimal quanlity) {
		return price.multiply(quanlity, amountcontext);
	}

	public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
		return checkValue(v1).add(checkValue(v2));
	}

	public static String add(String v1, String v2) {
		return checkValue(v1).add(checkValue(v2)).toString();
	}
}
