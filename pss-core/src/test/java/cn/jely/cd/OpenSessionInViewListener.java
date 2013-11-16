/*
 * 捷利商业进销存管理系统
 * @(#)OpenSessionInViewListener.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-8
 */
package cn.jely.cd;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

class OpenSessionInViewListener extends AbstractTestExecutionListener{

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		SessionFactory sessionFactory=testContext.getApplicationContext().getBean(SessionFactory.class);
		Session session=SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.MANUAL);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}
	
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		SessionFactory sessionFactory=testContext.getApplicationContext().getBean(SessionFactory.class);
		SessionHolder sessionHolder=(SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(sessionHolder.getSession());
	}
	
}
