/*
 * 捷利商业进销存管理系统
 * @(#)Fundaccount.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.domain.FundAccount;

/**
 * @ClassName:FundaccountDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2012-11-14 13:32:04 
 *
 */
@Repository("fundAccountDao")
public class FundAccountDaoImpl extends BaseDaoImpl<FundAccount> implements IFundAccountDao {

	@Override
	public void addCurrent(Serializable id, BigDecimal value) {
		FundAccount fd=getById(id);
		BigDecimal fdCurrent=fd.getCurrent()==null?BigDecimal.ZERO:fd.getCurrent();
		fd.setCurrent(fdCurrent.add(value));
		update(fd);
	}

	@Override
	public void subCurrent(Serializable id, BigDecimal value) {
		FundAccount fd=getById(id);
		BigDecimal fdCurrent=fd.getCurrent()==null?BigDecimal.ZERO:fd.getCurrent();
		fd.setCurrent(fdCurrent.subtract(value));
		update(fd);
	}

}
