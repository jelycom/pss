/*
 * 捷利商业进销存管理系统
 * @(#)ProductType.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.ProductType;
import cn.jely.cd.service.IProductTypeService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductTypeServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2012-11-30 14:56:06 
 *
 */
public class ProductTypeServiceTest extends BaseServiceTest{

	private IProductTypeService productTypeService;

	@Resource(name = "productTypeService")
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}

	@Test
	public void testSave() {
		ProductType productType = new ProductType("产品分类"); //如果出错请把字符串长度改短
		productType.setItem("CP001");
		productTypeService.save(productType, null);
		productType=productTypeService.findQueryObject(new ObjectQuery().addWhere("name=:name","name", "产品分类"));
		Long rootId=productType.getId();
		for (int i = 0; i < 3; i++) {
			ProductType productType2 = new ProductType();
			String name = "子分类"+i;
			productType2.setName(name);
			productTypeService.save(productType2,rootId);
//			ProductType findproductType2 = productTypeService.findQueryObject(new ObjectQuery("o.name=?", name));//TODO:Bug
			Long product2Id=productType2.getId();
			for(int j=0;j<3;j++){
				ProductType productType3 = new ProductType();
				productType3.setName("子"+i+"孙类"+j);
				productTypeService.save(productType3,product2Id);
			}
		}
	}
//	@Test
//	public void testSave2() {
//		ProductType productType = new ProductType("硬盘"); //如果出错请把字符串长度改短
////		productType.setItem("CP001");
//		ProductType parentType = productTypeService.findQueryObject(new ObjectQuery(" o.name=?", "电脑配件"));
//		productTypeService.save(productType, parentType==null?null:parentType.getId());
//		printList(productTypeService.findAll(null));
//	}

	@Test
	public void testUpdate() {
		ProductType productType = (ProductType) getRandomObject(productTypeService.getAll());
		String oldstr=productType.getName();
		productType.setName("u"+oldstr);
		productTypeService.update(productType);
		ObjectQuery objectQuery = new ObjectQuery().addWhere("name=:name","name", oldstr);
		ProductType productType2 = productTypeService.findQueryObject(objectQuery);
		Assert.assertNull(productType2);
	}


	@Test
	public void testGetAll() {
		List<ProductType> productTypes=productTypeService.getAll();
		Assert.assertTrue(productTypes.size()>0);
	}

	@Test
	public void testFindPager() {
		Pager<ProductType> pager=productTypeService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

	@Test
	public void testMoveChildForward(){
//		printList(productTypeService.getAll());
//		ProductType productType=productTypeService.findQueryObject(new ObjectQuery("name =?", "子0孙类0"));
//		ProductType parentType=productTypeService.findQueryObject(new ObjectQuery("name =?", "子分类1"));
//		productTypeService.MoveIn(productType.getId(), parentType.getId());
//		printList(productTypeService.getAll());
	}
	
	@Test
	public void testMoveChildBackward(){
//		ProductType productType=productTypeService.findQueryObject(new ObjectQuery("name =?", "子0孙类0"));
//		ProductType parentType=productTypeService.findQueryObject(new ObjectQuery("name =?", "子分类0"));
//		productTypeService.MoveIn(productType.getId(), parentType.getId());
	}
}