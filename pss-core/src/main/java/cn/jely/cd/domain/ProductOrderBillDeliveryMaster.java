package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.Date;

import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.util.state.State;

/**
 * @ClassName:ProductOrderBillPurchaseMaster
 * Description:产品销售订单主表
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-5-27 下午2:04:07
 *
 */
public class ProductOrderBillDeliveryMaster extends ProductOrderBillMaster {

	public ProductOrderBillDeliveryMaster() {
		super();
	}

	public ProductOrderBillDeliveryMaster(BusinessUnits businessunit, Employee employee) {
		super(businessunit, employee);
	}

	public ProductOrderBillDeliveryMaster(BusinessUnits businessUnit, Warehouse warehouse, Employee employee,
			FundAccount fundaccount) {
		super(businessUnit, warehouse, employee, fundaccount);
	}

	public ProductOrderBillDeliveryMaster(Long id, String item, BusinessUnits businessUnit, Contacts contactor,
			Warehouse warehouse, Employee employee, FundAccount fundaccount, BaseData businessType,
			BaseData invoicType, String invoiceNo, AccountingPeriod accountingPeriodId, String deliveryItem,
			Date deliveryDate, String deliveryAddress, BigDecimal subTotal, BigDecimal valueAddTax, BigDecimal amount,
			BigDecimal arap, BigDecimal remainArap, String memos, State state) {
		super(id, item, businessUnit, contactor, warehouse, employee, fundaccount, businessType, invoicType, invoiceNo,
				accountingPeriodId, deliveryItem, deliveryDate, deliveryAddress, subTotal, valueAddTax, amount, arap,
				remainArap, memos, state);
	}
	
}
