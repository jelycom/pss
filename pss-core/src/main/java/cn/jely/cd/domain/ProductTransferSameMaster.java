/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferSameMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.domain;

import java.util.Date;

/**
 * 同价调拨主表
 * @ClassName:ProductTransferSameMaster
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-17 下午3:56:01
 *
 */
public class ProductTransferSameMaster extends ProductTransferMaster {

	public ProductTransferSameMaster() {
		super();
	}

	public ProductTransferSameMaster(String item, Warehouse outWarehouse, Employee outEmployee, Warehouse inWarehouse,
			Employee inEmployee, Date billDate) {
		super(item, outWarehouse, outEmployee, inWarehouse, inEmployee, billDate);
	}
	
}
