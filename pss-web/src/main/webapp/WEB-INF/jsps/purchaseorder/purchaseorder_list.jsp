<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>采购订单管理</title>
<script type="text/javascript">

	function subData(condition){    
		var editid = "";
	 	for (var i=1;i<=$("#jxctable tr[id]").length;i++){
   		var rowData = $("#jxctable").getRowData(i);
  		if(condition != "add"){
  			editid = rowData['id'];
  	 	 }
   		$("#mainform").append("<tr><td><input type='hidden' name='details["+(i-1)+"].id' value='"+editid+"'/><td/><tr/>"+
		    		"<tr><td><input type='hidden' name='details["+(i-1)+"].quantity' value='"+rowData['pquantity']+"'/><td/><tr/>"+
		    		"<tr><td><input type='hidden' name='details["+(i-1)+"].taxPrice' value='"+rowData['price']+"'/><td/><tr/>"+
		    		"<tr><td><input type='hidden' name='details["+(i-1)+"].amount' value='"+rowData['sum']+"'/><td/><tr/>"+
   				"<tr><td><input type='hidden' name='details["+(i-1)+"].product.id' value='"+rowData['pname']+"'/><td/><tr/>");
	 	} 
		$("#mainform").submit();
	}

	function showData() {
		$.ajax({
			type:"post",
			dataType:"json",
			data:{"id":$("#list").jqGrid('getGridParam','selrow')},
			url:"purchaseorder_viewData",
			error:function(){
				alert("暂时无法获取数据，请与管理员联系！");
			},
			success:function(data){
				$("#uid").val(data[0].id);
				$("#deliveryDate").val(data[0].deliveryDate);  
				$("#deliveryId").val(data[0].deliveryId)
				$("#supplier").val(data[0].businessunit.shortName);		
				$("#businessunitsId").val(data[0].businessunit.id);
				$("#contactor").val(data[0].contactor.name);
				$("#contactorId").val(data[0].contactor.id);
				$("#storage").val(data[0].warehouse.name);
				$("#warehouseId").val(data[0].warehouse.id);
				$("#handle").val(data[0].employee.name);
				$("#employeeId").val(data[0].employee.id);
				$("#remark").val(data[0].memos);
				$("#taxes").val(data[0].valueAddTax);
				$("#jiasui").val(data[0].amount);
				for(var i=1;i<data.length;i++){
				$("#jxctable").delRowData(i);
				$("#jxctable").addRowData(""+i+"", {"id":""+data[i].id+"","pname":""+data[i].product.id+"",
					"view":""+data[i].product.fullName+"","price": ""+data[i].taxPrice+"",
					"pquantity": ""+data[i].quantity+"","sum": ""+data[i].amount+""}, "first"); 
				}
				calculateTotalrow();//计算合计行
			}
		});
	}

	$(function() {
		$("#list").bindTable({deptf:function(cellData,options,rowObject){
	//		if (rowObject["region"]) {
	//			return rowObject["region"].name;
	//			}
				return "";
			},
			delf:"checkbox"});
		
		$("#definedForm").bind("dialogopen",function(event,ui){
				var $extendbuttons = $("<div class='extend-buttons' style='float:left;'>"+
										"<button onclick='selectPlan()'>选择计划单</button>"+
										"</div>")
				var $buttonDiv = $(this).next(); 
				if (!$buttonDiv.is(":has(.extend-buttons)")){
					$buttonDiv.prepend($extendbuttons);
					$("button",$buttonDiv).button();
				}
		});

		$("#add_list,#edit_list").click(function(){
			if(this.id=="add_list"){
				$.ajax({
    				type:"post",
    				dataType:"json",
    				url:"purchaseorder_getItem",
    				error:function(){
    					alert("生成单据编号错误!");
    				},
    				success:function(data){
        				$("#deliveryId").val(data.item);			
    				}
    			});
			}
			if(this.id=="edit_list"){
					showData();
				}
			if (!$("#editmodlist").is(":has(#form_header)")){
				$("#handle").autocomplete("employee_query",
				   		  { 
							extraParams:{value:function(){return $("#handle").val();}},   
				         	matchSubset: false,
				         	autoFill: true, 
				        	matchContains: true, 
				         	autoFill: false,
				         	dataType:'json',
				        	parse:function(data){   
				    		var rows=new Array();	
				        	for(var i=0;i<data.length;i++){
				             rows[rows.length]={   
				                 data:data[i].name,  
				                 value:data[i].id,//值 
				                 result:data[i].name//返回结果   
				            };
				         } 
				         return rows;  
				    },   
				    formatItem:function(row) {
				         return row; 
				    }
				  }).result(function(row,i,n){
					  	getBranch(n);
				   	   	$("#employeeId").val(n);
				  });

				function getBranch(id) {
		 			$.ajax({
		    				type:"post",
		    				dataType:"json",
		    				data:{"id":id,"other":"other"},
		    				url:"employee_save",
		    				error:function(){
		    					alert("服务器正忙，请稍后再试！");
		    				},
		    				success:function(data){
		    					$("#branch").val(data[0].department.name);
		    				}
		    			});
		 		}
				
				$("#storage").autocomplete("warehouse_query",
				   		  { 
							extraParams:{value:function(){return $("#storage").val();}},   
				         	matchSubset: false,
				         	autoFill: true, 
				        	matchContains: true, 
				         	autoFill: false,
				         	dataType:'json',
				        	parse:function(data){   
				    		var rows=new Array();	 
				        	for(var i=0;i<data.length;i++){
				             rows[rows.length]={   
				                 data:data[i].name,  
				                 value:data[i].id,//值 
				                 result:data[i].name//返回结果
				            };
				         } 
				         return rows;  
				    },   
				    formatItem:function(row) {
				         return row; 
				    }
				   }).result(function(row,i,n){
					   	   	$("#warehouseId").val(n);
				  });
				
				$("#contactor").autocomplete("contactor_query",
				   		  { 
							extraParams:{value:function(){return $("#contactor").val();}},   
				         	matchSubset: false,
				         	autoFill: true, 
				        	matchContains: true, 
				         	autoFill: false,
				         	dataType:'json',
				        	parse:function(data){   
				    		var rows=new Array();	 
				        	for(var i=0;i<data.length;i++){
				             rows[rows.length]={   
				                 data:data[i].name,  
				                 value:data[i].id,//值 
				                 result:data[i].name//返回结果
				            };
				         } 
				         return rows;  
				    },   
				    formatItem:function(row) {
				         return row; 
				    }
				   }).result(function(row,i,n){
					   	   	$("#contactorId").val(n);
				  });

				$("#supplier").autocomplete("businessunits_query",
				   		  { 
							extraParams:{value:function(){return $("#supplier").val();}},   
				         	matchSubset: false,
				         	autoFill: true, 
				        	matchContains: true, 
				         	autoFill: false,
				         	dataType:'json',
				        	parse:function(data){   
				    		var rows=new Array();	 
				        	for(var i=0;i<data.length;i++){
				             rows[rows.length]={   
				                 data:data[i].name,  
				                 value:data[i].id,//值 
				                 result:data[i].name//返回结果
				            };
				         } 
				         return rows;  
				    },   
				    formatItem:function(row) {
				         return row; 
				    }
				   }).result(function(row,i,n){
					   	   	$("#businessunitsId").val(n);
				  });
			}
		});	
	});		
		
	function edit(table){
		var id=table.jqGrid('getGridParam', 'selrow');
		table.jqGrid("editRow",id);
	}
</script>
</head>

<body>
	<div id='thbutton' title='过滤列'><button>过滤列</button></div>
	<div class='hidecol ui-jqdialog'><button>过滤列</button></div>
	<div id="hidecolName" class="ui-jqdialog">
		<ul></ul>
	</div>
	<div class="data_page">
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<input type="hidden" id="app" value="14"/>				
				<table id="list" title="采购订单管理" height=180% multiselect="1" colnum=3
						url="purchaseorder_listjson.action" deleteurl="purchaseorder_delete.action" saveurl="" 
						addfunc="addGrid" editfunc="editGrid" viewfunc="viewGrid">
				 	<thead>
						<tr>
							<th name="id" editable="0" width="40" sort=1 type="integer"></th>
							<th name="deliveryId" width="100" sort=1>单据编号</th>
							<th name="deliveryDate" type="date">日期</th>
							<th name="businessunit.shortName">供货单位</th>
							<th name="employee.name">经手人</th>
							<th name="warehouse.name">收货仓库</th>
							<th name="subTotal" align="right">总金额</th>
					 	</tr>
					</thead>
					<tfoot>
						<tr>
							<td>
								<button function="checkedall">自定义1</button>
								<button function="deleteall">自定义2</button>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
	
	<!-- 自定义Form -->
	<div id="definedForm" class="definedFormDiv">
		<form id="mainform" action="purchaseorder_save.action" method="post">
			<div class="add_del">
				<button class="add_product">添加商品</button>
				<button class="del_product">删除商品</button>
			</div>
			<table class="out_table" id="form_header">
	            <tr>
	            	<td>
	            	</td>
	            	<td>
	            	</td>
	            	<td>
	            		<label for="deliveryDate">录单日期</label>
	            	</td>
	            	<td>
	            		
	            		<input name="businesstype.id" type="hidden" value="1"/>
	            		<input id="uid" name="id" type="hidden"/>
	            		<input id="deliveryDate" class="date" name="deliveryDate" type="text" readonly="readonly" />
	            	</td>
	            	<td>
	            		<label for="deliveryId">单据编号</label>
	            	</td>
	            	<td>
	            		<input id="deliveryId" class="node" name="deliveryId" type="text" readonly="readonly" />
	            	</td>
	            </tr>
	            <tr>
					<td>
						<label for="supplier">供货单位</label>
					</td>
					<td>
						<input id="supplier" name="" type="text" />
						<input id="businessunitsId" name="businessunit.id" type="hidden"/>
					</td>
					<td>
						<label for="contactor">联系人</label>
					</td>
					<td>
						<input id="contactor" name="" type="text" />
						<input id="contactorId" name="contactor.id" type="hidden"/>
					</td>
					<td>
						<label for="storage">收货仓库</label>
					</td>
					<td>
						<input id="storage" name="" type="text" />
						<input id="warehouseId" name="warehouse.id" type="hidden"/>
					</td>
	            </tr>
	            <tr>
					<td>
						<label for="handle">经手人</label>
					</td>
					<td>
						<input id="handle" name="handle" type="text" />
						<input id="employeeId" name="employee.id" type="hidden"/>
					</td>
					<td>
						<label for="branch">部门</label>
					</td>
					<td>
						<input id="branch" name="" type="text" />
					</td>
					<td>
						<label for="remark">备注</label>
					</td>
					<td>
						<input id="remark" name="memos" type="text" />
					</td>
	            </tr>
	        </table>
	        <!-- 表头结束 -->
        
	        <!-- 数据部分 -->
	        <table id="jxctable" cellEdit="1" height=180% multiselect="1" url=" "
	         title="采购计划单">
	        	<thead>
	                <tr>
	                	<th name="id" editable="0" width="40" sort=1 type="integer"></th>
						<th name="pname" hidden=1>商品号</th>
						<th name="item">商品编号</th>
	                    <th name="view" edittype="custom" url="product_query.action">商品名称</th>
	                    <th name="specification" editable="0">规格</th>
	                    <th name="color" editable="0">颜色</th>
	                    <th name="unit" editable="0">单位</th>
	                    <th name="price" align="right">单价</th>
	                    <th name="pquantity" align="right">数量</th>
	                    <th name="sum" align="right">金额</th>
	                </tr>
	            </thead>
	        </table>
	        <!-- 数据部分结束 -->
	         <!--- 数量金额计算及保存打印 -->
	        <table class="out_table" id="form_foot" cellpadding="0" cellspacing="0">
	            <tr>
	            	<td>发票类型</td>
	            	<td>
	            		<select id="invoice_type" name="invoictype.id">
	            		</select>
	                </td>
	                <td><label for="taxes">税金</label></td>
	                <td><input id="taxes" name="valueAddTax" type="text" readonly="readonly"/></td>
	                <td><label for="jiasui">价税合计</label></td>
	                <td><input id="jiasui" name="amount" type="text" readonly="readonly" /></td>
	            </tr>
	         </table>
		</form>	
	</div>
</body>
</html>