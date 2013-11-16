;(function ($) { 
   $.fn.extend({
	   /**
		 * @param url 请求的地址
		 * @param options 自定义的参数
		 * @param funcs   Button按钮的方法
		 */
	   "bindTable":	function(){
			   var url=undefined;
			   var options=undefined;
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
		        	select+="</seelct>";
		        	return select;
		        }
		        //从一个装function对象取出属性为funname的function
		        function getfunction(funcs,funname){
		        	var func = funcs[funname]; 
		        	if (func == undefined) func = function () { eval(funname + "()"); }; 
		        	return func;
		        }
		        //初始化日期格式及按钮的function
		    	function initdate(elem){
			    	setTimeout(function() {
			    		$(elem).datepicker({
			    			showOn: "button",
			    			//buttonImage: "ui-icon-calculator",
			    			dateFormat:"yy-mm-dd",
			    			changeMonth: true,
			    			changeYear: true,
			    			showButtonPanel:true,
			    			showWeek:true});
			    		//fm-button ui-state-default ui-corner-all fm-button-icon-left
			    		$(elem).parent().find("button").addClass("fm-button ui-icon ui-icon-calculator");
			    		},10); 
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
			        var pagesize = me.attr("pagesize")||5; 
			        var datatype ="json"; 
			        var height=me.attr("height")||"auto";
			        var multiselect = me.attr("multiselect") == "1"; 
			        var title = me.attr("title") || document.title; 
			        var pagerId = me.attr("pager") || "pager"; 
			        var dataroot = me.attr("dataroot") || "rows"; 
			        var coltotal=parseInt(me.attr("colnum"))||3; //编辑时的列数
			        var saveurl=me.attr("saveurl");
			        var deleteurl=me.attr("deleteurl")||saveurl;
			        var colNames = []; 
			        var colModels = [];
			        var hideCols=[];
			        var idx = multiselect ? 1 : 0; 
			        var sequence=0;
			        $("thead th", me).each(function (i) { 
			            var th = $(this);
//			            var redinfo=$("<a></a>");必输项目的前缀
						var fix=(required)?"<a style='color:red'>(*)</a>":"";
			            var editable=th.attr("editable")!="0";
			            var viewable=editable?th.attr("viewable")!="0":editable; //如果editable为false则viewable也为false.
			            var name=th.attr("name");
						var index = th.attr("index")||name;
						var width = parseInt(th.attr("width")) || 0;
						// editrules: {edithidden:true, required:true(false), integer:true(false),number:true(false),minValue:val, maxValue:val}
						var sortable = th.attr("sort") == "1";
						var hidden = $.trim(th.text()) == "";
						var title = th.attr("showtitle") != "0";
						var type = th.attr("type") || "string";
						var edittype = th.attr("edittype")||"text";
						var searchable=th.attr("search")!="0";
						var formatter=th.attr("formatter");
						var required = th.attr("require")=="1";
						var edithidden=th.attr("edithidden")=="1";
						var searchhidden=th.attr("searchhidden")=="1";
						var editrules={required:required,edithidden:edithidden,searchhidden:searchhidden};
						var dataUrl=th.attr("url");
						var jsonName=th.attr("jsonName");
						var headerkey=th.attr("headerkey");
						var headervalue=th.attr("headervalue");
						var gridview=th.attr("gridview")!="0";
						if (edittype=="textarea"){
							var pos=sequence%coltotal;
							if (pos>0) {
								sequence+=coltotal-pos;
							}
						}
						var rowpos=Math.floor((sequence+coltotal)/coltotal);
						var formoption=editable?{elmsuffix : fix ,rowpos:rowpos,colpos:(sequence)%coltotal+1}:{};
						var fieldrules;
						var editoptions;
						var searchoptions;
						try{
							var rules=th.attr("fieldrules");
							fieldrules=typeof(eval(rules))=="object"?eval(rules):undefined;
							}catch(e){
						}
							try{
								editoptions=th.attr("editoptions");
								editoptions=typeof(eval(editoptions))=="object"?eval(editoptions):undefined;
							}catch(e){}
							try{
								searchoptions=th.attr("searchoptions");
								searchoptions=typeof(eval(searchoptions))=="object"?eval(searchoptions):undefined;
							}catch(e){}
						var editfunc={};
						var searchfunc={};
				        if(type=="date"){
				        	editfunc={
				        		dataInit:initdate
				        	};
				        }
//						if(edittype=="select"){
//							editfunc={dataUrl:dataUrl,
//										buildSelect:function(data){
//											var json=data;
//											if(typeof(data)=="string"){
//												json=$.parseJSON(data);
//											}
////											console.debug(data);
//											if(json&&jsonName){
//												var items=json[jsonName];
////												console.debug(items);
//												var s=eachitem(items,headerkey,headervalue);
////												console.debug(s);
//												return s;
//											}
//											//alert(data);
//										}
//							};
//							searchfunc={
//									sopt:["eq","cn"]
//
//							};
//						}
					
						editoptions=$.extend(editfunc,editoptions);
//						searchoptions=$.extend(searchfunc,editoptions);
//						editrules=$.extend(editrules,fieldrules);
						var colModel={ 
				                name: name, 
				                index: index,
				                width: width,
//			                    editoptions:{size:10,maxlength:15},
//			                    editrules:editrules,
				                sortable: sortable, 
				                hidden: hidden, 
				                title: title, 
				                type: type,
				                editable:editable,
				                edittype:edittype,  //edittype不同于type,详见手册
				                search:searchable,
				                stype:edittype,
				                viewable: viewable,
				                formoptions :formoption
				            };
//				        if (formatter) {
//				        	if(funcs[formatter]){
//				        		colModel["formatter"]=funcs[formatter];  //formatter不能随意加,如果加在不需要的字段上,会令表格线消失
//				        	}else{
//				        		colModel["formatter"]=formatter;
//				        		if(formatter=="date"){
//				        			var dateformate={srcformat:"Y-m-d",newformat:"Y-m-d"};
//				        			colModel["formatoptions"]=dateformate;
//				        		}
//				        	}
//						}
				        if (editoptions) {
							colModel["editoptions"]=editoptions;
						}
//				        if(searchoptions){
//				        	colModel["searchoptions"]=searchoptions;
//				        }

//						if (colModel.hidden) {
//							colModel["editrules"]={edithidden:true};
////							comModel["hidedlg"]=true;
//						}
//			            if (th.attr("edittype")) {
//							colModel["editoptions"]={
//									dataUrl:th.attr("url"),
//									buildSelect:function(data){
//										alert(data);
//									}
//									dataEvents:[{
//							            type:'change',fn:function(e){
//								alert(1);}},
////								console.debug("editoptions:");
////								console.debug(e);
////								var vid=$(me.find("#"+index));
////								if (!vid) {
////									me.append("<input type=\"hidden\" name='"+index+"' id='"+index+"'></input>")
////									vid=$(me.find("#"+index));
////								}
////								console.debug(vid);
////								vid.val($(this).val());
//								{type:'click',fn:function(e){
//									alert(2);
//							}}]
//				};//先写死
//						}
			            colModels.push(colModel); 
			            if (editable) { 
			            	if(edittype=="textarea"){ //如果是可编辑,并且是textarea则再起一行.
			            		sequence+=coltotal;
			            	}else{
			            		sequence++;//否则按顺序加一列
			            	}
			            }
			            colNames.push(th.text());
//			            if(!gridview){
//			            	hideCols.push(colModel["name"]);
//			            }
			        }); 
//			        //初始化默认配置
//			        var beforeSubmit=function( postdata,formid){
//			        		if(postdata["list_id"]=="_empty"){
//			        			postdata["list_id"]="";
//			        		}
//			        		return [true,''];
//			        	};
//			        //初始化默认按钮
//			        //全局配置
//			        var buttonconfig={
//			        		
//			        };
			        
//			        var initbuttons=[{name:"ok",
//			                        	 caption:"确 定",
//			                     		title:"确定",
//			                    		buttonicon:"ui-icon-plus"},
//			                    		{
//			                    			name:"cancel",
//			                    			caption:"取 消",
//			                    			title:"取消",
//			                    			buttonicon:"ui-icon-plus"
//			                    		},{
//			                    			name:"print",
//			                    			caption:"打 印",
//			                    			title:"打印文件"
//			                    		}];
//			        //---初始化用户自定义按钮
//			        var userbuttons = []; 
//			        // 循环tfoot中的所有button 
//			        $("tfoot button", me).each(function () { 
//			        	var fname = $(this).attr("function"); 
//			        	var myfunc=getfunction(funcs,fname);
//			            userbuttons.push({ 
//			                caption: $(this).text(), 
//			                onClickButton: myfunc
//			            }); 
//			        });
			      
			        //让传入参数可以覆盖初始值
				   options=$.extend({
			        	caption: title, 
			            url: url, 
			            mtype:'GET', //'GET' or 'POST'
			            datatype: datatype, 
			            height: height,
			            colNames: colNames, 
			            colModel: colModels, 
//			            toolbar:[true,"both"], //定义工具栏?
			            rowNum: pagesize, 
			            rowList:[5,10,20],  //可供选择的条数选项
			            pager: pagerId,  //分页工具栏
//			            multikey:'ctrlKey',//多选时按键
//			            autoencode:true,//对URL进行编码,暂时没有起作用
			            altRows:true,//班马线
//			            multiselect: multiselect, //是否可多选
//			        	multiboxonly:true,//点checkbox才有效
		 //        		height: 'auto', 
//			            viewrecords:true,
//			            autowidth: true, 
//						sortable:true,  //是否可排序
//						sortorder: "asc"  , 
						editurl:saveurl,	
			            jsonReader: { 
			            	root: dataroot, 
			            	page: "pageNo",	// json中代表当前页码的数据
							total: "totalPages",	// json中代表页码总数的数据
							records: "totalRows", // json中代表数据行总数的数据
			            	repeatitems: false }
				   ,
				   ajaxSelectOptions: {
					   type: "GET", // one need allows GET in the webmethod (UseHttpGet = true)     
					   contentType: 'application/json; charset=utf-8',    
					   dataType: "json",     
					   cache: false,     
					   data: {         
						   id: function () {  
							   var selid=me.jqGrid('getGridParam', 'selrow');
							   if(selid){
								   return selid;
							   }
							   return -1;
//							   return JSON.stringify($("#list").jqGrid('getGridParam', 'selrow'));
						   		}   
			            	}
			            }
				   },options);
				   me.jqGrid(options).jqGrid("hideCol",hideCols);
				  // me.jqGrid("hideCol",hideCols).trigger("reloadGrid");
			        me.navGrid("#" + pagerId, {add:true,view:true,viewtext:"看一看",cloneToTop:true,closeAfterAdd:true },//options
			        		{width:700,modal:true,closeAfterAdd:true,checkOnSubmit:true,closeOnEscape:true, closeAfterEdit: true,viewPagerButtons:false}, //edit option ,navkeys:[true,38,40]//导航记录,savekey:[true,13]//保存记录
							{width:700,modal:true,beforeSubmit:beforeSubmit,closeOnEscape:true}, //add option onclickSubmit 
							{url:deleteurl},//deloption closeOnEscape:true,
							{multipleSearch:true, multipleGroup:true, showQuery: true},//search option
							{width:700,viewPagerButtons:false,closeOnEscape:true}//view option
							);
			    //    me.jqGrid('filterToolbar',{stringResult: true,searchOnEnter : true});
//			        $("#search").filterGrid("#"+pagerId,{gridModel: true, 
//			        	gridNames:false, gridToolbar: true});//为表格的所有Search为true 的字段增加搜索框并放置在指定的div上
//			        $("#detail").hide();
//			        $("#edit_form").hide();
//			        tableToGrid("#edit_table",{
//			    		autowidth: true,
//			    		height:"auto",
//			    		caption:"新建工程"});
			        
			        if (userbuttons.length > 0) { 
			            $.each(userbuttons, function (i) { 
			                me.navButtonAdd("#" + pagerId, userbuttons[i]); //将按钮加到工具栏tfoot
			            }); 
			        }
			        if (initbuttons.length>0) {
						$.each(initbuttons,function(i){
//							me.navButtonAdd("#t_list",initbuttons[i]);
							var bb=$("#t_list").append("<button>"+this.caption+"</button>");
							
							//me.navButtonAdd("#"+pagerId,initbuttons[i]);
//							initbuttons[i].appendTo("#tb_"+me.id);
						});
					}
			        //将buttons转为jqueryui的button类型
			        $("#t_list :button").button();
				   return me;
			  });
	   },//第1个插件结束
	   "demo":function(options){
		   
	   }
   });
  
})(jQuery); 


