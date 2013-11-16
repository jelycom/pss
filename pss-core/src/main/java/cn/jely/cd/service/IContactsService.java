/*
 * 捷利商业进销存管理系统
 * @(#)Contactor.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.util.List;

import cn.jely.cd.domain.Contacts;

/**
 * @ClassName:ContactorService
 * @Description:Service
 * @author
 * @version 2012-11-14 14:08:55 
 *
 */
public interface IContactsService extends IBaseService<Contacts> {
	List<Contacts> find(String values);
}
