/*
 * 捷利商业进销存管理系统
 * @(#)UserListener.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.util;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jely.cd.domain.Employee;
import edu.emory.mathcs.backport.java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName:UserListener
 * @Description:HttpSessionActivationListener 主要用于分布式环境中。</br>
 *                                            session的passivation是指非活动的session被写入持久设备
 *                                            （比如硬盘）。</br> activate自然就是相反的过程
 * @author 周义礼
 * @version 2013-1-9 下午9:57:38
 * 
 */
public class UserHttpSessionListener implements HttpSessionActivationListener,
		HttpSessionBindingListener {
	Logger logger = LoggerFactory.getLogger(getClass());
	public static final String ONLINEUSERS="onlineUsers";
	private Employee user;
	
	public UserHttpSessionListener(Employee user) { //初始化时必须传用用户名
		this.user = user;
	}

	// HttpSessionBindingListener
	public void valueBound(HttpSessionBindingEvent event) {//可以实现只能在一处登录的逻辑，即如果在列表中有此用户，那么将此用户对应的Session或当前Session indicate，实现登录后不能再登或者后登踢出前登录用户。
		// 被设置到session中（setAttribute）
		HttpSession session = event.getSession();
//		Employee employee = (Employee) session.getAttribute(BaseAction.LOGIN_USER);
		String sessionId = session.getId();
		ServletContext servletContext = session.getServletContext();
		List<String> onlineUsers=(List<String>) servletContext.getAttribute(ONLINEUSERS);
		if(onlineUsers==null){//第一次需要初始化并放入ServletContext
			onlineUsers=new CopyOnWriteArrayList();
			servletContext.setAttribute(ONLINEUSERS, onlineUsers);
		}
		if(user!=null){
			onlineUsers.add(user.getName());//把名字放入列表
		}
		logger.info("valueBound(" + sessionId + event.getValue() + ")");
		System.out.println("bond");
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		// 从session中解除（removeAttribute）
		ServletContext servletContext=event.getSession().getServletContext();
		List<String> onlineUsers=(List<String>) servletContext.getAttribute(ONLINEUSERS);
		onlineUsers.remove(this.user.getName());
		logger.info("valueUnbound(" + event.getSession().getId()
				+ event.getValue() + ")");
	}

	// HttpSessionActivationListener
	public void sessionDidActivate(HttpSessionEvent event) {
		logger.info("sessionDidActivate(" + event.getSession().getId() + ")"); // 激活
	}

	public void sessionWillPassivate(HttpSessionEvent event) {
		// 被传送到别的jvm或 写到硬盘
		logger.info("sessionWillPassivate(" + event.getSession().getId() + ")");
	}

}
