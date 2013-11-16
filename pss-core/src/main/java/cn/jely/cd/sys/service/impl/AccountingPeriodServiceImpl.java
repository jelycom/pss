/*
 * 捷利商业进销存管理系统
 * @(#)AccountingPeriod.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBusinessUnitsDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.dao.IProductPlanDeliveryDao;
import cn.jely.cd.dao.IProductPlanPurchaseDao;
import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.sys.dao.IAccountingPeriodDao;
import cn.jely.cd.sys.dao.ICompanyInfoDao;
import cn.jely.cd.sys.dao.IPeriodARAPDao;
import cn.jely.cd.sys.dao.IPeriodAccountDao;
import cn.jely.cd.sys.dao.IPeriodStockDao;
import cn.jely.cd.sys.dao.ISystemSettingDao;
import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.sys.domain.CompanyInfo;
import cn.jely.cd.sys.domain.PeriodARAP;
import cn.jely.cd.sys.domain.PeriodAccount;
import cn.jely.cd.sys.domain.PeriodStock;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.service.IAccountingPeriodService;

/**
 * @ClassName:AccountingPeriodServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-04-11 17:30:51
 * 
 */
public class AccountingPeriodServiceImpl extends BaseServiceImpl<AccountingPeriod> implements IAccountingPeriodService {

	private IAccountingPeriodDao accountingPeriodDao;
	private IPeriodAccountDao periodAccountDao;
	private IPeriodARAPDao periodARAPDao;
	private IPeriodStockDao periodStockDao;
	private ICompanyInfoDao companyInfoDao;
	private ISystemSettingDao systemSettingDao;
	private IFundAccountDao fundAccountDao;
	private IProductDao productDao;
	private IBusinessUnitsDao businessUnitsDao;
	private IProductPlanDeliveryDao productPlanDeliveryDao;
	private IProductPlanPurchaseDao productPlanPurchaseDao;
	private IProductStockDetailDao productStockDetailDao;

	public void setSystemSettingDao(ISystemSettingDao systemSettingDao) {
		this.systemSettingDao = systemSettingDao;
	}

	public void setProductStockDetailDao(IProductStockDetailDao productStockDetailDao) {
		this.productStockDetailDao = productStockDetailDao;
	}

	public void setAccountingPeriodDao(IAccountingPeriodDao accountingPeriodDao) {
		this.accountingPeriodDao = accountingPeriodDao;
	}

	public void setProductPlanPurchaseDao(IProductPlanPurchaseDao productPlanPurchaseDao) {
		this.productPlanPurchaseDao = productPlanPurchaseDao;
	}

	public void setProductPlanDeliveryDao(IProductPlanDeliveryDao productPlanDeliveryDao) {
		this.productPlanDeliveryDao = productPlanDeliveryDao;
	}

	public void setBusinessUnitsDao(IBusinessUnitsDao businessUnitsDao) {
		this.businessUnitsDao = businessUnitsDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public void setFundAccountDao(IFundAccountDao fundAccountDao) {
		this.fundAccountDao = fundAccountDao;
	}

	public void setPeriodAccountDao(IPeriodAccountDao periodAccountDao) {
		this.periodAccountDao = periodAccountDao;
	}

	public void setPeriodARAPDao(IPeriodARAPDao periodARAPDao) {
		this.periodARAPDao = periodARAPDao;
	}

	public void setPeriodStockDao(IPeriodStockDao periodStockDao) {
		this.periodStockDao = periodStockDao;
	}

	public void setCompanyInfoDao(ICompanyInfoDao companyInfoDao) {
		this.companyInfoDao = companyInfoDao;
	}

	@Override
	public IBaseDao<AccountingPeriod> getBaseDao() {
		return accountingPeriodDao;
	}

	@Override
	public Boolean saveInitPeriod() {
		Timestamp date = getDBTime();
		SystemSetting systemSetting = systemSettingDao.getSetting();
		CompanyInfo companyInfo = systemSetting.getCompanyInfo();
		AccountingPeriod validPeriod = accountingPeriodDao.findValidPeriod();
		if (validPeriod == null) {// 如果不存在激活的期间
			AccountingPeriod accountingPeriod = new AccountingPeriod(AccountingPeriod.INITPERIOD, date, date);
			accountingPeriod.setState(AccountingPeriod.INUSE);
			accountingPeriodDao.save(accountingPeriod);
			// companyInfo.setCurrentPeriod(accountingPeriod);
			updateAccount(accountingPeriod);
			updateProduct(accountingPeriod);
			updateBusiness(accountingPeriod);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("accountingPeriod", accountingPeriod);
			periodAccountDao.executeHql("update PeriodAccount set accountingPeriod=:accountingPeriod,current=begin where accountingPeriod is null",
							param);
			periodARAPDao.executeHql("update PeriodARAP o set o.accountingPeriod=:accountingPeriod where o.accountingPeriod is null",
					param);
			// int i=3/0;//故意出错,看事务是否回滚
			periodStockDao.executeHql("update PeriodStock o set o.accountingPeriod=:accountingPeriod where o.accountingPeriod is null",
					param);
			companyInfo.setOpenningDate(date);
			companyInfo.setOpened(true);
			systemSettingDao.save(systemSetting);// 保存公司信息
			return true;
		}
		return false;
	}

	/**
	 * 更新往来单位的应收应付
	 * 
	 * @Title:updateBusiness
	 * @param accountingPeriod
	 *            void
	 */
	private void updateBusiness(AccountingPeriod accountingPeriod) {
		List<PeriodARAP> allPeriodARAPs = periodARAPDao.findAllUnAsso();
		if (allPeriodARAPs != null && allPeriodARAPs.size() > 0) {
			for (PeriodARAP periodARAP : allPeriodARAPs) {
				periodARAP.setAccountingPeriod(accountingPeriod);
				BusinessUnits bu = periodARAP.getBusinessUnits();
				bu.setCurrentPayAble(periodARAP.getPayable());
				bu.setCurrentReceiveAble(periodARAP.getReceivable());
				businessUnitsDao.save(bu);
				// periodARAPDao.save(periodARAP);
			}
		}
	}

	/**
	 * 更新产品的当前库存数及当前库存金额
	 * 
	 * @Title:updateProduct
	 * @param accountingPeriod
	 *            void
	 */
	private void updateProduct(AccountingPeriod accountingPeriod) {
		List<PeriodStock> allPeriodStocks = periodStockDao.findAllUnAsso();
		if (allPeriodStocks != null && allPeriodStocks.size() > 0) {
			for (PeriodStock periodStock : allPeriodStocks) {
				periodStock.setAccountingPeriod(accountingPeriod);
				ProductStockDetail detail = new ProductStockDetail();
				try {
					BeanUtils.copyProperties(detail, periodStock);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				detail.setInquantity(periodStock.getQuantity());
				productStockDetailDao.save(detail);
				// Product product = periodStock.getProduct();
				// productDao.save(product);
				// periodStockDao.save(periodStock);
			}
		}
	}

	/**
	 * 更新帐户的当前金额
	 * 
	 * @Title:updateAccount
	 * @param accountingPeriod
	 *            void
	 */
	private void updateAccount(AccountingPeriod accountingPeriod) {
		List<PeriodAccount> allperiodAccounts = periodAccountDao.findAllUnAsso();
		if (allperiodAccounts != null && allperiodAccounts.size() > 0) {
			for (PeriodAccount periodAccount : allperiodAccounts) {
				periodAccount.setAccountingPeriod(accountingPeriod);
				FundAccount fundAccount = periodAccount.getFundAccount();
				fundAccount.setCurrent(periodAccount.getBegin());
				fundAccountDao.update(fundAccount);
				// periodAccountDao.save(periodAccount);//最好在之后进行Session.evict和Session.flush.
			}
		}
	}

	@Override
	public Boolean saveUnStartInitPeriod() {
		logger.info("---------unStart InitPeriod---------");
		AccountingPeriod initPeriod = accountingPeriodDao.findValidPeriod();
		if (initPeriod != null && AccountingPeriod.INITPERIOD.equals(initPeriod.getName())
				&& AccountingPeriod.INUSE == initPeriod.getState()) {
			if (checkExistBill()) {// 如果已经存在生效单据
				return false;// 或者可抛出一个运行时错误
			} else {// 不存在才可进行反开帐操作
			// CompanyInfo companyInfo = companyInfoDao.findObject(null);
			// companyInfo.setCurrentPeriod(null);
			// companyInfoDao.save(companyInfo);
				unLinkPeriod(initPeriod);
				// initPeriod.setState(AccountingPeriod.UNUSE);
				// accountingPeriodDao.save(initPeriod);
				accountingPeriodDao.delete(initPeriod.getId());
				SystemSetting setting = systemSettingDao.getSetting();
				CompanyInfo companyInfo = setting.getCompanyInfo();
				companyInfo.setOpenningDate(null);
				companyInfo.setOpened(false);
				systemSettingDao.save(setting);
				return true;
			}
		}
		return false;
	}

	/**
	 * 解除期初数据所关联的期间
	 * 
	 * @Title:unLinkPeriod
	 * @param initPeriod
	 *            void
	 */
	private void unLinkPeriod(AccountingPeriod initPeriod) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("initPeriod", initPeriod);
		periodAccountDao.executeHql(
				"update PeriodAccount set accountingPeriod=null where accountingPeriod=:initPeriod", param);
		periodStockDao.executeHql("update PeriodStock set accountingPeriod=null where accountingPeriod=:initPeriod",
				param);
		periodARAPDao.executeHql("update PeriodARAP set accountingPeriod=null where accountingPeriod=:initPeriod",
				param);
		productDao.executeHql("update Product set currentQuanlity=null ,currentAmount=null");
		fundAccountDao.executeHql("update FundAccount set current=null");
		businessUnitsDao.executeHql("update BusinessUnits set currentPayAble=null,currentReceiveAble=null");
		productStockDetailDao.delete(null, null);
		// List<PeriodAccount>
		// accounts=periodAccountDao.findAllAssoTo(initPeriod);
		// for(PeriodAccount account:accounts){
		// account.setAccountingPeriod(null);
		// periodAccountDao.save(account);
		// }
		// List<PeriodStock> stocks=periodStockDao.findAllAssoTo(initPeriod);
		// for(PeriodStock stock:stocks){
		// stock.setAccountingPeriod(null);
		// periodStockDao.save(stock);
		// }
		// List<PeriodARAP> araps=periodARAPDao.findAllAssoTo(initPeriod);
		// for(PeriodARAP arap:araps){
		// arap.setAccountingPeriod(null);
		// periodARAPDao.save(arap);
		// }
	}

	/**
	 * 检查相关的业务单据是否存在
	 * 
	 * @Title:checkBill
	 * @return Boolean true:存在已生效的单据,false无生效单据
	 */
	private Boolean checkExistBill() {
		if (productPlanDeliveryDao.countAllFinished() > 0 || productPlanPurchaseDao.countAllFinished() > 0) {// 如果存在计划单
			return true;
		}
		return false;
	}

	@Override
	public Boolean ClosePeriod() {

		return null;
	}

	@Override
	public Boolean unClosePeriod() {
		return null;
	}

}
