package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.Department;
import cn.jely.cd.service.IDepartmentService;
import cn.jely.cd.util.Pager;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"/applicationContext-hibernate.xml", "/applicationContext-test.xml"})
import cn.jely.cd.util.query.ObjectQuery;

public class DepartmentServiceTest extends BaseServiceTest{

	private IDepartmentService departmentService;

	@Resource(name = "departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@Test
	public void testSave() {
		for (int i = 0; i < 5; i++) {
			Department department = new Department("dept"+i);
			departmentService.save(department);
			for(int j=0;j<3;j++){
				Department department2 = new Department("dept"+i+"_"+j);
				department2.setParent(department);
				departmentService.save(department2);
			}
		}
	List<Department> all=departmentService.getAll();
	Assert.assertTrue(all.size()>10);
	}

	@Test
	public void testUpdate() {
		Department department = (Department) getRandomObject(departmentService.getAll());
		String oldstr=department.getName();
		department.setName("u"+oldstr);
		departmentService.update(department);
		Department department2 = departmentService.findQueryObject(new ObjectQuery().addWhere("name=:name","name", oldstr));
		Assert.assertNull(department2);
	}


	@Test
	public void testFindPager() {
		Pager<Department> pager=departmentService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}