/*
 * 捷利商业进销存管理系统
 * @(#)AccountTransferMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.AccountTransferMaster;
import cn.jely.cd.service.IAccountTransferService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:AccountTransferMasterAction
 * @Description:Action
 * @author
 * @version 2013-07-03 17:12:13 
 *
 */
public class AccountTransferAction extends JQGridAction<AccountTransferMaster> {//有针对树的操作需继承自TreeOperateAction
	private AccountTransferMaster accountTransferMaster=new AccountTransferMaster();
	private IAccountTransferService accountTransferService;
	private Pager<AccountTransferMaster> pager;

	public void setAccountTransferService(IAccountTransferService accountTransferService) {
		this.accountTransferService = accountTransferService;
	}

	public Pager<AccountTransferMaster> getPager() {
		return pager;
	}

	@Override
	public AccountTransferMaster getModel() {
		return accountTransferMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		pager=accountTransferService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(accountTransferService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@OutputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				accountTransferService.update(accountTransferMaster);
			}else{
				accountTransferService.save(accountTransferMaster);
			}
			actionJsonResult=new ActionJsonResult(accountTransferMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			accountTransferService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				accountTransferMaster =new AccountTransferMaster();
//			}
		}else{
			accountTransferMaster=accountTransferService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				accountTransferMaster.setEmployee(null);
				accountTransferMaster.setFundAccount(null);
				accountTransferMaster.getDetails().clear();
			}
		}
	}
	/**
	 * 生成单据编号
	 * @return
	 * @throws IOException
	 */
	public String getItem() throws IOException {
		actionJsonResult =new ActionJsonResult((Object)((ItemGenAble)accountTransferService).generateItem(accountTransferMaster.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData() {
		actionJsonResult=new ActionJsonResult(accountTransferService.getById(id).toDisp(true));
		return JSONLIST;
	}
}
