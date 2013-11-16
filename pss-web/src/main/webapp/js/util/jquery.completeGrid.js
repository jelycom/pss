;(function($){
	$.fn.completeGrid=function(){
		var options,parameter;
		$.each(arguments,function(i,n){
			if ($.isArray(n)){
				options=n[0],
				parameter=n[1];
			}
		});
		if (!options)	options={};
		if (!parameter)	parameter={};
		var width=!parameter.width?200:parameter.width,						//默认宽度200
			keySize=!parameter.keylength?2:parameter.keylength,				//默认2字以上再搜索
			createInput=parameter.createInput!=undefined?parameter.createInput:true;		//是否需要创建一个新输入框
		var lastID,wordSize=0,initSize=0,timeoutSet;
		
		$.each(this,function(index){
			var $inputObj=$(this).attr("minlength",keySize),			//给输入框添加一个自定义属性用来判断过滤表格时所需最小长度
				inputID=$inputObj.attr("id"),
				inputClass=$inputObj.attr("class"),
				inputType=$inputObj[0].type;
			if (createInput){				//如果需要发送id，生成一个用来显示名称的输入框，将传送id输入框隐藏
				var nameInputID=inputID.indexOf(".")>=0?inputID.substring(0,inputID.indexOf("."))+"Name":inputID+"Name";
				var $nameInput=$("#"+nameInputID);
				if (!$inputObj.next().is("input")){
					if (inputType=="textarea"){
						var rows=$inputObj.attr("rows");
						$nameInput=$("<textarea class='"+inputClass+"' id='"+nameInputID+"' name='"+nameInputID+"' minlength='"+keySize+"' rows='"+rows+"' />");
					}else{
						$nameInput=$("<input type='text' class='"+inputClass+"' id='"+nameInputID+"' name='"+nameInputID+"' minlength='"+keySize+"' />");
					}
					if ($(this).attr("readonly"))	$nameInput.attr("readonly",true);
					if (!$(this).next("input").length>0)	$(this).val("").hide().after($nameInput);
				}
				$inputObj=$nameInput;					//将点击输入框的事件转给新添加的显示名称的输入框
				inputID=nameInputID;
			}
			if (options.treeGrid&&$.fn.completeGrid.defaults.treeData.resultObj!=null){
				$.extend(options,{datastr:$.fn.completeGrid.defaults.treeData,datatype:"jsonstring"});			//有树时读取缓存的数据，减少请求次数
			}
			$inputObj.data("options",options);			//将传入的参数附加给输入框
			
			var completeGrid="#"+inputID+"Grid",
				completePager="#completePager";
			$inputObj.unbind("click").bind("click",{inputID:inputID},function(event){
				var $completeDiv=$(".completeDiv"),
					$obj=$(this).prev().length>0?$(this).prev():$(this),
					$realInput=$(this),
					inputID=event.data.inputID,
					url=$obj.attr("url")||options.url,
					position=$realInput.offset(),
					lastID=$(".completeDiv table[id*=Grid]").attr("id"),
					condition=$realInput.val();
				initSize=$realInput.val().length;			//获取初始时字符串长度
				var newoptions=$.extend({},$.fn.completeGrid.defaults.options, $inputObj.data("options"),{url:url});
				
				if (!lastID&&!$completeDiv.length){		//载入时没有弹出层时将弹出层添加到body下，并初始化表格
					$completeDiv=$("<div class='completeDiv' style='width:"+width+"px;height:200px;position:absolute;display:none;z-index:2001;'>"+
									"<div class='gridTableDiv'><table id='' class='completeGrid'></table><div id='completePager'></div>" +
									"</div></div>").resizable({
//										animate:true,animateDuration:100,animateEasing: 'swing',
										distance:10,				//拖动10个像素后才开始改变大小
										ghost:true,					//半透明效果
										minHeight:180,maxHeight:360,minWidth:$completeDiv.width(),maxWidth:500,
										stop:function(){
											var $grid=$('.completeGrid',this);
											setTimeout(function(){
												gridSize($grid);
											},300);
										}
									});
					$("body").append($completeDiv);
					$(".completeGrid").attr("id",inputID+"Grid");
					$(completeGrid).jqGrid(newoptions);
				}else if (lastID!=inputID+"Grid"){		//已有表格时，点击的不是上次点击的对象，重新加载表格
					$(".completeDiv table[id*=Grid]").GridUnload();
					$(".completeGrid").attr("id",inputID+"Grid");
					$(completeGrid).jqGrid(newoptions);
				}else if (lastID==inputID+"Grid"&&$inputObj.data("options")){
					var cache=$inputObj.data("options"),
						nPost=$(completeGrid).getGridParam("postData"),			//新条件
						oPost=cache&&cache.postData?cache.postData:{pageSize:$(completeGrid).getGridParam("rowNum")};	//原始条件
					if((parameter.reload||!oPost.filters&&nPost.filters)||(oPost.filters&&nPost.filters!=oPost.filters)){
						//允许重载或者不存在原始过滤或者原始过滤不等于新过滤条件
						console.log("less");
						$(completeGrid).setGridParam({postData:null}).setGridParam({page:1,postData:oPost}).trigger("reloadGrid");	
					}
					if (cache.datatype=="jsonstring"){
						$(completeGrid).setGridParam(cache).trigger("reloadGrid");
					}
				}

				$completeDiv.css({"left":position.left,"top":position.top+$realInput.outerHeight()+2});
				if ((!$completeDiv.is(":visible")||lastID!=inputID+"Grid")&&$inputObj.data("options")){
					$completeDiv.width(width);
					$completeDiv.show(0,function(){	//显示弹出层并设置弹出层的位置
						$completeDiv.unbind('ajaxStop').one('ajaxStop',function(){
							if ($inputObj.is(':text')){
								if (!options.multiselect){
									_filterGrid($(completeGrid),condition,$inputObj);
								}else{
									var ids=$inputObj.prev().val().split(',');
									console.log(ids);
									$.each(ids,function(i,n){
										$(completeGrid).setSelection(n,false);
									});
								}
							}
						});
						$(document).click(function(){
							$completeDiv.hide(200);			//其他地方点击时，隐藏
						});
					}).click(function(e){
						e.stopPropagation();
					});
				}
				event.stopPropagation();
			});
			$inputObj.unbind(".keyevent");			//移除在命名空间内的事件
			$inputObj.bind("keydown.keyevent",function(){
				initSize=$(this).val().length;
			}).bind("keyup.keyevent",function(e){
				var condition=$inputObj.val(),
				keycode=e.which;
				clearTimeout(timeoutSet);
				timeoutSet=setTimeout(function(){
					if($(".completeDiv").is(":visible")&&initSize&&keySize>condition.length&&(wordSize==keySize||condition.length!=initSize)){		//字符长度小于规定字符长度重新加载表格
						console.log(condition.length+"  init:"+initSize);
						if ($(completeGrid).getGridParam('treeGrid')){
							console.log(condition);
							searchTreegrid($inputObj,$(completeGrid));						//树状表格，不是上下箭头、回车时时搜索tree
						}else{
							if (options.postData&&options.postData.filters){			//初始有过滤条件，使用初始过滤条件加载
								var postData=options.postData,
									cPost=$(completeGrid).getGridParam('postData');
								if (cPost.filters!=postData.filters)						//已经是初始表格时，不重新加载
									$(completeGrid).jqGrid('setGridParam',{page:1,postData:postData}).trigger("reloadGrid");
							}else{										//没有初始过滤，使用默认条件
								var postData=$(completeGrid).getGridParam("postData");
								delete postData.filters;
								$(completeGrid).jqGrid('setGridParam',{page:1,postData:postData}).trigger("reloadGrid");
							}
						}
					}
					if(initSize!=$inputObj.val().length&&$inputObj.prev().is("input")&&keycode!=13){			//按键时输入框的值改变时清空id输入框的值
						$inputObj.prev().val("").trigger("change");
					}
				},200);
				wordSize=condition.length;
				_handleKeyBoardNav(e, $(completeGrid));
			}).bind("blur.keyevent",function(){
				var $cInput=$(this),
					id=$cInput.prev().val(),
					val=$cInput.val();
				if (val.length!=0&&$(completeGrid).is(":visible")&&!$cInput.attr("readonly")){				//输入的条件改变后判断输入的值是否在在
					var nameArray=[];
					$("tr[id]",$(completeGrid)).each(function(i,obj){
						var $nameTd;
						if ($("td[aria-describedby$=shortName]",obj)[0])
							$nameTd=$("td[aria-describedby$=shortName]",obj);
						else if($("td[aria-describedby$=fullName]",obj)[0])
							$nameTd=$("td[aria-describedby$=fullName]",obj);
						else
							$nameTd=$("td[aria-describedby$=name]",obj);
						nameArray.push($nameTd.text());
						if ($nameTd.text()==val){		//用于判断手动输入的值是否在列出的表格中（包括隐藏的行）
							$cInput.prev().val(obj.id);
							return false;
						}
					});
					if ($cInput.prev()[0]&&!id){						//不存在id清空后面的值
//					if (val&&$.inArray(val,nameArray)<0){
						$cInput.val("");			//输入的值不存在，清空
//						$(".completeDiv").hide();
					}
				}
				initSize=$(this).val().length;
			});;
		});
		return this;
	}
	
	$.fn.completeGrid.defaults={
		options:{
			url:"",
			datatype:"json",
			mtype:"POST",
			treeGridModel: 'nested', 					//treeGrid为true的配置
            treeIcons: {plus:'ui-icon-folder-collapsed',minus:'ui-icon-folder-open',leaf:'ui-icon-document'},
            ExpandColumn: 'name',
			colNames:["id","名称","拼音"],
			colModel:[{name:"id",hidden:true},{name:"name"},{name:"py",hidden:true}],
			jsonReader:{
				root: "resultObj.rows", 
            	page: "resultObj.pageNo",	// json中代表当前页码的数据
				total: "resultObj.totalPages",	// json中代表页码总数的数据
				records: "resultObj.totalRows", // json中代表数据行总数的数据
				repeatitems:false
			},
			rowNum:5,					//每页的总行数
			rowList:[5,10],
			shrinkToFit:true,			//按比例初始化列宽度
			forceFit:true,				//为真时，改变列宽度不改变表格的宽度
			autoWidth:true,
			pager:"#completePager",
			pagerpos:"left",
			onSelectRow:function(rid,statu,e){
				var $grid=$(this),
					gridID=$grid.attr("id"),
					rd=$grid.getRowData(rid);
				var period=gridID.indexOf(".");			//确定ID选择器是否包含句点 
				if (period>=0)	gridID=gridID.substring(0,period)+"\\"+gridID.substring(period);
				var	$inputObj=$("#"+gridID.substring(0,gridID.lastIndexOf("Grid")));
				if (!$grid[0].p.multiselect){
					if ($inputObj.prev().is(":text,:hidden")&&$inputObj.prev().val()!=rd.id)	$inputObj.prev().val(rd.id).trigger("change");
					$inputObj.val(rd.name||rd.fullName||rd.shortName||rd.dispName).trigger("change").trigger("focus");
					$(".completeDiv").hide();
				}
			},
			loadComplete:function(xhr){		//载入完成后，将表格的pager重新改变尺寸
				if (xhr&&xhr.resultObj){
					$.fn.completeGrid.defaults.loadData={};
					$.extend($.fn.completeGrid.defaults.loadData,xhr);
				}
				var $grid=$(this),
					gridID=$.insertString($grid.attr("id"));
				if ($grid[0].p.pagerpos=='none')	$("#completePager_left").remove();
				$("#completePager_center").remove();
				var timeoutSet;
				var	$inputObj=$("#"+gridID.substring(0,gridID.lastIndexOf("Grid")));
				var initSize=$inputObj.val().length;
				gridSize($grid);
				$inputObj.unbind(".gridevent");
				$inputObj.bind("keydown.gridevent",function(){
					initSize=$inputObj.val().length;				//按键完成后将值的长度赋给初始长度，用来下次判断
				}).bind("keyup.gridevent",function(e){
					clearTimeout(timeoutSet);
					timeoutSet=setTimeout(function(){
						console.log('load');
						var valLength=$inputObj.val().length;
						if ($(".completeDiv").is(":visible")&&valLength!=initSize&&valLength>=$inputObj.attr("minLength")){
							console.log($inputObj.val().length>$inputObj.attr("minLength"));
			    			if ($grid.getGridParam("treeGrid")){
			    				console.log($inputObj.val());
				    			searchTreegrid($inputObj,$grid);	//不是上下箭头、回车时时搜索tree
				    		}else{
								var condition=$inputObj.val();
								_filterGrid($grid,condition,$inputObj);
				    		}
						}
					},600);
				});
				if ($grid[0].p.multiselect&&!$grid[0].nav){
					$grid.navGrid("#completePager",{add:false,edit:false,del:false,search:false,refresh:false,position:"right"})
						.navButtonAdd("#completePager",{
							caption:"确定",buttonicon:"ui-icon-check",position:"last",
							onClickButton:function(){
								var arrname=[];
								var selarr=$grid.getGridParam('selarrrow');
								$.each(selarr,function(i,n){
									var rd=$grid.getRowData(n);
									arrname.push(rd.name||rd.dispName);
								});
								$inputObj.val(arrname.toString()).prev().val(selarr.toString());
								$grid.parents(".completeDiv").hide();
							}
						}).navButtonAdd("#completePager",{
							caption:"关闭",buttonicon:"ui-icon-close",position:"last",
							onClickButton:function(){
								$grid.parents(".completeDiv").hide();
							}
						});
				}
			},
			ondblClickRow:null,
			gridComplete:function(){
				var $grid=$(this);
				if($grid.getGridParam("treeGrid"))	$($grid[0].grid.hDiv).hide();		//TreeGrid时隐藏标题行
			}
		},
		loadData:{},treeData:{}
	}
	
	var keyMap = {
      'left'  : 37,
      'up'    : 38,
      'right' : 39,
      'down'  : 40,
      'enter' : 13,
      'cancel': 27,
      'pageup': 33,
      'pagedn': 34
    };
	function _handleKeyBoardNav(e, $table) {
	    var
	      code		= e.keyCode,
	      tree		= $table.getGridParam("treeGrid"),
	      $tr		= $table.find('tr:[id]'),
	      $current	= $tr.filter("#"+$table.getGridParam("selrow")),
	      id		= $current.attr("id"), 
	      open		= $table.is(":visible"),
	      $pDiv		= !$table[0]?undefined:$($table[0].grid.bDiv),
	      first		= $tr.first(),
	      last		= $tr.last(),
	      pager		= !$table.getGridParam("pager")?'':$table.getGridParam("pager").substring(1),
	      $inputObj	= $(e.target),
	      next,prev,timeoutSet
	    ;
	    function scrollControl(){
			var $selrow=$("#"+$table.getGridParam("selrow"),$table);
			if ($selrow[0]){
				if ($selrow.position().top<$pDiv.scrollTop()){
					$pDiv.scrollTop($selrow.position().top);			////在显示区域上方时，滚动到选择行的顶部
				}else if($selrow.height()+$selrow.position().top>$pDiv.scrollTop()+$pDiv[0].clientHeight){
					if (e.which==keyMap.up)
						$pDiv.scrollTop($selrow.position().top);				//在显示区域下方时，向下滚动
					if (e.which==keyMap.down)
						$pDiv.scrollTop($pDiv.scrollTop()+$pDiv[0].clientHeight+$selrow.height());
				}
			}
		}
	
	    switch (code) {
	    	case keyMap.up:
		        if (open) {
		        	prev = $current.prevAll('[id]:visible:first');
					if (prev.length) {
					  $table.setSelection(prev.attr("id"),false);
					} else {
					  $table.setSelection($tr.last().attr("id"),false);
					}
					scrollControl();
		        } else {
					$inputObj.trigger("click");
		        }
		        e.preventDefault();
		        break;
		    case keyMap.down:
		        if (open) {
		        	next = $current.nextAll('[id]:visible:first');
			        if (next.length){
			            $table.setSelection(next.attr("id"),false);
			        }else{
			            $table.setSelection($tr.first().attr("id"),false);
			        }
			        scrollControl();
		        }else{
		        	$inputObj.trigger("click");
		        }
		        e.preventDefault();
		        break;
		    case keyMap.enter:
		    	if (open&&$current[0]){
		    		var gridID=$table.attr("id");
			    	if ($current[0]){
//			    		var rd=$table.getRowData(id);
//						if ($inputObj.prev().is(":text,:hidden")&&$inputObj.prev().val()!=rd.id){
//							$inputObj.val(rd.name||rd.fullName||rd.shortName).prev().val(rd.id).trigger("change");
//						}else{
//							$inputObj.val(rd.name||rd.fullName||rd.shortName).trigger("change");
//						}
			    		$table.setSelection(id,true);
//			    		$(".completeDiv").hide();
			    	}
		    	}
				e.preventDefault();
				break;
		    case keyMap.cancel:
		    	$(".completeDiv").hide();
		    	break;
		    case keyMap.left:
		    	if (open&&tree){
		    		console.log(keyMap.left);
	    			var rc=new Rc($current);
	    			if (rc.expanded&&rc.isLeaf=="false"){
						$current.find(".treeclick").trigger("click");									
					}else if(!rc.expanded&&$table.getNodeParent(rc)){
						var parentid = $table.getNodeParent(rc)._id_;
						$("#"+parentid,$table).find(".treeclick").trigger("click");
						$table.setSelection(parentid,false);
					}
					break;
		    	}
		    	break;
		    case keyMap.right:
		    	if (open&&tree){
		    		console.log(keyMap.right);
	    			var rc=new Rc($current);
	    			if (!rc.expanded&&rc.isLeaf=="false"){
						$current.find(".treeclick").trigger("click");									
					}
		    	}
		    	break;
		    case keyMap.pageup:
		    	if (open&&!tree)	$("#prev_"+pager).trigger("click");
		    	break;
		    case keyMap.pagedn:
		    	if (open&&!tree)	$("#next_"+pager).trigger("click");
		    	break;
		    default:
		    	if (!open&&$inputObj.attr('minlength')<=$inputObj.val().length){
		    		$inputObj.trigger("click");
		    	}
		    	break;
	    }
	 }
	
	//根据传入的值过滤表格中的数据
	function _filterGrid($grid,condition,$inputObj){
		if (!$grid[0]||!condition||!$inputObj[0])	return false;
		var colModel=$grid.jqGrid("getGridParam","colModel");
		var colname=[],rulesString="",newPostData;
		var postData=$inputObj.data("options").postData;
		$.each(colModel,function(){
	    	if (this.name!='id'&&this.fuzzy==undefined)	colname.push(this.name);
		});
		//模糊搜索条件
		$.each(colname,function(i,n){
			rulesString+='{"field":"'+ n +'","op":"cn","data":"'+condition+'"}';
			if (i+1<colname.length)	rulesString+=','
		});
		if (postData){		//传入有初始过滤条件时postData设置为多重过滤
			var rules=postData.filters;
			newPostData = {
				'filters':'{'+rules.substring(1,rules.length-1)+
					',"groups":[{'+
						'"groupOp":"OR","rules":['+rulesString+']'+
				',groups:[]}]}'
			};
		}else{						//没有初始过滤条件就过滤每个列
			newPostData={'filters':'{"groupOp":"OR","rules":['+rulesString+']}'};
		}
		$grid.jqGrid('setGridParam',{page:1,postData:newPostData}).trigger("reloadGrid");//过滤表格
		$grid.ajaxComplete(function() {
			$(this).setSelection($(this).find("tr[id]:first").attr("id"),false);
		});
	}
	
})(jQuery);