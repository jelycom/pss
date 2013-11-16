;(function($){
	$.fn.filterGrid=function(){
		$.each(this,function(){
			var $grid=$(this),
				gridID=$grid.attr("id");
			$grid.jqGrid("searchGrid",{afterShowSearch:function(){
				$("#searchcntfbox_"+gridID+" :button").livequery(function(){
					$(this).button();
				});
				if ($grid.attr("noSaveFilter")!="1"&&!$(".EditButton").is(":has(#"+gridID+"saveFilter)")){
					$(".EditButton:first").append("<a id='"+gridID+"saveFilter' class='fm-button ui-state-default ui-corner-all ui-search'>保存当前过滤器</a>");
				}
				var $saveFilter=$("#"+gridID+"saveFilter")
				$saveFilter.click(function(){			//点击保存过滤条件时，将当时的搜索条件存到一个值里
					//保存搜索条件的DIV
					var $saveName=$("#saveName");
					if (!$("body").is(":has(#saveName)")){
						$saveName = $("<div id='saveName' title='保存过滤器'>" +
								"<p id='tips'>　</p><br/>过滤器名称：<input type='text' id='filterName' name='filterName' />" +
								"<input type='hidden' id='filterCond' name='filterCond' /></div>");
						$saveName.dialog({autoOpen:false,modal:true,resizable:false,minHeight:false,
							buttons:{
								"确定":function(){
									//保存过滤器名称，名称为空返回，已有名称返回
									var $tips=$("#tips"),									//提示信息区域
										$filterName=$("#filterName"),						//名称输入框
										app = $("#app").val()||$grid.attr("actionName"),	//和某个action对应的条件
										name = $filterName.val(),							//保存的名称
										filterCond = $("#filterCond").val(),				//搜索条件
										$selectobj = $("#"+gridID+"filterId"),				//已有名称下拉框
										existsName = [];									//用来存放已有名称
									$selectobj.children("option").each(function(){
										existsName.push($(this).text()); 
									});
									if (name==""){
										$tips.addClass("ui-state-error").text("过滤器名称必填！");
										$filterName.trigger("focus");
										return false;
									}else if($.inArray(name,existsName)>0){
										$tips.addClass("ui-state-error").text("该名称已存在！");
										$filterName.trigger("focus");
										return false;
									}
									
									$.ajax({
										type:"post",
										dataType:"json",
										data:{name: name,value: filterCond,actionName: app},
										url:"querydata_save.action",
										error:function(){
											alert("过滤器保存失败!");
										},
										success:function(data){
											if (data.operate){
												alert("过滤器保存成功!");
												$selectobj.append("<option id='"+data.resultObj.id+"' value="+ filterCond +">"+name+"</option>");	//添加到过滤器下拉框
												if ($selectobj.parent().is(":hidden"))	$selectobj.parent().show();			//如果下拉框隐藏则显示。
											}
										}
									});
									$(this).dialog("close");
								},
								"放弃":function(){
									$(this).dialog("close");
								}
							},
							open:function(e){
								var groupOp=$(".group select:first :selected").val();
								var conditionTr=$(".group tr:has(.data)");
								var rules=[];
								conditionTr.each(function(index){				//遍历现在的条件，并存为一个字符串
									var $tr=$(this),
										field=$tr.find(".columns :selected").val(),
										op=$tr.find(".operators :selected").val(),
										data=$tr.find(".data").children().val();
									if (!data||data=="-1"){
										alert("条件不完整或为空！");
										$(e.target).dialog("close");
									}
									rules.push("{\'field\':'"+field+"\',\'op\':\'"+op+"\',\'data\':\'"+data+"\'}");
								});
								var filters;
								var initFilter=$grid.data("initFilter");
								if (initFilter){										//如果存在原始过滤条件，将其附加到搜索条件中
									filters='{'+initFilter.substring(1,initFilter.length-1)+
											',"groups":[{"groupOp":"OR","rules":['+rules.toString()+'],groups:[]}]}';
								}else{													//不存在就只使用现在的搜索条件
									filters="{\'groupOp\':'"+groupOp+"',\'rules\':["+rules.toString()+"]}";
								}
								$(":text",this).val("");								//清空输入框的值并清空提示信息行
								$("p",this).removeClass("ui-state-error").text("　");
								$("#filterCond",this).val(filters);
							}
						});
					}
					$saveName.dialog("open");
				});
			}});
		});
		return this;
	};
	
	$.fn.fuzzySearch=function(){
		$.each(this,function(){
			var $grid=$(this),
				gridID=$grid.attr("id");
			var $toolbar=$("#t_"+gridID);
			var colModels=$grid.getGridParam("colModel");
			var $searchDiv = $("<div id='"+gridID+"searchSpan' style='float:right;'>" +
					"<input type='text' id='"+gridID+"condition' style='width:80px;' /><button id='"+gridID+"filterButton' class='tButton'>查询</button>" +
					"</div>");
			if (!$toolbar.is(":has(#"+gridID+"searchSpan)")){
				$toolbar.append($searchDiv);
				//根据输入框的值作为条件模糊搜索
				$("#"+gridID+"filterButton").click(function(){				//点击查询按钮
					var initFilter=$grid.data("initFilter");				//读取表格的原始过滤条件
					console.log(initFilter);
					var condition = $("#"+gridID+"condition").val();
					var fuzzySearchFiled = [],
						newPostData;
						rulesString=[];
					//获取此表格的模糊查询字段
					$.each(colModels,function(){
					    if (this.fuzzySearch){
					        fuzzySearchFiled.push(this.name);
					    }
					});
					//模糊搜索条件
					$.each(fuzzySearchFiled,function(index){
						rulesString.push('{"field":"'+ fuzzySearchFiled[index] +'","op":"cn","data":"'+condition+'"}');
					});
					if (initFilter){										//如果存在原始过滤条件，将其附加到搜索条件中
						newPostData={
								'filters':'{'+initFilter.substring(1,initFilter.length-1)+
									',"groups":[{'+
										'"groupOp":"OR",' +
										'"rules":[' + rulesString +	']' +
								',groups:[]}]}'
							}
					}else{													//不存在就只使用现在的搜索条件
						newPostData = {'filters':'{"groupOp":"OR","rules":[' + rulesString + ']}'}
					}
					$grid.jqGrid('setGridParam',{page:1,postData:newPostData}).trigger("reloadGrid");
				});
				if ($grid.attr("noSaveFilter")!="1"){
					var option ="<option value='-1'>重置过滤器</option>";
					$.ajax({
						type:"post",
						dataType:"json",
						data:{actionName: $("#app").val()||$grid.attr("actionName")},
						url:"querydata_loadFilter.action",
						error:function(){
							alert("过滤器条件加载失败!");
						},
						success:function(data){
							var $filterSelect=$("<span id='"+gridID+"filterSelect' style='display:none'><select id='"+gridID+"filterId'>"+option+"</select>" +
												"<button id='"+gridID+"manageFilter' class='tButton'>管理过滤器</button></span>");
							$searchDiv.append($filterSelect);
							//过滤器改变时根据选中值过滤表格
							$("#"+gridID+"filterId").change(function(){
								var filters = $(this).val();
								var initFilter=$grid.data("initFilter");
								if (filters=="-1"&&initFilter){
									$grid.jqGrid("setGridParam",{postData:{'filters':initFilter}}).trigger("reloadGrid");
								}else{
									$grid.jqGrid("setGridParam",{postData:{'filters':filters}}).trigger("reloadGrid");
								}
							});
							if(data&&data.resultObj.length){
								data=data.resultObj;
								for(var i=0;i<data.length;i++){
									option = option + "<option value="+data[i].value+" id="+data[i].id+">"+data[i].name+"</option>";
								}
								$("#"+gridID+"filterId").empty().append(option);
								$filterSelect.show();
							}
							$searchDiv.find(":button").button();			 //将buttons转为jqueryui的button类型
						}
					});
				}else{
					$searchDiv.find(":button").button();			 //将buttons转为jqueryui的button类型	
				}
			}
			//管理过滤器按钮
			$("#"+gridID+"manageFilter").live("click",function(){
				if ($("#"+gridID+"filterId option").length==1){
					return false;
				}
				var $manageButton=$(this);
				if (!$("body").is(":has(#manageFilterDiv)")){		//添加管理过滤器的DIV
					$("body").append("<div id='manageFilterDiv' title='管理过滤器'>" +
									"<table id='manageTable'></table>" +
									"<div id='acceptDel'><p id='delName'></p><br /><p>你确定要删除此过滤器？</p><br />" +
									"<button class='accept'>确定</button><button class='cancel'>返回</button><div>" +
									"</div>");
					$("#manageFilterDiv").dialog({autoOpen:false,modal:true,resizable:false,minHeight:false,
									buttons:{
										"返回":function(){
											$(this).dialog("close");
										}
									},
									create:function(){			//初始化按钮
										$(this).find(".accept").button({icons:{primary:"ui-icon-check"}});
										$(this).find(".cancel").button({icons:{primary:"ui-icon-close"}});
									},
									open:function(){
										var $manageTable = $("#manageTable"),		//显示过滤器名称的表格
											$acceptDel = $("#acceptDel"),			//确定删除的DIV
											$filterId = $("#"+gridID+"filterId");				//选择过滤器下拉列表
										$manageTable.children().remove();
										var $options = $filterId.children(":gt(0)");
										$options.each(function(){					//将下拉框的过滤器名称添加到管理过滤器的表格
											$manageTable.append("<tr id='"+this.id+"'><td style='width:50%;text-align:center;'>"+$(this).text()+
																"</td><td><button class='delFilter'>删除过滤器</button></td></tr>");
										});
										$(".delFilter").button({icons:{primary:"ui-icon-trash"}}).unbind("click").click(function(){		//点击删除按钮
											var filterName=$(this).parent().prev().text();					//删除的名称
											var deleteId = $(this).parents("tr").attr("id");
											$manageTable.hide();					//隐藏过滤器名称表格
											$acceptDel.show(0,function(){			//显示确定删除的DIV
												$acceptDel.children(":first").html("<b>"+filterName+"</b>");
												$acceptDel.children(".accept").unbind("click").click(function(){			//确定删除时
													$.ajax({
														type:"post",
														dataType:"json",
														data:{id: deleteId},
														url:"querydata_delete.action",
														error:function(){
															alert("过滤器删除失败!");
														},
														success:function(data){
															alert("过滤器删除成功!");
															$manageTable.find("#"+deleteId).remove();
															$filterId.children("#"+deleteId).remove();
														}
													});
													$acceptDel.hide();
													$manageTable.show();
												});
												$acceptDel.children(".cancel").click(function(){
													$acceptDel.hide();
													$manageTable.show();
												});
											});
										});
									}
								});
				}
				$("#manageFilterDiv").dialog("open");
			});			
		});
		return this;
	};
})(jQuery)