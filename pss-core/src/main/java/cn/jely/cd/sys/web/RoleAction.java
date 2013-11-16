/*
 * 捷利商业进销存管理系统
 * @(#)Role.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.Role;
import cn.jely.cd.sys.service.IRoleService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ArrayConverter;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:RoleAction
 * @Description:Action
 * @author
 * @version 2013-04-05 09:52:39 
 *
 */
public class RoleAction extends JQGridAction<Role> {
	private Role role;
	private IRoleService roleService;
	private Pager<Role> pager;
	private String ids;
	
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public Pager<Role> getPager() {
		return pager;
	}

	@Override
	public Role getModel() {
		return role;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		parseCondition();
		pager=roleService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(roleService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		if(StringUtils.isNotBlank(ids)){
			String[] idsArray=ids.split(",");
			for(Long resid:ArrayConverter.Strings2Longs(idsArray)){
				role.getResources().add(new ActionResource(resid));
			}
		}
		if (StringUtils.isNotBlank(id)) {
			roleService.update(role);
		}else{
			roleService.save(role);
		}
		actionJsonResult=new ActionJsonResult(role);
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			roleService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				role =new Role();
			}
		}else{
			role=roleService.getById(id);
			if (isEditSave()) {
				role.getResources().clear();
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
