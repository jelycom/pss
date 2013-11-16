/*
 * 捷利商业进销存管理系统
 * @(#)${upperClass}.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.util.ActionJsonResult;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @ClassName:BaseAction
 * @Description:Action的基类
 * @author {周义礼}
 * @version 2012-11-9 上午11:53:35
 *
 */
public class BaseAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	public static final String LOGIN_USER = "loginUser";
	public static final String MAIN = "main";
	public static final String RELOAD = "reload";
	public static final String JSONLIST = "jsonList";
	public static final String JSONALL=	"jsonAll";
	public static final String JSONTREE = "jsonTree";
	public static final String SHOW = "show";
//	public static final String JSONINPUT="jsoninput";
	public static final String JSONfUZZY="jsonFuzzy";
	public static final String CONTEXT_PATH="contextpath";
	
	/**@Fields result:统一封装的json操作结果*/
	protected ActionJsonResult actionJsonResult;
	/**
	 * <p>Title:默认构造</p>
	 * <p>Description:添加项目的路径到Context中</p>
	 */
	public BaseAction(){
		putContext(CONTEXT_PATH, ServletActionContext.getServletContext().getContextPath());
		//ContextPath是指服务器本地的项目地址
		logger.debug(CONTEXT_PATH);
	}
	/**
	 * @Title:getRequest
	 * @Description:取得HttpServletRequest,给子类使用
	 * @return HttpServletRequest
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * @Title:getResponse
	 * @Description:取得HttpServletResponse,给子类作输出时使用
	 * @return HttpServletResponse
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * @Title:getSession
	 * @Description:取得Map格式的Session数据
	 * @return Map<String,Object>
	 */
	protected Map<String, Object> getSession() {
		return ActionContext.getContext().getSession();
	}

	protected Map<String, Object> getApplication() {
		return ActionContext.getContext().getApplication();
	}
	/**
	 * @Title:putContext
	 * @Description:将value以key放入ActionContext
	 * @param key Map的Key
	 * @param value Map的value
	 * @return void
	 */
	protected void putContext(String key, Object value) {
		ActionContext.getContext().put(key, value);
	}

	/**
	 * @Title:getLoginUser
	 * @Description:取得登录系统的用户
	 * @return Employee 登录的用户
	 */
	public Employee getLoginUser() {
		return (Employee) getSession().get(LOGIN_USER);
	}
	
	public void writeJson(Object object, Collection<Pattern> excludeProperties,
			Collection<Pattern> includeProperties, boolean excludeNullProperties,boolean ignoreHierarchy){
		getResponse().setContentType("application/json;charset=UTF-8");
		try {
			JSONWriter writer=new JSONWriter();
			//如果没有指定返回的格式,则使用此格式返回
			writer.setFormatter(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			writer.setIgnoreHierarchy(ignoreHierarchy);
			writer.setEnumAsBean(true);
			String json=writer.write(object, excludeProperties, includeProperties, excludeNullProperties);
//			String json=JSONUtil.serialize(object,null,null,false,true);
//			此方法返回的Date如果没有用注解@json指定格式则RFC3339_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"如果采用此格式则可不用JSONWriter类
			getResponse().getWriter().write(json);
			getResponse().getWriter().flush();
			getResponse().getWriter().close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeJson(Object object, Collection<Pattern> excludeProperties,
            Collection<Pattern> includeProperties, boolean excludeNullProperties){
		writeJson(object, excludeProperties, includeProperties, excludeNullProperties, false);
	}
	
	public void writejson(Object object){
		if(object instanceof ActionJsonResult){
			writeJson(object, null, null, true);
		}else{
			actionJsonResult = new ActionJsonResult(object);
			writeJson(actionJsonResult, null, null, true);
		}
	}
	public ActionJsonResult getActionJsonResult() {
		return actionJsonResult;
	}
	
	
}
