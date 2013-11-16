/*
 * 捷利商业进销存管理系统
 * @(#)InventoryProfitMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.ActionJsonResult;

import cn.jely.cd.domain.InventoryProfitMaster;
import cn.jely.cd.service.IInventoryProfitService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:InventoryProfitMasterAction
 * @Description:Action
 * @author
 * @version 2013-09-04 10:41:32 
 *
 */
public class InventoryProfitAction extends JQGridAction<InventoryProfitMaster> {//有针对树的操作需继承自TreeOperateAction
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private InventoryProfitMaster inventoryProfitMaster = new InventoryProfitMaster();
	private IInventoryProfitService inventoryProfitService;
	private Pager<InventoryProfitMaster> pager;

	public void setInventoryProfitService(IInventoryProfitService inventoryProfitService) {
		this.inventoryProfitService = inventoryProfitService;
	}

	public Pager<InventoryProfitMaster> getPager() {
		return pager;
	}

	@Override
	public InventoryProfitMaster getModel() {
		return inventoryProfitMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		parseCondition();
		pager=inventoryProfitService.findPager(objectQuery);
		for (InventoryProfitMaster master : pager.getRows()) {
			master=(InventoryProfitMaster) master.toDisp(false);
		}
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		List<InventoryProfitMaster> allips = inventoryProfitService.getAll();
		for (InventoryProfitMaster master : allips) {
			master=(InventoryProfitMaster) master.toDisp(false);
		}
		actionJsonResult=new ActionJsonResult(allips);
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				inventoryProfitService.update(inventoryProfitMaster);
			}else{
				inventoryProfitService.save(inventoryProfitMaster);
			}
			actionJsonResult=new ActionJsonResult(inventoryProfitMaster.toDisp(true));
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			inventoryProfitService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				inventoryProfitMaster =new InventoryProfitMaster();
//			}
		}else{
			inventoryProfitMaster=inventoryProfitService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				inventoryProfitMaster.getDetails().clear();
				inventoryProfitMaster.setEmployee(null);
				inventoryProfitMaster.setWarehouse(null);
			}
		}
	}

	/** 获取编号 */
	public String getItem() {
		actionJsonResult = new ActionJsonResult((Object)((ItemGenAble) inventoryProfitService).generateItem(inventoryProfitMaster.getBillDate()));
		return JSONLIST;
	}

	public String viewData() {
		inventoryProfitMaster = inventoryProfitService.getById(id);
		actionJsonResult = new ActionJsonResult((Object) inventoryProfitMaster.toDisp(true));
		return JSONLIST;
	}
}
