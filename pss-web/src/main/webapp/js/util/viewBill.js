/*
 *	调用查看收货单和销售单
 */
function viewBill(rowid,$grid,type){
	var deli=!type?false:true;										//判断是进货还是销售
	var $dialog=$('.viewBill');
	if (!$dialog[0]){
		$dialog=$(  '<div class="viewBill"><form id="echoForm">'+
					'<table class="out_table">'+
						'<tr><td></td><td></td><td>录单日期</td><td><input type="text" id="vbillDate" /></td><td>单据编号</td><td><input type="text" id="vitem" /></td></tr>'+
						'<tr><td>'+(deli?'客户':'供货')+'单位</td><td><input type="text" id="vbusinessUnitName" /></td><td>联系人</td><td><input type="text" id="vcontactorName" /></td><td>'+(deli?'发货':'收货')+'仓库</td><td><input type="text" id="vwarehouseName" /></td></tr>'+
						'<tr><td>经手人</td><td><input type="text" id="vemployeeName" /></td><td>发票类型</td><td><input type="text" id="vinvoiceName" /></td><td>发票号码</td><td><input type="text" id="vinvoiceNo" /></td></tr>'+
						'<tr><td>附加信息</td><td><input type="text" id="vinfo" /></td><td>备注</td><td><input type="text" id="vmemos" /></td></tr></table>'+
					'<table id="echoGrid"></table>'+
					'<table class="out_table">'+
						'<tr><td>此单预'+(deli?'收':'付')+'</td><td><input type="text" id="vadvance" /></td>'+
						'<td>'+(deli?'收':'付')+'款帐户</td><td><select id="vfundAccountId" name="fundAccount.id"></select></td><td>实'+(deli?'收':'付')+'金额</td><td><input type="text" id="vpaid" /></td></tr></table>'+
					'</form></div>');
		$('body').append($dialog);
		$("#vfundAccountId").unbind('change').change(function(){
			$(this).dropkick('val');
		});
		$('#echoGrid').jxcGrid({
			colNames:['id','商品号','商品名称','规格','颜色','单位','数量','单价','金额','含税单价','税金','价税合计','备注'],
			colModel:[
					{name:'id',editable:false,hidden:true,gridview:false}
				,	{name:'productId',editable:false,hidden:true,gridview:false}
				,	{name:'fullName',width:280,dataUrl:'product_listjson.action',completeGrid:productCompSet}
				,	{name:'specification',width:60,editable:false}
				,	{name:'color',width:60,editable:false}
				,	{name:'unit',width:50,editable:false}
				,	{name:'quantity',width:120,editrules:{number:true}}
				,	{name:'price',width:120,editrules:{number:true}}
				,	{name:'subTotal',editrules:{number:true}}
				,	{name:'taxPrice',width:120,editrules:{number:true},editoptions:{readonly:true}}
				,	{name:'tax',width:120,editrules:{number:true},editoptions:{readonly:true}}
				,	{name:'amount',editrules:{number:true},editoptions:{readonly:true}}
				,	{name:'memos',width:190}
			]
		});//查看收货单的参数
		//初始化自定义Form，增查改用
		$dialog.dialog({
			autoOpen:false,modal:true,stack:false,resizable:false,minHeight:false,width:750,closeOnEscape:false,
			buttons:{
				'关闭':function(){
					$(this).dialog('close');
				}
			},
			create:function(){	//初始化时给jxctable外的表格添加样式，初始化jxctable
				var formDiv=$(this).addClass('ui-jqdialog-content');
				var $outTable=formDiv.find('.out_table');
				var $dialog=$(this).parent();		//获取当前dialog用来改变按钮样式
				$outTable.addClass('EditTable');
				$outTable.find('td:even').addClass('CaptionTD').end()
						.find('td:odd').addClass('DataTD').children(':text').addClass('FormElement ui-widget-content ui-corner-all');
				$dialog.find('.ui-dialog-buttonset button').button('option','icons',{primary:'ui-icon-close'});
			},
			open:function(){//弹出后重新定位，显示被隐藏的按钮，清除输入框的只读属性，重新计算表格宽度
				var $formDiv=$(this),
					$table=$formDiv.find('#echoGrid'),
					$fundAccount=$('#vfundAccountId',this);
				$('#ui-datepicker-div').hide();
				$formDiv.find(':text,input:hidden').each(function(){
					var logval=$(this).attr('initDisabled')==1;
					$(this).attr('disabled',false).attr('disabled',logval).val('');
				});
				$formDiv.children('form').attr('autocomplete','off');
				gridSize($table);
				jqdialogPosition($formDiv);
	    		if ($fundAccount[0]&&!$fundAccount.children().length)
					$.ajax({				//初始帐户下拉框
						type:'post',
						dataType:'json',
						url:'fundAccount_listjson.action',
						error:function(){
							alert('账户加载失败!');
						},
						success:function(data){
							if (data&&data.operate){
								data=data.resultObj.rows;
								var option ='<option value="-1">请选择账户</option>';
								for(var i=0;i<data.length;i++){
									option = option + '<option value='+data[i].id+'>'+data[i].name+'</option>';
								}
								$('.viewBill #vfundAccountId').append(option).dropkick();
								$('#dk_container_vfundAccountId .dk_toggle').click(function(){			//不能选择帐户
									return false;
								});
							}
						}
					});
	    		if ($('#invoice_type',this)[0])
					$.ajax({			//初始发票类型下拉框
						type:'post',
						dataType:'json',
						url:'invoictype_getInvoictype',
						error:function(){
							alert('发票类型加载失败!');
						},
						success:function(data){
							var option ='<option value="-1">请选择发票类型</option>';
							for(var i=0;i<data[0].length;i++){
								option = option + '<option value='+data[0][i].id+' id='+data[0][i].ratio+'>'+data[0][i].name+'</option>';
							}
							$('#invoice_type').append(option).dropkick({});		
						}
					});
			},
			close:function(){//关闭表格时清空行，并且只保留五行
				resetGrid($(this).find('#jxctable'));
			}
		});
	}
	$dialog.dialog('option','title',(deli?'出':'收')+'货单详情');
	$dialog.dialog('open');
	$dialog.parent().attr('id','viewmod'+$grid.attr('id'));
	$('.viewBill .out_table td:even').css('font-weight','bold');
	$('.viewBill :text').attr('disabled',true);
	var url=$grid.getGridParam('url'),
		viewurl=$grid.getGridParam('viewurl');
//	var atName=url.substring(0,url.indexOf('_'));
	$.ajax({
		type:'post',
		dataType:'json',
		data:{'id':rowid},
		url:!viewurl?'product'+(deli?'Delivery':'Purchase')+'_viewData.action':viewurl,
		error:function(){
			alert('暂时无法获取数据，请与管理员联系！');
		},
		success:function(data){
			var data=data.resultObj;
			var form=$('.viewBill form')[0];
			$.each(data,function(key,val){
				if (val){
					if (key!='details'){					//主表数据填充到表单中
						if (typeof val!='object'){			//字符串直接填入
							if ($('#v'+key,form).is('select'))	$('#v'+key,form).val(val).trigger('change');		//下拉框选中对应的值
							else	$('#v'+key,form).val(val);
						}else{
							if ($("#v"+key+"Id",form).is("select"))	$("#v"+key+"Id",form).val(val.id).trigger('change');		//下拉框选中对应的值			
							else	$("#v"+key+"Name",form).val(val.name);							//id类
						}
					}else{
						for(var i=val.length-1,j=1;i>=0;i--,j++){
							$('#echoGrid').delRowData(j);
							var rowData={};
							$.each(data.details[i],function(i,n){
								if (i!='product')	rowData[i]=n;				//不是产品信息，直接添加
								else
									$.each(n,function(index,val){
										index!='id'?rowData[index]=val:rowData['productId']=val;	//添加产品信息，产品id特殊处理
									});
							});
							$('#echoGrid').addRowData(j,rowData,'first');
						}	
					}
				}
			});
			calculateTotalrow($('#echoGrid'));
		}
	});
	
}