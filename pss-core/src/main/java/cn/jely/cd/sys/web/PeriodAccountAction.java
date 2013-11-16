/*
 * 捷利商业进销存管理系统
 * @(#)PeriodAccount.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;

import cn.jely.cd.sys.domain.PeriodAccount;
import cn.jely.cd.sys.service.IPeriodAccountService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:PeriodAccountAction
 * @Description:Action
 * @author
 * @version 2013-06-07 22:04:21 
 *
 */
public class PeriodAccountAction extends JQGridAction<PeriodAccount> {//有针对树的操作需继承自TreeOperateAction
	private PeriodAccount periodAccount;
	private IPeriodAccountService periodAccountService;
	private Pager<PeriodAccount> pager;

	public void setPeriodAccountService(IPeriodAccountService periodAccountService) {
		this.periodAccountService = periodAccountService;
	}

	public Pager<PeriodAccount> getPager() {
		return pager;
	}

	@Override
	public PeriodAccount getModel() {
		return periodAccount;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		pager=periodAccountService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(periodAccountService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				periodAccountService.update(periodAccount);
			}else{
				periodAccountService.save(periodAccount);
			}
			actionJsonResult=new ActionJsonResult(periodAccount);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			periodAccountService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				periodAccount =new PeriodAccount();
			}
		}else{
			periodAccount=periodAccountService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
