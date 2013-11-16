<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单位分类及级别</title>
<%@include file="/common/css_scripts_common.jsp"%>
<script type="text/javascript">
	$(function() {
		$("#baseDataTypeTree").treeTable({url:"baseDataType_treejson.action",caption:'基础数据'},{
			tableReload:function($table,id){
				var rules='{"groupOp":"AND","rules":[{"field":"dataType.id","op":"eq","data":"'+id+'"}]}';
				$table.jqGrid('setGridParam',{datatype:"json",page:1,
					postData:{'filters':rules}}).trigger("reloadGrid").setGridParam({postData:{'filters':{}}});
				return rules;
			}
		});

		var gridoptions={
				caption:'数据字典管理',
				url:'baseData_listjson.action',
				datatype:'local',
				colNames:['','数据类别名称','拼音','所属分类','备注'],
				colModel:[
				    {name:'id',editable:false,hidden:true,gridview:false},
				    {name:'name',width:300,fuzzySearch:true,editrules:{required:true}},
				    {name:'py',fuzzySearch:true},
				    {name:'dataType.id',gridview:false,dataUrl:'baseDataType_treejson.action',completeGrid:completeGridSet,editrules:{required:true}},
				    {name:'memos',edittype:'textarea'}
				],
				editurl:'baseData_save.action'
		};
		var formatter={			//需要返回所属分类时再加
				typef:function(cellData,options,rowObject){
					return rowObject.dataType?rowObject.dataType.name:'';
				}
		}
		$('#list').selfGrid(gridoptions,{del:{url:'baseData_delete.action'}});
	});
	function completeGridSet(){
		return [{treeGrid: true,pager:false,rowNum:10000}];
	}
</script>
</head>
<body>
	<div class="data_page">
		<div class="tree_div">
			<div class="gridTableDiv">
				<table id="baseDataTypeTree"></table>
				<div id="baseDataTypeTreePager"></div>
			</div>
        </div>
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
