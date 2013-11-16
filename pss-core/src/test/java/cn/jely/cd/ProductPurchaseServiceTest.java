/*
 * 捷利商业进销存管理系统
 * @(#)ProductPurchaseMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonDetail;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.service.IProductOrderBillPurchaseService;
import cn.jely.cd.service.IProductPlanPurchaseService;
import cn.jely.cd.service.IProductPurchaseService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductPurchaseMasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-06-21 14:54:28 
 *
 */
public class ProductPurchaseServiceTest extends BaseServiceTest{

	private IProductPurchaseService productPurchaseService;
	private IProductOrderBillPurchaseService productOrderBillPurchaseService;
	private IEmployeeService employeeService;
	private IProductService productService;
	private IBusinessUnitsService businessUnitsService;
	private IFundAccountService fundAccountService;
	private IProductPlanPurchaseService productPlanPurchaseService;
	private IWarehouseService warehouseService;
	private IStateResourceOPService stateResourceOPService;
	private IActionResourceService actionResourceService;
	
	
	@Resource(name = "actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}
	@Resource(name = "productOrderBillPurchaseService")
	public void setProductOrderBillPurchaseService(IProductOrderBillPurchaseService productOrderBillPurchaseService) {
		this.productOrderBillPurchaseService = productOrderBillPurchaseService;
	}
	@Resource(name="stateResourceOPService")
	public void setStateResourceOPService(IStateResourceOPService stateResourceOPService) {
		this.stateResourceOPService = stateResourceOPService;
	}
	
	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@Resource(name = "productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	@Resource(name="businessUnitsService")
	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}
	@Resource(name="fundAccountService")
	public void setFundAccountService(IFundAccountService fundAccountService) {
		this.fundAccountService = fundAccountService;
	}
	@Resource(name="productPlanPurchaseService")
	public void setProductPlanPurchaseService(IProductPlanPurchaseService productPlanPurchaseService) {
		this.productPlanPurchaseService = productPlanPurchaseService;
	}
	
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	@Resource(name = "productPurchaseService")
	public void setProductPurchaseService(IProductPurchaseService productPurchaseMasterService) {
		this.productPurchaseService = productPurchaseMasterService;
	}

	@Test
	public void testSave() {
		List<Employee> allemps = employeeService.getAll();
		List<BusinessUnits> allUnits= businessUnitsService.getAll();
		List<FundAccount> allfas=fundAccountService.getAll();
		List<ProductPlanPurchaseMaster> allPlans=productPlanPurchaseService.getAll();
		List<ProductPlanPurchaseMaster> exclplans=new ArrayList<ProductPlanPurchaseMaster>();
		Iterator<ProductPlanPurchaseMaster> iterator = allPlans.iterator();
		while(iterator.hasNext()){
			ProductPlanPurchaseMaster tmpmaster=iterator.next();
			if(State.COMPLETE.equals(tmpmaster.getState())||State.DISCARD.equals(tmpmaster.getState())){
				exclplans.add(tmpmaster);
			}
		}
		allPlans.removeAll(exclplans);
//		List<ProductPlanPurchaseMaster> subPlans=new ArrayList<ProductPlanPurchaseMaster>(getRandomSubList(allPlans));
		List<Warehouse> allhouses=warehouseService.getAll();
		List<Product> allProducts=productService.getAll();
		ProductPurchaseMaster master = new ProductPurchaseMaster(); 
		master.setWarehouse((Warehouse) getRandomObject(allhouses));
		master.setFundAccount((FundAccount) getRandomObject(allfas));
		Date billDate = new Date();
		master.setBillDate(billDate);
		master.setItem(((ItemGenAble)productPurchaseService).generateItem(billDate));
//		master.setCreateMan(allemps.get(0));
		master.setEmployee((Employee) getRandomObject(allemps));
		master.setBusinessUnit((BusinessUnits) getRandomObject(allUnits));
		List<ProductCommonDetail> details=new ArrayList<ProductCommonDetail>();
		int detailsSize=new Random().nextInt(4)+1;
		BigDecimal totalAmount=BigDecimal.ZERO;
		for (int i=0;i<detailsSize;i++) {
			ProductCommonDetail detail=new ProductCommonDetail();
			detail.setMaster(master);
			detail.setProduct((Product) getRandomObject(allProducts));
			BigDecimal amount = new BigDecimal("800.00");
			detail.setAmount(amount);
			totalAmount=totalAmount.add(amount);
			Integer quanlity = new Random().nextInt(20)+1;
			detail.setQuantity(quanlity);
			detail.setPrice(SystemCalUtil.dividePrice(amount, new BigDecimal(quanlity)));
			details.add(detail);
		}
		master.setAmount(totalAmount);
		master.setPaid(totalAmount.divide(new BigDecimal(2)));
//		master.setPaid(new BigDecimal(""));
		master.setDetails(details);
		productPurchaseService.save(master);
	}

	@Test
	public void testGenerateItem(){
		String item=((ItemGenAble)productPurchaseService).generateItem(new Date());
		System.out.println(item);
	}
	
	@Test
	public void testUpdate() {
		ProductPurchaseMaster productPurchaseMaster = (ProductPurchaseMaster) getRandomObject(productPurchaseService.getAll());
		productPurchaseService.update(productPurchaseMaster);
		ProductPurchaseMaster productPurchaseMaster2 = productPurchaseService.getById(productPurchaseMaster.getId());
		Assert.assertTrue(true);
	}

	@Test
	public void testfindUnComplete(){
//		BusinessUnits businessUnit=new BusinessUnits(34l);
		List<ProductPurchaseMaster> masters=productPurchaseService.findAllUnFinished(new ObjectQuery());
		printList(masters);
	}
	@Test
	public void testfindUnCompleteWithReturn(){
//		BusinessUnits businessUnit=new BusinessUnits(34l);
		List<ProductCommonMaster> masters=productPurchaseService.findUnFinishedWithReturn(new ObjectQuery());
		printList(masters);
	}
	@Test
	public void testFindPager() {
		Pager<ProductPurchaseMaster> pager=productPurchaseService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	



}