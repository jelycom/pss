package cn.jely.cd.web;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.sys.domain.User;
import cn.jely.cd.sys.service.IUserService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ActionJsonResult.MessageType;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.UserHttpSessionListener;
import cn.jely.cd.util.VCodeGenerator;

/**
 * @ClassName:LoginAction
 * @Description:处理登录的Action
 * @author {周义礼}
 * @date 2012-11-9 上午9:47:12
 * 
 */
@Controller("loginAction")
@Scope("prototype")
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	/** @Fields name:登录用户名 */
	private String name;
	/** @Fields password:登录密码 */
	private String password;
	/** String:vcode:验证码 */
	private String verifyCode;
	/** @Fields employeeService:用户管理Service */
	private IEmployeeService employeeService;
	private IUserService userService;
	
	ActionJsonResult actionJsonResult;

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public ActionJsonResult getActionJsonResult() {
		return actionJsonResult;
	}

	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@Resource(name = "userService")
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public String execute() throws Exception {
		return LOGIN;
	}

	public String login() throws Exception {
		Map<String, Object> session = getSession();
		String validatecode = (String) session.get(ConstValue.VALIDATECODE);
		if (StringUtils.isNotBlank(validatecode) && validatecode.equalsIgnoreCase(verifyCode)) {// 验证码不区分大小写
			Employee loginUser = employeeService.findEmployee(name, password);
			if (loginUser != null) {
				// writejson(new ActionJsonResult(true, "success", loginUser));
				session.put("bindingListener", new UserHttpSessionListener(loginUser));// UserHttpSessionListener
				session.put(LOGIN_USER, loginUser);
				String skin = loginUser.getUser().getSkin();
				if(StringUtils.isNotBlank(skin)){
					session.put(ConstValue.USERSKIN, skin);
				} else if(session.containsKey(ConstValue.USERSKIN)){
					session.remove(ConstValue.USERSKIN);
				}
				session.remove(ConstValue.VALIDATECODE);
				// ServletActionContext.getRequest().getSession().setAttribute("bindingListener",new
				// UserHttpSessionListener(loginUser));
				return MAIN;
			} else {
				addFieldError("name", "用户名或密码错误!");
			}
		} else {
			addFieldError("verifyCode", "验证码错误！");
		}
		// return MAIN;
		return LOGIN;
	}

	public String getvcode() {
		HttpServletResponse response = getResponse();
		String contentType = response.getContentType();
		response.setContentType("image/gif");
		// response.setHeader("Cache-Control", "no-cache");
		ServletOutputStream os;
		try {
			os = response.getOutputStream();
			VCodeGenerator vcg = new VCodeGenerator(os);
			String vcode = vcg.drawCode();
			getSession().put(ConstValue.VALIDATECODE, vcode);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
			writejson(new ActionJsonResult(false, e.getMessage()));
		}
		response.setContentType(contentType);// 还原原有类型设置。
		return null;
	}
}
