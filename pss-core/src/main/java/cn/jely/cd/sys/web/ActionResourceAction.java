/*
 * 捷利商业进销存管理系统
 * @(#)ActionResource.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.service.IBaseTreeService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.TreeOperateAction;

import com.opensymphony.xwork2.ActionContext;

/**
 * @ClassName:ActionResourceAction
 * @Description:Action
 * @author
 * @version 2013-04-04 22:57:05
 * 
 */
public class ActionResourceAction extends TreeOperateAction<ActionResource> {
	/** long:serialVersionUID: */
	private static final long serialVersionUID = 1L;
	private ActionResource actionResource;
	private IActionResourceService actionResourceService;
	private IEmployeeService employeeService;
	private Pager<ActionResource> pager;

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}

	public Pager<ActionResource> getPager() {
		return pager;
	}

	@Override
	public ActionResource getModel() {
		return actionResource;
	}

	@Override
	public String list() {
		logger.debug("ActionResource list.....");
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		return SUCCESS;
	}

	@Override
	public String listjson() {
		pager = actionResourceService.findPager(objectQuery);
		actionJsonResult = new ActionJsonResult(pager);
		return JSONALL;
	}

	public String treejson() {
		List<ActionResource> rows = actionResourceService.findTreeNodes(null, true);
		int size = rows.size();
		pager = new Pager<ActionResource>(1, size, size, 1, rows);
		actionJsonResult = new ActionJsonResult(pager);
		return JSONLIST;
	}

	@Override
	public String listall() {
		actionJsonResult = new ActionJsonResult(actionResourceService.getAll());
		return JSONALL;
	}

//	public String listbyuser() {
//		Employee loginUser = (Employee) getSession().get(LOGIN_USER);
//		List<ActionResource> actionResources = null;
//		if (loginUser != null) {
//			SystemSetting setting = (SystemSetting) ActionContext.getContext().getApplication()
//					.get(ConstValue.SYS_SETTING);
//			if (setting != null) {
//				Boolean opened = setting.getCompanyInfo().getOpened();
//				if (opened) {
//					actionResources = actionResourceService.findControlActionResourceLinkAddresses();
//				}
//			} else {
//				actionResources = actionResourceService.findInitActionResources();
//				
//			}
//			// actionResourceService.findall
//		}
//		return JSONLIST;
//	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("ActionResource save.....");
		if (StringUtils.isNotBlank(id)) {
			actionResourceService.update(actionResource, Long.valueOf(pid));
		} else {
			try {
				if (StringUtils.isNotBlank(pid)) {
					actionResourceService.save(actionResource, Long.valueOf(pid));
				} else {
					actionResourceService.save(actionResource, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		actionJsonResult = new ActionJsonResult((Object) actionResource);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("ActionResource delete.....");
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			Long lid = Long.valueOf(id);
			actionResourceService.deleteCascade(lid);
		}
		actionJsonResult = new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				// 新增的时候不需要初始化，保存的时候要有个对象保存值
				actionResource = new ActionResource();
			}
		} else {
			actionResource = actionResourceService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	@Override
	protected IBaseTreeService<ActionResource> getTreeService() {
		return actionResourceService;
	}

}
