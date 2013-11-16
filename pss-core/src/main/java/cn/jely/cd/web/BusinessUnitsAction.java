/*
 * 捷利商业进销存管理系统
 * @(#)BusinessUnits.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;

/**
 * @ClassName:BusinessUnitsAction
 * @Description:Action
 * @author
 * @version 2012-11-14 14:35:42 
 *
 */
@Controller("businessUnitsAction")
@Scope("prototype")
public class BusinessUnitsAction extends JQGridAction<BusinessUnits> {
	private BusinessUnits businessUnits;
	private IBusinessUnitsService businessUnitsService;
	private Pager<BusinessUnits> pager;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	@Resource(name="businessUnitsService")
	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}

	public Pager<BusinessUnits> getPager() {
		return pager;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}
	
	@Override
	public BusinessUnits getModel() {
		return businessUnits;
	}

	@Override
	public String list() {
		logger.debug("BusinessUnits list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		//pager=businessUnitsService.findPager(objectQuery);
		return SUCCESS;
	}
	public String listjson(){
		pager=businessUnitsService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}
	
	public String listall(){
		actionJsonResult=new ActionJsonResult(businessUnitsService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				businessUnitsService.update(businessUnits);
			}else{
				businessUnitsService.save(businessUnits);
			}
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
			return JSONLIST;
		}
		actionJsonResult=new ActionJsonResult(businessUnits);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("BusinessUnits delete.....");
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			businessUnitsService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				businessUnits =new BusinessUnits();
			}
		}else{
			businessUnits=businessUnitsService.getById(id);
			if (isEditSave()) {
				businessUnits.setRegion(null);
				businessUnits.setCustomerType(null);
				businessUnits.setCustomerLevel(null);
				businessUnits.setSupplierLevel(null);
				businessUnits.setSupplierType(null);
				businessUnits.getContactors().clear();
//				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
//				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
