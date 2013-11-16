/*
 * 捷利商业进销存管理系统
 * @(#)ProductDeliveryMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.ProductDeliveryReturnMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.service.IProductDeliveryService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * @ClassName:ProductDeliveryMasterAction
 * @author
 * @version 2013-06-21 14:54:28 
 *
 */
public class ProductDeliveryAction extends JQGridAction<ProductDeliveryMaster> {//有针对树的操作需继承自TreeOperateAction
	private ProductDeliveryMaster productDeliveryMaster=new ProductDeliveryMaster();
	private IProductDeliveryService productDeliveryService;

	public void setProductDeliveryService(IProductDeliveryService productDeliveryService) {
		this.productDeliveryService = productDeliveryService;
	}


	@Override
	public ProductDeliveryMaster getModel() {
		return productDeliveryMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		Pager<ProductDeliveryMaster> pager=productDeliveryService.findPager(objectQuery);
		Pager<ProductDeliveryMaster> pms=new Pager<ProductDeliveryMaster>(pager.getPageNo(),pager.getPageSize(),pager.getTotalRows());
		for(ProductDeliveryMaster master:pager.getRows()){
			pms.getRows().add((ProductDeliveryMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		writejson(pms);
		return null;
	}
	
	public String findunfinished(){
		parseCondition();
		List<ProductDeliveryMaster> unCompletes=productDeliveryService.findAllUnFinished(objectQuery);
		List<ProductDeliveryMaster> pmrows=new ArrayList<ProductDeliveryMaster>();
		for(ProductDeliveryMaster master:unCompletes){
			pmrows.add((ProductDeliveryMaster) master.toDisp(false));
		}
		Pager<ProductDeliveryMaster> uncompletePager=new Pager<ProductDeliveryMaster>(1, pmrows.size(), pmrows.size(), 1, pmrows);
		actionJsonResult=new ActionJsonResult(uncompletePager);
		return JSONLIST;
	}
	
	public String findunfinishedwithreturn(){
		parseCondition();
		List<ProductCommonMaster> allunCompletes = productDeliveryService.findUnFinishedWithReturn(objectQuery);
		List<ProductCommonMaster> unCompletes = new ArrayList<ProductCommonMaster>();
		for (ProductCommonMaster master : allunCompletes) {
			ProductCommonMaster pcmPM = master.toDisp(false);
			if (master instanceof ProductDeliveryReturnMaster) {
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
		List<ProductDeliveryMaster> allMasters = productDeliveryService.getAll();
		List<ProductDeliveryMaster> allPMs=new ArrayList<ProductDeliveryMaster>();
		for(ProductDeliveryMaster master:allMasters){
			allPMs.add((ProductDeliveryMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				productDeliveryService.update(productDeliveryMaster);
			}else{
				productDeliveryService.save(productDeliveryMaster);
			}
			actionJsonResult=new ActionJsonResult(productDeliveryMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productDeliveryService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				productDeliveryMaster =new ProductDeliveryMaster();
//			}
		}else{
			productDeliveryMaster=productDeliveryService.getById(id);
			if (isEditSave()) {
				productDeliveryMaster.getDetails().clear();
				productDeliveryMaster.setBusinessUnit(null);
				productDeliveryMaster.setFundAccount(null);
				productDeliveryMaster.setWarehouse(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
	/** 获取编号 */
	public String getItem() {
		actionJsonResult=new ActionJsonResult((Object)((ItemGenAble)productDeliveryService).generateItem(productDeliveryMaster.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData() {
		productDeliveryMaster=productDeliveryService.getById(id);
		writejson(productDeliveryMaster.toDisp(true));
		return null;
//		productDeliveryMaster=productDeliveryService.getById(id);
//		Pattern p1=Pattern.compile("\\S*mployee.roles");
//		Pattern p2=Pattern.compile("details\\[\\d+\\].product.productType");
//		Pattern p3=Pattern.compile("info");
//		Pattern p4=Pattern.compile("contactor.business\\S*");
//		java.util.Collection<Pattern> execlude=Arrays.asList(new Pattern []{p1,p2,p3,p4});
//		actionJsonResult=new ActionJsonResult((Object)productDeliveryMaster);
//		writeJson(actionJsonResult, execlude, null, true);
////		actionJsonResult=new ActionJsonResult(productPlanDeliveryMaster);
//		return null;
	}
}
