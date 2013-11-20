/*
 * 捷利商业进销存管理系统
 * @(#)JQGrid2QueryGroupTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-30
 */
package cn.jely.cd.util;

import org.junit.Test;

import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.ObjectQueryTool;
import cn.jely.cd.util.query.QueryGroup;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-8-30 下午5:18:44
 */
public class JQGrid2QueryGroupTest {

	@Test
	public void testParseJqgridJson(){
		String filters="{\"groupOp\":\"OR\",\"rules\":[{\"field\":\"item\",\"op\":\"en\",\"data\":\"123\"}],\"groups\":[{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"billDate\",\"op\":\"eq\",\"data\":\"2013-08-02\"},{\"field\":\"billDate\",\"op\":\"eq\",\"data\":\"2013-08-08\"}],\"groups\":[]},{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"billDate\",\"op\":\"eq\",\"data\":\"\"}],\"groups\":[]}]}";
		QueryGroup qg=ObjectQueryTool.QueryGroupPaser.parseJqgridJson(filters);
		System.out.println(ObjectQueryTool.getAllRule(qg));
		ObjectQuery objectQuery=new ObjectQuery();
		objectQuery.setQueryClass(ProductPurchaseMaster.class);
		objectQuery.addWhere(" id=:id", "id",6);
		objectQuery.setQueryGroup(qg);
		System.out.println(objectQuery.getWhere());
		System.out.println(qg);
	}
	
	@Test
	public void testGetRealData(){
		Object realData = ObjectQueryTool.getRealData(java.util.Date.class, "");
		System.out.println(realData);
	}
}
