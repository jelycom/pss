package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.domain.WarehouseAllocation;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

public class WarehouseServiceTest extends BaseServiceTest{

	private IWarehouseService warehouseService;

	@Resource(name = "warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 10; i++) {
			Warehouse warehouse = new Warehouse("Warehouse"+i);
//			warehouse.setManager(new Employee(1l));
//			Warehouse warehouse2 = new Warehouse();
//			warehouse2.setId(1l);
//			warehouse.setParent(warehouse2);
			warehouseService.save(warehouse);
		}
	}

	@Test
	public void testUpdate() {
		Warehouse warehouse = warehouseService.getById(3L);
		String oldstr=warehouse.getName();
		warehouse.setName("Warehouse"+"m");
		warehouseService.update(warehouse);
		Warehouse warehouse2 = warehouseService.getById(3L);
		String newstr=warehouse2.getName();
		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testDelete() {
		warehouseService.delete(2l);
		Warehouse warehouse=warehouseService.getById(2l);
		Assert.assertNull(warehouse);
	}

	@Test
	public void testDeletecascade(){
//		warehouseService.deletecascade(6l, WarehouseAllocation.class, "warehouse");
		
	}
	@Test
	public void testGetById() {
		Warehouse warehouse=warehouseService.getById(4l);
		Assert.assertNotNull(warehouse);
	}

	@Test
	public void testGetAll() {
		List<Warehouse> warehouses=warehouseService.getAll();
		Assert.assertTrue(warehouses.size()>0);
	}

	@Test
	public void testFindPager() {
		
		Pager<Warehouse> pager=warehouseService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}