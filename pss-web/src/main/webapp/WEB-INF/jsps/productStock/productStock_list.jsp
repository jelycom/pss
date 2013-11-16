<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<script type="text/javascript" src="js/util/regionWare.js"></script>
<title>商品库存</title>
<script type="text/javascript">
	$(function() {
		$("#productTypeTree").treeTable({url:'productType_treejson.action',caption:'产品分类树'},{
			tableReload:function($table,id,$treeGrid){
				var rc=new Rc($("#"+id),$treeGrid);
				var wareID=$('#regionWare').val();
				var wareString=!wareID?'':',{"field":"warehouse.id","op":"eq","data":"'+wareID+'"}';
				var rules='{"groupOp":"AND",' +
						'"rules":[{"field":"product.productType.lft","op":"ge","data":"'+rc.lft+'"},' +
						'{"field":"product.productType.rgt","op":"le","data":"'+rc.rgt+'"}'+wareString+
					']}';
				$table.jqGrid('setGridParam',{datatype:"json",page:1,postData:{'filters':rules}}).trigger("reloadGrid");
				return rules;
			}
		});
		var gridoptions={
			url:'productStock_listjson.action',
			datatype:'local',
			caption:'商品库存',
			footerrow:true,
			colNames:['','商品编号','商品名称','型号','规格','单位','数量','单价','库存总额'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false,formatter:'prodf'}
				,	{name:'product.item',width:180,editable:false,fuzzySearch:true}
				,	{name:'product.fullName',width:180}
				,	{name:'product.marque',width:100,editable:false,sortable:false}
				,	{name:'product.specification',width:100,editable:false,sortable:false}
				,	{name:'product.unit',width:80,editable:false,sortable:false}
				,	{name:'quantity',width:100,editrules:{number:true}}
				,	{name:'price',width:100,align:'right',formatter:'pricf',formatoptions:{decimalPlaces:4},sortable:false}
				,	{name:'amount',width:160,formatter:'currency'}
			],
			editurl:'productStock_save.action',
			gridComplete:function(){
				var $grid=$(this),
					gridID=$grid.attr("id");
				var total=$grid.getCol('quantity',false,'sum');
				var amount=$grid.getCol('amount',false,'sum');
				$grid.footerData('set',{'product.item':'合计','quantity':total,'amount':amount});
				var $toolbar=$("#t_"+gridID);
				$toolbar.regionWare();//加载地区仓库表
			}
		};
		var formatter={
				prodf:function(cellData,options,rowObject){
					return rowObject.product?rowObject.product.id:'';
				}
			,	pricf:function(cellData,options,rowObject){
					return rowObject.amount&&rowObject.quantity?(rowObject.amount/rowObject.quantity).toFixed(4):'';
				}
		};
		var naviPrm={
				parameter:{
					add:false,edit:false,view:true,viewtext:'商品分布',del:false,searchtext:'搜索',refreshtext:'刷新',
					beforeRefresh:function(){
						if($('.tree_div')[0]&&$(this).is('[id!=location]')){		//刷新时左边有树，根据左边选择的行来重新加载主表
							$treeGrid=$('.tree_div .ui-jqgrid-btable')
							var id=$treeGrid.getGridParam('selrow');
							if (id){
								$('#'+id,$treeGrid).trigger('dblclick');
						    }
							return false;
						}
					},
					afterRefresh:function(){
						$(this).setGridParam({postData:{'filters':$(this).data('initFilter')}});
					},
					viewfunc:function(rowid){
						var rowData=$(this).getRowData(rowid);
						if (!$('body').is(':has(#stockDistribute)')){
							var $distributeDiv = $("<div id='stockDistribute' title='"+rowData['product.fullName']+"库存分布'><div class='gridTableDiv'>" +
									"<table id='distribute'></table>" +
									"</div></div>");
							$distributeDiv.appendTo('body');
							$distributeDiv.dialog({autoOpen:false,modal:true,width:350,resizable:false,minHeight:false,
								buttons:{
									'确定':function(){
										$(this).dialog("close");
									}
								},
								create:function(){
									var options={
											url:'productStock_listjson.action',
											width:576,
											colNames:['','仓库','数量','库存金额'],
											colModel:[
											    {name:'id',hidden:true},      
												{name:'warehouse.id',width:'160',formatter:function(c,o,r){return r.warehouse?r.warehouse.name:''}},
												{name:'quantity',width:'120'},
												{name:'amount',align:'right',width:'120'},
											],
											postData:{'filters':'{"groupOp":"AND","rules":[{"field":"product.id","op":"eq","data":"'+rowData.id+'"}]}','group':'warehouse'},
											footerrow:true,
											rownumbers:true,
											rownumWidth:35,
											loadComplete:function(xhr){
												var $grid=$(this);
												var quantityArray=$grid.getCol('quantity',false),
													amountArray=$grid.getCol('amount',false),
													totalQuantity=0,totalAmount=0;
												for (var i=0;i<quantityArray.length;i++){			//计算合计数量和总金额
													if (!isNaN(Number(quantityArray[i]))){
														totalQuantity+=Number(quantityArray[i]);
														totalAmount+=Number(amountArray[i]);
													}
												}
												$grid.footerData('set',{'rn':'合计','quantity':totalQuantity,'amount':totalAmount.toFixed(2)});
												gridSize($grid);
											}
									};
									$('#distribute').simpGrid(options);
								}
							});
						}else{
							$('#distribute').setGridParam({postData:{'filters':'{"groupOp":"AND","rules":[{"field":"product.id","op":"eq","data":"'+rowData.id+'"}]}'}}).trigger('reloadGrid');
						}
						$('#stockDistribute').dialog('open');
					}//自定义查看，改变为查看该商品在各仓库中的库存
				}
		};
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
	});
</script>
</head>

<body>
	<div class="data_page">
		<div class="tree_div">
	  		<div class="gridTableDiv treemenu">
				<table id="productTypeTree"></table>
				<div id="productTypeTreePager"></div>
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