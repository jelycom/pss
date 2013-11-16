<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>现金银行期初</title>
<script type="text/javascript">
	$(function() {
		var gridoptions={
			url:'periodAccount_listjson.action',
			caption:'现金银行期初',
			colNames:['','会计期间','帐户名称','开户行','帐号','期初金额','当前金额'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'acountingPeriod.id',editable:false,hidden:true,gridview:false}
				,	{name:'fundAccount.id',fuzzySearch:true,dataUrl:"fundAccount_listjson.action",completeGrid:accountCompSet,formatter:'accof'}
				,	{name:'fundAccount.bankName',width:280,editable:false}
				,	{name:'fundAccount.accountNo',width:180,editable:false}
				,	{name:'begin',formatter:'currency'}
				,	{name:'current',align:'right',editable:false,formatter:'currf'}
			],
			editurl:'periodAccount_save.action'
		};
		var formatter={
				accof:function(c,o,d){
					return d.fundAccount?d.fundAccount.name:'';
				}
			,	currf:function(c,o,d){
					return d.fundAccount?d.fundAccount.current:'';
				}
		};
		$('#list').selfGrid(gridoptions,{del:{url:'periodAccount_delete.action'}},formatter);
	});
	function accountCompSet(){
		return [{},{keylength:1}]
	}
</script>
</head>

<body>
	<div class="data_page">
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