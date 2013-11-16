/*
 * 捷利商业进销存管理系统
 * @(#)TimerJob.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.sys.domain.TimerJob;
import cn.jely.cd.sys.service.ITimerJobService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * @ClassName:TimerJobAction
 * @author
 * @version 2013-01-13 15:24:29 
 *
 */
@Controller("timerJobAction")
@Scope("prototype")
public class TimerJobAction extends JQGridAction<TimerJob> {
	private TimerJob timerJob;
	private ITimerJobService timerJobService;
	private Pager<TimerJob> pager;
	@Resource(name="timerJobService")
	public void setTimerJobService(ITimerJobService timerJobService) {
		this.timerJobService = timerJobService;
	}

	public Pager<TimerJob> getPager() {
		return pager;
	}


	
	@Override
	public TimerJob getModel() {
		return timerJob;
	}

	@Override
	public String list() {
		logger.debug("TimerJob list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		//pager=timerJobService.findPager(objectQuery);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String listjson(){
		pager=timerJobService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}
	
	public String listall(){
		actionJsonResult=new ActionJsonResult(timerJobService.getAll());
		return JSONALL;
	}
	
	
	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("TimerJob save.....");
		if (id!=null) {
			timerJobService.update(timerJob);
		}else{
			timerJobService.save(timerJob);
		}
		return NONE;
	}

	@Override
	public String delete() {
		logger.debug("TimerJob delete.....");
		//id对应的记录不存在已经在Dao作了处理
		if (id!=null) {
			timerJobService.delete(id);
		}
		return NONE;
	}

	@Override
	protected void beforInputSave() {
		if (id==null) {
			if (!isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				timerJob =new TimerJob();
			}
		}else{
			timerJob=timerJobService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
