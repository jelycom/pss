/*
 * 捷利商业进销存管理系统
 * @(#)ReportInterceptor.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-21
 */
package cn.jely.cd.util.interceptor;

import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.web.JQGridAction;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 将查询条件存入Session以便报表可以使用。
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-21 上午10:57:56
 */
public class ObjectQueryInterceptor extends AbstractInterceptor {

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Action action = (Action) invocation.getAction();
		String methodName = invocation.getProxy().getMethod();
		if(action instanceof JQGridAction<?> && "listjson".equalsIgnoreCase(methodName)){
			JQGridAction<?> jqAction = (JQGridAction<?>) action;
			ObjectQuery objectQuery = jqAction.getObjectQuery();
//			objectQuery.setPageNo(1);
//			objectQuery.setPageSize(Integer.MAX_VALUE);
			invocation.getInvocationContext().getSession().put(jqAction.getActionName(), objectQuery);
		}
		return null;
	}

}
