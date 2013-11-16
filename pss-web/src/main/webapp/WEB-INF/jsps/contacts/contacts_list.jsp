<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-联系人管理</title>
<script type="text/javascript">
	$(function() {
		var gridoptions={
			url:'contacts_listjson.action',
			caption:'联系人信息',
			colNames:['','中文名','姓名拼音','英文名','单位','职称/职务','联系电话','手机','电子邮件','生日','纪念日说明'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'name',width:120,editrules:{required:true},fuzzySearch:true}
				,	{name:'py',width:100,fuzzySearch:true}
				,	{name:'englishName',width:160,fuzzySearch:true}
				,	{name:'businessUnit.id',width:300,formatter:'unitf',dataUrl:'businessUnits_listjson.action',completeGrid:completeGridSet,editrules:{required:true}}
				,	{name:'title',width:100}
				,	{name:'phone'}
				,	{name:'mobilePhone'}
				,	{name:'email',formatter:'email',width:200}
				,	{name:'birthday',formatter:'date',hidden:true}
				,	{name:'dateDescription',hidden:true}
			],
			editurl:'contacts_save.action'
		};
		var formatter={
				unitf:function(cellData,options,rowObject){
					return rowObject.businessUnit?rowObject.businessUnit.name:'';
				}
		}
		$('#list').selfGrid(gridoptions,{del:{url:'contacts_delete.action'}},formatter);
	});
	
	//联系人所所属单位的自动完成参数
	function completeGridSet(){
		return	[{
			colNames:["id","编号","简称","拼音"],
			colModel:[{name:"id",hidden:true},{name:"item",width:"40%"},{name:"shortName",width:"60%"},{name:"py",hidden:true}]
		},
		{width:240,reload:false}];
	}
</script>
</head>

<body>
	<div class="data_page" id="employee">
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id='list' actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>
</body>
</html>