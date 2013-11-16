/*
 * 捷利商业进销存管理系统
 * @(#)InventoryProfitMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.InventoryProfitMaster;
import cn.jely.cd.service.IInventoryProfitService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:InventoryProfitMasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-09-04 10:41:32 
 *
 */
public class InventoryProfitServiceTest extends BaseServiceTest{

	private IInventoryProfitService inventoryProfitService;

	@Resource(name = "inventoryProfitService")
	public void setInventoryProfitService(IInventoryProfitService inventoryProfitService) {
		this.inventoryProfitService = inventoryProfitService;
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 10; i++) {
			InventoryProfitMaster inventoryProfitMaster = new InventoryProfitMaster(); //如果出错请把字符串长度改短
//			InventoryProfitMaster inventoryProfitMaster2 = new InventoryProfitMaster();
//			inventoryProfitMaster2.setId(1l);
//			inventoryProfitMaster.setParent(inventoryProfitMaster2);
			inventoryProfitService.save(inventoryProfitMaster);
		}
	}

	@Test
	public void testUpdate() {
		InventoryProfitMaster inventoryProfitMaster = (InventoryProfitMaster) getRandomObject(inventoryProfitService.getAll());
//		String oldstr=inventoryProfitMaster.getName();
//		inventoryProfitMaster.setName("u"+oldstr);
//		inventoryProfitMasterService.update(inventoryProfitMaster);
//		InventoryProfitMaster inventoryProfitMaster2 = inventoryProfitMasterService.getById(inventoryProfitMaster.getId());
//		String newstr=inventoryProfitMaster2.getName();
//		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testFindPager() {
		
		Pager<InventoryProfitMaster> pager=inventoryProfitService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	

	@Test
	public void testDeleteAllInventoryProfitMaster() {
		List<InventoryProfitMaster> inventoryProfitMasters=inventoryProfitService.getAll();
		for(InventoryProfitMaster inventoryProfitMaster:inventoryProfitMasters){
			inventoryProfitService.delete(inventoryProfitMaster.getId());
			
		}
		Assert.assertTrue(inventoryProfitService.getAll().size()==0);
	}

	@Test
	public void testGenitem(){
		System.out.println(((ItemGenAble)inventoryProfitService).generateItem(new Date()));
	}
}