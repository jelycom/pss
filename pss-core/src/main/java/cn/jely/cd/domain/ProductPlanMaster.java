package cn.jely.cd.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import cn.jely.cd.pagemodel.ProductPlanDetailPM;
import cn.jely.cd.pagemodel.ProductPlanMasterPM;
import cn.jely.cd.pagemodel.ToPageModel;
import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-16 下午1:52:02
 */
public class ProductPlanMaster implements java.io.Serializable, IStateAble, IItemAble, Cloneable,
		ToPageModel<ProductPlanMasterPM> {

	/** long:serialVersionUID: */
	private static final long serialVersionUID = 1L;
	private Long id;
	private List<ProductPlanDetail> details = new ArrayList<ProductPlanDetail>();
	/** Date:billDate:单据录入日期 */
	private Date billDate;
	/** Date:startDate:计划开始日期 */
	private Date startDate;
	/** Date:endDate:计划结束日期 */
	private Date endDate;
	/** Employee:planEmployee:计划人 */
	private Employee planEmployee;
	/** Employee:executeEmployee:执行人 */
	private Employee executeEmployee;
	private InfoComponent info = new InfoComponent();
	private String item;
	private State state;
	private Integer version;
	private String memos;

	public ProductPlanMaster() {
	}

	public ProductPlanMaster(Long id) {
		super();
		this.id = id;
	}

	public ProductPlanMaster(Employee planEmployee, Employee executeEmployee, Date startDate, Date endDate, State state) {
		this.planEmployee = planEmployee;
		this.executeEmployee = executeEmployee;
		this.startDate = startDate;
		this.endDate = endDate;
		this.state = state;
	}

	public List<ProductPlanDetail> getDetails() {
		return details;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getEndDate() {
		return endDate;
	}

	public Employee getExecuteEmployee() {
		return executeEmployee;
	}

	public Long getId() {
		return id;
	}

	public InfoComponent getInfo() {
		return info;
	}

	public String getItem() {
		return item;
	}

	public Employee getPlanEmployee() {
		return planEmployee;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getStartDate() {
		return startDate;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	private Integer getVersion() {
		return version;
	}

	public void setDetails(List<ProductPlanDetail> details) {
		this.details = details;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setExecuteEmployee(Employee executeEmployee) {
		this.executeEmployee = executeEmployee;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setInfo(InfoComponent info) {
		this.info = info;
	}

	public void setItem(String planItem) {
		this.item = planItem;
	}

	public void setPlanEmployee(Employee planEmployee) {
		this.planEmployee = planEmployee;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	/**
	 * 改访问修饰符为private的原因是避免被人为设置Version
	 */
	private void setVersion(Integer version) {
		this.version = version;
	}

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public ProductPlanMaster toDisp(boolean withDetail) {
		ProductPlanMaster master = null;
		try {
			master = (ProductPlanMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setPlanEmployee(new Employee(this.getPlanEmployee().getId(), this.getPlanEmployee().getName()));
		master.setExecuteEmployee(new Employee(this.getExecuteEmployee().getId(), this.getExecuteEmployee().getName()));
		if (withDetail) {
			List<ProductPlanDetail> details = master.getDetails();
			int size = details.size();
			for (int i = 0; i < size; i++) {
				ProductPlanDetail masterdetail = details.get(i);
				masterdetail.setProductPlanMaster(null);
				Product product = masterdetail.getProduct();
				masterdetail.setProduct(new Product(product.getId(), product.getFullName(), product.getItem(), product
						.getSpecification(), product.getUnit(), product.getColor()));
			}
		} else {
			master.getDetails().clear();
		}
		return master;
	}

	@Override
	public ProductPlanMasterPM convert(boolean withDetail) {
		ProductPlanMasterPM masterPM = new ProductPlanMasterPM();
		org.springframework.beans.BeanUtils.copyProperties(this, masterPM, new String[] { "details" });
		Employee executeEmployee = this.getExecuteEmployee();
		Employee planEmployee = this.getPlanEmployee();
		if (executeEmployee != null) {
			masterPM.setExecuteEmployeeId(executeEmployee.getId());
			masterPM.setExecuteEmployeeName(executeEmployee.getName());
		}
		if (planEmployee != null) {
			masterPM.setPlanEmployeeId(planEmployee.getId());
			masterPM.setPlanEmployeeName(planEmployee.getName());
		}
		if (withDetail) {
			List<ProductPlanDetail> details = this.getDetails();
			int size = details.size();
			System.out.println(size);
			for (int i = 0; i < size; i++) {
				ProductPlanDetailPM detailpm = new ProductPlanDetailPM();
				ProductPlanDetail detail = details.get(i);
				org.springframework.beans.BeanUtils.copyProperties(detail, detailpm);
				Product product = detail.getProduct();
				detailpm.setProductId(product.getId());
				detailpm.setFullName(product.getFullName());
				detailpm.setSpecification(product.getSpecification());
				detailpm.setColor(product.getColor());
				detailpm.setUnit(product.getUnit());
				masterPM.getDetails().add(detailpm);
			}
		}
		return masterPM;
	}
}
