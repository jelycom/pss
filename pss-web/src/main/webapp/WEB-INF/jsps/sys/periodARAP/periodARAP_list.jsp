<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>期初应收应付</title>
<script type="text/javascript">
	$(function() {
		var gridoptions={
			url:'periodARAP_listjson.action',
			caption:'期初应收应付',
			colNames:['','会计期间','单位编号','单位名称','期初应收款','期初应付款'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'acountingPeriod.id',editable:false}
				,	{name:'businessUnits.item',editable:false,fuzzySearch:true}
				,	{name:'businessUnits.id',dataUrl:'businessUnits_listjson.action',completeGrid:unitCompSet,formatter:'busif'}
				,	{name:'receivable',formatter:'currency'}
				,	{name:'payable',formatter:'currency'}
			],
			editurl:'periodARAP_save.action'
		};
		var formatter={
				busif:function(cellData,options,rowObject){
					return rowObject.businessUnits?rowObject.businessUnits.name:'';
				}
		};
		$('#list').selfGrid(gridoptions,{del:{url:'periodARAP_delete.action'}},formatter);
	});
	function unitCompSet(){
		return [{},{keylength:1}]
	}
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