/*
 * 捷利商业进销存管理系统
 * @(#)Productmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductOrderBillDeliveryMaster;
import cn.jely.cd.pagemodel.ProductOrderBillMasterPM;
import cn.jely.cd.service.IProductOrderBillDeliveryService;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:ProductOrderBillDeliveryAction
 * @Description:Action
 * @author
 * @version 2013-01-08 15:45:02 
 *
 */
//@Controller("DeliveryorderAction")
//@Scope("prototype")
public class ProductOrderBillDeliveryAction extends JQGridAction<ProductOrderBillDeliveryMaster> {
	private ProductOrderBillDeliveryMaster master=new ProductOrderBillDeliveryMaster();
	private IProductOrderBillDeliveryService productOrderBillDeliveryService;
	private IStateResourceOPService stateResourceOPService;
	private IActionResourceService actionResourceService;

	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}

	public void setProductOrderBillDeliveryService(IProductOrderBillDeliveryService productOrderBillDeliveryService) {
		this.productOrderBillDeliveryService = productOrderBillDeliveryService;
	}

	public void setStateResourceOPService(IStateResourceOPService stateResourceOPService) {
		this.stateResourceOPService = stateResourceOPService;
	}

	@Override
	public ProductOrderBillDeliveryMaster getModel() {
		return master;
	}

	@Override
	public String list() {
		logger.debug("Productmaster list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		//pager=productOrderBillDeliveryService.findPager(objectQuery);
		return SUCCESS;
	}
	public String listjson(){
		Pager<ProductOrderBillDeliveryMaster> pager=productOrderBillDeliveryService.findPager(objectQuery);
		Pager<ProductOrderBillDeliveryMaster> pms=new Pager<ProductOrderBillDeliveryMaster>(pager.getPageNo(),pager.getPageSize(),pager.getTotalRows());
		for(ProductOrderBillDeliveryMaster master:pager.getRows()){
			pms.getRows().add((ProductOrderBillDeliveryMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		writejson(pms);
		return null;
	}
	public String listall(){
		List<ProductOrderBillDeliveryMaster> allMasters = productOrderBillDeliveryService.getAll();
		List<ProductOrderBillDeliveryMaster> allPMs=new ArrayList<ProductOrderBillDeliveryMaster>();
		for(ProductOrderBillDeliveryMaster master:allMasters){
			allPMs.add((ProductOrderBillDeliveryMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}
	
	@Override
	public String input() {
		logger.debug("Productmaster input.....");
		//将需要在页面展示的关联对象放入Context；
		//putContext(key,value);		
		return INPUT;
	}
//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try {
			if(master.getContactor()!=null&&master.getContactor().getId()==null){
				master.setContactor(null);
			}
			if (StringUtils.isNotBlank(id)) {
				productOrderBillDeliveryService.update(master);
			} else {
				productOrderBillDeliveryService.save(master);
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
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productOrderBillDeliveryService.delete(id);
		}
		return NONE;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				master =new ProductOrderBillDeliveryMaster();
//			}
		}else{
			master=productOrderBillDeliveryService.getById(id);
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
		actionJsonResult=new ActionJsonResult((Object)((ItemGenAble)productOrderBillDeliveryService).generateItem(master.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData()  {
		writejson(productOrderBillDeliveryService.getById(id).toDisp(true));
		return null;
	}

	public String submit(){
		master=productOrderBillDeliveryService.getById(id);
		stateResourceOPService.getStateSequence(actionResourceService.findByLinkAddress(this.getClass().getName()+".list"));
//		master.setState(state);
		//TODO:和状态相关的操作涉及State,StateResourceOP等多个操作.
		return JSONLIST;
	}


}
