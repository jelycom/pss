;(function($){
	var formwidth=700;
    var viewParameter={
			width:formwidth,viewPagerButtons:false,closeOnEscape:true,labelswidth:"",
			onClose:function(){
				$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
			},
			beforeShowForm:function(formid){
				var $grid=arguments[1]?arguments[1]:$(this),
					colModels=$grid[0].p.colModel;
				$.each(colModels,function(){				//判断某行显示时隐藏
					if (this.Ahidden){
						var tdname=$.insertString(this.name);
						$("#trv_"+tdname).hide();
					}
				});
				pagination(formid,$grid);
				jqdialogPosition(formid);					//弹出框定位
			}
		};//查看的参数配置
    function judgeTree($grid){
		var $tree_div=$grid.parents(".treedata_div").prev();
		if ($tree_div.length>0){
			var url=$grid.getGridParam("url")
			var $treeGrid=$(".tree_div table[id]"),
				meUrlName=url.substring(0,url.indexOf("_")),
				treeGridUrl=$treeGrid.getGridParam("url"),
				treeGridUrlName=treeGridUrl.substring(0,treeGridUrl.indexOf("_"));
			return [meUrlName==treeGridUrlName,$treeGrid];			//返回两个表格是否关联和树表格
		}
	}//判断树是否存在且是否关联
    function afterComplete(statu,node,form){
    	var $grid=$(this);
    	eval("var reObj="+statu.responseText);
		if (reObj.operate&&$(".tree_div table[id]")[0]&&!$grid.is("[id*=location]")){			//判断是否有树
			var reTree=judgeTree($(this));
			if (reTree&&reTree[0]){			//如果提交数据后更新了左边的树，则展开新添加节点及父级点
				var treeRow=reTree[1].find("#"+reObj.resultObj.id);
				var rc=new Rc(treeRow);
				nodeAncestors(reTree[1],rc);
				var parentID=reTree[1].getNodeParent(rc)._id_;		//将父级变量作为参数用于展开
				reTree[1].setSelection(parentID,true);
			}else{						//不更新左边的树，则展开新增记录对应的节点
				var idfield;
				$.each(node,function(i,n){
					if (i.indexOf(".id")>=0){
						idfield=n;
						return false;
					}
				});
				if (idfield)	var rc=new Rc($("#"+idfield,reTree[1]));			//获取新增节点
				else	var rc=new Rc($("#"+reTree[1].jqGrid("getGridParam","selrow"),reTree[1]));
				nodeAncestors(reTree[1],rc);
				if(reTree[1].getNodeParent(rc))		$("#"+rc._id_,reTree[1]).trigger("dblclick");
			}
		}else{
			var initFilter=$grid.data("initFilter");
			$grid.setGridParam({page:1,postData:{"filters":initFilter}}).trigger("reloadGrid");
		}
    }//编辑和新增后的判断
    
	$.fn.extend({
		"selfGrid":function(options){
			var formatfuncs={};
			var naviPrm={};
			for (var i=1;i<arguments.length;i++){
				var tmp=arguments[i];
				$.each(tmp,function(index,n){			//传入的参数作为函数集存入formatfuncs
					if (typeof n=="function"){
						formatfuncs=tmp;
						return false;
					}else {
						naviPrm=tmp;
						return false;
					}
				});
				//continue;
			}
			options=$.extend({},$.fn.selfGrid.defaults.options,options);
			$.each(naviPrm,function(i,n){
				naviPrm[i]=$.extend({},$.fn.selfGrid.defaults.naviPrm[i],n);
			});
			naviPrm=$.extend({},$.fn.selfGrid.defaults.naviPrm,naviPrm);
			
			//迭代取回的json数据并将其生成select标签
	        function eachitem(jsonitems,headerkey,headervalue){
	        	var select="<select>";
	        	if(headerkey){
	        		select+="<option value="+headerkey+">"+headervalue+"</option>";
	        	}
	        	if(jsonitems&&jsonitems.rows.length>0){
	        		rows=jsonitems.rows;
	        		for(var i=0;i<rows.length;i++){
	        			var dispName=!rows[i].dispName?rows[i].name:rows[i].dispName;
	        			select+="<option value="+rows[i].id+">"+dispName+"</option>";
	        		}
	        	}
	        	select+="</select>";
	        	return select;
	        }
	        
	      //初始化日期格式及按钮的function
	    	function initdate(elem,yearRange){
		    	setTimeout(function() {
		    		$(elem).datepicker({
		    			showOn: "focus",
		    			autosize:true,
		    			//buttonImage: "ui-icon-calculator",
		    			dateFormat:"yy-mm-dd",
		    			changeMonth: true,
		    			changeYear: true,
		    			showButtonPanel:true,
		    			yearRange:yearRange,
		    			showWeek:true
		    		});
//		    		$(elem).parent().find("button").button({icons:{primary:"ui-icon-calculator"},text:false}).css("left","3px");
		    	},10); 
			}
	        
	        return this.each(function(){
				var $grid=$(this),
					editcolnum=parseInt($grid.attr("editcolnum"))||3;				//form编辑时需要几列
				var sequence=0,hidecol=[];
				$.each(options.colModel,function(i,col){
					var editoptions={},
						searchoptions={};
					if (!col.index)					col.index=col.name;							//index默认使用name
					if (col.editable==undefined)	col.editable=true;							//默认可编辑
					if (!col.align)					col.align="left";							//默认左对齐
					if (formatfuncs[col.formatter])	col.formatter=formatfuncs[col.formatter];	//有则使用自定义格式化函数
					if (col.gridview==undefined)	col.gridview=true;							//默认列可见
					if (col.editable){					//允许编辑的情况下设置editoptions和searchoptions
						if (col.edittype=="textarea"||(i>0?options.colModel[i-1].lineFeed:false)){								//文本框或需要换行时另起一行
							var pos=sequence%editcolnum;
							if (pos>0)	sequence+=editcolnum-pos;
						}
						if (!col.editoptions)		col.editoptions={};
						if (!col.searchoptions)		col.searchoptions=$.extend({searchhidden:true},col.editoptions);
						if (!col.formoptions)		col.formoptions={};
						if (!col.editrules)			col.editrules={required:false};
						var rowpos=Math.floor((sequence+editcolnum)/editcolnum);
						var colpos=sequence%editcolnum+1;
						$.extend(col.formoptions,{rowpos:rowpos,colpos:colpos});			//编辑时的列位置和行位置
//						if (col.edittype=="textarea"||(i>0?options.colModel[i-1].lineFeed:false))	sequence+=editcolnum;
//						else	sequence++;
						sequence++;
						switch (col.edittype){
				        	case undefined:					//文本输入框默认搜索条件
					        	if (col.formatter!='date'||col.formatter!='number'||col.formatter!='currency')	searchoptions={sopt: ["eq", "cn","bw"]};
					        	if (col.dataUrl||col.completeGrid){				//有关联属性的输入框
					        		editoptions={
		        						maxlength:3,url:col.dataUrl?col.dataUrl:"",
		        						dataInit:function(elem){
		        							var e=jQuery.Event("keydown");
											e.keyCode=50;
		        							setTimeout(function(){
		        								if (col.completeGrid){
			        								var options=col.completeGrid($(elem));
			        								$(elem).completeGrid(options);
			        								$(elem).change(function(){
														$(elem).trigger(e);
													});
			        							}
		        							});
		        						}
		        					};
					        		searchoptions={sopt: ["eq","ne"]};
					        	}
					        	break;
				        	case "select":
				        		var jsonName=col.jsonName||"resultObj";
				        			headerkey=col.headerkey||"-1",
				        			headervalue=col.headervalue||"-请选择-";
				        		if(col.dataUrl){
									editoptions={
										dataUrl:col.dataUrl,
										buildSelect:function(data){
											var json=data;
											if(typeof(data)=="string"){
												json=$.parseJSON(data);
											}
											if(json&&jsonName){
												var items=$.myjqgrid.getAccessor(json,jsonName);
												var s=eachitem(items,headerkey,headervalue);
												return s;
											}
										}
									};
								}
								searchoptions={sopt:["eq","ne"]};
								col.stype=col.edittype;
				        		break;
					        case "custom":					//自定义输入框的editoptions和searchoptions
					        	editoptions={
									custom_element:function(value,options){
										var el=document.createElement("input");
										el.type="text";
										el.value=value;
										$(el).addClass("FormElement ui-widget-content ui-corner-all");
										if (col.dataUrl)	$(el).attr("url",col.dataUrl)
										return el;
									},
									custom_value:function(elem){
										return $(elem).val();
									}
								};
								searchoptions={sopt:["eq","ne"],
										dataInit:function(elem){
											var $elem=$(elem);
											var e=jQuery.Event("keydown");
											e.keyCode=50;
											setTimeout(function(){
												relevanceInput($elem,col.dataUrl);
												$elem.change(function(){
													$elem.trigger(e);
												});
											},20);
										}};
				        		break;
					        case "checkbox":
					        	searchoptions={ sopt: ["eq", "ne"], value: "1:是;0:否" };
					        	if (!col.editoptions.value)	editoptions={value:"true:false"};
					        	if (!col.width) col.width=50;
					        	$.extend(col,{formatter: "checkbox", align: "center",stype: "select"});
					        	break;
					        case "textarea":
					        	editoptions={rows:5,dataInit:function(el){
					        		$(el).removeAttr("cols");
					        		setTimeout(function(){
					        			if ($(el).parents().is("form")){
					        				var $td=$(el).parent("td");
					        				var colNum=$td.nextAll().length+1;
					        				$td.attr("colspan",colNum).nextAll().remove();
					        			}
					        		},50);
					        	}};
					        	searchoptions={sopt: ["eq", "cn","bw"]};
					        	if (col.dataUrl){				//有关联属性的输入框
					        		editoptions={
		        						url:col.dataUrl,rows:3,
		        						dataInit:function(elem){
		        							var e=jQuery.Event("keydown");
											e.keyCode=50;
		        							setTimeout(function(){
		        								if ($(elem).parents().is("form")){
							        				var $td=$(elem).parent("td");
							        				var colNum=$td.nextAll().length+1;
							        				$td.attr("colspan",colNum).nextAll().remove();
							        			}
		        								if (col.completeGrid){
			        								var options=col.completeGrid($(elem));
			        								$(elem).completeGrid(options);
			        								$(elem).change(function(){
														$(elem).trigger(e);
													});
			        							}
		        							},50);
		        						}
		        					};
					        	}
					        	break;
				        	default:
				        		break;
						}
						switch (col.formatter) {
							case "date":
								col.formatoptions={srcformat:"Y-m-d",newformat:"Y-m-d"};
				        		searchoptions={sopt:["eq","gt","lt","ge","le"]};
				        		editoptions={dataInit:function(elem){
				        			var yearRange=col.editoptions.yearRange?col.editoptions.yearRange:"-10:+5";
				        			initdate(elem,yearRange);
				        		}};
				        		if (!col.width)	col.width=100;
								$.extend(col.editrules,{date:true});
								break;
							case "number":
								searchoptions={sopt:["eq","gt","lt","ge","le"]};
								$.extend(col.editrules,{number:true});
								break;
							case "email":
								$.extend(col.editrules,{email:true});
								break;
							case "currency":
								searchoptions={sopt:["eq","gt","lt","ge","le"]};
								$.extend(col.editrules,{number:true});
								if (!col.formatoptions)		col.formatoptions={decimalPlaces:2,thousandsSeparator:","};
								$.extend(col,{align:"right"});
								break;
							default:
								break;
						}
						if (col.editrules.number){
							editoptions={
									dataInit:function(elem){
										$(elem).keyup(function(){
				    		        		CheckInputIntFloat(this);
				    		        	});
									}
							};
						}
						if (col.editrules.required)	$.extend(col.formoptions,{elmsuffix:"<a style='color:red'>(*)</a>"});
						if (col.editrules.edithidden!=false) $.extend(col.editrules,{edithidden:true});
						if (!col.gridview)	col.hidden=true,col.viewable=false;
						$.extend(col.editoptions,editoptions);
						$.extend(col.searchoptions,editoptions,searchoptions);
					}else{
						col.searchoptions={sopt: ["eq","ne","cn","nc","bw","ew"]};
						if (col.viewable==undefined)	col.viewable=false;
						if (col.lineFeed)	sequence+=editcolnum;
						if (col.name=="item")	col.searchoptions={sopt: ["eq", "cn","bw"]};
					}
					if (col.editrules&&col.editrules.number)	col.align="right";
				});
				$grid.jqGrid(options).navGrid(options.pager,naviPrm.parameter,naviPrm.edit,naviPrm.add,naviPrm.del,naviPrm.search,naviPrm.view);
				//将自定义按钮移动到toolbar，初始化禁用编辑、查看、删除
		        var $gridDiv=$grid.parents(".ui-jqgrid");
		        var $toolbar=$("#t_"+$grid.attr("id"),$gridDiv);
		        $gridDiv.find(".ui-jqgrid-pager .navtable").prependTo($toolbar);
		        $(options.pager+"_left").remove();
		        if (options.datatype!="local")	$toolbar.find("td[id*=edit],td[id*=view], td[id*=del]").css("zoom","1").addClass("ui-state-disabled");
		        else	$toolbar.find("td[id]").css("zoom","1").addClass("ui-state-disabled");
			});
		},//主表插件
		
		"treeTable":function(){
			var options={},tableReload=null;
			for (var i=0;i<arguments.length;i++){
				var tmp=arguments[i];
				if (typeof tmp=="object"){
					$.each(tmp,function(i,n){
						if (typeof n=="function"){
							tableReload=n;
							return false;
						}else{
							options=tmp;
							return false;
						}
					});
				}
			}
			return this.each(function(){
				var $treeGrid=$(this),
					gridID=$treeGrid.attr("id")
					lastsel='';
				var treeGridSet={
						treeGrid:true,
						pager:"#"+gridID+"Pager",
						colNames:["item","name","py"],
						colModel:[
						        {name:"item",hidden:true}
						      ,	{name:"name",editable:false,sortable:false}
						      ,	{name:"py",hidden:true}
						],
						rowNum:10000,
						viewrecords:false,
						toolbar:[false,''],
						hidegrid:false,					//不显示隐藏grid按钮
						ondblClickRow:function(rowid){
							lastsel=undefined;
							$treeGrid.setSelection(rowid,true);
						},
						onSelectRow:function(id){
				        	if (id&&id!=lastsel){			//不是上一次点击的行就重新加载主表的内容
				        		lastsel=id;
					     	   	var rc=new Rc($("#"+id),$treeGrid);
					        	var rowdata=$treeGrid.getRowData(id);
						        $(".selectedFiled td:odd:eq(0)").text(rowdata.item);
						        $(".selectedFiled td:odd:eq(1)").text(rowdata.name||rowdata.fullName||rowdata.shortName);
						        if ($treeGrid.getNodeParent(rc)){
						        	var parentData=$treeGrid.getRowData($treeGrid.getNodeParent(rc)._id_);
						        	$(".selectedFiled td:odd:eq(2)").text(parentData.name||parentData.fullName||parentData.shortName);
						        }else{$(".selectedFiled td:odd:eq(2)").text("　　　");}
						        var $table=$(".treedata_div .ui-jqgrid-btable:first");
						        var rules;
						        if (!tableReload){						//默认的主表重载方式：选中行时将选中行的lft、rgt和level值作为搜索参数传给主表，并执行搜索
						        	var level=Number(rc.level)?Number(rc.level)+1:1;
						        	rules='{"groupOp":"AND",' +
												'"rules":[{"field":"lft","op":"gt","data":"'+rc.lft+'"},' +
														'{"field":"rgt","op":"lt","data":"'+rc.rgt+'"},' +
														'{"field":"depth","op":"eq","data":"'+level+'"}]}';
							        $table.jqGrid('setGridParam',{page:1,datatype:"json",postData:{'filters':rules}}).trigger("reloadGrid");
						        }else{
						        	rules=tableReload($table,id,$treeGrid);					//使用传入的方式重载主表
						        }
						        $table.data("initFilter",rules);							//先缓存表格的原始过滤条件
				        	}
						},
						loadComplete:function(xhr){
							$.extend($.fn.completeGrid.defaults.treeData,xhr);				//加载成功后将返回的数据缓存到一个变量中
							gridSize($treeGrid);
							lastsel=undefined;
							$(this.grid.hDiv).hide();
							var $searchArea=$("<span class='searchNodeArea'><input type='text' id='searchNode' />"+
												"<a id='searchButton' href='#'><span class='ui-icon ui-icon-search'></span></a></span>");
							if (!$(this.grid.cDiv).is(":has(.searchNodeArea)"))	$(this.grid.cDiv).append($searchArea);		//加载一个过滤树的输入框
						}
					};
				var newoptions=$.extend({},$.fn.selfGrid.defaults.options,treeGridSet,options);
				var url=newoptions.url,
					pager=newoptions.pager,
					moveName=url.substring(0,url.indexOf("_"));
				$treeGrid.jqGrid(newoptions);
				if (pager){
					$treeGrid.jqGrid("navGrid",pager,{edit:false,add:false,del:false,search:false,refresh:false})
				  	$treeGrid.navButtonAdd(pager,{caption:"",title:"First",buttonicon:"ui-icon-arrowstop-1-n",onClickButton:locateLast});
					$treeGrid.navButtonAdd(pager,{caption:"",title:"Pre",buttonicon:"ui-icon-arrow-1-n",onClickButton:locateLast});
					$treeGrid.navButtonAdd(pager,{caption:"",title:"Next",buttonicon:"ui-icon-arrow-1-s",onClickButton:locateLast});
					$treeGrid.navButtonAdd(pager,{caption:"",title:"Last",buttonicon:"ui-icon-arrowstop-1-s",onClickButton:locateLast});
					$treeGrid.jqGrid("navButtonAdd",pager,{
						caption:"",title:"刷新",buttonicon:"ui-icon-refresh",onClickButton:function(){
							var selrow=$treeGrid.getGridParam("selrow");
							$treeGrid.unbind("ajaxSuccess").bind("ajaxSuccess",{selrow:selrow},function(e,r,s){
								if(e.data.selrow&&s.url.indexOf("tree")>0){
									nodeAncestors($treeGrid,new Rc($("#"+e.data.selrow,$treeGrid)));
									$treeGrid.resetSelection();
									$treeGrid.jqGrid("setSelection",e.data.selrow,false);
								}
							});
							$treeGrid.trigger("reloadGrid");
						}
					});
				}
				function locateLast(e){
					var $grid=$(this),
						title=e.currentTarget.title,
						selrow=$treeGrid.getGridParam("selrow");
					if(selrow){
						$.ajax({url:moveName+"_move"+title+".action",cache:false,data:"id="+selrow,success:function(){
							$grid.jqGrid("resetSelection").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
							setTimeout(function(){
								nodeAncestors($grid,new Rc($("#"+selrow,$grid)));
								$grid.jqGrid("setSelection",selrow,false);
							},400);
						}});
					}
				}
//				function locateLast($grid,selrow){
//					$grid.jqGrid("resetSelection").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
//					setTimeout(function(){
//						nodeAncestors($grid,new Rc($("#"+selrow,$grid)));
//						$grid.jqGrid("setSelection",selrow,false);
//					},400);
//				}
			});
		},//treeGrid插件
		
		'jxcGrid':function(options){
			return this.each(function(){
				var lastSel;
				var $jxcGrid=$(this);
				$.each(options.colModel,function(i,col){
					var editoptions={};
					col.sortable=false;															//不允许排序
					if (!col.index)					col.index=col.name;							//index默认使用name
					if (col.editable==undefined)	col.editable=true;							//默认可编辑
					if (col.gridview==undefined)	col.gridview=true;							//默认列可见
					if (col.editable){					//允许编辑的情况下设置editoptions和searchoptions
						if (!col.editoptions)		col.editoptions={};
						if (!col.editrules)			col.editrules={};
						if (!col.formatoptions)		col.formatoptions={};
						switch (col.formatter) {
							case "date":
								col.formatoptions={srcformat:"Y-m-d",newformat:"Y-m-d"};
				        		editoptions={dataInit:function(elem){
				        			var yearRange=col.editoptions.yearRange?col.editoptions.yearRange:"-10:+5";
				        			initdate(elem,yearRange);
				        		}};
								$.extend(col.editrules,{date:true});
								break;
							case "number":
								$.extend(col.editrules,{number:true});
								break;
							case "currency":
								$.extend(col.editrules,{number:true});
								$.extend(col.formatoptions,{decimalPlaces:2,thousandsSeparator:","});
								$.extend(col,{align:"right"});
								break;
							default:
								break;
						}
						if (col.editrules.number){
							col.align="right";
							editoptions={
									dataInit:function(elem){
										$(elem).keyup(function(){
				    		        		CheckInputIntFloat(this);
				    		        	});
									}
							};
						}
						switch (col.edittype){
				        	case undefined:					//文本输入框默认搜索条件
					        	if (col.dataUrl||col.completeGrid){				//有关联属性的输入框
					        		editoptions={
		        						url:col.dataUrl,
		        						dataInit:function(elem){
		        							var e=jQuery.Event("keydown");
											e.keyCode=50;
		        							setTimeout(function(){
		        								if (col.completeGrid){
			        								var options=col.completeGrid($(elem));
			        								$(elem).completeGrid(options);
			        								$(elem).change(function(){
														$(elem).trigger(e);
													});
			        							}
		        							});
		        						}
		        					};
					        	}
					        	break;
					        case "checkbox":
					        	searchoptions={ sopt: ["eq", "ne"], value: "1:是;0:否" };
					        	editoptions={value:"1:0"};
					        	$.extend(col,{width:"50",formatter: "checkbox", align: "center",stype: "select"});
					        	break;
				        	default:
				        		break;
						}
						if (!col.gridview)	col.hidden=true,col.viewable=false;
						$.extend(col.editoptions,editoptions);
						$.extend(col.editrules,{edithidden:true});
					}
				});
				var jxcGridSet={
					url:' ',
					footerrow:true,
					height:140,
					altRows:false,
					rownumbers:true,
					rownumWidth:35,
					pager:false,
					multiselect:false,
					cellsubmit:"clientArray",
					blankrows:5,					//进销存表格默认5个空行
					toolbar:[false,""],
					onSelectRow:function(id){
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
			        						selrow=$completeGrid.getGridParam("selrow");
//			        						colNames=$completeGrid[0].p.colNames,
//			        						colModel=$completeGrid[0].p.colModel,
//			        						colIndex=[];
//			        					$.each(colModel,function(i,n){
//			        						colIndex.push(n.name);			//将列名称存为数组，以便获取索引
//			        					});
			        					if (!$(this).val()){				//删除名称时清除这一行
			        						var colName=$jxcGrid[0].p.colNames,
			        							rd=$jxcGrid.getRowData(rowid);
			        						$jxcGrid.jqGrid("restoreRow",rowid);
			        						$jxcGrid.delRowData(rowid);
			        						$jxcGrid.addRowData(rowid,blankData,"last");
			        						if ($.inArray('订单号',colName)>0){					//如果是收货单和出货单
			        							var orders=$jxcGrid.getCol('orderBillID'),
			        								$advance=$jxcGrid.parents('form').find('[name=advance]');
			        							if ($.inArray(rd.orderBillID,orders)<0){		//预付（收）款金额扣除不存在的订单预付（收）
			        								var advance=$advance.val(),
			        									orderAdvance=$advance.data(rd.orderBillID);
			        								$advance.val(advance-orderAdvance);
			        							}
			        						}
			        						calculateTotalrow();
			        						return false;
			        					}
			        					gridData=$completeGrid.getRowData(selrow);
			        					$.each(gridData,function(i,n){
			        						if (i=="id"){
			        							$("td[aria-describedby*=productId]",$rowObj).text(n);
			        							var warehouseId=$("#warehouseId,#outWarehouseId").val();					//获取仓库ID
			        							if (warehouseId){											//如果有仓库ID请求获取仓库下该商品的库存数量并显示
			        								$.ajax({
				        							    url:'productStock_findquantity.action',
				        							    type:'post',
				        							    data:{warehouseId:warehouseId,productId:n},
				        							    success:function(data){
				        							    	var $input=$(":text[id$=ame]",$rowObj),
				        							    		position=$input.position(),
				        							    		top=position.top+$input.outerHeight(true),
				        							    		right=$rowObj.width()-position.left-$input.outerWidth(true);	//获取定位数据,在输入框的右下方显示
				        							    	var $tips=$(".stockTips"),
				        							    		$positionDiv=$jxcGrid.parent();
				        							    	if (!$positionDiv.is(":has(.stockTips)")){
				        							    		$tips=$("<span class='stockTips' style='top:"+top+"px;right:"+right+"px;'>库存："+data.resultObj+"</span>");
				        							    		$positionDiv.append($tips);
				        							    	}else{
				        							    		$tips.css({'top':top+'px','right':right+'px'}).text("库存："+data.resultObj);
				        							    	}
//				        							    	$tips.show();
				        							    	$tips.slideDown(600,function(){
				        							    		setTimeout(function(){
				        							    			$tips.slideUp(500);
				        							    		},600);
				        							    	});
				        							    }
				        							});
			        							}
			        						}else{
												var $td=$("td[aria-describedby*=_"+i+"]",$rowObj);
			        							$td.children().is(":text")?$td.children().val(n):$td.text(n);
			        						}
			        					});
			        				});
			    		        	var $price=$("input[name=price]",$rowObj),
			    		        		$quantity=$("input[name=quantity]",$rowObj),
			    		        		$sum=$("input[name=subTotal]",$rowObj),
			    		        		$taxPrice=$("input[name=taxPrice]",$rowObj),
			    		        		$tax=$("input[name=tax]",$rowObj),
			    		        		$amount=$("input[name=amount]",$rowObj),
			    		        		taxRate=!$("#taxRate").attr("initValue")?0.17:$("#taxRate").attr("initValue");
			    		        	$rowObj.find(":text:last").keydown(function(e){				//行内最后一个输入框按键事件
			    		        		if (e.which==13||e.which==39){							//回车和右箭头保存编辑行并跳转到下一行
			    		        			if (!$rowObj.next()[0]){							//最后一行最后一格时添加一个空行并选中
			    		        				$jxcGrid.addRowData(Number(rowid)+1,{});
			    		        			}
			    		        			$jxcGrid.setSelection(Number(rowid)+1);
			    		        		}
			    		        	});
			    		        	$rowObj.find(":text:first").keydown(function(e){				//行内第一格时按左键头跳转到上一行
			    		        		if(e.which==37){
			    			        		$jxcGrid.setSelection(Number(rowid)-1);
			    			        		$rowObj.prev().find("input:last").trigger("focus").trigger("select");
			    		        		}
			    		        	});
			    		        	if (!$quantity.val())	$quantity.val(1).trigger("change");
		    		        		$quantity.change(function(){									//数量变化时
		    		        			var quantity=Number($quantity.val()),
		    		        				price=$price.val(),
		    		        				amount=$sum.val();
		    		        			if (quantity&&price)			$sum.val((quantity*price).toFixed(2)).trigger("change");	//有单价计算金额
		    		        			if (quantity&&!price&&amount)	$price.val((amount/quantity).toFixed(4));					//没有单价有金额计算单价
		    		        			if (!quantity)					$sum.add($price).add($tax).add($taxPrice).add($amount).val('');	
		    		        			calculateTotalrow($jxcGrid,$rowObj)
		    		        		});
		    		        		$price.click(function(){
		    		        			var productId=$.trim($("td[aria-describedby*=productId]",$rowObj).text());	//获取商品ID
		    		        			if (!productId)	return false;
		    		        			var data=$price.parent().data("price");
		    		        			if (!data||data.productId!=productId){
			    		        			$.ajax({															//通过ID请求该商品的信息
			    		        				url:"product_listjson.action",
			    		        				type:"post",
			    		        				data:{'filters':'{"groupOp":"AND","rules":[{"field":"id","op":"eq","data":"'+productId+'"}]}'},
			    		        				error:function(){
			    		        					alert("error");
			    		        				},
			    		        				success:function(data){
			    		        					if (data.operate){
			    		        						var result=data.resultObj.rows[0];
			    		        						var priceArray=[];
			    		        						for (var i=1;i<4;i++){
			    		        							priceArray.push({name:'预售价'+i,price:!result["salePrice"+i]?0:result["salePrice"+i]});
			    		        						}
			    		        						var priceCache={"productId":result.id,"priceData":{"rows":priceArray}};
			    		        						$price.parent().data("price",priceCache);
			    		        						dialogPrice(priceCache.priceData);
			    		        					}
			    		        				}
			    		        			});
		    		        			}
		    		        			else	dialogPrice(data.priceData);//弹出单价选择框
		    		        			function dialogPrice(dataStr){
		    		        				var $priceDiv=$("#priceDiv");
			    		        			if (!$priceDiv[0]){
			    		        				$priceDiv=$("<div id='priceDiv' stype='display:none;'><div class='gridTableDiv'><table id='priceGrid'></table>"
			    		        							+"</div></div>");
			    		        				$("body").append($priceDiv);
			    		        				$priceDiv.dialog({
			    		        					autoOpen:false,modal:true,stack:false,resizable:false,
			    		        					minHeight:false,height:180,width:150,title:"请选择单价",
			    		        					open:function(){
			    		        						gridSize($("#priceGrid"));
			    		        					},
			    		        					close:function(){
			    		        						var jxcrow=$priceDiv.attr("rowid");
														$("#"+jxcrow+" input[name=price]").trigger("focus").trigger("select");
			    		        					}
				    		        			});
			    		        				$("#priceGrid").jqGrid({
			    		        					datastr:dataStr,
													datatype:"jsonstring",
													colNames:["价格类型","单价"],
													colModel:[{name:"name"},{name:"price",align:"right"}],
													jsonReader:{
														root: "rows", 
														repeatitems:false
													},
													rowNum:100,					//每页的总行数
													shrinkToFit:true,			//按比例初始化列宽度
													forceFit:true,				//为真时，改变列宽度不改变表格的宽度
													width:150,
													ondblClickRow:function(id,r,c,e){
														$priceDiv.dialog("close");
														var jxcrow=$priceDiv.attr("rowid");
														$jxcGrid.setSelection(jxcrow);
														$("#"+jxcrow+" input[name=price]",$jxcGrid).trigger("focus").trigger("select")
																					.val($(this).getCell(id,1)).trigger("change");
													}
			    		        				});
			    		        			}else{
			    		        				$("#priceGrid").setGridParam({datastr:dataStr,datatype:"jsonstring"}).trigger("reloadGrid");
			    		        			}
			    		        			$priceDiv.attr("rowid",rowid).dialog("open");
		    		        			}
		    		        		}).change(function(){
		    		        			var quantity=parseFloat($quantity.val()),
		    		        				price=Number($price.val());
		    		        			if (quantity&&$price.val().length){
		    		        				if (!$sum[0])	$amount.val((quantity*price).toFixed(2)).change();
		    		        				else	$sum.val((quantity*price).toFixed(2)).trigger("change");				//有单价和数量计算金额
		    		        			}
		    		        			else	$sum.add($tax).add($taxPrice).add($amount).val('');					//缺一个金额置空
		    		        		});
		    		        		$sum.change(function(){
		    		        			var quantity=Number($quantity.val()),
		    		        				subTotal=Number($sum.val()),
		    		        				price=(subTotal/quantity).toFixed(4);
		    		        			if (quantity&&$sum.val().length){
	    		        					$amount.val((subTotal+subTotal*taxRate).toFixed(2));
		    		        				if ($sum.attr("id")!=$amount.attr("id")){
		    		        					$amount.trigger("change");
		    		        					$sum.val(subTotal.toFixed(2));
		    		        				}
		    		        				$price.val(price);			//有数量和金额计算单价
		    		        			}
		    		        			else if(!subTotal)	$sum.add($price).add($tax).add($taxPrice).add($amount).val('');					//缺一个单价置空
		    		        			calculateTotalrow($jxcGrid,$rowObj);
		    		        		});
		    		        		$amount.change(function(){
		    		        			var quantity=Number($quantity.val()),
	    		        					amount=Number($amount.val());
		    		        			if (quantity&&$amount.val().length){
		    		        				if (!$sum[0]){
		    		        					$price.val((amount/quantity).toFixed(4));
		    		        				}else{
		    		        					$taxPrice.val((amount/quantity).toFixed(2));
		    		        					$tax.val(((amount*taxRate)/(1+taxRate)).toFixed(2));
		    		        					$sum.val((amount-$tax.val()).toFixed(2));
		    		        					$price.val(($sum.val()/quantity).toFixed(4));
		    		        				}
		    		        			}
		    		        			calculateTotalrow($jxcGrid,$rowObj);
		    		        		});
			        			}//编辑时执行的操作
			        		};
				        	var $blankTr,gridData,currentData,editID;		//第一个空行、自动完成选中的行、当前选中行、需要编辑的行
				        	
				        	if (id!=lastSel&&lastSel){				//已有选中行时，保存已选中行
				        		if(!$("#"+lastSel+"_fullName").val()){
				        			$jxcGrid.restoreRow(lastSel);	//商品为空时，恢复成空行
				        			var total=calculateTotalrow();
				        			inputCalculate(total);
				        		}else{
				        			$jxcGrid.saveRow(lastSel,{url:"clientArray"});
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
				        			if ($("#"+lastSel+" :text:first",$jxcGrid).val()!="")	$jxcGrid.jqGrid("saveRow",lastSel,{url:"clientArray"});
				        			else	$jxcGrid.jqGrid("restoreRow",lastSel);
				        			$jxcGrid.resetSelection();
				        			lastSel=undefined;
				        		}
				        		e.stopPropagation();
				        	});
				        	
				        	$jxcGrid.parents(".definedForm").on("dialogbeforeclose",function(){
				        		if (lastSel){
				        			$jxcGrid.restoreRow(lastSel).resetSelection();
				        			lastSel=undefined;
				        		}
				        	});	
			        	}
			        },
					beforeRequest:function(){
						var url=document.URL;							//获取页面名称
						var $grid=$(this),										//当前表格
							page=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."))+$grid.attr("id");
						var colHidden=$.cookie(page+"hiddenAttr"),			//根据页面名称和表格id来读取cookie
							hiddenName=$.cookie(page+"hiddenName");
						if (colHidden){											//判断是否有cookie
							colHidden=colHidden.split(",");
							hiddenName=hiddenName.split(",");
							for (var i=0;i<colHidden.length;i++){
								$grid[colHidden[i]=="true"?'hideCol':'showCol'](hiddenName[i]);
							}
						}
						$grid.bind("hideShow",function(){
							var newModel=$grid.getGridParam("colModel"),			//表格尺寸变化时获取当前表格的colModel参数
								hiddenAttr=[],
								gridColName=[]; 
							for (var i=0;i<newModel.length;i++){
								hiddenAttr.push(newModel[i].hidden);						//将表格colModel的hidden属性值存到hiddenAttr数组中
								gridColName.push(newModel[i].name);							//将表格colModel的name属性值存到gridColName数组中
							}
							$.cookie(page+"hiddenAttr",hiddenAttr,{expires:10});		//将两个数组存到cookie中，cookie以页面名称和表格id作为名称
							$.cookie(page+"hiddenName",gridColName,{expires:10});
						});
						
						var $thbutton=$("#thbutton"),
							$hidecol=$(".hidecol");
						//鼠标移动标题行上时，显示隐藏列按钮
						$(this.grid.hDiv).find("th").mouseenter(function(){
							if ($hidecol.is(":hidden"))		$thbutton.show().appendTo($(this).find(".ui-jqgrid-sortable"));
						}).mouseleave(function(){
							if ($hidecol.is(":hidden"))		$thbutton.hide();
						});
						var data={};
						var colModel=$grid.getGridParam("colModel");
						$.each(colModel,function(i,n){
							if (i>0)	data[n.name]="";
						});
						for (var i=0;i<newoptions.blankrows;i++){
							$grid.addRowData(i+1,data,"last");						//添加一个空行
						}
						inputMove($jxcGrid.parents("form"));
						$(".add_product, .del_product").remove();
//						$(".add_product").click(function(){						//点击添加商品按钮
//							var rowNum=$grid.find("tr").length;
//							$grid.addRowData(rowNum,data);						//添加一个空行
//							return false;
//						});
//						$(".del_product").click(function(){
//							var selid=$grid.getGridParam("selrow");
//							if (selid){
//								$grid.restoreRow(selid).resetSelection();
//								$grid.setRowData(selid,data);
//							}else{
//								alert("请选择要删除的记录！");
//							}
//							calculateTotalrow($grid);
//							lastSel=undefined;
//							return false;
//						});
						$grid.footerData("set",{rn:"合计"});	//合计行
					}
				};
				var newoptions=$.extend({},$.fn.selfGrid.defaults.options,jxcGridSet,options);
				$jxcGrid.jqGrid(newoptions);
			});
		}//进销存表格插件
	});
	
	$.fn.selfGrid.defaults={
		options:{
        	caption:"", 
            url:"", 
            mtype:'POST', //'GET' or 'POST'
            datatype:"json",
            colNames:[],
            colModel:[],
            treeGridModel: 'nested', 					//treeGrid为true的配置
            treeIcons: {plus:'ui-icon-folder-collapsed',minus:'ui-icon-folder-open',leaf:'ui-icon-document'},
            ExpandColClick:true,
            ExpandColumn: 'name',
            rowNum:10, 
            rowList:[5,10,20],  	//可供选择的条数选项
            pager:"#pager",  		//分页工具栏
//          multikey:'ctrlKey',		//多选时按键
            autoencode:true,		//对URL进行编码,暂时没有起作用
            altRows:true,			//班马线
            multiselect:true, 		//是否可多选
        	multiboxonly:true,		//点checkbox才有效
            viewrecords:true,
            width:$(this).parents(".gridTableDiv").width()-2,
			sortable:true,  		//是否可排序
			sortorder: "asc",
			shrinkToFit:true,
			forceFit:true,
			toolbar:[true,"top"],	//定义工具栏?
			jsonReader: { 
            	root: "resultObj.rows", 
            	page: "resultObj.pageNo",	// json中代表当前页码的数据
				total: "resultObj.totalPages",	// json中代表页码总数的数据
				records: "resultObj.totalRows", // json中代表数据行总数的数据
            	repeatitems: false
            },
            ondblClickRow:function(rowid,iRow,iCol,e){					//双击行产生的事件，参数rowid为行ID，iRow为行索引，iCol为列索引，e为事件对象
				var $viewButton=$(this).parents(".ui-jqgrid").find("td[id^=view]");
				$viewButton.trigger("click");
			},
			onSelectAll:function(selarrrow,statu){
				var toolbar=$("#t_"+this.id);
				if (selarrrow.length){				//有记录时才进行操作
					if (statu)	toolbar.find(".navtable .ui-state-disabled").removeClass("ui-state-disabled");
					else	toolbar.find("td[id^=edit], td[id^=view],td[id^=del]").addClass("ui-state-disabled");
				}
			},
			onSelectRow:function(rowid,status){							//选中行时编辑、查看、删除可用，否则不可用
				var $grid=$(this);
				var row=$grid.find("#"+rowid);
				var toolbar=$("#t_"+this.id);
				if ($(".ui-state-highlight",$grid).length>0){
					toolbar.find(".navtable .ui-state-disabled").removeClass("ui-state-disabled");
				}else{
					toolbar.find("td[id^=edit], td[id^=view],td[id^=del]").addClass("ui-state-disabled");
				}
				if (this.id=="warehouse") {					//如果是仓库被选中，则加载该仓库的库位
					var $list2=$("#warehouseAllocation"),
						list2ID=$list2.attr("id");
					if (status){
						$("#t_"+list2ID+" td[id*=add]").removeClass("ui-state-disabled");
						$("#add_"+list2ID+", #edit_"+list2ID).livequery("click",function(e){		//新增时，货位所属仓库默认为选中id
							$("#warehouse\\.id").val(rowid);
							$("#tr_warehouse\\.id").hide();
						});
						var rules='{"groupOp":"AND","rules":[{"field":"warehouse.id","op":"eq","data":"'+rowid+'"}]}';
						$list2.jqGrid('setGridParam',{datatype:"json",postData:{'filters':rules}}).trigger("reloadGrid").setGridParam({postData:{"filters":null}});
						$list2.data("initFilter",rules);						//缓存过滤条件
					}else{
						$list2.jqGrid("clearGridData");
						$("#t_"+list2ID+" td[id]").addClass("ui-state-disabled");
					}
				}
			},
//			onPaging:function(){
//				if ($(this).data("initFilter"))	$(this).setGridParam({postData:{"filters":$(this).data("initFilter")}});
//			},
			onRightClickRow:function(rowid,iRow,iCol,e){
//				e.preventDefault();
//				var $grid=$(this),
//					$bdiv=$(this.grid.bDiv),
//					position=$(e.target).position(),
//					left=position.left+$(e.target).width(),
//					top=position.top;
//				var $rightkeyDiv=$("#rightkeyDiv");
//				if (!$bdiv.is(":has(#rightkeyDiv)")){
//					$rightkeyDiv=$("<div id='rightkeyDiv' class='smallParts' style='position:absolute;display:none'><ul class='rightFunc'>" +
//							"<li><button>删除</button></li>"+
//							"</ul></div>").appendTo($bdiv.children());
//					$rightkeyDiv.find("button").button({icons:{primary:"ui-icon-trash"}});
//				}
//				$rightkeyDiv.css({"left":left,"top":top}).show();
			},
			loadComplete:function(xhr){
				if (xhr&&xhr.resultObj&&$(this).attr("xhrcache")!="false")	var loadData=$.extend($.fn.selfGrid.defaults.loadData,xhr.resultObj.rows);			//加载成功后将返回的数据缓存到一个变量中
				var url=document.URL;									//获取页面名称
				var $grid=$(this),										//当前表格
					gridID=$grid.attr('id'),
					datatype=$grid.jqGrid("getGridParam","datatype"),
					page=url.substring(url.lastIndexOf("/")+1,url.indexOf("."))+$grid.attr("id");
				var colHidden=$.cookie(page+"hiddenAttr"),		//根据页面名称和表格id来读取cookie
					hiddenName=$.cookie(page+"hiddenName");
				newPostData=$grid.getGridParam("postData")
				if (colHidden){											//判断是否有cookie
					colHidden=colHidden.split(",");
					hiddenName=hiddenName.split(",");
					for (var i=0;i<colHidden.length;i++){//根据获取到的cookie属性，来判断显示还是隐藏列
						$grid[colHidden[i]=="true"?'hideCol':'showCol'](hiddenName[i]);
					}
				}
				gridSize($grid);
				$grid.bind("hideShow",function(){
					var newModel=$grid.getGridParam("colModel");			//表格尺寸变化时获取当前表格的colModel参数
					var hiddenAttr=[],gridColName=[]; 
					for (var i=0;i<newModel.length;i++){
						hiddenAttr.push(newModel[i].hidden);						//将表格colModel的hidden属性值存到hiddenAttr数组中
						gridColName.push(newModel[i].name);							//将表格colModel的name属性值存到gridColName数组中
					}
					$.cookie(page+"hiddenAttr",hiddenAttr,{expires:10});		//将两个数组存到cookie中，cookie以页面名称和表格id作为名称
					$.cookie(page+"hiddenName",gridColName,{expires:10});
				});
				$grid.fuzzySearch();
				
				var $toolbar=$("#t_"+this.id);
				$("#pager_center").width(210);
				if (datatype!="local"){
					$toolbar.find("td[id]:not(td[id^=edit],td[id^=view],td[id^=del])").removeClass("ui-state-disabled");	//数据来源是local时，所有按钮禁用
					$toolbar.find("td[id^=edit],td[id^=view],td[id^=del]").addClass("ui-state-disabled");
				}
	       		else	$toolbar.find("td[id]").css("zoom","1").addClass("ui-state-disabled");
				
				var $thbutton=$("#thbutton"),
					$hidecol=$(".hidecol");
				//鼠标移动标题行上时，显示隐藏列按钮
				$(this.grid.hDiv).find("th").mouseenter(function(){
					var th=$(this).find(".ui-jqgrid-sortable");
					if ($hidecol.is(":hidden"))	$thbutton.show().appendTo(th);
				}).mouseleave(function(){
					if ($hidecol.is(":hidden"))	$thbutton.hide();
				});				
			},
		    ajaxSelectOptions: {
			   type: "GET", // one need allows GET in the webmethod (UseHttpGet=true)     
			   contentType: 'application/json; charset=utf-8',    
			   dataType: "json",     
			   cache: false,     
			   data: {         
				   id: function () {  
					   var selid=$(this).jqGrid('getGridParam', 'selrow');
					   return selid?selid:-1;
				   	}   
	            }
		    }
		},
		naviPrm:{
			pager:"#pager",
			parameter:{
				addtext:"新增",edittext:"编辑",view:true,viewtext:"查看",deltext:"删除",searchtext:"搜索",refreshtext:"刷新",
				beforeRefresh:function(){
					if($(".tree_div")[0]&&$(this).is("[id!=location]")){		//刷新时左边有树，根据左边选择的行来重新加载主表
						$treeGrid=$(".tree_div .ui-jqgrid-btable")
						var id=$treeGrid.getGridParam("selrow");
						if (id){
							$("#"+id,$treeGrid).trigger("dblclick");
					    }
						return false;
					}
				},
				afterRefresh:function(){
					$(this).setGridParam({postData:{"filters":$(this).data("initFilter")}});	//刷新完成后根据表格缓存的过滤条件重新过滤
				}
			},
			edit:{
    			width:formwidth,jqModal:false,modal:true,closeAfterAdd:true,checkOnSubmit:true,closeAfterEdit: true,reloadAfterSubmit:false,
    			afterShowForm:function(formid){
    				var $grid=arguments[1]?arguments[1]:$(this),
    					colModels=$grid.getGridParam("colModel");
					$(".customelement").each(function(){
						if (!$(this).next().is("input"))   relevanceInput($(this));
					});
					var rowData;
					$.each($.fn.selfGrid.defaults.loadData,function(i,n){		//从缓存中读取该行数据
						if (n.id==$grid.getGridParam("selrow")){
							rowData=n;
							return false;
						}
					});
					setTimeout(function(){
						var completeCol=$(colModels).filter(function(){
							return this.completeGrid;
						});
						$.each(completeCol,function(i,n){
							var name=n.name,
								period=name.lastIndexOf(".");			//句点的位置
							var selector=name.replace(/\./gi,"\\.");
							if ($("#"+selector).next().is(':text')){
								$("#"+selector).val("").next().val("");
								if (period>=0){			//如果找到句点，给句点前加一个转义符，从缓存的数据中读取对应的数据
									var releAttr=name.substring(0,period),
										releVal=eval("rowData."+releAttr);
									if (releVal){
										if (!$.isArray(releVal)){
											$("#"+selector).val(eval("rowData."+name))
												.next().val(eval("rowData."+releAttr+".shortName?rowData."+releAttr+".shortName:rowData."+releAttr+".name")).removeAttr("disabled");
										}else{											//数据是数组的时候处理
											var idArray=[],nameArray=[];
											$.each(releVal,function(i,n){
												idArray.push(n.id);
												if (n.shortName)					nameArray.push(n.shortName);
												if (n.name)							nameArray.push(n.name);
												if (n.fullName&&!n.shortName)		nameArray.push(n.fullName);
											});
											$("#"+selector).val(idArray.toString()).next().val(nameArray.toString()).removeAttr("disabled");
										}
									}
								}else{
									var $treeGrid= $(".tree_div table[id]");
									var id=$treeGrid.jqGrid("getGridParam","selrow"),
									rc=new Rc($("#"+id,$treeGrid));
									$("#"+selector).val(rc._id_).next().val(rc.name).attr("disabled",false);
								}
							}
						});
					},100);
//					setTimeout(function(){
//						$("select",formid).dropkick();
//					},200);
					$.each(colModels,function(){			//判断某行编辑时显示，新增时隐藏
						if (this.Ahidden){
							var tdname=$.insertString(this.name);
							$("#tr_"+tdname).show();
						}
					});
					pagination(formid,$grid);
					setTimeout(function(){inputMove(formid);},20);
					jqdialogPosition(formid);		//弹出框定位
				},
				onClose:function(){
					$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
				},
				beforeSubmit:function(){
					console.log(arguments[0]);
					return [true,"修改成功"];
				},
				afterSubmit:function(r,node){
					var reTree=judgeTree($(this));			//用于判断左侧表格是否需要刷新，返回true或false和左侧的树
					if (reTree&&reTree[0])	reTree[1].resetSelection().trigger("reloadGrid");			//返回值为true时刷新
				},
				afterComplete:afterComplete
			},//editoptions
			add:{
				width:formwidth,jqModal:false,modal:true,closeAfterAdd:true,closeAfterEdit:true,reloadAfterSubmit:false,
				afterShowForm:function(formid){
					var $grid=arguments[1]?arguments[1]:$(this),
	    					colModels=$grid.getGridParam("colModel");
					formid.find(":input:not(:checkbox)").val("");
					$(".customelement").each(function(){
						if (!$(this).next().is("input"))	relevanceInput($(this));
					});
					if ($(".tree_div")[0]){			//有树时给父级输入框一个默认值
						var $treeGrid= $(".tree_div table[id]"),
							id=$treeGrid.jqGrid("getGridParam","selrow"),
							rc=new Rc($("#"+id,$treeGrid));
						setTimeout(function() {
							$("input[id*=\\.id],#pid").filter(":not([id^=warehouse])").val(rc._id_).next().val(rc.name).attr("disabled",true);
						},20);
					}
					$.each(colModels,function(){		//判断某行编辑时显示，新增时隐藏
						if (this.Ahidden){
							var tdname=$.insertString(this.name);
							$("#tr_"+tdname).hide();
						}
					});
					pagination(formid,$grid);
					setTimeout(function(){inputMove(formid);},20);
					jqdialogPosition(formid);		//弹出框定位
				},
				onClose:function(){
					$(".completeDiv").remove()
					$(".selectTree,.ui-widget-overlay").hide();
				},
				beforeSubmit:function(){
					console.log(arguments[0]);
					return [true,"添加成功"];
				},
				afterSubmit:function(r,node){
					var reTree=judgeTree($(this));			//用于判断左侧表格是否需要刷新，返回true或false和左侧的树
					eval("var reObj="+r.responseText);
					if (reTree&&reTree[0]&&reObj.operate)	reTree[1].resetSelection().trigger("reloadGrid");			//返回值为true时刷新
					return [reObj.operate,"不刷新"];
				},
				afterComplete:afterComplete
			},//addoptions
			del:{
				reloadAfterSubmit:false,
				afterShowForm:function(formid){
					jqdialogPosition(formid);		//弹出框定位
				},
				beforeSubmit:function(){
					$(this).jqGrid("setGridParam",{page:1});
					return [true,"删除成功"];
				},
				afterSubmit:function(){
					var reTree=judgeTree($(this));			//用于判断左侧表格是否需要刷新，返回true或false和左侧的树
					if (reTree&&reTree[0])	reTree[1].trigger("reloadGrid");			//返回值为true时刷新
					return [true,"成功删除"];
				},
				afterComplete:function(){
					var $grid=$(this);
					var reTree=judgeTree($(this));
					if (reTree&&reTree[0]){			//左侧树刷新后展开原先选中行的祖先节点，并选中该行
						id=reTree[1].jqGrid("getGridParam","selrow");
						var rc=new Rc($("#"+id,reTree[1]));
						nodeAncestors(reTree[1],rc);
						reTree[1].jqGrid("resetSelection").setSelection(id,false);
					}else{
						var initFilter=$grid.data("initFilter");
						$grid.setGridParam({page:1,postData:{"filters":initFilter}}).trigger("reloadGrid");
					}
					$(toolbar).find("td[id^=edit], td[id^=view],td[id^=del]").addClass("ui-state-disabled");
				},
				onClose:function(){
					$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
				}
			},//deloptions
			search:{
				multipleSearch:true, multipleGroup:true, showQuery: true,closeOnEscape:true,recreateFilter:true,
				sopt:["eq","ne","cn","nc","lt","le","gt","ge","bw","bn"],
				afterRedraw:function(p){
				},
				afterShowSearch:function(formid){
					jqdialogPosition(formid);		//弹出框定位
					$(this).filterGrid();
				},
				onClose:function(){
					$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
				}
			},//searchoptions
			view:viewParameter//viewoptions
		},
		treeData:{},
		loadData:{}
	};//默认设置
})(jQuery);