/*
 * 捷利商业进销存管理系统
 * @(#)ProductReportAction.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-25
 */
package cn.jely.cd.export.web;

import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JasperPrint;

import cn.jely.cd.export.service.IExporterService;
import cn.jely.cd.export.util.ExportUtil;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.web.BaseAction;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-10-25 下午2:07:41
 */
public class ProductReportAction extends BaseReportAction {

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	/**Long:warehouseId:仓库*/
	private Long warehouseId;
	/**Long:productId:商品*/
	private Long productId;
	/**Long:businessUnitId:往来单位*/
	private Long businessUnitId;
	/**Long:productTypeId:产品类型*/
	private Long productTypeId;
	/**Date:startDate:开始日期*/
	private Date startDate;
	/**Date:endDate:结束日期*/
	private Date endDate;
	
	IExporterService exporterService;
	IProductStockDetailService productStockDetailService;
	//商品库存分布表
	public String  printbywarehouse(){
		return NONE;
	}
	
//商品进货表
//商品出货表
//商品进销存变动表
	public void setExporterService(IExporterService exporterService) {
		this.exporterService = exporterService;
	}
	
}
