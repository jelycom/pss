/*
 * 捷利商业进销存管理系统
 * @(#)Menu.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;

import cn.jely.cd.sys.domain.Menu;
import cn.jely.cd.sys.service.IMenuService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:MenuAction
 * @Description:Action
 * @author
 * @version 2013-04-08 10:14:43 
 *
 */
public class MenuAction extends JQGridAction<Menu> {//有针对树的操作需继承自TreeOperateAction
	private Menu menu;
	private IMenuService menuService;
	private Pager<Menu> pager;

	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}

	public Pager<Menu> getPager() {
		return pager;
	}

	@Override
	public Menu getModel() {
		return menu;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson(){
		pager=menuService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall(){
		actionJsonResult=new ActionJsonResult(menuService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		if (id!=null) {
			menuService.update(menu);
		}else{
			menuService.save(menu);
		}
		actionJsonResult=new ActionJsonResult(menu);
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (id!=null) {
			menuService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (id==null) {
			if (!isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				menu =new Menu();
			}
		}else{
			menu=menuService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
