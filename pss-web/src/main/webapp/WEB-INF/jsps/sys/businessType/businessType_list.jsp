<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-管理</title>
<script type="text/javascript">

</script>
</head>
<body>
	<!-- 加载前遮罩 -->
	<!-- <div class="cover"></div> -->
	<div id="search"></div>
	<div class="data_page"> <!-- 计算整体宽度 如果有左侧树class为tree_div-->
		<div class="treedata_div"><!-- 右侧列表页,如果左侧有树,则前面还有个tree_div -->
			<div class="gridTableDiv"> <!-- 计算每个Grid的宽,高 -->
				<table id="businessType_list" title="xx管理" height=180% multiselect="1" colnum=3 url="<s:url value="sys/businessType_listjson.action"/>"
				 saveurl="<s:url value="sys/businessType_save.action"/>" deleteurl="<s:url value="sys/businessType_delete.action"/>">
				 		<thead>
						<tr>
						<th name="id" editable="0" width="40" sort=1 type="integer"></th>
							<th name="name" width="100" sort=1 required=1>姓名</th>
							<th name="py">拼音</th>
							<th name="department.id" index="department.id" formatter="deptf" edittype="select" url="department_listall.action" jsonName="depts" headerkey="-1" headervalue="-请选择-">部门</th>
							<th name="job">职务</th>
							<th name="mobilePhone" >手机</th>
							<th name="telePhone" >电话</th>
							<th name="address">地址</th>
							<th name="birthday" edittype="text" type="date" yearRange="c-30:">出生日期</th>
					 		<th name="hireDate" edittype="text" type="date">入职日期</th>
							<th name="nickName" gridview="0">昵称</th>
							<th name="loginName" gridview="1">登录名</th>
							<th name="password" gridview="0">密码</th>
							<th name="leaveDate" type="date" yearRange="-5:+0">离职日期</th>
							<th name="expireDate" type="date">过期日期</th>
							<th name="deleted" type="checkbox" edittype="checkbox" formatter="delf">删除</th>
							<th name="valid" type="checkbox">有效</th>
							<th name="memo" edittype="textarea">备注</th>
					 	</tr>
					</thead>
				</table>
				<div id="pager"></div>
			</div>
	</div>
</div>
</body>
</html>