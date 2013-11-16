/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherInMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.AccountOtherInMaster;
import cn.jely.cd.domain.ProductDeliveryReturnMaster;
import cn.jely.cd.service.IAccountOtherInService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:AccountOtherInMasterAction
 * @Description:Action
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountOtherInAction extends JQGridAction<AccountOtherInMaster> {//有针对树的操作需继承自TreeOperateAction
	private AccountOtherInMaster accountOtherInMaster=new AccountOtherInMaster();
	private IAccountOtherInService accountOtherInService;

	public void setAccountOtherInService(IAccountOtherInService accountOtherInService) {
		this.accountOtherInService = accountOtherInService;
	}

	@Override
	public AccountOtherInMaster getModel() {
		return accountOtherInMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		Pager<AccountOtherInMaster> pager=accountOtherInService.findPager(objectQuery);
		Pager<AccountOtherInMaster> pms=new Pager<AccountOtherInMaster>(pager.getPageNo(),pager.getPageSize(),pager.getTotalRows());
		for(AccountOtherInMaster master:pager.getRows()){
			pms.getRows().add((AccountOtherInMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		List<AccountOtherInMaster> allMasters = accountOtherInService.getAll();
		List<AccountOtherInMaster> allPMs=new ArrayList<AccountOtherInMaster>();
		for(AccountOtherInMaster master:allMasters){
			allPMs.add((AccountOtherInMaster) master.toDisp(false));
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
				accountOtherInService.update(accountOtherInMaster);
			}else{
				accountOtherInService.save(accountOtherInMaster);
			}
			actionJsonResult=new ActionJsonResult(accountOtherInMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			accountOtherInService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				accountOtherInMaster =new AccountOtherInMaster();
//			}
		}else{
			accountOtherInMaster=accountOtherInService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				accountOtherInMaster.setBusinessUnit(null);
				accountOtherInMaster.setEmployee(null);
				accountOtherInMaster.setFundAccount(null);
				accountOtherInMaster.getDetails().clear();
			}
		}
	}
	/**
	 * 生成单据编号
	 * @return
	 * @throws IOException
	 */
	public String getItem() throws IOException {
		actionJsonResult =new ActionJsonResult((Object)((ItemGenAble)accountOtherInService).generateItem(accountOtherInMaster.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData() {
		actionJsonResult=new ActionJsonResult((Object)accountOtherInService.getById(id).toDisp(true));
		return JSONLIST;
	}
}
