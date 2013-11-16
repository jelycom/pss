<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>${SystemSetting.systemName}-${SystemSetting.systemVersion }</title>
	<%@include file="/common/css_scripts.jsp"%>
	<script type="text/javascript">
		$(function(){
/* 			$('#menu').treeTable({
				url:'employee_findemployeemenus.action',
				pager:false,
				height:$(this).parent().height()-2
			}); */
   			$.ajax({
				url:'employee_findemployeemenus.action',
				type:'post',
				dataType:'json',
				error:function(){
					alert('网络错误，请联系管理员！');
				},
				success:function(data){
					if (data&&!data.operate){
						var href=window.location.href;
						window.location.replace(href.substring(0,href.lastIndexOf('/')));		//超时后返回登陆界面
						return false;
					}
					if (data&&data.operate){
						data=data.resultObj.children;
						var menu='';
						$.each(data,function(i,n){
							menu+='<h3><a href="#">'+n.name+'</a></h3><div><ul>';
							$.each(n.children,function(j,k){
								menu+='<li><a href="'+k.url+'">'+k.name+'</a></li>';
							});
							menu+='</ul></div>';
						});
						$('.menutree').append(menu);
						$(".menutree li").addClass("actionLink");
						$(".menutree").accordion({fillSpace:true,collapsible:true,active:false});	//使用UI的Accordion插件
					}
				}
			});
			$('#changepwd').click(function(e){
				e.preventDefault();
				var $changePass=$('#changePass');
				if (!$changePass[0]){
					$changePass=$('<div id="changePass" title="修改密码"><form id="changeForm" method="POST" action="user_changepwd.action"><table style="width:100%;">'+
								  '<tr class="error"><td colspan="2"></td></tr>'+
								  '<tr id="1"><td style="width:25%;">旧密码</td><td><input type="password" name="oldPassword" title="旧密码" /></td></tr>'+
								  '<tr id="2"><td>请输入新密码</td><td><input type="password" name="newPassword" title="新密码" /></td></tr>'+
								  '<tr id="3"><td>确认新密码</td><td><input type="password" name="confirmPassword" /></td></tr>'+
								  '</table></form></div>');
					$changePass.appendTo('body');
					$changePass.dialog({
						autoOpen:false,modal:true,width:400,resizable:false,minHeight:false,
						buttons:{
							"确定":function(){
								var	$form=$('#changeForm'),
									$input=$('input',$form),
									$tips=$('.tips',$form),
									$error=$('.error td',$form);
								if ($tips.length)	return false;
								var options={
										beforeSerialize:function($form,options){
											var frag=true;
											$.each($input,function(){
												if (!$(this).val()){
													$error.addClass('ui-state-error').text('表单未填完整！');
													frag=false;
													return false;
												}
											});
											if ($input.eq(1).val()!=$input.eq(2).val()){
												$error.addClass('ui-state-error').text('两次输入的密码不一致！');
												frag=false;
											}
											if (!frag)	return false;
										},
										success:function(resp,status,xhr,$form){
											$error.addClass('ui-state-error').text(resp.message);
											if (resp.operate){
												setTimeout(function(){
								 					$changePass.dialog("close");
												},1000);
							 				}
										}
									};
								$form.unbind('submit').submit(function(){
									$form.ajaxSubmit(options);
									return false;
								});
								$form.submit();
							},
							"取消":function(){
								$(this).dialog('close');
							}
						},
						create:function(){
							var $form=$('#changeForm');
							$form.find('input').addClass('ui-widget-content ui-corner-all').css('padding','0.3em')
								 .end().find('td').css('padding','0.2em');
						},
						open:function(){
							var $form=$('#changeForm'),
								$input=$form.find('input'),
								$error=$('.error td',$form);
							$form[0].reset();
							$('.tips').remove();
							$input.bind('focus blur keyup',function(){
								var $obj=$(this),
									title=$obj.attr('title'),
									val=$obj.val(),
									$last=$input.filter(':last');
								$obj.next().remove();
								$error.text('').removeClass('ui-state-error');
								if (!val){
									if ($obj.is($last))	$obj.after('<span class="tips">请再输入一次新密码</span>');
									else	$obj.after('<span class="tips">请输入'+title+'</span>');
								}else{
									if ($obj.is($input.eq(1))){
										if (val==$last.val())	$last.next().remove();
									}
									if ($obj.is($last)){		//判断两次新密码是否一致
										if (val!=$input.eq(1).val()){
											$obj.next().remove();
											$obj.after('<span class="tips">两次输入的密码不一致</span>');
										}
									}
								}
							});
						}
					});
				}
				$changePass.dialog('open');
			});
		});
	</script>
</head>

<body scroll="no">
	<!-- 网页头部 -->
	<div class="ui-layout-north">
		<div class="top">
	    	<div class="topimage">
	        	<table cellpadding="0" cellspacing="0">
	            	<tr>
	                	<td><img src="images/Title.gif" /></td>
	                </tr>
	            </table>
	        </div>
	        <div class="topmenu">
	        	<table cellpadding="0" cellspacing="0">
	            	<tr>
	                	<td style="width:170px;height:35px;"></td>
	                    <td>
	                    	<ul>
	                        	<li id="tbarPinWest"><span></span>关闭菜单</li>
	                            <li>关闭所有标签</li>
	                        </ul>
	                    </td>
	                    <td style="text-align:right;">
	                    	欢迎您，<s:property value="#session.loginUser.name"/>
	                    	<a id="changepwd" href="#" style="margin:0 8px;">修改密码</a>
	                    	<a href="logout.action" style="margin:0 8px;">退出登录</a>
	                    </td>
	                    <td width="10%">
	                    	<ul>
	                        	<li class="bgcolor" title="blue" style="background:#73A3D4;"></li>
	                            <li class="bgcolor" title="brown" style="background:#CC9966;"></li>
	                            <li class="bgcolor" title="gray" style="background:#666666;"></li>
	                        </ul>
	                    </td>
	                </tr>
	            </table>
	        </div>
	    </div>
	</div>
    <!-- 网页头部结束 -->
 
     <!-- 菜单开始 -->
     <div class="ui-layout-west leftMenu">
    	<div class="menuBlank">
    		<div class="menutree">
        	</div>
		</div>
	</div>
    <!-- 菜单结束 -->
    
    <!-- 操作界面开始 -->
    <div class="ui-layout-center">
	    <div id="tabs" class="lables">
	    	<div class="scroller_control">
	        	<div class="leftIcon"></div>
	            <div class="rgtIcon"></div>
	        </div>
	    	<ul style="width:5000px">
	        	<li><a href="#tabs-1">常用功能</a></li>
	        </ul>
	        <div class="ul_control"></div>
			<div id="tabs-1" class="default" style="padding:30px 0 0 15px" title="常用功能">
	              <!-- <ul>
	                  <li><a href="product_list.action">产品管理</a></li>
	                  <li>进货单</li>
	                  <li>销售单</li>
	              </ul> -->
	              <div class="often">
	              		<button><a href="product_list.action">产品管理</a></button>
	              		<button><a href="productPurchase_list.action">进货单</a></button>
	              		<button>销售单</button>
	              </div>
	        </div>
		</div>
	</div>
    <!-- 操作界面结束 --> 
    
    <div class="ui-layout-south">South</div>  
</body>
</html>
