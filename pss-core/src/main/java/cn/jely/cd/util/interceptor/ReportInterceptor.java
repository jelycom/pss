/*
 * 捷利商业进销存管理系统
 * @(#)ReportInterceptor.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-22
 */
package cn.jely.cd.util.interceptor;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.repo.JasperDesignReportResourceCache;
import net.sf.jasperreports.web.WebReportContext;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-22 下午1:56:02
 */
public class ReportInterceptor extends AbstractInterceptor {
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	JasperDesignReportResourceCache cache = JasperDesignReportResourceCache.getInstance(WebReportContext.getInstance(ServletActionContext.getRequest()));
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		return null;
	}

}
