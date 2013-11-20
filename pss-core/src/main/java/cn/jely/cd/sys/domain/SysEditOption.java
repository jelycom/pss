/*
 * 捷利商业进销存管理系统
 * @(#)SysEditOption.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-9
 */
package cn.jely.cd.sys.domain;

import cn.jely.cd.util.CostMethod;

/**
 *
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-9 上午9:12:46
 */
public class SysEditOption {

	//月结算日
	private int monthCloseDay;
	//自动月结
	private boolean autoMonthClose;
	//成本异常提示
	private boolean costUnusualWarn;
	//成本异常幅度,百分比
	private int unusualPercent;
	//单价保留小数位
	private int priceScale;
	//合计保留小数位
	private int amountScale;

	//默认成本计算方法
	private CostMethod defaultCostMethod;
	//统一成本计算方法
	private boolean uniteCostMethod;
	//打印前必须保存
	private boolean saveBeforePrint;
	//单据自动编号
	private boolean autoGenerateBillItem;
	//进/出货单调订单时允许修改价格
	private boolean billChangeOrderPrice;
	//进价异常提示-和成本异常不同
	private boolean purchasePriceUnusualwarn;
	//连续录单记忆仓库及经手人
	private boolean rememberwar_emp;
	//允许修改单据日期
	private boolean changeBillDate;
	//录单日期必须为当前日期
	private boolean billDateMustToday;
	//录单必须录入部门
	private boolean departmentBeNull;
	//录单时必须录入经手人
	private boolean employeeBeNull;
	//单据不允许修改经手人
	private boolean changeEmployee;
	//录单时提示库存报警
	private boolean inventoryWarn;
	//录单时记忆商品，单位，职员位置
	//生产拆装使用多仓库
	//售价低于成本时提示
	private boolean priceBelowCostWarn;
	//售价低于最近进货价提示
	private boolean priceBelowBidWarn;
	//销售价低于最低售价时提示
	private boolean priceBelowAlowWarn;
	//销售单价自动修改商品的预设售价
	private boolean autoSaveNestPrice;
	//销售单默认收款帐户和收款金额
	private boolean defaultACC_AMT;
	//选择商品时显示库存量
	private boolean dispRealStock;
	//选择往来单位时显示应收应付
	private boolean dispARAP;
	//自动生成摘要
	private boolean autoGenSummary;
	//超过应收上限允许过帐
	private boolean performWhenARExceed;
	//	系统允许负库存
	private boolean alowNegativeStock;
	//默认的皮肤样式
	private String defaultSkin;
	//系统路径
	private String basePath;

	public int getMonthCloseDay() {
		return monthCloseDay;
	}
	public void setMonthCloseDay(int monthCloseDay) {
		this.monthCloseDay = monthCloseDay;
	}
	public boolean isAutoMonthClose() {
		return autoMonthClose;
	}
	public void setAutoMonthClose(boolean autoMonthClose) {
		this.autoMonthClose = autoMonthClose;
	}
	public boolean isCostUnusualWarn() {
		return costUnusualWarn;
	}
	public void setCostUnusualWarn(boolean costUnusualWarn) {
		this.costUnusualWarn = costUnusualWarn;
	}
	public int getUnusualPercent() {
		return unusualPercent;
	}
	public void setUnusualPercent(int unusualPercent) {
		this.unusualPercent = unusualPercent;
	}
	public boolean isSaveBeforePrint() {
		return saveBeforePrint;
	}
	public void setSaveBeforePrint(boolean saveBeforePrint) {
		this.saveBeforePrint = saveBeforePrint;
	}
	public boolean isAutoGenerateBillItem() {
		return autoGenerateBillItem;
	}
	public void setAutoGenerateBillItem(boolean autoGenerateBillItem) {
		this.autoGenerateBillItem = autoGenerateBillItem;
	}

	public boolean isPurchasePriceUnusualwarn() {
		return purchasePriceUnusualwarn;
	}
	public void setPurchasePriceUnusualwarn(boolean purchasePriceUnusualwarn) {
		this.purchasePriceUnusualwarn = purchasePriceUnusualwarn;
	}
	public boolean isBillDateMustToday() {
		return billDateMustToday;
	}
	public void setBillDateMustToday(boolean billDateMustToday) {
		this.billDateMustToday = billDateMustToday;
	}
	public boolean isDepartmentBeNull() {
		return departmentBeNull;
	}
	public void setDepartmentBeNull(boolean departmentBeNull) {
		this.departmentBeNull = departmentBeNull;
	}
	public boolean isEmployeeBeNull() {
		return employeeBeNull;
	}
	public void setEmployeeBeNull(boolean employeeBeNull) {
		this.employeeBeNull = employeeBeNull;
	}
	public boolean isChangeEmployee() {
		return changeEmployee;
	}
	public void setChangeEmployee(boolean changeEmployee) {
		this.changeEmployee = changeEmployee;
	}
	public boolean isInventoryWarn() {
		return inventoryWarn;
	}
	public void setInventoryWarn(boolean inventoryWarn) {
		this.inventoryWarn = inventoryWarn;
	}
	public boolean isPriceBelowCostWarn() {
		return priceBelowCostWarn;
	}
	public void setPriceBelowCostWarn(boolean priceBelowCostWarn) {
		this.priceBelowCostWarn = priceBelowCostWarn;
	}
	public boolean isPriceBelowBidWarn() {
		return priceBelowBidWarn;
	}
	public void setPriceBelowBidWarn(boolean priceBelowBidWarn) {
		this.priceBelowBidWarn = priceBelowBidWarn;
	}
	public boolean isPriceBelowAlowWarn() {
		return priceBelowAlowWarn;
	}
	public void setPriceBelowAlowWarn(boolean priceBelowAlowWarn) {
		this.priceBelowAlowWarn = priceBelowAlowWarn;
	}

	public boolean isDefaultACC_AMT() {
		return defaultACC_AMT;
	}
	public void setDefaultACC_AMT(boolean defaultACC_AMT) {
		this.defaultACC_AMT = defaultACC_AMT;
	}
	public boolean isDispRealStock() {
		return dispRealStock;
	}
	public void setDispRealStock(boolean dispRealStock) {
		this.dispRealStock = dispRealStock;
	}
	public boolean isDispARAP() {
		return dispARAP;
	}
	public void setDispARAP(boolean dispARAP) {
		this.dispARAP = dispARAP;
	}
	public boolean isAutoGenSummary() {
		return autoGenSummary;
	}
	public void setAutoGenSummary(boolean autoGenSummary) {
		this.autoGenSummary = autoGenSummary;
	}

	public boolean isBillChangeOrderPrice() {
		return billChangeOrderPrice;
	}
	public void setBillChangeOrderPrice(boolean billChangeOrderPrice) {
		this.billChangeOrderPrice = billChangeOrderPrice;
	}
	public boolean isRememberwar_emp() {
		return rememberwar_emp;
	}
	public void setRememberwar_emp(boolean rememberwar_emp) {
		this.rememberwar_emp = rememberwar_emp;
	}
	public boolean isChangeBillDate() {
		return changeBillDate;
	}
	public void setChangeBillDate(boolean changeBillDate) {
		this.changeBillDate = changeBillDate;
	}
	public boolean isAutoSaveNestPrice() {
		return autoSaveNestPrice;
	}
	public void setAutoSaveNestPrice(boolean autoSaveNestPrice) {
		this.autoSaveNestPrice = autoSaveNestPrice;
	}
	public boolean isPerformWhenARExceed() {
		return performWhenARExceed;
	}
	public void setPerformWhenARExceed(boolean performWhenARExceed) {
		this.performWhenARExceed = performWhenARExceed;
	}
	public boolean isAlowNegativeStock() {
		return alowNegativeStock;
	}
	public void setAlowNegativeStock(boolean alowNegativeStock) {
		this.alowNegativeStock = alowNegativeStock;
	}
	public int getPriceScale() {
		return priceScale;
	}
	public void setPriceScale(int priceScale) {
		this.priceScale = priceScale;
	}
	public int getAmountScale() {
		return amountScale;
	}
	public void setAmountScale(int amountScale) {
		this.amountScale = amountScale;
	}

	public CostMethod getDefaultCostMethod() {
		return defaultCostMethod;
	}
	public void setDefaultCostMethod(CostMethod defaultCostMethod) {
		this.defaultCostMethod = defaultCostMethod;
	}
	public boolean isUniteCostMethod() {
		return uniteCostMethod;
	}
	public void setUniteCostMethod(boolean uniteCostMethod) {
		this.uniteCostMethod = uniteCostMethod;
	}
	public String getDefaultSkin() {
		return defaultSkin;
	}
	public void setDefaultSkin(String defaultskin) {
		this.defaultSkin = defaultskin;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
}
