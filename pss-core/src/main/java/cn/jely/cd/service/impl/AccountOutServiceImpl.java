/*
 * 捷利商业进销存管理系统
 * @(#)AccountOutServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-2
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.jely.cd.dao.IAccountOutDao;
import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBusinessUnitsDao;
import cn.jely.cd.dao.IProductCommonDao;
import cn.jely.cd.dao.IProductPurchaseDao;
import cn.jely.cd.dao.IProductPurchaseReturnDao;
import cn.jely.cd.domain.AccountOutMaster;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.domain.ProductPurchaseReturnMaster;
import cn.jely.cd.service.IAccountOutService;
import cn.jely.cd.util.code.DateCoder;
import cn.jely.cd.util.code.ICodeGenerator;
import cn.jely.cd.util.code.impl.MonthGenerator;

/**
 * @ClassName:AccountOutServiceImpl Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-2 下午7:46:07
 * 
 */
public class AccountOutServiceImpl extends AccountCommonServiceAbstractImpl<AccountOutMaster> implements
		IAccountOutService {

	private IAccountOutDao accountOutDao;
	private IProductPurchaseDao productPurchaseDao;
	private IProductPurchaseReturnDao productPurchaseReturnDao;
	private IBusinessUnitsDao businessUnitsDao;

	public void setProductPurchaseReturnDao(IProductPurchaseReturnDao productPurchaseReturnDao) {
		this.productPurchaseReturnDao = productPurchaseReturnDao;
	}

	public void setBusinessUnitsDao(IBusinessUnitsDao businessUnitsDao) {
		this.businessUnitsDao = businessUnitsDao;
	}

	public void setProductPurchaseDao(IProductPurchaseDao productPurchaseDao) {
		this.productPurchaseDao = productPurchaseDao;
	}

	public void setAccountOutDao(IAccountOutDao accountOutDao) {
		this.accountOutDao = accountOutDao;
	}

	// @Override
	// public Long save(AccountOutMaster master) {
	// List<AccountCommonDetail> details = master.getDetails();
	// BigDecimal total = master.getDiscount() == null ? BigDecimal.ZERO :
	// master.getDiscount();
	// total = total.add(master.getAmount());// 可冲销合计=付款金额+优惠
	// int orders = 1;
	// for (AccountCommonDetail detail : details) {
	// detail.setMaster(master);
	// detail.setOrders(orders++);
	// BigDecimal currentPay = detail.getCurrentPay();
	// ProductCommonMaster productMaster = detail.getProductMaster();
	// //更新进货单的已付应付款
	// productPurchaseDao.update((ProductPurchaseMaster)productMaster,currentPay);
	// }
	// //更新往来单位的实时应付款
	// businessUnitsDao.updateSubtractPayAble(master.getBusinessUnit(), total);
	// return super.save(master);
	// }

	@Override
	protected void updateARAP(Serializable unitId, BigDecimal paid) {
		businessUnitsDao.updateSubtractPayAble(unitId, paid, null);
	}

	@Override
	protected void updateProductMaster(ProductCommonMaster master, BigDecimal current) {
		Long id = master.getId();
		master = productPurchaseDao.getAllMasterById(id);
		productPurchaseDao.updatePaidARAP(master, current);
//		if (master instanceof ProductPurchaseMaster) {
//		} else if (master instanceof ProductPurchaseReturnMaster) {
//			productPurchaseReturnDao.updatePaidARAP(master, current);
//		}
	}

	@Override
	protected IBaseDao<AccountOutMaster> getBaseDao() {
		return accountOutDao;
	}

	@Override
	protected void updateFundAccount(FundAccount fundAccount, BigDecimal amount) {
		fundAccountDao.subCurrent(fundAccount.getId(), amount);
	}

}
