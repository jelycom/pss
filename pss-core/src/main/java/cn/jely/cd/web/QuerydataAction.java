/*
 * 捷利商业进销存管理系统
 * @(#)Querydata.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.Querydata;
import cn.jely.cd.service.IQuerydataService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

/**
 * @ClassName:QuerydataAction
 * @Description: 该anction作用用于处理过滤器
 * @author
 * @version 2013-03-01 15:09:02 
 *
 */
@Controller("querydataAction")
@Scope("prototype")
public class QuerydataAction extends JQGridAction<Querydata> {
	
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private Querydata querydata;
	private IQuerydataService querydataService;
	private Pager<Querydata> pager;
	
	@Resource(name="querydataService")
	public void setQuerydataService(IQuerydataService querydataService) {
		this.querydataService = querydataService;
	}

	public Pager<Querydata> getPager() {
		return pager;
	}

	
	@Override
	public Querydata getModel() {
		return querydata;
	}

	@Override
	public String list() {
		logger.debug("Querydata list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		//pager=querydataService.findPager(objectQuery);
		return SUCCESS;
	}
	public String listjson(){
		pager=querydataService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	public String listall(){
		actionJsonResult=new ActionJsonResult(querydataService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("Querydata save.....");
		if (StringUtils.isNotBlank(id)) {
			querydataService.update(querydata);
		}else{
			try {
				querydataService.save(querydata);
			} catch (Exception e) {
				actionJsonResult=new ActionJsonResult(e.getMessage());
				return JSONLIST;
			}
			
		}
		actionJsonResult=new ActionJsonResult(querydata);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("Querydata delete.....");
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			querydataService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				querydata =new Querydata();
			}
		}else{
			querydata=querydataService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	/**
	 * 用于加载过滤器
	 * @return
	 */
	public String loadFilter() {
		List<Querydata> querydatas = querydataService.loadFilter(actionName);
		actionJsonResult =new ActionJsonResult(querydatas);
		return JSONLIST;
	}
}
