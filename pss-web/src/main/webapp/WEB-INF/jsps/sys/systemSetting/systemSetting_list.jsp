<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/css_scripts_common.jsp"%>
<title>捷利进销存系统-系统配置</title>
<style type="text/css">
	body{ overflow:hidden !important; }
	#settingTabs{ width:90%; margin:10px auto; }
	#settingTabs table{ width:90%; margin:.5em auto; }
	#settingTabs td{ padding:.2em .4em; font-size:1.2em; }
	.caption{ font-weight:400; text-align:right; }
	.formElement{ padding:.2em; width:98%; }
	#pageCenter{ overflow-y:scroll !important; }
</style>
<script type="text/javascript">
	$(function() {
		var $tabs=$('#settingTabs').tabs(),
			$td=$('td',$tabs),
			$form=$('#systemSetting');
		$td.filter(':even').addClass('caption');
		$td.filter(':odd').children(':text,textarea').addClass('formElement ui-widget-content ui-corner-all');
		$td.find(":input").each(function(){
			var $obj=$(this),
				attr=$obj.closest('div').attr('title'),
				name=$obj.attr('name');
			$obj.attr('name',attr+'.'+name);
		});
		var $save=$('#buttonset .save');		
		$save.click(function(){
			$form.unbind('submit').submit(function(){
				$form.ajaxSubmit({
					beforeSubmit:function(formData,$form,options){
						console.log('submit');
					},
					beforeSerialize:function($form,options){
						console.log('serialize');
					}
				});
				return false;
			});
//			$form.submit();
		});
	});
</script>
</head>
<body>
	<div class="data_page" style="height:80%;margin:auto 0;">
		<div class="treedata_div">
			<form id="systemSetting" action="<s:url value="/sys/systemSetting_save.action"/>" method="post" autocomplete="off">
				<div id="settingTabs">
					<ul>
						<li><a href="#companyInfo">公司信息</a></li>
						<li><a href="#editOption1">录帐配置一</a></li>
						<li><a href="#editOption2">录帐配置二</a></li>
					</ul>
					<div id="companyInfo" title="companyInfo">
						<table>
							<tr>
								<td style="width:30%;">公司名称</td><td><input type="text" name="companyName" /></td>
							</tr>
							<tr>
								<td>注册地址</td><td><input type="text" name="registeredAddress" /></td>
							</tr>
							<tr>
								<td>经营地址</td><td><input type="text" name="businessAddress" /></td>
							</tr>
							<tr>
								<td>机构代码</td><td><input type="text" name="organizationCode" /></td>
							</tr>
							<tr>
								<td>公司税号</td><td><input type="text" name="corporationTax" /></td>
							</tr>
							<tr>
								<td>法人代表</td><td><input type="text" name="legalRepresentative" /></td>
							</tr>
							<tr>
								<td>业务范围</td><td><input type="text" name="businessScope" /></td>
							</tr>
							<tr>
								<td>电话号码</td><td><input type="text" name="telephoneNumber" /></td>
							</tr>
							<tr>
								<td>传真号码</td><td><input type="text" name="faxNumber" /></td>
							</tr>

							<tr>
								<td>开帐日期</td><td><input type="text" name="openningDate" readonly="readonly" /></td>
							</tr>
							<tr>
								<td>备注</td><td><textarea rows="3"></textarea></td>
							</tr>
						</table>
					</div>
					<div id="editOption1" title="editOption">
						<table>
							<tr>
								<td style="width:35%;">月结算日</td><td><input type="text" name="monthCloseDay" /></td>
							</tr>
							<tr>
								<td>自动月结</td><td><input type="checkbox" name="autoMonthClose" /></td>
							</tr>
							<tr>
								<td>打印单据前必须保存数据</td><td><input type="checkbox" name="saveBeforePrint" /></td>
							</tr>
							<tr>
								<td>成本异常提示</td><td><input type="checkbox" name="costUnusualWarn" /></td>
							</tr>
							<tr>
								<td>成本异常幅度</td><td><input type="text" name="unusualPercent" class="number" /></td>
							</tr>
							<tr>
								<td>进价异常提示</td><td><input type="checkbox" name="purchasePriceUnusualwarn" /></td>
							</tr>
							<tr>
								<td>连续录单保存经手人仓库</td><td><input type="checkbox" name="rememberwar_emp" /></td>
							</tr>
							<tr>
								<td>允许修改单据日期</td><td><input type="checkbox" name="changeBillDate" /></td>
							</tr>
							<tr>
								<td>录单日期必须为当前日期</td><td><input type="checkbox" name="billDateMustToday" /></td>
							</tr>
							<tr>
								<td>单据自动编号</td><td><input type="checkbox" name="autoGenerateBillItem" /></td>
							</tr>
							<tr>
								<td>调用订单时允许修改价格</td><td><input type="checkbox" name="billChangeOrderPrice" /></td>
							</tr>							
							<tr>
								<td>录单必须录入部门</td><td><input type="checkbox" name="departmentBeNull" /></td>
							</tr>
							<tr>
								<td>录单必须录入经手人</td><td><input type="checkbox" name="employeeBeNull" /></td>
							</tr>
							<tr>
								<td>单据不允许修改经手人</td><td><input type="checkbox" name="changeEmployee" /></td>
							</tr>
							<tr>
								<td>录单时提示库存报警</td><td><input type="checkbox" name="inventoryWarn" /></td>
							</tr>
						</table>
					</div>
					<div id="editOption2" title="editOption">
						<table>
							<tr>
								<td>售价低于成本时提示</td><td><input type="checkbox" name="priceBelowCostWarn" /></td>
							</tr>
							<tr>
								<td>售价低于最近进价时提示</td><td><input type="checkbox" name="priceBelowBidWarn" /></td>
							</tr>
							<tr>
								<td>销售单价自动修改预设售价</td><td><input type="checkbox" name="autoSaveNestPrice" /></td>
							</tr>
							<tr>
								<td>销售单默认收款帐户和金额</td><td><input type="checkbox" name="defaultACC_AMT" /></td>
							</tr>
							<tr>
								<td>选择商品时提示库存量</td><td><input type="checkbox" name="dispRealStock" /></td>
							</tr>
							<tr>
								<td>选择往来单位显示应收应付</td><td><input type="checkbox" name="dispARAP" /></td>
							</tr>
							<tr>
								<td>自动生成摘要</td><td><input type="checkbox" name="autoGenSummary" /></td>
							</tr>
							<tr>
								<td>超过应收上限允许过帐</td><td><input type="checkbox" name="performWhenARExceed" /></td>
							</tr>
							<tr>
								<td>允许负库存</td><td><input type="checkbox" name="alowNegativeStock" /></td>
							</tr>
							<tr>
								<td>单价保留小数位数</td><td><input type="text" name="priceScale" class="number" style="width:10%" /></td>
							</tr>
							<tr>
								<td>合计保留小数位数</td><td><input type="text" name="amountScale" class="number" style="width:10%" /></td>
							</tr>
							<tr>
								<td style="width:30%;">默认成本计算方法</td><td>
									<select name="defaultCostMethod" id="defaultCostMethod">
										<s:iterator value="@cn.jely.cd.util.CostMethod@values()" var="costm">
											<option value="<s:property value="#costm.name()"/>"
											<s:if test="#costm==defaultServiceCostMethod">selected="selected"</s:if> >
												<s:property value="#costm.methodName" />
											</option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr>
								<td>统一成本计算方法</td><td><input type="checkbox" name="uniteCostMethod" /></td>
							</tr>
<!--  						<tr>
								<td>产品成本计算方法</td><td>
									<select name="defaultProductionCostMethod" id="defaultProductionCostMethod">
										<s:iterator value="@cn.jely.cd.util.CostMethod@values()" var="costm">
											<option value="<s:property value="#costm.name()"/>"
											<s:if test="#costm==defaultServiceCostMethod">selected="selected"</s:if> >
												<s:property value="#costm.methodName" />
											</option>
										</s:iterator>
									</select>
								</td>
							</tr>
							<tr>
								<td>服务成本计算方法</td><td>
									<select name="defaultServiceCostMethod" id="defaultServiceCostMethod">
											<s:iterator value="@cn.jely.cd.util.CostMethod@values()" var="costm">
												<option value="<s:property value="#costm.name()"/>"
												<s:if test="#costm==defaultServiceCostMethod">selected="selected"</s:if> >
													<s:property value="#costm.methodName" />
												</option>
											</s:iterator>
									</select>
								</td>
							</tr>-->
						</table>
					</div>
				</div>
				<div id="buttonset" style="text-align:center;">
					<button class="save">确定</button>
					<button class="cancel">重置</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>