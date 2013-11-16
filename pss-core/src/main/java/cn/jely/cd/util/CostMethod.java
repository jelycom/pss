/*
 * 捷利商业进销存管理系统
 * @(#)CostMethod.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-15
 */
package cn.jely.cd.util;

/**
 * 成本计算方法类,包含各种不同的计算方法名称
 * @ClassName:CostMethod
 * @author 周义礼
 * @version 2013-4-15 上午10:43:26
 *
 */
public enum CostMethod {
	FIFO("先进先出"),LIFO("后进先出"),MOAV("移动平均"),ASSI("手工指定");//Moving Average
//	private String sn=null;
	private String methodName=null;

	private CostMethod(String methodName) {
//		this.sn=sn;
		this.methodName = methodName;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public static void main(String[] args) {
		for(CostMethod method:CostMethod.values()){
			System.out.println(method.getMethodName());
		}
	}

}
