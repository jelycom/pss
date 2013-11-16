/*
 * 捷利商业进销存管理系统
 * @(#)Region.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.NotTransactional;

import cn.jely.cd.domain.Region;
import cn.jely.cd.service.IRegionService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:RegionServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2012-11-09 13:48:12
 * 
 */
public class RegionServiceTest extends BaseServiceTest {

	private IRegionService regionService;

	@Resource(name = "regionService")
	public void setRegionService(IRegionService regionService) {
		this.regionService = regionService;
	}

	@Test
	@NotTransactional
	public void testSave() {
		Region region = new Region("地区树");
		regionService.save(region, null);
		for (int i = 0; i < 5; i++) {
			Region regl1 = new Region("re_" + i);
			regionService.save(regl1, region.getId());
			for (int j = 0; j < 5; j++) {
				Region regl2 = new Region("re_" + i + "_" + j);
				regionService.save(regl2, regl1.getId());
				for (int k = 0; k < 2; k++) {
					Region regl3 = new Region("re_" + i + "_" + j + "_" + k);
					regionService.save(regl3, regl2.getId());
				}
			}
		}
		Assert.assertTrue(regionService.getAll().size() > 10);
	}

	@Test
	public void testUpdate() {
		Region region = (Region) getRandomObject(regionService.getAll());
		String oldstr = region.getName();
		region.setName("u" + oldstr);
		regionService.update(region);
		Region region2 = regionService.findQueryObject(new ObjectQuery().addWhere("name=:name","name", oldstr));
		Assert.assertNull(region2);
	}

	@Test
	public void testGetAll() {
		List<Region> regions = regionService.getAll();
		Assert.assertTrue(regions.size() > 0);
	}

	@Test
	public void testFindPager() {

		Pager<Region> pager = regionService.findPager(new ObjectQuery());
		System.out.println("pageNo:" + pager.getPageNo());
		System.out.println("pageSize:" + pager.getPageSize());
		System.out.println("datas:" + pager.getRows());
		System.out.println("totalPages:" + pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size() > 0);
	}

	@Test
	public void testfindRegionsWithDepth() {
		List<Region> regions = regionService.findTreeNodes(null,true);
		for (Region region : regions) {
			System.out.println(region);
		}
	}

	@Test
	public void testMoveIn() {
		List<Region> all = regionService.getAll();
		Region r1 = (Region) getRandomObject(all);
		Region r2 = (Region) getRandomObject(all);
		Long id1 = r1.getId();
		Long pid = r2.getId();
		regionService.MoveIn(id1, pid);
		r1=regionService.getById(id1);
		r2=regionService.getById(pid);
		int d1 = r1.getDepth();
		int d2 = r2.getDepth();
		logger.debug("child Level:"+d1+"      parent Level:"+d2);
		System.out.println("child :"+r1+"      parent:"+r2);
		System.out.println("child Level:"+d1+"      parent Level:"+d2);
//		Assert.assertTrue(d2 == d1 - 1);
	}

	@Test
	public void testChangeOrder() {
//		List<Region> all = regionService.getAll();
//		Region r1 = (Region) getRandomObject(all);
//		Region r2 = (Region) getRandomObject(all);
//		Long id1 = r1.getId();
//		Long pid = r2.getId();
//		if(r1.getLft()>r2.getLft()){
//			regionService.ChangeOrder(id1, pid);
//			r1=regionService.getById(id1);
//			r2=regionService.getById(pid);
//			Assert.assertTrue(r1.getLft()<r2.getLft());
//		}else if(r1.getLft()<r2.getLft()){
//			regionService.ChangeOrder(id1, pid);
//			r1=regionService.getById(id1);
//			r2=regionService.getById(pid);
//			Assert.assertTrue(r1.getLft()>r2.getLft());
//		}
	}

//	@Test
//	public void testMovePre() {
//		
//		regionService.MovePre(4l);
//	}
//
//	@Test
//	public void testMoveNext() {
//		regionService.MoveNext(4l);
//	}
//
//	@Test
//	public void testMoveFirst() {
//		regionService.MoveFirst(12l);
//	}
//
//	@Test
//	public void testMoveLast() {
//		regionService.MoveLast(12l);
//	}
//
//	@Test
//	public void testUpdateTreeNode() {
//		regionService.update(regionService.getById(3l), 2l);
//	}
}