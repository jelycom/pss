	//获取网站的绝对路径
	function siteUrl(){
		var website=window.parent.location.href;		//获取链接地址
		var s=0;
		for (var i=0;i<=3;i++){
			s=website.indexOf("/",s+1);
		}
		var tld=website.substring(0,s+1);            //获取到网站绝对地址
		return tld;
	}	
	//重新计算grid表格的高度和宽度
	function gridSize($grid){
		setTimeout(function(){
			var gridDiv=$grid.parents(".ui-jqgrid-view"),
				gridLength=$(".treedata_div .ui-jqgrid:visible").length,
				$gridFather=$grid.parents(".gridTableDiv"),
				formWidth=$("#mainform").width();
//				wWidth=$(window).width(),													//窗口宽度
//				newWidth=wWidth - $(".tree_div").width();
			var gridId=$grid.attr("id");
			var cDivH=gridDiv.find(".ui-jqgrid-titlebar:visible").outerHeight(true),	//表格标题的高度
				pagerH=gridDiv.nextAll(".ui-jqgrid-pager").outerHeight(true),			//分页行的高度
				hdivH=gridDiv.find(".ui-jqgrid-hdiv:visible").outerHeight(true),				//标题单元格行的高度
				sdivH=gridDiv.find(".ui-jqgrid-sdiv").outerHeight(true),				//脚部行的高度
				toolbarH=$("#t_"+gridId).outerHeight(true)+$("#tb_"+gridId).outerHeight(true);			//上下工具行的高度
			if(gridId=="jxctable"||gridId=="echoGrid"){
				$grid.setGridWidth(formWidth-3);
			}else if (gridId!="jxctable"&&gridId.indexOf("_table")<0){
				$grid.setGridHeight($gridFather.height()-cDivH-pagerH-hdivH-sdivH-toolbarH-3);
				$gridFather.is($("#pageWest").children())?$grid.setGridWidth($("#pageWest").width()-2):$grid.setGridWidth($gridFather.width()-3);
			}else if(gridId.indexOf("_table")>=0){
				$grid.setGridWidth($gridFather.width()-3);			//弹出的表格只改变宽度
			}
		},100);
	}
		
	//根据行生成treegrid记录用来展开节点
	function Rc(record,$grid){
		if (!$grid)	$grid=$(record).parents("table");
		$record=$grid.find($(record));
		var rc=this;
		rc._id_=$record.attr("id");
		$.each($record.children(),function(i,n){
			var attr=$(n).attr("aria-describedby"),
				real=attr.substring(attr.indexOf('_')+1);
			switch (real) {
				case 'shortName':
				case 'fullName':
					rc.name=n.title;
					break;
				default:
					rc[real]=n.title;
					break;
			}
		});
		rc.expanded=$record.find(".ui-icon").hasClass("tree-minus");
		return rc;
	}
	
	function nodeAncestors($grid,rc){
		var rc=$grid.getNodeParent(rc);		//将父级变量作为参数用于展开
		while (rc){
			var record=$("#"+rc._id_,$grid);
			var icon=record.find(".treeclick");
			if (icon.hasClass("tree-plus")){
				$grid.jqGrid("expandRow",rc);
				$grid.jqGrid("expandNode",rc);
			}
			rc=$grid.getNodeParent(rc);					//搜索祖先级元素,没有就跳出循环
		}
	};
	
	//过滤treegrid行
	function searchTreegrid($input,$treeGrid){
		setTimeout(function(){
			var text=$input.val(),
				$treeTr=$treeGrid.find("tr[id]"),
				$bDiv=$($treeGrid[0].grid.bDiv);
			if (!text){
				$treeTr.find("span:first").each(function(){
					if ($(this).parent().is("b")) $(this).unwrap();			//先去除其他行的加粗样式
				});
				var rootNode=$treeGrid.getRootNodes()[0];
				var trunkTr=$treeTr.filter(function(index){
						return $(this).children("td[aria-describedby*=isLeaf]").text()!="true"&&$(this).find(".ui-icon").hasClass("tree-minus");		//展开的节点
					});
				trunkTr.find(".ui-icon").trigger("click");
				if (rootNode)  $treeGrid.find("#"+rootNode._id_).show();
			}else if(text.length>1){
				$treeTr.show();
				var $containsTr=$treeTr.filter(function(index){
					return $(this).children("td[aria-describedby*=item],td[aria-describedby*=ame],td[aria-describedby*=py]").text().indexOf(text)>=0;	//返回有输入框值的行
				});
				$treeTr.find("span:first").each(function(){
					if ($(this).parent().is("b")) $(this).unwrap();			//先去除其他行的加粗样式
				});
				var showTr=[];					//用来储存要显示的行，即搜索出来的行及其祖先行
				$containsTr.each(function(){
					var rowid=this.id;
					$(this).find("span:first").wrap("<b></b>");				//给搜索结果加粗
					var parentsTr=$treeGrid.getNodeAncestors(new Rc(this));
					showTr.push(rowid);
					for (var i=0;i<parentsTr.length;i++){
						showTr.push(parentsTr[i]._id_);						//将祖先行的ID存到变量中
					}
					var rc=new Rc(this);		
					nodeAncestors($treeGrid,rc);//搜索节点的祖先行并展开
				});
				showTr.sort();												//排序并去掉重复行
				$.unique(showTr);
				$treeTr.each(function(){
					if  ($.inArray(this.id,showTr)<0)	$(this).hide();		//不是祖先行及本身行就隐藏
				});
			}
			$bDiv.scrollTop(0);				//回到初始位置
		},500);
	}
	
	//jqdialog定位
	function jqdialogPosition($dialog){
		$dialog.siblings(".ui-widget-overlay").hide();				//解决先查看引起的编辑新增时弹出保存确认的overlay
		var dgID=$dialog.attr("id"),
			$obj=$dialog.closest(".ui-jqdialog,.ui-dialog");
		if (dgID){
			var dgName=dgID.substring(dgID.indexOf("_")+1);
			var $overlay=$obj.siblings(".ui-widget-overlay[id*="+dgName+"]");
		}else{
			var $overlay=$obj.siblings(".ui-widget-overlay");
		}
		zindex=parseInt($obj.css("z-index"));
		if (!$obj.parent().is("body")){								//如果弹出框不在body下并且有同级的overlay时
			if ($overlay[0])										//都附加到body下
				$overlay.css("z-index",zindex-1).add($obj).prependTo("body");
			else{													//如果没有同级overlay，显示body下的overlay，只将弹出框附加到body下
				$obj.prependTo("body");
				$("body>.ui-widget-overlay[id]").show();
			}
		}
		else	$overlay.show();									//已在body下时，同级的overlay显示
		var wWidth=$('body')[0].clientWidth,
			wHeight=$('body')[0].clientHeight,
			left=(wWidth-$obj.outerWidth(true))/2,
			top=(wHeight-$obj.outerHeight(true))/2;
		$obj.css({"position":"absolute","left":left,"top":top});
	}
	
	//控制文本输入框焦点的移动
	function inputMove($obj){
   		var $input=$obj.find(":text:visible:not(:disabled)");
   		$input.filter("[name$=fullName],[name$=name]").keyup(function(){
   			$input.filter("[name*=py]").val($(this).toJP());		//将名称或全名转换成拼音码
   		});
   		$input.filter(":not([readonly])").eq(0).trigger("focus").trigger("select");			//编辑窗口打开后，第一个输入框获取焦点并选中
      	$input.keydown(function(event){
			var keycode=event.which;
			var index=$input.index(this);
			var name=$(this).attr("name");
			var divStatu=$(".selectTree").is(":visible")||$(".completeDiv").is(":visible");
			if ((keycode==13||keycode==39)&&!divStatu){
				$input.eq(index+1).trigger("focus").trigger("select");
				return false;
			}else if (keycode==37&&!divStatu){
				if (this.selectionEnd==0){
					if (!index)   return false;
					$input.eq(index-1).trigger("focus").trigger("select");
//						return false;
				}
			}else if(keycode==27&&$(this).parents("#jxctable")[0]){
//					var id=$(this).parents("tr").attr("id");
//					$(this).parents("#jxctable").jqGrid("restoreRow",id).jqGrid("resetSelection");
//					return false;
			}
		});
   }
	
	function DateFormat(value,currentDate) {			//返回的日期格式
			var date=new Date(parseInt(value.replace("/Date(", "").replace(")/", ""), 10));
            var month=date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
			var currentDate=currentDate < 10 ? "0" + currentDate : currentDate;
            return date.getFullYear() + "-" + month + "-" + currentDate;
        }
	
	function currDate($a){								//获取并填入当前日期
		if (!$a)	$a=$(".currDate");
		var today=new Date(),
			tyear=today.getFullYear(),
			tmonth=today.getMonth()+1,
			tday=today.getDate();
		if (tmonth<10)	tmonth="0"+tmonth;				//设置日期，月、日不到10的前面加0
		if (tday<10)	tday="0"+tday;
		$a.val(tyear+"-"+tmonth+"-"+tday).trigger("change");
	}
	function isDate(str){
	    var reg=/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;   
	    return reg.test(str)?true:false;
	}

	//取得行内容，用来判断是否为空
	function getObjData(table,rowid){
		var rowdata=table.getRowData(rowid);
		delete rowdata.id;
		var data="";
		for (var p in rowdata){
			data+=$.trim(rowdata[p]);					//将行数据存放成一个字符串
		}
		return [rowdata,data];
	}
	
	function resetGrid($grid){
		var colModel=$grid.jqGrid("getGridParam","colModel"),
			colname=[],
			blankrows=!$grid[0].p.blankrows?5:$grid[0].p.blankrows;
		$.each(colModel,function(index){
			if (this.name!="rn")	colname.push(this.name);
		});
		var rowdata={},footer={rn:'合计'};
		$.each(colname,function(){
			rowdata[this]='';
			footer[this]='';
		});
		$grid.find("tr[id]").each(function(){
			$grid.delRowData(this.id);								//删除现有行
		});
		for (var i=1;i<=blankrows;i++){
			$grid.addRowData(i,rowdata,"last");						//添加5个空行
		}
		$grid.footerData("set",footer);
		$grid.resetSelection();
	}//重置Grid数据为空
	
	function inputCalculate(totalAmount){
		if (!totalAmount)	totalAmount=0;
		var $tax=$("#valueAddTax");						//税费输入框
		$("#subTotal").val(totalAmount);
		var taxrate=$("#invoice_type").val();		//税率
		!taxrate?$tax.val(0):$tax.val((taxrate*totalAmount).toFixed(2));
	}//自动计算各种数据
	
	function calculateTotalrow($grid,$row){				//第一个参数计算的表格，第二个参数是否编辑时计算
		if (!$grid)	$grid=$("#jxctable");
		var colModel=$grid[0].p.colModel,
			totalArray={},totalVal={},nameArray=[],totalObj={};
		$.each(colModel,function(i,n){
			if (n.total){
				var $input=$(':text[name='+n.name+']',$row);
				nameArray.push(n.name);
				totalArray[n.name]=$grid.getCol(n.name,false);
				if (!$row){
					totalVal[n.name]=0;
				}else{
					totalVal[n.name]=!$input[0]?Number($('td[aria-describedby$='+n.name+']',$row).text()):Number($input.val());
				}
			}
		});
		$.each(nameArray,function(i,n){
			for (var j=0;j<totalArray[n].length;j++){			//计算合计数量和总金额
				if (!isNaN(Number(totalArray[n][j])))
					totalVal[n]+=Number(totalArray[n][j]);
			}
			totalObj[n]=totalVal[n].toFixed(2);
		});
		$grid.footerData("set",totalObj);
		return !totalArray['amount']?'':totalArray['amount'];
	}//计算合计行的值
	
	//数字类文本框只能输入数字
	function CheckInputIntFloat(oInput){
	    if('' != oInput.value.replace(/\d{1,}\.{0,1}\d{0,}/,'')){
	        oInput.value=oInput.value.match(/\d{1,}\.{0,1}\d{0,}/) == null ? '' :oInput.value.match(/\d{1,}\.{0,1}\d{0,}/);
	    }
	}
	function CheckInputInt(oInput){
	    if('' != oInput.value.replace(/\d{1,}\.{0,1}\d{0,}/,'')){
	        oInput.value=oInput.value.match(/\d{1,}\.{0,1}\d{0,}/) == null ? '' :oInput.value.match(/\d{1,}/);
	    }
	}
	Number.prototype.toFixed=function( fractionDigits ){					//修改Number的原型,参数为保留的小数位数
		fractionDigits=parseInt(fractionDigits);
		var digit=this.valueOf();
		var minus=digit<0;													//判断数字是否为负数
		if (minus)	digit=Math.abs(digit);									//如果是负数取绝对值
		if (isNaN(fractionDigits))	return minus?-Math.round(digit):Math.round(digit);
		var result=(parseInt(digit * Math.pow( 10, fractionDigits  ) + 0.5)/Math.pow(10,fractionDigits)).toString();
		var dotpos=result.indexOf(".");
		if (dotpos<0){
			result+=".";
			for (var i=0;i<fractionDigits;i++){
				result+="0";												//小数位不足添0
			}
		}else{
			var deciDig=result.substring(dotpos+1).length;
			if (deciDig<fractionDigits){
				for (var i=0;i<fractionDigits-deciDig;i++){
					result+="0";
				}
			}
		}
		return minus?-result:result;
	}
	
	//键盘按下获取键值，主要为按下ESC键退出当前弹出的窗口
	function init() {
		document.onkeydown=showKeyDown
	}
	function showKeyDown(evt) {
		evt=(evt) ? evt : window.event
		if (evt.keyCode == 27){
			$(".popup_window:visible").find(".close_window").trigger("click");
//			$(".ui-jqdialog:visible .ui-jqdialog-titlebar-close").trigger("click");
//			$("#thbutton, .hidecol, #hidecolName").hide();
			$(".smallParts").hide();
		}
	}

$(function(){
	init();
//	$("body").prepend("<div class='cover'></div>");				//给遮盖层加一个iframe，解决IE6下拉框不隐藏的问题
	var tld=siteUrl();											//获取网站绝对地址
	var dom=window.parent.document;								//设置颜色方案
	function bgColor(uicolor){
		var $css=$(dom).find("#bgcolor");
		var cssLink=$css.attr("href");
		var cssHerf=cssLink.substring(cssLink.indexOf("css"));
		if (!uicolor)	uicolor=cssHerf.substring(cssHerf.indexOf("_")+1,cssHerf.indexOf("."));
		$(".iframecolor").attr("href",tld+cssHerf);
		$("#uicolor").attr("href",tld +'css/themes/ui-'+ uicolor +'/jquery-ui-1.8.23.custom.css')
	}
	bgColor();
	var bgTimeout;
	$(dom).find(".bgcolor").click(function(){
		clearTimeout(bgTimeout);
		var uicolor=$(this).attr("title");
		bgTimeout=setTimeout(function(){
			bgColor(uicolor);			
		},100);
	});	
	//给按钮添加样式
	$(".save").button({icons:{primary:"ui-icon-disk"}});
	$(".edit").button({icons:{primary:"ui-icon-pencil"}});
	$(".print").button({icons:{primary:"ui-icon-print"}});
	$(".discard, .cancel").button({icons:{primary:"ui-icon-close"}});
	$(".accept").button({icons:{primary:"ui-icon-check"}});
	$(".searchButton").button({icons:{primary:"ui-icon-search"}});

	$('.date').livequery("dateon",function(){
		$(this).datepicker({
/*				buttonImage:tld+'images/calendar.gif',
				showOn: 'both', //输入框和图片按钮都可以使用日历控件。
				buttonImageOnly: true,*/
			changeMonth:true,
			changeYear:true,
			showButtonPanel: true,
			yearRange:'-20:+20'
		});
	});//日期选择框加一个脚本
	$(".date").livequery("click",function(event){
		var $obj=$(this);
		event.preventDefault();
		if (!$obj.hasClass('hasDatepicker')){
			$obj.trigger("dateon");
			$obj.trigger("focus");
		}
	});
	
	//金额输入框和数量输入框只能输入数字
	$(".currency").keyup(function(){
		return CheckInputIntFloat(this);
	}).change(function(){
		$(this).val(Number($(this).val()).toFixed(2));
		//if ($(this).val() == "0.00")    $(this).val("");
	});
	$(".number").keyup(function(){
		return CheckInputIntFloat(this);
	});
	$("#fundAccountId,#vfundAccountId").change(function(){			//帐户改变时插件的值也要改变
		$(this).dropkick('val');
	});
	
	//隐藏/显示列按钮的事件
	var $thbutton=$("#thbutton"),
		$hidecol=$(".hidecol"),
		$ul=$("#hidecolName"),
		lastGridID="";
	if (!$thbutton[0]){
		$thbutton=$("<div id='thbutton' class='smallParts' title='过滤列'><button>过滤列</button></div>");
		$hidecol=$("<div class='hidecol ui-jqdialog smallParts'><button>过滤列</button></div>");
		$ul=$("<div id='hidecolName' class='ui-jqdialog smallParts'><ul></ul></div>");
		$thbutton.add($hidecol).add($ul).prependTo("body");
	}
	$thbutton.find("button").button({icons:{primary:'ui-icon-triangle-1-s'},text:false});
	$hidecol.find("button").button({icons:{secondary:"ui-icon-triangle-1-e"}});
	var $hideAssembly=$("#thbutton,.hidecol,#hidecolName");
	//过滤列按钮点击时，显示过滤列DIV，鼠标移上去后，显示表格中所有的列，点击前面的复选框来隐藏显示列
	var buttonTimeout;
	$thbutton.click(function(event){
		if ($(this).is(":hidden"))	return false;
		var offset=$thbutton.offset(),						//获取按钮在窗口中的偏移
			left=offset.left,
			top=offset.top,
			buttonHeight=$thbutton.height(),				//获取按钮的高
			hidecolWidth=$hidecol.outerWidth(true),
			parentGrid=$thbutton.parents(".ui-jqgrid").find(".ui-jqgrid-btable");
		var windowWidth=$(window).width(),
	    	windowHeight=$(window).height();
		
		if (lastGridID!=parentGrid.attr("id")){
			//获取表格的列名称和colModel参数,去掉第一列复选框
			var colNames=parentGrid.getGridParam("colNames").slice(1),
				colModel=parentGrid.getGridParam("colModel").slice(1),
				showNames=[],
				showcolModel=[];
			$.each(colNames,function(i,n){
				if (parentGrid.attr("id").indexOf("jxc")>=0){
					if (n!=""&&colModel[i].gridview&&!colModel[i].editable){		//jxc表格的过滤必须是可见的且不能编辑的
						showNames.push(n);
						showcolModel.push(colModel[i]);
					}
				}else{
					if (colModel[i].gridview){		//主表能过滤的列必须是可编辑且可见的
						showNames.push(n);
						showcolModel.push(colModel[i]);
					}
				}
			});
			//生成所有列的名字添加到隐藏列的列表中
			$ul.find("ul").empty();
			for (var i=0;i<showNames.length;i++){
				var nameLi=$("<li style='white-space:nowrap;'><input type='checkbox' id='col_"+showcolModel[i].name+"' name='colname' />"
							+ showNames[i] +"</li>");
				nameLi.find("input").attr("checked",!showcolModel[i].hidden).end().appendTo($ul.find("ul"));
			}
		}
		
		if (left+hidecolWidth > windowWidth){				//最右边一个标题行单元格时，选择列按钮的左偏移是窗口宽度减去按钮宽度
			left=left - hidecolWidth+$(this).outerWidth(true);
		}
		$hidecol.addClass("ui-jqdialog").show().css({"left":left,"top":top+buttonHeight});	//设置选择过滤列按钮的左偏移和上偏移

		var timeoutId;
		$hidecol.click(function(event){
			var ulLeft=left+$hidecol.find("button").outerWidth(true);		//选择框的左边距是按钮的左偏移加上按钮的宽度
			var ulTop=top+buttonHeight;
			if (ulLeft+$ul.outerWidth(true) > windowWidth){					//选择框在最右边的时候
				ulLeft=ulLeft-$ul.outerWidth();								//左边距是窗口宽度减少选择框的宽度
				ulTop=top + buttonHeight + $(this).outerHeight(true)-1	;	//上边距是按钮上偏移加按钮的高度
			}
			timeoutId=setTimeout(function(){
				$ul.addClass("ui-helper-reset ui-widget-content").css({"left":ulLeft,"top":ulTop}).show();
			},300);
			event.stopPropagation();
		});
		$ul.mouseenter(function(){
//			$hideAssembly.show();
		}).	mouseleave(function(){
//			$hideAssembly.hide();
		});
		var input=$ul.find("input");				//定义复选框选中或不选中的事件
		input.unbind("change").change(function(event){
			var inputobj=$(this);
			var selectCol=this.id.substring(4);
			if (input.filter(":checked").length == 0){     							  //只有一个复选框被选中时，不执行操作
				inputobj.attr("checked",true);
			}
			if (!inputobj.is(":checked") && input.filter(":checked").length > 0){	  //复选框选中时显示，取消时隐藏
				parentGrid.hideCol(selectCol);
				parentGrid.trigger("hideShow");
				gridSize(parentGrid,event);
			}else{
				parentGrid.showCol(selectCol);
				parentGrid.trigger("hideShow");
				gridSize(parentGrid,event);
			}												
		}).click(function(event){
			event.stopPropagation();
		});
		$ul.find("li").unbind("click").click(function(event){		//禁止冒泡，以免点击复选框时引发操作
			var statu=$(this).children("input").attr("checked");
			$(this).children("input").attr("checked",!statu).trigger("change");
			event.stopPropagation();
		});
		lastGridID=parentGrid.attr("id");
		return false;
	});
});