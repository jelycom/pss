/*
 * 捷利商业进销存管理系统
 * @(#)Contactor.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IContactsDao;
import cn.jely.cd.domain.Contacts;

/**
 * @ClassName:ContactorDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2012-11-14 14:08:55 
 *
 */
@Repository("contactsDao")
public class ContactsDaoImpl extends BaseDaoImpl<Contacts> implements IContactsDao {

}
