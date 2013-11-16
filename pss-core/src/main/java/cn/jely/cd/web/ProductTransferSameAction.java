/*
 * 捷利商业进销存管理系统
 * @(#)ProductTransferMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.ProductTransferSameMaster;
import cn.jely.cd.service.IProductTransferSameService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;

/**
 * 同价调拨相关控制层
 * @ClassName:ProductTransferMasterAction
 * @author
 * @version 2013-07-17 14:54:28 
 *
 */
public class ProductTransferSameAction extends JQGridAction<ProductTransferSameMaster> {//有针对树的操作需继承自TreeOperateAction
	private ProductTransferSameMaster productTransferSameMaster=new ProductTransferSameMaster();
	private IProductTransferSameService productTransferSameService;

	public void setProductTransferSameService(IProductTransferSameService productTransferSameService) {
		this.productTransferSameService = productTransferSameService;
	}

	@Override
	public ProductTransferSameMaster getModel() {
		return productTransferSameMaster;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		Pager<ProductTransferSameMaster> masterpg=productTransferSameService.findPager(objectQuery);
		Pager<ProductTransferSameMaster> pager=new Pager<ProductTransferSameMaster>(masterpg.getPageNo(),masterpg.getPageSize(),masterpg.getTotalRows());
		for(ProductTransferSameMaster master:masterpg.getRows()){
			pager.getRows().add((ProductTransferSameMaster) master.toDisp(false));
		}
//		actionJsonResult=new ActionJsonResult(pager);
		writejson(pager);
		return null;
	}
	
	@Override
	public String listall() {
		List<ProductTransferSameMaster> allMasters = productTransferSameService.getAll();
		List<ProductTransferSameMaster> allPMs=new ArrayList<ProductTransferSameMaster>();
		for(ProductTransferSameMaster master:allMasters){
			allPMs.add((ProductTransferSameMaster) master.toDisp(false));
		}
		actionJsonResult=new ActionJsonResult(allPMs);
		return JSONALL;
	}
	
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				productTransferSameService.update(productTransferSameMaster);
			}else{
				productTransferSameService.save(productTransferSameMaster);
			}
			actionJsonResult=new ActionJsonResult(productTransferSameMaster);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productTransferSameService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
//			if (isEditSave()) {
//				//新增的时候不需要初始化，保存的时候要有个对象保存值
//				productTransferSameMaster =new ProductTransferSameMaster();
//			}
		}else{
			productTransferSameMaster=productTransferSameService.getById(id);
			if (isEditSave()) {
				productTransferSameMaster.getDetails().clear();
				productTransferSameMaster.setInWarehouse(null);
				productTransferSameMaster.setOutWarehouse(null);
				productTransferSameMaster.setInEmployee(null);
				productTransferSameMaster.setOutEmployee(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
	/** 获取编号 */
	public String getItem() {
		actionJsonResult=new ActionJsonResult((Object)((ItemGenAble)productTransferSameService).generateItem(productTransferSameMaster.getBillDate()));
		return JSONLIST;
	}
	
	public String viewData() {
		productTransferSameMaster=productTransferSameService.getById(id);
		writejson(productTransferSameMaster.toDisp(true));
		return null;
//		productTransferMaster=productTransferService.getById(id);
//		Pattern p1=Pattern.compile("\\S*mployee.roles");
//		Pattern p2=Pattern.compile("details\\[\\d+\\].product.productType");
//		Pattern p3=Pattern.compile("info");
//		Pattern p4=Pattern.compile("contactor.business\\S*");
//		java.util.Collection<Pattern> execlude=Arrays.asList(new Pattern []{p1,p2,p3,p4});
//		actionJsonResult=new ActionJsonResult((Object)productTransferMaster);
//		writeJson(actionJsonResult, execlude, null, true);
////		actionJsonResult=new ActionJsonResult(productPlanTransferMaster);
//		return null;
	}
}
