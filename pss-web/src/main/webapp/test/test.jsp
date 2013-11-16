<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>JQGrid功能测试</title>
<link href="../css/themes/le-frog/jquery-ui-1.8.23.custom.css"
	rel="stylesheet" type="text/css" />
<link href="../css/ui.jqgrid.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="../jquery/jquery1.7.2.js"></script>
<script type="text/javascript" src="../js/i18n/grid.locale-cn.js"></script>
<script type="text/javascript"
	src="../jquery/jquery-ui-1.8.23.custom.min.js"></script>
	<script type="text/javascript" src="../js/jquery.jqGrid.min.js"></script>
<!-- <script type="text/javascript" src="test.js"></script> -->
<script type="text/javascript">
	$(function(){
/* 	$.ajax({
			type:"GET",
			url:"/pss/employee_listjson.action",
			dataType:"json",
			success:function(data){
				alert(data);
			}
		});  */
		
	var jgrid= $("#test");
		jgrid.jqGrid({
		 colNames:['id','name','birthday','department'],
/* 			colModel:[{name:"id",index:"id",type:"integer",editable:false},
						{name:"name",index:"name",edittype:"text",editable:true},
			           {name:"department.id",index:"department.id",
						stype:"select",surl:"department_listall.action",editable:true,formatter:function(cellvalue, options, rowObject){return rowObject.name;},edittype:"select",editoptions:{
							dataUrl:"department_listall.action",
							buildSelect:function(data){
								console.debug(data);
							}
						}
							}
						] */
			colModel:[{name:"id",index:"id",width:100},
						{name:"name",index:"name",width:100},
						{name:"birthday",index:"birthday",editable:true,edittype:"text",editoptions:{
						    dataInit: function(elem) {
 						    	setTimeout(function() {
						    		$(elem).datepicker({
						    			showOn: "button",
						    			dateFormat:"yy-mm-dd",
						    			changeMonth: true,
						    			changeYear: true,
						    			showButtonPanel:true,
						    			showWeek:true});
						    		},10); 
						    	}
						}},
			           {name:"department.id",index:"department.id"}
						]
		,
							
/* 		onSelectRow:function(id){
			if(id&&id!==last){
				
				//$("#test").restoreRow(last);
				//$("#test").editRow(id,true,onGridEdit);
				//last=id;
			}
		}, */
		 url:"/pss/employee_listjson.action",
			mtype:"POST",
			datatype:"json",
		    rowNum:10,
    	   	rowList:[10,20,30],
    	   	width:760,
    	   	height:"auto",
    	   	autoencode:true,
    	   	pager: '#testpager',
    	   	sortname: 'id',
    	    viewrecords: true,
    	    sortable:false,
    	    sortorder: "asc",
    	    editurl:"/pss/employee_save.action",
    	    jsonReader: { 
            	root: "rows", 
            	page: "pageNo",	// json中代表当前页码的数据
				total: "totalPages",	// json中代表页码总数的数据
				records: "totalRows", // json中代表数据行总数的数据
            	repeatitems: false }, 
	            loadComplete: function (data) { 
	            	console.debug("load complete");
			            },
		 caption:"Toolbar Example",
					ajaxSelectOptions: {
					   type: "POST", // one need allows GET in the webmethod (UseHttpGet = true)     
					   contentType: 'application/json; charset=utf-8',    
					   dataType: "json",     
					   cache: false,     
					   data: {         
						   id: function () {             
							   return $("#test").jqGrid('getGridParam', 'selrow');
							  // return JSON.stringify($("#test").jqGrid('getGridParam', 'selrow'));
						   		}    
					          	}
					          }
	 });
		jgrid.navGrid("#testpager",{view:true},{},{},{},{multipleSearch:true,showQuery:true});

	});
	function onGridEdit(rowid){
		$("#"+rowid+"_birthday").datepicker({
			showOn: "button",
			dateFormat:"yy-mm-dd",
			changeMonth: true,
			changeYear: true,
			showButtonPanel:true,
			constrainInput: false,
			showWeek:true});
	}
	function departmentformatter(cellvalue, options, rowObject){
		console.debug(cellvalue);
		console.debug(options);
		console.debug(rowObject);
		if (rowObject.department) {
		return rowObject.department.name;
		}
		return "";
	}
</script>
</head>

<body>
<table id="test"></table>
<div id="testpager"></div>
<table id="toolbar1"></table>
<div id="pgtoolbar1"></div>
<div id="search"></div>
<br/>
<table id="toolbar2"></table>
<div id="pgtoolbar2"></div>

</body>
</html>
