/*
 * 捷利商业进销存管理系统
 * @(#)FundAccount.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.service.IFundAccountService;

/**
 * @ClassName:FundAccountServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2012-11-14 13:32:04 
 *
 */
@Service("fundAccountService")
public class FundAccountServiceImpl extends BaseServiceImpl<FundAccount> implements
		IFundAccountService {

	private IFundAccountDao fundAccountDao;
	@Resource(name="fundAccountDao")
	public void setFundAccountDao(IFundAccountDao fundAccountDao) {
		this.fundAccountDao = fundAccountDao;
	}

	@Override
	public IBaseDao<FundAccount> getBaseDao() {

		return fundAccountDao;
	}


}
