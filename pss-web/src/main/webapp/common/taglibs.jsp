<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- <%@ taglib prefix="c" uri="/jstl" %> --%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%-- 
 <s:set var="ctx" value="%{CONTEXT_PATH}"></s:set> 
 <s:set var="ctx" value="%{ServletActionContext.getServletContext().getRealPath('/')};"></s:set> --%> 