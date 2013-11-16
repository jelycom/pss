package cn.jely.cd;

import java.util.UUID;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

public class FundAccountServiceTest extends BaseServiceTest {

	private IFundAccountService fundAccountService;

	@Resource(name = "fundAccountService")
	public void setFundAccountService(IFundAccountService fundAccountService) {
		this.fundAccountService = fundAccountService;
	}

	@Test
	public void testSaveCash() {
		FundAccount fundAccount = new FundAccount("现金");
		fundAccountService.save(fundAccount);
		fundAccount = new FundAccount("门市现金");
		fundAccount.setIsDefault(true);
		fundAccountService.save(fundAccount);
	}

	@Test
	public void testSaveBankAccount() {
		for (int i = 0; i < 5; i++) {
			FundAccount fundAccount = new FundAccount("", "BankA" + i, "", UUID.randomUUID().toString(), "XX银行" + i);
			fundAccountService.save(fundAccount);
		}
	}

	@Test
	public void testUpdate() {
		FundAccount fundAccount = (FundAccount) getRandomObject(fundAccountService.getAll());
		String oldstr = fundAccount.getName();
		fundAccount.setName("u" + oldstr);
		fundAccountService.update(fundAccount);
		FundAccount emp2 = fundAccountService.findQueryObject(new ObjectQuery().addWhere("name=:name","name", oldstr));
		Assert.assertNull(emp2);
	}

	@Test
	public void testFindPager() {
		ObjectQuery fundAccountQuery = new ObjectQuery();
		// fundAccountQuery.setOrderType("desc");
		// fundAccountQuery.setOrderField("id");
		Pager<FundAccount> pager = fundAccountService.findPager(fundAccountQuery);
		System.out.println("pageNo:" + pager.getPageNo());
		System.out.println("pageSize:" + pager.getPageSize());
		System.out.println("datas:" + pager.getRows());
		for (FundAccount fundAccount : pager.getRows()) {
			System.out.println(fundAccount.getId() + ":" + fundAccount.getName());
		}
		System.out.println("totalPages:" + pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size() > 0);
	}
}