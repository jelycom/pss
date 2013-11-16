/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockingMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductStockingMaster;
import cn.jely.cd.service.IProductStockingService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:ProductStockingMasterAction
 * @Description:Action
 * @author
 * @version 2013-08-16 10:54:17 
 *
 */
public class ProductStockingAction extends JQGridAction<ProductStockingMaster> {//有针对树的操作需继承自TreeOperateAction
	
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private ProductStockingMaster productStockingMaster = new ProductStockingMaster();
	private IProductStockingService productStockingService;
	private Pager<ProductStockingMaster> pager;


	public void setProductStockingService(IProductStockingService productStockingService) {
		this.productStockingService = productStockingService;
	}

	public Pager<ProductStockingMaster> getPager() {
		return pager;
	}

	@Override
	public ProductStockingMaster getModel() {
		return productStockingMaster;
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
		pager=productStockingService.findPager(objectQuery);
//		Pager<ProductStockingMaster> psms=new Pager<ProductStockingMaster>(pager.getPageNo(), pager.getPageSize(), pager.getTotalRows(), pager.getTotalPages(), pager.getRows());
		for (ProductStockingMaster master : pager.getRows()) {
			master = master.toDisp(false);
		}
//		actionJsonResult=new ActionJsonResult(pager);
		writejson(pager);
		return null;
	}
	
	@Override
	public String listall() {
		List<ProductStockingMaster> allpsms = productStockingService.getAll();
		for (ProductStockingMaster master : allpsms) {
			master = master.toDisp(false);
		}		
		actionJsonResult=new ActionJsonResult(allpsms);
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				productStockingService.update(productStockingMaster);
			}else{
				productStockingService.save(productStockingMaster);
			}
			actionJsonResult=new ActionJsonResult(productStockingMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}
	
	/**
	 * 继续某些单据的盘点,必须是同一仓库的单据
	 */
	public String continueStocking(){
		try {
			productStockingMaster=productStockingService.continueStocking(id);
			actionJsonResult=new ActionJsonResult(productStockingMaster.toDisp(true));
		} catch (Exception e) {
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}
	
	@Override
	public String delete() {
//		if (StringUtils.isNotBlank(id)) {
//			productStockingMasterService.delete(id);
//		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				productStockingMaster =new ProductStockingMaster();
//			}
		}else{
			productStockingMaster=productStockingService.getById(id);
			if (isEditSave()) {
				productStockingMaster.setWarehouse(null);
				productStockingMaster.setEmployee(null);
				productStockingMaster.getDetails().clear();
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
	
	/** 获取编号 */
	public String getItem() {
		actionJsonResult = new ActionJsonResult((Object)((ItemGenAble) productStockingService).generateItem(productStockingMaster.getBillDate()));
		return JSONLIST;
	}

	public String viewData() {
		productStockingMaster = productStockingService.getById(id);
		writejson(productStockingMaster.toDisp(true));
		return null;
	}
	
}
