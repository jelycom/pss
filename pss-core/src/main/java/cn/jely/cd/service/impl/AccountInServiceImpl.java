/*
 * 捷利商业进销存管理系统
 * @(#)AccountInServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-2
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.jely.cd.dao.IAccountInDao;
import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductDeliveryDao;
import cn.jely.cd.dao.IProductDeliveryReturnDao;
import cn.jely.cd.domain.AccountInMaster;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.ProductDeliveryReturnMaster;
import cn.jely.cd.service.IAccountInService;

/**
 * @ClassName:AccountInServiceImpl
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-2 下午7:46:07
 *
 */
public class AccountInServiceImpl extends AccountCommonServiceAbstractImpl<AccountInMaster> implements IAccountInService {
	
	private IAccountInDao accountInDao;
	private IProductDeliveryDao productDeliveryDao;
	private IProductDeliveryReturnDao productDeliveryReturnDao;
	
	public void setProductDeliveryReturnDao(IProductDeliveryReturnDao productDeliveryReturnDao) {
		this.productDeliveryReturnDao = productDeliveryReturnDao;
	}

	public void setProductDeliveryDao(IProductDeliveryDao productDeliveryDao) {
		this.productDeliveryDao = productDeliveryDao;
	}
	
	public void setAccountInDao(IAccountInDao accountInDao) {
		this.accountInDao = accountInDao;
	}


	@Override
	protected IBaseDao<AccountInMaster> getBaseDao() {
		return accountInDao;
	}

	@Override
	protected void updateARAP(Serializable unitId, BigDecimal paid) {
		businessUnitsDao.updateSubtractReceiveAble(unitId, paid, null);
	}

	@Override
	protected void updateProductMaster(ProductCommonMaster master, BigDecimal current) {
		master=productDeliveryDao.getAllMasterById(master.getId());
		productDeliveryDao.updatePaidARAP( master, current);
//		if(master instanceof ProductDeliveryMaster){
//		}else if(master instanceof ProductDeliveryReturnMaster){
//			productDeliveryReturnDao.updatePaidARAP(master, current);
//		}
	}

	@Override
	protected void updateFundAccount(FundAccount fundAccount, BigDecimal amount) {
		fundAccountDao.addCurrent(fundAccount.getId(), amount);		
	}

//	@Override
//	protected ProductCommonMaster getProductMaster(Serializable id) {
//		return productDeliveryDao.getById(id);
//	}
}
