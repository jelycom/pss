//编辑内容太多时分成两个tabs标签
	function pagination(formid,$grid){
		 if ((!formid.is(":has(.ui-tabs)"))&&$grid.attr("tabs")==1){
		 	var tabs = $('<div id="newEditForm">'
		 				+'<ul><li><a href="#tabs-1">主要信息</a></li></ul>'
		 				+'<div id="tabs-1"></div></div>');
		 	formid.prepend(tabs);
		 	var tr=formid.find("tr[rowpos]");
		 	var tableid=tr.parents("table").attr("id");
		 	var lineFeedName=[],
		 		tabsTitle=[],
		 		colName=[],
		 		colModel=$grid[0].p.colModel;
			$.each(colModel,function(i,n){				//先获取需要创建的tabs名称和标题
				colName.push(this.name);
				if (n.editable&&n.lineFeed){
					if (!colModel[i+1].editable)	i++;
					lineFeedName.push(colModel[i+1].name);
					tabsTitle.push(colModel[i+1].tabsTitle);
				}
			});
			tabs.tabs({
				tabTemplate: "<li title='#{label}'><a href='#{href}'>#{label}</a></li>",
				show:function(event,ui){
					jqdialogPosition(formid);
					inputMove(formid);
				},
				create:function(e){
					if (lineFeedName.length<=1){//默认分为两页
						if (!lineFeedName.length){
							tabs.tabs( "add", "#tabs-2","辅助信息");
							tr.filter(":gt(2)").hide().end().filter(":lt(3)").show();
							tabs.data('endRecord',3);
						}
						else {
							tabs.tabs( "add", "#tabs-2",tabsTitle[0]);
							var name=$.insertString(lineFeedName[0]);
							var pos=Number(tr.filter('[id$='+name+']').attr('rowpos'));
							tr.filter(":gt("+(pos-2)+")").hide().end().filter(":lt("+(pos-1)+")").show();
							tabs.data('endRecord',pos-1);
						}
					}else{//自定义分页
						var endRecord=[];
						var lineFeedRow=tr.filter(function(){
							var rowid=$(this).attr("id");
							var rowName=rowid.substring(rowid.indexOf("_")+1);
							return $.inArray(rowName,lineFeedName)>=0;
						});//获取分页的起始行
						$.each(lineFeedName,function(i,n){
							tabs.tabs( "add", "#tabs-"+(2+i),tabsTitle[i]);
							endRecord.push(tr.index(lineFeedRow.eq(i)));//需要换行的行的索引存到数组中以便缓存
						});
						tabs.data("endRecord",endRecord);
					}
				},
				select:function(e,ui,form){
					tr.show();
					if (lineFeedName.length<=1){//默认分为两组，第一 组3行，第二组就是3行以后的所有行
						var pos=Number(tabs.data('endRecord'));
						if (ui.index==0)		tr.filter(":gt("+(pos-1)+")").hide();
						else if(ui.index==1)	tr.filter(":lt("+pos+")").hide();
						else					tr.hide();
					}else{
						var endRecord=tabs.data("endRecord");
						if (ui.index==0){//第一个
							tr.filter(":gt("+(endRecord[0]-1)+")").hide();
						}else if(ui.index==endRecord.length){//最后一个
							tr.filter(":lt("+endRecord[ui.index-1]+")").hide();
						}else{//其他
							tr.filter(":lt("+endRecord[ui.index-1]+")").hide();
							tr.filter(":gt("+(endRecord[ui.index]-1)+")").hide();
						}
					}
				}
			}).tabs("select",0);
		 } 		
	}
;(function ($) {
	$.myjqgrid=$.myjqgrid||{};
	$.extend($.myjqgrid,{getAccessor : function(obj, expr) {
		var ret,p,prm = [], i;
		if( typeof expr === 'function') { return expr(obj); }
		ret = obj[expr];
		if(ret===undefined) {
			try {
				if ( typeof expr === 'string' ) {
					prm = expr.split('.');
				}
				i = prm.length;
				if( i ) {
					ret = obj;
					while (ret && i--) {
						p = prm.shift();
						ret = ret[p];
					}
				}
			} catch (e) {}
		}
		return ret;
	}});
	
	//扩展jquery插件,字符串添加指定的字符
	$.insertString=function(string,code,instr){
		if (!string||typeof string!="string"){
			return "";
		}
		var period;
		if (!code){
			period=string.indexOf(".");
		}else{
			if (typeof code==="string"){
				period=string.indexOf(code);
			}else if(typeof code==="number"){
				period=code;
			}else{
				alert("位置参数错误");
			}
		}
		if (period>=0){
			if (!instr)	instr="\\";
			return string.substring(0,period)+instr+string.substring(period);
		}else{
			if (instr)	return string+instr;
			else return string;
		}
		
	};
	
	$.fn.extend({
	   /**
		 * @param url 请求的地址
		 * @param options 自定义的参数
		 * @param funcs   Button按钮的方法
		 */
		"bindTable":	function(){
   		   var $thwidth = $("thead th[thwidth]",$(this));
   		   var thlength = $("thead th",$(this)).length,
   		       widthlength = $thwidth.length,
   		       hiddenlength = $("thead th[hidden]",$(this)).length,
   		       parentWidth = $(this).parents(".gridTableDiv").width(),
   		       th_width=0;
   		   for (var i=0;i<widthlength;i++){
   		       th_width +=  Number($thwidth.eq(i).attr("thwidth"));
   		   }
		   var url=undefined;
		   var options=undefined;
		   var colname=[];
		   var colnameAttr=[];
		   var funcs={};
		   var newPostData;				//保存当前搜索条件
		   for ( var i = 0; i < arguments.length; i++) {
			   var tmp=arguments[i];
				if (typeof tmp=="string") {
					url=tmp;
					continue;
				}
				if (typeof tmp=="object") {//传入可能是jqgrid的设置,也可能是按钮的处理函数
					$.each(tmp,function(i,n){
						if (typeof n=="function") { //如果第1个就是function,那么就是处理函数
							funcs=tmp;
							return false;
						}else{
							options=tmp; //如果第1个不是function,那么就是设置.
							return false;
						}
					});
					continue;
				}
				
			}
		   //迭代取回的json数据并将其生成select标签
	        function eachitem(jsonitems,headerkey,headervalue){
	        	var select="<select>";
	        	if(headerkey){
	        		select+="<option value="+headerkey+">"+headervalue+"</option>";
	        	}
	        	if(jsonitems&&jsonitems.length>0){
	        		for(var i=0;i<jsonitems.length;i++){
	        			select+="<option value="+jsonitems[i].id+">"+jsonitems[i].name+"</option>";
	        		}
	        	}
	        	select+="</seelct>";
	        	return select;
	        }
	        //从一个装function对象取出属性为funname的function
	        function getfunction(funcs,funname){
	        	var func = funcs[funname]; 
	        	if (func == undefined) func = function () { eval(funname + "()"); }; 
	        	return func;
	        }

			return this.each(function(){   //可能匹配到多个元素,故用each();避免只作用一个
			//取页面参数开始,用于初始化jqGrid
				var me = $(this); //each里面的this是DOM对象,需要转换成jquery对象
				var tableid= me.attr("id");
				var tableurl=me.attr("url");
				var pagesize=me.attr("pagesize")||10; 
				var datatype=me.attr("datatype")||"json"; 
				if (!url&&!tableurl&&datatype!="local") {
					  $("<div>未设置列表URL:<br/>1.可在方法参数传入.<br/>2.或者表格属性中加入'url'属性</div>").dialog();
				  }else if(!url){ //命令行的地址优先于table的url属性
					  url=tableurl;
				  }
				var height=me.attr("height")||"auto";
				var multiselect = me.attr("multiselect") == "1"; 
				var title = me.attr("title") || document.title; 
				var pagerId = me.attr("pager") || "pager"; 
				var dataroot = me.attr("dataroot") || "resultObj.rows"; 
				var coltotal=parseInt(me.attr("colnum"))||3; //编辑时的列数
				var saveurl=me.attr("saveurl");
				var deleteurl=me.attr("deleteurl")||saveurl;
				var selerowfun=me.attr("selerowfun");//||function(){};
				var addbutton = me.attr("addButton")!=0;			//是否需要增删改查
				var editbutton = me.attr("editButton")!=0;
				var delbutton = me.attr("delButton")!=0;
				var viewbutton = me.attr("viewButton")!=0;
				var toolbar = true||me.attr("toolbar")==0;
				var toolbarposition = "top"||me.attr("toolbarPosition");
				var colNames = []; 
				var colModels = [];
				var hideCols=[];
				var jxcdoc=me.attr("jxcdoc")==1;				//表格类型，用于进销存记录添加按钮
				var idx=multiselect ? 1 : 0; 
				var sequence=0;
				var editfunc=me.attr("editfunc")?function(rowid){eval(me.attr("editfunc"))(rowid,me,title)}:null;
				var addfunc=me.attr("addfunc")?function(){eval(me.attr("addfunc"))(me,title)}:null;
				var viewfunc=me.attr("viewfunc")?function(rowid){eval(me.attr("viewfunc"))(rowid,me,title)}:null;
			        $("thead th", me).each(function (i) { 
			            var th = $(this);
			            var editable=th.attr("editable")!="0";
			            var viewable=editable?th.attr("viewable")!="0":editable; //如果editable为false则viewable也为false.
			            var name=th.attr("name");
						var index=th.attr("index")||name;
						var align=th.attr("align")||"left";
						var width = parentWidth*Number(th.attr("thwidth"))|| parentWidth*(1-th_width)/(thlength-hiddenlength);
						// editrules: {edithidden:true, required:true(false), integer:true(false),number:true(false),minValue:val, maxValue:val}
						var sortable = th.attr("sort") == "1";
						var hidden = th.attr("hidden")=="hidden"||$.trim(th.text()) == ""||th.attr("gridview")==0||th.attr("Ahidden")==1;
						var title = th.attr("showtitle") != "0";
						var type = th.attr("type") || "string";
						var edittype = th.attr("edittype");
						var checkboxvalue=th.attr("checkboxvalue")||"1:0";
						var searchable=!th.attr("search")&&name!="id";				//没有search属性并且name不为id
						var formatter=th.attr("formatter");
						var datefmt=th.attr("datefmt")||"Y-m-d";
						var yearRange=th.attr("yearRange")||"c-10:c+5";
						var required = th.attr("require")=="1",
							edithidden=true&&(!th.attr("gridview")),		//
							searchhidden=th.attr("searchhidden")=="1",
							number=th.attr("number")==1,
							email=th.attr("email")==1;
						var fix=(required)?"<a style='color:red'>(*)</a>":"";
						var editrules={required:required,edithidden:edithidden,searchhidden:searchhidden,number:number,email:email};
						var dataUrl=th.attr("url");
						var jsonName=th.attr("jsonName");
						var lineFeed=th.prev().attr("lineFeed")==1||false;			//前一个单元格的lineFeed属性为1时另起一行
						var headerkey=th.attr("headerkey");
						var headervalue=th.attr("headervalue");
						var gridview=th.attr("gridview")!="0"&&th.attr("Ahidden")!=1;		//这两个属性用来判断是否显示在过滤列中
						var Ahidden=th.attr("Ahidden")==1;
						var searchhidden=!Ahidden;
						var fuzzySearch=th.attr("fuzzySearch")==1;
						
						if (edittype=="textarea"||lineFeed){
							var pos=sequence%coltotal;
							if (pos>0) {
								sequence+=coltotal-pos;
							}
						}
						var rowpos=Math.floor((sequence+coltotal)/coltotal);
						var formoption=editable?{elmsuffix : fix ,rowpos:rowpos,colpos:(sequence)%coltotal+1}:{};
						var fieldrules;
						var editoptions=null;
						var searchoptions;
						
						try{
							var rules=th.attr("fieldrules");
							fieldrules=typeof(eval(rules))=="object"?eval(rules):undefined;
							}catch(e){
						}
						try{
							editoptions=th.attr("editoptions");
							if(editoptions){
								editoptions=eval('('+editoptions+')');
							}
						}catch(e){
							editoptions=null;
						}
						try{
							searchoptions=th.attr("searchoptions");
							searchoptions=typeof(eval(searchoptions))=="object"?eval(searchoptions):{searchhidden:true};
						}catch(e){}
						var editfunc={};
						var searchfunc={};
				        if(type=="date"){
				        	editfunc={
				        		dataInit:initdate
				        	};
				        }
					
						editrules=$.extend(editrules,fieldrules);
						var colModel={ 
				                name: name, 
				                index: index,
				                fuzzySearch:fuzzySearch,
				                gridview:gridview,
				                Ahidden:Ahidden,
				                width: width,
			                    editrules:editrules,
				                sortable: sortable, 
				                hidden: hidden, 
				                title: title, 
				                type: type,
				                width:width,
				                editable:editable,
				                edittype:edittype,  //edittype不同于type,详见手册
				                search:searchable,
				                viewable: viewable,
				                formoptions :formoption
				            };
				        if (formatter) {
				        	if(funcs[formatter]){
				        		colModel["formatter"]=funcs[formatter];  //formatter不能随意加,如果加在不需要的字段上,会令表格线消失
				        	}else{
				        		colModel["formatter"]=formatter;
				        		if(formatter=="date"){
				        			var dateformate={srcformat:"Y-m-d",newformat:"Y-m-d"};
				        			colModel["formatoptions"]=dateformate;
				        		}
				        	}
						}
				        if (editable){
				        	switch (edittype){
					        	case undefined:					//文本输入框默认搜索条件
						        	searchfunc={sopt: ["eq", "cn","bw"]};
						        	if (dataUrl){				//有关联属性的输入框
						        		editfunc={
			        						maxlength:3,url:dataUrl,
			        						dataInit:function(elem){
			        							var e=jQuery.Event("keydown");
												e.keyCode=50;
			        							setTimeout(function(){
			        								if (th.attr("completeGrid")){
				        								var options=eval(th.attr("completeGrid")+"(th)");
				        								$(elem).completeGrid(options[0],options[1]);
				        								$(elem).change(function(){
															$(elem).trigger(e);
														});
				        							}
			        							});
			        						}
			        					};
						        		searchfunc={sopt: ["eq"]};
						        	}
						        	break;
					        	case "select":
					        		if(dataUrl){
										editfunc={dataUrl:dataUrl,buildSelect:function(data){
															var json=data;
															if(typeof(data)=="string"){
																json=$.parseJSON(data);
															}
															if(json&&jsonName){
																var items=$.myjqgrid.getAccessor(json,jsonName);
																var s=eachitem(items,headerkey,headervalue);
																return s;
															}
														}};
									}
									searchfunc={sopt:["eq"]};
									colModel["stype"]=edittype;
					        		break;
						        case "custom":
						        	editfunc={
										custom_element:function(value,options){
											var el = document.createElement("input");
											el.type="text";
											el.value=value;
											$(el).addClass("FormElement ui-widget-content ui-corner-all");
											if (dataUrl)	$(el).attr("url",dataUrl)
											return el;
										},
										custom_value:function(elem){
											return $(elem).val();
										}
									};
									searchfunc={sopt:["eq"],
											dataInit:function(elem){
												var $elem=$(elem);
												var e=jQuery.Event("keydown");
												e.keyCode=50;
												setTimeout(function(){
													relevanceInput($elem,dataUrl);
													$elem.change(function(){
														$elem.trigger(e);
													});
												},20);
											}};
									colModel["stype"]="text";
					        		break;
						        case "checkbox":
						        	searchfunc={ sopt: ["eq", "ne"], value: "1:是;0:否" };
						        	editfunc={value:checkboxvalue};
						        	$.extend(colModel,{formatter: "checkbox", align: "center",stype: "select"});
						        	break;
						        case "textarea":
						        	editfunc={rows:5,dataInit:function(el){
						        		$(el).removeAttr("cols");
						        		setTimeout(function(){
						        			if ($(el).parents().is("form")){
						        				var $td=$(el).parent("td");
						        				var colNum=$td.nextAll().length+1;
						        				$td.attr("colspan",colNum).nextAll().remove();
						        			}
						        		},50);
						        	}};
						        	searchfunc={sopt: ["eq", "cn","bw"]};
						        	break;
					        	default:
					        		break;
					        }
				        }
				        
						editoptions=$.extend(editfunc,editoptions);
						searchoptions=$.extend(searchoptions,editoptions,searchfunc);
				        if (editoptions) {
							colModel["editoptions"]=editoptions;
						}
				        if(searchoptions){
				        	colModel["searchoptions"]=searchoptions;
				        }
				        if("date"==type){
				        	colModel["datefmt"]=datefmt;
				        	colModel["editrules"]["date"]=true;
				        	if(sortable){
				        		colModel["sorttype"]=type;
				        	}
				        }
			            colModels.push(colModel); 
			            if (editable) { 
			            	if(edittype=="textarea"){ //如果是可编辑,并且是textarea则再起一行.
			            		sequence+=coltotal;
			            	}else{
			            		sequence++;//否则按顺序加一列
			            	}
			            }
			            colNames.push(th.text());					//获取列名称以便填充到过滤列的列表中
			            if(!gridview){
			            	hideCols.push(colModel["name"]);
			            }
				        //初始化日期格式及按钮的function
				    	function initdate(elem){
					    	setTimeout(function() {
					    		$(elem).datepicker({
					    			showOn: "button",
					    			autosize:true,
					    			//buttonImage: "ui-icon-calculator",
					    			dateFormat:"yy-mm-dd",
					    			changeMonth: false,
					    			changeYear: true,
					    			showButtonPanel:true,
					    			yearRange:yearRange,
					    			showWeek:true
					    		});
					    		//fm-button ui-state-default ui-corner-all fm-button-icon-left
					    		$(elem).parent().find("button").button({icons:{primary:"ui-icon-calculator"},text:false}).css("left","3px");
					    	},10); 
						}
			        }); 
			        //初始化默认配置
			        var beforeSubmit=function( postdata,formid){
			        		if(postdata["list_id"]=="_empty"){
			        			postdata["list_id"]="";
			        		}
			        		if(postdata["list1_id"]=="_empty"){
			        			postdata["list1_id"]="";
			        		}
			        		if(postdata["list2_id"]=="_empty"){
			        			postdata["list2_id"]="";
			        		}
			        		return [true,''];
			        	};
//			        //初始化默认按钮
//			        //全局配置
//			        var buttonconfig={
//			        		
//			        };
			        //审核按钮，进销存单据需要	
			        var auditingButton={
			        	name:"auditing",
			        	caption:"审核",
			        	title:"审核",
			        	buttonicon:"ui-icon-check",
			        	onClickButton:function(){
			        		var id = $("#list").jqGrid('getGridParam','selrow');
			        		$.ajax({
			        			type:"post",
			        			dataType:"json",
			        			data:{"id":id,"rigthId":1},
			        			url:"auditing_auditDel",
			        			error:function(){
			        				alert("生成单据编号错误!");
			        			},
			        			success:function(data){
			        				if (data[0].message != null) {
										alert(data[0].message);
									}else {
										$("#list").setCell(id,"state.stateName",data[0].state.stateName);
									}
			        				
			        			}
			        		});
			        	}
			        };
			        var initbuttons=[];
			        initbuttons=[{
			        				name:"ok",
			                        caption:"确 定",
			                     	title:"确定",
			                    	buttonicon:"ui-icon-plus"
			                    },{
			                    	name:"cancel",
			                    	caption:"取 消",
			                    	title:"取消",
			                    	buttonicon:"ui-icon-plus"
			                    },{
			                    	name:"print",
			                    	caption:"打 印",
			                    	title:"打印文件"
			                    }];
//			        //---初始化用户自定义按钮
			        var userbuttons = []; 
//			        // 循环tfoot中的所有button 
//			        $("tfoot button", me).each(function () { 
//			        	var fname = $(this).attr("function"); 
//			        	var myfunc=getfunction(funcs,fname);
//			            userbuttons.push({ 
//			                caption: $(this).text(), 
//			                onClickButton: myfunc
//			            }); 
//			        });
			      var windowWidth=$(window).width();
			      var windowHeight=$(window).height();
			      var formwidth=700;
			      var viewParameter={
						width:formwidth,viewPagerButtons:false,closeOnEscape:true,labelswidth:"",
						onClose:function(){
							$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
						},
						beforeShowForm:function(formid){
							$.each(colModels,function(){		//判断某行显示时隐藏
								if (this.Ahidden){
									var tdname=$.insertString(this.name);
									$("#trv_"+tdname).hide();
								}
							});
							pagination(formid,$(this));
							jqdialogPosition(formid);		//弹出框定位
						}
					};
					
			        //让传入参数可以覆盖初始值
				   var defaults={
				        	caption: title, 
				            url: url, 
				            mtype:'POST', //'GET' or 'POST'
				            datatype: datatype, 
				            height: height,
				            colNames: colNames, 
				            colModel: colModels,
				            forceFit:true,
				            onSelectRow:function () { eval(selerowfun + "()"); },
				            rowNum: pagesize, 
				            rowList:[5,10,20],  //可供选择的条数选项
				            pager: pagerId,  //分页工具栏
//				            multikey:'ctrlKey',//多选时按键
				            autoencode:true,//对URL进行编码,暂时没有起作用
				            altRows:true,//班马线
				            multiselect: multiselect, //是否可多选
				        	multiboxonly:true,//点checkbox才有效
				            viewrecords:true,
				            width:me.parents(".gridTableDiv").width()-2,
							sortable:true,  //是否可排序
							sortorder: "asc",
							shrinkToFit:true,
							toolbar:[toolbar,toolbarposition],	//定义工具栏?
							editurl:saveurl,	
				            jsonReader: { 
				            	root: "resultObj.rows", 
				            	page: "resultObj.pageNo",	// json中代表当前页码的数据
								total: "resultObj.totalPages",	// json中代表页码总数的数据
								records: "resultObj.totalRows", // json中代表数据行总数的数据
				            	repeatitems: false },
					   ondblClickRow:function(rowid,iRow,iCol,e){					//双击行产生的事件，参数rowid为行ID，iRow为行索引，iCol为列索引，e为事件对象
								if (!viewfunc)	$(this).viewGridRow(rowid,viewParameter);
								else eval(me.attr("viewfunc"))(rowid,me,title);
							},
						onSelectAll:function(selarrrow,statu){
							var toolbar = $("#t_"+this.id);
							if (selarrrow.length){				//有记录时才进行操作
								if (statu)	toolbar.find(".navtable .ui-state-disabled").removeClass("ui-state-disabled");
								else	toolbar.find("td[id^=edit], td[id^=view],td[id^=del]").addClass("ui-state-disabled");
							}
						},
						onSelectRow:function(rowid,status){							//选中行时编辑、查看、删除可用，否则不可用
							var row = $(this).find("#"+rowid);
							var toolbar = $("#t_"+this.id);
							if ($(".ui-state-highlight",me).length>0){
								toolbar.find(".navtable .ui-state-disabled").removeClass("ui-state-disabled");
							}else{
	//								row.removeClass("ui-state-highlight").attr("aria-selected",false);
	//								$(this).setSelection(rowid,true);
								toolbar.find("td[id^=edit], td[id^=view],td[id^=del]").addClass("ui-state-disabled");
							}
							var tree_title = $(".tree_title");
								dataChild = tree_title.attr("id");
							if (dataChild == "warehouse_showjson"&&this.id=="list1") {
								 $.getJSON('warehouseAllocation_showjson?id='+rowid,function(data){
									   var mygrid = $("table[id=list2]")[0];
									   mygrid.addJSONData(data);
								   });
							}
						},
						beforeRequest:function(){
							$(".cover").hide();
						},
						loadComplete:function(xhr){
							if (xhr&&xhr.resultObj)	var loadData=$.extend($.fn.bindTable.defaults.loadData,xhr.resultObj.rows);			//加载成功后将返回的数据缓存到一个变量中
							var currentPage = document.URL;									//获取页面名称
							datatype=me.jqGrid("getGridParam","datatype");
							var currentGrid = $(this),										//当前表格
								currentDIV = currentGrid.parents(".ui-jqgrid");
								currentCol = currentGrid.getGridParam("colModel");
							gridSize(currentGrid);
							var pageName = currentPage.substring(currentPage.lastIndexOf("/")+1,currentPage.indexOf("."))
														+currentGrid.attr("id");
							var colHidden = $.cookie(pageName+"hiddenAttr");		//根据页面名称和表格id来读取cookie
							var hiddenName = $.cookie(pageName+"hiddenName");
							newPostData = me.getGridParam("postData")
							if (colHidden){											//判断是否有cookie
								var colHidden = colHidden.split(",");
								var hiddenName = hiddenName.split(",");
								for (var i=0;i<colHidden.length;i++){
									if (colHidden[i]=="true"){						//根据获取到的cookie属性，来判断显示还是隐藏列
										currentGrid.hideCol(hiddenName[i]);
										gridSize(currentGrid);
									}else{
										currentGrid.showCol(hiddenName[i]);
										gridSize(currentGrid);
									}
								}
							}
							currentGrid.bind("hideShow",function(){
								var newModel = currentGrid.getGridParam("colModel");			//表格尺寸变化时获取当前表格的colModel参数
								var hiddenAttr = [];
								var gridColName = []; 
								for (var i=0;i<newModel.length;i++){
									hiddenAttr.push(newModel[i].hidden);						//将表格colModel的hidden属性值存到hiddenAttr数组中
									gridColName.push(newModel[i].name);							//将表格colModel的name属性值存到gridColName数组中
								}
								$.cookie(pageName+"hiddenAttr",hiddenAttr,{expires:10});		//将两个数组存到cookie中，cookie以页面名称和表格id作为名称
								$.cookie(pageName+"hiddenName",gridColName,{expires:10});
							});
							me.fuzzySearch();
							
							var $toolbar = $("#t_"+this.id);
							$("#pager_center").width(210);
							if (datatype!="local"){
								$toolbar.find("td[id]:not(td[id^=edit],td[id^=view],td[id^=del])").removeClass("ui-state-disabled");	//数据来源是local时，所有按钮禁用
								$toolbar.find("td[id^=edit],td[id^=view],td[id^=del]").addClass("ui-state-disabled");
							}
				       		else	$toolbar.find("td[id]").css("zoom","1").addClass("ui-state-disabled");
							var $thbutton=$("#thbutton"),
								$hidecol = $(".hidecol"),
								$hidecolName = $("#hidecolName");
							//鼠标移动标题行上时，显示隐藏列按钮
							currentDIV.find(".ui-jqgrid-htable th").mouseenter(function(){
								var th = $(this).find(".ui-jqgrid-sortable");
								if ($hidecol.is(":hidden")){								
									$thbutton.show().appendTo(th);
								}
							}).mouseleave(function(){
								if ($hidecol.is(":hidden")){
									$thbutton.hide();
								}
							});
							//进销存单加上审核按钮
							if (!$("#t_"+tableid).is(":has(.ui-pg-button .ui-icon-check)")&&jxcdoc) {
								me.navButtonAdd("#t_"+me.attr("id"),auditingButton);
							}
//							if ($(".tree_div table[id]")[0]){
//								if (me.getGridParam("postData").filters){
//									delete me.getGridParam("postData").filters;
//								}
//							}
						},
					   ajaxSelectOptions: {
						   type: "GET", // one need allows GET in the webmethod (UseHttpGet = true)     
						   contentType: 'application/json; charset=utf-8',    
						   dataType: "json",     
						   cache: false,     
						   data: {         
							   id: function () {  
								   var selid=me.jqGrid('getGridParam', 'selrow');
								   if(selid){
//									   console.debug(selid);
									   return selid;
								   }
								   return -1;
//								   return JSON.stringify($("#list").jqGrid('getGridParam', 'selrow'));
							   	}   
				            }
					    }
				   };
			        //让传入参数可以覆盖初始值
				   options=$.extend(defaults,options);
				   me.jqGrid(options);
				   if(hideCols&&hideCols.length>0){
					   me.jqGrid("hideCol",hideCols);
				   }

				   //每个有关联属性的输入框隐藏后追加一个输入框用来显示关联的名称
				   function relevanceInput(relevanceInput,url){
				   		var relevanceInputName = relevanceInput.attr("id")||"relevance.id",
				   			relevanceName =relevanceInputName.substring(0,relevanceInputName.indexOf(".")>0?relevanceInputName.indexOf("."):relevanceInputName.length), 
				   			showInputName = relevanceName+"Name";
						//给每个custom类型的输入框后面创建一个输入框，点击树节点时将id和text分别传给两个文本框
						var parentName = $("<input type='text' class='FormElement ui-widget-content ui-corner-all' id='"+ showInputName +"' name='"+ showInputName +"' />");
						var tree = $("<div class='selectTree ui-widget-content' id='"+ showInputName +"_tree'>" +
									"<div class='gridTableDiv'><table id='"+ showInputName +"_table'></table></div></div>");
						relevanceInput.hide().after(parentName);		//隐藏本来的输入框用来传id,增加一个输入框用来显示
						tree.bind("click",function(event){
							event.stopPropagation();
						});
						parentName.bind("click",function(event){
							var inputobj = $(this);
							var offset = inputobj.offset(),
								inputParent = inputobj.offsetParent(),
								right = $(window).width()-offset.left-inputobj.outerWidth(true),//弹出框定位在输入框的下方，与输入框右侧靠齐
								top = offset.top+inputobj.outerHeight(true)+2;
							treeWidth = inputobj.outerWidth(true)+inputobj.parents("td").prev().outerWidth(true);
							tree.width(treeWidth)
								.resizable({
										animate:true,animateDuration:200,animateEasing: 'swing',
										distance:10,
										ghost:true,
										minHeight:130,maxHeight:300,minWidth:200,maxWidth:400,
										stop:function(){
											setTimeout(function(){
												gridSize($("#"+showInputName +"_table"));
											},300);
										}
									})
								.bind("scrollstop",function(){
									var scrollTop=tree.scrollTop();
									$(".ui-resizable-handle",tree).each(function(){
										var thisTop = tree.height()-$(this).height()+scrollTop;
										$(this).css("top",thisTop+"px");
									});
								});
							if (!$(".ui-jqdialog").is(":has(#"+ showInputName +"_tree)")){			//没有关联菜单的，加入关联菜单并生成treegrid															
								inputParent.append(tree);
								var treeGrid = $("#"+showInputName+"_table").jqGrid({
							       treeGrid: true, 
					               treeGridModel: 'nested', 
					               treeIcons: {plus:'ui-icon-folder-collapsed',minus:'ui-icon-folder-open',leaf:'ui-icon-document'},
					               ExpandColumn: 'name', 
//					               ExpandColClick: true, 
					               url: relevanceInput.attr("url")||url,						//从ID输入框的url属性调取数据生成Grid
					               mtype:'POST', //'GET' or 'POST'
					               datastr:$.fn.bindTable.defaults.treeData,
					               datatype: $.fn.bindTable.defaults.treeData.resultObj==null?"json":"jsonstring", 
					               colNames: ["item","name","py"], 
					               colModel: [
					               		{name:"item",hidden:true},
					               		{name:"name",index:"name",width:"100%"},
					               		{name:"py",hidden:true}
					               	], 
					               pager: "false", 
					               height: 'auto', 
					               rowNum:10000,
					               autowidth: true, 
					               // caption: 'none', 
					               jsonReader: { 
					                   root: "resultObj.rows", 
					                   total: "resultObj.totalPages", 
					                   repeatitems: false 
					               },
								   ondblClickRow:function(rid,iRow,iCol,e){
									   var rc = new Rc($(this).find("#"+rid));//选中行后将行显示的名称显示在编辑框中，编号赋给隐藏域
									   inputobj.val(rc.name);
									   inputobj.prev().val(rc._id_).trigger("change");
									   $(".selectTree").hide();
								   },
								   loadComplete:function(){
								   		var $grid=$(this);
//								   		if ($.fn.bindTable.defaults.treeData.resultObj==null){
//								   			$grid.jqGrid("setGridParam",{url:relevanceInput.attr("url"),mtype:"POST",datatype:"json"}).trigger("reloadGrid");
//								   		}
								   		$grid.parents(".selectTree").find(".ui-jqgrid-hdiv").hide();
									}
								});
							}else{
								$(this).trigger("keyup");
								$("#"+showInputName+"_table").jqGrid("setGridParam",{datastr:$.fn.bindTable.defaults.treeData}).trigger("reloadGrid");
							}
							$("#"+ showInputName +"_tree").show(200,function(){
								$("#"+showInputName+"_table").setGridWidth(tree[0].clientWidth-2);
								$(document).click(function(){
									$(".selectTree").hide(200);
								});
							}).css({"right":right,"top":top});
						event.stopPropagation();
					})
					//有关联的输入框按键事件
					parentName.keyup(function(event){
						$(this).prev().val("");
						var keycode=event.which;
						var treeGrid=$("#"+showInputName+"_table");
						var selecttr=$("#"+treeGrid.getGridParam("selrow"),treeGrid),		//获取选中行
							rc=new Rc(selecttr),
							$tr=treeGrid.find("tr[id]:visible"),
							open=treeGrid.is(":visible"),
							nexttr=selecttr.nextAll("[id]:visible:first"),
							prevtr=selecttr.prevAll("[id]:visible:first");
						function scrollControl(){
							var selrow = $("#"+treeGrid.getGridParam("selrow"));
							if (selrow.position().top<tree.scrollTop()){
								tree.scrollTop(selrow.position().top);			////在显示区域上方时，滚动到选择行的顶部
							}else if(selrow.height()+selrow.position().top>tree.scrollTop()+tree[0].clientHeight){
								if (keycode==38)	tree.scrollTop(selrow.position().top);				//在显示区域下方时，向下滚动
								if (keycode==40)    tree.scrollTop(tree.scrollTop()+selrow.height());
							}
						}
						switch (keycode){
							case 40:				//下 箭头时选中下一行，当前选中最后一行或没有选中行时先选中最后一行再滚动到第一行
								if (open){
							        var id=nexttr[0]?nexttr.attr("id"):$tr.first().attr("id");
							        treeGrid.resetSelection().setSelection(id);
							        scrollControl();
							    } else {
							        $(event.target).trigger("click");
							    }
							    event.preventDefault();
								break;
							case 38:				//上箭头时选中上一行，当前选中第一行时，滚动到最后一行
								if (open){
									var id=prevtr[0]?prevtr.attr("id"):$tr.last().attr("id");
									treeGrid.resetSelection().setSelection(id);
							        scrollControl();
							    } else {
							        $(event.target).trigger("click");
							    }
							    event.preventDefault();
								break;
							case 13:			//回车键模拟点击选中行
								if (treeGrid.getGridParam("selrow")){
									var rc = new Rc($("#"+treeGrid.getGridParam("selrow"),treeGrid));//选中行后将行显示的名称显示在编辑框中，编号赋给隐藏域
									$(this).val(rc.name);
									$(this).prev().val(rc._id_);
								}
								$(".selectTree").hide();
								break;
							case 37:	//左箭头如果节点不是树叶节点，并且是展开的，就折叠节点，如果未展开或是树叶节点就折叠父节点
								if (rc.expanded&&rc.isLeaf=="false"){
									selecttr.find(".treeclick").trigger("click");									
								}else if(!rc.expanded&&treeGrid.getNodeParent(rc)){
									var parentid = treeGrid.getNodeParent(rc)._id_;
									$("#"+parentid).find(".treeclick").trigger("click");
									treeGrid.setSelection(parentid);
								}
								break;
							case 39:	//如果不是树叶节点就展开
								if (!rc.expanded&&rc.isLeaf=="false"){
									selecttr.find(".treeclick").trigger("click");									
								}
								break;
							case 27:
								$(".selectTree").hide();
								break;
							default:
								searchTreegrid($(this),$("#"+showInputName+"_table"));	//不是上下箭头、回车时时搜索tree
								break;
						}
					}).blur(function(){
						var id=$(this).prev().val();
						var val=$(this).val();
						var $treeGrid=$("#"+showInputName+"_table");
						var nameArray=[];
						if (!id){
							$("tr[id]",$treeGrid).each(function(i,obj){
								nameArray.push($("span:first",obj).text());
								if ($("span:first",obj).text()==val){		//用于判断手动输入的值是否在列出的表格中（包括隐藏的行）
									parentName.prev().val(obj.id);
									return false;
								}
							});
						}
						if (val&&$.inArray(val,nameArray)<0&&nameArray.length)	$(this).val("").prev().val("");	//判断输入的值不存在清空值
					});
				 }
				 				
				//用于判断页面是否存在树并且在增删改后是否需要重新加载
				function judgeTree($grid){
					var $tree_div=$grid.parents(".treedata_div").prev();
					if ($tree_div.length>0){
						var $treeGrid=$(".tree_div table[id]"),
							meUrlName=url.substring(0,url.indexOf("_")),
							treeGridUrl=$treeGrid.getGridParam("url"),
							treeGridUrlName=treeGridUrl.substring(0,treeGridUrl.indexOf("_"));
						return [meUrlName==treeGridUrlName,$treeGrid];			//返回两个表格是否关联和树表格
					}
				}
			    me.navGrid("#"+pagerId, {add:addbutton,addtext:"新增",edittext:"编辑",deltext:"删除",searchtext:"搜索",refreshtext:"刷新",view:true,viewtext:"查看",
										beforeRefresh:function(){
											if($(".tree_div")[0]){		//刷新时左边有树，根据左边选择的行来重新加载主表
												$treeGrid=$(".tree_div .ui-jqgrid-btable")
												var id=$treeGrid.getGridParam("selrow");
												if (id){
													var rc=new Rc($("#"+id,me));
											        var level=Number(rc.level)?Number(rc.level)+1:1;
												    me.jqGrid('setGridParam',{page:1,datatype:"json",
															postData:{
																'filters':'{' +
																	'"groupOp":"AND",' +
																	'"rules":[{"field":"lft","op":"gt","data":"'+rc.lft+'"},' +
																			'{"field":"rgt","op":"lt","data":"'+rc.rgt+'"},' +
																			'{"field":"depth","op":"eq","data":"'+level+'"}]' +
																'}'
															}
														}).trigger("reloadGrid");
											    }
												return false;
											}
										},editfunc:editfunc,viewfunc:viewfunc,addfunc:addfunc},//options
			        		{
			        			width:formwidth,jqModal:false,modal:true,closeAfterAdd:true,checkOnSubmit:true,closeAfterEdit: true,viewPagerButtons:true,
			        			afterShowForm:function(formid){
									$(".customelement").each(function(){
										if (!$(this).next().is("input"))   relevanceInput($(this));
									});
									var rowData;
									$.each($.fn.bindTable.defaults.loadData,function(i,n){		//从缓存中读取该行数据
										if (n.id==me.getGridParam("selrow")){
											rowData=n;
											return false;
										}
									});
									setTimeout(function(){
										$.each(formid[0],function(i,n){
											var name=n.id,
												period=name.indexOf(".id");			//句点的位置
										    if (period>=0){							//如果找到句点，给句点前加一个转义符，从缓存的数据中读取对应的数据
										    	var releAttr=name.substring(0,period);
										    	var selector=releAttr+"\\"+name.substring(period);
										    	if (rowData[releAttr]){
										    		$("#"+selector).val(eval("rowData."+name))
										    				.next().val(eval("rowData."+releAttr+".shortName?rowData."+releAttr+".shortName:rowData."+releAttr+".name"));
										    	}else{
										    		$("#"+selector).next().val("");
										    	}
										    }
										});
										if ($(".tree_div")[0]){			//有树时给父级输入框一个默认值
											var $treeGrid= $(".tree_div table[id]");
											var id = $treeGrid.jqGrid("getGridParam","selrow"),
												rc = new Rc($("#"+id,$treeGrid));
											$("input[id*=\\.id],#pid").val(rc._id_).next().val(rc.name).attr("disabled",false);
										}
									},100);
//									setTimeout(function(){
//										$("select",formid).dropkick();
//									},200);
									$.each(colModels,function(){			//判断某行编辑时显示，新增时隐藏
										if (this.Ahidden){
											var tdname=$.insertString(this.name);
											$("#tr_"+tdname).show();
										}
									});
									pagination(formid,$(this));
									setTimeout(function(){inputMove(formid);},20);
									jqdialogPosition(formid);		//弹出框定位
								},
								onClose:function(){
									$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
								},
								afterSubmit:function(r,node){
									var eqTreeName=judgeTree(me);			//用于判断左侧表格是否需要刷新，返回true或false和左侧的树
									if (eqTreeName&&eqTreeName[0])	eqTreeName[1].resetSelection().trigger("reloadGrid");			//返回值为true时刷新
								},
								afterComplete:function(statu,node,form){
									if ($(".tree_div table[id]")[0]){			//判断是否有树
										var eqTreeName=judgeTree(me);
										if (eqTreeName&&eqTreeName[0]){			//如果提交数据后更新了左边的树，则展开新添加节点及父级点
											eval("var responseObj="+statu.responseText);
											var treeRow = eqTreeName[1].find("#"+responseObj.resultObj.id);
											var rc = new Rc(treeRow);
											nodeAncestors(eqTreeName[1],rc);
											var parentID = eqTreeName[1].getNodeParent(rc)._id_;		//将父级变量作为参数用于展开
											eqTreeName[1].setSelection(parentID,true);
										}else{						//不更新左边的树，则展开新增记录对应的节点
											var idfield;
											$.each(node,function(i,n){
												if (i.indexOf(".id")>=0){
													idfield=n;
													return false;
												}
											});
											if (idfield)	var rc = new Rc($("#"+idfield,eqTreeName[1]));			//获取新增节点
											else	var rc = new Rc($("#"+eqTreeName[1].jqGrid("getGridParam","selrow"),eqTreeName[1]));
											nodeAncestors(eqTreeName[1],rc);
//											var parentID = eqTreeName[1].getNodeParent(rc)._id_;		//将父级变量作为参数用于展开
											if (eqTreeName[1].getGridParam("selrow")!=rc._id_)
												eqTreeName[1].jqGrid("resetSelection").setSelection(rc._id_,true);	//选中新增节点
											else{
												eqTreeName[1].jqGrid("resetSelection").setSelection(eqTreeName[1].getNodeParent(new Rc($("#"+rc._id_,eqTreeName[1])))._id_,false);
												eqTreeName[1].jqGrid("resetSelection").setSelection(rc._id_,true);;	//选中新增节点
											}
										}
									}
								}
							}, //edit option ,navkeys:[true,38,40]//导航记录,savekey:[true,13]//保存记录
							{
								width:formwidth,jqModal:false,modal:true,cloaseAfterAdd:true,beforeSubmit:beforeSubmit,closeAfterEdit:true,
								afterShowForm:function(formid){
									formid.find(":input").val("");
									$(".customelement").each(function(){
										if (!$(this).next().is("input"))	relevanceInput($(this));
									});
									if ($(".tree_div")[0]){			//有树时给父级输入框一个默认值
										var $treeGrid= $(".tree_div table[id]"),
											id = $treeGrid.jqGrid("getGridParam","selrow"),
											rc = new Rc($("#"+id,$treeGrid));
										$("input[id*=\\.id],#pid").val(rc._id_).next().val(rc.name).attr("disabled",true);
									}
									$.each(colModels,function(){		//判断某行编辑时显示，新增时隐藏
										if (this.Ahidden){
											var tdname=$.insertString(this.name);
											$("#tr_"+tdname).hide();
										}
									});
									pagination(formid,$(this));
									setTimeout(function(){inputMove(formid);},20);
									jqdialogPosition(formid);		//弹出框定位
								},
								onClose:function(){
									$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
								},
								afterSubmit:function(r,node){
									var eqTreeName=judgeTree(me);			//用于判断左侧表格是否需要刷新，返回true或false和左侧的树
									if (eqTreeName&&eqTreeName[0])	eqTreeName[1].resetSelection().trigger("reloadGrid");			//返回值为true时刷新
									return [true,"不刷新"];
								},
								afterComplete:function(statu,node,form){
									if ($(".tree_div table[id]")[0]&&tableid!="list2"){			//判断是否有树
										var eqTreeName=judgeTree(me);
										if (eqTreeName&&eqTreeName[0]){			//如果提交数据后更新了左边的树，则展开新添加节点及父级点
											eval("var responseObj="+statu.responseText);
											var treeRow = eqTreeName[1].find("#"+responseObj.resultObj.id);
											var rc = new Rc(treeRow);
											nodeAncestors(eqTreeName[1],rc);
											var parentID = eqTreeName[1].getNodeParent(rc)._id_;		//将父级变量作为参数用于展开
											eqTreeName[1].setSelection(parentID,true);
										}else{						//不更新左边的树，则展开新增记录对应的节点
											var idfield;
											$.each(node,function(i,n){
												if (i.indexOf(".id")>=0){
													idfield=n;
													return false;
												}
											});
											if (idfield)	var rc = new Rc($("#"+idfield,eqTreeName[1]));			//获取新增节点
											else	var rc = new Rc($("#"+eqTreeName[1].jqGrid("getGridParam","selrow"),eqTreeName[1]));
											nodeAncestors(eqTreeName[1],rc);
											if(eqTreeName[1].getNodeParent(rc))		$("#"+rc._id_,eqTreeName[1]).trigger("dblclick");
										}
									}
								}
							}, //add option 
							{
								url:deleteurl,
								afterShowForm:function(formid){
									jqdialogPosition(formid);		//弹出框定位
								},
								beforeSubmit:function(){
									me.jqGrid("setGridParam",{page:1});
									return [true,"删除成功"];
								},
								afterSubmit:function(){
									var eqTreeName=judgeTree(me);			//用于判断左侧表格是否需要刷新，返回true或false和左侧的树
									if (eqTreeName&&eqTreeName[0])	eqTreeName[1].trigger("reloadGrid");			//返回值为true时刷新
									return [true,"成功删除"];
								},
								afterComplete:function(){
									var eqTreeName=judgeTree(me);
									if (eqTreeName&&eqTreeName[0]){			//左侧树刷新后展开原先选中行的祖先节点，并选中该行
										id=eqTreeName[1].jqGrid("getGridParam","selrow");
										var rc=new Rc($("#"+id,eqTreeName[1]));
										nodeAncestors(eqTreeName[1],rc);
										eqTreeName[1].jqGrid("resetSelection").setSelection(id,false);
									}
									$(toolbar).find("td[id^=edit], td[id^=view],td[id^=del]").addClass("ui-state-disabled");
								},
								onClose:function(){
									$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
								}
							},//deloption closeOnEscape:true,
							{
								multipleSearch:true, multipleGroup:true, showQuery: true,closeOnEscape:true,recreateFilter:true,
								sopt:["eq","ne","cn","nc","lt","le","gt","ge","bw","bn"],
								afterRedraw:function(p){
								},
								afterShowSearch:function(formid){
									jqdialogPosition(formid);		//弹出框定位
									me.filterGrid();
								},
								onClose:function(){
									$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
								},
							},//search option
							{
								width:formwidth,viewPagerButtons:false,closeOnEscape:true,labelswidth:"",
								onClose:function(){
									$(".selectTree,.completeDiv,.ui-widget-overlay").hide();
								},
								beforeShowForm:function(formid){
									$.each(colModels,function(){		//判断某行显示时隐藏
										if (this.Ahidden){
											var tdname=$.insertString(this.name);
											$("#trv_"+tdname).hide();
										}
									});
									pagination(formid,$(this));
									jqdialogPosition(formid);		//弹出框定位
								}
							}//view option
						);
//			        me.jqGrid('filterToolbar',{stringResult: true,searchOnEnter : true});
//			        $("#search").filterGrid("#"+pagerId,{gridModel: true, 
//			       	gridNames:false, gridToolbar: true});//为表格的所有Search为true 的字段增加搜索框并放置在指定的div上
//			        $("#detail").hide();
//			        $("#edit_form").hide();
//			        tableToGrid("#edit_table",{
//			    		autowidth: true,
//			    		height:"auto",
//			    		caption:"新建工程"});

			    	toolbar = "#t_"+tableid;
					//将自定义按钮移动到toolbar，初始化禁用编辑、查看、删除
			        var thisgrid = me.parents(".ui-jqgrid");
			        thisgrid.find(".ui-jqgrid-pager .navtable").prependTo(toolbar);
			        $("#"+pagerId+"_left").remove();
			        if (datatype!="local")	$(toolbar).find("td[id*=edit],td[id*=view], td[id*=del]").css("zoom","1").addClass("ui-state-disabled");
			        else	$(toolbar).find("td[id]").css("zoom","1").addClass("ui-state-disabled");
							
			        if (userbuttons&&userbuttons.length > 0) { 
			            $.each(userbuttons, function (i) { 
			                me.navButtonAdd("#" + pagerId, userbuttons[i]); //将按钮加到工具栏tfoot
			            }); 
			        }
			        if (initbuttons&&initbuttons.length>0) {
						$.each(initbuttons,function(i){
//							me.navButtonAdd("#t_list",initbuttons[i]);
//							var bb=$("#t_"+me.attr("id")).append("<button>"+this.caption+"</button>");
//							me.navButtonAdd("#" + pagerId, userbuttons[i]); //将按钮加到工具栏tfoot
//							me.navButtonAdd("#"+pagerId,initbuttons[i]);
						});
					}
					

				   return me;
			  });
	   },//第1个插件结束
	   
	   "jxctable":function(){
	   	   var $thwidth = $("thead th[thwidth]",$(this));
   		   var thlength = $("thead th",$(this)).length,
   		       widthlength = $thwidth.length,
   		       hiddenlength = $("thead th[hidden]",$(this)).length,
   		       parentWidth = $(this).width(),
   		       th_width=0;
   		   for (var i=0;i<widthlength;i++){
   		       th_width +=  Number($thwidth.eq(i).attr("thwidth"));
   		   }
		   var url=undefined;
		   var options=undefined;
		   var colname=[];
		   var colnameAttr=[];
		   var funcs={};
		   for ( var i = 0; i < arguments.length; i++) {
			   var tmp=arguments[i];
				if (typeof tmp=="string") {
					url=tmp;
					continue;
				}
				if (typeof tmp=="object") {//传入可能是jqgrid的设置,也可能是按钮的处理函数
					$.each(tmp,function(i,n){
						if (typeof n=="function") { //如果第1个就是function,那么就是处理函数
							funcs=tmp;
							return false;
						}else{
							options=tmp; //如果第1个不是function,那么就是设置.
							return false;
						}
					});
					continue;
				}
				
			}
		   //迭代取回的json数据并将其生成select标签
	        function eachitem(jsonitems,headerkey,headervalue){
	        	var select="<select>";
	        	if(headerkey){
	        		select+="<option value="+headerkey+">"+headervalue+"</option>";
	        	}
	        	if(jsonitems&&jsonitems.length>0){
	        		for(var i=0;i<jsonitems.length;i++){
	        			select+="<option value="+jsonitems[i].id+">"+jsonitems[i].name+"</option>";
	        		}
	        	}
	        	select+="</select>";
	        	return select;
	        }
	        //从一个装function对象取出属性为funname的function
	        function getfunction(funcs,funname){
	        	var func = funcs[funname]; 
	        	if (func == undefined) func = function () { eval(funname + "()"); }; 
	        	return func;
	        }
	
		  return this.each(function(){   //可能匹配到多个元素,故用each();避免只作用一个
			  //取页面参数开始,用于初始化jqGrid
			  var me = $(this); //each里面的this是DOM对象,需要转换成jquery对象
			  var tableurl=me.attr("url");
			if (!url&&!tableurl) {
				  $("<div>未设置列表URL:<br/>1.可在方法参数传入.<br/>2.或者表格属性中加入'url'属性</div>").dialog();
			  }else if(!url){ //命令行的地址优先于table的url属性
				  url=tableurl;
			  }
		        var pagesize=me.attr("pagesize")||5; 
		        var datatype="json"; 
		        var height=me.attr("height")||"auto";
		        var multiselect = me.attr("multiselect") == "1"; 
		        var title=me.attr("title") || document.title; 
		        var dataroot=me.attr("dataroot") || "resultObj.rows"; 
		        var coltotal=parseInt(me.attr("colnum"))||3; //编辑时的列数
		        var saveurl=me.attr("saveurl");
		        var deleteurl=me.attr("deleteurl")||saveurl;
		        var selerowfun=me.attr("selerowfun");//||function(){};
		        var cellEdit=me.attr("cellEdit")==1;
		        var rows=me.attr("rows")||5;
		        var colNames=[]; 
		        var colModels=[];
		        var hideCols=[];
		        var idx=multiselect ? 1 : 0; 
		        var sequence=0;
		        $("thead th", me).each(function (i) { 
		            var th = $(this);
		            var editable=th.attr("editable")!="0";
		            var viewable=th.attr("viewable")!="0"; //如果editable为false则viewable也为false.
		            var name=th.attr("name");
					var index = th.attr("index")||name;
					var width = parentWidth*Number(th.attr("thwidth"))|| parentWidth*(1-th_width)/(thlength-hiddenlength);
//					editrules: {edithidden:true, required:true(false), integer:true(false),number:true(false),minValue:val, maxValue:val}
					var sortable = th.attr("sort") == "1";
					var hidden = th.attr("hidden")=="hidden"||$.trim(th.text()) == "";
					var title = th.attr("showtitle") != "0";
					var type = th.attr("type") || "string";
					var edittype = th.attr("edittype");
					var checkboxvalue=th.attr("checkboxvalue")||"1:0";
					var searchable=th.attr("search")!="0";
					var formatter=th.attr("formatter");
					var datefmt=th.attr("datefmt")||"Y-m-d";
					var yearRange=th.attr("yearRange")||"c-10:c+5";
					var required = th.attr("require")=="1",
						edithidden=true,
						number=th.attr("number")=="1",
						searchhidden=th.attr("searchhidden")=="1";
					var editrules={required:required,edithidden:edithidden,number:number,searchhidden:searchhidden};
					var dataUrl=th.attr("url");
					var align=number?"right":th.attr("align")||"left";
					var jsonName=th.attr("jsonName");
					var headerkey=th.attr("headerkey");
					var headervalue=th.attr("headervalue");
					var gridview=th.attr("gridview")!="0"&&th.attr("Ahidden")!=1;		//这两个属性用来判断是否显示在过滤列中
					var rowpos=Math.floor((sequence+coltotal)/coltotal);
					var formoption=editable?{rowpos:rowpos,colpos:(sequence)%coltotal+1}:{};
					var fieldrules;
					var editoptions=null;
					var searchoptions;
					try{
						var rules=th.attr("fieldrules");
						fieldrules=typeof(eval(rules))=="object"?eval(rules):undefined;
						}catch(e){
					}
						try{
							editoptions=th.attr("editoptions");
							if(editoptions){
								editoptions=eval('('+editoptions+')');
							}
						}catch(e){
							editoptions=null;
						}
						try{
							searchoptions=th.attr("searchoptions");
							searchoptions=typeof(eval(searchoptions))=="object"?eval(searchoptions):undefined;
						}catch(e){}
					var editfunc={};
					var searchfunc={};
			        				
					editrules=$.extend(editrules,fieldrules);
					var colModel={ 
			                name: name, 
			                index: index,
					        width: width,
					        align:align,
		                    editrules:editrules,
		                    gridview:gridview,
			                sortable: sortable, 
			                hidden: hidden, 
			                title: title, 
			                type: type,
			                editable:editable,
			                edittype:edittype,  //edittype不同于type,详见手册
			                search:searchable,
			                viewable: viewable,
			                formoptions :formoption
			            };
			        if (formatter) {
			        	if(funcs[formatter]){
			        		colModel["formatter"]=funcs[formatter];  //formatter不能随意加,如果加在不需要的字段上,会令表格线消失
			        	}else{
			        		colModel["formatter"]=formatter;
			        		if(formatter=="date"){
			        			var dateformate={srcformat:"Y-m-d",newformat:"Y-m-d"};
			        			colModel["formatoptions"]=dateformate;
			        		}
			        	}
					}
					
					if (edittype==undefined){
						if (dataUrl){				//有关联属性的输入框
			        		var editfunc={
	        						url:dataUrl,
	        						dataInit:function(elem){
	        							var e=jQuery.Event("keydown");
										e.keyCode=50;
										var timeoutSet;
	        							setTimeout(function(){
	        								if (th.attr("completeGrid")){
		        								var options=eval(th.attr("completeGrid")+"(th)");
		        								$(elem).completeGrid(options);
		        								$(elem).change(function(){
													$(elem).trigger(e);
												}).focus(function(){
													$(elem).trigger("click");
												});
		        							}
	        							});
	        						}
	        					};
			        	}
					}
					
					editoptions=$.extend(editfunc,editoptions);
					searchoptions=$.extend(searchfunc,editoptions);
			        if (editoptions) {
						colModel["editoptions"]=editoptions;
					}
			        if("date"==type){
			        	colModel["datefmt"]=datefmt;
			        	colModel["editrules"]["date"]=true;
			        	if(sortable){
			        		colModel["sorttype"]=type;
			        	}
			        }else if("checkbox"==type){
			        	colModel["editoptions"]={value:checkboxvalue};
			        	if(!edittype){
			        		colModel["edittype"]="checkbox";
			        	}
			        	if(!formatter){ //如果没有定义自己的formatter则使用默认的
			        		colModel["formatter"]="checkbox";
	//				        		colModel["formatoptions"]={disabled:false}; //是否在表格中可用checkbox
			        	}
			        }
		            colModels.push(colModel); 
		            colNames.push(th.text());					//获取列名称以便填充到过滤列的列表中
		            if(!gridview){
		            	hideCols.push(colModel["name"]);
		            };
			        //自定义的function
			        //初始化日期格式及按钮的function
			    	function initdate(elem){
				    	setTimeout(function() {
				    		$(elem).datepicker({
				    			showOn: "button",
				    			autosize:true,
				    			//buttonImage: "ui-icon-calculator",
				    			dateFormat:"yy-mm-dd",
				    			changeMonth: true,
				    			changeYear: true,
				    			showButtonPanel:true,
				    			yearRange:yearRange,
				    			showWeek:true});
				    		//fm-button ui-state-default ui-corner-all fm-button-icon-left
				    		$(elem).parent().find("button").button({icons:{primary:"ui-icon-calculator"},text:false}).css("left","3px");
				    		},10); 
				}
		        }); 
		        //初始化默认配置
		        var beforeSubmit=function( postdata,formid){
		        		if(postdata["list_id"]=="_empty"){
		        			postdata["list_id"]="";
		        		}
		        		if(postdata["list1_id"]=="_empty"){
		        			postdata["list1_id"]="";
		        		}
		        		if(postdata["list2_id"]=="_empty"){
		        			postdata["list2_id"]="";
		        		}
		        		return [true,''];
		        	};
		      var lastSel;
		        //让传入参数可以覆盖初始值
			   options=$.extend({
		            url: url, 
		            mtype:'GET', //'GET' or 'POST'
		            datatype: datatype, 
		            colNames: colNames, 
		            colModel: colModels,
		            footerrow:true,
//		            onSelectRow:function () { eval(selerowfun + "()"); },
		            rowNum: pagesize, 
		            rowList:[5,10,20],  //可供选择的条数选项
//		            multikey:'ctrlKey',//多选时按键
//		            autoencode:true,//对URL进行编码,暂时没有起作用
//		            altRows:true,//班马线
	         		height: 140, 
		            width:me.parents(".gridTableDiv").width()-2,
		            rownumbers:true,
		            rownumWidth:35,
//					sortable:true,  //是否可排序
					sortorder: "asc",
					shrinkToFit:true,
					forceFit:true,
					editurl:saveurl,
					cellsubmit:"clientArray",
		            jsonReader: { 
		            	root: dataroot, 
		            	page: "resultObj.pageNo",	// json中代表当前页码的数据
						total: "resultObj.totalPages",	// json中代表页码总数的数据
						records: "resultObj.totalRows", // json中代表数据行总数的数据
		            	repeatitems: false },
//			        onCellSelect:function(rowid,iCol,cellcontent,event){
//			        	me.jqGrid("setSelection",rowid);
//			        },
		        onSelectRow:function(id){
		        	if (!$(this).parents(".ui-dialog").is("[id^=view]")){
		        		//行编辑时的参数
			        	var editRowSet={keys:false,
					        			oneditfunc:function(rowid){
					        				var $rowObj=me.find("#"+rowid);
					        				me.jqGrid("setSelection",rowid,false);
					        				inputMove($rowObj);
					        				//商品改变时自动填充资料
					        				$(":text[id$=ame]",$rowObj).change(function(){
					        					var $completeGrid=$(".completeDiv table[id]");
					        					var rowid=$completeGrid.getGridParam("selrow");
					        					gridData=$completeGrid.getRowData(rowid);
					        					$.each(gridData,function(i,n){
					        						if (i=="id"){
					        							$("td[aria-describedby*=productId]",$rowObj).text(n);
					        						}else{
					        							$("td[aria-describedby*=_"+i+"]",$rowObj).children().is(":text")?$("td[aria-describedby*=_"+i+"]",$rowObj).children().val(n):$("td[aria-describedby*=_"+i+"]",$rowObj).text(n)
					        						}
					        					});
					        				});
					    		        	var $priceInput = $("input[name=price]",$rowObj),
					    		        		$countInput = $("input[name=pquantity]",$rowObj),
					    		        		$sumInput = $("input[name=sum]",$rowObj).attr("readonly",true),
					    		        		$numberInput=$().add($priceInput).add($countInput).add($sumInput);
					    		        	$rowObj.find(":text:last").keydown(function(e){				//行内最后一个输入框按键事件
					    		        		if (e.which==13||e.which==39){							//回车和右箭头保存编辑行并跳转到下一行
					    		        			if ($rowObj.next()[0]){
					    		        				me.setSelection((Number(rowid)+1).toString());
					    		        			}else{												//最后一行最后一格时添加一个空行并选中
					    		        				me.addRowData(Number(rowid)+1,{});
					    		        				me.setSelection((Number(rowid)+1).toString());
					    		        			}
					    		        		}
					    		        	});
					    		        	$rowObj.find(":text:first").keydown(function(e){				//行内第一格时按左键头跳转到上一行
					    		        		if(e.which==37){
					    			        		me.setSelection((Number(rowid)-1).toString());
					    			        		$rowObj.prev().find("input:last").trigger("focus").trigger("select");
					    		        		}
					    		        	});
					    		        	$priceInput.change(function(){
					    		        		var value = $(this).val();
					    		        		$(this).val(Number(value).toFixed(2));
					    		        		if ($countInput.val())   calculation();
					    		        	});
					    		        	$countInput.change(function(){
					    		        		calculation();
					    		        	});
					    		        	if (!$countInput.val())	$countInput.val(0).trigger("change");
					    		        	$numberInput.keyup(function(){
					    		        		CheckInputIntFloat(this);
					    		        	});
					    		        	
					    		        	//计算合计，单价、数量改变时，保存行数据时要用到
					    		        	function calculation(){
					    		        		var count = Number($countInput.val()),
					    		        			price = Number($priceInput.val());
					    		        		if (count*price!=0&&!isNaN(count*price))   $sumInput.val((count*price).toFixed(2));
					    		        		var totalArray = me.getCol("pquantity",false);
					    		        		var totalAmountArray = me.getCol("sum",false);
					    		        		var total = Number(count),
					    		        			totalAmount = Number($sumInput.val());
					    		        		for (var i=0;i<totalArray.length;i++){
					    		        			if (isNaN(Number(totalArray[i]))==false){
					    		        				total+=Number(totalArray[i]);
					    		        				totalAmount+=Number(totalAmountArray[i]);
					    		        			}
					    		        		}
					    		        		me.footerData("set",{pquantity:total,sum:totalAmount.toFixed(2)});
					    		        		inputCalculate(totalAmount);
					    		        	}
					        			}//编辑时执行的操作
					        		};
			        	var $blankTr,gridData,saveData,currentData,editID;		//第一个空行、自动完成选中的行、上次编辑的行、当前选中行、需要编辑的行
			        	
			        	if (id!=lastSel&&lastSel){				//已有选中行时，保存已选中行
			        		me.jqGrid("saveRow",lastSel,{url:"clientArray"});
			        		saveData=getObjData(me,lastSel);
			        		currentData=getObjData(me,id);
			        		$.each(me.find("tr[id]"),function(i,n){					////找到第一个空行
								var rowVal=getObjData(me,n.id);
								if (rowVal[1]==""){
									$blankTr=$(n);
									return false;
								}
							});
			        		//必填项为空时弹出警告层
			        		if(saveData[1]!=""&&(saveData[0]["pquantity"]==""||saveData[0]["fullName"]==""||saveData[0]["sum"]=="")){
//			        			if (!$("body").is(":has(.alertDiv)")){
//			        				var alertDialog = $("<div class='alertDiv' style='display:none;' title='警告'>商品编号、数量、单价为必填项！</div>");
//			        				alertDialog.appendTo("body").dialog({autoOpen:false,modal:true,resizable:false,minHeight:false});
//			        			}
//			        			$(".alertDiv").click(function(e){e.stopPropagation();}).dialog("open");
			        			editID=lastSel;
			        		}else{						//保存完成后
			        			if (currentData[1]!=""){			//选中行不为空时编辑该行
			        				editID=id;
			        			}else{								//选中行为空时编辑第一个空行
			        				editID=$blankTr.attr("id");
			        			}
			        		}
		        		}else if(!lastSel){						//没有已选中行时
		        			$.each(me.find("tr[id]"),function(i,n){					////找到第一个空行
								var rowVal=getObjData(me,n.id);
								if (rowVal[1]==""){
									$blankTr=$(n);
									return false;
								}
							});
		        			currentData=getObjData(me,id);
		        			if(currentData[1]!=""){		//本次选中行有数据时编辑该行
		        				editID=id;
		        			}else{					//没有已选中行且本次选中行为空时，编辑第一个空白行
				        		editID=$blankTr.attr("id");
		        			}
		        		}else if (id==lastSel) {
							return false;
						}
			        	lastSel=editID;
			        	me.jqGrid("editRow",editID,editRowSet);
			        	
			        	$(document).unbind("click").click(function(e){				//在表格外点击保存正在编辑的行并取消选中
			        		if ($.inArray(e.target,me.find("*"))<0&&me.is(":visible")){
			        			if ($("#"+lastSel+" :text:first").val()!=""){
			        				me.jqGrid("saveRow",lastSel,{url:"clientArray"});
			        			}else {
									me.jqGrid("restoreRow",lastSel);
								}
			        			me.resetSelection();
			        			lastSel=undefined;
			        		}
			        	});
			        	
			        	me.parents(".definedForm").on("dialogbeforeclose",function(){
			        		if (lastSel){
			        			me.restoreRow(lastSel).resetSelection();
			        			lastSel=undefined;
			        		}
			        	});	
		        	}
		        },
				beforeRequest:function(){						
					var currentPage = document.URL;									//获取页面名称
					var currentGrid = $(this),										//当前表格
						currentDiv = currentGrid.parents(".ui-jqgrid");
						currentCol = currentGrid.getGridParam("colModel");
					var pageName = currentPage.substring(currentPage.lastIndexOf("/")+1,currentPage.lastIndexOf("."))
												+currentGrid.attr("id");
					var colHidden = $.cookie(pageName+"hiddenAttr");		//根据页面名称和表格id来读取cookie
					var hiddenName = $.cookie(pageName+"hiddenName");
					if (colHidden){											//判断是否有cookie
						var colHidden = colHidden.split(",");
						var hiddenName = hiddenName.split(",");
						for (var i=0;i<colHidden.length;i++){
							if (colHidden[i]=="true"){						//根据获取到的cookie属性，来判断显示还是隐藏列
								currentGrid.hideCol(hiddenName[i]);
								gridSize(currentGrid);
							}else{
								currentGrid.showCol(hiddenName[i]);
								gridSize(currentGrid);
							}
						}
					}
					currentGrid.bind("hideShow",function(){
						var newModel = currentGrid.getGridParam("colModel");			//表格尺寸变化时获取当前表格的colModel参数
						var hiddenAttr = [];
						var gridColName = []; 
						for (var i=0;i<newModel.length;i++){
							hiddenAttr.push(newModel[i].hidden);						//将表格colModel的hidden属性值存到hiddenAttr数组中
							gridColName.push(newModel[i].name);							//将表格colModel的name属性值存到gridColName数组中
						}
						$.cookie(pageName+"hiddenAttr",hiddenAttr,{expires:10});		//将两个数组存到cookie中，cookie以页面名称和表格id作为名称
						$.cookie(pageName+"hiddenName",gridColName,{expires:10});
					});
					
					var $thbutton=$("#thbutton"),
						$hidecol = $(".hidecol"),
						$hidecolName = $("#hidecolName");
					//鼠标移动标题行上时，显示隐藏列按钮
					currentDiv.find(".ui-jqgrid-htable th").mouseenter(function(){
						if ($hidecol.is(":hidden"))		$thbutton.show().appendTo($(this).find(".ui-jqgrid-sortable"));
					}).mouseleave(function(){
						if ($hidecol.is(":hidden"))		$thbutton.hide();
					});
//					var addColModel = currentGrid.getGridParam("colModel");
//							var addcolname =[];
//							for (var i=0;i<addColModel.length;i++){
//								addcolname.push(addColModel[i].name);
//							}
					var data=new Object();
					for (var i=0;i<rows;i++){
						currentGrid.addRowData(i+1,data);						//添加一个空行
					}
					gridSize(currentGrid);
					inputMove(me.parents("form"));
					$(".add_product, .del_product").remove();
					jqdialogPosition(currentDiv);		//弹出框重新定位
//					$(".add_product").click(function(){						//点击添加商品按钮
//						var rowNum = currentGrid.find("tr").length;
//						currentGrid.addRowData(rowNum,data);						//添加一个空行
//					});
//					$(".del_product").click(function(){
//						var selid=$("td.edit-cell").parent().attr("id");
//						var tdindex = $("td.edit-cell").index();
//						if (selid){
//							currentGrid.delRowData(selid);
//							currentGrid.children(":eq("+(selid-1)+")").children(":eq("+tdindex+")").addClass("edit-cell");;
//						}else{
//							alert("请选择要删除的记录！");
//						}
//					});
					//合计行
					currentGrid.footerData("set",{rn:"合计",pquantity:0,sum:0});
				}},options);
			   me.jqGrid(options).jqGrid("hideCol",hideCols);
			  return me;
		  });
	   },//进销存表格插件结束
	   
	   "treetable":function(arg){
		   var url=undefined;
		   var options=undefined;
		   var lastsel;
		   var funcs={};
		   var tableReload=null;
		   if (arg){
				tableReload=arg.tableReload;			//传入有参数时，定义主表的重载方式，如果没有，采用默认的方式
		   }
		   for ( var i = 0; i < arguments.length; i++) {
			   var tmp=arguments[i];
				if (typeof tmp=="string") {
					url=tmp;
					continue;
				}
				if (typeof tmp=="object") {//传入可能是jqgrid的设置,也可能是按钮的处理函数
					$.each(tmp,function(i,n){
						if (typeof n=="function") { //如果第1个就是function,那么就是处理函数
							funcs=tmp;
							return false;
						}else{
							options=tmp; //如果第1个不是function,那么就是设置.
							return false;
						}
					});
					continue;
				}
				
			}
	
			return this.each(function(){   //可能匹配到多个元素,故用each();避免只作用一个
				//取页面参数开始,用于初始化jqGrid
				var me = $(this); //each里面的this是DOM对象,需要转换成jquery对象
				var tableurl=me.attr("url");
				if (!url&&!tableurl) {
					$("<div>未设置列表URL:<br/>1.可在方法参数传入.<br/>2.或者表格属性中加入'url'属性</div>").dialog();
				}else if(!url){ //命令行的地址优先于table的url属性
					url=tableurl;
				}
		        var pagesize=me.attr("pagesize")||5; 
		        var datatype="json";
		        var pager=me.attr("id")+"Pager"||"treePager";
		        var height=me.attr("height")||me.parent().height()-2;
		        var multiselect=me.attr("multiselect") == "1"; 
		        var dataroot=me.attr("dataroot") || "resultObj.rows"; 
		        var moveActionName=url.substring(0,url.indexOf("_"));
		        var selerowfun=me.attr("selerowfun");//||function(){};
		        var rows=me.attr("rows")||10000;
//		        var nameArray=[];
//		        var datastr={};
//		        $.ajax({
//					type: "GET",
//					async: false,
//					contentType: "application/json;utf-8",
//					url: url,
//					success: function(data) {
//								datastr=data;
//					    		for (var p in eval("data."+dataroot)[0]){
//					    			if (p!="isLeaf")	nameArray.push(p);
//						 		}
//							}
//				});
//				var colNames=[];
//		        var colModels=[];
//				for (var i in nameArray){
//					var hidden=nameArray[i]!="name";
//		        	colNames.push(nameArray[i]);
//		        	colModels.push({name:nameArray[i],hidden:hidden,sortable:false});
//		        }
		        colNames=["item","name","py"];
		        colModels=[
		        	{name:"item",hidden:true},
		        	{name:"name",editable:false,sortable:false},
		        	{name:"py",hidden:true}
		        ];
		        var sequence=0;
		        //让传入参数可以覆盖初始值
			   options=$.extend({
		            url: url, 
		            mtype:'POST', //'GET' or 'POST'
		            datatype: datatype,
		            treeGrid: true,
					treeGridModel:"nested",
					ExpandColumn:"name",
					treeIcons: {plus:'ui-icon-folder-collapsed',minus:'ui-icon-folder-open',leaf:'ui-icon-document'},
//					ExpandColClick: true,
		            colNames: colNames, 
		            colModel: colModels,
		            onSelectRow:function () { eval(selerowfun + "()"); },
		            rowNum: pagesize, 
		            autoencode:true,//对URL进行编码,暂时没有起作用
	//			    altRows:true,//班马线
	         		height: height,
		            viewrecords:false,
		            width:me.parent().width()-2,
	//				sortable:true,  //是否可排序
					sortorder: "asc",
	//				shrinkToFit:true,
					pager:pager,
					jsonReader: {
						       repeatitems: false,
						       root: dataroot
						   },
					ondblClickRow:function(rowid){
						lastsel=undefined;
						me.setSelection(rowid,true);
					},
					onSelectRow:function(id){
			        	if (id&&id!=lastsel){			//不是上一次点击的行就重新加载主表的内容
			        		lastsel=id;
				     	   	var rc = new Rc($("#"+id,me));
				        	var rowdata = me.getRowData(id)
					        $(".selectedFiled td:odd:eq(0)").text(rowdata.item);
					        $(".selectedFiled td:odd:eq(1)").text(rowdata.name);
					        if (me.getNodeParent(rc)){
					        	var parentData=me.getRowData(me.getNodeParent(rc)._id_);
					        	$(".selectedFiled td:odd:eq(2)").text(parentData.name);
					        }else{$(".selectedFiled td:odd:eq(2)").text("　　　");}
					        var $table = $(".treedata_div .ui-jqgrid-btable:visible:first");
					        var rules;
					        if (!tableReload){									//默认的主表重载方式：选中行时将选中行的lft、rgt和level值作为搜索参数传给主表，并执行搜索
					        	var level=Number(rc.level)?Number(rc.level)+1:1;
					        	rules='{"groupOp":"AND",' +
											'"rules":[{"field":"lft","op":"gt","data":"'+rc.lft+'"},' +
													'{"field":"rgt","op":"lt","data":"'+rc.rgt+'"},' +
													'{"field":"depth","op":"eq","data":"'+level+'"}]}';
						        $table.jqGrid('setGridParam',{page:1,datatype:"json",postData:{'filters':rules}}).trigger("reloadGrid");
					        }else{
					        	rules=tableReload($table,id);					//使用传入的方式重载主表
					        }
					        $table.data("initFilter",rules);					//先缓存表格的原始过滤条件
			        	}
					},
					loadComplete:function(xhr){
						$.extend($.fn.completeGrid.defaults.treeData,xhr);			//加载成功后将返回的数据缓存到一个变量中
						gridSize(me);
						lastsel=undefined;
					}
				},options);
			  	me.jqGrid(options);
			  	me.jqGrid("navGrid","#"+pager,{edit:false,add:false,del:false,search:false,refresh:false})
			  		.jqGrid("navButtonAdd",pager,{caption:"",buttonicon:"ui-icon-arrowstop-1-n",title:"First",onClickButton:function(){
						var selrow=me.getGridParam("selrow");
						if(selrow){
							$.ajax({url:moveActionName+"_moveFirst.action",cache:false,data:"id="+selrow,success:function(){
								locateLast(me,selrow);
							}});
						}
					}
				});
				me.jqGrid("navButtonAdd",pager,{caption:"",title:"Prev",buttonicon:"ui-icon-arrow-1-n",onClickButton:function(){
						var selrow=me.getGridParam("selrow");
						if(selrow){
							$.ajax({url:moveActionName+"_movePre.action",cache:false,data:"id="+selrow,success:function(){
								locateLast(me,selrow);
							}});
						}
					}
				});
				me.jqGrid("navButtonAdd",pager,{
					caption:"",title:"Next",buttonicon:"ui-icon-arrow-1-s",onClickButton:function(){
						var selrow=me.getGridParam("selrow");
						if(selrow){
							$.ajax({url:moveActionName+"_moveNext.action",cache:false,data:"id="+selrow,success:function(){
								locateLast(me,selrow);
							}});
						}
					}
				});
				me.jqGrid("navButtonAdd",pager,{
					caption:"",title:"Last",buttonicon:"ui-icon-arrowstop-1-s",onClickButton:function(){
						var selrow=me.getGridParam("selrow");
						if(selrow){
							$.ajax({url:moveActionName+"_moveLast.action",cache:false,data:"id="+selrow,success:function(){
								locateLast(me,selrow);
							}});
						}
					}
				});
				
				function locateLast($grid,selrow){
					$grid.jqGrid("resetSelection").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
					setTimeout(function(){
						nodeAncestors($grid,new Rc($("#"+selrow,$grid)));
						$grid.jqGrid("setSelection",selrow,false);
					},400);
				}
				// me.jqGrid("hideCol",hideCols).trigger("reloadGrid");
				//输入框焦点左右移动
				return me;
			});
	   }
   });
   $.fn.bindTable.defaults={treeData:{},loadData:{}};

})(jQuery); 

//;(function ($) { 
//	$.extend($.fn.bindTable.methods,say:function(var msg){
//		alert(msg);
//	});
//})(jQuery);
