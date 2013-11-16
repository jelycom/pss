/*
 * 捷利商业进销存管理系统
 * @(#)Productmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductOrderBillPurchaseMaster;
import cn.jely.cd.pagemodel.ProductOrderBillMasterPM;
import cn.jely.cd.service.IProductOrderBillPurchaseService;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:ProductOrderBillPurchaseAction
 * @Description:Action
 * @author
 * @version 2013-01-08 15:45:02
 * 
 */
// @Controller("purchaseorderAction")
// @Scope("prototype")
public class ProductOrderBillPurchaseAction extends JQGridAction<ProductOrderBillPurchaseMaster> {
	private ProductOrderBillPurchaseMaster master=new ProductOrderBillPurchaseMaster();
	private IProductOrderBillPurchaseService productOrderBillPurchaseService;
	private IStateResourceOPService stateResourceOPService;
	private IActionResourceService actionResourceService;
	private Pager<ProductOrderBillPurchaseMaster> pager;

	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}

	public void setProductOrderBillPurchaseService(IProductOrderBillPurchaseService productOrderBillPurchaseService) {
		this.productOrderBillPurchaseService = productOrderBillPurchaseService;
	}

	public void setStateResourceOPService(IStateResourceOPService stateResourceOPService) {
		this.stateResourceOPService = stateResourceOPService;
	}

	public Pager<ProductOrderBillPurchaseMaster> getPager() {
		return pager;
	}

	@Override
	public ProductOrderBillPurchaseMaster getModel() {
		return master;
	}

	@Override
	public String list() {
		logger.debug("Productmaster list.....");
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		// pager=productOrderBillPurchaseService.findPager(objectQuery);
		return SUCCESS;
	}

	public String listjson() {
		Pager<ProductOrderBillPurchaseMaster> pager = productOrderBillPurchaseService.findPager(objectQuery);
		Pager<ProductOrderBillPurchaseMaster> pms=new Pager<ProductOrderBillPurchaseMaster>(pager.getPageNo(),pager.getPageSize(),pager.getTotalRows());
		for(ProductOrderBillPurchaseMaster master:pager.getRows()){
			pms.getRows().add((ProductOrderBillPurchaseMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		writejson(pms);
		return null;
	}

	public String listall() {
		List<ProductOrderBillPurchaseMaster> allMasters = productOrderBillPurchaseService.getAll();
		List<ProductOrderBillPurchaseMaster> allPMs=new ArrayList<ProductOrderBillPurchaseMaster>();
		for(ProductOrderBillPurchaseMaster master:allMasters){
			allPMs.add((ProductOrderBillPurchaseMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}
	
	public String findallunfinished(){
		parseCondition();
		List<ProductOrderBillPurchaseMaster> rows = productOrderBillPurchaseService.findAllUnFinished(objectQuery);
		List<ProductOrderBillPurchaseMaster> unfinisheds=new ArrayList<ProductOrderBillPurchaseMaster>();
		for(ProductOrderBillPurchaseMaster master:rows){
			unfinisheds.add((ProductOrderBillPurchaseMaster) master.toDisp(false));
		}
		pager=new Pager<ProductOrderBillPurchaseMaster>(1, unfinisheds.size(), unfinisheds.size(), 1, unfinisheds);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}
	@Override
	public String input() {
		logger.debug("Productmaster input.....");
		// 将需要在页面展示的关联对象放入Context；
		// putContext(key,value);
		return INPUT;
	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	@Override
	public String save() {
		try {
			if(master.getContactor()!=null&&master.getContactor().getId()==null){
				master.setContactor(null);
			}
			if (StringUtils.isNotBlank(id)) {
				productOrderBillPurchaseService.update(master);
			} else {
				productOrderBillPurchaseService.save(master);
			}
			actionJsonResult = new ActionJsonResult((Object)master);
		} catch (Exception e) {
			actionJsonResult = new ActionJsonResult(e.getMessage());
			// e.printStackTrace();
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productOrderBillPurchaseService.delete(id);
		}
		return NONE;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				// 新增的时候不需要初始化，保存的时候要有个对象保存值
//				master = new ProductOrderBillPurchaseMaster();
//			}
		} else {
			master = productOrderBillPurchaseService.getById(id);
			if (isEditSave()) {
				master.setBusinessUnit(null);
				master.setContactor(null);
				master.setWarehouse(null);
				master.setEmployee(null);
				master.getDetails().clear();
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	/** 获取编号 */
	public String getItem() {
		actionJsonResult = new ActionJsonResult((Object)((ItemGenAble) productOrderBillPurchaseService).generateItem(master.getBillDate()));
		return JSONLIST;
	}

	public String viewData()  {
		writejson(productOrderBillPurchaseService.getById(id).toDisp(true));
		return null;
	}

	public String submit() {
		master = productOrderBillPurchaseService.getById(id);
		stateResourceOPService.getStateSequence(actionResourceService.findByLinkAddress(this.getClass().getName()
				+ ".list"));
		// master.setState(state);
		// TODO:和状态相关的操作涉及State,StateResourceOP等多个操作.
		return JSONLIST;
	}

}
