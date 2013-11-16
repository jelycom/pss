/*
 * 捷利商业进销存管理系统
 * @(#)AccountingPeriod.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.service.IAccountingPeriodService;
import cn.jely.cd.sys.service.ISystemSettingService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:AccountingPeriodAction
 * @Description:Action
 * @author
 * @version 2013-04-11 17:30:51 
 *
 */
public class AccountingPeriodAction extends JQGridAction<AccountingPeriod> {
	
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	//有针对树的操作需继承自TreeOperateAction
	
	private AccountingPeriod accountingPeriod;
	private IAccountingPeriodService accountingPeriodService;
	private ISystemSettingService systemSettingService;
	private Pager<AccountingPeriod> pager;


	public void setSystemSettingService(ISystemSettingService systemSettingService) {
		this.systemSettingService = systemSettingService;
	}

	public void setAccountingPeriodService(IAccountingPeriodService accountingPeriodService) {
		this.accountingPeriodService = accountingPeriodService;
	}

	public Pager<AccountingPeriod> getPager() {
		return pager;
	}

	@Override
	public AccountingPeriod getModel() {
		return accountingPeriod;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		parseCondition();
		pager=accountingPeriodService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(accountingPeriodService.getAll());
		return JSONALL;
	}

	@Override
	protected String save() {
		return null;
	}

	/**
	 * 期初开帐
	 * @return String
	 */
	public String saveinitperiod() {
		Boolean isOpened = accountingPeriodService.saveInitPeriod();
		if(isOpened){
			SystemSetting setting=(SystemSetting) getApplication().get(ConstValue.SYS_SETTING);
			setting.getCompanyInfo().setOpened(isOpened);
			systemSettingService.save(setting);
		}
		actionJsonResult=new ActionJsonResult(isOpened,"");
		return JSONLIST;
	}

	/**
	 * 期初反开帐
	 * @return String
	 */
	public String saveunstartinitperiod(){
		Boolean operate = accountingPeriodService.saveUnStartInitPeriod();
		actionJsonResult=new ActionJsonResult(operate, "");
		return JSONLIST;
	}
	
	@Override
	public String delete() {
		return null;
	}

	@Override
	protected void beforInputSave() {
//		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				accountingPeriod =new AccountingPeriod();
//			}
//		}else{
//			accountingPeriod=accountingPeriodService.getById(id);
//			if (isEditSave()) {
//				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
//				// 所以要在保存前将其关联的对象置空
//			}
//		}
	}

}
