/*
 * 捷利商业进销存管理系统
 * @(#)InventoryLossMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;

import cn.jely.cd.domain.InventoryLossMaster;
import cn.jely.cd.service.IInventoryLossService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.web.JQGridAction;

/**
 * @ClassName:InventoryLossMasterAction
 * @Description:Action
 * @author
 * @version 2013-09-04 10:41:32 
 *
 */
public class InventoryLossAction extends JQGridAction<InventoryLossMaster> {//有针对树的操作需继承自TreeOperateAction
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private InventoryLossMaster inventoryLossMaster = new InventoryLossMaster();
	private IInventoryLossService inventoryLossService;
	private Pager<InventoryLossMaster> pager;

	public void setInventoryLossService(IInventoryLossService inventoryLossService) {
		this.inventoryLossService = inventoryLossService;
	}

	public Pager<InventoryLossMaster> getPager() {
		return pager;
	}

	@Override
	public InventoryLossMaster getModel() {
		return inventoryLossMaster;
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
		pager=inventoryLossService.findPager(objectQuery);
		List<InventoryLossMaster> rows = pager.getRows();
		for (InventoryLossMaster master : rows) {
			master=(InventoryLossMaster) master.toDisp(false);
		}
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		List<InventoryLossMaster> allils = inventoryLossService.getAll();
		for (InventoryLossMaster master : allils) {
			master=(InventoryLossMaster) master.toDisp(false);
		}
		actionJsonResult=new ActionJsonResult(allils);
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				inventoryLossService.update(inventoryLossMaster);
			}else{
				inventoryLossService.save(inventoryLossMaster);
			}
			actionJsonResult=new ActionJsonResult(inventoryLossMaster.toDisp(true));
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			inventoryLossService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				inventoryLossMaster =new InventoryLossMaster();
//			}
		}else{
			inventoryLossMaster=inventoryLossService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				inventoryLossMaster.getDetails().clear();
				inventoryLossMaster.setEmployee(null);
				inventoryLossMaster.setWarehouse(null);
			}
		}
	}

	/** 获取编号 */
	public String getItem() {
		actionJsonResult = new ActionJsonResult((Object)((ItemGenAble) inventoryLossService).generateItem(inventoryLossMaster.getBillDate()));
		return JSONLIST;
	}

	public String viewData() {
		inventoryLossMaster = inventoryLossService.getById(id);
		actionJsonResult = new ActionJsonResult((Object) inventoryLossMaster.toDisp(true));
		return JSONLIST;
	}
}
