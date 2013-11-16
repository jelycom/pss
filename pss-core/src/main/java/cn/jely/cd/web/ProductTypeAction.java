/*
 * 捷利商业进销存管理系统
 * @(#)Producttype.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.ProductType;
import cn.jely.cd.service.IBaseTreeService;
import cn.jely.cd.service.IProductTypeService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ArrayConverter;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.TreeNode;

/**
 * @ClassName:ProducttypeAction
 * @Description:Action
 * @author
 * @version 2012-11-30 14:56:06
 * 
 */
@Controller("productTypeAction")
@Scope("prototype")
public class ProductTypeAction extends TreeOperateAction<ProductType> {
	private ProductType productType;
	private IProductTypeService productTypeService;
	private Pager<ProductType> pager;
	private List<ProductType> rows;

	@Resource(name = "productTypeService")
	public void setProducttypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}

	public Pager<ProductType> getPager() {
		return pager;
	}

	public List<ProductType> getRows() {
		return rows;
	}

	@Override
	public ProductType getModel() {
		return productType;
	}

	@Override
	public String list() {
		logger.debug("Producttype list.....");
		// 将需要在列表页面展示的关联对象放入Context；
		// putContext(key,value);
		// pager=productTypeService.findPager(objectQuery);
		return SUCCESS;
	}

	public String listjson() {
		pager = productTypeService.findPager(objectQuery);
		actionJsonResult = new ActionJsonResult(pager);
		return JSONALL;
	}

	public String listChildren() {
		List<ProductType> children = productTypeService.findTreeNodes(Long.valueOf(pid),true);
		actionJsonResult = new ActionJsonResult(children);
		return JSONALL;
	}

	public String treejson() throws Exception {
		// TreeNode node=null;
		// List<TreeNode> nodes=new ArrayList<TreeNode>();
		// if(id==null){
		// node=new TreeNode("-2", "商品分类");
		// rows=productTypeService.findAll(objectQuery);
		// createNodeList(nodes);
		// node.setChildren(nodes);
		// putContext("linked", new TreeNode[]{node});
		// }else{
		// // node=new TreeNode(id, "");
		// // rows=productTypeService.findChild("parent.id", new
		// StringBuffer().append(id).append(","));
		// createNodeList(nodes);
		// putContext("linked",nodes);
		// }
		// TreeNode [] nodes=new TreeNode[1];
		// nodes[0]=tnode1;
		// List<Map<String, Object>> listData = new
		// ArrayList<Map<String,Object>>();
		// List<Producttype> productTypes = productTypeService.findAll();
		// for (Producttype productType : productTypes) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("id", productType.getId().toString());
		// map.put("text", productType.getName());
		// map.put("name", productType.getId().toString());
		// if (productType.getParent() != null) {
		// map.put("parentId", productType.getParent().getId().toString());
		// }
		// listData.add(map);
		// }
		// Tree tree = new Tree();
		// tree.returnTree(listData);

		// return JSONALL;
		rows = productTypeService.findTreeNodes(null,true);
		int size = rows.size();
		pager = new Pager<ProductType>(1, size, size, 1, rows);
		actionJsonResult = new ActionJsonResult(pager);
		return JSONLIST;
	}

	public String listall() {
		actionJsonResult = new ActionJsonResult(productTypeService.getAll());
		return JSONALL;
	}

	// @Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	// @InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("Region save.....");
		if (StringUtils.isNotBlank(id)) {
			productTypeService.update(productType, Long.valueOf(pid));
		} else {
			if (StringUtils.isNotBlank(pid)) {
				productTypeService.save(productType, Long.valueOf(pid));
			} else {
				productTypeService.save(productType, null);
			}
		}
		actionJsonResult = new ActionJsonResult(productType);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("Producttype delete.....");
		// id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			Long [] ids=ArrayConverter.Strings2Longs(id.split(","));
			productTypeService.deleteCascade(ids);
		}
		actionJsonResult = new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				// 新增的时候不需要初始化，保存的时候要有个对象保存值
				productType = new ProductType();
			}
		} else {
			productType = productTypeService.getById(id);
			if (isEditSave()) {
				// productType.setParent(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	public String genItem() {
		actionJsonResult=new ActionJsonResult((Object)productTypeService.generateItem(productTypeService.getById(pid)));
		return JSONLIST;
	}

	@Override
	protected IBaseTreeService<ProductType> getTreeService() {
		return productTypeService;
	}
}
