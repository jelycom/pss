/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductTransferDiffMaster;
import cn.jely.cd.domain.ProductTransferSameMaster;
import cn.jely.cd.pagemodel.ProductTransferMasterPM;
import cn.jely.cd.service.IProductTransferDiffService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * 同价调拨相关控制层
 * 
 * @ClassName:ProductTransferMasterAction
 * @author
 * @version 2013-07-17 14:54:28
 * 
 */
public class ProductTransferDiffAction extends JQGridAction<ProductTransferDiffMaster> {// 有针对树的操作需继承自TreeOperateAction
	private ProductTransferDiffMaster productTransferDiffMaster=new ProductTransferDiffMaster();
	private IProductTransferDiffService productTransferDiffService;

	public void setProductTransferDiffService(IProductTransferDiffService productTransferDiffService) {
		this.productTransferDiffService = productTransferDiffService;
	}

	@Override
	public ProductTransferDiffMaster getModel() {
		return productTransferDiffMaster;
	}

	@Override
	public String list() {
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		return SUCCESS;
	}

	@Override
	public String listjson() {
		Pager<ProductTransferDiffMaster> masterpg = productTransferDiffService.findPager(objectQuery);
		Pager<ProductTransferDiffMaster> pager = new Pager<ProductTransferDiffMaster>(masterpg.getPageNo(),
				masterpg.getPageSize(), masterpg.getTotalRows());
		for (ProductTransferDiffMaster master : masterpg.getRows()) {
			pager.getRows().add((ProductTransferDiffMaster) master.toDisp(false));
		}
//		actionJsonResult = new ActionJsonResult(pager);
		writejson(pager);
		return null;
	}

	@Override
	public String listall() {
		List<ProductTransferDiffMaster> allMasters = productTransferDiffService.getAll();
		List<ProductTransferDiffMaster> allPMs = new ArrayList<ProductTransferDiffMaster>();
		for (ProductTransferDiffMaster master : allMasters) {
			allPMs.add((ProductTransferDiffMaster) master.toDisp(false));
		}
		actionJsonResult = new ActionJsonResult(allMasters);
		return JSONALL;
	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	@Override
	public String save() {
		try {
			if (StringUtils.isNotBlank(id)) {
				productTransferDiffService.update(productTransferDiffMaster);
			} else {
				productTransferDiffService.save(productTransferDiffMaster);
			}
			actionJsonResult = new ActionJsonResult(productTransferDiffMaster);
		} catch (Exception e) {
			actionJsonResult = new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productTransferDiffService.delete(id);
		}
		actionJsonResult = new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				// 新增的时候不需要初始化，保存的时候要有个对象保存值
//				productTransferDiffMaster = new ProductTransferDiffMaster();
//			}
		} else {
			productTransferDiffMaster = productTransferDiffService.getById(id);
			if (isEditSave()) {
				productTransferDiffMaster.getDetails().clear();
				productTransferDiffMaster.setInWarehouse(null);
				productTransferDiffMaster.setOutWarehouse(null);
				productTransferDiffMaster.setInEmployee(null);
				productTransferDiffMaster.setOutEmployee(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	/** 获取编号 */
	public String getItem() {
		actionJsonResult = new ActionJsonResult((Object) ((ItemGenAble) productTransferDiffService).generateItem(productTransferDiffMaster.getBillDate()));
		return JSONLIST;
	}

	public String viewData() {
		productTransferDiffMaster = productTransferDiffService.getById(id);
		writejson(productTransferDiffMaster.toDisp(true));
		return null;
	}
}
