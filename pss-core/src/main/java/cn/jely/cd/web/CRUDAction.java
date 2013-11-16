/*
 * 捷利商业进销存管理系统
 * @(#)${upperClass}.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.lang.reflect.InvocationTargetException;

import cn.jely.cd.pagemodel.PageModel;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @ClassName:CRUDAction
 * @Description:基础的添删改查Action
 * @author {周义礼}
 * @version 2012-11-9 上午10:43:57
 * @param <T>
 */
public abstract class CRUDAction<T> extends BaseAction implements
		ModelDriven<T>,Preparable {
	private static final long serialVersionUID = 1L;
	protected String id;
//	protected Querydata querydata;
	protected String value;
	/**	根据传入的actionName查询过滤条件 */
	protected String actionName;
	protected String param;
	protected String rigthId;
	/**@Fields pid:父节点主键,如果有*/
	protected String pid;

	@Override
	public void prepare() throws Exception {
//		this.objectQuery=parseCondition();
	}
	
	@Override
	public String execute()  {
		try {
			return list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * @Title:prepareInput
	 * @Description:Input和Save方法调用一个统一的方法.
	 * @return void
	 * @exception
	 * @throws
	 */
	public void prepareInput() {
		beforInputSave();
	}

	/**
	 * @Title:prepareSave
	 * @Description:Save方法和Input方法调用一个统一的方法.
	 * @return void
	 * @exception
	 * @throws
	 */
	public void prepareSave() {
		beforInputSave();
	}

	/**
	 * @Title:beforInputSave
	 * @Description:由子类实现在Input和Save方法中实际的逻辑.
	 * @return void
	 * @exception
	 * @throws
	 */
	protected abstract void beforInputSave();

	/**
	 * @Title:save
	 * @Description:保存方法,使用json就不需要跳转页面.
	 * @return String
	 * @exception
	 * @throws Exception
	 */
	protected abstract String save() ;

	/**
	 * @Title:delete
	 * @Description:删除方法,使用json就不需要跳转页面.
	 * @return String
	 * @exception
	 * @throws Exception
	 */
	protected abstract String delete() ;

	/**
	 * @Title:list
	 * @Description:列表方法,转向列表页面.
	 * @return String
	 * @throws Exception
	 */
	protected abstract String list() ;

	/**
	 * @Title:listjson
	 * @Description:返回json格式的分页列表数据.
	 * @return String
	 * @throws Exception
	 */
	protected abstract String listjson() ;

	/**
	 * @Title:listall
	 * @Description:返回json格式的未分页全部数据.
	 * @return String
	 * @throws Exception
	 */
	protected abstract String listall() ;
	
	/**
	 * 将实体类转换为页面模型用于返回数据显示用,子类覆盖此方法
	 * @return PageModel
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected  PageModel domain2PageModel(T t) throws IllegalAccessException, InvocationTargetException{
		return null;
	}
	@Override
	public String input(){
		return INPUT;
	}
	/**
	 * @Title:isEditSave
	 * @Description:是否是保存.
	 * @return boolean true,是保存方法.</br>false,不是修改,即新增或修改前的方法.
	 * @exception
	 * @throws
	 */
	public boolean isEditSave() {
		String requestURI = getRequest().getRequestURI();
		return requestURI.contains("_save")||requestURI.contains("_getItem");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
//		if ("_empty".equals(id) || id == null||"".equals(id)) {
//			this.id = null;
//		} else {
//			this.id=id;
//		}//此代码无效,因为ModelDriven对应采用prepareParameterStack拦截器组有两次设置parameter
//		第一次在getModel之前,这时可以,但getModel之后相应的Model已经在ValueStack中,这时由parameter
//		拦截器再次设置值时,会将客户端提交的数据再次写到Model里.会将此方法中进行的操作相对于这个Model
//		来说全部重新设置一次.
		this.id=id;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getParam() {
		return param;
	}


	public void setParam(String param) {
		this.param = param;
	}


	public String getRigthId() {
		return rigthId;
	}


	public void setRigthId(String rigthId) {
		this.rigthId = rigthId;
	}

}
