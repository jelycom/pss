<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-会计期间管理</title>
<script type="text/javascript">
	$(function(){
 		var gridoptions={
				url:'accountingPeriod_listjson.action',
				colNames:['id','会计期间名称', '会计期间年度','会计期间月度','开始日期','结束日期','状态'],
				colModel:[
					{name:'id',editable:false,hidden:true,gridview:false},
					{name:'name',fuzzySearch:true,editrules:{required:true}},
					{name:'year',fuzzySearch:true},
					{name:'month',fuzzySearch:true},
					{name:'start',formatter:'date',editrules:{required:true}},
					{name:'end',formatter:'date',editrules:{required:true}},
					{name:'state',editable:false}
				],
				datatype:'json',
				editurl:"accountingPeriod_save.action",
				caption:"会计期间管理"
			};
		$("#list").selfGrid(gridoptions,{del:{url:"accountingPeriod_delete.action"}});
	});
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