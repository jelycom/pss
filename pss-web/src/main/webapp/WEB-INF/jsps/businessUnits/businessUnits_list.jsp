<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-单位信息管理</title>
<script type="text/javascript">
	$(function() {
		var contGridoptions={
				pager:false,
				rowNum:100,
				altRows:false,
				caption:false,
				colNames:['','中文名','姓名拼音','英文名','职称/职务','联系电话','手机','电子邮件','生日','纪念日说明'],
				toolbar:[false,''],
				colModel:[
						{name:'id',editable:false,hidden:true,gridview:false}
					,	{name:'name',width:120,editrules:{required:true},fuzzySearch:true}
					,	{name:'py',width:100,fuzzySearch:true}
					,	{name:'englishName',width:120,fuzzySearch:true}
					,	{name:'title',width:100}
					,	{name:'phone'}
					,	{name:'mobilePhone'}
					,	{name:'email',formatter:'email',width:200}
					,	{name:'birthday',formatter:'date',editoptions:{yearRange:'-80:-14'},hidden:true}
					,	{name:'dateDescription',hidden:true}
				],
				editurl:' '
			};
		$('#contacts').selfGrid(contGridoptions);
		
		var lastID;
		function tabsSelect($grid,$form,rowid){
			var $tabs=$(".ui-tabs",$form),
				$contactsDiv=$('#contactsDiv'),
				$contGrid=$('#contacts');
			$tabs.tabs('option','selected',0);
			if (!$tabs.is(':has(#tabs-3)')){
				$tabs.tabs( 'add', '#tabs-3','联系人信息');
			}
			var addPrm={
					width:700,jqModal:false,modal:false,cloaseAfterAdd:true,closeAfterEdit:true,reloadAfterSubmit:false,
					afterShowForm:function(formid){
						var $grid=arguments[1]?arguments[1]:$(this),
		    				colModels=$grid.getGridParam('colModel');
						pagination(formid,$grid);
						setTimeout(function(){inputMove(formid);},20);
						jqdialogPosition(formid);		//弹出框定位
					},
					onClose:function(){
						$('.selectTree,.completeDiv,#lui_contacts').hide();
					},
					beforeSubmit:function(data,$form){
						var $grid=$(this),
							rowid=$grid.find('tr[id]:last').attr('id');
						if (!rowid)	rowid=0;
						else if (!isNaN(parseInt(rowid)))	rowid=parseInt(rowid);
						$grid.addRowData(rowid+1,data);
						$form.closest('.ui-jqdialog').find('#cData').trigger('click');
						return [false,'不提交'];
					}
			};
			var editPrm={
					width:700,jqModal:false,modal:false,cloaseAfterAdd:true,closeAfterEdit:true,reloadAfterSubmit:false,
					afterShowForm:function(formid){
						var $grid=arguments[1]?arguments[1]:$(this);
						pagination(formid,$grid);
						setTimeout(function(){inputMove(formid);},20);
						jqdialogPosition(formid);		//弹出框定位
					},
					onClose:function(){
						$('.selectTree,.completeDiv,#lui_contacts').hide();
					},
					beforeSubmit:function(data,$form){
						var $grid=$(this);
						var rowid=data.contacts_id;
						var next=$contGrid.find("#"+rowid).next().attr("id");
						$grid.delRowData(rowid);
						!next?$grid.addRowData(rowid,data,'last'):$grid.addRowData(rowid,data,'before',next);
						return [true,'不提交'];
					}
			};
			$form.find("button").unbind(".disabled");
			$('#de_add').removeClass('ui-state-disabled').button({icons:{primary:'ui-icon-plus'}}).bind("click.events",function(){
				$contGrid.editGridRow('new',addPrm);
			});
			$('#de_edit').removeClass('ui-state-disabled').button({icons:{primary:'ui-icon-pencil'}}).bind("click.events",function(){
				var editid=$contGrid.getGridParam('selrow');
				!editid?alert('没有选中记录！'):$contGrid.editGridRow(editid,editPrm);
			});
			$('#de_del').removeClass('ui-state-disabled').button({icons:{primary:'ui-icon-trash'}}).bind("click.events",function(){
				var selArray=$contGrid.getGridParam("selarrrow");
				$contGrid.delGridRow(selArray);
			});
			if (!$('#tabs-3',$tabs).is(':has(#contactsDiv)')){//没有联系人表格时添加
				$('#tabs-3',$tabs).append($contactsDiv);
				$contactsDiv.show();
			}
			$('.ui-tabs').unbind('tabsselect').bind('tabsselect',function(e,obj){
				gridSize($contGrid);
			});
			if (rowid&&rowid!==lastID){
				var rowData;
				$.each($.fn.selfGrid.defaults.loadData,function(i,n){		//从缓存中读取该行数据
					if (n.id==rowid){
						rowData=n;
						return false;
					}
				});
				var contactors=rowData['contactors'];
				if (contactors){
					$contGrid.clearGridData();
					for(var i=contactors.length-1,j=1;i>=0;i--,j++){
						$contGrid.addRowData(contactors[i].id,{
							'id':contactors[i].id,
							'name':contactors[i].name,
							'py':contactors[i].py,
							'englishName':contactors[i].englishName,
							'tltle':contactors[i].title,
							'phone':contactors[i].phone,
							'mobilePhone':contactors[i].mobilePhone,
							'email':contactors[i].email,
							'birthday':contactors[i].birthday,
							'dateDescription':contactors[i].dateDescription
						}, 'first');
					}
				}
				lastID=rowid;
			}else if (!rowid){
				$contGrid.find('tr[id]').each(function(){
					$contGrid.delRowData($(this).attr('id'));
				});
				$contGrid.setGridParam({datatype:'local',postData:{'filters':''}}).trigger("reloadGrid");
				lastID=undefined;
			}
		}
		var gridoptions={
				url:'businessUnits_listjson.action',
				colNames:['','编号','单位全称','拼音码', '统一编号','所属地区','单位简称','负责人','身份证号','联系电话1','联系电话2','联系人','传真','开户行','账号',
				          '收付款方式','对供应商付款日','供应商帐期','客户请款日','客户账期','信用额度','信用余额','单位地址','供应商类别','供应商级别','客户类别','客户级别'
				          ,'发票地址','发票样式','备注'],
				colModel:[
					{name:'id',editable:false,hidden:true,gridview:false},
					{name:'item',width:200,editable:false,fuzzySearch:true},
					{name:'name',width:250,fuzzySearch:true,editrules:{required:true}},
					{name:'py',width:100,fuzzySearch:true},
					{name:'unitCode'},
					{name:'region.id',width:100,dataUrl:"region_treejson.action",formatter:'regf',completeGrid:regionCompleteGridSet},
					{name:'shortName',width:120,fuzzySearch:true,editrules:{required:true}},
					{name:'owner'},
					{name:'rocId',hidden:true},
					{name:'contactPhone1',editrules:{required:true}},
					{name:'contactPhone2',hidden:true},
					{name:'contactors',hidden:true,editable:false,gridview:false},
					{name:'fax',hidden:true},
					{name:'bankName',hidden:true,lineFeed:true},
					{name:'account',hidden:true,tabsTitle:'辅助信息'},
					{name:'paymentTerm',hidden:true},
					{name:'payDate',hidden:true},
					{name:'payDays',hidden:true},
					{name:'receiveDate',hidden:true},
					{name:'receivedays',hidden:true},
					{name:'creditLine',hidden:true},
					{name:'creditBalance',hidden:true},
					{name:'address',hidden:true},
					{name:'supplierType.id',hidden:true,dataUrl:'baseData_listjson.action',formatter:'stf',completeGrid:completeGridSet},
					{name:'supplierLevel.id',hidden:true,dataUrl:'baseData_listjson.action',formatter:'slf',completeGrid:completeGridSet},
					{name:'customerType.id',hidden:true,dataUrl:'baseData_listjson.action',formatter:'ctf',completeGrid:completeGridSet},
					{name:'customerLevel.id',hidden:true,dataUrl:'baseData_listjson.action',formatter:'clf',completeGrid:completeGridSet},
					{name:'invoiceAddress',hidden:true},
					{name:'invoiceType',hidden:true},
					{name:'memos',edittype:'textarea',hidden:true}
				],
				editurl:'businessUnits_save.action',
				caption:'单位信息管理'
			};
		var naviPrm={
				edit:{
					afterShowForm:function($form){
						var $grid=$(this);
						$.fn.selfGrid.defaults.naviPrm.edit.afterShowForm($form,$grid);
						var rowid=$grid.getGridParam('selrow');
						tabsSelect($grid,$form,rowid);
					},
 					onclickSubmit:function(params,postdata){
 						$('#contacts').find("tr[id]").each(function(i){
							var rData=$('#contacts').getRowData(this.id);
							for (var p in rData){
								if (rData[p]&&$.trim(rData[p]))	postdata['contactors['+i+'].'+p]=rData[p];
							}
						});  
						return postdata;
					}
				},
				add:{
					afterShowForm:function($form){
						var $grid=$(this);
						$.fn.selfGrid.defaults.naviPrm.add.afterShowForm($form,$grid);
						tabsSelect($grid,$form);
					},
					beforeSubmit:null,
					onclickSubmit:function(params,postdata){
 						$('#contacts').find("tr[id]").each(function(i){
							var rData=$('#contacts').getRowData(this.id);
							for (var p in rData){
								if (rData[p]&&$.trim(rData[p]))	postdata['contactors['+i+'].'+p]=rData[p];
							}
						}); 
						return postdata; 
					}
				},
				view:{
					beforeShowForm:function($form){
						var $grid=$(this),
							rowid=$grid.getGridParam('selrow');
						$.fn.selfGrid.defaults.naviPrm.view.beforeShowForm($form,$grid);
						tabsSelect($grid,$form,rowid);
						$('.jqgrid-overlay[id*=contacts]').hide();
 						$form.find("button").addClass('ui-state-disabled').unbind(".events")
 							 .bind("click.disabled",function(){
								return false;
							});
					}
				},
				del:{url:'businessUnits_delete.action'}
		};
		var formatter={
				regf:function(c,o,r){
					return r.region?r.region.name:'';
				},
				stf:function(c,o,r){
					return r.supplierType?r.supplierType.name:'';
				},
				slf:function(c,o,r){
					return r.supplierLevel?r.supplierLevel.name:'';
				},
				ctf:function(c,o,r){
					return r.customerType?r.customerType.name:'';
				},
				clf:function(c,o,r){
					return r.customerLevel?r.customerLevel.name:'';
				}
		};
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
	});
	
	function regionCompleteGridSet(){
		return [{treeGrid: true,pager:false,rowNum:10000}];
	}
	function completeGridSet(th){
		var value=th.attr('name');
		return [{
			postData:{
				'filters':'{"groupOp":"AND","rules":[{"field":"dataType.sn","op":"eq","data":"'+value.substring(0,value.indexOf("."))+'"}]}'
			}
		}]
	}

</script>
</head>

<body>
	<div class="data_page">
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id="list" tabs="1" actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>
	
	<!-- 自定义Form -->
	<div id="contactsDiv" style="display:none;padding:8px;">
		<div class="gridTool" style="margin-bottom:5px;">
			<button id="de_add">添加</button>
			<button id="de_edit">编辑</button>
			<button id="de_del">删除</button>
		</div>
		<div class="gridTableDiv" style="height:150px;">
			<table id="contacts" xhrcache=false></table>
		</div>
	</div>
</body>
</html>