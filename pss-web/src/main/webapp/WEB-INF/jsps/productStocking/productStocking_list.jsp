<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>盘点单管理</title>
<script type="text/javascript">
	$(function() {
		var lastSel;
		var $jxcGrid=$('#jxctable').jxcGrid({
			colNames:['rid','商品号','商品名称','规格','颜色','单位','库存数量','盘点数量','盈亏数量','已盘','备注'],
			colModel:[
					{name:'rid',editable:false,hidden:true,gridview:false}
				,	{name:'productId',editable:false,hidden:true,gridview:false}
				,	{name:'fullName',width:280,completeGrid:productCompSet}
				,	{name:'specification',width:60,editable:false}
				,	{name:'color',width:60,editable:false}
				,	{name:'unit',width:50,editable:false}
				,	{name:'inventoryQuantity',width:120,align:'right',editoptions:{readonly:true},total:true}
				,	{name:'checkQuantity',width:120,editrules:{number:true},total:true}
				,	{name:'quantity',width:120,align:'right',editoptions:{readonly:true},total:true}
				,	{name:'complete',editable:false,hidden:true,gridview:false}
				,	{name:'memos',width:180}
			],
			blankrows:10,			//空行
			onSelectRow:function(id){
				var $jxcGrid=$(this),
					options=$jxcGrid[0].p;
				if (!$('#warehouseName').val()){
					$jxcGrid.resetSelection();
					lastSel=undefined;
					alert('请先选择要盘点的仓库！');
					return false;
				}
				if (!$(this).parents(".ui-dialog").is("[id^=view]")){
	        		var editRowSet={keys:false,									//行编辑时的参数
	        			oneditfunc:function(rowid){
	        				var $rowObj=$jxcGrid.find("#"+rowid);
	        				var $name=$(":text[id$=ame]",$rowObj);
	        				var blankData={};
	        				$.each(options.colModel,function(i,n){
	        					blankData[n.name]="";
	        				});
	        				$jxcGrid.jqGrid("setSelection",rowid,false);
	        				inputMove($rowObj);
	        				//商品改变时自动填充资料
	        				$name.change(function(){
	        					var $completeGrid=$(".completeDiv table[id]"),
	        						selrow=$completeGrid.getGridParam("selrow"),
	        						colNames=$completeGrid[0].p.colNames,
	        						colModel=$completeGrid[0].p.colModel,
	        						colIndex=[];
	        					$.each(colModel,function(i,n){
	        						colIndex.push(n.name);			//将列名称存为数组，以便获取索引
	        					});
	        					if (!$(this).val()){				//删除名称时清除这一行
	        						$jxcGrid.delRowData(rowid);
	        						$jxcGrid.addRowData(rowid,blankData,"last");
	        						$('.completeDiv').hide();
	        						calculateTotalrow();
	        						return false;
	        					}
	        					gridData=$completeGrid.getRowData(selrow);
	        					$.each(gridData,function(i,n){
	        						if (i=="id"){
	        							$("td[aria-describedby*=productId]",$rowObj).text(n);
	        							var warehouseId=$("#warehouseId").val();					//获取仓库ID
	        							if (warehouseId){											//如果有仓库ID请求获取仓库下该商品的库存数量并显示
	        								$.ajax({
		        							    url:'productStock_findquantity.action',
		        							    type:'post',
		        							    data:{warehouseId:warehouseId,productId:n},
		        							    success:function(data){
		        							    	if (data&&data.resultObj)	$(":text[name=inventoryQuantity]",$rowObj).val(data.resultObj);
		        							    	calculateTotalrow($jxcGrid,$rowObj);
		        							    }
		        							});
	        							}
	        						}else{
										var $td=$("td[aria-describedby*=_"+i+"]",$rowObj);
	        							$td.children().is(":text")?$td.children().val(n):$td.text(n);
	        						}
	        					});
	        				});
	    		        	var $checkQuantity=$("input[name=checkQuantity]",$rowObj),
	    		        		$quantity=$('input[name=quantity]',$rowObj),
	    		        		$inventoryQuantity=$('input[name=inventoryQuantity]',$rowObj),
	    		        		$complete=$('td[aria-describedby*=complete]',$rowObj);
	    		        	$rowObj.find(":text:last").keydown(function(e){								//行内最后一个输入框按键事件
	    		        		if (e.which==13||e.which==39){											//回车和右箭头保存编辑行并跳转到下一行
	    		        			if (!$rowObj.next()[0])	$jxcGrid.addRowData(Number(rowid)+1,{});	//最后一行最后一格时添加一个空行并选中
	    		        			$jxcGrid.setSelection(Number(rowid)+1);
	    		        		}
	    		        	});
	    		        	$rowObj.find(":text:first").keydown(function(e){				//行内第一格时按左键头跳转到上一行
	    		        		if(e.which==37){
	    			        		$jxcGrid.setSelection(Number(rowid)-1);
	    			        		$rowObj.prev().find("input:last").trigger("focus").trigger("select");
	    		        		}
	    		        	});
    		        		$checkQuantity.change(function(){								//数量变化时
    		        			var checkQuantity=$checkQuantity.val(),
    		        				inventoryQuantity=$inventoryQuantity.val();
    		        			if (checkQuantity!='')	$quantity.val(checkQuantity-inventoryQuantity).next().text('true');	//计算盈亏数量并存已盘标记
    		        			else	$quantity.val('').next().text('');					//清空盈亏数量并清除已盘标记
    		        			calculateTotalrow($jxcGrid,$rowObj);
    		        		});
	    		        	function calculation(mode){
	    		        		var total={};
	    		        		total.quantity=$jxcGrid.getCol("quantity",false),
	    		        		total.checkQuantity=$jxcGrid.getCol("checkQuantity",false),
	    		        		total.inventoryQuantity=$jxcGrid.getCol("inventoryQuantity",false);
	    		        		var totalQuantity=!mode?Number($quantity.text()):0,
	    		        			totalInventory=!mode?Number($inventoryQuantity.text()):0,
	    		        			totalCheck=!mode?Number($checkQuantity.val()):0;
	    		        		for (var i=0;i<total['checkQuantity'].length;i++){
	    		        			if (isNaN(Number(total['checkQuantity'][i]))==false){
	    		        				totalQuantity+=Number(total["quantity"][i]);
	    		        				totalInventory+=Number(total["inventoryQuantity"][i]);
	    		        				totalCheck+=Number(total["checkQuantity"][i]);
	    		        			}
	    		        		}
	    		        		$jxcGrid.footerData("set",{quantity:totalQuantity,checkQuantity:totalCheck,inventoryQuantity:totalInventory});
	    		        	}//计算合计，单价、数量改变时，保存行数据时要用到
	        			}//编辑时执行的操作
	        		};
		        	var $blankTr,gridData,currentData,editID;		//第一个空行、自动完成选中的行、当前选中行、需要编辑的行
		        	
		        	if (id!=lastSel&&lastSel){				//已有选中行时，保存已选中行
		        		if(!$("#"+lastSel+"_fullName").val()){
		        			$jxcGrid.restoreRow(lastSel);	//商品为空时，恢复成空行
		        			var total=calculateTotalrow();
		        			inputCalculate(total);
		        		}else{
		        			$jxcGrid.saveRow(lastSel,{url:"clientArray",aftersavefunc:aftersave});
		        		}
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
		        		if (!$jxcGrid.find(e.target)[0]&&$jxcGrid.is(":visible")){
		        			if ($("#"+lastSel+" :text:first").val()!="")
		        				$jxcGrid.jqGrid("saveRow",lastSel,{url:"clientArray",aftersavefunc:aftersave});
		        			else	$jxcGrid.jqGrid("restoreRow",lastSel);
		        			$jxcGrid.resetSelection();
		        			lastSel=undefined;
		        		}
		        		e.stopPropagation();
		        	});
		        	$jxcGrid.parents(".definedForm").on("dialogbeforeclose",function(){
		        		if (lastSel)	$jxcGrid.restoreRow(lastSel).resetSelection();
	        			lastSel=undefined;
		        	});	
	        	}
			}
		});//进销存表格的参数
		
		var gridoptions={
			url:'productStocking_listjson.action',
			caption:'盘点单管理',
			colNames:['','录单日期','单据编号','盘点仓库','经手人','状态'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'billDate',formatter:'date'}
				,	{name:'item',fuzzySearch:true}
				,	{name:'warehouse.id',formatter:'waref',completeGrid:warehouseCompSet,fuzzySearch:true}
				,	{name:'employee.id',formatter:'emplf',completeGrid:employeeCompSet,fuzzySearch:true,width:100}
				,	{name:'state',formatter:'statf',width:60}
			]
		};//主表的参数
		var naviPrm={
				parameter:{editfunc:editGrid,addfunc:addGrid,viewfunc:viewGrid},		//自定义的增删改查方法
				del:{url:'productStocking_delete.action'}
			};
		var formatter=formatterFunc();
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
		
		$('#employeeName,#warehouseName').completeGrid([{},{createInput:false}]);
		$('#warehouseName').change(function(){
			resetGrid($jxcGrid);					//仓库改变清空表格
		});
		$('.definedForm').bind('dialogopen',function(event,ui){
			var $extendbuttons=$("<div class='extend-buttons' style='float:left;'>"+
									"<button id='selectProductType' onclick='selectPT()'>添加商品种类</button>"+
									"</div>");
			var $buttonDiv=$(this).next(),
				dialogHeight=$(this).height(),
				outTableH=$('.out_table',this).outerHeight(true),
				$grid=$('#jxctable',this),
				hdivH=$($grid[0].grid.hDiv).outerHeight(true),				//标题单元格行的高度
				sdivH=$($grid[0].grid.sDiv).outerHeight(true);				//脚部行的高度
			$grid.setGridHeight(dialogHeight-outTableH-hdivH-sdivH-6);
			if (!$buttonDiv.is(':has(.extend-buttons)')){
				$buttonDiv.prepend($extendbuttons);
				$('button',$buttonDiv).button();
			}
		});//给表单添加自定义按钮
	});
	
	function aftersave(rid){
		var $selrow=$('#'+rid,this).css('color','black');
		if (!$(this).getCell(rid,'quantity'))	return false;
		var quantity=Number($(this).getCell(rid,'quantity'));
		if (quantity<0)	$selrow.css('color','green');
		if (quantity>0)	$selrow.css('color','red');
		if (quantity==0)$selrow.css('color','blue');
	}//行编辑保存后根据盈亏数量设置字体颜色
	
	function selectPT(){
		var wareName=$('#warehouseName').val();
		if (!wareName)	return false;
		var $selectPT=$('#selectPT');
		if (!$('body').is(':has(#selectPT)')){
			$selectPT=$('<div id="selectPT"><p class="error"></p><div class="gridTableDiv"><table id="productType"></table></div></div>');
			$('body').append($selectPT);
			var $productType=$('#productType'),
				$error=$('.error',$selectPT);
			$selectPT.dialog({autoOpen:false,modal:true,width:240,minWidth:200,maxHeight:400,resizable:true,minHeight:false,title:'请选择要添加的商品种类',
				buttons:{
					'确定':function(){
						var selrow=$productType.getGridParam('selrow');
						if (!selrow){
							$error.addClass('ui-state-error').text('没有选中分类!');
						}else{
							var rc=new Rc($('#'+selrow,$productType),$productType),
								wareId=$('#warehouseId').val();
							$.ajax({
							    url:'productStock_findRealStockQuantity.action',
							    type:'post',
							    dataType:'json',
							    data:{'productTypeId':rc._id_,'warehouseId':wareId},
 							    success:function(data){
							        if (data&&data.operate){
							        	var result=data.resultObj,
							        		$jxcGrid=$('#jxctable');
							        	var tr=$jxcGrid.find("tr[id]").filter(function(index){
											var rowVal=getObjData($jxcGrid,this.id);
											return rowVal[1]=="";
										});
										//没有空行时id为最后一行id+1,有空行时id为空行id
										var id=!tr[0]?Number($jxcGrid.find("tr[id]:last").attr("id"))+1:Number(tr[0].id);
										var exist=$jxcGrid.getCol('productId',false),	//已存在的ID
											once=0;										//警告次数
							        	for ( var i=0,j=0; i<result.length; i++) {
							        		if ($.inArray(result[i].product.id.toString(),exist)>=0){
							        			if (!once){
							        				alert('添加的商品已存在！');
								        			once++;
							        			}
							        		}else{
							        			var rowData={};
												$.each(result[i],function(k,n){
													switch (k){
														case 'product':
															$.each(n,function(x,y){
																x!='id'?rowData[x]=y:rowData['productId']=y;	//添加产品信息，产品id特殊处理
															});
															break;
														case 'quantity':
															rowData['inventoryQuantity']=n;
															break;
														default:
															break;
													}
												});
												$jxcGrid[!$("#"+(id+j),$jxcGrid)[0]?"addRowData":"setRowData"](id+j,rowData);
												j++;
							        		}
										}
										calculateTotalrow();
							        }
							    }
							});
							$(this).dialog('close');
						}
					},
					'返回':function(){
						$(this).dialog('close');
					}
				},
				create:function(){
					var $button=$(this).nextAll('.ui-dialog-buttonpane');
					$button.prepend('<input type="text" id="filterTree" style="margin:.8em .2em .5em 0;" />');
					$button.find(':button:first').addClass('acceptSelect');
					$productType.treeTable({
						url:'productType_treejson.action',
						caption:false,
						pager:false,
						scroll:10,
						onSelectRow:null,
			            ondblClickRow:function(rowid,iRow,iCol,e){					//双击行产生的事件，参数rowid为行ID，iRow为行索引，iCol为列索引，e为事件对象
							$('.acceptSelect').click();
						},
						loadComplete:function(){
							$(this.grid.hDiv).hide();
						}
					});
 					$('#filterTree').keyup(function(){
						searchTreegrid($(this),$productType);
					});
				},
				open:function(){
					if ($('#filterTree').val())	$('#filterTree').val('').keyup();
					gridSize($productType);
				},
				resizeStop:function(){
					gridSize($productType);
				}
			});
		}
		$selectPT.dialog('open');
	}//选择商品种类，自动添加商品
	
	function subData(condition,$formDiv,options){
		var $form=$('#mainform'),
			$details=$form.find('.billDetails');
		$('#jxctable tr[id]').each(function(i){
	   		var rd=$('#jxctable').getRowData(this.id);
	  		if(condition!='add'&&rd['productId']){
	  			$details.append("<tr class='details'><td><input type='hidden' name='details["+i+"].id' value='"+rd['rid']+"'/></td>"+
		  				"<td><input type='hidden' name='details["+i+"].product.id' value='"+rd['productId']+"'/></td>"+
		   				"<td><input type='hidden' name='details["+i+"].quantity' value='"+rd['quantity']+"' /></td>"+
		   				"<td><input type='hidden' name='details["+i+"].memos' value='"+rd['memos']+"'/></td>"+
		   				"<td><input type='hidden' name='details["+i+"].complete' value='"+rd['complete']+"'/></td>"+
	   				"</tr>");
	  	 	}
	 	});
		var valiFunc={
				beforeSerialize:function($form,options){}
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
	<div class="definedForm" dialogHeight="100%" dialogWidth="100%">
		<form id="mainform" action="productStocking_save.action" method="POST">
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
	            		<input type="text" id="item" name="item" readonly="readonly" url="productPurchase_getItem.action" />
	            	</td>
	            </tr>
	            <tr>
	            	<td><label for="warehouseName">盘点仓库</label></td>
					<td>
						<input type="hidden" id="warehouseId" name="warehouse.id" url="warehouse_listjson.action" required=true title="盘点仓库" />
						<input type="text" id="warehouseName" name="warehouseName" />
					</td>
					<td><label for="employeeName">经手人</label></td>
					<td>
						<input type="hidden" id="employeeId" name="employee.id" url="employee_listjson.action" value="<s:property value="loginUser.id"/>" required=true title="经手人" />
						<input type="text" id="employeeName" name="employeeName" value='<s:property value="#session.loginUser.name"/>' />
					</td>
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