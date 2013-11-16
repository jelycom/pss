/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferDiffMaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.domain;

import java.util.Date;

/**
 * 异价调拨主表
 * @ClassName:ProductTransferDiffMaster
 * Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-17 下午3:56:48
 *
 */
public class ProductTransferDiffMaster extends ProductTransferMaster {

	public ProductTransferDiffMaster() {
		super();
	}

	public ProductTransferDiffMaster(String item, Warehouse outWarehouse, Employee outEmployee, Warehouse inWarehouse,
			Employee inEmployee, Date billDate) {
		super(item, outWarehouse, outEmployee, inWarehouse, inEmployee, billDate);
	}

}
