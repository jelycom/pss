<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>采购计划单管理</title>
<script type="text/javascript">
	$(function() {
		$('#jxctable').jxcGrid({
			colNames:['rid','商品号','商品名称','规格','颜色','单位','数量','说明'],
			colModel:[
			      		{name:'rid',editable:false,hidden:true,gridview:false}
					,	{name:'productId',editable:false,hidden:true,gridview:false}
					,	{name:'fullName',width:300,completeGrid:productCompSet}
					,	{name:'specification',width:60,editable:false}
					,	{name:'color',width:60,editable:false}
					,	{name:'unit',width:60,editable:false}
					,	{name:'quantity',width:120,align:'right',editrules:{number:true},total:true}
					,	{name:'memo'}
			]
		});//进销存表格的参数
		
		var gridoptions={
			url:'productPlanPurchase_listjson.action',
			caption:'采购计划单管理',
			colNames:['','单据编号', '计划开始日期', '计划结束日期', '计划提出人员', '计划执行人员','状态'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'item',fuzzySearch:true}
				,	{name:'startDate',formatter:'date'}
				,	{name:'endDate',formatter:'date'}
				,	{name:'planEmployee.id',width:100,formatter:'planf',completeGrid:employeeCompSet,fuzzySearch:true}
				,	{name:'executeEmployee.id',width:100,formatter:'execf',completeGrid:employeeCompSet,fuzzySearch:true}
				,	{name:'state',formatter:'statf',width:80}
			]
		};//主表的参数
		var naviPrm={
				parameter:{editfunc:editGrid,addfunc:addGrid,viewfunc:viewGrid},		//自定义的增删改查方法
				del:{url:'productPlanPurchase_delete.action'}
			};
		var formatter=formatterFunc();
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
		$('#planEmployeeName,#executeEmployeeName').completeGrid([{},{createInput:false}]);
	});	
	
	function subData(condition,$formDiv,options){
		var $form=$('#mainform');
		$('#jxctable tr[id]').each(function(i){
	   		var rowData=$('#jxctable').getRowData(this.id);
	  		if(condition!='add'&&rowData['productId']){
	  			$form.find('.billDetails').append("<tr class='details'><td><input type='hidden' name='details["+i+"].id' value='"+rowData['rid']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].product.id' value='"+rowData['productId']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].quantity' value='"+rowData['quantity']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].memo' value='"+rowData['memo']+"' /></td></tr>");
	  	 	}
	 	});
		var valiFunc={
				beforeSerialize:function($form,options){
					var $error=$form.prev('.formError');
					if ($form[0].endDate.value<$form[0].startDate.value){
						$error.addClass('ui-state-error').text('结束日期小于开始日期！');
						return false;
					}
					var errorRow="";
					$('.details').each(function(i){
						var quantity=$(this).find('input[name*=quantity]').val();
						if (!quantity){
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
		<form id="mainform" action="productPlanPurchase_save.action" method="post">
			<div class="add_del">
				<button class="add_product">添加商品</button>
				<button class="del_product">删除商品</button>
			</div>
			<table class="out_table" id="form_header">
				<tr>
	            	<td><label for="billDate">录单日期</label></td>
	            	<td>
	            		<input type="hidden" id="uid" name="id" />
	            		<input type="text" id="billDate" class="date currDate" name="billDate" readonly="readonly" />
	            	</td>
	            	<td><label for="item">单据编号</label></td>
	            	<td>
	            		<input type="text" id="item" name="item" readonly="readonly" url="productPlanPurchase_getItem.action" />
	            	</td>
	            </tr>
	            <tr>
	            	<td><label for="startdate">计划开始日期</label></td>
	            	<td>
	            		<input type="text" class="date currDate" id="startDate" name="startDate" readonly="readonly" />
	            	</td>
	            	<td><label for="enddate">计划结束日期</label></td>
	            	<td>
	            		<input type="text" class="date" id="endDate" name="endDate" readonly="readonly" />
	            	</td>
	            </tr>
	            <tr>
					<td><label for="planEmployeeName">计划提出人员</label></td>
					<td>
						<input id="planEmployeeId" name="planEmployee.id" url="employee_listjson.action" type="hidden" value="<s:property value="loginUser.id"/>" required=true title="计划提出人员" />
						<input id="planEmployeeName" name="planEmployeeName" type="text" value='<s:property value="#session.loginUser.name"/>'/>
					</td>
					<td><label for="executeEmployeeName">计划执行人员</label></td>
					<td>
						<input id="executeEmployeeId" name="executeEmployee.id" url="employee_listjson.action" type="hidden" />
						<input type="text" id="executeEmployeeName" name="executeEmployeeName" />
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