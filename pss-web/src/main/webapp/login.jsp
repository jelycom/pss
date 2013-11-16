<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>系统登陆</title>
	<link href="css/themes/ui-blue/jquery-ui-1.8.23.custom.css"	rel="stylesheet" type="text/css" />
	<style type="text/css">
		*{ padding:0px; margin:0px; }
		body{ margin:0px; padding:0px; background:#cde6fa; font-size:62.5%; }
		td{ font-size:12px; font-family:"宋体"; }
		li{ list-style:none; }
		.under:hover{ text-decoration:underline; cursor:pointer; }
		#login{ border:0; width:646px; }
		#codeImg{ height:28px; vertical-align:bottom; }
	</style>
	<script type="text/javascript" src="jquery/jquery1.7.2.js"></script>
	<script type="text/javascript" src="jquery/jquery-ui-1.8.23.custom.min.js"></script>
	<script type="text/javascript" src="<s:url value="/jquery/jquery.form.js"/>"></script>
	<script type="text/javascript">
		$(function() {
			var windowheight=$(window).height();
			var login=$('#login');
			$('table:first').height(windowheight);
			$('#loginform tr').find('td:first').css('text-align','right').end().find('td').css('padding','0.2em');
			$('#loginform tr').find('td:eq(1)').css('text-align','left');
			$('#loginform').find('input:not(:image)').addClass('ui-widget-content ui-corner-all').css('padding','0.3em');
			var $result=$('#result a');
			$('#refresh,#codeImg').click(function(){
				$('#codeImg').attr('src','login_getvcode.action?t='+new Date().getTime());
			});//更新验证码
			$('#dl').click(function(){
				if (!$('#verifyCode').val()){
					$result.text('验证码不能为空！');
					return false;
				}
				if (!$('#name').val()){
					$result.text('用户名不能为空！');
					return false;
				}
				if (!$('#password').val()){
					$result.text('密码不能为空！');
					return false;
				}
			});//登陆时输入框不能为空
			$('input').focus(function(){
				$result.text('');
			});
		});
	</script>
</head>

<body>
	<table style="width:100%;height:100%;">
		<tr>
			<td align="center" valign="middle">
				<table id="login" cellpadding="0" cellspacing="0">
					<tr>
						<td colspan="3"><img src="images/login_01.gif" /></td>
					</tr>
					<tr>
						<td><img src="images/login_02.gif" /></td>
						<td background="images/login_03.gif" width="476" align="center" valign="middle">
							<form id="loginform" action="login_login.action" method="post">
								<table style="width:80%;">
									<tr>
										<td>用户名：</td>
										<td><s:textfield name="name" value="admin" /></td>
									</tr>
									<tr>
										<td>密　码：</td>
										<td>
											<s:password name="password" value="admin" showPassword="true" />
										</td>
									</tr>
									<tr>
										<td>验证码：</td>
										<td>
											<input type="text" name="verifyCode" id="verifyCode" maxlength="4" size="6" />
											<span id="vcode">
												<img id="codeImg" alt="验证码" src="login_getvcode.action" />
												<span class="under" id="refresh">换一张</span>
											</span>
										</td>
									</tr>
									<tr style="height:26px;">
										<td></td>
										<td id="result"><a style="color: red"><s:fielderror/></a></td>
									</tr>
									<tr>
										<td><input type="checkbox" name="cookie" />记住密码</td>
										<td><input type="image" src="images/dl.gif" id="dl" /></td>
									</tr>
								</table>
							</form>
						</td>
						<td><img src="images/login_04.gif" /></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>