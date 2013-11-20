/*
 * 捷利商业进销存管理系统
 * @(#)CodeGeneratorUtil.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-1
 */
package cn.jely.cd.util.code.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.AccountInMaster;
import cn.jely.cd.domain.AccountOtherInMaster;
import cn.jely.cd.domain.AccountOtherOutMaster;
import cn.jely.cd.domain.AccountOutMaster;
import cn.jely.cd.domain.AccountTransferMaster;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.InventoryLossMaster;
import cn.jely.cd.domain.InventoryProfitMaster;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.ProductDeliveryReturnMaster;
import cn.jely.cd.domain.ProductOrderBillDeliveryMaster;
import cn.jely.cd.domain.ProductOrderBillPurchaseMaster;
import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.domain.ProductPurchaseReturnMaster;
import cn.jely.cd.domain.ProductStockingMaster;
import cn.jely.cd.domain.ProductTransferDiffMaster;
import cn.jely.cd.domain.ProductTransferSameMaster;
import cn.jely.cd.sys.domain.ItemSetting;
import cn.jely.cd.util.code.DateCoder;
import cn.jely.cd.util.code.ICodeGenerator;

/**
 * 需要使用工厂/工厂方法,读出实体类对应的生成器及生成格式,并按提供的值进行生成.
 * 
 * @ClassName:CodeGeneratorUtil Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-1 下午5:53:03
 * 
 */
public class CodeGeneratorUtil {
	public static String GENPERDAY="day";
	public static String GENPERMOTH="month";
	public static String GENPERYEAR="year";
	public static String GENNODATE="withoutdate";
	
	private static CodeGeneratorUtil instance=new CodeGeneratorUtil();
	private Map<String,ItemSetting> settings= new HashMap<String, ItemSetting>();
	private Map<String , ICodeGenerator> generators = new HashMap<String, ICodeGenerator>();
	
	private CodeGeneratorUtil() {
		generators.put(GENPERDAY, new DayGenerator());
		generators.put(GENPERMOTH, new MonthGenerator());
		generators.put(GENPERYEAR, new YearGenerator());
		generators.put(GENNODATE, new NoDateGenerator());
		settings.put(BusinessUnits.class.getName(),new ItemSetting("往来单位",BusinessUnits.class.getName(),"{BU}[yyyy](0000000)",GENNODATE));
		settings.put(ProductPlanPurchaseMaster.class.getName(),new ItemSetting("采购计划",ProductPlanPurchaseMaster.class.getName(),"{PPP}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductPlanDeliveryMaster.class.getName(),new ItemSetting("销售计划",ProductPlanDeliveryMaster.class.getName(),"{PPD}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductOrderBillPurchaseMaster.class.getName(),new ItemSetting("采购定单",ProductOrderBillPurchaseMaster.class.getName(),"{POP}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductOrderBillDeliveryMaster.class.getName(),new ItemSetting("销售定单",ProductOrderBillDeliveryMaster.class.getName(),"{POD}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductPurchaseMaster.class.getName(),new ItemSetting("采购进货",ProductPurchaseMaster.class.getName(),"{PP}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductDeliveryMaster.class.getName(),new ItemSetting("销售出货",ProductDeliveryMaster.class.getName(),"{PD}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductPurchaseReturnMaster.class.getName(),new ItemSetting("采购退货",ProductPurchaseReturnMaster.class.getName(),"{PPR}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductDeliveryReturnMaster.class.getName(),new ItemSetting("销售退货",ProductDeliveryReturnMaster.class.getName(),"{PDR}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductTransferSameMaster.class.getName(),new ItemSetting("同价调拨",ProductTransferSameMaster.class.getName(),"{TS}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductTransferDiffMaster.class.getName(),new ItemSetting("异价调拨",ProductTransferDiffMaster.class.getName(),"{TD}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(ProductStockingMaster.class.getName(),new ItemSetting("盘点表",ProductStockingMaster.class.getName(),"{PS}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(AccountInMaster.class.getName(),new ItemSetting("收款",AccountInMaster.class.getName(),"{AI}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(AccountOutMaster.class.getName(),new ItemSetting("付款",AccountOutMaster.class.getName(),"{AO}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(AccountOtherInMaster.class.getName(),new ItemSetting("其它收款",AccountOtherInMaster.class.getName(),"{OAI}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(AccountOtherOutMaster.class.getName(),new ItemSetting("其它付款",AccountOtherOutMaster.class.getName(),"{OAO}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(AccountTransferMaster.class.getName(),new ItemSetting("转帐",AccountTransferMaster.class.getName(),"{TR}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(InventoryProfitMaster.class.getName(),new ItemSetting("盘盈表",InventoryProfitMaster.class.getName(),"{IP}[yyyyMM](0000000)",GENPERMOTH));
		settings.put(InventoryLossMaster.class.getName(),new ItemSetting("盘盈表",InventoryLossMaster.class.getName(),"{IL}[yyyyMM](0000000)",GENPERMOTH));
	}
	
	public static CodeGeneratorUtil getInstance() {
		return instance;
	}


	public String genItem(String className, String lastValue,Date date) {
		ItemSetting setting=getItemSetting(className);
		if (setting!=null) {
			ICodeGenerator generator = generators.get(setting.getGeneratorName());
			return generator.Generate(new DateCoder(setting.getRegx(), lastValue, date));
		}
		return null;
	}
	
	public ICodeGenerator getGenerator(String className){
		ItemSetting setting=getItemSetting(className);
			if (setting!=null) {
				return  generators.get(setting.getGeneratorName());
			}
		return null;
	}
	
	private ItemSetting getItemSetting(String className){
		if(StringUtils.isNotBlank(className)){
			return settings.get(className);
		}
		return null;
	}
}
