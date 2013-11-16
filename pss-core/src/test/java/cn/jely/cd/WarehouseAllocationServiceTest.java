package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.domain.WarehouseAllocation;
import cn.jely.cd.service.IWarehouseAllocationService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

public class WarehouseAllocationServiceTest extends BaseServiceTest{

	private IWarehouseAllocationService warehouseAllocationService;
	private IWarehouseService warehouseService;


	@Resource(name = "warehouseAllocationService")
	public void setWarehouseAllocationService(IWarehouseAllocationService warehouseAllocationService) {
		this.warehouseAllocationService = warehouseAllocationService;
	}
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	@Test
	public void testSave() {
		Warehouse warehouse=warehouseService.getAll().get(0);
		for (int i = 0; i < 10; i++) {
			WarehouseAllocation warehouseAllocation = new WarehouseAllocation(warehouse,"Allocat"+i,"item"+i,true);
//			warehouseAllocation.setManager(new Employee(1l));
//			WarehouseAllocation warehouseAllocation2 = new WarehouseAllocation();
//			warehouseAllocation2.setId(1l);
//			warehouseAllocation.setParent(warehouseAllocation2);
			warehouseAllocationService.save(warehouseAllocation);
		}
	}

	@Test
	public void testUpdate() {
		WarehouseAllocation warehouseAllocation = warehouseAllocationService.getById(1L);
		String oldstr=warehouseAllocation.getName();
		warehouseAllocation.setName("Allocation");
		warehouseAllocationService.update(warehouseAllocation);
		WarehouseAllocation warehouseAllocation2 = warehouseAllocationService.getById(1L);
		String newstr=warehouseAllocation2.getName();
		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testDelete() {
		warehouseAllocationService.delete(2l);
		WarehouseAllocation warehouseAllocation=warehouseAllocationService.getById(2l);
		Assert.assertNull(warehouseAllocation);
	}

	@Test
	public void testGetById() {
		WarehouseAllocation warehouseAllocation=warehouseAllocationService.getById(1l);
		Assert.assertNotNull(warehouseAllocation);
	}

	@Test
	public void testGetAll() {
		List<WarehouseAllocation> warehouseAllocations=warehouseAllocationService.getAll();
		Assert.assertTrue(warehouseAllocations.size()>0);
	}

	@Test
	public void testFindPager() {
		
		Pager<WarehouseAllocation> pager=warehouseAllocationService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}