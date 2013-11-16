package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.Date;

import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.util.state.State;



/**
 * @ClassName:ProductOrderBillPurchaseMaster
 * Description:产品采购订单主表
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-5-27 下午2:04:07
 *
 */
public class ProductOrderBillPurchaseMaster extends ProductOrderBillMaster {

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = -4891008439598169757L;

	public ProductOrderBillPurchaseMaster() {
		super();
	}

	public ProductOrderBillPurchaseMaster(BusinessUnits businessunit, Employee employee) {
		super(businessunit, employee);
	}

	public ProductOrderBillPurchaseMaster(BusinessUnits businessUnit, Warehouse warehouse, Employee employee,
			FundAccount fundaccount) {
		super(businessUnit, warehouse, employee, fundaccount);
	}

	public ProductOrderBillPurchaseMaster(Long id, String item, BusinessUnits businessUnit, Contacts contactor,
			Warehouse warehouse, Employee employee, FundAccount fundaccount, BaseData businessType,
			BaseData invoicType, String invoiceNo, AccountingPeriod accountingPeriodId, String deliveryItem,
			Date deliveryDate, String deliveryAddress, BigDecimal subTotal, BigDecimal valueAddTax, BigDecimal amount,
			BigDecimal arap, BigDecimal remainArap, String memos, State state) {
		super(id, item, businessUnit, contactor, warehouse, employee, fundaccount, businessType, invoicType, invoiceNo,
				accountingPeriodId, deliveryItem, deliveryDate, deliveryAddress, subTotal, valueAddTax, amount, arap,
				remainArap, memos, state);
	}

}
