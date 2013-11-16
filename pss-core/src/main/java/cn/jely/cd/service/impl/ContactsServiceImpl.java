/*
 * 捷利商业进销存管理系统
 * @(#)Contactor.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IContactsDao;
import cn.jely.cd.domain.Contacts;
import cn.jely.cd.service.IContactsService;

/**
 * @ClassName:ContactorServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2012-11-14 14:08:55
 * 
 */
@Service("contactsService")
public class ContactsServiceImpl extends BaseServiceImpl<Contacts> implements IContactsService {

	private IContactsDao contactsDao;

	@Resource(name = "contactsDao")
	public void setContactsDao(IContactsDao contactsDao) {
		this.contactsDao = contactsDao;
	}

	@Override
	public IBaseDao<Contacts> getBaseDao() {
		return contactsDao;
	}

	@Override
	public List<Contacts> find(String values) {
		String hql = "from Contacts c where c.name like ? or c.py like ?";
		return super.findQuery(hql, null, new Object[] { "%" + values + "%", "%" + values + "%" });
	}

}
