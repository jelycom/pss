/*
 * 捷利商业进销存管理系统
 * @(#)AccountInMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.AccountCommonMaster;
import cn.jely.cd.domain.AccountInMaster;
import cn.jely.cd.pagemodel.AccountCommonMasterPM;
import cn.jely.cd.service.IAccountInService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:AccountInMasterAction
 * @Description:Action
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountInAction extends JQGridAction<AccountInMaster> {//有针对树的操作需继承自TreeOperateAction
	private AccountInMaster accountInMaster=new AccountInMaster();
	private IAccountInService accountInService;
	private Pager<AccountInMaster> pager;

	public void setAccountInService(IAccountInService accountInService) {
		this.accountInService = accountInService;
	}

	public Pager<AccountInMaster> getPager() {
		return pager;
	}

	@Override
	public AccountInMaster getModel() {
		return accountInMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		Pager<AccountInMaster> pager=accountInService.findPager(objectQuery);
		Pager<AccountInMaster> pms=new Pager<AccountInMaster>(pager.getPageNo(), pager.getPageSize(), pager.getTotalRows());
		for(AccountInMaster master:pager.getRows()){
			pms.getRows().add((AccountInMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		List<AccountInMaster> allMasters = accountInService.getAll();
		List<AccountInMaster> allPMs=new ArrayList<AccountInMaster>();
		for(AccountInMaster master:allMasters){
			allPMs.add((AccountInMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				accountInService.update(accountInMaster);
			}else{
				accountInService.save(accountInMaster);
			}
			actionJsonResult=new ActionJsonResult(accountInMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			accountInService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				accountInMaster =new AccountInMaster();
//			}
		}else{
			accountInMaster=accountInService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				accountInMaster.setBusinessUnit(null);
				accountInMaster.setEmployee(null);
				accountInMaster.setEmployee(null);
				accountInMaster.getDetails().clear();
			}
		}
	}
	/**
	 * 生成单据编号
	 * @return
	 * @throws IOException
	 */
	public String getItem() throws IOException {
		actionJsonResult =new ActionJsonResult((Object)((ItemGenAble)accountInService).generateItem(accountInMaster.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData() {
		actionJsonResult=new ActionJsonResult((Object)accountInService.getById(id).toDisp(true));
		return JSONLIST;
	}
}
