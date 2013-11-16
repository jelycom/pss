;(function($){
	$.fn.extend({
		'regionWare':function(){
			return this.each(function(){
				var $obj=$(this)
				if (!$obj.is(':has(#regionWare)')){		//表格完成后请求生成仓库列表
					$.ajax({											//获取仓库列表
							type:'GET'
						,	dataType:'json'
						,	url:'warehouse_listall.action'
						,	error:function(){
								alert('暂时无法获取数据，请与管理员联系！');
							}
						,	success:function(data){
								if (data&&data.operate){				//获取到仓库列表后获取地区列表
									var res=data.resultObj;
									var treeData=[];
									$.ajax({
											type:'get'
										,	dataType:'json'
										,	url:'region_treejson.action'
										,	success:function(rdata){
												if (rdata.operate){
													var treeJson=rdata.resultObj.rows;
													$.each(treeJson,function(i,n){
														if (n.level>0){
															n.parent=$(treeJson).filter(function(){		//获取该地区的父节点
																return this.level==(n.level-1)&&n.lft>this.lft&&n.rgt<this.rgt;
															}).attr('id');
															n.isLeaf=false;
															treeData.push(n);					//将地区存放到json数据串中
															$.each(res,function(i,ware){		//循环获取该地区包含的仓库
																if (ware.region.id==n.id){
																	ware.id=n.id+'_'+ware.id;
																	ware.isLeaf=true;
																	ware.level=n.level+1;
																	ware.parent=n.id;
																	treeData.push(ware);
																}
															});
														}else{									//存放根节点
															n.isLeaf=false;
															n.parent='';
															treeData.push(n);
														}
													});
													var datastring={'response':treeData};
													if (!$obj.is(':has(#regionWare)')){			//插入选择仓库按钮
														$obj.append('<button id="regionWare" class="tButton" value="">所有仓库</button>');
													}
													var $regionWare=$('#regionWare').button({icons:{secondary:'ui-icon-triangle-1-s'}});
													if (!$('body').is(':has(#wareSelect)')){					//插入选选择仓库的窗口
														var $wareSelect=$('<div id="wareSelect"><p class="error"></p>'+
																		'<div class="gridTableDiv"><table id="wareTree"></table></div>'+
																		'</div>');
														$('body').append($wareSelect);
														var $wareTree=$('#wareTree'),
															$error=$('.error');
														$wareSelect.dialog({autoOpen:false,modal:true,width:200,resizable:true,minHeight:false,title:'请选择仓库',
															buttons:{
																'所有仓库':function(){
																	$regionWare.button('option','label','所有仓库').val('');
																	$(this).dialog('close');
																	$('td[id^=refresh]').trigger('click');//选中后刷新主表
																},
																'确定':function(){
																	var selrow=$wareTree.getGridParam('selrow');
																	if (!selrow){
																		$error.addClass('ui-state-error').text('没有选中仓库!');
																	}else{
																		var rc=new Rc($('#'+selrow),$wareTree);
																		if (rc.isLeaf=='false'){
																			$error.addClass('ui-state-error').text('没有选中仓库!');//只能选仓库，不能选地区
																		}else{
																			var wareID=rc._id_;
																			wareID=wareID.substring(wareID.lastIndexOf('_')+1);
																			$regionWare.button('option','label',rc.name).val(wareID);
																			$("[id^=refresh]",$obj).trigger('click');//选中仓库后刷新主表
																			$(this).dialog('close');
																		}
																	}
																},
																'返回':function(){
																	$(this).dialog('close');
																}
															},
															create:function(){
																$wareTree.jqGrid({
																	datastr:datastring,
																	datatype:'jsonstring',
																	treeGrid:true,
																	treeGridModel: 'adjacency', 					//treeGrid为true的配置
														            treeIcons: {
														            	plus:'ui-icon-folder-collapsed',
														            	minus:'ui-icon-folder-open',
														            	leaf:'ui-icon-document'
														            },
														            ExpandColumn: 'name',
																	colNames:['id','名称'],
																	colModel:[{name:'id',hidden:true},{name:'name'}],
																	jsonReader:{
																		root: 'response', 
																		repeatitems:false
																	},
																	rowNum:10000,					//每页的总行数
																	shrinkToFit:true,			//按比例初始化列宽度
																	forceFit:true,				//为真时，改变列宽度不改变表格的宽度
																	autoWidth:true,
																});
															},
															open:function(){
																$error.removeClass('ui-state-error').text('');
																gridSize($wareTree);
															},
															resizeStop:function(){
																gridSize($wareTree);
															}
														});
													}
													$regionWare.click(function(){
														$('#wareSelect').dialog('open');
													});
												}
											}
									});
								}
							}
					});
				}
			});
		}
	});
})(jQuery)