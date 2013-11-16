/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.service.IProductPlanDeliveryService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:ProductDeliveryPlanMasterAction
 * @Description:Action
 * @author 周义礼
 * @version 2012-12-05 10:34:59
 * 
 */
public class ProductPlanDeliveryAction extends JQGridAction<ProductPlanDeliveryMaster> {
	private ProductPlanDeliveryMaster productPlanDeliveryMaster=new ProductPlanDeliveryMaster();
	private IProductPlanDeliveryService productPlanDeliveryService;

	public void setProductPlanDeliveryService(IProductPlanDeliveryService productplanService) {
		this.productPlanDeliveryService = productplanService;
	}

	@Override
	public ProductPlanDeliveryMaster getModel() {
		return productPlanDeliveryMaster;
	}

	@Override
	public String list() {
		logger.debug("Productplanmaster list.....");
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext("planEmployee",getLoginUser());
		// pager=productplanmasterService.findPager(objectQuery);
		return SUCCESS;
	}

	public String listjson() {
		Pager<ProductPlanDeliveryMaster> pager = productPlanDeliveryService.findPager(objectQuery);
		Pager<ProductPlanDeliveryMaster> pms=new Pager<ProductPlanDeliveryMaster>(pager.getPageNo(), pager.getPageSize(), pager.getTotalRows());
		for(ProductPlanDeliveryMaster master:pager.getRows()){
			pms.getRows().add((ProductPlanDeliveryMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		writejson(pms);
		return null;
	}

	public String listall() {
		List<ProductPlanDeliveryMaster> allMasters = productPlanDeliveryService.getAll();
		List<ProductPlanDeliveryMaster> allPMs=new ArrayList<ProductPlanDeliveryMaster>();
		for(ProductPlanDeliveryMaster master:allMasters){
			allPMs.add((ProductPlanDeliveryMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}

	public String listallUnFinished() {
		actionJsonResult = new ActionJsonResult(productPlanDeliveryService.findAllUnFinished());
		return JSONALL;
	}

	public String listallFinished() {
		actionJsonResult = new ActionJsonResult(productPlanDeliveryService.findAllFinished());
		return JSONALL;
	}

	public String show() {
		return SHOW;
	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("Productplanmaster save.....");
		try {
			if (StringUtils.isNotBlank(id)) {
				productPlanDeliveryService.update(productPlanDeliveryMaster);
			} else {
				productPlanDeliveryService.save(productPlanDeliveryMaster);
			}
			actionJsonResult = new ActionJsonResult(productPlanDeliveryMaster);
		} catch (Exception e) {
			actionJsonResult = new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	public String complete() {
		if (StringUtils.isNotBlank(id)) {
			productPlanDeliveryMaster = productPlanDeliveryService.getById(id);
//			productPlanDeliveryService.changeState(productPlanDeliveryMaster, State.COMPLETE);
			productPlanDeliveryService.update(productPlanDeliveryMaster);
			actionJsonResult = new ActionJsonResult(productPlanDeliveryMaster);
		}
		return JSONLIST;
	}

	public String process() {
		if (StringUtils.isNotBlank(id)) {
			productPlanDeliveryMaster = productPlanDeliveryService.getById(id);
//			productPlanDeliveryService.changeState(productPlanDeliveryMaster, State.PROCESS);
			productPlanDeliveryService.update(productPlanDeliveryMaster);
			actionJsonResult = new ActionJsonResult(productPlanDeliveryMaster);
		}
		return JSONLIST;
	}

	public String suspend() {
		if (StringUtils.isNotBlank(id)) {
			productPlanDeliveryMaster = productPlanDeliveryService.getById(id);
//			productPlanDeliveryService.changeState(productPlanDeliveryMaster, State.SUSPEND);
			productPlanDeliveryService.update(productPlanDeliveryMaster);
			actionJsonResult = new ActionJsonResult(productPlanDeliveryMaster);
		}
		return JSONLIST;
	}

	public String discard() {
		if (StringUtils.isNotBlank(id)) {
			productPlanDeliveryMaster = productPlanDeliveryService.getById(id);
//			productPlanDeliveryService.changeState(productPlanDeliveryMaster, State.DISCARD);
			productPlanDeliveryService.update(productPlanDeliveryMaster);
			actionJsonResult = new ActionJsonResult(productPlanDeliveryMaster);
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("Productplanmaster delete.....");
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			try {
				productPlanDeliveryService.delete(id);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return NONE;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				// 新增的时候不需要初始化，保存的时候要有个对象保存值
				productPlanDeliveryMaster = new ProductPlanDeliveryMaster();
			}
		} else {
			productPlanDeliveryMaster = productPlanDeliveryService.getById(id);
			if (isEditSave()) {
				productPlanDeliveryMaster.setExecuteEmployee(null);
				productPlanDeliveryMaster.setPlanEmployee(null);
				productPlanDeliveryMaster.getDetails().clear();
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	public String viewData() throws IOException {
		productPlanDeliveryMaster = productPlanDeliveryService.getById(id);
		writejson(productPlanDeliveryMaster.toDisp(true));
		return null;
	}

	/**
	 * 生成单据编号
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getItem() throws IOException {
		actionJsonResult = new ActionJsonResult((Object)((ItemGenAble) productPlanDeliveryService).generateItem(productPlanDeliveryMaster.getBillDate()));
		return JSONLIST;
	}

}
