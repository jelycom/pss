<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<div style="padding-left: 10px;">
<s:if test="hasFieldErrors()">
  <div style="color: red"><s:fielderror/></div>
</s:if>
<s:elseif test="hasActionErrors()">
  <div style="color: red"><s:actionerror/></div>
</s:elseif>
<s:elseif test="hasActionMessages()">
  <div style="color: blue"><s:actionmessage/></div>
</s:elseif>

</div>