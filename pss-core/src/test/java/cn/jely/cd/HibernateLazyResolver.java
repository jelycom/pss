/*
 * 捷利商业进销存管理系统
 * @(#)HibernateLazyResolver.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-7
 */
package cn.jely.cd;

import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;  
import org.hibernate.FlushMode;  
import org.hibernate.Session;  
import org.hibernate.SessionFactory;  
import org.springframework.beans.factory.InitializingBean;  
import org.springframework.dao.DataAccessResourceFailureException;  
import org.springframework.orm.hibernate3.SessionFactoryUtils;  
import org.springframework.orm.hibernate3.SessionHolder;  
import org.springframework.transaction.support.TransactionSynchronizationManager;  
 
public class HibernateLazyResolver implements InitializingBean {  
    private static Log logger = LogFactory.getLog(HibernateLazyResolver.class);;  
    private boolean singleSession = true;   
    private SessionFactory sessionFactory;  
    boolean participate = false;  
    protected Session session = null;  
         
    public final void setSessionFactory(SessionFactory sessionFactory) {  
        this.sessionFactory = sessionFactory;  
    }  
 
    public void setSingleSession(boolean singleSession) {  
        this.singleSession = singleSession;  
    }  
 
    protected boolean isSingleSession() {  
        return singleSession;  
    }  
      
    public void afterPropertiesSet() throws Exception {  
        if (sessionFactory == null) {  
           throw new IllegalArgumentException("SessionFactory is reqirued!");  
        }  
    }

    public void openSession() {  
        if (isSingleSession()) {  
            // single session mode  
            if (TransactionSynchronizationManager.hasResource(sessionFactory)) {  
                // Do not modify the Session: just set the participate flag.  
                participate = true;  
            }  
            else {  
                logger.debug("Opening single Hibernate Session in HibernateLazyResolver");  
                session = getSession(sessionFactory);  
                TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));  
            }  
        }  
        else {  
            // deferred close mode  
            if (SessionFactoryUtils.isDeferredCloseActive(sessionFactory)) {  
                // Do not modify deferred close: just set the participate flag.  
                participate = true;  
            }else {  
                SessionFactoryUtils.initDeferredClose(sessionFactory);  
            }  
        }      
    }

	/**
	 * @Title:getSession
	 * @param sessionFactory2
	 * @return Session
	 */
	private Session getSession(SessionFactory sessionFactory2) {
		
		return sessionFactory.getCurrentSession();
	}
}

