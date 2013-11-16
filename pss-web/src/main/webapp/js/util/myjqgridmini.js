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
//			        var height=me.attr("height")||"auto";
//			        var multiselect = me.attr("multiselect") == "1"; 
//			        var title = me.attr("title") || document.title; 
			        var pagerId = me.attr("pager") || "pager"; 
			        var dataroot = me.attr("dataroot") || "rows"; 
			        var coltotal=parseInt(me.attr("colnum"))||3; //编辑时的列数
			        var saveurl=me.attr("saveurl");
			        var deleteurl=me.attr("deleteurl")||saveurl;
			        var colNames = []; 
			        var colModels = [];
			        $("thead th", me).each(function (i) { 
			            var th = $(this);
			            var editable=th.attr("editable")!="0";
			            var name=th.attr("name");
						var index = th.attr("index")||name;
						var edittype = th.attr("edittype")||"text";
						var formatter=th.attr("formatter");


						var colModel={ 
				                name: name, 
				                index: index,
				                title: title, 
				                editable:editable,
				                edittype:edittype, 
				                editoptions:{
				                	dataInit:function(elem){
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
				    			    		//$(elem).parent().find("button").addClass("fm-button ui-icon ui-icon-calculator");
				    			    		},10); 
				                	}
				                } //edittype不同于type,详见手册
				            };

			            colModels.push(colModel); 

			            colNames.push(th.text());
			        }); 

			      
			        //让传入参数可以覆盖初始值
//				   options=$.extend({
//			        	caption: title, 
//			            url: url, 
//			            mtype:'GET', //'GET' or 'POST'
//			            datatype: datatype, 
//			            height: height,
//			            colNames: colNames, 
//			            colModel: colModels, 
//			            toolbar:[true,"both"], //定义工具栏?
//			            rowNum: pagesize, 
//			            rowList:[5,10,20],  //可供选择的条数选项
//			            pager: pagerId,  //分页工具栏
////			            multikey:'ctrlKey',//多选时按键
//			            autoencode:true,//对URL进行编码,暂时没有起作用
//			            altRows:true,//班马线
//			            multiselect: multiselect, //是否可多选
////			        	multiboxonly:true,//点checkbox才有效
//		 //        		height: 'auto', 
//			            viewrecords:true,
//			            autowidth: true, 
//						sortable:true,  //是否可排序
//						sortorder: "asc"  , 
//						editurl:saveurl,	
//			            jsonReader: { 
//			            	root: dataroot, 
//			            	page: "pageNo",	// json中代表当前页码的数据
//							total: "totalPages",	// json中代表页码总数的数据
//							records: "totalRows", // json中代表数据行总数的数据
//			            	repeatitems: false }
//				   ,
////				   ajaxSelectOptions: {
////					   type: "GET", // one need allows GET in the webmethod (UseHttpGet = true)     
////					   contentType: 'application/json; charset=utf-8',    
////					   dataType: "json",     
////					   cache: false,     
////					   data: {         
////						   id: function () {  
////							   var selid=me.jqGrid('getGridParam', 'selrow');
////							   if(selid){
////								   return selid;
////							   }
////							   return -1;
//////							   return JSON.stringify($("#list").jqGrid('getGridParam', 'selrow'));
////						   		}   
////			            	}
////			            }
//				   },options);
				   me.jqGrid(options).jqGrid("hideCol",hideCols);
				  // me.jqGrid("hideCol",hideCols).trigger("reloadGrid");
			        me.navGrid("#" + pagerId, {add:true,view:true,viewtext:"看一看",cloneToTop:true,closeAfterAdd:true },//options
			        		{width:700,modal:true,closeAfterAdd:true,checkOnSubmit:true,closeOnEscape:true, closeAfterEdit: true,viewPagerButtons:false}, //edit option ,navkeys:[true,38,40]//导航记录,savekey:[true,13]//保存记录
							{width:700,modal:true,closeOnEscape:true}, //add option onclickSubmit beforeSubmit:beforeSubmit,
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
			        
//			        if (userbuttons.length > 0) { 
//			            $.each(userbuttons, function (i) { 
//			                me.navButtonAdd("#" + pagerId, userbuttons[i]); //将按钮加到工具栏tfoot
//			            }); 
//			        }
//			        if (initbuttons.length>0) {
//						$.each(initbuttons,function(i){
////							me.navButtonAdd("#t_list",initbuttons[i]);
//							var bb=$("#t_list").append("<button>"+this.caption+"</button>");
//							
//							//me.navButtonAdd("#"+pagerId,initbuttons[i]);
////							initbuttons[i].appendTo("#tb_"+me.id);
//						});
//					}
			        //将buttons转为jqueryui的button类型
//			        $("#t_list :button").button();
				   return me;
			  });
	   },//第1个插件结束
	   "demo":function(options){
		   
	   }
   });
  
})(jQuery); 


