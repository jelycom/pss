package cn.jely.cd.domain;

import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import cn.jely.cd.util.state.State;


public class ProductPlanPurchaseMaster extends ProductPlanMaster implements  java.io.Serializable {

	// Constructors

	/** default constructor */
	public ProductPlanPurchaseMaster() {
	}

	
	public ProductPlanPurchaseMaster(Employee planEmployee,
			Employee executeEmployee, Date startDate, Date endDate, State state) {
		super(planEmployee,executeEmployee,startDate,endDate,state);
	}

}