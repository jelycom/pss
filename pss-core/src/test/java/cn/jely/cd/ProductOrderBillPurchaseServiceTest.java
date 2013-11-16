/*
 * 捷利商业进销存管理系统
 * @(#)ProductOrderBillmaster.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductOrderBillDetail;
import cn.jely.cd.domain.ProductOrderBillPurchaseMaster;
import cn.jely.cd.domain.ProductPlanDetail;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.service.IProductOrderBillPurchaseService;
import cn.jely.cd.service.IProductPlanPurchaseService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductOrderBillmasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2012-12-05 10:34:59 
 *
 */
public class ProductOrderBillPurchaseServiceTest extends BaseServiceTest{

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
	
	@Test
	public void testGenerateItem(){
		String item=((ItemGenAble)productOrderBillPurchaseService).generateItem(new Date());
		System.out.println(item);
	}
	
	@Test
	public void testSave() {
		List<Employee> allemps = employeeService.getAll();
		List<BusinessUnits> allUnits= businessUnitsService.getAll();
		List<FundAccount> allfas=fundAccountService.getAll();
		List<ProductPlanPurchaseMaster> allPlans=productPlanPurchaseService.findAllUnFinished();
		List<ProductPlanPurchaseMaster> subPlans=null;
		if(allPlans!=null&&allPlans.size()>1){
			subPlans=new ArrayList<ProductPlanPurchaseMaster>(getRandomSubList(allPlans));
		}
		List<Warehouse> allhouses=warehouseService.getAll();
		ActionResource resource=actionResourceService.findQueryObject(new ObjectQuery().addWhere("linkAddress=:address", "address", "cn.jely.cd.web.ProductOrderBillPurchaseAction.list"));
//		List<State> states=stateResourceOPService.getStateSequence(resource);
//		State state=states.get(0);
		for(int i=0;i<5;i++){
			Employee executeEmployee =(Employee) getRandomObject(allemps);
			BusinessUnits companyUnit=(BusinessUnits) getRandomObject(allUnits);
			FundAccount account=(FundAccount) getRandomObject(allfas);
			Warehouse warehouse=(Warehouse) getRandomObject(allhouses);
			ProductOrderBillPurchaseMaster master=new ProductOrderBillPurchaseMaster(companyUnit,warehouse,executeEmployee,account);
			master.setItem(((ItemGenAble)productOrderBillPurchaseService).generateItem(new Date()));
//			master.setState(state);
			List<ProductOrderBillDetail> details = new ArrayList<ProductOrderBillDetail>();
			List<Product> allProducts=productService.getAll();
			for(int j=0;j<3;j++){
				ProductOrderBillDetail detail=new ProductOrderBillDetail();
				detail.setProduct(allProducts.get(new Random().nextInt(allProducts.size())));
				detail.setQuantity(new Random().nextInt(20)+1);
				detail.setMemos("MEMOS FOR .........");
				details.add(detail);
			}
			if(subPlans!=null&&subPlans.size()>0){
				master.setProductPlans(subPlans);
			}
			master.getDetails().addAll(details);
			productOrderBillPurchaseService.save(master);
		}
		
	}
	@Test
	public void testSaveAssoPlan() {
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
		List<ProductPlanPurchaseMaster> subPlans=new ArrayList<ProductPlanPurchaseMaster>(getRandomSubList(allPlans));
		List<Warehouse> allhouses=warehouseService.getAll();
		ActionResource resource=actionResourceService.findQueryObject(new ObjectQuery().addWhere("linkAddress=:address", "address", "cn.jely.cd.web.ProductOrderBillPurchaseAction.list"));
//		List<State> states=stateResourceOPService.getStateSequence(resource);
//		State state=states.get(0);
		Employee executeEmployee =(Employee) getRandomObject(allemps);
		BusinessUnits companyUnit=(BusinessUnits) getRandomObject(allUnits);
		FundAccount account=(FundAccount) getRandomObject(allfas);
		Warehouse warehouse=(Warehouse) getRandomObject(allhouses);
		ProductOrderBillPurchaseMaster master=new ProductOrderBillPurchaseMaster(companyUnit,warehouse,executeEmployee,account);
		master.setItem(((ItemGenAble)productOrderBillPurchaseService).generateItem(new Date()));
//			master.setState(state);
		List<ProductOrderBillDetail> details = new ArrayList<ProductOrderBillDetail>();
		for(ProductPlanPurchaseMaster plan:subPlans){
			for(ProductPlanDetail plandetail:plan.getDetails()){
				ProductOrderBillDetail detail=new ProductOrderBillDetail();
				detail.setPlanMaster(plan);
				detail.setProduct(plandetail.getProduct());
//				detail.setQuantity(plandetail.getPlanQuantity());
				detail.setQuantity(new Random().nextInt()%2==0?plandetail.getQuantity():plandetail.getQuantity()/2);
				detail.setMemos(plan.getItem());
				details.add(detail);
			}
		}
		master.getDetails().addAll(details);
		productOrderBillPurchaseService.save(master);
	}

	@Test
	public void testUpdate() {
		try {
			ProductOrderBillPurchaseMaster master=(ProductOrderBillPurchaseMaster) getRandomObject(productOrderBillPurchaseService.getAll());
			Long oldId=master.getId();
			Integer oldSize=master.getDetails().size();
//		productOrderBillPurchaseService.changeState(master, ProductOrderBillMaster.DISCARD); 
			master.getDetails().remove(0);
			productOrderBillPurchaseService.update(master);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage()!=null);
		}
	}

	@Test
	public void testUpdateState() {
		Pager<ProductOrderBillPurchaseMaster> pager=productOrderBillPurchaseService.findPager(null);
		ProductOrderBillPurchaseMaster master=(ProductOrderBillPurchaseMaster) getRandomObject(productOrderBillPurchaseService.getAll());
		State oldState = master.getState();
		ActionResource resource=actionResourceService.findQueryObject(new ObjectQuery().addWhere("linkAddress=:address", "address", "cn.jely.cd.web.ProductOrderBillPurchaseAction.list"));
		if(resource!=null){
//			List<ActionResource> ops=stateResourceOPService.getOPList(resource,oldState);
//			Assert.assertTrue(ops.size()>0);
//			printList(ops);
		}
	}

}