<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-库房管理</title>
<script type="text/javascript">
$(function () {
	var $list1=$("#warehouse"),
		$list2=$("#warehouseAllocation");
	$("#regionTree").treeTable({url:'region_treejson.action',caption:'地区树'},{
		tableReload:function($table,id){
			var rc = new Rc($("#"+id));
			var rules='{"groupOp":"AND",' +
						'"rules":[{"field":"region.lft","op":"ge","data":"'+rc.lft+'"},' +
						'{"field":"region.rgt","op":"le","data":"'+rc.rgt+'"}]}'
			$table.jqGrid('setGridParam',{datatype:"json",page:1,
				postData:{'filters':rules}
			}).trigger("reloadGrid");
			return rules;
		}
	});
	
	var gridoptions={
		url:'warehouse_listjson.action',
		caption:'仓库管理',
		datatype:'local',
		pager:'#pager1',
		rowNum:'5',
		colNames:['id','编号','仓库名称','拼音码','仓库地址','所属地区','备注'],
		colModel:[
				{name:'id',editable:false,hidden:true,gridview:false}
			,	{name:'item',editable:false,fuzzySearch:true}
			,	{name:'name',editrules:{required:true},fuzzySearch:true}
			,	{name:'py',width:80,fuzzySearch:true}
			,	{name:'address',width:300}
			,	{name:'region.id',formatter:'regf',dataUrl:'region_treejson.action',completeGrid:regionCompSet,gridview:false}
			,	{name:'memos',edittype:'textarea',hidden:true}
		],
		editurl:'warehouse_save.action',
		beforeRequest:function(){
			$list2.setGridParam({datatype:'local'}).trigger('reloadGrid');//重新加载表格时清空库位表格数据
		}
	};
	$list1.selfGrid(gridoptions,{del:{url:'warehouse_delete.action'}});

	var locationoptions={
		url:'warehouseAllocation_listjson.action',
		caption:'库位管理',
		pager:'#pager2',
		datatype:'local',
		rowNum:'5',
		colNames:['id','编号','货位名称','拼音','失效','库房','备注'],
		colModel:[
				{name:'id',editable:false,hidden:true,gridview:false}
			,	{name:'item',fuzzySearch:true}
			,	{name:'name',editrules:{required:true},fuzzySearch:true}
			,	{name:'py',fuzzySearch:true}
			,	{name:'invalid',edittype:'checkbox',lineFeed:true}
			,	{name:'warehouse.id',search:false,gridview:false}
			,	{name:'memos',edittype:'textarea'}
		],
		editurl:'warehouseAllocation_save.action'
	};
	var naviPrm={
 		parameter:{beforeRefresh:function(){
			var rowid=$list1.getGridParam('selrow');
			$(this).jqGrid('setGridParam',{datatype:"json",
				postData:{
					'filters':'{' +
						'"groupOp":"AND",' +
						'"rules":[{"field":"warehouse.id","op":"eq","data":"'+rowid+'"}]' +
					'}'
				}
			}).trigger("reloadGrid");
			return false;
		}},
		del:{url:'warehouseAllocation_delete.action'}
	};
	$list2.selfGrid(locationoptions,naviPrm);
});
function regionCompSet(){
	return [{treeGrid:true,pager:false,rowNum:10000}];
}
</script>
</head>

<body>
	<div class="data_page">
		<div class="tree_div">
			<div class="gridTableDiv">
				<table id="regionTree"></table>
				<div id="regionTreePager"></div>
			</div>
        </div>
		<div class="treedata_div">
			<div class="gridTableDiv">		<!-- 仓库表格 -->
				<div id="pager1"></div>
				<div id="search1"></div>
				<table id='warehouse' actionName="${queryappmodel}"></table>
			</div>
			<div class="gridTableDiv">		<!-- 货位表格 -->
				<table id='warehouseAllocation' noSaveFilter="1" xhrcache=false></table>
				<div id="pager2"></div>
				<div id="search2"></div>
			</div>
		</div>
	</div>

</body>
</html>