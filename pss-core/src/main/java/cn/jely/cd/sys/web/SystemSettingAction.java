/*
 * 捷利商业进销存管理系统
 * @(#)CompanyInfo.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.service.ISystemSettingService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.SysConfigEnvActionHelper;
import cn.jely.cd.web.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @ClassName:CompanyInfoAction
 * @Description:Action
 * @author
 * @version 2013-04-15 11:23:38
 * 
 */
public class SystemSettingAction extends BaseAction implements ModelDriven<SystemSetting>, Preparable {/**long:serialVersionUID:*/
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 6532380326725730368L;
// 有针对树的操作需继承自TreeOperateAction
	private SystemSetting systemSetting;
	private ISystemSettingService systemSettingService;
	private SysConfigEnvActionHelper sysConfigEnvActionHelper;

	public void setSystemSettingService(ISystemSettingService systemSettingService) {
		this.systemSettingService = systemSettingService;
	}

	public void setSysConfigEnvActionHelper(SysConfigEnvActionHelper sysConfigEnvActionHelper) {
		this.sysConfigEnvActionHelper = sysConfigEnvActionHelper;
	}

	@Override
	public SystemSetting getModel() {
		return systemSetting;
	}

	@Override
	public String execute() {
		System.out.println("execute.........");
		Object object = ActionContext.getContext().getApplication().get("SystemSetting");
		logger.debug("common setting:"+object);
//		putContext("costMethod", CostMethod.values());
		return SUCCESS;
	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	public String save() {
		System.out.println("save.......");
		try {
			systemSettingService.save(systemSetting);
			//重新加载配置到ServletContext中
			sysConfigEnvActionHelper.reload(getRequest().getSession().getServletContext());
			actionJsonResult = new ActionJsonResult(systemSetting);
		} catch (Exception e) {
			actionJsonResult = new ActionJsonResult(false, e.getMessage());
		}
		return JSONLIST;
	}

	public void prepareSave() throws Exception{
		System.out.println("prepareSave......");
		systemSetting=systemSettingService.getSetting();
	}

	public void prepareExecute() throws Exception{
		System.out.println("prepareExecute.....");
		systemSetting = systemSettingService.getSetting();
	}

	@Override
	public void prepare() throws Exception {
	}

}
