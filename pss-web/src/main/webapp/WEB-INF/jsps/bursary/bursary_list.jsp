<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-会计科目管理</title>
<script type="text/javascript">
 	$(function() {
 		$("#bursaryTree").treeTable({url:'bursary_treejson.action',caption:'会计科目',ExpandColumn: 'fullName',
 									colNames:['id','item','名称','简称','拼音'],
 									colModel:[
 											{name:'id',hidden:true}
 										,	{name:'item',hidden:true}
 										,	{name:'fullName'}
 										,	{name:'shortName',hidden:true}
 										,	{name:'py',hidden:true}
 									]
 							});
 		var gridoptions={
				url:'bursary_listjson.action',
				colNames:['id','编号', '名称','拼音','简称', '上级'],
				colModel:[
					{name:'id',editable:false,hidden:true,gridview:false},
					{name:'item',editable:false,fuzzySearch:true,lineFeed:true,viewable:true},
					{name:'fullName',fuzzySearch:true,editrules:{required:true}},
					{name:'py',fuzzySearch:true},
					{name:'shortName',fuzzySearch:true},
					{name:'pid',dataUrl:"bursary_treejson.action",completeGrid:completeGridSet,gridview:false,editrules:{required:true}}
				],
				datatype:"local",
				editurl:"bursary_save.action",
				caption:"会计科目管理"
			};
		$("#list").selfGrid(gridoptions,{del:{url:"bursary_delete.action"},view:{width:300}});
	});
 	function completeGridSet(){
		return [{
				colNames:['id','item','名称','简称','拼音'],
				colModel:[
							{name:'id',hidden:true}
						,	{name:'item',hidden:true}
						,	{name:'fullName'}
						,	{name:'shortName',hidden:true}
						,	{name:'py',hidden:true}
				],
				ExpandColumn: 'fullName',
				treeGrid: true,pager:false,rowNum:10000
			},
			{width:200,reload:true}];
	}
</script>
</head>

<body scroll="no">
	<div class="data_page">
		<div class="tree_div">
			<div class="gridTableDiv">
				<table id="bursaryTree"></table>
				<div id="bursaryTreePager"></div>
			</div>
        </div>
		<div class="treedata_div">
			<div id='treeName' class='selectedInfo''>
				<div id='attrTitle' class='ui-widget-header'>
					当前所选科目信息
				</div>
				<div id="nodeInfo" class='ui-widget-content'>
					<table class='selectedFiled'>
						<tr>
							<td style="width:15%;font-weight:bold;">编号</td><td style="width:18%"></td>
							<td style="width:15%;font-weight:bold;">名称</td><td style="width:18%"></td>
							<td style="width:15%;font-weight:bold;">上级科目</td><td style="width:19%"></td>
						</tr>
					</table>
				</div>
			</div>
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id="list" actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>
</body>
</html>