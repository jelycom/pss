package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Contacts;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IContactsService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

public class ContactsServiceTest extends BaseServiceTest {

	private IContactsService contactsService;
	private IBusinessUnitsService businessUnitsService;
	
	@Resource(name="contactsService")
	public void setContactsService(IContactsService contactsService) {
		this.contactsService = contactsService;
	}
	
	@Resource(name="businessUnitsService")
	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}

	@Test
	public void testSave() {
		List<BusinessUnits> allUnits = businessUnitsService.getAll();
		for(int i=0;i<4;i++){
			BusinessUnits unit=(BusinessUnits) getRandomObject(allUnits);
			for (int j = 0; j < 3; j++) {
				Contacts contacts = new Contacts(unit,"u"+j+"cont"+i);
				contactsService.save(contacts);
			}
		}
	}

	@Test
	public void testUpdate() {
		Contacts contacts = (Contacts) getRandomObject(contactsService.getAll());
		String oldstr=contacts.getName();
		contacts.setName("u"+oldstr);
		contactsService.update(contacts);
		Contacts Contacts2 = contactsService.findQueryObject(new ObjectQuery().addWhere("name=:name","name", oldstr));
		Assert.assertNull(Contacts2);
	}


	@Test
	public void testGetAll() {
		List<Contacts> Contactss=contactsService.getAll();
		Assert.assertTrue(Contactss.size()>0);
	}

	@Test
	public void testFindPager() {
		Pager<Contacts> pager=contactsService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
}
