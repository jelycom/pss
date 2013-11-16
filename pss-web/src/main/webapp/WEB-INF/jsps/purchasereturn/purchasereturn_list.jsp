<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-采购退货单管理</title>
<script type="text/javascript">
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
									"<button onclick='selectPurchase()'>选择进货单</button></div>")
			var $buttonDiv = $(this).next(); 
			if (!$buttonDiv.is(":has(.extend-buttons)")){
				$buttonDiv.prepend($extendbuttons);
				$("button",$buttonDiv).button();
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
				<input type="hidden" id="app" value="16"/>
				<table id="list" title="采购退货单管理" height=180% multiselect="1" colnum=3 jxcdoc=1
						url="purchasereturn_listjson.action" saveurl="purchasereturn_save.action"
						addfunc="addGrid" editfunc="editGrid" viewfunc="viewGrid">
				 	<thead>
						<tr>
							<th name="id" editable="0" width="40" sort=1 type="integer"></th>
							<th name="deliveryId" thwidth="0.15" sort=1>单据编号</th>
							<th name="deliveryDate" thwidth="0.15" type="date">日期</th>
							<th name="businessunit.shortName">供应商</th>
							<th name="employee.name">经手人</th>
							<th name="warehouse.name">退货仓库</th>
							<th name="total" align="right">总金额</th>
							<th name="subTotal">状态</th>
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
	<div id="definedForm" class="definedFormDiv" title="采购退货单">
		<form id="mainform" action="purchasereturn_save.action" method="post">
			<div class="add_del">
				<button class="add_product">添加商品</button>
				<button class="del_product">删除商品</button>
			</div>
			<table class="out_table" id="form_header">
	            <tr>
	            	<td></td>
	            	<td></td>
	            	<td><label for="date">录单日期</label></td>
	            	<input name="businesstype.id" type="hidden" value="3"/>
	            	<td><input id="date" class="date" name="date" type="text" readonly="readonly" /></td>
	            	<td><label for="node">单据编号</label></td>
	            	<td><input id=deliveryId class="node" name="deliveryId" type="text" readonly="readonly" /></td>
	            </tr>
	            <tr>
					<td><label for="supplier">供应商</label></td>
					<td><input id="supplier" name="supplier" type="text" /></td>
					<td><label for="contactor">联系人</label></td>
					<td><input id="contactor" name="contactor" type="text" /></td>
					<td><label for="storage">退货仓库</label></td>
					<td><input id="storage" name="storage" type="text" /></td>
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
	        <table id="jxctable" cellEdit="1" height=180% multiselect="1" url=" " saveurl="clientArray">
	        	<thead>
	                <tr>
	                	<th name="id" editable="0" sort=1 type="integer"></th>
						<th name="pname" hidden=1>商品号</th>
						<th name="item" thwidth="0.15">商品编号</th>
	                    <th name="view" thwidth="0.15" edittype="custom" url="product_query.action">商品名称</th>
	                    <th name="specification" editable="0">规格</th>
	                    <th name="color" editable="0">颜色</th>
	                    <th name="unit" editable="0">单位</th>
	                    <th name="price" align="right">单价</th>
	                    <th name="pquantity" align="right">数量</th>
	                    <th name="sum" align="right">金额</th>
	                    <th name="purchase" thwidth="0.1">收货单号</th>
	                </tr>
	            </thead>
	        </table>
	        <!-- 数据部分结束 -->
	        <!--- 数量金额计算及保存打印 -->
	        <table class="out_table" id="form_foot" cellpadding="0" cellspacing="0">
	            <tr>
	            	<td><label for="invoice_type">发票类型</label></td>
	            	<td>
	            		<select id="invoice_type" name="invoictype.id">
	            		</select>
	                </td>
	                <td><label for="invoice_number">发票号</label></td>
	                <td><input type="text" id="" name="invoice_number" /></td>
	                <td><label for="taxes">税金</label></td>
	                <td><input id="taxes" name="" type="text" readonly="readonly"/></td>
	            </tr>
	            <tr>
	            	<td><label for="jiasui">价税合计</label></td>
	                <td><input id="jiasui" name="" type="text" readonly="readonly" /></td>
	                <td><label for="received_pay_mode">收款方式</label></td>
	                <td>
	                    <select name="received_pay_mode" id="received_pay_mode">
	                   	  	<option value="xianjin" selected="selected">现金</option>
	                        <option value="cunkuan">银行存款</option>
	                    </select>
	                </td>
	                <td><lable for="account">收款帐户</lable></td>
	                <td>
	                    <select name="" id="account" disabled="disabled">
	                    </select>
	                </td>
	            </tr>
	            <tr>
	            	<td><label for="actual_amount">退款金额</label></td>
	            	<td><input id="actual_amount" name="" type="text" class="moneyInput" /></td>
	                <td><label for="add_reduce">减少应付</label></td>
	                <td><input id="add_reduce" name="" type="text" readonly="readonly" /></td>
	                <td><label for="balance">应付余额</label></td>
	                <td><input id="balance" name="" type="text" readonly="readonly" /></td>
	            </tr>
	         </table>
		</form>	
	</div>

</body>
</html>