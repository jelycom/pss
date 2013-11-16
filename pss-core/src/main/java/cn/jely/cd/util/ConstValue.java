package cn.jely.cd.util;

import java.math.BigDecimal;

public interface ConstValue {
	public static final String JOB_AUTOCLEARJOB = "autoClear";

	public static final String LOGIN_ACTION = "LoginAction";
	public static final String CONTEXT_DEPTS = "depts";
	public static final String CONTEXT_EMPS = "emps";
	public static final String CONTEXT_BASEDATA_TYPES = "allTypes";
	public static final String CONTEXT_DEPARTMENT_MANAGERS = "managers";
	public static final String CONTEXT_APPLICATION_MODELS = "models";
	public static final String CONTEXT_APPLICATION_OPERATIONS = "operations";
	public static final String CONTEXT_ROLES = "allroles";
	public static final String JSON_ROOT = "linked";
	//
	public static final String BASEDATA_APPLICATIONMODEL = "ApplicationModel";
	public static final String BASEDATA_OPERATION = "Operation";
	public static final String ALLBASEDATATYPE = "allBaseDataType";
	public static final String ALLBASEDATA = "allBaseData";

	public static final String BASE_PRODUCT_BRAND = "productBrand";
	public static final String BASE_INVOICE_TYPE = "invoiceType";
	public static final String BASE_SUPPLIER_LEVEL = "supplierLevel";
	public static final String BASE_SUPPLIER_TYPE = "supplierType";
	public static final String BASE_CUSTOMER_LEVEL = "customerLevel";
	public static final String BASE_CUSTOMER_TYPE = "customerType";
	public static final String BASE_PRODUCT_UNIT = "productUnit";
	public static final String BASE_ROLE_GROUP = "roleGroup";
	public static final String BASE_BILL_STATE = "billState";
	/* SERVLET表示放入ServletContext中 */
	public static final String SERVLET_COMPANYINFO = "companyInfo";

	public static final String SYS_SETTING = "SystemSetting";// 系统舍入逻辑
	public static final String SYS_ROUNDINGMODE = "sys_roundingmode";// 系统舍入逻辑
	public static final String SYS_AMOUNTSCALE = "sys_amountscale";// 金额小数位
	public static final String SYS_PRICESCALE = "sys_pricescale";// 单价小数位
	public static final String SYS_LOCALE = "sys_locale";// 系统Locale
	public static final String SYS_BATCHINITSTOCK = "sys_batchinitstock";// 是否允许同一产品分批次设置期初

	public static final String CONTEXTPATH = "contextpath";
	public static final String VALIDATECODE = "validateCode";
	public static final String USERSKIN = "skin"; //用户自定义的样式键
	public static final String DEFAULTSKIN = "gray";//默认的样式

	public static final String REPORT_DESIGNPATH = "/report/design";
	public static final String REPORT_COMPILEPATH = "/report/compile";
	public static final String REPORT_SAVEPATH = "/report/save";
}
