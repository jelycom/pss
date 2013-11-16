package cn.jely.cd.util.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ActionJsonResult.MessageType;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.web.BaseAction;
import cn.jely.cd.web.LoginAction;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SecurityInterceptor extends AbstractInterceptor {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private IActionResourceService actionResourceService;
	private IEmployeeService employeeService;
	private List<String> allResources;
	private Map<Long, Set<String>> userResourceCache = new HashMap<Long, Set<String>>();
	private HttpServletResponse response;
	public SecurityInterceptor() {
		// 验证是否为单例 
		logger.info("--------init SecurityInterceptor------");
	}

	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Resource(name = "actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Action action = (Action) invocation.getAction();
		String actionName = action.getClass().getName();
		String methodName = invocation.getProxy().getMethod();
		String fullName = actionName + "." + methodName;
		// System.out.println("employeeSErvice:"+employeeService);
		logger.info("SecurityInterceptor:" + fullName);
		if (actionName == null) {
			return null;
		}
		Employee employee = (Employee) invocation.getInvocationContext().getSession().get(BaseAction.LOGIN_USER);
		if (action instanceof LoginAction) {
			if(employee != null){
				return "main";
			}
			return invocation.invoke();
		} else {
			if (allResources == null || allResources.size() <= 0) {
				allResources = actionResourceService.findControlActionResourceLinkAddresses();// 不管是否开帐状态，都需要对该检查的地址进行检查
				// SystemSetting setting = (SystemSetting)
				// ActionContext.getContext().getApplication().get(ConstValue.SYS_SETTING);
				// Boolean opened = setting.getCompanyInfo().getOpened();
				// if (setting != null && opened) {
				// }else{
				// allResources =
				// actionResourceService.findInitActionResourceLinkAddresses();
				// }
				// System.out.println(allResources.toString());
			}
			if (allResources.contains(fullName)) {
				System.out.println("有资源控制要求。。。");
				if (employee != null) {
					Map<String, Object> application = invocation.getInvocationContext().getApplication();
					Set<String> userResources = getUserResources(employee, application);
					if (!userResources.contains(fullName)) {
						// ActionJsonResult actionJsonResult = new
						// ActionJsonResult(false, "没有相应的权限");
						return Action.ERROR;
					}
				} else {
//					response = ServletActionContext.getResponse();
//					String logscript= "<script>alert('本次会话结束,请重新登录!');window.top.location='"+ServletActionContext.getRequest().getContextPath()+"/index.jsp';</script>";
//					response.getWriter().write(logscript);
//					response.getWriter().close();
//					没有登录用户，则需要返回重新登录，为适合多种客户端，需要将结果包装为JSON返回。让客户端决定返回页面。
//					return Action.LOGIN;
					ActionJsonResult actionJsonResult = new ActionJsonResult(false,MessageType.INTERRUPT,"");
					new BaseAction().writejson(actionJsonResult);
					return BaseAction.JSONLIST;
				}
			} else {
				System.out.println("没有资源控制要求。。。");
			}
			// }

			return invocation.invoke();
		}

	}

	/**
	 * 返回用户所拥有的资源权限
	 * 
	 * @Title:getUserResources
	 * @Description:TODO:用户退出时需要remove掉原有的权限,否则登录仍然是原来的权限
	 * @param employee
	 * @return Set<String>
	 */
	private Set<String> getUserResources(Employee employee, Map<String, Object> application) {
			SystemSetting setting = (SystemSetting) application.get(ConstValue.SYS_SETTING);
			if (setting != null && setting.getCompanyInfo() != null) {
				Boolean isOpen = setting.getCompanyInfo().getOpened();
				Set<String> userResources = employeeService.findUserResourceAddresses(employee.getId(), !isOpen);
				return userResources;
			}else{
				return null;
			}
//		if (userResourceCache.get(employee.getId()) != null) {
//			return userResourceCache.get(employee.getId());
//		} else {
//			SystemSetting setting = (SystemSetting) application.get(ConstValue.SYS_SETTING);
//			if (setting != null && setting.getCompanyInfo() != null) {
//				Boolean isOpen = setting.getCompanyInfo().getOpened();
//				Set<String> userResources = employeeService.findUserResourceAddresses(employee.getId(), !isOpen);
//				userResourceCache.put(employee.getId(), userResources);
//				return userResources;
//			}else{//如果没有系统信息，则用户没有任何访问权限。
//				return null;
//			}
//		}
	}

}
