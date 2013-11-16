<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>变价调拨单管理</title>
<script type="text/javascript">
	$(function() {
		$("#jxctable").jxcGrid({
			colNames:['rid','商品号','商品名称','规格','颜色','单位','数量','单价','金额','备注'],
			colModel:[
					{name:'rid',editable:false,hidden:true,gridview:false}
				,	{name:'productId',editable:false,hidden:true,gridview:false}
				,	{name:'fullName',width:280,dataUrl:'product_listjson.action',completeGrid:productCompSet}
				,	{name:'specification',width:60,editable:false}
				,	{name:'color',width:60,editable:false}
				,	{name:'unit',width:50,editable:false}
				,	{name:'quantity',width:120,editrules:{number:true},total:true}
				,	{name:'price',width:120,editrules:{number:true}}
				,	{name:'amount',editrules:{number:true},total:true}
				,	{name:'memos',width:190}
			]
		});//进销存表格的参数
		
		var gridoptions={
			url:'productTransferDiff_listjson.action',
			caption:'变价调拨单管理',
			colNames:['','日期','单据编号', '调出仓', '调出经手人', '调入仓', '调入经手人','状态'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'billDate',width:100}
				,	{name:'item',fuzzySearch:true}
				,	{name:'outWarehouse.id',formatter:'outwf',completeGrid:warehouseCompSet}
				,	{name:'outEmployee.id',formatter:'outef',completeGrid:employeeCompSet}
				,	{name:'inWarehouse.id',formatter:'inwaf',completeGrid:warehouseCompSet}
				,	{name:'inEmployee.id',formatter:'inemf',completeGrid:employeeCompSet,}
				,	{name:'state',formatter:'statf',width:80}
			]
		};//主表的参数
		var naviPrm={
				parameter:{editfunc:editGrid,addfunc:addGrid,viewfunc:viewGrid},		//自定义的增删改查方法
				del:{url:'productTransferDiff_delete.action'}
			};
		var formatter=formatterFunc();
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
		$('#outWarehouseName,#inWarehouseName').completeGrid([{url:'warehouse_listjson.action'},{createInput:false}]);
		$('#outEmployeeName,#inEmployeeName').completeGrid([{url:'employee_listjson.action'},{createInput:false}]);
	});	
	
	function subData(condition,$formDiv,options){
		var $form=$('#mainform');
		$('#jxctable tr[id]').each(function(i){
	   		var rowData=$('#jxctable').getRowData(this.id);
	  		if(condition != 'add'&&rowData['productId']){
	  			$form.find('.billDetails').append("<tr class='details'><td><input type='hidden' name='details["+i+"].id' value='"+rowData['rid']+"'/></td>"+
	  				"<td><input type='hidden' name='details["+i+"].product.id' value='"+rowData['productId']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].quantity' value='"+rowData['quantity']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].amount' value='"+rowData['amount']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].memos' value='"+rowData['memos']+"' /></td></tr>");
	  	 	}
	 	});
		var valiFunc={
				beforeSerialize:function($form,options){
					var $error=$form.prev('.formError');
					var errorRow="";
					$('.details').each(function(i){
						var quantity=$(this).find('input[name*=quantity]').val(),
							amount=$(this).find('input[name*=amount]').val();
						if (!quantity||!amount){
							errorRow=i+1;
							return false;
						}
					});
					if (errorRow){
						$error.addClass('ui-state-error').text('第'+errorRow+'行录入有误！');
						return false;
					}
				}
		};													//提交之前的验证
		var newoptions=$.extend({},options,valiFunc);		//扩展默认的验证
		$form.unbind('submit').submit(function(){
			$form.ajaxSubmit(newoptions);
			return false;
		});
	}//提交表单
</script>
</head>

<body>
	<div class="data_page">
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id="list" actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>
	
	<!-- 自定义Form -->
	<div class="definedForm">
		<form id="mainform" action="productTransferDiff_save.action" method="post">
			<div class="add_del">
				<button class="add_product">添加商品</button>
				<button class="del_product">删除商品</button>
			</div>
			<table class="out_table">
				<tr>
	            	<td></td>
	            	<td></td>
	            	<td><label for="billDate">录单日期</label></td>
	            	<td>
	            		<input type="hidden" id="uid" name="id" />
	            		<input type="text" id="billDate" class="date currDate" name="billDate" readonly="readonly" />
	            	</td>
	            	<td><label for="item">单据编号</label></td>
	            	<td>
	            		<input type="text" id="item" name="item" readonly="readonly" url="productTransferDiff_getItem.action" />
	            	</td>
	            </tr>
	            <tr>
	            	<td><label for="outWarehouseName">调出仓</label></td>
	            	<td>
	            		<input type="hidden" id="outWarehouseId" name="outWarehouse.id" required=true title="调出仓" />
	            		<input type="text" id="outWarehouseName" name="outWarehouseName" />
	            	</td>
	            	<td><label for="outEmployeeName">调出经手人</label></td>
	            	<td>
	            		<input type="hidden" id="outEmployeeId" name="outEmployee.id" required=true title="调出经手人" />
	            		<input type="text" id="outEmployeeName" name="outEmployeeName" />
	            	</td>
	            	<td><label for="inWarehouseName">调入仓</label></td>
	            	<td>
	            		<input type="hidden" id="inWarehouseId" name="inWarehouse.id" required=true title="调入仓" />
	            		<input type="text" id="inWarehouseName" name="inWarehouseName" />
	            	</td>
	            </tr>
	            <tr>
					<td><label for="inEmployeeName">调入经手人</label></td>
					<td>
						<input type="hidden" id="inEmployeeId" name="inEmployee.id" required=true title="调入经手人" />
	            		<input type="text" id="inEmployeeName" name="inEmployeeName" />
					</td>
					<td><label for="memos">备注</label></td>
					<td>
						<input type="text" id="memos" name="memos" />
						<input type="hidden" id="taxRate" initValue="0" />
					</td>
	            </tr>
	        </table>
	        <!-- 表头结束 -->
	        <!-- 进销存表格 -->
	        <table id="jxctable"></table>
		</form>	
	</div>
</body>
</html>