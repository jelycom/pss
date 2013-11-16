/*
 * 捷利商业进销存管理系统
 * @(#)Product.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.export.util.ExportUtil;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IProductTypeService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.Pager;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * @ClassName:ProductAction
 * @Description:Action
 * @author
 * @version 2012-11-30 16:26:19 
 *
 */
public class ProductAction extends JQGridAction<Product> {
	private Product product;
	private IProductService productService;
	private IProductTypeService productTypeService;
	private Pager<Product> pager;
	private List<Product> rows;
	
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}

	public Pager<Product> getPager() {
		return pager;
	}

	public List<Product> getRows() {
		return rows;
	}
	
	
	@Override
	public Product getModel() {
		return product;
	}

	@Override
	public String list() {
		logger.debug("Product list.....");
		StringBuilder costMethodBuilder=new StringBuilder();
		for(CostMethod costMethod:CostMethod.values()){
			if(costMethod.ordinal()>0){
				costMethodBuilder.append(";");
			}
			costMethodBuilder.append(costMethod.name()).append(":").append(costMethod.getMethodName());
		}
		//将需要在列表页面展示的关联对象放入Context；
		putContext("costMethod",costMethodBuilder.toString());
		//pager=productService.findPager(objectQuery);
		
	//	Product product = productService.findCode();

		return SUCCESS;
	}
	public String listjson(){
		pager=productService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	public String listall(){
		actionJsonResult=new ActionJsonResult(productService.getAll());
		return JSONALL;
	}
	public void downloadtemplate(){
		String editType = getRequest().getParameter("editType");//是否需要编辑类别
		Boolean editProductType = false;
		if(StringUtils.isNotBlank(editType)){
			editProductType = Boolean.parseBoolean(editType);
		}
		byte[] content;
		try {
			content = productService.getImportTemplate(editProductType);
			ExportUtil.downFile(getResponse(), "productTemplate.xlsx", content);
		} catch (IOException exception) {
			exception.printStackTrace();
			writejson(new ActionJsonResult(false, exception.getMessage()));
		}
	}
//	public String showjson(){
//		StringBuffer sid = new StringBuffer(id+",");
//		List list = getChildId(sid);
//		StringBuffer wid = new StringBuffer();
//		for (int i = 0; i < list.size(); i++) {
//			wid.append(list.get(i) + ",");
//		}
//		wid.append(id);
//		rows = productService.findChild("producttype.id", wid);
//		return JSONTREE;
//	}
	
//	public List getChildId(StringBuffer id) {
//		List<Producttype> producttypes = producttypeService.findChild("parent.id", id);
//		for (Producttype producttype : producttypes) {
//			listTotal.add(producttype.getId());
//		}
//		StringBuffer rid = new StringBuffer();
//		for (int i = 0; i < listTotal.size(); i++) {
//			rid.append(listTotal.get(i) + ",");
//		}
//		showChildByChild(producttypes);
//		return listTotal;
//	}
//	
//	public void showChildByChild(List<Producttype> producttypes) {
//		StringBuffer s = new StringBuffer();
//		for (Producttype producttype : producttypes) {
//			if (isHasChild(producttype) == true) {
//				s.append(producttype.getId() + ",");
//			}
//		}
//		if (s.length() > 0) {
//			getChildId(s);
//		}
//	}
//	
//	public boolean isHasChild(ProductType producttype) {
//		if (producttypeService.getById(producttype.getId().toString()) != null) {
//			return true;
//		}
//		return false;
//	}
	
//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="fullName",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("Product save.....");
		try{
			if (StringUtils.isNotBlank(id)) {
				productService.update(product);			
			}else{
				productService.save(product);
			}
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(false,e.getMessage());
			return JSONLIST;
		}
		actionJsonResult=new ActionJsonResult(product);
		return JSONALL;
	}

	@Override
	public String delete() {
		logger.debug("Product delete.....");
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			productService.delete(id);
		} 
		actionJsonResult=new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				product =new Product();
			}
		}else{
				product=productService.getById(id);
			if (isEditSave()) {
				product.setProductType(null);
				product.setCostMethod(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
	
//	public String query() throws IOException {
//		List<Product> products = productService.find(value);
//		for (Product product : products) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("name", product.getFullName());
//			map.put("id", product.getId());
//			map.put("item", product.getItem());
////			list.add(map);
//		}
//		return JSONfUZZY;
//	}

	public String genItem(){
		actionJsonResult=new ActionJsonResult((Object)productService.generateItem(productTypeService.getById(pid)));
		return JSONLIST;
	}

}
