/*
 * 捷利商业进销存管理系统
 * @(#)SystemServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-1
 */
package cn.jely.cd.sys.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import cn.jely.cd.domain.BaseData;
import cn.jely.cd.domain.BaseDataType;
import cn.jely.cd.domain.Bursary;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Contacts;
import cn.jely.cd.domain.Department;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductOrderBillDeliveryMaster;
import cn.jely.cd.domain.ProductOrderBillPurchaseMaster;
import cn.jely.cd.domain.ProductPlanDeliveryMaster;
import cn.jely.cd.domain.ProductPlanPurchaseMaster;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Region;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IBaseDataService;
import cn.jely.cd.service.IBaseDataTypeService;
import cn.jely.cd.service.IBursaryService;
import cn.jely.cd.service.IBusinessUnitsService;
import cn.jely.cd.service.IContactsService;
import cn.jely.cd.service.IDepartmentService;
import cn.jely.cd.service.IEmployeeService;
import cn.jely.cd.service.IFundAccountService;
import cn.jely.cd.service.IProductOrderBillDeliveryService;
import cn.jely.cd.service.IProductOrderBillPurchaseService;
import cn.jely.cd.service.IProductPlanDeliveryService;
import cn.jely.cd.service.IProductPlanPurchaseService;
import cn.jely.cd.service.IProductService;
import cn.jely.cd.service.IProductTypeService;
import cn.jely.cd.service.IRegionService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.sys.domain.Role;
import cn.jely.cd.sys.domain.User;
import cn.jely.cd.sys.service.IActionResourceService;
import cn.jely.cd.sys.service.ICompanyInfoService;
import cn.jely.cd.sys.service.IRoleService;
import cn.jely.cd.sys.service.IStateResourceOPService;
import cn.jely.cd.sys.service.IStateService;
import cn.jely.cd.sys.service.ISystemService;
import cn.jely.cd.sys.service.IUserService;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.PinYinUtils;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryGroup;
import cn.jely.cd.util.query.QueryRule;

/**
 * @ClassName:SystemServiceImpl
 * @author 周义礼
 * @version 2013-4-1 下午3:05:48
 * 
 */
public class SystemServiceImpl implements ISystemService {
	
	private IRoleService roleService;
	private IDepartmentService departmentService;
	private IEmployeeService employeeService;
	private IBaseDataTypeService baseDataTypeService;
	private IBaseDataService baseDataService;
	private IProductTypeService productTypeService;
	private IFundAccountService fundAccountService;
	private IBusinessUnitsService businessUnitsService;
	private ICompanyInfoService companyInfoService;
	private IRegionService regionService;
	private IWarehouseService warehouseService;
	private IContactsService contactsService;
	private IProductService productService;
	private IStateService stateService;
	private IStateResourceOPService stateResourceOPService;
	private IActionResourceService actionResourceService;
	private IBursaryService bursaryService;
	private IUserService userService;
	
	private IProductPlanPurchaseService productPlanPurchaseService;
	private IProductPlanDeliveryService productPlanDeliveryService;
	private IProductOrderBillPurchaseService productOrderBillPurchaseService;
	private IProductOrderBillDeliveryService productOrderBillDeliveryService;

	@Override
	public Boolean updaterepair() {
		deleteData();
		saveinitData();
		return null;
	}

	@Override
	public Boolean saveinitData() {
		initActionResource();
		initRole();
		initEmployee();
		initRegion();
		initWarehouse();
		initBaseDataType();
		initBaseData();
		initProductType();
//		initState();
//		initStateResourceOP();
		initFundaccount();
		initBusinessUnits();
		initBursary();
		return true;
	}


	/** 删除数据 */
	private void deleteData() {
		deleteAllOrderBill();
		deleteAllPlan();
		bursaryService.delete(null, null);
		baseDataService.delete(null, null);
		baseDataTypeService.delete(null, null);
		productService.delete(null, null);
		productTypeService.delete(null, null);
		fundAccountService.delete(null, null);
//		stateResourceOPService.delete(null, null);
//		stateService.delete(null,null);
		contactsService.delete(null, null);
		businessUnitsService.delete(null, null);
		warehouseService.delete(null, null);
//		roleService.delete(null, null);
//		actionResourceService.delete(null, null);
		deleteAllEmployee();
		deleteAllRole();
		deleteAllResource();
		employeeService.delete(null, null);
//		companyInfoService.delete(null, null);
		regionService.delete(null, null);
	}

	/**删除所有资源
	 * void
	 */
	private void deleteAllResource() {
		List<ActionResource> resouces = actionResourceService.getAll();
		for (ActionResource resource : resouces) {
			actionResourceService.delete(resource.getId());
		}
	}

	/**
	 * void
	 */
	private void deleteAllRole() {
		List<Role> roles = roleService.getAll();
		for (Role role : roles) {
			roleService.delete(role.getId());
		}
	}

	/** 删除所有用户
	 * void
	 */
	private void deleteAllEmployee() {
		List<Employee> allEmps = employeeService.getAll();
		for (Employee emp : allEmps) {
			employeeService.delete(emp.getId());
		}
	}

	/** 删除所有产品计划*/
	private void deleteAllPlan() {
		for(ProductPlanPurchaseMaster master:productPlanPurchaseService.getAll()){
			productPlanPurchaseService.delete(master.getId());
		}
		for(ProductPlanDeliveryMaster master:productPlanDeliveryService.getAll()){
			productPlanDeliveryService.delete(master.getId());
		}
		
	}

	/**删除所有产品订单*/
	private void deleteAllOrderBill() {
		for(ProductOrderBillPurchaseMaster master:productOrderBillPurchaseService.getAll()){
			productOrderBillPurchaseService.delete(master.getId());
		}
		for(ProductOrderBillDeliveryMaster master:productOrderBillDeliveryService.getAll()){
			productOrderBillDeliveryService.delete(master.getId());
		}
		
	}

	/************* 初始化数据部分开始 ************/

	/** */
//	private void initState() {
//		State state=new State("新建",true, false);
//		stateService.save(state);
//		state=new State("待审",false,false);
//		stateService.save(state);
//		state=new State("审核中",false,false);
//		stateService.save(state);
//		state=new State("审核通过",false,true);
//		stateService.save(state);
//		state=new State("审核未通过",false,false);
//		stateService.save(state);
//		state=new State("中止",false,false);
//		stateService.save(state);
//	}
	
	
	/**
	 * void
	 */
	private void initEmployee() {
		HashSet<Role> roles = new HashSet<Role>();
		ObjectQuery objectQuery=new ObjectQuery();
		QueryGroup queryGroup = new QueryGroup();
		queryGroup.getRules().add(new QueryRule("name", FieldOperation.eq, "user"));
		objectQuery.setQueryGroup(queryGroup);
		roles.add(roleService.findQueryObject(objectQuery));
		Department dept=new Department("默认部门");
		departmentService.save(dept);
		Employee admin=new Employee("张无忌");
		admin.setDepartment(dept);
		User adm = new User("admin","admin");
		adm.setRoles(new HashSet<Role>(roleService.getAll()));
		admin.setUser(adm);
		employeeService.save(admin);
		Employee emp2=new Employee("李寻欢");
		emp2.setDepartment(dept);
		User user = new User("user","user");
		emp2.setUser(user);
		user.setRoles(roles);
		userService.save(user);
		employeeService.save(emp2);
	}


	
	private void initBursary(){
		Bursary bursary=new Bursary("","会计科目","会计科目",true);
		bursaryService.save(bursary,null);
		Bursary assets=new Bursary("01","资产","资产",true);
		bursaryService.save(assets,bursary.getId());
		Bursary cash=new Bursary("1001","现金","现金",true);
		bursaryService.save(cash,assets.getId());
		Bursary bankAccount=new Bursary("1002","银行存款","银行存款",true);
		bursaryService.save(bankAccount,assets.getId());
		Bursary receiveable=new Bursary("1122", "应收帐款", "应收帐款",true);
		bursaryService.save(receiveable,assets.getId());
		Bursary advance=new Bursary("1123", "预付帐款", "预付帐款",true);
		bursaryService.save(advance,assets.getId());
		Bursary otherReceiveable=new Bursary("1221", "其它应收款", "其它应收款",true);
		bursaryService.save(otherReceiveable,assets.getId());
		Bursary stockProduct=new Bursary("1405", "库存商品", "库存商品",true);
		bursaryService.save(stockProduct,assets.getId());
		
		Bursary liability=new Bursary("02", "负债", "负债",true);
		bursaryService.save(liability,bursary.getId());
		Bursary payable=new Bursary("2202", "应付帐款", "应付帐款",true);
		bursaryService.save(payable,liability.getId());
		Bursary prepaid=new Bursary("2203", "预收帐款", "预收帐款",true);
		bursaryService.save(prepaid,liability.getId());
		Bursary salary=new Bursary("2211", "应付职工薪酬", "应付薪酬",true);
		bursaryService.save(salary,liability.getId());
		Bursary tax=new Bursary("2221", "应交税费", "应交税费",true);
		bursaryService.save(tax,liability.getId());
		bursaryService.save(new Bursary("2241", "其它应付款", "其它应付款",true), liability.getId());
		
		Bursary income=new Bursary("03","收入","收入",true);
		bursaryService.save(income,bursary.getId());
		Bursary otherincome = new Bursary("6051","其它业务收入","其它收入",true);
		bursaryService.save(otherincome, income.getId());
		bursaryService.save(new Bursary("6051001","调帐收入","调帐收入",false),otherincome.getId());
		bursaryService.save(new Bursary("6051002", "利息收入", "利息收入",false),otherincome.getId());
		bursaryService.save(new Bursary("6051003", "废品收入", "废品收入",false),otherincome.getId());
		
		Bursary outcome=new Bursary("04", "支出", "支出",true);
		bursaryService.save(outcome,bursary.getId());
		Bursary otheroutcome = new Bursary("6402", "其它业务支出","其它支出",true);
		bursaryService.save(otheroutcome,outcome.getId());
		bursaryService.save(new Bursary("6402001","调帐亏损","调帐亏损",false),otheroutcome.getId());
		bursaryService.save(new Bursary("6402002","发货费用","发货费用",false),otheroutcome.getId());
		Bursary ownershipinterest=new Bursary("05", "所有者权益", "权益",true);
		bursaryService.save(ownershipinterest, bursary.getId());
	}
	
	private void initRole(){
		Role adminRole=new Role("administrator", "超级管理员");
		List<ActionResource> resources=actionResourceService.findAll(new ObjectQuery());
		adminRole.setResources(resources);
		roleService.save(adminRole);
		Role userRole=new Role("user", "普通用户");
		List<ActionResource> userresources=actionResourceService.findAll(new ObjectQuery());
		userresources.removeAll(actionResourceService.findSysResources());
		userRole.setResources(userresources);
		roleService.save(userRole);
		System.out.println("ddd");
	}
	
	private void initActionResource(){
		ActionResource root=new ActionResource("系统资源", "","系统菜单",null,true);
		actionResourceService.save(root,null);
		ActionResource child=new ActionResource("基础数据","","基础数据",null,true);
		actionResourceService.save(child, root.getId());
		initResourceByMethod(child,"产品类别","cn.jely.cd.web.ProductTypeAction","productType", true);
		initResourceByMethod(child,"产品","cn.jely.cd.web.ProductAction","product", true);
		initResourceByMethod(child,"部门","cn.jely.cd.web.DepartmentAction","department", true);
		initResourceByMethod(child,"雇员","cn.jely.cd.web.EmployeeAction","employee", true);
		ActionResource grandson= new ActionResource("查询雇员资源", "cn.jely.cd.web.EmployeeAction.findemployeemenus", null,null, true);
		actionResourceService.save(grandson, child.getId());
		initResourceByMethod(child,"往来单位","cn.jely.cd.web.BusinessUnitsAction", "businessUnits",true);
		initResourceByMethod(child,"资金帐户","cn.jely.cd.web.FundAccountAction", "fundAccount",true);
		initResourceByMethod(child,"会计科目","cn.jely.cd.web.BursaryAction", "bursary",true);
		initResourceByMethod(child,"地区","cn.jely.cd.web.RegionAction", "region",true);
		initResourceByMethod(child,"数据字典","cn.jely.cd.web.BaseDataTypeAction", "baseDataType",true);
		initResourceByMethod(child,"仓库","cn.jely.cd.web.WarehouseAction", "warehouse",true);
		child=new ActionResource("进货管理","","进货管理",null,false);
		actionResourceService.save(child,root.getId());
		initResourceByMethod(child,"采购计划","cn.jely.cd.web.ProductPlanPurchaseAction", "productPlanPurchase",false);
		initResourceByMethod(child,"采购订单","cn.jely.cd.web.ProductOrderBillPurchaseAction", "productOrderBillPurchase",false);
		initResourceByMethod(child,"采购收货","cn.jely.cd.web.ProductPurchaseAction", "productPurchase",false);
		initResourceByMethod(child,"采购退货","cn.jely.cd.web.ProductPurchaseReturnAction", "productPurchaseReturn",false);
		initResourceByMethod(child,"采购付款","cn.jely.cd.web.AccountOutAction","accountOut", false);
		child=new ActionResource("出货管理","","出货管理",null,false);
		actionResourceService.save(child,root.getId());
		initResourceByMethod(child,"销售计划","cn.jely.cd.web.ProductPlanDeliveryAction", "productPlanDelivery",false);
		initResourceByMethod(child,"销售订单","cn.jely.cd.web.ProductOrderBillDeliveryAction","productOrderBillDelivery", false);
		initResourceByMethod(child,"销售出货","cn.jely.cd.web.ProductDeliveryAction","productDelivery", false);
		initResourceByMethod(child,"销售退货","cn.jely.cd.web.ProductDeliveryReturnAction","productDeliveryReturn", false);
		initResourceByMethod(child,"销售收款","cn.jely.cd.web.AccountInAction","accountIn", false);
		child=new ActionResource("库存管理","","库存管理",null,false);
		actionResourceService.save(child,root.getId());
		initResourceByMethod(child, "同价调拨", "cn.jely.cd.web.ProductTransferSameAction", "productTransferSame",false);
		initResourceByMethod(child, "异价调拨", "cn.jely.cd.web.ProductTransferDiffAction", "productTransferDiff",false);
		initResourceByMethod(child, "盘点单", "cn.jely.cd.web.ProductStockingAction", "productStocking",false);
		initResourceByMethod(child, "盘盈单", "cn.jely.cd.web.InventoryProfitAction", "inventoryProfit",false);
		initResourceByMethod(child, "盘亏单", "cn.jely.cd.web.InventoryLossAction", "inventoryLoss",false);
		initResourceByMethod(child, "库存分布", "cn.jely.cd.web.ProductStockAction", "productStock",false);
		ActionResource productStockResource=new ActionResource("类别产品库存", "cn.jely.cd.web.ProductStockAction.findrealstockquantity", "类别库存","productStock_findrealstockquantity.action",false);
		actionResourceService.save(productStockResource, child.getId());
		child=new ActionResource("钱流管理","","钱流管理",null,false);
		actionResourceService.save(child,root.getId());
		initResourceByMethod(child, "其它收入", "cn.jely.cd.web.AccountOtherInAction","accountOtherIn", false);
		initResourceByMethod(child, "其它支出", "cn.jely.cd.web.AccountOtherOutAction","accountOtherOut", false);
		initResourceByMethod(child, "帐户互转", "cn.jely.cd.web.AccountTransferAction","accountTransfer", false);
		child=new ActionResource("期初数据","","期初数据",null,true);
		actionResourceService.save(child,root.getId());
		initResourceByMethod(child, "期初应收应付", "cn.jely.cd.sys.web.PeriodARAPAction", "sys/periodARAP",true);
		initResourceByMethod(child, "期初库存", "cn.jely.cd.sys.web.PeriodStockAction", "sys/periodStock",true);
		initResourceByMethod(child, "期初资金", "cn.jely.cd.sys.web.PeriodAccountAction", "sys/periodAccount",true);
		ActionResource openPeriod = new ActionResource("系统开帐", "cn.jely.cd.sys.web.AccountingPeriodAction.saveinitperiod", "系统开帐","sys/accountingPeriod_saveinitperiod.action",true);
		actionResourceService.save(openPeriod,child.getId());
		ActionResource unOpenPeriod = new ActionResource("系统反开帐", "cn.jely.cd.sys.web.AccountingPeriodAction.saveunstartinitperiod", "系统反开帐","sys/accountingPeriod_saveunstartinitperiod.action",true);
		actionResourceService.save(unOpenPeriod,child.getId());
		child=new ActionResource("查询统计","","查询统计","",false);
		actionResourceService.save(child,root.getId());
		
		child=new ActionResource("系统维护","","系统维护","",true);
		actionResourceService.save(child,root.getId());
		initResourceByMethod(child, "帐户", "cn.jely.cd.web.EmployeeAction", "employee",true);
		initResourceByMethod(child, "角色", "cn.jely.cd.sys.web.RoleAction", "sys/role",true);
		initResourceByMethod(child, "资源", "cn.jely.cd.sys.web.ActionResourceAction","sys/actionResource", true);
		initResourceByMethod(child, "单据编号", "cn.jely.cd.web.ActionResourceAction", "sys/code",true );
		
	}

	/**
	 * 根据不同的模块初始化资源
	 * @Title:initResourceByMethod
	 * @param root 父结点 modelName:模块名称 className:类名称 void
	 * @param isinit 期初使用，true:是，则未开帐时可以使用。否则不可用
	 */
	private void initResourceByMethod(ActionResource root,String modelName,String className, String urlPrefix,boolean isinit) {
		ActionResource child=new ActionResource(modelName+"管理", "",modelName+"管理",urlPrefix+"_list.action",isinit);
		actionResourceService.save(child,root.getId());
		Long childId = child.getId();
		ActionResource resource=new ActionResource(modelName+"列表", className+".list",null,null,isinit);
		actionResourceService.save(resource,childId);
		resource=new ActionResource(modelName+"保存", className+".save",null,null,isinit);
		actionResourceService.save(resource,childId);
		resource=new ActionResource("所有"+modelName+"列表", className+".listall",null,null,isinit);
		actionResourceService.save(resource,childId);
		resource=new ActionResource(modelName+"分页列表", className+".listjson",null,null,isinit);
		actionResourceService.save(resource,childId);
		resource=new ActionResource("删除"+modelName, className+".delete",null,null,isinit);
		actionResourceService.save(resource,childId);
	}
	/** */
	private void initStateResourceOP() {
		
	}
	/** 初始化区域 */
	private void initRegion() {
		Region region = new Region("000000", "区域树");
		regionService.save(region,null);
		Region region1 = new Region("中国");
		regionService.save(region1,region.getId());
		Region region1_1 = new Region("四川");
		regionService.save(region1_1,region1.getId());
		Region region1_1_1 = new Region("成都");
		regionService.save(region1_1_1,region1_1.getId());
		Region region1_1_2 = new Region("绵阳");
		regionService.save(region1_1_2,region1_1.getId());
		Region region1_1_3 = new Region("德阳");
		regionService.save(region1_1_3,region1_1.getId());
		Region region1_2 = new Region("广西");
		regionService.save(region1_2,region1.getId());
		Region region1_3 = new Region("广东");
		regionService.save(region1_3,region1.getId());
	}

	private void initWarehouse(){
		Warehouse warehouse = new Warehouse("主仓库");
		Region region=regionService.findQueryObject(new ObjectQuery());
		warehouse.setRegion(region);
		warehouseService.save(warehouse);
	}


	/** 初始化往来单位 */
	private void initBusinessUnits() {
		BusinessUnits businessUnits = new BusinessUnits("1000000001", "捷利", "成都捷利电脑");
		businessUnitsService.save(businessUnits);
		initContacts(businessUnits);
	}

	/** 初始化往来单位联系人 */
	private void initContacts(BusinessUnits businessUnits) {
		Contacts contacts=new Contacts(businessUnits, "name", "py", "englishName", "职务", "028-65465465", "13564654897", "abc@落叶归根df", "纪念日说明:dateDescription");
		contactsService.save(contacts);
	}
	/** 初始化帐户 */
	private void initFundaccount() {
		FundAccount fundaccount = new FundAccount("现金", "xj");
		fundAccountService.save(fundaccount);
	}

	/** 初始化产品类别 */
	private void initProductType() {
		ProductType productType = new ProductType("产品分类");
		productType.setItem(UUID.randomUUID().toString());
		productType.setPy("cpfl");
		productTypeService.save(productType, null);
		ObjectQuery objectQuery = new ObjectQuery();
		QueryGroup queryGroup = new QueryGroup();
		queryGroup.getRules().add(new QueryRule("name", FieldOperation.eq, "产品分类"));
		objectQuery.setQueryGroup(queryGroup);
		ProductType rootType = productTypeService.findQueryObject(objectQuery);
		if (rootType != null) {
			String name = "电脑整机";
			productType = new ProductType(name, UUID.randomUUID().toString(), PinYinUtils.getPinYinShengMu(name));
			productTypeService.save(productType, rootType.getId());
			Product product=new Product(productType,CostMethod.FIFO,"","测试产品","产品全名");
			productService.save(product);
			name = "电脑配件";
			productType = new ProductType(name, UUID.randomUUID().toString(), PinYinUtils.getPinYinShengMu(name));
			productTypeService.save(productType, rootType.getId());
			name = "办公耗材";
			productType = new ProductType(name, UUID.randomUUID().toString(), PinYinUtils.getPinYinShengMu(name));
			productTypeService.save(productType, rootType.getId());
			name = "软件";
			productType = new ProductType(name, UUID.randomUUID().toString(), PinYinUtils.getPinYinShengMu(name));
			productTypeService.save(productType, rootType.getId());
		}
	}
	
	private void initProduct(){
		Product product=new Product();
		
	}

	/**
	 * 初始化分类的数据
	 * 
	 * @Title:initData
	 */
	private void initBaseData() {
		ObjectQuery objectQuery = new ObjectQuery();
		objectQuery.addWhere(" sn = :sn ", "sn",ConstValue.BASE_SUPPLIER_LEVEL);
		BaseDataType dataType = baseDataTypeService.findQueryObject(objectQuery);
		saveInitBaseData(new String[] { "一级供应商", "二级供应商", "临时供应商" }, dataType);
		// objectQuery.setSearchString(ConstValue.SUPPLIER_TYPE);
		objectQuery.getParamValueMap().put("sn", ConstValue.BASE_SUPPLIER_TYPE);
		dataType = baseDataTypeService.findQueryObject(objectQuery);
		saveInitBaseData(new String[] { "批发", "零售" }, dataType);
		// objectQuery.setSearchString(ConstValue.CUSTOMER_LEVEL);
		objectQuery.getParamValueMap().put("sn", ConstValue.BASE_CUSTOMER_LEVEL);
//		objectQuery = new ObjectQuery(" o.sn = ? ", ConstValue.BASE_CUSTOMER_LEVEL);
		dataType = baseDataTypeService.findQueryObject(objectQuery);
		saveInitBaseData(new String[] { "黄金客户", "白银客户", "普通会员", "临时客户" }, dataType);
		// objectQuery.setSearchString(ConstValue.CUSTOMER_TYPE);
		objectQuery.getParamValueMap().put("sn", ConstValue.BASE_CUSTOMER_TYPE);
//		objectQuery = new ObjectQuery(" o.sn = ? ", ConstValue.BASE_CUSTOMER_TYPE);
		dataType = baseDataTypeService.findQueryObject(objectQuery);
		saveInitBaseData(new String[] { "政府部门", "国营企业", "外(合)资企业", "民营企业", "个体商户" }, dataType);
//		objectQuery = new ObjectQuery(" o.sn = ? ", ConstValue.BASE_PRODUCT_UNIT);
		// objectQuery.setSearchString(ConstValue.PRODUCT_UNIT);
		objectQuery.getParamValueMap().put("sn", ConstValue.BASE_PRODUCT_UNIT);
		dataType = baseDataTypeService.findQueryObject(objectQuery);
		saveInitBaseData(new String[] { "个", "瓶", "条", "支", "筒", "张", "米" }, dataType);
//		objectQuery = new ObjectQuery(" o.sn = ? ", ConstValue.BASE_PRODUCT_BRAND);
		// objectQuery.setSearchString(ConstValue.PRODUCT_BRAND);
		objectQuery.getParamValueMap().put("sn", ConstValue.BASE_PRODUCT_BRAND);
		dataType = baseDataTypeService.findQueryObject(objectQuery);
		saveInitBaseData(new String[] { "联想", "方正", "华硕", "戴尔", "惠普", "英特尔", "AMD" }, dataType);
//		objectQuery = new ObjectQuery(" o.sn = ? ", ConstValue.BASE_INVOICE_TYPE);
		objectQuery.getParamValueMap().put("sn", ConstValue.BASE_INVOICE_TYPE);
		dataType = baseDataTypeService.findQueryObject(objectQuery);
		saveInitBaseData(new String[] { "收据", "普通销售发票", "建筑业发票", "增值税发票" }, new String[] { "0%", "4%", "3%", "17%" },	dataType);
		// objectQuery.setSearchString(ConstValue.INVOICE_TYPE);
//		objectQuery = new ObjectQuery(" o.sn = ? ", ConstValue.BASE_ROLE_GROUP);
		objectQuery.getParamValueMap().put("sn", ConstValue.BASE_ROLE_GROUP);
		dataType = baseDataTypeService.findQueryObject(objectQuery);
		saveInitBaseData(new String[] { "系统管理", "权限管理", "普通用户"}, dataType);
//		objectQuery = new ObjectQuery(" o.sn = ? ", ConstValue.BASE_BILL_STATE);
//		dataType = baseDataTypeService.findQueryObject(objectQuery);
//		saveInitBaseData(new String[] { "进货订单"}, new String[] { "PurchaseOrderBill"},	dataType);
	}

	private void saveInitBaseData(String[] names, BaseDataType dataType) {
		saveInitBaseData(names, null, dataType);
	}

	private void saveInitBaseData(String[] names, String[] values, BaseDataType dataType) {
		if (dataType != null && dataType.getId() > 0) {
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				BaseData baseData = new BaseData(name);
				if (values != null && values.length >= names.length) {
					String value = values[i];
					baseData.setValue(value);
				}
				baseData.setDataType(dataType);
				baseDataService.save(baseData);
			}
		}
	}

	/**
	 * 初始化分类
	 * 
	 * @Title:initDataType
	 */
	private void initBaseDataType() {
		BaseDataType baseDataType = new BaseDataType("数据字典", "systemDict");
		baseDataTypeService.save(baseDataType, null);
		ObjectQuery objectQuery = new ObjectQuery().addWhere("name=:name","name","数据字典");
		List<BaseDataType> result = baseDataTypeService.findAll(objectQuery);
		if (result != null && result.size() > 0) {
			BaseDataType root = result.get(0);
			Long rootId = root.getId();
			baseDataType = new BaseDataType("供应商级别", ConstValue.BASE_SUPPLIER_LEVEL);
			baseDataTypeService.save(baseDataType, rootId);
			baseDataType = new BaseDataType("供应商类别", ConstValue.BASE_SUPPLIER_TYPE);
			baseDataTypeService.save(baseDataType, rootId);
			baseDataType = new BaseDataType("客户级别", ConstValue.BASE_CUSTOMER_LEVEL);
			baseDataTypeService.save(baseDataType, rootId);
			baseDataType = new BaseDataType("客户类别", ConstValue.BASE_CUSTOMER_TYPE);
			baseDataTypeService.save(baseDataType, rootId);
			baseDataType = new BaseDataType("产品品牌", ConstValue.BASE_PRODUCT_BRAND);
			baseDataTypeService.save(baseDataType, rootId);
			baseDataType = new BaseDataType("产品单位", ConstValue.BASE_PRODUCT_UNIT);
			baseDataTypeService.save(baseDataType, rootId);
			baseDataType = new BaseDataType("权限分组", ConstValue.BASE_ROLE_GROUP);
			baseDataTypeService.save(baseDataType, rootId);
			baseDataType = new BaseDataType("发票类型", ConstValue.BASE_INVOICE_TYPE);
			baseDataTypeService.save(baseDataType, rootId);
//			baseDataType = new BaseDataType("单据状态", ConstValue.BASE_BILL_STATE);
//			baseDataTypeService.save(baseDataType, rootId);
		}
	}

	public void setBaseDataTypeService(IBaseDataTypeService baseDataTypeService) {
		this.baseDataTypeService = baseDataTypeService;
	}

	public void setBaseDataService(IBaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	public void setProductTypeService(IProductTypeService productTypeService) {
		this.productTypeService = productTypeService;
	}

	public void setFundAccountService(IFundAccountService fundAccountService) {
		this.fundAccountService = fundAccountService;
	}

	public void setBusinessUnitsService(IBusinessUnitsService businessUnitsService) {
		this.businessUnitsService = businessUnitsService;
	}

	public void setCompanyInfoService(ICompanyInfoService companyInfoService) {
		this.companyInfoService = companyInfoService;
	}

	public void setRegionService(IRegionService regionService) {
		this.regionService = regionService;
	}
	
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	public void setContactsService(IContactsService contactsService) {
		this.contactsService = contactsService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public void setStateService(IStateService stateService) {
		this.stateService = stateService;
	}
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setStateResourceOPService(IStateResourceOPService stateResourceOPService) {
		this.stateResourceOPService = stateResourceOPService;
	}

	public void setActionResourceService(IActionResourceService actionResourceService) {
		this.actionResourceService = actionResourceService;
	}

	public void setProductPlanPurchaseService(IProductPlanPurchaseService productPlanPurchaseService) {
		this.productPlanPurchaseService = productPlanPurchaseService;
	}

	public void setProductPlanDeliveryService(IProductPlanDeliveryService productPlanDeliveryService) {
		this.productPlanDeliveryService = productPlanDeliveryService;
	}

	public void setProductOrderBillPurchaseService(IProductOrderBillPurchaseService productOrderBillPurchaseService) {
		this.productOrderBillPurchaseService = productOrderBillPurchaseService;
	}

	public void setProductOrderBillDeliveryService(IProductOrderBillDeliveryService productOrderBillDeliveryService) {
		this.productOrderBillDeliveryService = productOrderBillDeliveryService;
	}

	public void setBursaryService(IBursaryService bursaryService) {
		this.bursaryService = bursaryService;
	}

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	
}
