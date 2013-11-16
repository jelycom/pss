package cn.jely.cd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cn.jely.cd.sys.ActionResourceServiceTest;
import cn.jely.cd.sys.BusinessTypeServiceTest;
import cn.jely.cd.sys.PeriodARAPServiceTest;
import cn.jely.cd.sys.PeriodAccountServiceTest;
import cn.jely.cd.sys.PeriodStockServiceTest;
import cn.jely.cd.sys.RoleServiceTest;
import cn.jely.cd.sys.StateResourceOPServiceTest;
import cn.jely.cd.sys.StateServiceTest;

@RunWith(Suite.class)
@SuiteClasses({RegionServiceTest.class,BaseDataTypeServiceTest.class,
		ActionResourceServiceTest.class,RoleServiceTest.class,StateServiceTest.class,
		StateResourceOPServiceTest.class,FundAccountServiceTest.class,
		PeriodAccountServiceTest.class,PeriodStockServiceTest.class,PeriodARAPServiceTest.class,
		BaseDataServiceTest.class, DepartmentServiceTest.class,EmployeeServiceTest.class,
		BusinessUnitsServiceTest.class,	ContactsServiceTest.class,
		ProductTypeServiceTest.class,ProductServiceTest.class,ProductPlanPurchaseServiceTest.class,
		ProductPlanDeliveryServiceTest.class,ProductOrderBillPurchaseServiceTest.class,ProductOrderBillDeliveryServiceTest.class,
		ProductPurchaseServiceTest.class,ProductDeliveryServiceTest.class,BusinessTypeServiceTest.class})
public class AllTests {

}
