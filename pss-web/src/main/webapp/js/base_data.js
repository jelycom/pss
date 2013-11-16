// JavaScript Document
$(function(){	
	//过滤列显示时在其他方点击触发ESC键按下事件——隐藏过滤列的按钮
	var e=jQuery.Event("keydown");
	e.keyCode=27;
	$(document).click(function(){
		if ($(".smallParts").is(":visible"))	$(".smallParts").hide();
	});
	
	//设置各个DIV的高度和宽度
	var $treeDiv=$(".tree_div"),
		$treeTitle=$(".tree_title"),
		$treemenu=$(".tree_div .gridTableDiv"),
		$treedataDiv=$(".treedata_div"),
		$gridTableDiv=$(".treedata_div .gridTableDiv"),
		tab_li=$(".ui-tabs-nav li"),								//添加编辑form时有分页显示时
		IEbrowser=$.browser.msie,									//浏览器类型
		browserVer=parseInt($.browser.version);						//浏览器版本
	$treeDiv.attr("id","pageWest");
	$treedataDiv.attr("id","pageCenter");
	if ($gridTableDiv.length)	$gridTableDiv.wrapAll("<div id='gridFather' style='height:100%;'></div>");
	
	function size(){
		var windowSize=[];
		var treeName=$("#treeName");
		windowSize[0]=$(window).height(),
		windowSize[1]=$("body")[0].clientWidth;
		$("body").height(windowSize[0]);
//			$treeDiv.add($treedataDiv).height(windowSize[0]);
		menuHeight=IEbrowser==true?$treeDiv.height() - $treeTitle.outerHeight(true)-1:$treeDiv.height() - $treeTitle.outerHeight(true);
		$treemenu.height(menuHeight);			//分类树的高度为整体高度减去标题高度和自己的边框
		var newWidth=windowSize[1] - $treeDiv.width()-1;
		$treedataDiv.width(newWidth);
		$("#gridFather").width(newWidth);
		$("#nodeInfo").height(treeName.height()-$("#attrTitle").outerHeight(true)-2);
//			$treedataDiv.height(windowSize[0]).width($treedataDiv.parent().width());	//jqgrid的宽度和高度
		$gridTableDiv.length>1?$gridTableDiv.height(($gridTableDiv.parent().height()-1)/2):$gridTableDiv.height($gridTableDiv.parent().height());
//			$gridTableDiv.length>1?$gridTableDiv.width(newWidth*0.5):windowSize[2]=$treedataDiv.height();		//根据个数决定宽度
		windowSize[3]=$treedataDiv.width();
		return windowSize;
	}
	tab_li.live("click",function(){
		$(this).find(":first-child").trigger("click");
	});
	
	if ($treeDiv.length>0){
		//生成分类树
		var title=$treeDiv.attr("rel");
		if (title){				//分类树有rel属性产生一个当前选中节点的信息的一个DIV
			var field=$treeDiv.attr("field").split(",");
			$treedataDiv.prepend("<div id='treeName' class='selectedInfo'>"
								+"<div id='attrTitle' class='ui-widget-header''>当前所选"+title+"信息</div>"
								+"<div id='nodeInfo' class='ui-widget-content'><table class='selectedFiled'></table></div></div>");
			var selectedFieldHeight=$(".selectedInfo").height()-$("#attrTitle").outerHeight();
			var row=Math.ceil(field.length/3);
			var j=0;
			for (var i=0;i<row;i++){			//循环产生表格单元格和行
				$(".selectedFiled").append("<tr id='row"+i+"'></tr>");
				for (var k=0;k<3;k++){
					if (field[j+k]){
						$("#row"+i).append("<td style='text-align:center;width:15%;font-weight:bold;'>"+ field[j+k]+"</td><td style='width:18%;'></td>");
					}
				}
				j+=3;
			}
		}
	}
	//页面分区块布局
	var layoutSettings_Outer={
		name: "outerLayout" // NO FUNCTIONAL USE, but could be used by custom code to 'identify' a layout
		// options.defaults apply to ALL PANES - but overridden by pane-specific settings
	,	defaults: {
			size:					"auto"
//		,	minSize:				50
		,	togglerClass:			"toggler"	// default='ui-layout-toggler'
		,	buttonClass:			"button"	// default='ui-layout-button'
		,	contentSelector:		".content"	// inner div to auto-size so only it scrolls, not the entire pane!
		,	contentIgnoreSelector:	"span"		// 'paneSelector' for content to 'ignore' when measuring room for content
		,	togglerLength_open:		35			// WIDTH of toggler on north/south edges - HEIGHT on east/west edges
		,	togglerLength_closed:	35			// "100%" OR -1=full height
		,	hideTogglerOnSlide:		true		// hide the toggler when pane is 'slid open'
		,	togglerTip_open:		"Close This Pane"
		,	togglerTip_closed:		"Open This Pane"
		,	resizerTip:				"Resize This Pane"
		//	effect defaults - overridden on some panes
		,	fxName:					"slide"		// none, slide, drop, scale
		,	fxSpeed_open:			750
		,	fxSpeed_close:			1500
		,	fxSettings_open:		{ easing: "easeInQuint" }
		,	fxSettings_close:		{ easing: "easeOutQuint" }
		,	onresize_end:			function(paneName,target,targetSize,layoutSet,layoutName){
										size();
										$gridTableDiv.add($treemenu).find(".ui-jqgrid-btable").each(function(){
											gridSize($("#"+this.id));
										});
									}
		 // using custom 'ID' paneSelectors
	}
	,	west: {
			size:					200			// 初始宽度
		,	minSize:				160			// 最小宽度
		,	maxSize:				260
		,	spacing_open:			1			// cosmetic spacing
		,	spacing_closed:			0			// wider space when closed
		,	togglerLength_closed:	21			// make toggler 'square' - 21x21
		,	togglerAlign_closed:	"top"		// align to top of resizer
		,	togglerLength_open:		0 			// NONE - using custom togglers INSIDE east-pane
		,	togglerTip_open:		"Close East Pane"
		,	togglerTip_closed:		"Open East Pane"
		,	resizerTip_open:		"Resize East Pane"
		,	slideTrigger_open:		"mouseover"
		//	override default effect, speed, and settings
		,	fxName:					"blind"
		,	fxSpeed:				"normal"
		,	fxSettings:				{ easing: "" } // nullify default easing
		,	paneSelector:   		"#pageWest"
		}
	,	center: {
			paneSelector:			"#pageCenter" 			// sample: use an ID to select pane instead of a class
//			minWidth:				700
//		,	minHeight:				500
		}
	};
	$("body").layout(layoutSettings_Outer);
	$("body").resize(function(){
		
	});

	//主体部分分区块布局
	var layoutSettings_Inner={
		  	name:						'innerLayout',
			defaults:{
					initChildren:				false
				,	destroyChildren:			false
				,	stateManagement__enabled:	true // maintain state between destroy/create
				,	includeChildren:			false
				,	spacing_open:				5
				,	togglerClass:			"toggler"	// default='ui-layout-toggler'
				,	togglerTip_open:		"Close This Pane"
				,	togglerTip_closed:		"Open This Pane"
				,	resizerTip:				"Resize This Pane"
				,	onresize_end:			function(paneName,target,targetSize,layoutSet,layoutName){
												size();
												$gridTableDiv.add($treemenu).find(".ui-jqgrid-btable").each(function(){
													gridSize($("#"+this.id));
												});
											}
//					,	north__paneSelector:  "#treeName"
//					,	center_paneSelector: ".gridTableDiv"
			},
			north:{
				paneSelector:  "#treeName",
				size:90,
				maxSize:120,
				minSize:80
			},
			center:{
				paneSelector: "#gridFather"
			}
	};
	if ($treedataDiv.is(":has(.gridTableDiv)"))	$(".treedata_div").layout(layoutSettings_Inner);
	size();
	
	//过滤分类树
	$("#searchButton",$treeDiv).live("click",function(){
		var treetable=$("table[id*=Tree]",$treemenu);
		var input=$("#searchNode",$treeDiv);
		searchTreegrid(input,treetable);
	});
	$("#searchNode",$treeDiv).live("keyup",function(e){
		if (e.which==13) $("#searchButton",$treeDiv).trigger("click"); 
	});

	//窗口变化时重新计算高度和宽度
	var t=0;
	$(window).resize(function(event){
		var now=new Date();
		now=now.getTime();
		if (now-t>600){
			t=now;
			setTimeout(function(){
				size();
				$gridTableDiv.add($treemenu).find(".ui-jqgrid-btable").each(function(){
					gridSize($("#"+this.id),event);
				});
			},100);
		}
	});
});