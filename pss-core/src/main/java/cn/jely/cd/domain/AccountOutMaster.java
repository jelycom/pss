/*
 * 捷利商业进销存管理系统
 * @(#)AccountOutMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-2
 */
package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @ClassName:AccountOutMaster
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-2 下午7:17:27
 *
 */
public class AccountOutMaster extends AccountCommonMaster {

	public AccountOutMaster() {
		super();
	}

	public AccountOutMaster(BusinessUnits businessUnit, Employee employee, FundAccount fundAccount, Date date, BigDecimal amount, BigDecimal discount) {
		super(businessUnit, employee, fundAccount, date, amount, discount);
	}

	public AccountOutMaster(Long id) {
		super(id);
	}

}
