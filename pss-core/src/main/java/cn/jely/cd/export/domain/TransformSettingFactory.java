/*
 * 捷利商业进销存管理系统
 * @(#)TransformSettingFactory.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-11-13
 */
package cn.jely.cd.export.domain;

import java.util.ArrayList;
import java.util.List;

import cn.jely.cd.util.CostMethod;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-11-13 下午3:03:15
 */
public class TransformSettingFactory {

	public List<TransformSetting> getProductSetting(){
		List<TransformSetting> productSettings = new ArrayList<TransformSetting>();
		productSettings.add(new TransformSetting(null,"product","productTypeName","类别名称",String.class));
		productSettings.add(new TransformSetting(null,"product","productTypeItem","类别编号",String.class));
		productSettings.add(new TransformSetting(null,"product","costMethod","成本方法",CostMethod.class));
		productSettings.add(new TransformSetting(null,"product","fullName","商品名称",String.class));
		productSettings.add(new TransformSetting(null,"product","shortName","简称",String.class));
		productSettings.add(new TransformSetting(null,"product","barCode","条码",String.class));
		productSettings.add(new TransformSetting(null,"product","unit","单位",String.class));
		productSettings.add(new TransformSetting(null,"product","marque","型号",String.class));
		productSettings.add(new TransformSetting(null,"product","specification","规格",String.class));
		productSettings.add(new TransformSetting(null,"product","color","颜色",String.class));
		productSettings.add(new TransformSetting(null,"product","safeStock","安全库存量",Integer.class));
		return productSettings;
	}
}
