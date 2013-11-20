/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherInServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-22
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.dao.IAccountOtherInDao;
import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.domain.AccountOtherCommonDetail;
import cn.jely.cd.domain.AccountOtherInMaster;
import cn.jely.cd.service.IAccountOtherInService;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.StateManager;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-22 下午9:51:24
 */
public class AccountOtherInServiceImpl extends BillStateServiceImpl<AccountOtherInMaster> implements IAccountOtherInService {
	private IAccountOtherInDao accountOtherInDao;
	private IFundAccountDao fundAccountDao;

	public void setAccountOtherInDao(IAccountOtherInDao accountOtherInDao) {
		this.accountOtherInDao = accountOtherInDao;
	}
	public void setFundAccountDao(IFundAccountDao fundAccountDao) {
		this.fundAccountDao = fundAccountDao;
	}


	@Override
	protected AccountOtherInMaster getOldDomain(AccountOtherInMaster master) {
		return getById(master.getId());
	}

	@Override
	protected void prepareModel(AccountOtherInMaster master) {
		if(master==null){
			throw new EmptyException();
		}
		if(master.getBusinessUnit()==null||master.getBusinessUnit().getId()==null){
			throw new EmptyException("往来单位不能为空!");
		}
		master.setAmount(SystemCalUtil.checkValue(master.getAmount()));
		master.setDiscount(SystemCalUtil.checkValue(master.getDiscount()));
		if(master.getAmount().compareTo(BigDecimal.ZERO)<=0){
			throw new RuntimeException("金额不能为空或小于0!");
		}
		BigDecimal detailTotal=checkDetails(master);
		if (master.getAmount() != null) {
			if (detailTotal.compareTo(master.getAmount().add(master.getDiscount())) != 0) {
				throw new RuntimeException("明细值合计与合计值不相等");
			}
		} else {
			master.setAmount(detailTotal);
		}
		if(master.getState()==null){
			master.setState(StateManager.getDefaultState());
		}
	}

	/**
	 * 检查明细
	 * @param master void
	 */
	private BigDecimal checkDetails(AccountOtherInMaster master) {
		List<AccountOtherCommonDetail> details = master.getDetails();
		if (details != null) {
			BigDecimal total = BigDecimal.ZERO;
			for (int i = 0; i < details.size(); i++) {
				AccountOtherCommonDetail detail1 = details.get(i);
				if (detail1.getBursary() == null || detail1.getPay()==null ||detail1.getPay().compareTo(BigDecimal.ZERO)==0) {
					details.remove(i);
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (AccountOtherCommonDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(master);// 维护主从关系
					total = total.add(SystemCalUtil.checkValue(detail.getPay()));
				}
			} else {
				throw new EmptyException("无有效的明细.");
			}
			return total;
		} else {
			throw new EmptyException();
		}
	}
	
	@Override
	protected void performChange(AccountOtherInMaster master) {
		fundAccountDao.addCurrent(master.getFundAccount().getId(), master.getAmount());
	}

	@Override
	protected IBaseDao<AccountOtherInMaster> getBaseDao() {
		return accountOtherInDao;
	}


}
