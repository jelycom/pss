/*
 * 捷利商业进销存管理系统
 * @(#)AccountTransferServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-22
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.dao.IAccountTransferDao;
import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.domain.AccountTransferDetail;
import cn.jely.cd.domain.AccountTransferMaster;
import cn.jely.cd.service.IAccountTransferService;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.StateManager;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-22 下午6:15:08
 */
public class AccountTransferServiceImpl extends BillStateServiceImpl<AccountTransferMaster> implements
		IAccountTransferService {

	private IAccountTransferDao accountTransferDao;
	private IFundAccountDao fundAccountDao;

	public void setFundAccountDao(IFundAccountDao fundAccountDao) {
		this.fundAccountDao = fundAccountDao;
	}

	public void setAccountTransferDao(IAccountTransferDao accountTransferDao) {
		this.accountTransferDao = accountTransferDao;
	}

	@Override
	protected AccountTransferMaster getOldDomain(AccountTransferMaster master) {
		return accountTransferDao.getById(master.getId());
	}

	@Override
	protected void prepareModel(AccountTransferMaster master) {
		if (master == null) {
			throw new EmptyException();
		}
		master.setAmount(SystemCalUtil.checkValue(master.getAmount()));
		master.setCost(SystemCalUtil.checkValue(master.getCost()));
		if (master.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			throw new EmptyException("金额不能为0!");
		}
		if (master.getFundAccount() == null
				|| (master.getFundAccount().getId() == -1L || master.getFundAccount().getId() == null)) {
			throw new RuntimeException("转出帐户为空");
		}
		if (master.getEmployee() == null || master.getEmployee().getId() == null) {
			throw new EmptyException();
		}
		if (master.getState() == null) {
			master.setState(StateManager.getDefaultState());
		}
		checkDetails(master);
	}

	/**
	 * 检查明细
	 * 
	 * @param master
	 *            void
	 */
	private void checkDetails(AccountTransferMaster master) {
		List<AccountTransferDetail> details = master.getDetails();
		if (details != null) {
			BigDecimal total = BigDecimal.ZERO;
			for (AccountTransferDetail detail : details) {
				detail.setInAmount(SystemCalUtil.checkValue(detail.getInAmount()));
				BigDecimal currentPay = detail.getInAmount();
				if (currentPay == null || currentPay.compareTo(BigDecimal.ZERO) == 0) {// 如果当前冲销为空或为0则删除这条明细
					details.remove(detail);
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (AccountTransferDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(master);// 维护主从关系
					total = total.add(SystemCalUtil.checkValue(detail.getInAmount()));
				}
			} else {
				throw new EmptyException();
			}
			BigDecimal cost = SystemCalUtil.checkValue(master.getCost());
			if (total.compareTo(master.getAmount().subtract(cost)) != 0) {// 检查付款是否与明细数据合计+手续费用相等
				throw new RuntimeException("明细值合计与合计值不相等");
			}
		} else {
			throw new EmptyException();
		}
	}

	@Override
	protected void performChange(AccountTransferMaster master) {
		BigDecimal totalAmount = master.getAmount().subtract(master.getCost());
		fundAccountDao.subCurrent(master.getFundAccount().getId(), totalAmount);
		for (AccountTransferDetail detail : master.getDetails()) {
			fundAccountDao.addCurrent(detail.getInAccount().getId(),detail.getInAmount());
		}
	}

	@Override
	protected IBaseDao<AccountTransferMaster> getBaseDao() {
		return accountTransferDao;
	}

}
