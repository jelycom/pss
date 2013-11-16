<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>销售收款单管理</title>
<script type="text/javascript">
	$(function() {
		var lastSel;
		$('#jxctable').jxcGrid({
			colNames:['rid','单据编号','开单日期','总金额','未收金额','结算金额','备注','销售单号'],
			colModel:[
					{name:'rid',editable:false,hidden:true,gridview:false}
				,	{name:'masterItem',width:300,editable:false}
				,	{name:'masterDate',width:100,editable:false}
				,	{name:'amount',width:100,align:'right',editoptions:{readonly:true},total:true}
				,	{name:'arap',width:100,align:'right',editoptions:{readonly:true},total:true}
				,	{name:'currentPay',width:100,editrules:{number:true},total:true}
				,	{name:'memos'}
				,	{name:'productMasterID',editable:false,hidden:true,gridview:false}
			],
			onSelectRow:function(id){
				var $jxcGrid=$(this);
				var rowData=$jxcGrid.getRowData(id);
				if (!rowData["masterItem"]){
					lastSel="";
					$jxcGrid.resetSelection();
					return false;
				}
	        	if (!$(this).parents(".ui-dialog").is("[id^=view]")){
		        	var editRowSet={keys:false,									//行编辑时的参数
				        			oneditfunc:function(rowid){
				        				var $rowObj=$jxcGrid.find("#"+rowid);
				        				$jxcGrid.jqGrid("setSelection",rowid,false);
				        				inputMove($rowObj);
				    		        	var $sum = $("input[name=currentPay]",$rowObj);
				    		        	$rowObj.find(":text:last").keydown(function(e){				//行内最后一个输入框按键事件
				    		        		if (e.which==13||e.which==39){							//回车和右箭头保存编辑行并跳转到下一行
				    		        			if (!$rowObj.next()[0]){							//最后一行最后一格时添加一个空行并选中
				    		        				$jxcGrid.addRowData(Number(rowid)+1,{});
				    		        			}
				    		        			$jxcGrid.setSelection(Number(rowid)+1);
				    		        		}
				    		        	});
				    		        	$rowObj.find(":text:first").keydown(function(e){				//行内第一格时按左键头跳转到上一行
				    		        		if(e.which==37){
				    			        		$jxcGrid.setSelection(Number(rowid)-1);
				    			        		$rowObj.prev().find("input:last").trigger("focus").trigger("select");
				    		        		}
				    		        	});
			    		        		$sum.change(function(){
			    		        			var val=parseFloat($sum.val()).toFixed(2);
			    		        			$sum.val(val);
			    		        			calculateTotalrow($jxcGrid,$rowObj);
			    		        		});
				        			}//编辑时执行的操作
				        		};
		        	var $blankTr,gridData,currentData,editID;		//第一个空行、自动完成选中的行、当前选中行、需要编辑的行
		        	
		        	if (lastSel&&id!=lastSel){				//已有选中行时，保存已选中行
		        		$jxcGrid.saveRow(lastSel,{url:"clientArray"});
		        		currentData=getObjData($jxcGrid,id);
		        		$.each($jxcGrid.find("tr[id]"),function(i,n){					////找到第一个空行
							var rowVal=getObjData($jxcGrid,n.id);
							if (rowVal[1]==""){
								$blankTr=$(n);
								return false;
							}
						});
		        		editID=currentData[1]!=""?id:$blankTr.attr("id");			//选中行不为空时编辑该行,选中行为空时编辑第一个空行
	        		}else if(!lastSel){						//没有已选中行时
	        			$.each($jxcGrid.find("tr[id]"),function(i,n){					////找到第一个空行
							var rowVal=getObjData($jxcGrid,n.id);
							if (rowVal[1]==""){
								$blankTr=$(n);
								return false;
							}
						});
	        			currentData=getObjData($jxcGrid,id);
	        			editID=currentData[1]!=""?id:$blankTr.attr("id");		//本次选中行有数据时编辑该行,没有已选中行且本次选中行为空时，编辑第一个空白行
	        		}else if (id==lastSel)	return false;
		        	lastSel=editID;
		        	$jxcGrid.jqGrid("editRow",editID,editRowSet);
		        	
		        	$(document).unbind(".docEvent").bind("click.docEvent",function(e){				//在表格外点击保存正在编辑的行并取消选中
		        		if ($.inArray(e.target,$jxcGrid.find("*"))<0&&$jxcGrid.is(":visible")){
		        			if ($("#"+lastSel+" :text:first").val()!=""){
		        				$jxcGrid.jqGrid("saveRow",lastSel,{url:"clientArray"});
		        			}else {
								$jxcGrid.jqGrid("restoreRow",lastSel);
							}
		        			$jxcGrid.resetSelection();
		        			lastSel=undefined;
		        		}
		        		e.stopPropagation();
		        	});
		        	
		        	$jxcGrid.parents(".definedForm").on("dialogbeforeclose",function(){
		        		if (lastSel){
		        			$jxcGrid.restoreRow(lastSel).resetSelection();
		        			lastSel=undefined;
		        		}
		        	});	
	        	}
	        }
		});//进销存表格的参数
		
		var gridoptions={
			url:'accountIn_listjson.action',
			caption:'销售收款单管理',
			colNames:['','日期','单据编号','付款单位','经手人', '收款帐户','收款金额','优惠','状态'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'billDate',formatter:'date'}
				,	{name:'item',fuzzySearch:true}
				,	{name:'businessUnit.id',width:180,formatter:'unitf',completeGrid:unitCompSet}
				,	{name:'employee.id',formatter:'emplf',completeGrid:employeeCompSet,fuzzySearch:true,width:100}
				,	{name:'fundAccount.id',width:100,formatter:'fundf',edittype:'select',dataUrl:'fundAccount_listall.action',fuzzySearch:true}
				,	{name:'amount',formatter:'currency',width:120}
				,	{name:'discount',formatter:'currency',width:120}
				,	{name:'state',formatter:'statf',width:60}
			]
		};//主表的参数
		var naviPrm={
				parameter:{editfunc:editGrid,addfunc:addGrid,viewfunc:viewGrid,del:false},		//自定义的增删改查方法
				del:{url:'accountOut_delete.action'}
			};
		var formatter=formatterFunc();
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
		
		$('#businessUnitName,#employeeName').completeGrid([{},{createInput:false}]);
		$('.definedForm').bind('dialogopen',function(event,ui){
			var $extendbuttons = $("<div class='extend-buttons' style='float:left;'>"+
									"<button onclick='selectBill(\"productDelivery\")'>选择销售单</button>"+
									"<button id='viewBillButton'>查看销售单</button>"+
									"<button onclick='autoAllot()'>自动分配</button>"+
									"</div>");
			var $buttonDiv=$(this).next(); 
			if (!$buttonDiv.is(':has(.extend-buttons)')){
				$buttonDiv.prepend($extendbuttons);
				$('button',$buttonDiv).button();
			}
		});
		
		$('#viewBillButton').livequery('click',function(){
			var selrow=$('#jxctable').getGridParam('selrow');
			var billId=$('#jxctable').getCell(selrow,'productMasterID');
			if (!billId){
				return false;
			}else{
				viewBill(billId,$('#bill'),true);
			}
		});
		$('#businessUnitId').change(function(){			//供货单位改变后,清空表格中的数据
			resetGrid($('#jxctable'));
		});
	});	
	
	function subData(condition,$formDiv,options){
		var $form=$('#mainform');
		$('#jxctable tr[id]').each(function(i){
	   		var rowData=$('#jxctable').getRowData(this.id);
	  		if(condition!='add'&&rowData['masterItem']){
	  			$form.find('.billDetails').append("<tr class='details'><td><input type='hidden' name='details["+i+"].id' value='"+rowData['rid']+"' /></td>"+
	  					"<td><input type='hidden' name='details["+i+"].productMaster.id' value='"+rowData['productMasterID']+"' /></td>"+
	  				"<td><input type='hidden' name='details["+i+"].arap' value='"+rowData['arap']+"' /></td>"+
	  				"<td><input type='hidden' name='details["+i+"].currentPay' value='"+rowData['currentPay']+"'/></td>"+
	   				"<td><input type='hidden' name='details["+i+"].memos' value='"+rowData['memos']+"'/></td>"+
	   				"</tr>");
	  	 	}
	 	});
		var valiFunc={
				beforeSerialize:function($form,options){
					var $error=$form.prev('.formError'),
						$amount=$('#amount'),
						$discount=$('#discount'),
						$fundAccount=$('#fundAccountId'),
						amount=Number($amount.val()),
						discount=Number($discount.val());
					if (amount>0&&$fundAccount.val()=='-1'){
						$error.addClass('ui-state-error').text('请选择收款帐户');
						return false;
					}else if(amount+discount<=0){
						$error.addClass('ui-state-error').text('收款金额不正确');
						return false;
					}else if(amount+discount!=$('#jxctable').footerData('get').currentPay){
						$error.addClass('ui-state-error').text('收款金额与结算金额不相等');
						return false;
					}
					var errorRow="";
					$('.details').each(function(i){
						if (!$(this).find('input[name*=currentPay]').val()){
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
		<form id="mainform" action="accountIn_save.action" method="POST">
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
	            		<input type="text" id="item" name="item" readonly="readonly" url="accountIn_getItem.action" />
	            	</td>
	            </tr>
	            <tr>
	            	<td><label for="businessUnitName">付款单位</label></td>
	            	<td>
	            		<input type="hidden" id="businessUnitId" name="businessUnit.id" url="businessUnits_listjson.action" required=true title="收款单位" />
	            		<input type="text" id="businessUnitName" name="businessUnitName" />
	            	</td>
	            	<td><label for="contactorName">联系人</label></td>
	            	<td>
	            		<input type="hidden" id="contactorId" name="contactor.id" url="contacts_listjson.action" />
	            		<input type="text" id="contactorName" name="contactorName" initDisabled=1 />
	            	</td>
					<td><label for="employeeName">经手人</label></td>
					<td>
						<input type="hidden" id="employeeId" name="employee.id" url="employee_listjson.action" value="<s:property value="loginUser.id"/>" required=true title="经手人" />
						<input type="text" id="employeeName" name="employeeName" value='<s:property value="#session.loginUser.name"/>' />
					</td>
	            </tr>
	            <tr>
	            	<td>收款帐户</td>
	            	<td>
	            		<select id="fundAccountId" name="fundAccount.id">
	            		</select>
	                </td>
					<td><label for="amount">收款金额</label></td>
	            	<td><input type="text" class="number" id="amount" name="amount" /></td>
	            	<td><label for="discount">优惠</label></td>
	            	<td><input type="text" class="number" id="discount" name="discount" /></td>
	            </tr>
	            <tr>
	            	<td><label for="memos">备注</label></td>
	            	<td><input type="text" id="memos" name="memos" /></td>
	            </tr>
	        </table>
	        <!-- 表头结束 -->
	        <!-- 进销存表格 -->
	        <table id="jxctable"></table>
		</form>	
	</div>
</body>
</html>