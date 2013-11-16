/*
 *	用来存放进销存界面相关的函数 
 */
$(function(){
	var $contact=$('#contactorName'),
		$unitName=$('#businessUnitName');
	$unitName.change(function(){						//供货单位改变后,修改联系人输入框的参数
		var keyval=$unitName.prev().val(),				//单位id
			unitName=$unitName.val();
		if (!keyval){									//清空后将联系人清空并禁用
			$contact.attr('disabled',true).val('').prev().val('');
			console.log($contact.attr('disabled'));
		}else{										//有值时启用联系人
			$contact.attr('disabled',false).val("").prev().val("");
			var xhr=$.fn.completeGrid.defaults.loadData;
			var eligible=[];						//用来缓存联系人
			$.each(xhr.resultObj.rows,function(i,n){
				if (n.id==keyval){					//找到对应单位的联系人
					eligible=n.contactors;
					return false;
				}
			});
			var newxhr={};
			$.each(xhr,function(i,n){
				if (i!="resultObj")	newxhr[i]=n;
				else newxhr[i]={};
			});
			newxhr.resultObj.rows=eligible;
			newxhr.resultObj.pageSize=100;
			newxhr.resultObj.totalRows=eligible.length;
			$contact.completeGrid([{
					pager:false,
					rowNum:100,
					datatype:'jsonstring',
					datastr:newxhr
			},{createInput:false}]);
		}
	});
	
	$contact.focus(function(){
		var unitId=$unitName.prev().val();
		if (!unitId){
			return false;
		}
		if (!$contact.data('options')){
			$.ajax({							//如果有单位且输入框没有缓存的数据，请求该单位的联系人
				url:'businessUnits_listjson.action',
				type:'POST',
				dataType:'json',
				data:{'filters':'{"groupOp":"AND","rules":[{"field":"id","op":"eq","data":"'+unitId+'"}]}'},
				error:function(){
					alert('该单位不正确或有错误，请联系管理员！');
				},
				success:function(data){
					var eligible=data.resultObj.rows[0].contactors;						//用来缓存联系人
					var newxhr={};
					$.each(data,function(i,n){
						if (i!="resultObj")	newxhr[i]=n;
						else newxhr[i]={};
					});
					newxhr.resultObj.rows=eligible;
					newxhr.resultObj.pageSize=100;
					newxhr.resultObj.totalRows=eligible.length;
					$contact.data("options",newxhr);
					$contact.completeGrid([{
							pager:false,
							rowNum:100,
							datatype:'jsonstring',
							datastr:newxhr
					},{createInput:false}]);
					$contact.trigger('click');					//设置完成后再触发事件显示自动完成
				}
			});
		}
	});
	
	//初始化自定义Form，增查改用
	var $definedForm=$('.definedForm'),
		formWidth=$definedForm.attr('dialogWidth'),
		formHeight=$definedForm.attr('dialogHeight'),
		winWidth=$('body')[0].clientWidth-8,
		winHeight=$('body')[0].clientHeight-8;
	$(".definedForm").dialog({
		autoOpen:false,modal:true,stack:false,resizable:false,minHeight:false,width:!formWidth?750:winWidth,height:!formHeight?'auto':winHeight,closeOnEscape:false,
		buttons:{
			"提交":function(){
				var caozuo = $(this).dialog("option","title"),
					condition=caozuo.indexOf("添加")>0?"add":"edit",
					$jxctable=$("#jxctable",this),
					selrowid=$jxctable.getGridParam("selrow"),
					$formDiv=$(this),
					$mainform=$("#mainform");
				if (selrowid){
					$jxctable.saveRow(selrowid,{url:"clientArray"});
				}
				var $error=$formDiv.find(".formError"),
					$billdetails=$formDiv.find(".billDetails");
				$error.removeClass("ui-state-error").text("");
				$billdetails.find("tr").remove();
				var options={
			 			beforeSubmit:function(formData,$form,options){
			 				var missRequired=[];							//用来存放提交前验证必须的字段
			 				for (var i=0;i<formData.length;i++){
			 					if (formData[i].required&&!formData[i].value){
			 						missRequired.push($($form[0][formData[i].name]).attr("title"));		//获取无值的必须字段的中文名
			 					}
			 				}
			 				if (missRequired.length>0){						//如果有无值的字段，则提示
			 					$error.addClass('ui-state-error').text(missRequired.toString()+"不存在或为空!");
			 					return false;
			 				}
			 				if (!$form.find(".billDetails tr").length){
								$error.addClass('ui-state-error').text('单据详情不存在或为空！请检查！');
								return false;
							}
			 				$error.removeClass('ui-state-error').text("");
			 				var queryString=$.param(formData);
			 				return true;
			 			},
			 			success:function(resp,status,xhr,$form){
			 				if (resp.operate){
			 					$error.removeClass('ui-state-error').text("");
			 					$formDiv.dialog("close");
			 					$(".treedata_div table[id]").trigger("reloadGrid");
			 				}else{
			 					$error.addClass('ui-state-error').text(resp.message);
			 				}
			 			}
			 	};//提交前的验证参数
				subData(condition,$formDiv,options);
				$("#mainform").submit();
			},
			"重置":function(){
				var $formDiv=$(this);
				$formDiv.find(".formError").removeClass('ui-state-error').text("");
				$formDiv.find(":text:not(#item,.currDate),select,input:hidden").val("").trigger("change");
				resetGrid($("#jxctable",$formDiv));
			},
			"关闭":function(){
				$(this).dialog("close");
			}
		},
		create:function(){	//初始化时给jxctable外的表格添加样式，初始化jxctable
			var $formDiv=$(this).addClass("ui-jqdialog-content");
			var $outTable=$formDiv.find(".out_table").addClass("EditTable");
			var $dialog=$(this).parent();		//获取当前dialog用来改变按钮样式
			$outTable.find("td:even").addClass("CaptionTD").end()
					.find("td:odd").addClass("DataTD").children(":text").addClass("FormElement ui-widget-content ui-corner-all");
			$dialog.find(".ui-dialog-buttonset button").each(function(index){
				switch (index){
					case 0:
						$(this).button("option","icons",{primary:"ui-icon-disk"});
						break;
					case 1:
						$(this).button("option","icons",{primary:"ui-icon-refresh"});
						break;
					case 2:
						$(this).button("option","icons",{primary:"ui-icon-close"});
						break;
				}
			});
//			$('#jxctable',$formDiv).wrap("<div class='gridFather'></div>");
			$("#mainform").before('<p class="formError"></p>').append('<table class="billDetails"></table>');
		},
		open:function(){//弹出后重新定位，显示被隐藏的按钮，清除输入框的只读属性，重新计算表格宽度
			var $formDiv=$(this),
				$table=$formDiv.find("#jxctable"),
				$fundAccount=$("#fundAccountId",this).val("-1").trigger("change");
			$("#ui-datepicker-div").hide();
			$formDiv.next().find("button:hidden").show();
			$formDiv.find(":text,input:hidden,select").each(function(){
				var logval=$(this).attr("initDisabled")==1;
				$(this).attr("disabled",logval).val("").change(function(){
					$formDiv.find(".formError").removeClass("ui-state-error").text("");
				});
			});
			$formDiv.children("form").attr("autocomplete","off");
			inputMove($formDiv.children("form"));
			gridSize($table);
			jqdialogPosition($formDiv);
    		if ($fundAccount[0]&&!$fundAccount.children().length)
				$.ajax({				//初始帐户下拉框
					type:"post",
					dataType:"json",
					url:"fundAccount_listjson.action",
					error:function(){
						alert("账户加载失败!");
					},
					success:function(data){
						if (data&&data.operate){
							data=data.resultObj.rows;
							var isview=$("#fundAccountId").parents(".ui-dialog").attr("id").indexOf("view")>=0;
							var option ="<option value='-1'>请选择账户</option>";
							for(var i=0;i<data.length;i++){
								option = option + "<option value="+data[i].id+">"+data[i].name+"</option>";
							}
							$("#fundAccountId").append(option).attr("disabled",isview);
//							$("#fundAccountId").append(option).dropkick();
//							$("#dk_container_fundAccountId .dk_toggle").click(function(){
//								if ($(this).hasClass("ui-state-disabled"))	return false;
//							});
//							$("#dk_container_fundAccountId .dk_toggle")[isview?"addClass":"removeClass"]('ui-state-disabled');
						}
					}
				});
    		if ($("#invoice_type",this)[0])
				$.ajax({			//初始发票类型下拉框
					type:"post",
					dataType:"json",
					url:"invoictype_getInvoictype",
					error:function(){
						alert("发票类型加载失败!");
					},
					success:function(data){
						var option ="<option value='-1'>请选择发票类型</option>";
						for(var i=0;i<data[0].length;i++){
							option=option + "<option value="+data[0][i].id+" id="+data[0][i].ratio+">"+data[0][i].name+"</option>";
						}
						$("#invoice_type").append(option)	
								.dropkick({			//发票类型改变时计算税金等输入框的值
									change:function(val,label){
										var totalAmount = $("#jxctable").footerData("get").sum;
										inputCalculate(totalAmount);
									}
						});
					}
				});
		},
		close:function(){//关闭表 格时清空行，并且只保留空白行（默认五行）
			$(".formError").removeClass('ui-state-error').text("");
			$(".out_table td:even",this).css("font-weight","normal");
			$("#dk_container_fundAccountId .dk_toggle").removeClass('ui-state-disabled');
			$contact.removeData('options');
			resetGrid($(this).find("#jxctable"));
		}
	});
});

function productCompSet(){
	return	[{
		url:'product_listjson.action',
		colNames:['id','编号','名称','拼音','规格','颜色','单位','预售价1','预售价2','预售价3'],
		colModel:[
		          	{name:'id',hidden:true}
		          ,	{name:'item',width:'40%'}
		          ,	{name:'fullName',width:'60%'}
		          ,	{name:'py',hidden:true}
		          ,	{name:'specification',hidden:true,fuzzy:false}
		          ,	{name:'color',hidden:true,fuzzy:false}
		          ,	{name:'unit',hidden:true,fuzzy:false}
		          ,	{name:'salePrice1',hidden:true,fuzzy:false}
		          ,	{name:'salePrice2',hidden:true,fuzzy:false}
		          ,	{name:'salePrice3',hidden:true,fuzzy:false}
		         ],
		postData:{'filters':'{"groupOp":"AND","rules":[{"field":"invalid","op":"eq","data":"0"},{"field":"deleted","op":"eq","data":"0"}]}'}
	},
	{width:260,reload:false,createInput:false}];
}//选择商品自动完成的参数

function unitCompSet(){
	return	[{
		url:"businessUnits_listjson.action",
		colNames:["id","编号","简称","拼音"],
		colModel:[{name:"id",hidden:true},{name:"item",width:"40%"},{name:"shortName",width:"60%"},{name:"py",hidden:true}]
	},
	{width:240,reload:false}];
}//往来单位自动完成的参数

function fundAccountCompSet(){
	return	[{
		url:"fundAccount_listjson.action",
		colNames:["id","编号","名称","拼音"],
		colModel:[{name:"id",hidden:true},{name:"item",hidden:true},{name:"name"},{name:"py",hidden:true}]
	},
	{width:240,reload:false,createInput:false}];
}//帐户自动完成的参数

function employeeCompSet(){
	return	[{
		url:"employee_listjson.action",
		colNames:["id","姓名","拼音"],
		colModel:[{name:"id",hidden:true},{name:"name",width:200},{name:"py"}]
	},
	{width:200,reload:false}];
}//经手人自动完成参数

function warehouseCompSet(){
	return	[{url:"warehouse_listjson.action"},{reload:false}];
}//仓库自动完成参数

function formatterFunc(){
	return {
		unitf:function(c,o,d){
			return d.businessUnit?d.businessUnit.shortName||d.businessUnit.name:'';
		},//单位
		emplf:function(c,o,d){
			return d.employee?d.employee.name:'';
		},//雇员
		waref:function(c,o,d){
			return d.warehouse?d.warehouse.name:'';
		},//仓库
		statf:function(c,o,d){
			return d.state?d.state.msg:'';
		},//状态
		planf:function(c,o,d){
			return d.planEmployee?d.planEmployee.name:'';
		},//计划提出人员
		execf:function(c,o,d){
			return d.executeEmployee?d.executeEmployee.name:'';
		},//计划执行人员
		fundf:function(c,o,d){
			return d.fundAccount?d.fundAccount.name:'';
		},//帐户
		outwf:function(c,o,d){
			return d.outWarehouse?d.outWarehouse.name:'';
		},//调拨出货仓
		inwaf:function(c,o,d){
			return d.inWarehouse?d.inWarehouse.name:'';
		},//调拨进货仓
		outef:function(c,o,d){
			return d.outEmployee?d.outEmployee.name:'';
		},//调出经手人
		inemf:function(c,o,d){
			return d.inEmployee?d.inEmployee.name:'';
		}//调入经手人
	};
}//回显函数

//自定义编辑
function editGrid(rowid){
	var $grid=$(this),
		title=this.p.caption;
	title=title.substring(0,title.indexOf("管")>0?title.indexOf("管"):title.length);
	$(".definedForm").dialog("option","title","编辑"+title).dialog("open");
	$(".definedForm").parent().attr("id","editmod"+$grid.attr("id"));
	showData(rowid,$grid);
	if (!$("#businessUnitId").val())	$("#contactorName").attr("disabled",false);
}

//自定义增加
function addGrid(){
	var $grid=$(this),
		title=this.p.caption;
	title=title.substring(0,title.indexOf("管")>0?title.indexOf("管"):title.length);
	$(".definedForm").dialog("option","title","添加"+title).dialog("open");
	currDate();			//获取当前日期
	$(".definedForm").parent().attr("id","editmod"+$grid.attr("id"));
	item($("#billDate").val());
//	$("#billDate").unbind('change').change(function(){
//		item($(this).val());
//	});
	function item(date){			//获取编号
		if ($("#item")[0]&&date){
			$.ajax({
				type:"post",
				data:{"billDate":date},
				dataType:"json",
				url:$("#item").attr("url"),
				error:function(){
					alert("生成单据编号错误!");
				},
				success:function(data){
					if(data.resultObj)	$("#item").val(data.resultObj);
				}
			});
		}
	}
}

//查看测试用
function viewGrid(rowid){
	var $grid=$(this),
		title=this.p.caption;
	var $dialog=$(".definedForm");
	title=title.substring(0,title.indexOf("管")>0?title.indexOf("管"):title.length);
	$dialog.dialog("option","title",title+"详情").dialog("open");
	$dialog.parent().attr("id","viewmod"+$grid.attr("id"));
	$(".out_table td:even",$dialog).css("font-weight","bold");
	$(":text",$dialog).attr("disabled",true);
	showData(rowid,$grid);
	$dialog.next().find("button:not(:last):not(#viewBillButton)").hide();
	$("#dk_container_fundAccountId .dk_toggle").addClass('ui-state-disabled');
	$("select",$dialog).attr("disabled",true);
}

function showData(rowid,$grid) {
	var url=$grid.getGridParam('url'),
		viewurl=$grid.getGridParam('viewurl'),
		atName=url.substring(0,url.indexOf('_'));
	$.ajax({
		type:"post",
		dataType:"json",
		data:{"id":rowid},
		url:!viewurl?atName+'_viewData.action':viewurl,
		error:function(){
			alert("暂时无法获取数据，请与管理员联系！");
		},
		success:function(data){
			var data=data.resultObj;
			var form=$('.definedForm form')[0];
			$.each(data,function(key,val){
				if (val){
					if (key!='details'){			//主表数据填充到表单中
						if (typeof val!='object'){
							if (key=="id")	$("#uid",form).val(val);
							if ($("#"+key,form).is("select"))
//								$("#"+key,form).children("[value="+val+"]").attr("selected",true).trigger('change');		//下拉框选中对应的值
								$("#"+key,form).val(val).trigger('change');		//下拉框选中对应的值
							else	$("#"+key,form).val(val);
						}else{
							if ($(form[key+".id"]).is("select"))	$(form[key+'Id']).val(val.id).trigger('change');		//下拉框选中对应的值			
							else	$(form[key+".id"]).val(val.id).next().val(val.name);							//id类
						}
					}else{
						for(var i=val.length-1;i>=0;i--){
							var rd={};												//存放行数据
							$.each(data.details[i],function(i,n){
								switch (i) {
									case "id":
										rd["rid"]=n;
										break;
									case "product":
										$.each(n,function(index,val){
											index!='id'?rd[index]=val:rd['productId']=val;	//添加产品信息，产品id特殊处理
										});
										break;
									case "productMaster":							//付款单收款单回显
										rd["masterItem"]=n.item;
										rd["masterDate"]=n.billDate;
										rd["amount"]=n.amount;
										rd["productMasterID"]=n.id;
										break;
									case "bursary":
										rd["bursaryId"]=n.id;
										rd["bursaryName"]=n.fullName;
										break;
									default:
										rd[i]=n;				//不是产品信息，直接添加
										break;
								}
							});
							$('#jxctable').setRowData(i+1,rd);
							if (rd.complete){					//盘点单的着色
								var $targetRow=$('#'+(i+1),$('#jxctable'));
								if (rd['quantity']<0)	$targetRow.css('color','green');
								if (rd['quantity']>0)	$targetRow.css('color','red');
								if (rd['quantity']==0)	$targetRow.css('color','blue');
							}
						}	
					}
				}
			});
			calculateTotalrow();
		}
	});
}//编辑或查看时获取数据

//选择计划单DIV
function selectPlan(type){
	if (!$("body").is(":has(#selectPlan)")){
		var selectPlanDiv = $("<div id='selectPlan' title='选择计划单' type="+type+">请选择一个计划单：" +
								"<table id='plan'></table>" +
								"</div>");
		selectPlanDiv.appendTo("body");
		selectPlanDiv.dialog({autoOpen:false,modal:true,width:500,resizable:false,minHeight:false,
			buttons:{
				"确定":function(){
					var $table=$("#jxctable");
					var tr=$table.find("tr[id]").filter(function(index){
						var rowVal=getObjData($table,this.id);
						return rowVal[1]=="";
					});
					//没有空行时id为最后一行id+1,有空行时id为空行id
					var id=!tr[0]?Number($table.find("tr[id]:last").attr("id"))+1:Number(tr[0].id);
					var selrow=$("#plan").jqGrid('getGridParam','selrow');
					if (!selrow)	return false;
					else	$(this).dialog("close");			//选中行后确定关闭
					$.ajax({
						type:"post",
						dataType:"json",
						data:{"id":selrow},
						url:$(this).attr("type")+"_viewData.action",
						error:function(){
							alert("暂时无法获取数据，请与管理员联系！");
						},
						success:function(data){
							if (data&&data.operate){
								data=data.resultObj;
								for ( var i=0; i<data.details.length; i++) {
									var rowData={};
									$.each(data.details[i],function(k,n){
										if (k!='product')	rowData[k]=n;				//不是产品信息，直接添加
										else
											$.each(n,function(index,val){
												index!='id'?rowData[index]=val:rowData['productId']=val;	//添加产品信息，产品id特殊处理
											});
									});
									rowData["memos"]=data.item,
									rowData["planID"]=data.id;
									$table[!$("#"+(id+i),$table)[0]?"addRowData":"setRowData"](id+i,rowData);
								}
								calculateTotalrow();
							}
						}
					});
				},
				"返回":function(){
					$(this).dialog("close");
				}
			},
			create:function(){
				var options={
						url:$(this).attr("type")+"_listjson.action",
						colNames:["计划单号","计划开始日期","计划结束日期","提出人员","执行人员"],
						colModel:[
							{name:"item",width:"120"},
							{name:"startDate",width:"90"},
							{name:"endDate",width:"90"},
							{name:"planEmployee.name",width:"80"},
							{name:"executeEmployee.name",width:"78"}
						]
					};
				$("#plan").simpGrid(options);
			}
		});
	}
	$("#selectPlan").dialog("open");
}

//选择订单DIV
function selectOrder(type){
	var $form=$(".definedForm form"),
		unitID=$("#businessUnitId",$form).val(),
		$error=$form.prev(".formError").removeClass("ui-state-error").text("");
	if (!unitID){
		$error.addClass("ui-state-error").text("请先选择单位名称");
		return false;
	}
	var postData={'filters':'{"groupOp":"AND","rules":[{"field":"businessUnit.id","op":"eq","data":"'+unitID+'"}]}'};
	if (!$("body").is(":has(#selectOrder)")){
		var selectOrderDiv = $("<div id='selectOrder' title='选择订单' type='"+type+"'>请选择一个订单：" +
								"<table id='order'></table></div>");
		selectOrderDiv.appendTo("body");
		selectOrderDiv.dialog({autoOpen:false,modal:true,width:500,resizable:false,minHeight:false,
			buttons:{
				"确定":function(){
					var $table=$("#jxctable");
					var tr=$table.find("tr[id]").filter(function(index){
						var rowVal=getObjData($table,this.id);
						return rowVal[1]=="";
					});
					var id=!tr[0]?Number($table.find("tr[id]:last").attr("id"))+1:Number(tr[0].id);		//没有空行时id为最后一行id+1,有空行时id为空行id
					var selrow=$("#order").jqGrid('getGridParam','selrow');
					if (!selrow)	return false;
					else	$(this).dialog("close");		//选中行后确定关闭
					$.ajax({
						type:"post",
						dataType:"json",
						data:{"id":selrow},
						url:$(this).attr("type")+"_viewData.action",
						error:function(){
							alert("暂时无法获取数据，请与管理员联系！");
						},
						success:function(data){
							if (data&&data.operate){
								var form=$(".definedForm form")[0],
									data=data.resultObj,
									$advance=$(form["advance"]);//预付金额输入框
								var advance=Number($advance.val());
								$advance.val(advance+Number(data.arap));
								$advance.data(data.id.toString(),data.arap);//累加并缓存单据金额
								for(var i=0;i<data.details.length;i++){
									var rowData={};
									$.each(data.details[i],function(k,n){
										if (k!='product')	rowData[k]=n;				//不是产品信息，直接添加
										else
											$.each(n,function(index,val){
												index!='id'?rowData[index]=val:rowData['productId']=val;	//添加产品信息，产品id特殊处理
											});
									});
									rowData["memos"]=data.item;
									rowData["orderBillID"]=data.id;
									$table[!$("#"+id,$table)[0]?"addRowData":"setRowData"](id,rowData);
									id=$("#"+id,$table).next().attr('id');
								}
								var totalAmount=calculateTotalrow();
								inputCalculate(totalAmount);
							}
						}
					});
				},
				"返回":function(){
					$(this).dialog("close");
				}
			},
			create:function(){
				var options={
						url:$(this).attr("type")+"_listjson.action",
						colNames:["订单号","下单日期","交货日期","经手人","总金额"],
						colModel:[
						        {name:"item",width:"130"}
							,	{name:'orderDate',width:"90"}
							,	{name:'deliveryDate',width:"90"}
							,	{name:"employee.name",width:"70"}
							,	{name:"amount",width:"100",formatter:"currency"}
						],
						postData:postData
					};
				$("#order").simpGrid(options);
			}
		});
	}else{				//重新加载表格
		$("#order").setGridParam({postData:postData}).trigger("reloadGrid");
	}
	$("#selectOrder").dialog("open");
}

//选择进货、销货单
var lastUnit="";
function selectBill(type,isDetail){
	var withreturn=isDetail?"":"withreturn";
	var $form=$(".definedForm form"),
		unitID=$("#businessUnitId",$form).val(),
		$error=$form.prev(".formError"),
		whetherDeli=type.indexOf("Delivery")>0;
	if (!withreturn)	withreturn="";
	if (!unitID){
		$error.addClass("ui-state-error").text("请先选择单位名称");
		return false;
	}
	$error.removeClass("ui-state-error").text("");
	if (!$("body").is(":has(#selectBill)")){
		var $selectBill=$("<div id='selectBill' title='选择开单' type='"+type+"'>"+
								"<p class='dateFilter' style='margin-bottom:5px;'>单据时间从<input type='text' class='date' id='sDate' /> 至 <input type='text' class='date currDate' id='eDate' />"+
								"<button id='refreshBill'>刷新</button></p>"+
								"<table id='bill'></table>" +
								"</div>");
		$selectBill.appendTo("body");
		$selectBill.find(".date").datepicker({changeMonth:true,
												changeYear:true,
												showButtonPanel: true,
												yearRange:'-10:+10'
											});
		$selectBill.children("input").attr("autocomplete","off");
		$selectBill.dialog({autoOpen:false,modal:true,width:600,resizable:false,minHeight:false,
			buttons:{
				"确定":function(){
					var $table=$("#jxctable");
					var blankTr=[],existsTr=[];
					var $dialog=$(this);
					$table.find("tr[id]").each(function(i){
						var rowVal=getObjData($table,this.id);
						!rowVal[1]?blankTr.push(this):existsTr.push($table.getRowData(this.id).productMasterID);
					});
					//没有空行时id为最后一行id+1,有空行时id为空行id
					var id=!blankTr[0]?Number($table.find("tr[id]:last").attr("id"))+1:Number(blankTr[0].id);
					var selrow=isDetail?$('#bill').getGridParam('selrow'):$("#bill").getGridParam('selarrrow');
					if (typeof selrow=="string"?!selrow:!selrow.length)	return false;
					else		$(this).dialog("close");			//选中行后确定关闭
					if (isDetail){
						$.ajax({
							type:'post',
							dataType:'json',
							data:{'id':selrow},
							url:$dialog.attr("type")+'_viewData.action',
							error:function(){
								alert('暂时无法获取数据，请与管理员联系！');
							},
							success:function(data){
								var data=data.resultObj;
								var form=$form[0];
								$.each(data,function(key,val){
									if (val){
										if (key!='details'){					//主表数据填充到表单中
//											if (typeof val!='object'){			//字符串直接填入
//												if ($('#'+key,form).is('select'))	$('#'+key,form).val(val).trigger('change');		//下拉框选中对应的值
//												else	$('#'+key,form).val(val);
//											}else{
//												if ($("#"+key+"Id",form).is("select"))	$("#"+key+"Id",form).val(val.id).trigger('change');		//下拉框选中对应的值			
//												else	$("#"+key+"Name",form).val(val.name);							//id类
//											}
										}else{
											for(var i=0;i<data.details.length;i++){
												var rowData={};
												$.each(data.details[i],function(i,n){
													if (i!='product')	rowData[i]=n;				//不是产品信息，直接添加
													else
														$.each(n,function(index,val){
															index!='id'?rowData[index]=val:rowData['productId']=val;	//添加产品信息，产品id特殊处理
														});
												});
												rowData['memos']=data.item;
												$table[!$("#"+(id+i),$table)[0]?"addRowData":"setRowData"](id+i,rowData);
											}	
											calculateTotalrow();
										}
									}
								});
							}
						});
					}else{
						var insertRow=[];
						for (var i=0;i<selrow.length;i++){
							if ($.inArray(selrow[i],existsTr)<0){			//已添加的单据不再添加,从数组中删除
								insertRow.push(selrow[i]);
							}
						}
						$.each(insertRow,function(i,n){
							var rowData=$("#bill").getRowData(n);
							var newData={"masterItem":rowData["item"],"masterDate":rowData["billDate"],"productMasterID":rowData["id"],arap:rowData["surplusArap"]};
							$.extend(newData,rowData);
							$table[!$table.getInd(id+i,true)?"addRowData":"setRowData"](id+i,newData);
							calculateTotalrow();
						});
					}
					lastUnit="";										//点击确定后再选择进货单时重新发送请求
				},
				"返回":function(){
					$(this).dialog("close");
				}
			},
			create:function(){
				var options={
						url:$(this).attr("type")+"_findunfinished"+withreturn+".action",
						width:576,
						colNames:["","单号","日期","经手人","总金额","已"+(whetherDeli?"收":"付")+"款","未"+(whetherDeli?"收":"付")+"款"],
						colModel:[
						    {name:"id",hidden:true},      
							{name:"item",width:"160"},
							{name:"billDate",width:"120"},
							{name:"employee.name",width:"80"},
							{name:"amount",width:"100",formatter:"currency"},
							{name:"totalPaid",width:"100",align:"right",formatter:function(c,o,d){
								return c==undefined?(d.paid+d.paidArap).toFixed(2):c.toFixed(2);
							}},
							{name:"surplusArap",width:"100",align:"right",formatter:function(c,o,d){return c==undefined?d.arap-d.paidArap:c.toFixed(2);}}
						],
						postData:{'filters':'{"groupOp":"AND","rules":[{"field":"businessUnit.id","op":"eq","data":"'+unitID+'"}]}'},
						footerrow:true,
						multiselect:!isDetail,
						loadComplete:function(xhr){
							var $grid=$(this);
							var amountArray=$grid.getCol("amount",false),
								paidArray=$grid.getCol("totalPaid",false),
								arapArray=$grid.getCol("surplusArap",false),
								totalAmount=0,totalPaid=0,totalArap=0;
							for (var i=0;i<amountArray.length;i++){			//计算合计数量和总金额
								if (!isNaN(Number(amountArray[i]))){
									totalAmount+=Number(amountArray[i]);
									totalPaid+=Number(paidArray[i]);
									totalArap+=Number(arapArray[i]);
								}
							}
							$grid.footerData("set",{item:"合计",amount:totalAmount,totalPaid:totalPaid,surplusArap:totalArap});
						}
				};
				var $sDate=$("#sDate"),
					$eDate=$("#eDate"),
					$bill=$("#bill");
				$bill.simpGrid(options).data("businessUnit",options.postData);
				$sDate.change(function(){
					var sDate=$(this).val();
					if (isDate(sDate)){
						var year=sDate.substring(0,4),
							month=sDate.substring(5,7),
							date=sDate.substring(8);
						$eDate.datepicker("option","minDate",new Date(year,month-1,date));//设置结束日期的最小值
					}
				});
				currDate($eDate);
				$("#refreshBill").button({icons:{primary:"ui-icon-refresh"}}).click(function(){
					var sDate=$sDate.val(),
						eDate=$eDate.val(),
						postData=$bill.data("businessUnit"),
						newPostData;
					if (!sDate){
						$bill.setGridParam({postData:postData}).trigger("reloadGrid");
						return false;
					}else if(!eDate){
						rulesString='{"field":"billDate","op":"ge","data":"'+sDate+'"}';
					}else{
						rulesString='{"field":"billDate","op":"ge","data":"'+sDate+'"},{"field":"billDate","op":"le","data":"'+eDate+'"}';
					}
					if (!postData){
						newPostData={'filters':'{"groupOp":"AND","rules":['+rulesString+']}'};
					}else{
						var rules=postData.filters;
						newPostData={
								'filters':'{'+rules.substring(1,rules.length-1)+
								',"groups":[{'+
									'"groupOp":"AND","rules":['+rulesString+']'+
							',groups:[]}]}'
						};
					}
					$bill.setGridParam({postData:newPostData}).trigger("reloadGrid");
				});//过滤某个时间段内的单据
			},
			open:function(){
				$("#sDate").datepicker("hide").trigger("blur");
			}
		});
	}else{
		if (unitID!=lastUnit){
			var newPostData={'filters':'{"groupOp":"AND","rules":[{"field":"businessUnit.id","op":"eq","data":"'+unitID+'"}]}'};
			$("#bill").setGridParam({postData:newPostData}).trigger("reloadGrid").data("businessUnit",newPostData);
		}
	}
	lastUnit=unitID;										//存放本次选择的单位，仍选择本单位时就不重新请求
	$("#selectBill").dialog("open");
}

//自动分配
function autoAllot(){
	var total=Number($("#amount").val())+Number($("#discount").val()),
		$jxcGrid=$("#jxctable"),
		totalArap=$jxcGrid.footerData("get").arap,
		selrow=$jxcGrid.getGridParam('selrow');
	if (selrow){
		$jxcGrid.restoreRow(selrow).resetSelection();
	}
	if (total>0){									//有付款金额时根据付款金额来分配
		$jxcGrid.find("tr[id]").each(function(i,n){
			var rData=getObjData($jxcGrid,n.id);
			if (rData[1]){								//给有数据的行分配金额
				if (total<0){
					$jxcGrid.setRowData(n.id,{"currentPay":0});
				}else{
					$jxcGrid.setRowData(n.id,{"currentPay":total-rData[0].arap>=0?rData[0].arap:total.toFixed(2)});
//					if (total-rData[0].arap>=0){		//剩余金额大于本行未付金额时，付款金额等于未付金额
//						$jxcGrid.setRowData(n.id,{"currentPay":rData[0].arap});
//					}else{
//						$jxcGrid.setRowData(n.id,{"currentPay":total.toFixed(2)});
//					}
				}
				calculateTotalrow();
				total-=rData[0].arap;
			}else{
				calculateTotalrow();
				return false;
			}
		});
	}else{												//没有合计金额付清全部单据，并得出合计值
		$jxcGrid.find("tr[id]").each(function(i,n){
			var rData=getObjData($jxcGrid,n.id);
			if (rData[1]){
				$jxcGrid.setRowData(n.id,{"currentPay":rData[0].arap});
			}
			if(!rData[1]||i==$jxcGrid.find("tr[id]").length-1){
				calculateTotalrow();
				$("#amount").val($jxcGrid.footerData("get").currentPay);
				return false;
			}
		});
	}
}

$.fn.simpGrid=function(options){
	options=$.extend({},$.fn.simpGrid.defaults.options,options);
	return this.each(function(){
		$.each(options.colModel,function(i,col){
			if (!col.index)					col.index=col.name;
			if (col.sortable==undefined)	col.sortable=false;
			if (col.formatter=="currency")	col.align="right";
		});
		$(this).jqGrid(options);
	});
}
$.fn.simpGrid.defaults={
	options:{
		url:"",
		datatype:"json",
		mtype:"POST",
		colNames:[],
		colModel:[],
		jsonReader:{
			root: "resultObj.rows", 
        	page: "resultObj.pageNo",		// json中代表当前页码的数据
			total: "resultObj.totalPages",	// json中代表页码总数的数据
			records: "resultObj.totalRows", // json中代表数据行总数的数据
			repeatitems:false
		},
		rowNum:10000,
		height:150,
		scroll:true,
		shrinkToFit:true,
		forceFit:true,
		width:476
	}
}