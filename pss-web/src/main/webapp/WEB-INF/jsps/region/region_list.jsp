<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-地区管理</title>
<script type="text/javascript">
 	$(function() {
 		$("#regionTree").treeTable({url:'region_treejson.action',caption:'地区树'});
 		var gridoptions={
				url:'region_listjson.action',
				colNames:['id','编号', '名称', '所属地区'],
				colModel:[
					{name:'id',editable:false,hidden:true,gridview:false},
					{name:'item',editable:false,fuzzySearch:true,lineFeed:true,viewable:true},
					{name:'name',fuzzySearch:true,editrules:{required:true}},
					{name:'pid',dataUrl:"region_treejson.action",completeGrid:completeSet,gridview:false,editrules:{required:true}}
				],
				datatype:"local",
				editurl:"region_save.action",
				caption:"地区管理"
			};
		$("#list").selfGrid(gridoptions,{del:{url:"region_delete.action"},view:{width:300}});
	});
 	function completeSet(){
		return [{treeGrid: true,pager:false,rowNum:10000},{width:200,reload:true}];
	}
</script>
</head>

<body scroll="no">
	<div class="data_page">
		<div class="tree_div" rel="所选地区信息" field="编号,名称,所属地区">
			<div class="gridTableDiv">
				<table id="regionTree"></table>
				<div id="regionTreePager"></div>
			</div>
        </div>
		<div class="treedata_div">
<!-- 			<div id='treeName' class='selectedInfo''>
				<div id='attrTitle' class='ui-widget-header'>
					当前所选地区信息
				</div>
				<div id="nodeInfo" class='ui-widget-content'>
					<table class='selectedFiled'>
						<tr>
							<td style="width:15%">编号</td><td></td>
							<td style="width:15%">名称</td><td></td>
							<td style="width:15%">所属地区</td><td></td>
						</tr>
					</table>
				</div>
			</div> -->
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id="list" actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>
</body>
</html>