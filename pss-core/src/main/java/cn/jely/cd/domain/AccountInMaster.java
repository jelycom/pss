/*
 * 捷利商业进销存管理系统
 * @(#)AccountInMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-2
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @ClassName:AccountInMaster
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-2 下午7:17:12
 *
 */
public class AccountInMaster extends AccountCommonMaster {

	public AccountInMaster() {
		super();
	}

	public AccountInMaster(BusinessUnits businessUnit, Employee employee, FundAccount fundAccount, Date date, BigDecimal amount, BigDecimal discount) {
		super(businessUnit, employee, fundAccount, date, amount, discount);
	}

	public AccountInMaster(Long id) {
		super(id);
	}
	
}
