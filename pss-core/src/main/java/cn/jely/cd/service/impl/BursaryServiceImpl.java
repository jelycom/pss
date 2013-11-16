/*
 * 捷利商业进销存管理系统
 * @(#)Bursary.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.util.List;

import com.sun.tools.apt.util.Bark;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBursaryDao;
import cn.jely.cd.domain.Bursary;
import cn.jely.cd.service.IBursaryService;
import cn.jely.cd.service.NestedTreeServiceImpl;

/**
 * @ClassName:BursaryServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-08-05 10:50:45 
 *
 */
public class BursaryServiceImpl extends NestedTreeServiceImpl<Bursary> implements
		IBursaryService {

	private IBursaryDao bursaryDao;

	public void setBursaryDao(IBursaryDao bursaryDao) {
		this.bursaryDao = bursaryDao;
	}

	@Override
	public IBaseDao<Bursary> getBaseDao() {
		return bursaryDao;
	}

	@Override
	protected Boolean beforeSaveCheck(Bursary t) {
		return !bursaryDao.checkExist(t);//如果存在则返回false来阻止保存.
	}

	@Override
	public List<Bursary> findOtherInCome() {
		Bursary otherInCome=bursaryDao.findByItem(Bursary.OTHERINCOME_ITEM);
		return bursaryDao.findTreeNodes(otherInCome.getId(), true);
	}

	@Override
	public List<Bursary> findOtherOutCome() {
		Bursary otherInCome=bursaryDao.findByItem(Bursary.OTHEROUTCOME_ITEM);
		return bursaryDao.findTreeNodes(otherInCome.getId(), true);
	}

	@Override
	public List<Bursary> findAccount() {
		Bursary cash=bursaryDao.findByItem(Bursary.CASH_ITEM);
		List<Bursary> cashes=findTreeNodes(cash.getId(), true);//现金帐户
		Bursary bankAccount=bursaryDao.findByItem(Bursary.BANK_ITEM);
		List<Bursary> bankAccounts=bursaryDao.findTreeNodes(bankAccount.getId(), true);//银行帐户
		cashes.addAll(bankAccounts);
		return cashes;
	}

}
