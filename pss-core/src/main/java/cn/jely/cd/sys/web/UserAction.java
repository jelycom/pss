/*
 * 捷利商业进销存管理系统
 * @(#)User.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.sys.domain.User;
import cn.jely.cd.sys.service.IUserService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ActionJsonResult.MessageType;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:UserAction
 * @Description:Action
 * @author
 * @version 2013-10-08 21:14:26
 * 
 */
public class UserAction extends JQGridAction<User> {// 有针对树的操作需继承自TreeOperateAction
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private User user;
	private IUserService userService;
	private Pager<User> pager;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public Pager<User> getPager() {
		return pager;
	}

	@Override
	public User getModel() {
		return user;
	}

	@Override
	public String list() {
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		return SUCCESS;
	}

	@Override
	public String listjson() {
		parseCondition();
		pager = userService.findPager(objectQuery);
		actionJsonResult = new ActionJsonResult(pager);
		return JSONALL;
	}

	@Override
	public String listall() {
		actionJsonResult = new ActionJsonResult(userService.getAll());
		return JSONALL;
	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	@Override
	public String save() {
		try {
			if (StringUtils.isNotBlank(id)) {
				userService.update(user);
			} else {
				userService.save(user);
			}
			actionJsonResult = new ActionJsonResult(user);
		} catch (Exception e) {
			actionJsonResult = new ActionJsonResult(false,e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			userService.delete(id);
//			userService.setDeleteState(id);//置为删除状态。不是真正删除
		}
		actionJsonResult = new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				// 新增的时候不需要初始化，保存的时候要有个对象保存值
				user = new User();
			}
		} else {
			user = userService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	public String changepwd() {
		HttpServletRequest request = getRequest();
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		if (StringUtils.isBlank(oldPassword)) {
			actionJsonResult = new ActionJsonResult(false, MessageType.ERROR, "原密码不能为空");
		} else if (StringUtils.isBlank(newPassword) || StringUtils.isBlank(confirmPassword)) {
			actionJsonResult = new ActionJsonResult(false, MessageType.ERROR, "新密码和确认密码不能为空");
		} else if (!newPassword.equals(confirmPassword)) {
			actionJsonResult = new ActionJsonResult(false, MessageType.ERROR, "新密码、确认密码不一致");
		} else {
			Employee employee = getLoginUser();
			user = employee.getUser();
			if (user != null && oldPassword.equals(user.getPassword())) {
				user.setPassword(newPassword);
				userService.update(user);
				actionJsonResult = new ActionJsonResult(true, "修改密码成功");
			} else {
				actionJsonResult = new ActionJsonResult(MessageType.ERROR, "原密码不正确");
			}
		}
		return JSONLIST;
	}

	public String changeskin() {
		HttpServletRequest request = getRequest();
		String skin = request.getParameter(ConstValue.USERSKIN);
		if(StringUtils.isNotBlank(skin)){
			user = getLoginUser().getUser();
			if(user != null){
				user = userService.getById(user.getId());
				user.setSkin(skin);
				userService.update(user);
				getSession().put(ConstValue.USERSKIN, skin);
				actionJsonResult = new ActionJsonResult(true,"");
			}else{
				actionJsonResult = new ActionJsonResult(false,"此员工无登陆帐户");
			}
		}else {
			actionJsonResult = new ActionJsonResult(false,MessageType.ERROR,"skin不能为空");
		}
		return JSONLIST;
	}
}
