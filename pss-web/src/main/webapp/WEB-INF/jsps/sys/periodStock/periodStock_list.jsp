<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<script type="text/javascript" src="/pss/js/util/regionWare.js"></script>
<title>期初库存</title>
<script type="text/javascript">
	$(function() {
		$("#productTypeTree").treeTable({url:'productType_treejson.action',caption:'产品分类树'},{
			tableReload:function($table,id,$treeGrid){
				var rc = new Rc($("#"+id),$treeGrid);
				var wareID=$('#regionWare').val();
				var rules='{"groupOp":"AND",' +
						'"rules":[{"field":"productType.lft","op":"ge","data":"'+rc.lft+'"},' +
						'{"field":"productType.rgt","op":"le","data":"'+rc.rgt+'"}'+
					']}';
				$table.setGridParam({postData:{'sidx':null}});
				console.log($table.getGridParam('postData').sidx);
				$table.jqGrid('setGridParam',{datatype:"json",page:1,postData:{'filters':rules,'warehouseId':wareID}}).trigger("reloadGrid");
				console.log($table.getGridParam('postData').sidx);
				return rules;
			}
		});
		var gridoptions={
			url:'periodStock_listjson.action',
			datatype:'local',
			caption:'期初库存',
			rowNum:100000,
			pagerpos:null,
			colNames:['','会计期间','商品ID','商品编号','商品名称','型号','规格','单位','数量','单价','库存总额'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'acountingPeriod.id',editable:false,hidden:true,gridview:true,search:false}
				,	{name:'product.id',hidden:true,gridview:false,search:false}
				,	{name:'product.item',index:'item',width:180,editable:false,fuzzySearch:true}
				,	{name:'product.fullName',index:'fullName',width:180}
				,	{name:'product.marque',width:100,sortable:false}
				,	{name:'product.specification',width:100,sortable:false}
				,	{name:'product.unit',width:80,sortable:false}
				,	{name:'quantity',width:100,editrules:{number:true}}
				,	{name:'price',width:100,formatter:'currency',formatoptions:{decimalPlaces:4},sortable:false}
				,	{name:'amount',width:160,formatter:'currency',lineFeed:true}
			],
			editurl:'periodStock_save.action',
			gridComplete:function(){
				var $grid=$(this),
					gridID=$grid.attr("id");
				var $toolbar=$("#t_"+gridID);
				$toolbar.regionWare();//加载地区仓库列表
			}
		};
		var naviPrm={parameter:{add:false,view:false,del:false,editfunc:periodEdit}};
		$('#list').selfGrid(gridoptions,naviPrm);
		$('#warehouseName').completeGrid([{},{createInput:false}]);
	});
	
	function periodEdit(rowid){
		var $grid=$(this),
			gridID=$grid.attr('id');
		var rData=$grid.getRowData(rowid),
			wareName=$('#regionWare').text(),
			wareId=$('#regionWare').val();
		var $periodEdit=$('#'+gridID+'Edit');
		if (!wareId){
			alert('请先选择仓库！');
			return false;
		}
		if (!$periodEdit[0]){
			$periodEdit=$("<div id='"+gridID+"Edit' title='商品期初库存修改'><form id='productPeriod' action='periodStock_save.action' method='post'><table>"+
					"<tr style='display:none;'><td>id</td><td>"+rData["id"]+"<input type='hidden' id='id' name='id' value='"+rData["id"]+"' /></td></tr>"+
					"<tr><td>商品名称</td><td><span id='pname'>"+rData["product.fullName"]+"</span><input type='hidden' id='productID' name='product.id' value='"+rData["product.id"]+"' /></td></tr>"+
					"<tr><td>仓库</td><td><span id='wname'>"+wareName+"</span><input type='hidden' id='warehouseID' name='warehouse.id' value='"+wareId+"' /></td></tr>"+
					"<tr><td>期初数量</td><td><input type='text' id='quantity' name='quantity' value='"+rData["quantity"]+"' /></td></tr>"+
					"<tr><td>成本单价</td><td><input type='text' id='price' name='price' value='"+rData["price"]+"' /></td></tr>"+
					"<tr><td>库存金额</td><td><input type='text' id='amount' name='amount' value='"+rData["amount"]+"' /></td></tr>"+
					"</table></form></div>");
			$periodEdit.appendTo('body');
			$periodEdit.dialog({autoOpen:false,modal:true,width:260,resizable:false,minHeight:false,
				buttons:{
					'确定':function(){
						$('#productPeriod').unbind('submit').submit(function(){
							$(this).ajaxSubmit({success:function(r,s){
								if (r&&r.operate){
									$(".treedata_div table[id]").trigger("reloadGrid");
									console.log('success');
								}
							}});
							return false;
						});
						$('#productPeriod').submit();
						$(this).dialog("close");
					},
					'取消':function(){
						$(this).dialog("close");
					}
				},
				create:function(){
					var $formDiv=$(this).addClass("ui-jqdialog-content"),
						$table=$formDiv.find('table').addClass('EditTable');
					$table.find("td:even").addClass("CaptionTD").end()
						  .find("td:odd").addClass("DataTD").children(":text").addClass("FormElement ui-widget-content ui-corner-all");
				},
				open:function(){
					inputMove($("table",this));
					compute();
				}
			});
		}else{
			var form=$('form',$periodEdit)[0];
			$('#pname').text(rData['product.fullName']);
			$('#wname').text(wareName);
			$('#warehouseID').val(wareId);
			$.each(rData,function(i,n){
				$(form[i]).val(n);
			});
		}
		$periodEdit.dialog('open');
	}
	
	function compute(){							//计算金额和单价
		var $quantity=$('#quantity');
		var $price=$('#price');
		var $amount=$('#amount');
		$quantity.change(function(){
			var quantity=$quantity.val(),
				price=$price.val(),
				amount=$amount.val();
			if (quantity&&price)			$amount.val((quantity*price).toFixed(2));	//有单价计算金额
			if (quantity&&!price&&amount)	$price.val((amount/quantity).toFixed(4));	//没有单价有金额计算单价
		});
		$price.change(function(){
			var quantity=$quantity.val(),
				price=$price.val();
			if (quantity&&price)	$amount.val((quantity*price).toFixed(2));			//有单价和数量计算金额
			else	$amount.val('');													//缺一个金额置空
		});
		$amount.change(function(){
			var quantity=$quantity.val(),
				amount=$amount.val();
			if (quantity&&amount)	$price.val((amount/quantity).toFixed(4));			//有数量和金额计算单价		
			else	$price.val('');														//缺一个单价置空
		});
	}
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