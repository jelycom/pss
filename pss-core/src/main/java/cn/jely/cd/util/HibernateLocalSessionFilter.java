/*
 * 捷利商业进销存管理系统
 * @(#)HibernateLocalSessionFilter.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-7
 */
package cn.jely.cd.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @ClassName:HibernateLocalSessionFilter
 * @author 周义礼
 * @version 2013-4-7 下午5:30:38
 * 
 */
public class HibernateLocalSessionFilter {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void getHSession(ProceedingJoinPoint pjp) {
		System.out.println("-----------HibernateLocalSessionFilter----------");
//		Session session = sessionFactory.getCurrentSession();
		try {
			pjp.proceed();
			System.out.println(pjp.getTarget().getClass().getName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
//		session.close();
		System.out.println("--------close Session----------");

	}
}
