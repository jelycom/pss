$(function(){
	var IE=$.browser.msie,						//浏览器类型
		IEVer=parseInt($.browser.version);					//浏览器版本
		
		//使用layout插件
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
	}
	,	north: {
			spacing_open:			-1			// 显示时的间距
		,	maxSize:				65
		,	spacing_closed:			6			// 隐藏时的间距
		,	togglerLength_open:		0			// 是否显显按钮
		,	togglerLength_closed:	-1			// "100%" OR -1=full width of pane
		,	resizable: 				false		//是否可以改变尺寸
		,	slidable:				false
		//	override default effect
		,	fxName:					"none"
		}
	,	south: {
			size:					30
		,	maxSize:				30
		,	spacing_open:			4			// cosmetic spacing
		,	spacing_closed:			6			// HIDE resizer & toggler when 'closed'
		,	slidable:				false		// REFERENCE - cannot slide if spacing_closed=0
		,	resizable:				false
		,	onclose_end:			function(){
										calculateSize();
									}
		,	onopen_end:				function(){
										calculateSize();
									}
		}
	,	west: {
			size:					172			// 初始宽度
		,	minSize:				140			// 最小宽度
		,	maxSize:				200
		,	spacing_open:			2			// cosmetic spacing
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
		,	onresize_end:			function(paneName,target,targetSize,layoutSet,layoutName){
										calculateSize();
									}
		,	onclose_end:			function(){
										calculateSize();
									}
		,	onopen_end:				function(){
										calculateSize();
									}
		}
	,	center: {
			onresize_end:			function(){
										$(window).trigger("resize");
									}
//			paneSelector:			"#mainContent" 			// sample: use an ID to select pane instead of a class
//			minWidth:				700
//		,	minHeight:				500
		}
	};
	var outerLayout=$('body').layout(layoutSettings_Outer);
	outerLayout.addPinBtn( "#tbarPinWest", "west" );				//添加控制菜单按钮
	
	//计算各个区块的大小函数
	function calculateSize(){
		var timeoutID=setTimeout(function(){
			var $content=$(".menutree .ui-accordion-content");
			var $header=$(".menutree .ui-accordion-header");
			$content.height($(".menutree").height()-$header.outerHeight(true)*$header.length-($content.outerHeight(true)-$content.height()));	//每一个菜单的高度控制
			var $lables=$(".lables");
			$lables.width($lables.parent().width()-($lables.outerWidth(true)-$lables.width()));
			$lables.height($lables.parent().height()-($lables.outerHeight(true)-$lables.height()));
			$(".ui-tabs-panel").height($lables.height() - $(".ui-tabs-nav").outerHeight(true));
			$("iframe").width($tabs.width()).height($(".ui-tabs-panel").height());
			$(".ul_control").width($("#tabs").width());
		},200);
	}
	//删除IE6下菜单有文本空节点
	function cleanWhitespace(oEelement){
		 for(var i=0;i<oEelement.childNodes.length;i++){
			  var node=oEelement.childNodes[i];
			  if(node.nodeType==3 && !/\S/.test(node.nodeValue)){node.parentNode.removeChild(node)}
		 }
	}
	var domsite=$(":parent");
	for (var i=0;i<domsite.length;i++){
		cleanWhitespace(domsite[i]);
	}
			
	var	$leftIcon=$(".leftIcon"),
		$rgtIcon=$(".rgtIcon"),
		tabNum=2;

	var $frameObj;							//定义点击左边菜单时添加的iframe内容
	var $tabs=$("#tabs").tabs({
		tabTemplate:"<li title='#{label}'><a href='#{href}'>#{label}</a>"+
					"<a class='ui-icon ui-icon-close' style='padding:0;float:right;margin:0.3em 0'>Remove Tab</a></li>",
		add: function( event, ui ) {								//添加新标签后执行的操作
			if (IE&&IEVer<8){
				$(ui.tab).parents("ul").find("li").css({"top":"0"});			//IE低版本标签宽度要加宽
			}
			$(ui.panel).append( $frameObj ).width($tabs.width())
						 .height($tabs.height()- $(".ui-tabs-nav").outerHeight(true));
			$(ui.tab).trigger("click");
			$frameObj.width($tabs.width()).height($(ui.panel).height());
		},
		select: function(event,ui){
			//IE低版本需要特殊处理
			if (ui.index==0&&IE&&IEVer<8){
				$(ui.tab).parents("ul").find("li").css("top","1px");
			}else if(IE&&IEVer<8){
				$(ui.tab).parents("ul").find("li").css("top","0");
			}
			var conData=controlData();
			if (conData[3]<0&&conData[5]*ui.index<-conData[3]){	//选中的标签在左边隐藏，把左边的边距设置为设置为当前索引值乘以标签宽度
				conData[3]=conData[5]*ui.index-18;								//18是左箭头的宽度
				$tabsNav.css("left",-conData[3]);
			}else if(conData[5]*(ui.index+1)+conData[3]>$(".ul_control").width()){//在右边隐藏时，把左边的边距设置为当前索引值乘以标签宽度减去界面宽度
				conData[3]=conData[5]*(ui.index+1)+18-$(".ul_control").width();
				$tabsNav.css("left",-conData[3]);					
			}
		}
	});
	//添加和关闭tabs
	function addTab(link,title) {
		$tabs.tabs("add","#tabs-"+tabNum,title);
		tabNum++;
	}
	$("#tabs a.ui-icon-close").live("click",function(){
		var index=$("li",$tabs ).index($(this).parent());
		$tabs.tabs("remove",index);
		controlData();
	});
	
	//网页头部
	var $topLi=$(".topmenu li");
	$topLi.addClass("li_out").hover(function(){
		$(this).toggleClass("li_over");
	});
	$topLi.eq(0).toggle(function(){					//隐藏、显示菜单
		$(this).text("显示菜单");
	},function(){
		$(this).text("关闭菜单");
	});
	$topLi.eq(1).click(function(){						//关闭所有标签按钮
		$tabsNav.find(".ui-icon-close").trigger("click");
	});
	var cookieSkin=$.cookie("cssColor");
	function switchSkin(colorname){
		var cc=$("#bgcolor").attr("href");		//当前颜色
		var nc=cc.substring(0,(cc.indexOf("_")+1))+colorname+".css";	//设置新的css文件
		$("#uicolor").attr("href","css/themes/ui-"+colorname+"/jquery-ui-1.8.23.custom.css");			//设置新的ui外观
		$("#bgcolor").attr("href",nc);
		$.cookie("cssColor",colorname,{path:'/',expires:20});
	}
	$(".bgcolor").click(function(){						//切换背景颜色
		var color=$(this).attr("title");
		switchSkin(color);
		$.ajax({
			url:'user_changeskin.action',
			type:'post',
			data:{skin:color},
			dataType:'json',
			error:function(){
				alert('网络错误，请联系管理员！');
			},
			success:function(data){
				if (data&&data.operate)	alert('主题保存成功！');
			}
		});
	});
	
	//如果tabs中的所有li总长度超过tabs的宽度，用左右箭头控制移动
	var $tabsNav=$(".ui-tabs-nav");
	function controlData(){
		var leftString=$tabsNav.css("left");
		var li_width=$tabsNav.find("li").outerWidth(true);
		var conData =[];
		conData[0]=$(".ul_control").width();									//tabs的宽度
		conData[1]=li_width * $tabsNav.find("li").length;						//li的总宽度
		conData[2]=conData[1]>conData[0];
		conData[3]=Number(leftString.substring(0,leftString.indexOf("p")));		//现在的左边定位
		conData[4]=$(".ui-tabs-nav li").length;									//现有的标签个数
		conData[5]=li_width;
		if (conData[2]){														//标签总和超过界面宽度显示控制箭头，否则隐藏
			$leftIcon.add($rgtIcon).show();
		}else{
			$tabsNav.css("left","0");
			$leftIcon.add($rgtIcon).hide();
		}
		return conData;
	}
	
	//菜单部分
	var $tabsLabel=$(".ui-tabs-nav li");
	//IE6时在每一个菜单UL的最前面添加一个空<li>标签，解决IE6下显示不正常的问题
	if (IE&&IEVer<7){
		$("h3").next().find("ul").prepend("<li></li>");
	}
//	$(".menutree li").addClass("actionLink");
//	$(".menutree").accordion({fillSpace:true,collapsible:true,active:false});	//使用UI的Accordion插件
	if (IE && IEVer<7){
		$(".ui-accordion-content").css("overflow-x","hidden");
		$(".ui-accordion-header").click(function(){
			$(this).next().css({"overflow-x":"hidden","overflow-y":"auto"});
		});
	}
	calculateSize();			//计算尺寸
	//有二级菜单的li
	$(".actionLink:has(.haveChild)").css("padding-left","6px")
									.click(function(){
										$(this).children(".tree-hit").trigger("click");
									});
	$(".tree-hit").mouseover(function(){
		var thisClass=$(this).attr("class");
		if(thisClass.indexOf("collapsed") > 0){
			$(this).addClass("tree-collapsed-hover");
		}else if(thisClass.indexOf("expanded") > 0){
			$(this).addClass("tree-expanded-hover");
		}
	}).mouseout(function(){
		$(this).removeClass("tree-collapsed-hover tree-expanded-hover");
	}).click(function(event){
		var index=$(this).index(".tree-hit");
		$(this).toggleClass("tree-collapsed").toggleClass("tree-expanded").removeClass("tree-collapsed-hover tree-expanded-hover");
		$(this).siblings("ul").toggle();
/*		$(".tree-hit:not(:eq("+ index +"))").removeClass("tree-expanded").addClass("tree-collapsed")
											.siblings("ul").hide();*/
		event.stopPropagation();
	});
	$(".actionLink").livequery('click',function(event){
		return false;
	});

	//给菜单添加鼠标hover和click事件,改变背景图,添加内嵌框架
	$(".actionLink:not(:has(.haveChild))").hover(function(){
		$(this).toggleClass('actionLink_over');
	});
	$(".actionLink:not(:has(.haveChild)),.oftenButton").livequery('click',function(){
		var lableName=$(this).text(),												//获取标签的名字
			link=$('a',this).attr("href");
		if (!link)	return false;
		var	startSite=link.lastIndexOf("/") + 1,
			lastSite=link.lastIndexOf("."),
			iframeName=link.substring(startSite, lastSite<0?link.length:lastSite),	//用链接的网页名定义框架的名字
			$existFrame=$(".ui-tabs-panel iframe[name='" + iframeName +"']");		
		//如果还没有对应框架没有添加一个框架
		if (!$existFrame[0]){
			var $newFrame=$("<iframe src='"+link+"' name='"+iframeName+"' frameborder='0' style='display:inline'></iframe>");
			$frameObj=$newFrame;
			$(this).children("a").attr("target",iframeName);						//给当前点击的链接加一个target属性，以便在指定的框架打开
			addTab(link,lableName);
			$newFrame.load(function(){
				if ($newFrame.contents().find('body').contents().length<2){
					var href=window.location.href,
						$overTime=$('<div id="overTime">登录已超时，请重新登录！</div>');
					$overTime.appendTo('body');
					$overTime.dialog({autoOpen:true,modal:true,width:300,resizable:false,
									  minHeight:false,closeOnEscape:false,title:'登录超时',
						buttons:{
							'关闭':function(){
								window.location.replace(href.substring(0,href.lastIndexOf('/')));		//超时后返回登陆界面
							}
						},
						close:function(){
							window.location.replace(href.substring(0,href.lastIndexOf('/')));		//超时后返回登陆界面
						}
					});
				}
			});
		}else{											//已有对应框架则只显示不添加，并将其他框架隐藏
			var reg="/"+lableName+"$/ig";
			var conData=controlData();
			for (var i=0;i<conData[4];i++){
				if (eval(reg).test($(".ui-tabs-nav li").eq(i).children(":first").text())){
					$tabs.tabs("select",i);
					break;
				}
			}
		}
	});
	//点击菜单链接时禁止链接的默认行为，以免二次请求
	$(".actionLink a,.oftenButton a").click(function(event){
		event.preventDefault();
	});
	//在标签上点击
	$tabsLabel.live("click",function(){
		$(this).find(":first-child").trigger("click");
	});
	$leftIcon.click(function(){												//向左移动
		var conData=controlData();
		$rgtIcon.removeClass('rgtIcon_disabled');
		if (conData[3]<-conData[5]){										//左边有隐藏时向左移动一个标签宽度（left为负数时）
			$tabsNav.css({"left":(conData[3]+conData[5])+"px"});
		}else{
			$tabsNav.css({"left":"0px"});
			$leftIcon.addClass('leftIcon_disabled');
			$rgtIcon.removeClass('rgtIcon_disabled');
		}
	}).hover(function(){
		$leftIcon.toggleClass('leftIcon_over');
	});
	$rgtIcon.click(function(){												//向右移动
		var conData=controlData();
		$leftIcon.removeClass('leftIcon_disabled');
		if (-conData[3]+conData[0]<conData[1]-conData[5]){
			$tabsNav.css("left",(conData[3]-conData[5])+"px");
		}else{
			$tabsNav.css("left",(conData[0]-conData[1]-18)+"px");
			$rgtIcon.addClass('rgtIcon_disabled');
		}
	}).hover(function(){
		$rgtIcon.toggleClass('rgtIcon_over');
	});
	
	//操作界面部分
	//设置操作界面的宽度和高度
	$(".often button").addClass("oftenButton").button();
//	$(".default li").addClass("oftenButton li_out").hover(function(){
//		$(this).toggleClass("li_over");
//	});
});