<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-角色管理</title>
<style type="text/css">
	.selecting .selecttips {position: absolute;width: auto;z-index:101;padding:6px;margin:5px;text-align:center;font-weight: bold;border-width: 2px !important; font-size:11px;}
	.completeDiv .selecting { position:absolute;width:100%;height:100%;background-color:white; }
</style>
<script type="text/javascript">
	$(function() {
		var gridoptions={
				url:'role_listjson.action',
				colNames:['','角色名称', '显示名称', '资源','备注'],
				colModel:[
					{name:'id',editable:false,hidden:true,gridview:false},
					{name:'name',width:'50',fuzzySearch:true,editrules:{required:true}},
					{name:'dispName',width:'50',fuzzySearch:true},
					{name:'ids',edittype:'textarea',dataUrl:'actionResource_listjson.action',formatter:'resf',completeGrid:completeGridSet,editoptions:{readonly:true}},
					{name:'memos',edittype:'textarea',hidden:true}
				],
				editurl:'role_save.action',
				caption:'角色管理'
			};
		var naviPrm={
				edit:{
	    			afterShowForm:function(formid){
	    				var $grid=$(this),
	    					colModels=$grid[0].p.colModel;
						var rowData;
						$.each($.fn.selfGrid.defaults.loadData,function(i,n){		//从缓存中读取该行数据
							if (n.id==$grid.getGridParam("selrow")){
								rowData=n;
								return false;
							}
						});
						setTimeout(function(){
							var $input=$("#ids");
							$input.val("").next().val("");
							if (rowData['resources']){
								var idArray=[],nameArray=[];
								$.each(rowData['resources'],function(i,n){
									idArray.push(n.id);
									nameArray.push(n.name);
								});
								$input.val(idArray.toString()).next().val(nameArray.toString());
							}
						},80);
						pagination(formid,$(this));
						setTimeout(function(){inputMove(formid);},20);
						jqdialogPosition(formid);		//弹出框定位
					}
				},//editoptions
				del:{url:'role_delete.action'}
		};
		var formatter={
			resf:function(cellData,options,rowObject){			//资源列表返回后根据返回的数组来显示
				var dispName=[];
				if (rowObject.resources&&rowObject.resources.length>0){
					$.each(rowObject.resources,function(i,n){
						dispName.push(n.name);
					});
				}
				return dispName.length>0?dispName.toString():'';
			}	
		};
		
		$('#list').selfGrid(gridoptions,naviPrm,formatter);
		var count=0;
 		$('#idsName').livequery('click',function(){				//点击资源输入框时的事件
 			var ids=$('#ids').val(),
 				idsData=$(this).data('ids'),					//读取上一次的值
 				$compDiv=$('.completeDiv'),
 				$grid=$('table[id$=Grid]',$compDiv);
 			var t=new Date().getTime();
 			$($grid[0].grid.bDiv).scrollTop(0);					//滚动到顶部
 			if (ids==idsData)	return false;					//判断是否和上一次的值一样
 			$grid.resetSelection();
 			$grid.find('tr[id]').removeClass('ui-state-highlight');
			$grid.find('tr[id]').find(':checkbox').each(function(){
				$(this).attr('checked',false);
				this.indeterminate=false;
			});
 			if (!count){
 				$(this).ajaxStop(function(){
 	 				boxStatu();
 	 				console.log(new Date().getTime()-t);
 	 				return false;
 	 			});
 			}else{
 				if (!$compDiv.is(':has(.selecting)')){
 	 				$compDiv.append('<div class="selecting"><div class="selecttips ui-state-active">请稍候...</div></div>');
 	 				var $select=$('.selecting')
 	 				$select.css('opacity','0.8');
 	 				var left=($select.width()-$select.children().outerWidth(true))/2,
 	 					top=($select.height()-$select.children().outerHeight(true))/2;
 	 				$('.selecttips',$select).css({'left':left,'top':top});
 	 			}else{
 	 				$('.selecting',$compDiv).show();
 	 			}
 				setTimeout(function(){
 	 				boxStatu();
 	 				$('.selecting',$compDiv).hide();
 	 				console.log(new Date().getTime()-t);
 				},100);
 			}
 			count++;
 			$(this).data('ids',ids);
		});
	});
	function completeGridSet(){
		return [{
				treeGrid: true,		            
	            rowNum:10000,
	            onSelectRow:function(rowid,statu){
	            	var $grid=$(this),
	            		$tr=$('#'+rowid,this),
	            		$checkbox=$tr.find(':checkbox'),
	            		checkStatu=$checkbox.is(':checked');
	            	$checkbox.attr('checked',!checkStatu);
	            	var childs=[];
	            	function getChildren($grid,node,array){							//用来遍历子节点
	            		array.push(node._id_);
	            		var childNode=$grid.getNodeChildren(node);
	            		if (childNode.length>0){
		            		for (var i=0;i<childNode.length;i++){
		            			arguments.callee($grid,childNode[i],array);			//内部调用
		            		}
	            		}
	            		return array;												//返回子节点
	            	}
	            	var rc=new Rc($tr);
					getChildren($grid,rc,childs);
 					$.each(childs,function(i,n){								//遍历子节点，确定checkbox状态
 						var $box=$('#'+n,$grid).find(':checkbox');
						if ($box.is(':checked')!=!checkStatu)	$box.attr("checked",!checkStatu);
					});
 					pboxStatu(rc,$grid);
	            	$("tr[id]",this).each(function(){
	            		var $trobj=$(this),$curBox=$trobj.find(":checkbox");
	            		$trobj[$curBox.is(':checked')&&!$curBox[0].indeterminate?'addClass':'removeClass']('ui-state-highlight');
	            	});
	            },
	            gridComplete:function(){
	            	var $grid=$(this);
					if($grid.getGridParam('treeGrid')){
						$grid.parents('.completeDiv').find('.ui-jqgrid-hdiv').hide();
					}
					var gridID=$grid.attr('id');
					$input=$('#'+gridID.substring(0,gridID.indexOf('Grid')));
	            	var $tr=$('tr[id]',$grid);
  	            	$('td[aria-describedby$=name]',$tr).each(function(){			//每行增加一个复选框
	            		$(this).prepend("<input type='checkbox' name='multiselect' class='multiSelect' style='margin-top:2px;float:left' />");
	            	});
	            	$grid.navGrid("#completePager",{add:false,edit:false,del:false,search:false,refresh:false,position:"right"})
						.navButtonAdd("#completePager",{
							caption:"确定",buttonicon:"ui-icon-check",position:"last",
							onClickButton:function(){
								var arrrow=[],arrname=[];
								$("tr[id]",$grid).each(function(){
									var $tr=$(this),
										rc=new Rc($tr,$grid);
									if ($tr.find(":checkbox").is(":checked")){
										arrrow.push(rc._id_);
										arrname.push($tr.children("[aria-describedby$=name]").text());
									}
								});
								$input.val(arrname.toString()).prev().val(arrrow.toString());
								$grid.parents(".completeDiv").hide();
							}
					}).navButtonAdd("#completePager",{
						caption:"关闭",buttonicon:"ui-icon-close",position:"last",
						onClickButton:function(){
							$grid.parents(".completeDiv").hide();
						}
					});
	            	$('.multiSelect').change(function(e){
	            		var $checkbox=$(this).attr("checked",!$(this).attr("checked"));		//checkbox改变后先反选，交给selectRow处理
	            		var rowid=$checkbox.parents('tr').attr('id');
            			$grid.setSelection(rowid,true);
            		});
	            }
	        }];
	}
	function getSib($grid,parent){
		return $("tr[id]",$grid).filter(function(){
			var curtr=new Rc($(this),$grid);
			return curtr.lft>parent.lft&&curtr.rgt<parent.rgt&&curtr.level==parent.level+1;
		});
	}//用来获取同级节点
	function pboxStatu(rc,$grid){
		var ancestors=$grid.getNodeAncestors(rc),
			parent=$grid.getNodeParent(rc),
			children=$grid.getNodeChildren(rc);
		if (parent){
			var $pbox=$("#"+parent._id_+" :checkbox",$grid);			//上级菜单的复选框
//			if ($pbox.attr('checked'))	return false;
			var $sibTr=getSib($grid,parent);
			var $sibCheckbox=$sibTr.find(":checkbox");
			var flag=true,nocheckNum=0;							//flag决断是否全选中,nocheckNum判断没有选中行的数量
			$.each($sibCheckbox,function(i,n){					//判断是否全选中
				if (!n.checked)	flag=false,nocheckNum++;
			});
			if (flag){
				$pbox[0].indeterminate=false;					//全选时选中上级菜单复选框
				$pbox.attr('checked',flag);
				arguments.callee(parent,$grid);					//内部调用
			}else{
				$.each(ancestors,function(i,n){
					$("#"+n._id_+" :checkbox",$grid).attr('checked',flag);
				});
				if (nocheckNum==$sibCheckbox.length){			//全没选中时取消上级菜单复选框的第三种状态
					$pbox[0].indeterminate=false;
					arguments.callee(parent,$grid);			//内部调用
				}else{
					$.each(ancestors,function(i,n){
						$("#"+n._id_+" :checkbox",$grid)[0].indeterminate=true;
					});
				}
			}
		}
	}//确定父级选择框的状态
	function boxStatu(){
		if ($('.completeDiv').is(':visible')){
			var $grid=$('.completeDiv table[id$=Grid]');			
			if($('#ids').val()!=''){							//资源不为空时，根据资源ID来选择行
				var ids=$('#ids').val().split(',');
				for (var i=0;i<ids.length;i++){
					var rc=new Rc($('#'+ids[i],$grid),$grid),
						parent=$grid.getNodeParent(rc);
					if (!$('#'+ids[i],$grid).find(':checkbox').attr('checked')){
						$grid.setSelection(ids[i],true);
					}
					if (!parent)	return false;				//根节点已选时退出循环
				}
			}
		}
	}
</script>
</head>

<body>
	<div class="data_page">
		<div class="treedata_div">
			<div class="gridTableDiv">
				<div id="pager"></div>
				<div id="search"></div>
				<table id="list" actionName="${queryappmodel}"></table>
			</div>
		</div>
	</div>
</body>
</html>