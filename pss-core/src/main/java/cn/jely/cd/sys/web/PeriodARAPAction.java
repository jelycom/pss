/*
 * 捷利商业进销存管理系统
 * @(#)PeriodARAP.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;

import cn.jely.cd.sys.domain.PeriodARAP;
import cn.jely.cd.sys.service.IPeriodARAPService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:PeriodARAPAction
 * @Description:Action
 * @author
 * @version 2013-06-08 15:45:12 
 *
 */
public class PeriodARAPAction extends JQGridAction<PeriodARAP> {//有针对树的操作需继承自TreeOperateAction
	private PeriodARAP periodARAP;
	private IPeriodARAPService periodARAPService;
	private Pager<PeriodARAP> pager;

	public void setPeriodARAPService(IPeriodARAPService periodARAPService) {
		this.periodARAPService = periodARAPService;
	}

	public Pager<PeriodARAP> getPager() {
		return pager;
	}

	@Override
	public PeriodARAP getModel() {
		return periodARAP;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		pager=periodARAPService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(periodARAPService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				periodARAPService.update(periodARAP);
			}else{
				periodARAPService.save(periodARAP);
			}
			actionJsonResult=new ActionJsonResult(periodARAP);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			periodARAPService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				periodARAP =new PeriodARAP();
			}
		}else{
			periodARAP=periodARAPService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
