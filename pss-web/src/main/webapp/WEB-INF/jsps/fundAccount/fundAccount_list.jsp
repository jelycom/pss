<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-现金银行管理</title>
<script type="text/javascript">
	$(function() {
		var gridoptions={
			url:'fundAccount_listjson.action',
			caption:'现金银行管理',
			colNames:['','帐户编号','帐户名称','拼音码','开户行','帐号','当前余额','银行帐户','默认帐户','失效'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'item',width:120,fuzzySearch:true}
				,	{name:'name',fuzzySearch:true,editrules:{required:true}}
				,	{name:'py',width:80,fuzzySearch:true}
				,	{name:'bankName',width:240}
				,	{name:'accountNo',width:160,editrules:{required:true}}
				,	{name:'current',editable:false,align:'right',formatter:'currency'}
 				,	{name:'isBank',width:80,edittype:'checkbox'}
				,	{name:'isDefault',width:80,edittype:'checkbox'}
				,	{name:'invalid',edittype:'checkbox',editoptions:{value:'true:false'}}
			],
			editurl:'fundAccount_save.action'
		};
		
		$('#list').selfGrid(gridoptions,{del:{url:'fundAccount_delete.action'}});
	});

</script>
</head>

<body>
	<div class="data_page">
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