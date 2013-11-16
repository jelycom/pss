/*
 * 捷利商业进销存管理系统
 * @(#)Productplanmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.domain.ProductPlanDetail;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.pagemodel.ProductPlanMasterPM;
import cn.jely.cd.service.IProductPlanPurchaseService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductPurchasePlanMasterAction
 * @Description:Action
 * @author 周义礼
 * @version 2012-12-05 10:34:59 
 *
 */
public class ProductPlanPurchaseAction extends JQGridAction<ProductPlanPurchaseMaster> {
	private ProductPlanPurchaseMaster productPlanPurchaseMaster=new ProductPlanPurchaseMaster();
	private IProductPlanPurchaseService productPlanPurchaseService;
	private List<ProductPlanDetail> details=new ArrayList<ProductPlanDetail>();

	public void setProductPlanPurchaseService(IProductPlanPurchaseService productplanService) {
		this.productPlanPurchaseService = productplanService;
	}

	@Override
	public ProductPlanPurchaseMaster getModel() {
		return productPlanPurchaseMaster;
	}
	
	public List<ProductPlanDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductPlanDetail> details) {
		this.details = details;
	}

	@Override
	public String list() {
		logger.debug("Productplanmaster list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext("planEmployee",getLoginUser());
		//pager=productplanmasterService.findPager(objectQuery);
		return SUCCESS;
	}
	public String listjson(){
		Pager<ProductPlanPurchaseMaster> pager=productPlanPurchaseService.findPager(objectQuery);
		Pager<ProductPlanPurchaseMaster> pms=new Pager<ProductPlanPurchaseMaster>(pager.getPageNo(), pager.getPageSize(), pager.getTotalRows());
		for(ProductPlanPurchaseMaster master:pager.getRows()){
			pms.getRows().add((ProductPlanPurchaseMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(pms);
		writejson(pms);
		return null;
	}
	public String listall(){
		List<ProductPlanPurchaseMaster> allMasters = productPlanPurchaseService.getAll();
		List<ProductPlanPurchaseMaster> allPMs=new ArrayList<ProductPlanPurchaseMaster>();
		for(ProductPlanPurchaseMaster master:allMasters){
			allPMs.add((ProductPlanPurchaseMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}
	
	public String listallUnFinished(){
		actionJsonResult=new ActionJsonResult(productPlanPurchaseService.findAllUnFinished());
		return JSONALL;
	}
	public String listallFinished(){
		actionJsonResult=new ActionJsonResult(productPlanPurchaseService.findAllFinished());
		return JSONALL;
	}
	
	public String show(){
		return SHOW;
	}
	
	
//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("Productplanmaster save.....");
		try{
			if (StringUtils.isNotBlank(id)) {
				productPlanPurchaseService.update(productPlanPurchaseMaster);
			}else{
				productPlanPurchaseService.save(productPlanPurchaseMaster);
			}
			actionJsonResult=new ActionJsonResult((Object)productPlanPurchaseMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	public String complete(){
		if(StringUtils.isNotBlank(id)){
			productPlanPurchaseMaster=productPlanPurchaseService.getById(id);
//			productPlanPurchaseService.changeState(productPlanPurchaseMaster, State.COMPLETE);
			productPlanPurchaseService.update(productPlanPurchaseMaster);
			actionJsonResult=new ActionJsonResult(productPlanPurchaseMaster);
		}
		return JSONLIST;
	}
	
	public String process(){
		if(StringUtils.isNotBlank(id)){
			productPlanPurchaseMaster= productPlanPurchaseService.getById(id);
//			productPlanPurchaseService.changeState(productPlanPurchaseMaster, State.PROCESS);
			productPlanPurchaseService.update(productPlanPurchaseMaster);
			actionJsonResult=new ActionJsonResult(productPlanPurchaseMaster);
		}
		return JSONLIST;
	}
	public String suspend(){
		if(StringUtils.isNotBlank(id)){
			productPlanPurchaseMaster=productPlanPurchaseService.getById(id);
//			productPlanPurchaseService.changeState(productPlanPurchaseMaster, State.SUSPEND);
			productPlanPurchaseService.update(productPlanPurchaseMaster);
			actionJsonResult=new ActionJsonResult(productPlanPurchaseMaster);
		}
		return JSONLIST;
	}
	public String discard(){
		if(StringUtils.isNotBlank(id)){
			productPlanPurchaseMaster=productPlanPurchaseService.getById(id);
//			productPlanPurchaseService.changeState(productPlanPurchaseMaster, State.DISCARD);
			productPlanPurchaseService.update(productPlanPurchaseMaster);
			actionJsonResult=new ActionJsonResult(productPlanPurchaseMaster);
		}
		return JSONLIST;
	}
	@Override
	public String delete() {
		logger.debug("Productplanmaster delete.....");
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			try {
				productPlanPurchaseService.delete(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return NONE;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				productPlanPurchaseMaster =new ProductPlanPurchaseMaster();
//			}
		}else{
			productPlanPurchaseMaster=productPlanPurchaseService.getById(id);
			if (isEditSave()) {
				productPlanPurchaseMaster.setExecuteEmployee(null);
				productPlanPurchaseMaster.setPlanEmployee(null);
				productPlanPurchaseMaster.getDetails().clear();
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
	
	public String viewData() throws IOException {
		writejson(productPlanPurchaseService.getById(id).toDisp(true));
		return null;
//		productPlanPurchaseMaster=productPlanPurchaseService.getById(id);
//		Pattern p=Pattern.compile("\\S*mployee.roles");
//		Pattern p2=Pattern.compile("details\\[\\d+\\].product.productType");
//		java.util.Collection<Pattern> execlude=Arrays.asList(new Pattern []{p,p2});
//		actionJsonResult=new ActionJsonResult((Object)productPlanPurchaseMaster);
//		writeJson(actionJsonResult, execlude, null, true);
//		actionJsonResult=new ActionJsonResult(productPlanPurchaseMaster);
//		return null;
	}
	
	/**
	 * 生成单据编号
	 * @return
	 * @throws IOException
	 */
	public String getItem() throws IOException {
		actionJsonResult =new ActionJsonResult((Object)((ItemGenAble)productPlanPurchaseService).generateItem(productPlanPurchaseMaster.getBillDate()));
		return JSONLIST;
	}

}
