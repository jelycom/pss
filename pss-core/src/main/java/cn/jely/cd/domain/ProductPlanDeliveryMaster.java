package cn.jely.cd.domain;

import java.util.Date;

import cn.jely.cd.util.state.State;

/**
 * 出货计划主表
 * @author 周义礼
 *
 */
public class ProductPlanDeliveryMaster extends ProductPlanMaster implements  java.io.Serializable {



	// Constructors

	/** default constructor */
	public ProductPlanDeliveryMaster() {
	}

	
	public ProductPlanDeliveryMaster(Employee planEmployee,
			Employee executeEmployee, Date startDate, Date endDate, State state) {
		super(planEmployee,executeEmployee,startDate,endDate,state);
	}
//
//
//	/** full constructor */
//	public ProductPurchasePlanMaster(Employee planEmployee, Employee executeEmployee,
//			Date startDate, Date endDate, String state,String planItem) {
//		this.planEmployee = planEmployee;
//		this.executeEmployee = executeEmployee;
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.state = state;
//		this.item = planItem;
//	}

}