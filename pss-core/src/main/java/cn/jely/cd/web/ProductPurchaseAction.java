/*
 * 捷利商业进销存管理系统
 * @(#)ProductPurchaseMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.domain.ProductPurchaseReturnMaster;
import cn.jely.cd.service.IProductPurchaseService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:ProductPurchaseMasterAction
 * @Description:Action
 * @author
 * @version 2013-06-21 14:54:28
 * 
 */
public class ProductPurchaseAction extends JQGridAction<ProductPurchaseMaster> {// 有针对树的操作需继承自TreeOperateAction
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private ProductPurchaseMaster productPurchaseMaster = new ProductPurchaseMaster();
	private IProductPurchaseService productPurchaseService;

	public void setProductPurchaseService(IProductPurchaseService productPurchaseMasterService) {
		this.productPurchaseService = productPurchaseMasterService;
	}

	@Override
	public ProductPurchaseMaster getModel() {
		return productPurchaseMaster;
	}

	@Override
	public String list() {
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		return SUCCESS;
	}

	@Override
	public String listjson() {
		Pager<ProductPurchaseMaster> pager = productPurchaseService.findPager(objectQuery);
		Pager<ProductPurchaseMaster> pms = new Pager<ProductPurchaseMaster>(pager.getPageNo(), pager.getPageSize(),
				pager.getTotalRows());
		for (ProductPurchaseMaster master : pager.getRows()) {
			pms.getRows().add((ProductPurchaseMaster) master.toDisp(false));
		}
		actionJsonResult = new ActionJsonResult(pms);
		writejson(pms);
		return null;
	}

	public String findunfinished() {
		parseCondition();
		List<ProductPurchaseMaster> unCompletes = productPurchaseService.findAllUnFinished(objectQuery);
		List<ProductPurchaseMaster> pmrows=new ArrayList<ProductPurchaseMaster>();
		for(ProductPurchaseMaster master:unCompletes){
			pmrows.add((ProductPurchaseMaster) master.toDisp(false));
		}
		Pager<ProductPurchaseMaster> uncompletePager = new Pager<ProductPurchaseMaster>(1, unCompletes.size(),
				unCompletes.size(), 1l, unCompletes);
		actionJsonResult = new ActionJsonResult(uncompletePager);
		return JSONLIST;
	}

	public String findunfinishedwithreturn(){
		parseCondition();
		List<ProductCommonMaster> allunCompletes = productPurchaseService.findUnFinishedWithReturn(objectQuery);
		List<ProductCommonMaster> unCompletes = new ArrayList<ProductCommonMaster>();
		for (ProductCommonMaster master : allunCompletes) {
			ProductCommonMaster pcmPM = master.toDisp(false);
			if (master instanceof ProductPurchaseReturnMaster) {
				BigDecimal multiplicand = new BigDecimal("-1");
				pcmPM.setAmount(pcmPM.getAmount().multiply(multiplicand));
				pcmPM.setPaid(pcmPM.getPaid().multiply(multiplicand));
				pcmPM.setArap(pcmPM.getArap().multiply(multiplicand));
				pcmPM.setPaidArap(pcmPM.getPaidArap().multiply(multiplicand));
			}
			unCompletes.add(pcmPM);
		}
		Pager<ProductCommonMaster> uncompletePager = new Pager<ProductCommonMaster>(1, unCompletes.size(),
				unCompletes.size(), 1l, unCompletes);
		actionJsonResult = new ActionJsonResult(uncompletePager);
		return JSONLIST;
	}
	@Override
	public String listall() {
		List<ProductPurchaseMaster> allMasters = productPurchaseService.getAll();
		List<ProductPurchaseMaster> allPMs = new ArrayList<ProductPurchaseMaster>();
		for (ProductPurchaseMaster master : allMasters) {
			allPMs.add((ProductPurchaseMaster) master.toDisp(false));
		}
		actionJsonResult = new ActionJsonResult(allPMs);
		return JSONALL;
	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	@Override
	public String save() {
		try {
			if (StringUtils.isNotBlank(id)) {
				productPurchaseService.update(productPurchaseMaster);
			} else {
				productPurchaseService.save(productPurchaseMaster);
			}
			actionJsonResult = new ActionJsonResult(productPurchaseMaster);
		} catch (Exception e) {
			actionJsonResult = new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productPurchaseService.delete(id);
		}
		actionJsonResult = new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				// 新增的时候不需要初始化，保存的时候要有个对象保存值
//				productPurchaseMaster = new ProductPurchaseMaster();
//			}
		} else {
			productPurchaseMaster = productPurchaseService.getById(id);
			if (isEditSave()) {
				productPurchaseMaster.getDetails().clear();
				productPurchaseMaster.setBusinessUnit(null);
				productPurchaseMaster.setFundAccount(null);
				productPurchaseMaster.setWarehouse(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
	
	public void prepareGetItem(){
		beforInputSave();
	}
	/** 获取编号 */
	public String getItem() {
		actionJsonResult = new ActionJsonResult((Object)((ItemGenAble) productPurchaseService).generateItem(productPurchaseMaster.getBillDate()));
		return JSONLIST;
	}

	public String viewData() {
		productPurchaseMaster = productPurchaseService.getById(id);
		writejson(productPurchaseMaster.toDisp(true));
		return null;
	}
}
