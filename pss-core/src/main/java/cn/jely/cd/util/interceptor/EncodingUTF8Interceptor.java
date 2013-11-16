/*
 * 捷利商业进销存管理系统
 * @(#)EncodingUTF8Interceptor.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-9
 */
package cn.jely.cd.util.interceptor;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 有乱码时才需要此拦截器，默认可不用。
 * @ClassName:EncodingUTF8Interceptor
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-9 上午8:59:17
 *
 */
public class EncodingUTF8Interceptor implements Interceptor {

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {

	}

	@Override
	public void init() {

	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String encoding="UTF-8";
		ServletActionContext.getRequest().setCharacterEncoding(encoding);
		ServletActionContext.getResponse().setCharacterEncoding(encoding);
		return invocation.invoke();
	}

}
