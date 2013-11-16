<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>部门管理-部门列表</title>
<%@include file="/common/css_scripts_common.jsp"%>
<script type="text/javascript">
	$(function(){
		var gridoptions={
				url:'department_listjson.action',
				colNames:['','部门名称', '拼音码', '上级部门', '部门管理员', '备注'],
				colModel:[
					{name:'id',editable:false,hidden:true,gridview:false},
					{name:'name',fuzzySearch:true,editrules:{required:true}},
					{name:'py',width:100,fuzzySearch:true},
					{name:'parent.id',dataUrl:"department_listjson.action",formatter:"deptf",completeGrid:completeGridSet},
					{name:'manager.id',dataUrl:"employee_listjson.action",edittype:'select'}, //editable是否允许修改,cellEdit设置为true有效
					{name:'memo',edittype:"textarea"}
				],
				editurl:"department_save.action",
				caption:"部门管理"
			};
		var formatter={
				deptf:function(c,o,d){
					return d.parent?d.parent.name:"";
				},
				manaf:function(c,o,d){
					return d.manager?d.manager.name:"";
				}
		};
		$("#list").selfGrid(gridoptions,{del:{url:"department_delete.action"}},formatter);
	});
	function completeGridSet(){
		return [{},{reload:true}];
	}
</script>
</head>

<body>
	<div class="data_page">
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id="list" actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>
</body>
</html>