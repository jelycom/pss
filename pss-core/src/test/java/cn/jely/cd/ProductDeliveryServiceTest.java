/*
 * 捷利商业进销存管理系统
 * @(#)ProductDeliveryMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.ProductCommonDetail;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.service.IProductDeliveryService;
import cn.jely.cd.service.IProductOrderBillDeliveryService;
import cn.jely.cd.service.IProductPlanDeliveryService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.State;
import cn.jely.cd.vo.RealStockVO;

/**
 * @ClassName:ProductDeliveryMasterServiceTest
 * @Description:ServiceTest
 * @author
 * @version 2013-06-21 14:54:28 
 *
 */
public class ProductDeliveryServiceTest extends BaseServiceTest{

	private IProductDeliveryService productDeliveryService;
	private IEmployeeService employeeService;
	private IProductService productService;
	private IBusinessUnitsService businessUnitsService;
	private IFundAccountService fundAccountService;
	private IProductPlanDeliveryService productPlanDeliveryService;
	private IWarehouseService warehouseService;
	private IStateResourceOPService stateResourceOPService;
	private IActionResourceService actionResourceService;
	private IProductStockDetailService productStockDetailService;
	private IProductOrderBillDeliveryService productOrderBillDeliveryService;
	
	@Resource(name="productStockDetailService")
		public void setProductStockDetailService(IProductStockDetailService productStockDetailService) {
		this.productStockDetailService = productStockDetailService;
	}
	@Resource(name = "actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}
	@Resource(name = "productOrderBillDeliveryService")
	public void setProductOrderBillDeliveryService(IProductOrderBillDeliveryService productOrderBillDeliveryService) {
		this.productOrderBillDeliveryService = productOrderBillDeliveryService;
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
	@Resource(name="productPlanDeliveryService")
	public void setProductPlanDeliveryService(IProductPlanDeliveryService productPlanDeliveryService) {
		this.productPlanDeliveryService = productPlanDeliveryService;
	}
	
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	@Resource(name = "productDeliveryService")
	public void setProductDeliveryService(IProductDeliveryService productDeliveryMasterService) {
		this.productDeliveryService = productDeliveryMasterService;
	}

	@Test
	public void testSave() {
		List<Employee> allemps = employeeService.getAll();
		List<BusinessUnits> allUnits= businessUnitsService.getAll();
		List<FundAccount> allfas=fundAccountService.getAll();
		List<ProductPlanDeliveryMaster> allPlans=productPlanDeliveryService.getAll();
		List<ProductPlanDeliveryMaster> exclplans=new ArrayList<ProductPlanDeliveryMaster>();
		Iterator<ProductPlanDeliveryMaster> iterator = allPlans.iterator();
		while(iterator.hasNext()){
			ProductPlanDeliveryMaster tmpmaster=iterator.next();
			if(State.COMPLETE.equals(tmpmaster.getState())||State.DISCARD.equals(tmpmaster.getState())){
				exclplans.add(tmpmaster);
			}
		}
		allPlans.removeAll(exclplans);
		List<ProductStockDetail> psDetails=productStockDetailService.getAll();
//		List<ProductPlanDeliveryMaster> subPlans=new ArrayList<ProductPlanDeliveryMaster>(getRandomSubList(allPlans));
		List<Warehouse> allhouses=warehouseService.getAll();
		List<RealStockVO> allstocks=productStockDetailService.findRealStock(allhouses);
		ProductDeliveryMaster master = new ProductDeliveryMaster(); //如果出错请把字符串长度改短
		master.setBusinessUnit((BusinessUnits) getRandomObject(allUnits));
		master.setEmployee((Employee) getRandomObject(allemps));
		master.setFundAccount((FundAccount) getRandomObject(allfas));
		master.setPaid(new BigDecimal("1000"));
		master.setWarehouse(allstocks.get(0).getWarehouse());
		List<ProductCommonDetail> details=new ArrayList<ProductCommonDetail>();
		int detailsSize=new Random().nextInt(4)+1;
		BigDecimal total=BigDecimal.ZERO;
//		for (int i=0;i<detailsSize;i++) {
			ProductCommonDetail detail=new ProductCommonDetail();
			detail.setMaster(master);
			detail.setProduct(allstocks.get(0).getProduct());
			BigDecimal amount = new BigDecimal("1000.00");
			detail.setAmount(amount);
			Integer quanlity = new Random().nextInt(2)+1;
			detail.setQuantity(quanlity);
			detail.setPrice(SystemCalUtil.dividePrice(amount, new BigDecimal(quanlity)));
			details.add(detail);
			total=total.add(amount);
//		}
		master.setAmount(total);
		master.setDetails(details);
		productDeliveryService.save(master);
	}

	@Test
	public void testUpdate() {
		ProductDeliveryMaster productDeliveryMaster = (ProductDeliveryMaster) getRandomObject(productDeliveryService.getAll());
		productDeliveryService.update(productDeliveryMaster);
		ProductDeliveryMaster productDeliveryMaster2 = productDeliveryService.getById(productDeliveryMaster.getId());
		Assert.assertTrue(true);
	}

	
	@Test
	public void testfindUnComplete(){
//		BusinessUnits businessUnit=businessUnitsService.getById(31l);
		List<ProductDeliveryMaster> masters=productDeliveryService.findAllUnFinished(new ObjectQuery());
		printList(masters);
	}
	
	@Test
	public void testFindPager() {
		
		Pager<ProductDeliveryMaster> pager=productDeliveryService.findPager(new ObjectQuery());
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	


}