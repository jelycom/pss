package cn.jely.cd.util.interceptor;

import cn.jely.cd.service.IQuerydataService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class QueryDataInterceptor extends AbstractInterceptor {
	private IQuerydataService querydataService;
	private static final String LOADQUERYDATAMETHOD="list";
	public static final String QUERYAPPMODEL="queryappmodel";
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String methodName=invocation.getProxy().getMethod();
		if(LOADQUERYDATAMETHOD.equals(methodName)){//如果是list方法则会将action名称放入context给前台
			ActionContext.getContext().put(QUERYAPPMODEL, invocation.getAction().getClass().getSimpleName());
		}
		return invocation.invoke();
	}

	public void setQuerydataService(IQuerydataService querydataService) {
		this.querydataService = querydataService;
	}

}
