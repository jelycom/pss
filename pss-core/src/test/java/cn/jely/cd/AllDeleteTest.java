package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.domain.AccountInMaster;
import cn.jely.cd.domain.AccountOtherInMaster;
import cn.jely.cd.domain.AccountOtherOutMaster;
import cn.jely.cd.domain.AccountOutMaster;
import cn.jely.cd.domain.AccountTransferMaster;
import cn.jely.cd.domain.BaseData;
import cn.jely.cd.domain.BaseDataType;
import cn.jely.cd.domain.Bursary;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Contacts;
import cn.jely.cd.domain.Department;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductDeliveryMaster;
import cn.jely.cd.domain.ProductOrderBillDeliveryMaster;
import cn.jely.cd.domain.ProductOrderBillMaster;
import cn.jely.cd.domain.ProductOrderBillPurchaseMaster;
import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductTransferDiffMaster;
import cn.jely.cd.domain.ProductTransferSameMaster;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Region;
import cn.jely.cd.service.IAccountInService;
import cn.jely.cd.service.IAccountOtherInService;
import cn.jely.cd.service.IAccountOtherOutService;
import cn.jely.cd.service.IAccountOutService;
import cn.jely.cd.service.IAccountTransferService;
import cn.jely.cd.service.IBaseDataService;
import cn.jely.cd.service.IBaseDataTypeService;
import cn.jely.cd.service.IBursaryService;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IContactsService;
import cn.jely.cd.service.IDepartmentService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.service.IProductDeliveryService;
import cn.jely.cd.service.IProductOrderBillDeliveryService;
import cn.jely.cd.service.IProductOrderBillPurchaseService;
import cn.jely.cd.service.IProductPlanDeliveryService;
import cn.jely.cd.service.IProductPlanPurchaseService;
import cn.jely.cd.service.IProductPurchaseService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IProductStockDetailService;
import cn.jely.cd.service.IProductTransferDiffService;
import cn.jely.cd.service.IProductTransferSameService;
import cn.jely.cd.service.IProductTypeService;
import cn.jely.cd.service.IRegionService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.BusinessType;
import cn.jely.cd.sys.domain.PeriodARAP;
import cn.jely.cd.sys.domain.PeriodAccount;
import cn.jely.cd.sys.domain.PeriodStock;
import cn.jely.cd.sys.domain.Role;
import cn.jely.cd.sys.domain.State;
import cn.jely.cd.sys.domain.StateResourceOP;
import cn.jely.cd.sys.domain.TimerJob;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.IBusinessTypeService;
import cn.jely.cd.sys.service.IPeriodARAPService;
import cn.jely.cd.sys.service.IPeriodAccountService;
import cn.jely.cd.sys.service.IPeriodStockService;
import cn.jely.cd.sys.service.IRoleService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.sys.service.IStateService;
import cn.jely.cd.sys.service.ITimerJobService;
import cn.jely.cd.util.query.ObjectQuery;

public class AllDeleteTest extends BaseServiceTest{
	private ITimerJobService timerJobService;
	private IRegionService regionService;
	private IBursaryService bursaryService;
	private IBaseDataTypeService baseDataTypeService;
	private IBaseDataService baseDataService;
	private IEmployeeService employeeService;
	private IRoleService roleService;
	private IActionResourceService actionResourceService;
	private IDepartmentService departmentService;
	private IBusinessUnitsService businessUnitsService;
	private IProductTypeService productTypeService;
	private IProductService productService;
	private IContactsService contactsService;
	private IProductPlanPurchaseService productPlanPurchaseService;
	private IProductPlanDeliveryService productPlanDeliveryService;
	private IProductOrderBillPurchaseService productOrderBillPurchaseService;
	private IProductOrderBillDeliveryService productOrderBillDeliveryService;
	private IProductPurchaseService productPurchaseService;
	private IProductDeliveryService productDeliveryService;
	private IStateService stateService;
	private IStateResourceOPService stateResourceOPService;
	private IPeriodAccountService periodAccountService;
	private IPeriodARAPService periodARAPService;
	private IPeriodStockService periodStockService;
	private IFundAccountService fundAccountService;
	private IBusinessTypeService businessTypeService;
	private IAccountTransferService accountTransferService;
	private IAccountInService accountInService;
	private IAccountOutService accountOutService;
	private IAccountOtherInService accountOtherInService;
	private IAccountOtherOutService accountOtherOutService;
	private IProductStockDetailService productStockDetailService;
	private IProductTransferSameService productTransferSameService;
	private IProductTransferDiffService productTransferDiffService;
	
	@Resource(name="productTransferSameService")
	public void setProductTransferSameService(IProductTransferSameService productTransferSameService) {
		this.productTransferSameService = productTransferSameService;
	}
	@Resource(name="productTransferDiffService")
	public void setProductTransferDiffService(IProductTransferDiffService productTransferDiffService) {
		this.productTransferDiffService = productTransferDiffService;
	}
	@Resource(name="productStockDetailService")
	public void setProductStockDetailService(IProductStockDetailService productStockDetailService) {
		this.productStockDetailService = productStockDetailService;
	}
	@Resource(name="accountInService")
	public void setAccountInService(IAccountInService accountInService) {
		this.accountInService = accountInService;
	}
	@Resource(name="accountOutService")
	public void setAccountOutService(IAccountOutService accountOutService) {
		this.accountOutService = accountOutService;
	}
	@Resource(name="accountOtherInService")
	public void setAccountOtherInService(IAccountOtherInService accountOtherInService) {
		this.accountOtherInService = accountOtherInService;
	}
	@Resource(name="accountOtherOutService")
	public void setAccountOtherOutService(IAccountOtherOutService accountOtherOutService) {
		this.accountOtherOutService = accountOtherOutService;
	}
	@Resource(name="accountTransferService")
	public void setAccountTransferService(IAccountTransferService accountTrasferService) {
		this.accountTransferService = accountTrasferService;
	}
	@Resource(name="productPurchaseService")
	public void setProductPurchaseService(IProductPurchaseService productPurchaseService) {
		this.productPurchaseService = productPurchaseService;
	}
	@Resource(name="productDeliveryService")
	public void setProductDeliveryService(IProductDeliveryService productDeliveryService) {
		this.productDeliveryService = productDeliveryService;
	}

	@Resource(name="productOrderBillDeliveryService")
	public void setProductOrderBillDeliveryService(IProductOrderBillDeliveryService productOrderBillDeliveryService) {
		this.productOrderBillDeliveryService = productOrderBillDeliveryService;
	}

	@Resource(name="businessTypeService")
	public void setBusinessTypeService(IBusinessTypeService businessTypeService) {
		this.businessTypeService = businessTypeService;
	}

	@Resource(name="fundAccountService")
	public void setFundAccountService(IFundAccountService fundAccountService) {
		this.fundAccountService = fundAccountService;
	}

	@Resource(name="periodStockService")
	public void setPeriodStockService(IPeriodStockService periodStockService) {
		this.periodStockService = periodStockService;
	}

	@Resource(name="periodARAPService")
	public void setPeriodARAPService(IPeriodARAPService periodARAPService) {
		this.periodARAPService = periodARAPService;
	}

	@Resource(name="periodAccountService")
	public void setPeriodAccountService(IPeriodAccountService periodAccountService) {
		this.periodAccountService = periodAccountService;
	}

	@Resource(name="stateResourceOPService")
	public void setStateResourceOPService(IStateResourceOPService stateResourceOPService) {
		this.stateResourceOPService = stateResourceOPService;
	}

	@Resource(name="stateService")
	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}
	
	@Resource(name="productOrderBillPurchaseService")
	public void setProductOrderBillPurchaseService(IProductOrderBillPurchaseService productOrderBillPurchaseService) {
		this.productOrderBillPurchaseService = productOrderBillPurchaseService;
	}
	@Resource(name = "productPlanDeliveryService")
	public void setProductPlanDeliveryService(IProductPlanDeliveryService productPlanDeliveryService) {
		this.productPlanDeliveryService = productPlanDeliveryService;
	}
	@Resource(name = "productPlanPurchaseService")
	public void setProductPlanPurchaseService(IProductPlanPurchaseService productPlanPurchaseService) {
		this.productPlanPurchaseService = productPlanPurchaseService;
	}
	@Resource(name="contactsService")
	public void setContactsService(IContactsService contactsService) {
		this.contactsService = contactsService;
	}
	@Resource(name = "productService")
	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	@Resource(name = "productTypeService")
	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}

	@Resource
	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}
	@Resource(name = "departmentService")
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Resource(name = "roleService")
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	@Resource(name="actionResourceService")
	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}
	@Resource(name = "employeeService")
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	@Resource(name = "timerJobService")
	public void setTimerJobService(ITimerJobService timerJobService) {
		this.timerJobService = timerJobService;
	}
	@Resource(name = "baseDataTypeService")
	public void setBaseDataTypeService(IBaseDataTypeService baseDataTypeService) {
		this.baseDataTypeService = baseDataTypeService;
	}
	@Resource(name = "regionService")
	public void setRegionService(IRegionService regionService) {
		this.regionService = regionService;
	}
	@Resource(name="bursaryService")
	public void setBursaryService(IBursaryService bursaryService) {
		this.bursaryService = bursaryService;
	}
	@Resource(name = "baseDataService")
	public void setBaseDataService(IBaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}
	
	@Test
	public void testDeleteAll(){
		testDeleteAllAccountOtherOutMaster() ;//付款单
		testDeleteAllAccountOtherInMaster();//收款单
		testDeleteAllAccountOutMaster() ;//付款单
		testDeleteAllAccountInMaster();//收款单
		testDeleteAllAccountTransferMaster();//转帐单
		testDeleteAllProductTransferMaster();//调拨单
		testDeleteAllProductStockDetail();
		testDeleteAllProductDeliveryMaster();//出货单
		testDeleteAllProductPurchaseMaster();//进货单
		testDeleteAllProductOrderBillPurchase();//进货订单
		testDeleteAllProductOrderbillDelivery(); //出货订单
		testDeleteAllProductPlanDelivery();//出货计划
		testDeleteAllProductPlanPurchase();//进货计划
		testDeleteAllPeriodAccount();//结帐期间帐户
		testDeleteAllPeriodARAP();//期初应收应付
		testDeleteAllPeriodStock();//期初库存
		testDeleteAllFundAccount();//帐户
		testDeleteAllBaseData();
		testDeleteAllBaseDataType();
		testDeleteAllEmployee();
		testDeleteAllDepartment();
		testDeleteAllRole();
		testDeleteAllStateResourceOP();
		testDeleteAllActionResource();
		testDeleteAllState();
		testDeleteAllContacts();
		testDeleteAllBusinessUnit();
		testDeleteAllProduct();
		testDeleteAllProductType();
		testDeleteAllRegion();
		testDeleteAllBusinessType();
		testDeleteAllBursary();
		testDeleteAllTimerJob();
	}
	

	/**
	 * void
	 */
	private void testDeleteAllBursary() {
		Bursary root=bursaryService.findQueryObject(new ObjectQuery().addWhere("lft=:lft","lft" ,1l ));
		if(root!=null){
			bursaryService.deleteCascade(root.getId());
		}
		List<Bursary> all=bursaryService.findAll(null);
		Assert.assertTrue(all.size()==0);
	}
	/**删除所有产品调拨
	 * void
	 */
	private void testDeleteAllProductTransferMaster() {
		List<ProductTransferDiffMaster> allDiff = productTransferDiffService.getAll();
		List<ProductTransferSameMaster> allSame = productTransferSameService.getAll();
		for(ProductTransferDiffMaster master:allDiff){
			productTransferDiffService.delete(master.getId());
		}
		for (ProductTransferSameMaster master : allSame) {
			productTransferSameService.delete(master.getId());
		}
	}
	/**删除库存批次
	 * void
	 */
	private void testDeleteAllProductStockDetail() {
		List<ProductStockDetail> sds=productStockDetailService.getAll();
		for(ProductStockDetail detail:sds){
			productStockDetailService.delete(detail.getId());
		}
		Assert.assertTrue(productStockDetailService.getAll().size()==0);
	}
	public void testDeleteAllAccountOtherOutMaster() {
		List<AccountOtherOutMaster> AccountOtherOutMasters=accountOtherOutService.getAll();
		for(AccountOtherOutMaster AccountOtherOutMaster:AccountOtherOutMasters){
			accountOtherOutService.delete(AccountOtherOutMaster.getId());
			
		}
		Assert.assertTrue(accountOtherOutService.getAll().size()==0);
	}
	public void testDeleteAllAccountOtherInMaster() {
		List<AccountOtherInMaster> AccountOtherInMasters=accountOtherInService.getAll();
		for(AccountOtherInMaster AccountOtherInMaster:AccountOtherInMasters){
			accountOtherInService.delete(AccountOtherInMaster.getId());
			
		}
		Assert.assertTrue(accountOtherInService.getAll().size()==0);
	}	
	public void testDeleteAllAccountOutMaster() {
		List<AccountOutMaster> accountOutMasters=accountOutService.getAll();
		for(AccountOutMaster accountOutMaster:accountOutMasters){
			accountOutService.delete(accountOutMaster.getId());
			
		}
		Assert.assertTrue(accountOutService.getAll().size()==0);
	}
	public void testDeleteAllAccountInMaster() {
		List<AccountInMaster> accountInMasters=accountInService.getAll();
		for(AccountInMaster accountInMaster:accountInMasters){
			accountInService.delete(accountInMaster.getId());
			
		}
		Assert.assertTrue(accountInService.getAll().size()==0);
	}	
	public void testDeleteAllAccountTransferMaster() {
		List<AccountTransferMaster> masters=accountTransferService.getAll();
		for(AccountTransferMaster master:masters){
			accountTransferService.delete(master.getId());
			
		}
		Assert.assertTrue(accountTransferService.getAll().size()==0);
	}
	public void testDeleteAllProductPurchaseMaster() {
		List<ProductPurchaseMaster> productPurchaseMasters=productPurchaseService.getAll();
		for(ProductPurchaseMaster productPurchaseMaster:productPurchaseMasters){
			productPurchaseService.delete(productPurchaseMaster.getId());
			
		}
		Assert.assertTrue(productPurchaseService.getAll().size()==0);
	}
	
	public void testDeleteAllProductDeliveryMaster() {
		List<ProductDeliveryMaster> productDeliveryMasters=productDeliveryService.getAll();
		for(ProductDeliveryMaster productDeliveryMaster:productDeliveryMasters){
			productDeliveryService.delete(productDeliveryMaster.getId());
			
		}
		Assert.assertTrue(productDeliveryService.getAll().size()==0);
	}
	
	public void testDeleteAllBusinessType() {
		List<BusinessType> businessTypes=businessTypeService.getAll();
		for(BusinessType businessType:businessTypes){
			businessTypeService.delete(businessType.getId());
			
		}
		Assert.assertTrue(businessTypeService.getAll().size()==0);
	}
	
	public void testDeleteAllFundAccount() {
		List<FundAccount> fundAccounts=fundAccountService.getAll();
		for(FundAccount fundAccount:fundAccounts){
			fundAccountService.delete(fundAccount.getId());
		}
		Assert.assertTrue(fundAccountService.getAll().size()==0);
	}

	@Test
	public void testDeleteAllPeriodStock() {
		List<PeriodStock> periodStocks=periodStockService.getAll();
		for(PeriodStock periodStock:periodStocks){
			periodStockService.delete(periodStock.getId());
		}
		Assert.assertTrue(periodStockService.getAll().size()==0);
	}
	
	public void testDeleteAllPeriodARAP() {
		List<PeriodARAP> periodARAPs=periodARAPService.getAll();
		for(PeriodARAP periodARAP:periodARAPs){
			periodARAPService.delete(periodARAP.getId());
		}
		Assert.assertTrue(periodARAPService.getAll().size()==0);
	}

	public void testDeleteAllPeriodAccount() {
		List<PeriodAccount> periodAccounts=periodAccountService.getAll();
		for(PeriodAccount periodAccount:periodAccounts){
			periodAccountService.delete(periodAccount.getId());
		}
		Assert.assertTrue(periodAccountService.getAll().size()==0);
	}
	
	public void testDeleteAllStateResourceOP() {
		List<StateResourceOP> stateResourceOPs=stateResourceOPService.getAll();
		for(StateResourceOP stateResourceOP:stateResourceOPs){
			stateResourceOPService.delete(stateResourceOP.getId());
		}
		Assert.assertTrue(stateResourceOPService.getAll().size()==0);
	}
	public void testDeleteAllState(){
		List<State> states=stateService.getAll();
		for(State state:states){
			stateService.delete(state.getId());
		}
		Assert.assertTrue(stateService.getAll().size() == 0);
	}
	public void testDeleteAllProductOrderBillPurchase() {
		List<ProductOrderBillPurchaseMaster> masters=productOrderBillPurchaseService.getAll();
		for(ProductOrderBillMaster master:masters){
			productOrderBillPurchaseService.delete(master.getId());
		}
		Assert.assertTrue(productOrderBillPurchaseService.getAll().size()==0);
	}
	public void testDeleteAllProductOrderbillDelivery() {
		List<ProductOrderBillDeliveryMaster> masters=productOrderBillDeliveryService.getAll();
		for(ProductOrderBillMaster master:masters){
			productOrderBillDeliveryService.delete(master.getId());
		}
		Assert.assertTrue(productOrderBillPurchaseService.getAll().size()==0);
	}

	public void testDeleteAllProductPlanDelivery(){
		List<ProductPlanDeliveryMaster> masters=productPlanDeliveryService.getAll();
		for(ProductPlanDeliveryMaster master:masters){
			productPlanDeliveryService.delete(master.getId());
		}
		Assert.assertTrue(productPlanDeliveryService.getAll().size()==0);
	}
	private void testDeleteAllProductPlanPurchase(){
		List<ProductPlanPurchaseMaster> masters=productPlanPurchaseService.getAll();
		for(ProductPlanPurchaseMaster master:masters){
			productPlanPurchaseService.delete(master.getId());
		}
		Assert.assertTrue(productPlanPurchaseService.getAll().size()==0);
	}
	
	private void testDeleteAllActionResource() {
		ActionResource root=actionResourceService.findQueryObject(new ObjectQuery().addWhere("lft=:lft","lft" ,1l ));
		if(root!=null){
			actionResourceService.deleteCascade(root.getId());
		}
		List<ActionResource> all=actionResourceService.getAll();
		Assert.assertTrue(all.size()==0);
	}

	private void testDeleteAllRole(){
		List<Role> roles=roleService.getAll();
		for(Role role:roles){
			roleService.delete(role.getId());
		}
		roles=roleService.getAll();
		Assert.assertTrue(roles.size()==0);
	}
	
	private void testDeleteAllRegion() {
		Region root=regionService.findQueryObject(new ObjectQuery().addWhere("lft=:lft","lft" ,1l ));
		if(root!=null){
			regionService.deleteCascade(root.getId());
		}
		List<Region> all=regionService.findAll(null);
		Assert.assertTrue(all.size()==0);
	}
	private void testDeleteAllContacts() {
		List<Contacts> allContacts=contactsService.getAll();
		for(Contacts con:allContacts){
			contactsService.delete(con.getId());
		}
		allContacts=contactsService.getAll();
		Assert.assertTrue(allContacts.size()==0);
	}
	
	public void testDeleteAllTimerJob(){
		List<TimerJob> jobs=timerJobService.getAll();
		for(TimerJob job:jobs){
			timerJobService.delete(job.getId());
		}
		jobs=timerJobService.getAll();
		Assert.assertTrue(jobs.size()==0);
	}
	
	public void testDeleteAllBaseDataType(){
		List<BaseDataType> allTypes=baseDataTypeService.getAll();
		for(BaseDataType dataType:allTypes){
			baseDataTypeService.delete(dataType.getId());
		}
		allTypes=baseDataTypeService.getAll();
		Assert.assertTrue(allTypes.size()==0);
	}
	
	public void testDeleteAllBaseData(){
		List<BaseData> allTypes=baseDataService.getAll();
		for(BaseData dataType:allTypes){
			baseDataService.delete(dataType.getId());
		}
		allTypes=baseDataService.getAll();
		Assert.assertTrue(allTypes.size()==0);
	}
	

	public void testDeleteAllEmployee(){
		List<Employee> allemps=employeeService.getAll();
		for(Employee emp:allemps){
			employeeService.delete(emp.getId());
		}
		allemps=employeeService.getAll();
		Assert.assertTrue(allemps.size()==0);
	}
	
	public void testDeleteAllDepartment(){
		List<Department> alldepts=departmentService.getAll();
		for(Department dept:alldepts){
			departmentService.delete(dept.getId());
		}
		alldepts=departmentService.getAll();
		Assert.assertTrue(alldepts.size()==0);
	}
	
	public void testDeleteAllBusinessUnit(){
		List<BusinessUnits> allbus=businessUnitsService.getAll();
		for(BusinessUnits bu:allbus){
			businessUnitsService.delete(bu.getId());
		}
		allbus=businessUnitsService.getAll();
		Assert.assertTrue(allbus.size()==0);
	}
	
	public void testDeleteAllProduct(){
		List<Product> allProducts=productService.getAll();
		for(Product p:allProducts){
			productService.delete(p.getId());
		}
		allProducts=productService.getAll();
		Assert.assertTrue(allProducts.size()==0);
	}

	public void testDeleteAllProductType() {
		ProductType type=productTypeService.findQueryObject(new ObjectQuery().addWhere("lft=:lft","lft" ,1l ));
		if(type!=null){
			productTypeService.deleteCascade(type.getId());
		}
		List<ProductType> types=productTypeService.findAll(null);
		Assert.assertTrue(types.size()==0);
	}
}
