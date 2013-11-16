package cn.jely.cd.web;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("logoutAction")
@Scope("prototype")
public class LogoutAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
//		getSession().put(LOGIN_USER, null);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.removeAttribute(LOGIN_USER);
		session.invalidate();
		return LOGIN;
	}

	
}
