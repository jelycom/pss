/*
 * 捷利商业进销存管理系统
 * @(#)BusinessType.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;

import cn.jely.cd.sys.domain.BusinessType;
import cn.jely.cd.sys.service.IBusinessTypeService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:BusinessTypeAction
 * @Description:Action
 * @author
 * @version 2013-06-18 13:25:35 
 *
 */
public class BusinessTypeAction extends JQGridAction<BusinessType> {//有针对树的操作需继承自TreeOperateAction
	private BusinessType businessType;
	private IBusinessTypeService businessTypeService;
	private Pager<BusinessType> pager;

	public void setBusinessTypeService(IBusinessTypeService businessTypeService) {
		this.businessTypeService = businessTypeService;
	}

	public Pager<BusinessType> getPager() {
		return pager;
	}

	@Override
	public BusinessType getModel() {
		return businessType;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		pager=businessTypeService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(businessTypeService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				businessTypeService.update(businessType);
			}else{
				businessTypeService.save(businessType);
			}
			actionJsonResult=new ActionJsonResult(businessType);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			businessTypeService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				businessType =new BusinessType();
			}
		}else{
			businessType=businessTypeService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
