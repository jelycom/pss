/*
 * 捷利商业进销存管理系统
 * @(#)Product.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.testng.annotations.Test;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IProductTypeService;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:ProductServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2012-11-30 16:26:19 
 *
 */
public class ProductServiceTest extends BaseServiceTest{

	private IProductService productService;
	private IProductTypeService productTypeService;

	@Resource(name = "productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	@Resource(name = "productTypeService")
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 10; i++) {
			ProductType productType = (ProductType) getRandomObject(productTypeService.getAll());
			Product product = new Product(productType,CostMethod.FIFO,productService.generateItem(productType),"Product"+i,"Full Name Product"+i); //如果出错请把字符串长度改短
			productService.save(product);
		}
		
	}

	@Test
	public void testsave1(){
		Product product=new Product(null,(ProductType) getRandomObject(productTypeService.getAll()),CostMethod.FIFO,"100000213123","测试产品","cscp","产品全名");
		productService.save(product);
		
	}
	@Test
	public void testUpdate() {
		Product product = (Product) getRandomObject(productService.getAll());
		String oldFullName=product.getFullName();
		String oldShortName=product.getShortName();
		product.setFullName("u"+oldFullName);
		product.setShortName("u"+oldShortName);
		productService.update(product);
		Product product2=productService.findQueryObject(new ObjectQuery().addWhere("fullName=:name","name", oldFullName));
		Assert.assertNull(product2);
	}

	@Test
	public void testGetAll() {
		List<Product> products=productService.getAll();
		Assert.assertTrue(products.size()>0);
	}

	@Test
	public void testFindPager() {
		Pager<Product> pager=productService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

	@Test
	public void testCheckExist(){
	}
	
	@Test
	public void testDownTemplate(){
		try {
			byte[] content = productService.getImportTemplate(false);
			FileOutputStream fos = new FileOutputStream(new File("f:/tddownload/product.xlsx"));
			fos.write(content);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}