/*
 * 捷利商业进销存管理系统
 * @(#)ProductDeliveryReturnMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductDeliveryReturnMaster;
import cn.jely.cd.domain.ProductPurchaseReturnMaster;
import cn.jely.cd.pagemodel.ProductCommonMasterPM;
import cn.jely.cd.service.IProductDeliveryReturnService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:ProductDeliveryReturnMasterAction
 * @Description:Action
 * @author
 * @version 2013-07-17 14:54:28
 * 
 */
public class ProductDeliveryReturnAction extends JQGridAction<ProductDeliveryReturnMaster> {// 有针对树的操作需继承自TreeOperateAction
	private ProductDeliveryReturnMaster productDeliveryReturnMaster = new ProductDeliveryReturnMaster();
	private IProductDeliveryReturnService productDeliveryReturnService;

	public void setProductDeliveryReturnService(IProductDeliveryReturnService productDeliveryReturnMasterService) {
		this.productDeliveryReturnService = productDeliveryReturnMasterService;
	}

	@Override
	public ProductDeliveryReturnMaster getModel() {
		return productDeliveryReturnMaster;
	}

	@Override
	public String list() {
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		return SUCCESS;
	}

	@Override
	public String listjson() {
		Pager<ProductDeliveryReturnMaster> pager = productDeliveryReturnService.findPager(objectQuery);
		Pager<ProductDeliveryReturnMaster> pms=new Pager<ProductDeliveryReturnMaster>(pager.getPageNo(),pager.getPageSize(),pager.getTotalRows());
		for(ProductDeliveryReturnMaster master:pager.getRows()){
			pms.getRows().add((ProductDeliveryReturnMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		writejson(pms);
		return null;
	}

	@Override
	public String listall() {
		List<ProductDeliveryReturnMaster> allMasters = productDeliveryReturnService.getAll();
		List<ProductDeliveryReturnMaster> allPMs=new ArrayList<ProductDeliveryReturnMaster>();
		for(ProductDeliveryReturnMaster master:allMasters){
			allPMs.add((ProductDeliveryReturnMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}

	@Override
	public String save() {
		try {
			if (StringUtils.isNotBlank(id)) {
				productDeliveryReturnService.update(productDeliveryReturnMaster);
			} else {
				productDeliveryReturnService.save(productDeliveryReturnMaster);
			}
			actionJsonResult = new ActionJsonResult(productDeliveryReturnMaster);
		} catch (Exception e) {
			actionJsonResult = new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productDeliveryReturnService.delete(id);
		}
		actionJsonResult = new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				// 新增的时候不需要初始化，保存的时候要有个对象保存值
//				productDeliveryReturnMaster = new ProductDeliveryReturnMaster();
//			}
		} else {
			productDeliveryReturnMaster = productDeliveryReturnService.getById(id);
			if (isEditSave()) {
				productDeliveryReturnMaster.getDetails().clear();
				productDeliveryReturnMaster.setBusinessUnit(null);
				productDeliveryReturnMaster.setFundAccount(null);
				productDeliveryReturnMaster.setWarehouse(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	/** 获取编号 */
	public String getItem() {
		actionJsonResult = new ActionJsonResult((Object) ((ItemGenAble) productDeliveryReturnService).generateItem(productDeliveryReturnMaster.getBillDate()));
		return JSONLIST;
	}

	public String viewData() {
		productDeliveryReturnMaster = productDeliveryReturnService.getById(id);
		writejson(productDeliveryReturnMaster.toDisp(true));
		return null;
	}
}
