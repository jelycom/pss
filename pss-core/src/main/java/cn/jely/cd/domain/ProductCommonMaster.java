package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jely.cd.pagemodel.ProductCommonDetailPM;
import cn.jely.cd.pagemodel.ProductCommonMasterPM;
import cn.jely.cd.pagemodel.ToPageModel;
import cn.jely.cd.sys.domain.BusinessType;
import cn.jely.cd.sys.domain.InfoComponent;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;



public class ProductCommonMaster implements java.io.Serializable,IStateAble,IItemAble,ToPageModel<ProductCommonMasterPM>,Cloneable {

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 5459871891427935726L;
	// Fields
	protected Long id;
	/** 单据编号 */
	protected String item;
	/** 业务类型(准备移到自定义业务中去)*/
	protected BusinessType businessType;
	/** 往来单位*/
	private BusinessUnits businessUnit;
	/** 单位联系人*/
	private Contacts contactor;
	/** 所影响的仓库*/
	protected Warehouse warehouse;
	/** 此单经手人*/
	protected Employee employee;
	/**单据信息*/
	protected InfoComponent info=new InfoComponent();
	/** 资金帐户*/
	protected FundAccount fundAccount;
	/** 发票类型*/
	private BaseData invoiceType;
	/**int:direction:业务方向,+1为增加,-1为减少*/
//	protected int direction;
	/** 发票号码*/
	private String invoiceNo;
	/** 业务单据发生日期*/
	protected Date billDate;
	/** 小计(不含税)*/
	protected BigDecimal subTotal;
	/** 税费*/
	protected BigDecimal valueAddTax;
	/**BigDecimal:prepare:预/暂收(付)款*/
	protected BigDecimal advance;
	/** @Fields discount:优惠,折让 */
	private BigDecimal discount;
	/** 总计(小计+税费+折扣/优惠)*/
	protected BigDecimal amount;
	/** 已付,在以下情况amount将不等于arap:一单总金额为100元,此次付了20元,那么应收应付就有80元
	 * 则此时:amount:100,paid:20,arap:80
	 * 如果在之前还有订单,并且订单也付了20元订金,则选择此单据后的各属性为:amount:100,prepare:20,paid:20,arap:60,
	 * ,下次冲销的时候将此单的arap及paidArap进行运算即可得出还有多少未付*/
	protected BigDecimal paid;
	/** 总计(应收应付)*/
	protected BigDecimal arap;
	/** 已收/付(应收应付)*/
	protected BigDecimal paidArap;
	/**boolean:arapComplete:应收应付是否已经完成*/
	protected boolean arapComplete;
	/** 状态*/
	protected State state;
	/**List<ProductPlanDeliveryMaster>:productPlans:关联计划*/
//	protected List<ProductPlanMaster> productPlans=new ArrayList<ProductPlanMaster>();
//	/**List<ProductOrderBillDeliveryMaster>:productOrderBills:关联定单*/
//	protected List<ProductOrderBillMaster> productOrderBills=new ArrayList<ProductOrderBillMaster>();
	/** 商品明细*/
	protected List<ProductCommonDetail> details=new ArrayList<ProductCommonDetail>();
	/** 备注*/
	protected String memos;
	
	
	public BusinessUnits getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(BusinessUnits businessUnit) {
		this.businessUnit = businessUnit;
	}
	public Contacts getContactor() {
		return contactor;
	}
	public void setContactor(Contacts contactor) {
		this.contactor = contactor;
	}
	public BaseData getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(BaseData invoicType) {
		this.invoiceType = invoicType;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public FundAccount getFundAccount() {
		return fundAccount;
	}
	public void setFundAccount(FundAccount fundaccount) {
		this.fundAccount = fundaccount;
	}
	public List<ProductCommonDetail> getDetails() {
		return details;
	}
	public void setDetails(List<ProductCommonDetail> details) {
		this.details = details;
	}

	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public boolean isArapComplete() {
		return arapComplete;
	}
	public void setArapComplete(boolean arapComplete) {
		this.arapComplete = arapComplete;
	}
	public InfoComponent getInfo() {
		return info;
	}
	public void setInfo(InfoComponent info) {
		this.info = info;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getValueAddTax() {
		return valueAddTax;
	}
	public void setValueAddTax(BigDecimal valueAddTax) {
		this.valueAddTax = valueAddTax;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPaid() {
		return paid;
	}
	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}
	public BigDecimal getArap() {
		return arap;
	}
	public void setArap(BigDecimal arap) {
		this.arap = arap;
	}

	
	// Constructors

	public BigDecimal getPaidArap() {
		return paidArap;
	}
	public void setPaidArap(BigDecimal paidArap) {
		this.paidArap = paidArap;
	}
	/** default constructor */
	public ProductCommonMaster() {
	}
	

	// Property accessors


	public ProductCommonMaster(Long id, String item, Warehouse warehouse, Employee employee, Date billDate,
			BigDecimal subTotal, BigDecimal valueAddTax, BigDecimal prepaid, BigDecimal discount, BigDecimal amount,
			BigDecimal paid, BigDecimal arap, BigDecimal paidArap, boolean arapComplete, State state) {
		super();
		this.id = id;
		this.item = item;
		this.warehouse = warehouse;
		this.employee = employee;
		this.billDate = billDate;
		this.subTotal = subTotal;
		this.valueAddTax = valueAddTax;
		this.advance = prepaid;
		this.discount = discount;
		this.amount = amount;
		this.paid = paid;
		this.arap = arap;
		this.paidArap = paidArap;
		this.arapComplete = arapComplete;
		this.state = state;
	}
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getMemos() {
		return this.memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public BusinessType getBusinessType() {
		return businessType;
	}
	
	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	public BigDecimal getAdvance() {
		return advance;
	}
	public void setAdvance(BigDecimal advance) {
		this.advance = advance;
	}
//	public List<ProductPlanMaster> getProductPlans() {
//		return productPlans;
//	}
//	public void setProductPlans(List<ProductPlanMaster> productPlans) {
//		this.productPlans = productPlans;
//	}
//	public List<ProductOrderBillMaster> getProductOrderBills() {
//		return productOrderBills;
//	}
//	public void setProductOrderBills(List<ProductOrderBillMaster> productOrderBills) {
//		this.productOrderBills = productOrderBills;
//	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
	public ProductCommonMaster toDisp(boolean withDetail){
		ProductCommonMaster master = null;
		try {
			master = (ProductCommonMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setBusinessUnit(new BusinessUnits(this.getBusinessUnit().getId(),this.getBusinessUnit().getName()));
		master.setWarehouse(new Warehouse(this.getWarehouse().getId(),this.getWarehouse().getName()));
		master.setEmployee(new Employee(this.getEmployee().getId(),this.getEmployee().getName()));
		if(this.contactor!=null){
			master.setContactor(new Contacts(this.getContactor().getId(),this.getContactor().getName()));
		}
		if(this.fundAccount!=null){
			master.setFundAccount(new FundAccount(this.getFundAccount().getId(),this.getFundAccount().getName()));
		}
		if(this.getInvoiceType()!=null){
			master.setInvoiceType(new BaseData(this.getInvoiceType().getId(),this.getInvoiceType().getName()));
		}
		if(withDetail){
			List<ProductCommonDetail> details=this.getDetails();
			int size = details.size();
			for(int i=0;i<size;i++){
				ProductCommonDetail masterdetail=details.get(i);
				Product product = masterdetail.getProduct();
				masterdetail.setProduct(new Product(product.getId(),product.getFullName(),product.getItem(),product.getSpecification(),product.getUnit(),product.getColor()));
				masterdetail.setMaster(null);
				if(masterdetail.getPlanMaster()!=null){
					masterdetail.setPlanMaster(new ProductPlanMaster(masterdetail.getPlanMaster().getId()));
				}
				if (masterdetail.getOrderBillMaster() != null) {
					masterdetail.setOrderBillMaster(new ProductOrderBillMaster(masterdetail.getOrderBillMaster().getId()));
				}
				//				master.getDetails().add(masterdetail);
			}
		}else{
			master.getDetails().clear();
		}
		return master;
	}
	
	@Override
	public ProductCommonMasterPM convert(boolean withDetail) {
		ProductCommonMasterPM masterPM=new ProductCommonMasterPM();
		org.springframework.beans.BeanUtils.copyProperties(this,masterPM,new String[]{"details"});
		BusinessUnits businessUnit = this.getBusinessUnit();
		Contacts contactor = this.getContactor();
		Warehouse warehouse = this.getWarehouse();
		FundAccount fd=this.getFundAccount();
		Employee employee = this.getEmployee();
		BaseData invoicType = this.getInvoiceType();
		masterPM.setBusinessUnitId(businessUnit.getId());
		masterPM.setBusinessUnitName(businessUnit.getName());
		masterPM.setWarehouseId(warehouse.getId());
		masterPM.setWarehouseName(warehouse.getName());
		masterPM.setEmployeeId(employee.getId());
		masterPM.setEmployeeName(employee.getName());
		if(contactor!=null){
			masterPM.setContactorId(contactor.getId());
			masterPM.setContactorName(contactor.getName());
		}
		if(fd!=null){
			masterPM.setFundAccountId(fd.getId());
			masterPM.setFundAccountName(fd.getName());
		}
		if(invoicType!=null){
			masterPM.setInvoiceTypeId(invoicType.getId());
			masterPM.setInvoiceTypeName(invoicType.getName());
		}
		if(withDetail){
			List<ProductCommonDetail> details = this.getDetails();
			int size = details.size();
			for(int i=0;i<size;i++){
				ProductCommonDetailPM detailpm=new ProductCommonDetailPM();
				ProductCommonDetail detail=details.get(i);
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