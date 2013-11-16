<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>雇员信息</title>
<%@include file="/common/css_scripts_common.jsp"%>
<script type="text/javascript">
	$(function() {
		var gridoptions={
			url:'employee_listjson.action',
			caption:'员工管理',
			colNames:['','姓名','拼音','部门','职务','手机','电话','地址','出生日期','入职日期'
			          ,'昵称','离职日期','登录ID','备注','登录名','登录密码','帐户状态','帐户状态','离职日期','帐户角色','备注'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'name',width:180,fuzzySearch:true,editrules:{required:true}}
				,	{name:'py',width:80,fuzzySearch:true}
				,	{name:'department.id',formatter:'depf',dataUrl:'department_listjson.action',completeGrid:completeGridSet}
				,	{name:'job',width:100}
				,	{name:'mobilePhone'}
				,	{name:'telePhone'}
				,	{name:'address',hidden:true}
				,	{name:'birthday',formatter:'date',editoptions:{yearRange:'-80:-14'}}
				,	{name:'hireDate',formatter:'date',editoptions:{yearRange:'-30:+0'}}
				,	{name:'nikeName',hidden:true}
				,	{name:'leaveDate',formatter:'date',hidden:true}
				,	{name:'user.id',editable:false,hidden:true,gridview:false}
				,	{name:'memos',edittype:'textarea',hidden:true,lineFeed:true}
				,	{name:'user.name',tabsTitle:'登录信息'}
				,	{name:'user.password'}
				,	{name:'user.stateId',edittype:'select',editoptions:{value:{'-1':'删除','0':'禁用','1':'正常'}},hidden:true,gridview:false}
				,	{name:'user.stateName',editable:false,search:false}
				,	{name:'user.expireDate',formatter:'date'}
				,	{name:'user.roles.ids',formatter:'rolef',completeGrid:rolesCompSet,dataUrl:'role_listjson.action',editoptions:{readonly:true}}				
//				,	{name:'user.roles.id',formatter:'rolef',edittype:'select',dataUrl:'role_listjson.action',editoptions:{multiple:true}}
				,	{name:'user.memos',edittype:'textarea',hidden:true,gridview:false}
			],
			editurl:'employee_save.action'
		};
		var formatter={
				depf:function(c,o,d){
					return d.department?d.department.name:'';
				},
				rolef:function(c,o,d){
					if (d.user.roles){
						var dispName='',rows=d.user.roles;
						$.each(rows,function(i,n){
							dispName=dispName+n.dispName+(i==rows.length-1?'':',');
						});
						return dispName;
					}
					return '';
				}
		};
		$("#list").selfGrid(gridoptions,{del:{url:'employee_delete.action'}},formatter);
	});
	function completeGridSet(){
		return [];
	}
	function rolesCompSet(){
		return [{
			multiselect:true,
			rowNum:10000,
			pagerpos:'none',
			colNames:['','角色名称'],
			colModel:[{name:'id',hidden:true},{name:'dispName'}]
		},{reload:true}]
	}
</script>
</head> 
<body>
	<div class="data_page" id="employee">
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id='list' tabs=1 actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>

</body>
</html>
