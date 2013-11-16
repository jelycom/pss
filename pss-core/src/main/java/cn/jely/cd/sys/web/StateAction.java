/*
 * 捷利商业进销存管理系统
 * @(#)State.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.sys.domain.State;
import cn.jely.cd.sys.service.IStateService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:StateAction
 * @Description:Action
 * @author
 * @version 2013-06-03 09:12:39 
 *
 */
public class StateAction extends JQGridAction<State> {//有针对树的操作需继承自TreeOperateAction
	private State state;
	private IStateService stateService;
	private Pager<State> pager;

	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}

	public Pager<State> getPager() {
		return pager;
	}

	@Override
	public State getModel() {
		return state;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		pager=stateService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(stateService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				stateService.update(state);
			}else{
				stateService.save(state);
			}
			actionJsonResult=new ActionJsonResult(state);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			stateService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				state =new State();
			}
		}else{
			state=stateService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
