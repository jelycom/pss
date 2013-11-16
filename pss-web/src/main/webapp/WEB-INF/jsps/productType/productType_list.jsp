<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-商品种类管理</title>
<script type="text/javascript">
 	$(function() {
 		$("#producttypeTree").treeTable({url:'productType_treejson.action',caption:'产品分类树'});
		var gridoptions={
				url:'productType_listjson.action',
				colNames:['','编号', '名称', '拼音','所属种类'],
				colModel:[
					{name:'id',editable:false,hidden:true,gridview:false},
					{name:'item',editable:false,fuzzySearch:true},
					{name:'name',fuzzySearch:true,editrules:{required:true}},
					{name:'py',fuzzySearch:true},
					{name:'pid',dataUrl:"productType_treejson.action",completeGrid:completeGridSet,gridview:false,editrules:{required:true}}
				],
				datatype:"local",
				editurl:"productType_save.action",
				caption:"商品种类管理"
			};
		$("#list").selfGrid(gridoptions,{del:{url:"productType_delete.action"}});
	});
 	function completeGridSet(){
		return [{
					treeGrid: true,		            
		            pager:false,
		            rowNum:10000
           		},{width:200,reload:true}];
	}
</script>
</head>

<body>
	<div class="data_page">
		<div class="tree_div" rel="商品种类" field="编号,名称,所属商品">
			<div class="gridTableDiv treemenu">
				<table id="producttypeTree"></table>
				<div id="producttypeTreePager"></div>
			</div>
       </div>
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