package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jely.cd.pagemodel.ProductCommonDetailPM;
import cn.jely.cd.pagemodel.ProductCommonMasterPM;
import cn.jely.cd.pagemodel.ProductOrderBillDetailPM;
import cn.jely.cd.pagemodel.ProductOrderBillMasterPM;
import cn.jely.cd.pagemodel.ToPageModel;
import cn.jely.cd.sys.domain.AccountingPeriod;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;


public class ProductOrderBillMaster implements java.io.Serializable,IStateAble,IItemAble,Cloneable,ToPageModel<ProductOrderBillMasterPM> {

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	// Fields
	private Long id;
	/** 单据编号 */
	private String item;
	/** 往来单位*/
	private BusinessUnits businessUnit;
	/** 单位联系人*/
	private Contacts contactor;
	/** 所属进货仓库*/
	private Warehouse warehouse;
	/** 此单经手人*/
	private Employee employee;
	/** 资金帐户*/
	private FundAccount fundAccount;
	/** 业务类型*/
	private BaseData businessType;
	/** 发票类型*/
	private BaseData invoiceType;
	/** 发票号码*/
	private String invoiceNo;
	/** 单据所属会计期间*/
	private AccountingPeriod accountingPeriodId;
	/** 到货单号*/
	private String deliveryItem;
	/** 定货时间 */
	private Date billDate;
	/** 到(交)货时间*/
	private Date deliveryDate;
	/** 到(交)货地址*/
	private String deliveryAddress;
	/** 小计(不含税)*/
	private BigDecimal subTotal;
	/** 税费*/
	private BigDecimal valueAddTax;
	/** 总计(小计+税费)*/
	private BigDecimal amount;
	/** 已付金额*/
	private BigDecimal paid;
	/** 总计(应收应付)付采购定单是应收,销售定单是应付,作为进出货时冲减总金额*/
	private BigDecimal arap;
//	/** 总计(剩余应收应付)*/
	private BigDecimal paidArap;
	/** 备注*/
	private String memos;
	/** 状态*/
	private State state;
	/** 订单明细*/
	private List<ProductOrderBillDetail> details=new ArrayList<ProductOrderBillDetail>();
	/** 订单对应计划单*/
	private List<? extends ProductPlanMaster> productPlans=new ArrayList<ProductPlanMaster>();
	// Constructors

	/** default constructor */
	public ProductOrderBillMaster() {
	}

	
	public ProductOrderBillMaster(Long id) {
		super();
		this.id = id;
	}
	/** minimal constructor */
	public ProductOrderBillMaster(BusinessUnits businessunit, Employee employee) {
		this.businessUnit = businessunit;
		this.employee = employee;
	}

	public ProductOrderBillMaster(BusinessUnits businessUnit, Warehouse warehouse, Employee employee,
			FundAccount fundaccount) {
		this.businessUnit = businessUnit;
		this.warehouse = warehouse;
		this.employee = employee;
		this.fundAccount = fundaccount;
	}

	/** full constructor */
	public ProductOrderBillMaster(Long id, String item,
			BusinessUnits businessUnit, Contacts contactor,
			Warehouse warehouse, Employee employee, FundAccount fundaccount,
			BaseData businessType, BaseData invoicType, String invoiceNo,
			AccountingPeriod accountingPeriodId, String deliveryItem, Date deliveryDate,
			String deliveryAddress,	BigDecimal subTotal, BigDecimal valueAddTax, BigDecimal amount,
			BigDecimal arap, BigDecimal remainArap, String memos, State state) {
		super();
		this.id = id;
		this.item = item;
		this.businessUnit = businessUnit;
		this.contactor = contactor;
		this.warehouse = warehouse;
		this.employee = employee;
		this.fundAccount = fundaccount;
		this.businessType = businessType;
		this.invoiceType = invoicType;
		this.invoiceNo = invoiceNo;
		this.accountingPeriodId = accountingPeriodId;
		this.deliveryItem = deliveryItem;
		this.deliveryDate = deliveryDate;
		this.deliveryAddress = deliveryAddress;
		this.subTotal = subTotal;
		this.valueAddTax = valueAddTax;
		this.amount = amount;
		this.arap = arap;
		this.memos = memos;
		this.state = state;
	}


	// Property accessors


	public List<ProductOrderBillDetail> getDetails() {
		return details;
	}
	
	public void setDetails(List<ProductOrderBillDetail> details) {
		this.details = details;
	}
	
	public AccountingPeriod getAccountingPeriodId() {
		return accountingPeriodId;
	}
	
	public void setAccountingPeriodId(AccountingPeriod accountingPeriodId) {
		this.accountingPeriodId = accountingPeriodId;
	}

	public String getDeliveryItem() {
		return deliveryItem;
	}

	public void setDeliveryItem(String deliveryItem) {
		this.deliveryItem = deliveryItem;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryAddress() {
		return this.deliveryAddress;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date orderDate) {
		this.billDate = orderDate;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		if(subTotal==null){
			this.subTotal=BigDecimal.ZERO;
		}else{
			this.subTotal = subTotal;
		}
	}

	public BigDecimal getValueAddTax() {
		return valueAddTax;
	}

	public void setValueAddTax(BigDecimal valueAddTax) {
		if(valueAddTax==null){
			this.valueAddTax=BigDecimal.ZERO;
		}else{
			this.valueAddTax = valueAddTax;
		}
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		if(amount==null){
			this.amount=BigDecimal.ZERO;
		}else{
			this.amount = amount;
		}
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(BigDecimal paid) {
		if(paid==null){
			this.paid=BigDecimal.ZERO;
		}else{
			this.paid = paid;
		}
	}

	public BigDecimal getArap() {
		return arap;
	}

	public void setArap(BigDecimal arap) {
		if(arap==null){
			this.arap=BigDecimal.ZERO;
		}else{
			this.arap = arap;
		}
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

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public FundAccount getFundAccount() {
		return fundAccount;
	}

	public void setFundAccount(FundAccount fundAccount) {
		this.fundAccount = fundAccount;
	}

	
	public BaseData getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(BaseData invoicType) {
		this.invoiceType = invoicType;
	}

	public BaseData getBusinessType() {
		return businessType;
	}

	public void setBusinessType(BaseData businessType) {
		this.businessType = businessType;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public List<? extends ProductPlanMaster> getProductPlans() {
		return productPlans;
	}

	public void setProductPlans(List<? extends ProductPlanMaster> productPlans) {
		this.productPlans = productPlans;
	}

	public BigDecimal getPaidArap() {
		return paidArap;
	}

	public void setPaidArap(BigDecimal paidArap) {
		if(paidArap==null){
			this.paidArap=BigDecimal.ZERO;
		}else{
			this.paidArap = paidArap;
		}
	}

	public ProductOrderBillMaster toDisp(boolean withDetail){
		ProductOrderBillMaster master = null;
		try {
			master = (ProductOrderBillMaster) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		master.setBusinessUnit(new BusinessUnits(this.getBusinessUnit().getId(),this.getBusinessUnit().getName()));
		if(this.getWarehouse()!=null){
			master.setWarehouse(new Warehouse(this.getWarehouse().getId(),this.getWarehouse().getName()));
		}
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
			List<ProductOrderBillDetail> details=master.getDetails();
			for (ProductOrderBillDetail masterdetail : details) {
				Product product = masterdetail.getProduct();
				masterdetail.setProduct(new Product(product.getId(),product.getFullName(),product.getItem(),product.getSpecification(),product.getUnit(),product.getColor()));
				masterdetail.setOrderBillMaster(null);
				if (masterdetail.getPlanMaster() != null) {
					masterdetail.setPlanMaster(new ProductPlanMaster(masterdetail.getPlanMaster().getId()));
				}
			}
		}else{
			master.getDetails().clear();
		}
		return master;
	}
	
	@Override
	public ProductOrderBillMasterPM convert(boolean withDetail) {
		ProductOrderBillMasterPM masterPM=new ProductOrderBillMasterPM();
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
			List<ProductOrderBillDetail> details = this.getDetails();
			int size = details.size();
			System.out.println(size);
			for(int i=0;i<size;i++){
				ProductOrderBillDetailPM detailpm=new ProductOrderBillDetailPM();
				ProductOrderBillDetail detail=details.get(i);
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