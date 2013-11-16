/*
 * 捷利商业进销存管理系统
 * @(#)AccountOutMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.AccountInMaster;
import cn.jely.cd.domain.AccountOutMaster;
import cn.jely.cd.pagemodel.AccountCommonMasterPM;
import cn.jely.cd.service.IAccountOutService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:AccountOutMasterAction
 * @Description:Action
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountOutAction extends JQGridAction<AccountOutMaster> {//有针对树的操作需继承自TreeOperateAction
	private AccountOutMaster accountOutMaster = new AccountOutMaster();
	private IAccountOutService accountOutService;

	public void setAccountOutService(IAccountOutService accountOutService) {
		this.accountOutService = accountOutService;
	}

	@Override
	public AccountOutMaster getModel() {
		return accountOutMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		Pager<AccountOutMaster> pager=accountOutService.findPager(objectQuery);
		Pager<AccountOutMaster> pms=new Pager<AccountOutMaster>(pager.getPageNo(), pager.getPageSize(), pager.getTotalRows());
		for(AccountOutMaster master:pager.getRows()){
			pms.getRows().add((AccountOutMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		List<AccountOutMaster> allMasters = accountOutService.getAll();
		List<AccountOutMaster> allPMs=new ArrayList<AccountOutMaster>();
		for(AccountOutMaster master:allMasters){
			allPMs.add((AccountOutMaster) master.toDisp(false));
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
				accountOutService.update(accountOutMaster);
			}else{
				accountOutService.save(accountOutMaster);
			}
			actionJsonResult=new ActionJsonResult(accountOutMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			accountOutService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				accountOutMaster =new AccountOutMaster();
//			}
		}else{
			accountOutMaster=accountOutService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				accountOutMaster.setBusinessUnit(null);
				accountOutMaster.setFundAccount(null);
				accountOutMaster.setEmployee(null);
				accountOutMaster.getDetails().clear();
			}
		}
	}
	/**
	 * 生成单据编号
	 * @return
	 * @throws IOException
	 */
	public String getItem() throws IOException {
		actionJsonResult =new ActionJsonResult((Object)((ItemGenAble)accountOutService).generateItem(accountOutMaster.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData() {
		actionJsonResult=new ActionJsonResult((Object)accountOutService.getById(id).toDisp(true));
		return JSONLIST;
	}
}
