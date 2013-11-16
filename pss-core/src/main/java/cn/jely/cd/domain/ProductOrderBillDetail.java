package cn.jely.cd.domain;

import java.math.BigDecimal;


public class ProductOrderBillDetail implements java.io.Serializable {

	// Fields

	private Long id;
	/** 订单主表 */
	private ProductOrderBillMaster orderBillMaster;
	/**ProductPlanMaster:planMaster:如果有对应的计划*/
	private ProductPlanMaster planMaster;
	private Integer orders;
	/** 产品*/
	private Product product;
	/** 订单数量 */
	private Integer quantity;
	/** 产品单价 */
	private BigDecimal price;
	/** 小计 */
	private BigDecimal subTotal;
	/** 产品税金 */
	private BigDecimal taxPrice;
	/** 产品税金小计 */
	private BigDecimal tax;
	/** 合计 :tax+subtotal*/
	private BigDecimal amount;
	/** 完成数量 */
	private Integer completeQuantity;
	/**boolean:complete:用于中间业务计算用,不用映射*/
	private boolean complete;
	/** 备注(如果有计划单则自动添入计划单号) */
	private String memos;

	// Constructors

	/** default constructor */
	public ProductOrderBillDetail() {
	}

	/** minimal constructor */
	public ProductOrderBillDetail(Integer orders, Product product, Integer quantity) {
		this.orders = orders;
		this.product = product;
		this.quantity = quantity;
	}


	public ProductOrderBillDetail(Long id,
			ProductOrderBillMaster productmaster, Integer orders,
			Product product, Integer quantity, BigDecimal unitPrice,
			BigDecimal amount, BigDecimal taxPrice, BigDecimal taxAmount,
			Integer completeQuantity, String memos) {
		this.id = id;
		this.orderBillMaster = productmaster;
		this.orders = orders;
		this.product = product;
		this.quantity = quantity;
		this.price = unitPrice;
		this.amount = amount;
		this.taxPrice = taxPrice;
		this.tax = taxAmount;
		this.completeQuantity = completeQuantity;
		this.memos = memos;
	}

	// Property accessors

	public Integer getOrders() {
		return this.orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public Integer getCompleteQuantity() {
		return completeQuantity;
	}

	public void setCompleteQuantity(Integer completeQuantity) {
		this.completeQuantity = completeQuantity;
	}

	public String getMemos() {
		return memos;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductOrderBillMaster getOrderBillMaster() {
		return orderBillMaster;
	}

	public void setOrderBillMaster(ProductOrderBillMaster orderBillMaster) {
		this.orderBillMaster = orderBillMaster;
	}

	public ProductPlanMaster getPlanMaster() {
		return planMaster;
	}

	public void setPlanMaster(ProductPlanMaster planMaster) {
		this.planMaster = planMaster;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

}