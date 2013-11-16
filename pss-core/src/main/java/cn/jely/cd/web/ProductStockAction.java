/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockDetail.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Region;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.export.service.IExporterService;
import cn.jely.cd.export.util.ExportUtil;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.QueryRule;
import cn.jely.cd.vo.RealStockVO;

/**
 * @ClassName:ProductStockDetailAction
 * @author
 * @version 2013-06-21 14:54:28
 * 
 */
public class ProductStockAction extends JQGridAction<ProductStockDetail> {// 有针对树的操作需继承自TreeOperateAction
	private ProductStockDetail productStockDetail;
	private IProductStockDetailService productStockDetailService;
	private IExporterService exporterService;
	private Pager<RealStockVO> pager;
	private Long warehouseId;
	private Long productTypeId;
	private Long productId;
	private Long regionId;
	private String group;
	
	public void setExporterService(IExporterService exporterService) {
		this.exporterService = exporterService;
	}

	public void setProductStockDetailService(IProductStockDetailService productStockDetailService) {
		this.productStockDetailService = productStockDetailService;
	}

	public Pager<RealStockVO> getPager() {
		return pager;
	}

	@Override
	public ProductStockDetail getModel() {
		return productStockDetail;
	}

	public String list() {
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		return SUCCESS;
	}

	public String listjson() {
		parseCondition();
		if (StringUtils.isNotBlank(group)) {
			objectQuery.setGroup(group);
		}
		List<RealStockVO> realStocks = productStockDetailService.findRealStock(warehouseId,productTypeId,objectQuery);
		List<RealStockVO> vos = new ArrayList<RealStockVO>();
		for (RealStockVO vo : realStocks) {
			vos.add(vo.toDisp(false));
		}
		Pager<RealStockVO> pager = new Pager<RealStockVO>(1, vos.size(), vos.size(), 1, vos);
//		actionJsonResult = new ActionJsonResult(pager);
		writejson(pager);
		getSession().put(getClass().getSimpleName(), objectQuery);// 将查询条件放入Session进行保存,以供其它模块或导出使用
		return null;
	}

	/**
	 * 查询实际库存
	 * 
	 * @return String
	 */
	public String findquantity() {
		Warehouse warehouse = null;
		if (warehouseId != null) {
			warehouse = new Warehouse();
			warehouse.setId(warehouseId);
		}
		Product product = null;
		if (productId != null) {
			product = new Product(productId);
		}
		actionJsonResult = new ActionJsonResult(productStockDetailService.findRealStockQuantity(warehouse, product));
		return JSONALL;
	}

	public String findRealStockQuantity() {
		if (warehouseId != null) {
			ProductType productType = null;
			if (productTypeId != null || productTypeId != -1) {
				productType = new ProductType(productTypeId);
			}
			Warehouse warehouse = new Warehouse(warehouseId);
			List<RealStockVO> realStocks = productStockDetailService.findRealStock(warehouse, productType);
			List<RealStockVO> vos = new ArrayList<RealStockVO>();
			for (RealStockVO vo : realStocks) {
				vos.add(vo.toDisp(false));
			}
			actionJsonResult = new ActionJsonResult(vos);
		} else {
			actionJsonResult = new ActionJsonResult(false, "仓库或产品类别错误！");
		}

		return JSONALL;
	}

	public String listall() {
		actionJsonResult = new ActionJsonResult(productStockDetailService.getAll());
		return JSONALL;
	}
	
	public String viewData() {
		// productStockDetail=productStockDetailService.getById(id);
		// actionJsonResult=new
		// ActionJsonResult((Object)productStockDetail.convert(true));
		return JSONLIST;
	}

	public String export(){
		try {
			JasperPrint jasperPrint = exporterService.findRealStock(warehouseId, productTypeId, 2l);
			ExportUtil.toPdf(getResponse(), jasperPrint);
		} catch (JRException exception) {
			exception.printStackTrace();
			writejson(exception.getMessage());
		} catch (IOException exception) {
			exception.printStackTrace();
			writejson(exception.getMessage());
		}
		return null;
	}
	
	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	protected void beforInputSave() {
	}

	@Override
	protected String save() {
		return null;
	}

	@Override
	protected String delete() {
		return null;
	}
}
