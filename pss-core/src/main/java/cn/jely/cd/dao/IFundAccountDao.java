/*
 * 捷利商业进销存管理系统
 * @(#)Fundaccount.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.jely.cd.domain.FundAccount;

/**
 * @ClassName:FundaccountAction
 * @Description:Dao
 * @author
 * @version 2012-11-14 13:32:04 
 *
 */
public interface IFundAccountDao extends IBaseDao<FundAccount> {
	public void addCurrent(Serializable id,BigDecimal value);
	public void subCurrent(Serializable id,BigDecimal value);
}
