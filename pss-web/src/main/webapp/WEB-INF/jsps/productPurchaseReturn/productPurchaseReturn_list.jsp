<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>采购退货单管理</title>
<script type="text/javascript">
	$(function() {
		$('#jxctable').jxcGrid({
			colNames:['rid','商品号','商品名称','规格','颜色','单位','数量','单价','金额','含税单价','税金','价税合计','备注'],
			colModel:[
					{name:'rid',editable:false,hidden:true,gridview:false}
				,	{name:'productId',editable:false,hidden:true,gridview:false}
				,	{name:'fullName',width:280,completeGrid:productCompSet}
				,	{name:'specification',width:60,editable:false}
				,	{name:'color',width:60,editable:false}
				,	{name:'unit',width:50,editable:false}
				,	{name:'quantity',width:100,editrules:{number:true},total:true}
				,	{name:'price',width:120,editrules:{number:true}}
				,	{name:'subTotal',editrules:{number:true},total:true}
				,	{name:'taxPrice',width:120,editrules:{number:true},editoptions:{readonly:true}}
				,	{name:'tax',width:120,editrules:{number:true},editoptions:{readonly:true},total:true}
				,	{name:'amount',editrules:{number:true},total:true}
				,	{name:'memos',width:190}
			]
		});//进销存表格的参数
		
		var gridoptions={
			url:'productPurchaseReturn_listjson.action',
			caption:'采购退货管理',
			colNames:['','录单日期','单据编号','收货单位','联系人','经手人', '发货仓库','总金额','状态'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'billDate',formatter:'date'}
				,	{name:'item',fuzzySearch:true}
				,	{name:'businessUnit.id',formatter:'unitf',completeGrid:unitCompSet}
				,	{name:'contactor.name',hidden:true}
				,	{name:'employee.id',formatter:'emplf',completeGrid:employeeCompSet,width:100}
				,	{name:'warehouse.id',formatter:'waref',completeGrid:warehouseCompSet}
				,	{name:'amount',formatter:'currency',width:100}
				,	{name:'state',formatter:'statf',width:60}
			]
		};//主表的参数
		var naviPrm={
				parameter:{editfunc:editGrid,addfunc:addGrid,viewfunc:viewGrid},		//自定义的增删改查方法
				del:{url:'productPurchaseReturn_delete.action'}
			};
		var formatter=formatterFunc();
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
		
		$('#businessUnitName,#warehouseName').completeGrid([{
				colNames:["id","编号","简称","拼音"],
				colModel:[{name:"id",hidden:true},{name:"item",width:"40%"},{name:"name",width:"60%"},{name:"py",hidden:true}]
			},{createInput:false,reload:false}]);
		$('#employeeName').completeGrid([{},{createInput:false}]);
		$('.definedForm').bind('dialogopen',function(event,ui){
			var $extendbuttons = $("<div class='extend-buttons' style='float:left;'>"+
									"<button onclick='selectBill(\"productPurchase\",true)'>选择进货单</button>"+
									"</div>")
			var $buttonDiv = $(this).next(); 
			if (!$buttonDiv.is(':has(.extend-buttons)')){
				$buttonDiv.prepend($extendbuttons);
				$('button',$buttonDiv).button();
			}
		});//添加自定义按钮
		$('#businessUnitId').change(function(){			//供货单位改变后,清空表格中的数据
			$("#advance").val('');
			resetGrid($('#jxctable'));
			inputCalculate();
		});
	});	
	
	function subData(condition,$formDiv,options){
		var $form=$('#mainform'),
			$details=$form.find('.billDetails');
		$('#jxctable tr[id]').each(function(i){
	   		var rowData=$('#jxctable').getRowData(this.id);
	  		if(condition != 'add'&&rowData['productId']){
	  			$details.append("<tr class='details'><td><input type='hidden' name='details["+i+"].id' value='"+rowData['rid']+"'/></td>"+
	  				"<td><input type='hidden' name='details["+i+"].product.id' value='"+rowData['productId']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].quantity' value='"+rowData['quantity']+"' /></td>"+
	   				"<td><input type='hidden' name='details["+i+"].unitPrice' value='"+rowData['price']+"' /></td>"+
	   				"<td><input type='hidden' name='details["+i+"].subTotal' value='"+rowData['subTotal']+"' /></td>"+
	   				"<td><input type='hidden' name='details["+i+"].taxPrice' value='"+rowData['taxPrice']+"' /></td>"+
	   				"<td><input type='hidden' name='details["+i+"].tax' value='"+rowData['tax']+"' /></td>"+
	   				"<td><input type='hidden' name='details["+i+"].amount' value='"+rowData['amount']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].memos' value='"+rowData['memos']+"'/></td>"+
	   				"</tr>");
	  	 	}
	 	});
		var totalData=$('#jxctable').footerData('get');
		if ($details.find('tr')[0])
			$details.append('<tr><td><input type="hidden" name="subToal" value="'+totalData.subTotal+'" /></td>'+
				'<td><input type="hidden" name="valueAddTax" value="'+totalData.tax+'" /></td>'+
				'<td><input type="hidden" name="amount" value="'+totalData.amount+'" /></td></tr>');
		var valiFunc={
				beforeSerialize:function($form,options){
					var $error=$form.prev('.formError');
					if ($form[0].paid.value>0){
 						if ($form[0]['fundAccount.id'].value<0){
							$error.addClass('ui-state-error').text('请选择帐户！');
							return false;
 						}
					}else if($form[0].paid.value<0){
						$error.addClass('ui-state-error').text('付款金额不正确！');
						return false;
					}
					var errorRow="";
					$('.details').each(function(i){
						var quantity=$(this).find('input[name*=quantity]').val(),
							subTotal=$(this).find('input[name*=subTotal]').val();
						if (!quantity||!subTotal){
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
		<form id="mainform" action="productPurchaseReturn_save.action" method="POST">
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
	            		<input type="text" id="item" name="item" readonly="readonly" url="productPurchaseReturn_getItem.action" />
	            	</td>
	            </tr>
	            <tr>
	            	<td><label for="businessUnitName">收货单位</label></td>
	            	<td>
	            		<input type="hidden" id="businessUnitId" name="businessUnit.id" url="businessUnits_listjson.action" required=true title="供货单位" />
	            		<input type="text" id="businessUnitName" name="businessUnitName" />
	            	</td>
	            	<td><label for="contactorName">联系人</label></td>
	            	<td>
	            		<input type="hidden" id="contactorId" name="contactor.id" />
	            		<input type="text" id="contactorName" name="contactorName" initDisabled=1 />
	            	</td>
	            	<td><label for="warehouseName">发货仓库</label></td>
					<td>
						<input type="hidden" id="warehouseId" name="warehouse.id" url="warehouse_listjson.action" required=true title="收货仓库" />
						<input type="text" id="warehouseName" name="warehouseName" />
					</td>
	            </tr>
	            <tr>
					<td><label for="employeeName">经手人</label></td>
					<td>
						<input type="hidden" id="employeeId" name="employee.id" url="employee_listjson.action" value="<s:property value="loginUser.id"/>" required=true title="经手人" />
						<input type="text" id="employeeName" name="employeeName" value='<s:property value="#session.loginUser.name"/>' />
					</td>
					<td><label for="invoiceName">发票类型</label></td>
					<td>
						<input type="hidden" id="invoiceTypeId" name="invoiceType.id" />
						<input type="text" id="invoiceName" name="invoiceName" />
					</td>
					<td><label for="invoiceNo">发票号码</label></td>
					<td><input type="text" id="invoiceNo" name="invoiceNo" /></td>
	            </tr>
	            <tr>
	            	<td><label for="info">附加信息</label></td>
	            	<td><input type="text" id="info" name="info" /></td>
	            	<td><label for="memos">备注</label></td>
	            	<td><input type="text" id="memos" name="memos" /></td>
	            </tr>
	        </table>
	        <!-- 表头结束 -->
	        <!-- 进销存表格 -->
	        <table id="jxctable"></table>
	        <!--- 数量金额计算及保存打印 -->
	        <table class="out_table">
	        	<tr>
	                <td><label for="advance">此单预收</label></td>
	                <td><input type="text" id="advance" name="advance" readonly="readonly" /></td>
	            	<td>收款帐户</td>
	            	<td>
	            		<select id="fundAccountId" name="fundAccount.id">
	            		</select>
	                </td>
	                <td><label for="paid">实收金额</label></td>
	                <td><input type="text" id="paid" name="paid" /></td>
	            </tr>
	        </table>
		</form>	
	</div>
</body>
</html>