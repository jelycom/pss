<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-URL管理</title>
<script type="text/javascript">
	$(function() {
		var gridoptions={
				url:'actionResource_listjson.action',
				treeGrid:true,
				colNames:['','名称','链接地址','菜单名称','上级菜单','备注'],
				colModel:[
					{name:'id',editable:false,hidden:true,gridview:false},
					{name:'name',fuzzySearch:true},
					{name:'linkAddress'},
					{name:'menu.name',fuzzySearch:true,editrules:{required:true}},
					{name:'pid',search:false,dataUrl:"actionResource_listjson.action",completeGrid:completeGridSet,gridview:false},
					{name:'memos',edittype:'textarea',hidden:true}
				],
				editurl:"actionResource_save.action",
				caption:"URL资源管理"
			};
		$("#list").selfGrid(gridoptions,{del:{url:"actionResource_delete.action"}});
	});
	function completeGridSet(){
		return [{
				treeGrid: true,		            
	            pager:false,
	            rowNum:10000
            },{reload:true}];
	}
</script>
</head>

<body>
	<div class="data_page">
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id="list" nosaveFilter=1></table>
			</div>
		</div>
	</div>
</body>
</html>