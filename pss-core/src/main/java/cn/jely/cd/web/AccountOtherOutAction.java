/*
 * 捷利商业进销存管理系统
 * @(#)AccountOtherOutMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.AccountOtherOutMaster;
import cn.jely.cd.service.IAccountOtherOutService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:AccountOtherOutMasterAction
 * @Description:Action
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountOtherOutAction extends JQGridAction<AccountOtherOutMaster> {//有针对树的操作需继承自TreeOperateAction
	private AccountOtherOutMaster accountOtherOutMaster = new AccountOtherOutMaster();
	private IAccountOtherOutService accountOtherOutService;
	private Pager<AccountOtherOutMaster> pager;

	public void setAccountOtherOutService(IAccountOtherOutService accountOtherOutService) {
		this.accountOtherOutService = accountOtherOutService;
	}

	public Pager<AccountOtherOutMaster> getPager() {
		return pager;
	}

	@Override
	public AccountOtherOutMaster getModel() {
		return accountOtherOutMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		Pager<AccountOtherOutMaster> pager=accountOtherOutService.findPager(objectQuery);
		Pager<AccountOtherOutMaster> pms=new Pager<AccountOtherOutMaster>(pager.getPageNo(),pager.getPageSize(),pager.getTotalRows());
		for(AccountOtherOutMaster master:pager.getRows()){
			pms.getRows().add((AccountOtherOutMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		List<AccountOtherOutMaster> allMasters = accountOtherOutService.getAll();
		List<AccountOtherOutMaster> allPMs=new ArrayList<AccountOtherOutMaster>();
		for(AccountOtherOutMaster master:allMasters){
			allPMs.add((AccountOtherOutMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@OutputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				accountOtherOutService.update(accountOtherOutMaster);
			}else{
				accountOtherOutService.save(accountOtherOutMaster);
			}
			actionJsonResult=new ActionJsonResult(accountOtherOutMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			accountOtherOutService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				accountOtherOutMaster =new AccountOtherOutMaster();
//			}
		}else{
			accountOtherOutMaster=accountOtherOutService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				accountOtherOutMaster.setBusinessUnit(null);
				accountOtherOutMaster.setEmployee(null);
				accountOtherOutMaster.setFundAccount(null);
				accountOtherOutMaster.getDetails().clear();
			}
		}
	}
	/**
	 * 生成单据编号
	 * @return
	 * @throws IOException
	 */
	public String getItem() throws IOException {
		actionJsonResult =new ActionJsonResult((Object)((ItemGenAble)accountOtherOutService).generateItem(accountOtherOutMaster.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData() {
		actionJsonResult=new ActionJsonResult((Object)accountOtherOutService.getById(id).toDisp(true));
		return JSONLIST;
	}
}
