/*
 * 捷利商业进销存管理系统
 * @(#)AccountCommonServiceAbstractImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-3
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.dao.IBusinessUnitsDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.domain.AccountCommonDetail;
import cn.jely.cd.domain.AccountCommonMaster;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.service.IAccountCommonService;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:AccountCommonServiceAbstractImpl Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-3 上午9:56:12
 */
public abstract class AccountCommonServiceAbstractImpl<T extends AccountCommonMaster> extends BillStateServiceImpl<T>
		implements IAccountCommonService<T> {

	protected IFundAccountDao fundAccountDao;
	protected IBusinessUnitsDao businessUnitsDao;

	/**
	 * 更新往来单位的应收应付
	 * 
	 * @param unit
	 * @param paid
	 *            void
	 */
	protected abstract void updateARAP(Serializable unitId, BigDecimal paid);

	/**
	 * 更新冲销明细中的进/出货单
	 * 
	 * @param master
	 * @param current
	 *            void
	 */
	protected abstract void updateProductMaster(ProductCommonMaster master, BigDecimal current);

	/**
	 * 更新帐户金额
	 * 
	 * @param fundAccount
	 * @param amount
	 *            void
	 */
	protected abstract void updateFundAccount(FundAccount fundAccount, BigDecimal amount);

	// protected abstract ProductCommonMaster getProductMaster(Serializable id);

	public void setBusinessUnitsDao(IBusinessUnitsDao businessUnitsDao) {
		this.businessUnitsDao = businessUnitsDao;
	}

	public void setFundAccountDao(IFundAccountDao fundAccountDao) {
		this.fundAccountDao = fundAccountDao;
	}

	@Override
	protected void prepareModel(T master) {
		if (master == null) {
			throw new EmptyException();
		}
		if (master.getFundAccount() != null
				&& (master.getFundAccount().getId() == -1L || master.getFundAccount().getId() == null)) {
			master.setFundAccount(null);
		}
		if (master.getFundAccount() == null && master.getAmount() != null
				&& master.getAmount().compareTo(BigDecimal.ZERO) > 0) {
			throw new RuntimeException("帐户为空,但金额不为空");
		}
		if (master.getBusinessUnit() == null || master.getBusinessUnit().getId() == null) {
			throw new EmptyException();
		}
		if (master.getEmployee() == null || master.getEmployee().getId() == null) {
			throw new EmptyException();
		}
		if (master.getState() == null) {
			master.setState(StateManager.getDefaultState());
		}
		master.setAmount(SystemCalUtil.checkValue(master.getAmount()));
		master.setDiscount(SystemCalUtil.checkValue(master.getDiscount()));
		checkDetails(master);
	}

	/**
	 * 检查明细数据
	 * 
	 * @param master
	 *            void
	 */
	private void checkDetails(T master) {
		List<AccountCommonDetail> details = master.getDetails();
		if (details != null) {
			BigDecimal total = BigDecimal.ZERO;
			for (AccountCommonDetail detail : details) {
				detail.setArap(SystemCalUtil.checkValue(detail.getArap()));
				detail.setCurrentPay(SystemCalUtil.checkValue(detail.getCurrentPay()));
				BigDecimal currentPay = detail.getCurrentPay();
				if (currentPay == null || currentPay.compareTo(BigDecimal.ZERO) == 0) {// 如果当前冲销为空或为0则删除这条明细
					details.remove(detail);
				} else if (currentPay.compareTo(detail.getArap()) > 0) {// 不允许冲销金额大于可冲销金额
					throw new AttrConflictException("实际冲销金额大于可冲销金额!");
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (AccountCommonDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(master);// 维护主从关系
					total = total.add(detail.getCurrentPay());
				}
			} else {
				throw new EmptyException();
			}
			BigDecimal discount = SystemCalUtil.checkValue(master.getDiscount());
			if (master.getAmount() != null) {
				if (total.compareTo(master.getAmount().add(discount)) != 0) {// 检查付款+折扣是否与明细数据相等
					throw new RuntimeException("明细值合计与合计值不相等");
				}
			} else {
				master.setAmount(total.subtract(discount));
			}
		} else {
			throw new EmptyException();
		}
	}

	@Override
	protected T getOldDomain(T master) {
		return getById(master.getId());
	}

	@Override
	protected void performChange(T master) {
		List<AccountCommonDetail> details = master.getDetails();
		for (AccountCommonDetail detail : details) {
			BigDecimal currentPay = detail.getCurrentPay();
			ProductCommonMaster productMaster = detail.getProductMaster();
			// BigDecimal arap=detail.getArap();
			// BigDecimal
			// remainArap=productMaster.getArap().subtract(productMaster.getPaidArap());
			// if(arap.compareTo(remainArap)>0){
			// throw new RuntimeException("未付款金额与进/出货单据余额不一致");
			// }
			// 更新进货单的已付应付款/出货单的已收应收款
			updateProductMaster(productMaster, currentPay);
			// 更新往来单位的实时应付款
		}
		updateARAP(master.getBusinessUnit().getId(), master.getAmount().add(SystemCalUtil.checkValue(master.getDiscount())));
		if (master.getFundAccount() != null) {
			updateFundAccount(master.getFundAccount(), master.getAmount());
		}
		// businessUnitsDao.updateSubtractPayAble(master.getBusinessUnit(),
		// total);
	}

}
