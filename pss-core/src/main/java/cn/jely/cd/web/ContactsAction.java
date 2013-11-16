/*
 * 捷利商业进销存管理系统
 * @(#)contacts.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

import cn.jely.cd.domain.Contacts;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.service.IContactsService;
import cn.jely.cd.util.Pager;

/**
 * @ClassName:ContactorAction
 * @Description:Action
 * @author
 * @version 2012-11-14 14:08:55
 * 
 */
@Controller("contactsAction")
@Scope("prototype")
public class ContactsAction extends JQGridAction<Contacts> {
	private Contacts contacts;
	private IContactsService contactsService;
	private Pager<Contacts> pager;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	@Resource(name = "contactsService")
	public void setContactsService(IContactsService contactsService) {
		this.contactsService = contactsService;
	}

	public Pager<Contacts> getPager() {
		return pager;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	@Override
	public Contacts getModel() {
		return contacts;
	}

	@Override
	public String list() {
		logger.debug("contacts list.....");
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		// pager=contactorService.findPager(objectQuery);
		return SUCCESS;
	}

	public String listjson() {
		pager = contactsService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}

	public String listall() {
		actionJsonResult = new ActionJsonResult(contactsService.getAll());
		return JSONALL;
	}


//	@Validations(requiredStrings = { @RequiredStringValidator(fieldName = "name", message = "名称必须输入") })
	@InputConfig(methodName = "list")
	@Override
	public String save() {
		logger.debug("contacts save.....");
		if (StringUtils.isNotBlank(id)) {
			contactsService.update(contacts);
		} else {
			contactsService.save(contacts);
		}
		actionJsonResult=new ActionJsonResult(contacts);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("contacts delete.....");
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			contactsService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				// 新增的时候不需要初始化，保存的时候要有个对象保存值
				contacts = new Contacts();
			}
		} else {
			contacts = contactsService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				contacts.setBusinessUnit(null);
			}
		}
	}
}
