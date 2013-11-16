/*
 * 捷利商业进销存管理系统
 * @(#)FundAccount.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

/**
 * @ClassName:FundAccountAction
 * @Description:Action
 * @author
 * @version 2012-11-14 13:32:04 
 *
 */
@Controller("fundAccountAction")
@Scope("prototype")
public class FundAccountAction extends JQGridAction<FundAccount> {
	private FundAccount fundAccount;
	private IFundAccountService fundAccountService;
	private Pager<FundAccount> pager;
	@Resource(name="fundAccountService")
	public void setFundAccountService(IFundAccountService fundAccountService) {
		this.fundAccountService = fundAccountService;
	}

	public Pager<FundAccount> getPager() {
		return pager;
	}


	
	@Override
	public FundAccount getModel() {
		return fundAccount;
	}

	@Override
	public String list() {
		logger.debug("FundAccount list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		//pager=fundAccountService.findPager(objectQuery);
		return SUCCESS;
	}
	public String listjson(){
		pager=fundAccountService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	public String listall(){
		actionJsonResult=new ActionJsonResult(fundAccountService.getAll());
		return JSONALL;
	}
	
	@Override
	public String input() {
		logger.debug("FundAccount input.....");
		//将需要在页面展示的关联对象放入Context；
		//putContext(key,value);		
		return INPUT;
	}
//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("FundAccount save.....");
		if (StringUtils.isNotBlank(id)) {
			fundAccountService.update(fundAccount);
		}else{
			fundAccountService.save(fundAccount);
		}
		actionJsonResult=new ActionJsonResult(fundAccount);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("FundAccount delete.....");
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			fundAccountService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				fundAccount =new FundAccount();
			}
		}else{
			fundAccount=fundAccountService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
