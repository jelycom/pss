<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>帐户互转管理</title>
<script type="text/javascript">
	$(function() {
		$('#jxctable').jxcGrid({
			colNames:['rid','帐户号','帐户名称','金额','说明'],
			colModel:[
			      		{name:'rid',editable:false,hidden:true,gridview:false}
					,	{name:'productId',hidden:true,gridview:false,editable:false}
					,	{name:'fullName',width:300,completeGrid:fundAccountCompSet}
					,	{name:'subTotal',width:120,align:'right',editrules:{number:true}}
					,	{name:'memos'}
			]
		});//进销存表格的参数
		
		var gridoptions={
			url:'accountTransfer_listjson.action',
			caption:'帐户互转管理',
			colNames:['','日期','单据编号', '转出帐户', '经手人','总金额','手续费','状态'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'billDate',editable:false}
				,	{name:'item',fuzzySearch:true}
				,	{name:'fundAccount.name',width:180}
				,	{name:'employee.name'}
				,	{name:'amount'}
				,	{name:'cost'}
				,	{name:'state',formatter:'statf',width:80}
			]
		};//主表的参数
		var naviPrm={
				parameter:{editfunc:editGrid,addfunc:addGrid,viewfunc:viewGrid},		//自定义的增删改查方法
				del:{url:'accountTransfer_delete.action'}
			};
		var formatter=formatterFunc();
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
		$('#fundAccountName').completeGrid([{url:'fundAccount_listjson.action'},{createInput:false}]);
		$('#employeeName').completeGrid([{url:'employee_listjson.action'},{createInput:false}]);
	});	
	
	function subData(condition,$formDiv,options){
		var $form=$('#mainform'),
			$dataRow=$('#jxctable tr[id]');
		for (var i=0;i<$dataRow.length;i++){
			var id=$dataRow[i].id;
	   		var rowData=$('#jxctable').getRowData(id);
	  		if(condition != 'add'&&rowData['productId']){
	  			$form.find('.billDetails').append("<tr class='details'><td><input type='hidden' name='details["+i+"].id' value='"+rowData['rid']+"'/></td>"+
	  				"<td><input type='hidden' name='details["+i+"].inAccount.id' value='"+rowData['productId']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].inAmount' value='"+rowData['subTotal']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].memos' value='"+rowData['memos']+"' /></td></tr>");
	  	 	}
	 	}
		var valiFunc={
				beforeSerialize:function($form,options){
					var $error=$form.prev('.formError');
					var errorRow="";
					$('.details').each(function(i){
						var amount=$(this).find('input[name*=inAmount]').val();
						if (!amount){
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
		<form id="mainform" action="accountTransfer_save.action" method="post">
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
	            		<input type="text" id="item" name="item" readonly="readonly" url="accountTransfer_getItem.action" />
	            	</td>
	            </tr>
	            <tr>
	            	<td><label for="fundAccountName">转出帐户</label></td>
	            	<td>
	            		<input type="hidden" id="fundAccountId" name="fundAccount.id" required=true title="转出帐户" />
	            		<input type="text" id="fundAccountName" name="fundAccountName" />
	            	</td>
	            	<td><label for="employeeName">经手人</label></td>
	            	<td>
	            		<input type="hidden" id="employeeId" name="employee.id" required=true title="经手人" />
	            		<input type="text" id="employeeName" name="employeeName" />
	            	</td>
	            	<td><label for="amount">总金额</label></td>
	            	<td>
	            		<input type="text" id="amount" class="number" name="amount" />
	            	</td>
	            </tr>
	            <tr>
					<td><label for="cost">手续费</label></td>
					<td>
	            		<input type="text" id="cost" class="number" name="cost" />
					</td>
					<td><label for="memos">备注</label></td>
					<td>
						<input type="text" id="memos" name="memos" />
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