/*
 * 捷利商业进销存管理系统
 * @(#)ProductChangeDetail.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-8-8
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;

/**
 * 盘盈盘亏明细
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-8-8 下午5:23:47
 */
public class ProductChangeDetail {
	private Long id;
	private Product product;
	private int quantity;
	private BigDecimal amount;
	private String memos;
}
