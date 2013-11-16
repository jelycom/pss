/*
 * 捷利商业进销存管理系统
 * @(#)PeriodStock.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.Product;
import cn.jely.cd.sys.domain.PeriodStock;
import cn.jely.cd.sys.service.IPeriodStockService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:PeriodStockAction
 * @Description:Action
 * @author
 * @version 2013-06-08 15:45:12
 * 
 */
public class PeriodStockAction extends JQGridAction<PeriodStock> {// 有针对树的操作需继承自TreeOperateAction
	private PeriodStock periodStock;
	private IPeriodStockService periodStockService;
	private Pager<PeriodStock> pager;
	private Long warehouseId;

	public void setPeriodStockService(IPeriodStockService periodStockService) {
		this.periodStockService = periodStockService;
	}

	public Pager<PeriodStock> getPager() {
		return pager;
	}

	@Override
	public PeriodStock getModel() {
		return periodStock;
	}

	@Override
	public String list() {
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		return SUCCESS;
	}

	@Override
	public String listjson() {
		// pager=periodStockService.findPager(objectQuery);
		// for (Object[] objects : allStocks) {
		// for (int i=1;i<objects.length;i++) {
		// if(objects[i] == null){
		// Product product=(Product)objects[0];
		// objects[i]=new PeriodStock(product, 0, BigDecimal.ZERO);
		// }
		// }
		// }
		parseCondition();
		List<PeriodStock> allStocks = periodStockService.findAllStock(objectQuery, warehouseId);
		List<PeriodStock> dispStocks = new ArrayList<PeriodStock>();
		for (PeriodStock periodStock : allStocks) {
			dispStocks.add(periodStock.toDisp(false));
		}
		pager = new Pager<PeriodStock>(1, dispStocks.size(), dispStocks.size(), 1l, dispStocks);
		actionJsonResult = new ActionJsonResult(pager);
		return JSONALL;
	}

	@Override
	public String listall() {
		actionJsonResult = new ActionJsonResult(periodStockService.getAll());
		return JSONALL;
	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	@Override
	public String save() {
		try {
			if (StringUtils.isNotBlank(id)) {
				periodStockService.update(periodStock);
			} else {
				periodStockService.save(periodStock);
			}
			actionJsonResult = new ActionJsonResult(periodStock);
		} catch (Exception e) {
			actionJsonResult = new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			periodStockService.delete(id);
		}
		actionJsonResult = new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				// 新增的时候不需要初始化，保存的时候要有个对象保存值
				periodStock = new PeriodStock();
			}
		} else {
			periodStock = periodStockService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				periodStock.setWarehouse(null);
				periodStock.setProduct(null);
			}
		}
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

}
