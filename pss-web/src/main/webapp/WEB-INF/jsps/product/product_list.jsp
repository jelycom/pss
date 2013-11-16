<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-管理</title>
<script type="text/javascript">
	$(function() {
 			$("#productTypeTree").treeTable({url:'productType_treejson.action',caption:'产品分类树'},{
				tableReload:function($table,id,$treeGrid){
					var rc = new Rc($("#"+id),$treeGrid);
					var rules='{"groupOp":"AND",' +
									'"rules":[{"field":"productType.lft","op":"ge","data":"'+rc.lft+'"},' +
									'{"field":"productType.rgt","op":"le","data":"'+rc.rgt+'"}]}';
					$table.jqGrid('setGridParam',{datatype:"json",page:1,
						postData:{'filters':rules}
					}).trigger("reloadGrid");
					return rules;
				}
			});
			
			var gridoptions={
					url:'product_listjson.action',
					datatype:'local',
					colNames:['','编号','名称','所属类别', '成本方法','简称','拼音码','型号','规格','单位','辅助单位','条码','颜色','安全库存量',
					          '最后进货日','最后销售日','最大库存量','最小库存量','货位可存量','预售价1','预售价2','预售价3','失效','删除','备注'],
					colModel:[
						{name:'id',editable:false,hidden:true,gridview:false},
						{name:'item',width:200,editable:false,fuzzySearch:true},
						{name:'fullName',fuzzySearch:true,width:200,editrules:{required:true}},
						{name:'productType.id',dataUrl:'productType_treejson.action',formatter:'prodf',completeGrid:ptCompSet,editrules:{required:true}},
						{name:'costMethod',edittype:'select',formatter:'costf',editoptions:{value:"${costMethod}"}},
						{name:'shortName',fuzzySearch:true},
						{name:'py',width:100,fuzzySearch:true},
						{name:'marque'},
						{name:'specification',width:100,hidden:true},
						{name:'unit',align:'center',dataUrl:'baseData_listjson.action',completeGrid:unitCompSet,width:60},
						{name:'auxUnit',hidden:true,dataUrl:'baseData_listjson.action',completeGrid:unitCompSet},
						{name:'barCode',hidden:true},
						{name:'color',hidden:true},
						{name:'safeStock',hidden:true},
						{name:'lastPurchaseDate',hidden:true,editoptions:{readonly:true}},
						{name:'lastDeliveryDate',hidden:true,editoptions:{readonly:true}},
						{name:'maxStock',hidden:true},
						{name:'minStock',hidden:true},
						{name:'allocationCanStock',hidden:true,editoptions:{readonly:true}},
						{name:'salePrice1',hidden:true},
						{name:'salePrice2',hidden:true},
						{name:'salePrice3',hidden:true},
						{name:'invalid',edittype:'checkbox',hidden:true},
						{name:'deleted',edittype:'checkbox',hidden:true},
						{name:'memos',edittype:'textarea',hidden:true}
					],
					editurl:"product_save.action",
					caption:"单位信息管理"
				};
			var formatter={
					prodf:function(c,o,d){
						return d.productType?d.productType.name:"";
					},
					costf:function(c,o,d){
						return d.costMethod?d.costMethod.methodName:"";
					}
			};
			$("#list").selfGrid(gridoptions,{del:{url:"product_delete.action"}},formatter);
			
	});
	function ptCompSet(){
		return [{
					treeGrid: true,		            
		            pager:false,
		            rowNum:10000
           		}];
	}
	function unitCompSet(){
		return [{
			colNames:["名称","拼音"],
			colModel:[{name:"name"},{name:"py",hidden:true}],
			pager:false,
			rowNum:1000,
			postData:{
				'filters':'{"groupOp":"AND","rules":[{"field":"dataType.sn","op":"eq","data":"productUnit"}]}'
			},
			gridComplete:function(){
				$(this.grid.hDiv).hide();
			}
		},{width:60,keylength:1,createInput:false,reload:false}];
	}
</script>
</head>

<body>
	<div class="data_page">
	  	<div class="tree_div" rel="商品种类" field="编号,名称,所属商品种类">
	  		<div class="gridTableDiv treemenu">
				<table id="productTypeTree"></table>
				<div id="productTypeTreePager"></div>
			</div>
        </div>
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id="list" tabs=1 actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>
</body>
</html>