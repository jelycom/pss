/*
 * 捷利商业进销存管理系统
 * @(#)Bursary.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.jely.cd.domain.Bursary;
import cn.jely.cd.service.IBursaryService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.BaseServiceTest;

/**
 * @ClassName:BursaryServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-08-05 10:50:45 
 *
 */
public class BursaryServiceTest extends BaseServiceTest{

	private IBursaryService bursaryService;

	@Resource(name = "bursaryService")
	public void setBursaryService(IBursaryService bursaryService) {
		this.bursaryService = bursaryService;
	}

	@Test
	public void testSave() {
		Bursary bursary=new Bursary("0000","会计科目","会计科目");
		bursaryService.save(bursary,null);
		Bursary assets=new Bursary("01","资产","资产");
		bursaryService.save(assets,bursary.getId());
		Bursary cash=new Bursary("1001","现金","现金");
		bursaryService.save(cash,assets.getId());
		Bursary bankAccount=new Bursary("1002","银行存款","银行存款");
		bursaryService.save(bankAccount,assets.getId());
		Bursary receiveable=new Bursary("1122", "应收帐款", "应收帐款");
		bursaryService.save(receiveable,assets.getId());
		Bursary advance=new Bursary("1123", "预付帐款", "预付帐款");
		bursaryService.save(advance,assets.getId());
		Bursary otherReceiveable=new Bursary("1221", "其它应收款", "其它应收款");
		bursaryService.save(otherReceiveable,assets.getId());
		Bursary stockProduct=new Bursary("1405", "库存商品", "库存商品");
		bursaryService.save(stockProduct,assets.getId());
		
		Bursary liability=new Bursary("02", "负债", "负债");
		bursaryService.save(liability,bursary.getId());
		Bursary payable=new Bursary("2202", "应付帐款", "应付帐款");
		bursaryService.save(payable,liability.getId());
		Bursary prepaid=new Bursary("2203", "预收帐款", "预收帐款");
		bursaryService.save(prepaid,liability.getId());
		Bursary salary=new Bursary("2211", "应付职工薪酬", "应付薪酬");
		bursaryService.save(salary,liability.getId());
		Bursary tax=new Bursary("2221", "应交税费", "应交税费");
		bursaryService.save(tax,liability.getId());
		bursaryService.save(new Bursary("2241", "其它应付款", "其它应付款"), liability.getId());
		
		Bursary income=new Bursary("03","收入","收入");
		bursaryService.save(income,bursary.getId());
		Bursary otherincome = new Bursary("6051","其它业务收入","其它收入");
		bursaryService.save(otherincome, income.getId());
		bursaryService.save(new Bursary("6051001","调帐收入","调帐收入"),otherincome.getId());
		bursaryService.save(new Bursary("6051002", "利息收入", "利息收入"),otherincome.getId());
		bursaryService.save(new Bursary("6051003", "废品收入", "废品收入"),otherincome.getId());
		
		Bursary outcome=new Bursary("04", "支出", "支出");
		bursaryService.save(outcome,bursary.getId());
		Bursary otheroutcome = new Bursary("6402", "其它业务支出","其它支出");
		bursaryService.save(otheroutcome,outcome.getId());
		bursaryService.save(new Bursary("6402001","调帐亏损","调帐亏损"),otheroutcome.getId());
		bursaryService.save(new Bursary("6402002","发货费用","发货费用"),otheroutcome.getId());
		Bursary ownershipinterest=new Bursary("05", "所有者权益", "权益");
		bursaryService.save(ownershipinterest, bursary.getId());
	}

	@Test
	public void testUpdate() {
		Bursary bursary = (Bursary) getRandomObject(bursaryService.getAll());
		String oldstr=bursary.getFullName();
		bursary.setFullName("u"+oldstr);
		bursaryService.update(bursary);
		Bursary bursary2 = bursaryService.getById(bursary.getId());
		String newstr=bursary2.getFullName();
		Assert.assertTrue(!oldstr.equals(newstr));
	}

	@Test
	public void testFindPager() {
		
		Pager<Bursary> pager=bursaryService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	@Test
	public void testListAccount(){
		printList(bursaryService.findAccount());
	}

	@Test
	public void testListOtherInCome(){
		printList(bursaryService.findOtherInCome());
	}
	
	@Test
	public void testListOtherOutCome(){
		printList(bursaryService.findOtherOutCome());
	}
	
	@Test
	public void testDeleteAllBursary() {
		List<Bursary> bursarys=bursaryService.getAll();
		for(Bursary bursary:bursarys){
			bursaryService.delete(bursary.getId());
		}
		Assert.assertTrue(bursaryService.getAll().size()==0);
	}

}