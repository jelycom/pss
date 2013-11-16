/*
 * 捷利商业进销存管理系统
 * @(#)ProductPurchaseReturnMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductPurchaseReturnMaster;
import cn.jely.cd.pagemodel.ProductCommonMasterPM;
import cn.jely.cd.service.IProductPurchaseReturnService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:ProductPurchaseReturnMasterAction
 * @author
 * @version 2013-07-17 14:54:28 
 *
 */
public class ProductPurchaseReturnAction extends JQGridAction<ProductPurchaseReturnMaster> {//有针对树的操作需继承自TreeOperateAction
	private ProductPurchaseReturnMaster productPurchaseReturnMaster = new ProductPurchaseReturnMaster();
	private IProductPurchaseReturnService productPurchaseReturnService;

	public void setProductPurchaseReturnService(IProductPurchaseReturnService productPurchaseReturnService) {
		this.productPurchaseReturnService = productPurchaseReturnService;
	}

	@Override
	public ProductPurchaseReturnMaster getModel() {
		return productPurchaseReturnMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		Pager<ProductPurchaseReturnMaster> pager=productPurchaseReturnService.findPager(objectQuery);
		Pager<ProductPurchaseReturnMaster> pms=new Pager<ProductPurchaseReturnMaster>(pager.getPageNo(),pager.getPageSize(),pager.getTotalRows());
		for(ProductPurchaseReturnMaster master:pager.getRows()){
			pms.getRows().add((ProductPurchaseReturnMaster) master.toDisp(false));
		}
//		actionJsonResult=new ActionJsonResult(pms);
		writejson(pms);
		return null;
	}
	
	@Override
	public String listall() {
		List<ProductPurchaseReturnMaster> allMasters = productPurchaseReturnService.getAll();
		List<ProductPurchaseReturnMaster> allPMs=new ArrayList<ProductPurchaseReturnMaster>();
		for(ProductPurchaseReturnMaster master:allMasters){
			allPMs.add((ProductPurchaseReturnMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}
	
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				productPurchaseReturnService.update(productPurchaseReturnMaster);
			}else{
				productPurchaseReturnService.save(productPurchaseReturnMaster);
			}
			actionJsonResult=new ActionJsonResult(productPurchaseReturnMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productPurchaseReturnService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				productPurchaseReturnMaster =new ProductPurchaseReturnMaster();
			}
		}else{
			productPurchaseReturnMaster=productPurchaseReturnService.getById(id);
			if (isEditSave()) {
				productPurchaseReturnMaster.getDetails().clear();
				productPurchaseReturnMaster.setBusinessUnit(null);
				productPurchaseReturnMaster.setFundAccount(null);
				productPurchaseReturnMaster.setWarehouse(null);
				productPurchaseReturnMaster.setEmployee(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
	/** 获取编号 */
	public String getItem() {
		actionJsonResult=new ActionJsonResult((Object)((ItemGenAble)productPurchaseReturnService).generateItem(productPurchaseReturnMaster.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData() {
		productPurchaseReturnMaster=productPurchaseReturnService.getById(id);
		writejson(productPurchaseReturnMaster.toDisp(true));
		return null;
//		productPurchaseReturnMaster=productPurchaseReturnService.getById(id);
//		Pattern p1=Pattern.compile("\\S*mployee.roles");
//		Pattern p2=Pattern.compile("details\\[\\d+\\].product.productType");
//		Pattern p3=Pattern.compile("info");
//		Pattern p4=Pattern.compile("contactor.business\\S*");
//		java.util.Collection<Pattern> execlude=Arrays.asList(new Pattern []{p1,p2,p3,p4});
//		actionJsonResult=new ActionJsonResult((Object)productPurchaseReturnMaster);
//		writeJson(actionJsonResult, execlude, null, true);
////		actionJsonResult=new ActionJsonResult(productPlanPurchaseReturnMaster);
//		return null;
	}
}
