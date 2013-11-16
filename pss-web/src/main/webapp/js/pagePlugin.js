var lastSel;
function accountOtherEdit(id){
	var $jxcGrid=$(this),
		options=$jxcGrid[0].p;
	if (!$(this).parents(".ui-dialog").is("[id^=view]")){
		var editRowSet={keys:false,									//行编辑时的参数
			oneditfunc:function(rowid){
				var $rowObj=$jxcGrid.find("#"+rowid);
				var blankData={};
				$.each(options.colModel,function(i,n){
					if (n.name!='rn')	blankData[n.name]="";
				});
				$jxcGrid.jqGrid("setSelection",rowid,false);
				inputMove($rowObj);
				//商品改变时自动填充资料
				$(":text[id$=ame]",$rowObj).change(function(){
					var $completeGrid=$(".completeDiv table[id]"),
						selrow=$completeGrid.getGridParam("selrow"),
						colNames=$completeGrid[0].p.colNames,
						colModel=$completeGrid[0].p.colModel,
						colIndex=[],
						priceArray=[];
					$.each(colModel,function(i,n){
						colIndex.push(n.name);			//将列名称存为数组，以便获取索引
					});
					if (!$(this).val()){
						$jxcGrid.delRowData(rowid);
						if($("#"+next,$jxcGrid)[0])	$jxcGrid.addRowData(rowid,blankData,"before",next);
						else						$jxcGrid.addRowData(rowid,blankData,"last");
						calculateTotalrow();
						return false;
					}
					gridData=$completeGrid.getRowData(selrow);
					$.each(gridData,function(i,n){
						if (i=="id"){
							$("td[aria-describedby$=Id]",$rowObj).text(n);
						}
					});
				});
	        	var $pay=$("input[name=pay]",$rowObj);
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
        		$pay.change(function(){									//数量变化时
        			calculation();
        		});
	        	function calculation(){
	        		var payArray=$jxcGrid.getCol("pay",false),
	        			totalPay=Number($pay.val());
	        		for (var i=0;i<payArray.length;i++){
	        			if (isNaN(Number(payArray[i]))==false){
	        				totalPay+=Number(payArray[i]);
	        			}
	        		}
	        		$jxcGrid.footerData("set",{pay:totalPay});
	        		inputCalculate(totalPay);
	        	}//计算合计，单价、数量改变时，保存行数据时要用到
			}//编辑时执行的操作
		};
    	var $blankTr,gridData,currentData,editID;		//第一个空行、自动完成选中的行、当前选中行、需要编辑的行
    	
    	if (id!=lastSel&&lastSel){				//已有选中行时，保存已选中行
    		if(!$("#"+lastSel+" :text:first",$jxcGrid).val()){
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
    		if ($.inArray(e.target,$jxcGrid.find("*"))<0&&$jxcGrid.is(":visible")){
    			if ($("#"+lastSel+" :text:first",$jxcGrid).val()!=""){
    				$jxcGrid.jqGrid("saveRow",lastSel,{url:"clientArray"});
    			}else {
					$jxcGrid.jqGrid("restoreRow",lastSel);
				}
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
}