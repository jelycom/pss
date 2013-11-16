<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
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
			colModel:[{name:"id",index:"id",type:"integer",editable:false},
						{name:"name",index:"name",edittype:"text",editable:true},
						{name:"birthday",index:"birthday",edittype:"text",editable:true,editoptions:{
/* 							dataInit:function(elem){
								$(elem).datepicker({
									dateFormat:"yy-mm-dd",
									changeMonth: true,
									changeYear: true,
									showButtonPanel:true,
									showWeek:true
								});
								
							} */
						}},
			           {name:"department.id",index:"department.id",search:true,
						stype:"select",searchoptions:{
							dataUrl:"/pss/department_listall.action",
							sopt:["eq","cn"],
							buildSelect:function(data){
								console.debug(data);
								//var response=$.parseJSON(data.responseText)["depts"]; //3.8.2版本需要这句
								var response=data["depts"];
								var s='<select id="department" name="department.id">';
								if (response&&response.length>0) {
									for ( var i = 0; i < response.length; i++) {
									s+="<option value="+response[i].id+">"+response[i].name+"</option>";
									}
								}
								s+='</select>';
								console.debug(s);
								
								return s;
							}	
						},
						editable:true,edittype:"select",formatter:departmentformatter,	
						editoptions:{
							dataUrl:"/pss/department_listall.action",
							buildSelect:function(data){   //如果设置了ajaxSelectOptions,则逶回去和就是一个json对象
								var response=data["depts"];
								var s='<select id="department" name="department.id" class="FormElement ui-widget-content ui-corner-all">';
								if (response&&response.length>0) {
									for ( var i = 0; i < response.length; i++) {
									s+="<option value="+response[i].id+">"+response[i].name+"</option>";
									}
								}
								s+="</select>";
								return s;
							},
						dataEvents:[{type:"change", data: { i: 7 }, fn:function(e){
							console.debug(e);
							console.debug($("#department.id").val());
						}}]
							}
							}
						],
							

		 url:"/pss/employee_listjson.action",
			mtype:"POST",
			datatype:"json",
				    rowNum:10,
		    	   	rowList:[10,20,30],
		    	   	width:760,
		    	   	height:480,
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
	
	function departmentformatter(cellvalue, options, rowObject){
		console.debug(cellvalue);
		console.debug(options);
		console.debug(rowObject);
		if (rowObject.department) {
		return rowObject.department.name;
		}
		return " ";
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
