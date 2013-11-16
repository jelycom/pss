/*
 * 捷利商业进销存管理系统
 * @(#)InventoryLossMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.InventoryLossMaster;
import cn.jely.cd.service.IInventoryLossService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:InventoryLossMasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-09-04 10:41:32 
 *
 */
public class InventoryLossServiceTest extends BaseServiceTest{

	private IInventoryLossService inventoryLossService;

	@Resource(name = "inventoryLossService")
	public void setInventoryLossService(IInventoryLossService inventoryLossService) {
		this.inventoryLossService = inventoryLossService;
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 10; i++) {
			InventoryLossMaster inventoryLossMaster = new InventoryLossMaster(); //如果出错请把字符串长度改短
//			InventoryLossMaster inventoryLossMaster2 = new InventoryLossMaster();
//			inventoryLossMaster2.setId(1l);
//			inventoryLossMaster.setParent(inventoryLossMaster2);
			inventoryLossService.save(inventoryLossMaster);
		}
	}

	@Test
	public void testUpdate() {
		InventoryLossMaster inventoryLossMaster = (InventoryLossMaster) getRandomObject(inventoryLossService.getAll());
//		String oldstr=inventoryLossMaster.getName();
//		inventoryLossMaster.setName("u"+oldstr);
//		inventoryLossMasterService.update(inventoryLossMaster);
//		InventoryLossMaster inventoryLossMaster2 = inventoryLossMasterService.getById(inventoryLossMaster.getId());
//		String newstr=inventoryLossMaster2.getName();
//		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testFindPager() {
		
		Pager<InventoryLossMaster> pager=inventoryLossService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	

	@Test
	public void testDeleteAllInventoryLossMaster() {
		List<InventoryLossMaster> inventoryLossMasters=inventoryLossService.getAll();
		for(InventoryLossMaster inventoryLossMaster:inventoryLossMasters){
			inventoryLossService.delete(inventoryLossMaster.getId());
			
		}
		Assert.assertTrue(inventoryLossService.getAll().size()==0);
	}


	@Test
	public void testGenitem(){
		System.out.println(((ItemGenAble)inventoryLossService).generateItem(new Date()));
	}
}