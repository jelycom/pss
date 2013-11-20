/*
 * 捷利商业进销存管理系统
 * @(#)SystemSettingDaoImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-6
 */
package cn.jely.cd.sys.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.callback.ConfirmationCallback;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.tree.DefaultExpressionEngine;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.util.LocalizedTextUtil;

import cn.jely.cd.sys.dao.ISystemSettingDao;
import cn.jely.cd.sys.domain.CompanyInfo;
import cn.jely.cd.sys.domain.SysEditOption;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.DateUtils;
import cn.jely.cd.util.ProjectConfig;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-6 上午10:33:23
 */
public class SystemSettingDaoImpl implements ISystemSettingDao {

	private XMLConfiguration configuration = ProjectConfig.getInstance().init().getXmlConfiguration();
	private String root = "configuration";

	private String companyInfoGroup = "companyInfo";
	private String companyName = "name";//公司名称
	private String companyRAddress = "registeraddress";//注册地址
	private String companyBAddress = "businessaddress";//经营地址
	private String companyTel = "tel";//电话
	private String companyFax = "fax";//传真
//	private String companyBank = "bank";
	private String companyTaxNo = "taxNo";// 税号
	private String companyOrgNo = "organizationCode";// 机构代码
	private String companyLR = "legalRepresentative";// 法人代表
	private String companyBS = "businessScope";// 法人代表
	private String opened = "opened";//是否开帐
	private String openningDate = "openningDate";//开帐日期
	private String locale = "locale";//区域

	private String editOption = "editOption";
	private String monthCloseDay = "monthCloseDay";
	private String autoMonthClose = "autoMonthClose";
	private String priceScale = "priceScale";
	private String amountScale = "amountScale";
	private String uniteCostMethod = "uniteCostMethod";
	private String defaultCostMethod = "defaultCostMethod";
	private String costUnusualWarn = "costUnusualWarn";
	private String unusualPercent = "unusualPercent";
	private String saveBeforePrint = "saveBeforePrint";
	private String autoGenerateBillItem = "autoGenerateBillItem";
	private String billChangeOrderPrice = "billChangeOrderPrice";
	private String purchasePriceUnusualwarn = "purchasePriceUnusualwarn";
	private String rememberwar_emp = "rememberwar_emp";
	private String changeBillDate = "changeBillDate";
	private String billDateMustToday = "billDateMustToday";
	private String departmentBeNull = "departmentBeNull";
	private String employeeBeNull = "employeeBeNull";
	private String changeEmployee = "changeEmployee";
	private String inventoryWarn = "inventoryWarn";
	private String priceBelowCostWarn = "priceBelowCostWarn";
	private String priceBelowBidWarn = "priceBelowBidWarn";
	private String priceBelowAlowWarn = "priceBelowAlowWarn";
	private String autoSaveNestPrice = "autoSaveNestPrice";
	private String defaultACC_AMT = "defaultACC_AMT";
	private String dispRealStock = "dispRealStock";
	private String dispARAP = "dispARAP";
	private String autoGenSummary = "autoGenSummary";
	private String performWhenARExceed = "performWhenARExceed";
	private String alowNegativeStock = "alowNegativeStock";
	private String defaultSkin = "defaultSkin";
	private String systemName = "systemName";
	private String systemVersion = "systemVersion";
	private String basePath = "basePath";
	// public XMLConfiguration getXmlConfiguration(){
	// XMLConfiguration xmlConfiguration = new XMLConfiguration();
	// try {
	// xmlConfiguration.setReloadingStrategy(getFileChangedReloadingStrategy());
	// xmlConfiguration.setDelimiterParsingDisabled(true);
	// xmlConfiguration.setAttributeSplittingDisabled(true);
	// xmlConfiguration.setURL(getConfigURL(confDir, fileName));
	// } catch (Exception e) {
	// LOGGER.error("failed to load xml config:" + fileName, e);
	// }
	// return xmlConfiguration;
	// }
	//
	// private FileChangedReloadingStrategy getFileChangedReloadingStrategy() {
	// FileChangedReloadingStrategy reloadingStrategy = new
	// FileChangedReloadingStrategy();
	// reloadingStrategy.setRefreshDelay(10000);// 10s
	// return reloadingStrategy;
	// }
	@Override
	public SystemSetting getSetting() {
		try {
			SystemSetting setting = SystemSetting.getInstance();
			Node rootNode = configuration.getRoot();
			setting.setSystemName(configuration.getString(systemName,"捷利进销存管理系统"));
			setting.setSystemVersion(configuration.getString(systemVersion,"0"));
//			configuration.setExpressionEngine(new DefaultExpressionEngine());
			String comPreFix = "companyInfo.";
			CompanyInfo companyInfo = setting.getCompanyInfo();
			String comName = configuration.getString(comPreFix + companyName);
			companyInfo.setCompanyName(comName);
			companyInfo.setBusinessAddress(configuration.getString(comPreFix + companyBAddress));
			companyInfo.setRegisteredAddress(configuration.getString(comPreFix + companyRAddress));
			companyInfo.setLegalRepresentative(configuration.getString(comPreFix + companyLR));
			companyInfo.setTelephoneNumber(configuration.getString(comPreFix + companyTel));
			companyInfo.setFaxNumber(configuration.getString(comPreFix + companyFax));
			companyInfo.setCorporationTax(configuration.getString(comPreFix + companyTaxNo));
			companyInfo.setOrganizationCode(configuration.getString(comPreFix + companyOrgNo));
			companyInfo.setBusinessScope(configuration.getString(comPreFix + companyBS));
			Locale loca = LocalizedTextUtil.localeFromString(configuration.getString(comPreFix + locale),
					Locale.getDefault());
			companyInfo.setLocale(loca);
			Boolean open=configuration.getBoolean(comPreFix+opened, false);
			companyInfo.setOpened(open);
			if(open){
				String dateString = configuration.getString(comPreFix + openningDate);
				if(StringUtils.isNotBlank(dateString)){
					try {
						DateFormat df=DateFormat.getDateInstance(DateFormat.MEDIUM, loca);
						companyInfo.setOpenningDate(df.parse(dateString));
					} catch (ParseException e) {
						throw new RuntimeException("开帐日期解析出错！");
					}
				}
			}
			
			SysEditOption edtOption = setting.getEditOption();
			String edtPreFix = editOption + ".";
			edtOption.setPriceScale(configuration.getInt(edtPreFix + priceScale));
			edtOption.setAmountScale(configuration.getInt(edtPreFix + amountScale));
			edtOption.setCostUnusualWarn(configuration.getBoolean(edtPreFix + costUnusualWarn));
			edtOption.setUnusualPercent(configuration.getInt(edtPreFix + unusualPercent));
			edtOption.setSaveBeforePrint(configuration.getBoolean(edtPreFix + saveBeforePrint));
			edtOption.setAutoGenerateBillItem(configuration.getBoolean(edtPreFix + autoGenerateBillItem));
			edtOption.setBillChangeOrderPrice(configuration.getBoolean(edtPreFix + billChangeOrderPrice));
			edtOption.setPurchasePriceUnusualwarn(configuration.getBoolean(edtPreFix + purchasePriceUnusualwarn));
			edtOption.setRememberwar_emp(configuration.getBoolean(edtPreFix + rememberwar_emp));
			edtOption.setChangeBillDate(configuration.getBoolean(edtPreFix + changeBillDate));
			edtOption.setBillDateMustToday(configuration.getBoolean(edtPreFix + billDateMustToday));
			edtOption.setDepartmentBeNull(configuration.getBoolean(edtPreFix + departmentBeNull));
			edtOption.setEmployeeBeNull(configuration.getBoolean(edtPreFix + employeeBeNull));
			edtOption.setChangeEmployee(configuration.getBoolean(edtPreFix + changeEmployee));
			edtOption.setInventoryWarn(configuration.getBoolean(edtPreFix + inventoryWarn));
			edtOption.setPriceBelowCostWarn(configuration.getBoolean(edtPreFix + priceBelowCostWarn));
			edtOption.setPriceBelowBidWarn(configuration.getBoolean(edtPreFix + priceBelowBidWarn));
			edtOption.setPriceBelowAlowWarn(configuration.getBoolean(edtPreFix + priceBelowAlowWarn));
			edtOption.setAutoSaveNestPrice(configuration.getBoolean(edtPreFix + autoSaveNestPrice));
			edtOption.setDefaultACC_AMT(configuration.getBoolean(edtPreFix + defaultACC_AMT));
			edtOption.setDispRealStock(configuration.getBoolean(edtPreFix + dispRealStock));
			edtOption.setDispARAP(configuration.getBoolean(edtPreFix + dispARAP));
			edtOption.setAutoGenSummary(configuration.getBoolean(edtPreFix + autoGenSummary));
			edtOption.setPerformWhenARExceed(configuration.getBoolean(edtPreFix + performWhenARExceed));
			edtOption.setAlowNegativeStock(configuration.getBoolean(edtPreFix + alowNegativeStock));
			edtOption.setAutoMonthClose(configuration.getBoolean(edtPreFix + autoMonthClose, false));
			edtOption.setMonthCloseDay(configuration.getInteger(edtPreFix + monthCloseDay, 1));
			edtOption.setDefaultSkin(configuration.getString(edtPreFix+defaultSkin,ConstValue.DEFAULTSKIN));
			boolean uniteCost = configuration.getBoolean(edtPreFix+uniteCostMethod);
			edtOption.setUniteCostMethod(uniteCost);
			String cmString = configuration.getString(edtPreFix+defaultCostMethod);
			CostMethod cm = CostMethod.valueOf(cmString);
			edtOption.setDefaultCostMethod(cm);
			edtOption.setBasePath(configuration.getString(edtPreFix + basePath));
//		Object openDateproperty = configuration.getProperty(edtPreFix + openDate);
//		if(openDateproperty != null){
//			edtOption.setOpenDate((Date) openDateproperty);
//		}
			// setting.setRoundingMode(roundingMode)
			return setting;
		} catch (Exception e) {
			SystemSetting defaultSetting = getDefaultSetting();
			save(defaultSetting);
			return defaultSetting;
		}
	}

	@Override
	public void save(SystemSetting systemSetting) {
		configuration.clear();
		Node rootNode = configuration.getRoot();
		rootNode.removeChildren();
		Node companyInfo = new Node(companyInfoGroup);
//		rootNode = new Node(root);
//		configuration.setRoot(rootNode);
		rootNode.addChild(new Node(systemName,systemSetting.getSystemName()));
		rootNode.addChild(new Node(systemVersion,systemSetting.getSystemVersion()));
		rootNode.addChild(companyInfo);
		CompanyInfo comInfo = systemSetting.getCompanyInfo();
		if (comInfo != null) {
			companyInfo.addChild(new Node(companyName, comInfo.getCompanyName()));
			companyInfo.addChild(new Node(companyRAddress, comInfo.getRegisteredAddress()));
			companyInfo.addChild(new Node(companyBAddress, comInfo.getBusinessAddress()));
			companyInfo.addChild(new Node(companyTel, comInfo.getTelephoneNumber()));
			companyInfo.addChild(new Node(companyFax, comInfo.getFaxNumber()));
			companyInfo.addChild(new Node(companyTaxNo, comInfo.getCorporationTax()));
			companyInfo.addChild(new Node(companyOrgNo, comInfo.getOrganizationCode()));
			companyInfo.addChild(new Node(companyLR, comInfo.getLegalRepresentative()));
			companyInfo.addChild(new Node(companyBS, comInfo.getBusinessScope()));
			companyInfo.addChild(new Node(locale, comInfo.getLocale().toString()));
			companyInfo.addChild(new Node(opened, comInfo.getOpened()));
			Date oDate = comInfo.getOpenningDate();
			if(oDate!=null){
				DateFormat df=DateFormat.getDateInstance(DateFormat.MEDIUM, comInfo.getLocale());
				companyInfo.addChild(new Node(openningDate, df.format(oDate)));
			}
		}
		SysEditOption sysEditOption = systemSetting.getEditOption();
		Node editOptionNode = new Node(this.editOption);
		rootNode.addChild(editOptionNode);
		if (sysEditOption != null) {
			editOptionNode.addChild(new Node(priceScale, sysEditOption.getPriceScale()));
			editOptionNode.addChild(new Node(amountScale, sysEditOption.getAmountScale()));
			editOptionNode.addChild(new Node(costUnusualWarn, sysEditOption.isCostUnusualWarn()));
			editOptionNode.addChild(new Node(unusualPercent, sysEditOption.getUnusualPercent()));
			editOptionNode.addChild(new Node(saveBeforePrint, sysEditOption.isSaveBeforePrint()));
			editOptionNode.addChild(new Node(autoGenerateBillItem, sysEditOption.isAutoGenerateBillItem()));
			editOptionNode.addChild(new Node(billChangeOrderPrice, sysEditOption.isBillChangeOrderPrice()));
			editOptionNode.addChild(new Node(purchasePriceUnusualwarn, sysEditOption.isPurchasePriceUnusualwarn()));
			editOptionNode.addChild(new Node(rememberwar_emp, sysEditOption.isRememberwar_emp()));
			editOptionNode.addChild(new Node(changeBillDate, sysEditOption.isChangeBillDate()));
			editOptionNode.addChild(new Node(billDateMustToday, sysEditOption.isBillDateMustToday()));
			editOptionNode.addChild(new Node(departmentBeNull, sysEditOption.isDepartmentBeNull()));
			editOptionNode.addChild(new Node(employeeBeNull, sysEditOption.isEmployeeBeNull()));
			editOptionNode.addChild(new Node(changeEmployee, sysEditOption.isChangeEmployee()));
			editOptionNode.addChild(new Node(inventoryWarn, sysEditOption.isInventoryWarn()));
			editOptionNode.addChild(new Node(priceBelowCostWarn, sysEditOption.isPriceBelowCostWarn()));
			editOptionNode.addChild(new Node(priceBelowBidWarn, sysEditOption.isPriceBelowBidWarn()));
			editOptionNode.addChild(new Node(priceBelowAlowWarn, sysEditOption.isPriceBelowAlowWarn()));
			editOptionNode.addChild(new Node(autoSaveNestPrice, sysEditOption.isAutoSaveNestPrice()));
			editOptionNode.addChild(new Node(defaultACC_AMT, sysEditOption.isDefaultACC_AMT()));
			editOptionNode.addChild(new Node(dispRealStock, sysEditOption.isDispRealStock()));
			editOptionNode.addChild(new Node(dispARAP, sysEditOption.isDispARAP()));
			editOptionNode.addChild(new Node(autoGenSummary, sysEditOption.isAutoSaveNestPrice()));
			editOptionNode.addChild(new Node(performWhenARExceed, sysEditOption.isPerformWhenARExceed()));
			editOptionNode.addChild(new Node(alowNegativeStock, sysEditOption.isAlowNegativeStock()));
			editOptionNode.addChild(new Node(autoMonthClose, sysEditOption.isAutoMonthClose()));
			editOptionNode.addChild(new Node(monthCloseDay, sysEditOption.getMonthCloseDay()));
			editOptionNode.addChild(new Node(uniteCostMethod, sysEditOption.isUniteCostMethod()));
			editOptionNode.addChild(new Node(defaultCostMethod,sysEditOption.getDefaultCostMethod().toString()));
			if(StringUtils.isNotBlank(sysEditOption.getDefaultSkin())){
				editOptionNode.addChild(new Node(defaultSkin,sysEditOption.getDefaultSkin()));
			}
			editOptionNode.addChild(new Node(basePath,sysEditOption.getBasePath()));
		}
		try {
			configuration.setEncoding("utf-8");
			configuration.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("保存出错！");
		}
	}
	private SystemSetting getDefaultSetting(){
		SystemSetting defaultSetting=SystemSetting.getInstance();
		defaultSetting.setSystemName("捷利进销存管理系统");
		defaultSetting.setSystemVersion("0.3beta");
		CompanyInfo companyInfo = new CompanyInfo("公司名称", "注册地址", "经营地址", "机构代码", "税号", "法人", "经营范围", CostMethod.FIFO, CostMethod.ASSI, false, null, null, ""); 
		companyInfo.setLocale(Locale.getDefault());
		companyInfo.setOpened(false);
		defaultSetting.setCompanyInfo(companyInfo);
		SysEditOption edtOption = defaultSetting.getEditOption();
		edtOption.setAmountScale(2);
		edtOption.setPriceScale(4);
		edtOption.setMonthCloseDay(25);
		edtOption.setUnusualPercent(20);
		edtOption.setDefaultCostMethod(CostMethod.FIFO);
		edtOption.setUniteCostMethod(false);
		edtOption.setCostUnusualWarn(true);
		edtOption.setSaveBeforePrint(true);
		edtOption.setAutoGenerateBillItem(true);
		edtOption.setBillChangeOrderPrice(true);
		edtOption.setPurchasePriceUnusualwarn(true);
		edtOption.setRememberwar_emp(false);
		edtOption.setChangeBillDate(true);
		edtOption.setBillDateMustToday(false);
		edtOption.setDepartmentBeNull(true);
		edtOption.setEmployeeBeNull(false);
		edtOption.setChangeEmployee(true);
		edtOption.setInventoryWarn(true);
		edtOption.setPriceBelowCostWarn(true);
		edtOption.setPriceBelowBidWarn(true);
		edtOption.setPriceBelowAlowWarn(true);
		edtOption.setAutoSaveNestPrice(true);
		edtOption.setDefaultACC_AMT(false);
		edtOption.setDispRealStock(true);
		edtOption.setDispARAP(true);
		edtOption.setAutoGenSummary(true);
		edtOption.setPerformWhenARExceed(false);
		edtOption.setAlowNegativeStock(true);
		edtOption.setAutoMonthClose(false);
		return defaultSetting;
	}
}
