package cn.jely.cd.domain;

import java.math.BigDecimal;

import cn.jely.cd.util.math.SystemCalUtil;

/**
 * Productplandetail entity. @author MyEclipse Persistence Tools
 */

public class ProductCommonDetail implements java.io.Serializable {

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private ProductCommonMaster master;
	private ProductOrderBillMaster orderBillMaster;
	private ProductPlanMaster planMaster;
	/** 明细的排序号 */
	private Integer orders;
	/** 商品 */
	private Product product;
	/** 产品数量 */
	private int quantity;
	/** 产品单价 */
	private BigDecimal price;
	/**BigDecimal:subTotal:小计:price*quantity*/
	private BigDecimal subTotal;
	/**@Fields costAmount:明细成本小计*/
	private BigDecimal costAmount;
	/**@Fields productStockDetailId:对应的批次的编号号,手于手工指定批次出货,赋值时应用关联的ProductCommonDetail的id进行*/
	private Long stockDetailID;
	/**BigDecimal:taxPrice:单个产品税金*/
	private BigDecimal taxPrice;
	/**BigDecimal:taxAmount:税金小计*/
	private BigDecimal tax;
	/** 产品金额合计 :tax+subTotal*/
	private BigDecimal amount;
	/** 是否完成,运算过程中保持其状态,不需要映射保存到数据库 */
	private boolean complete;
	private String memos;

	// Constructors

	/** default constructor */
	public ProductCommonDetail() {
	}

	// Property accessors

	public ProductCommonDetail(Product product, Integer quantity, BigDecimal amount) {
		this.product = product;
		this.quantity = quantity;
		this.amount = amount;
		this.price = SystemCalUtil.dividePrice(amount, new BigDecimal(Integer.toString(quantity)));
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductCommonMaster getMaster() {
		return master;
	}

	public void setMaster(ProductCommonMaster master) {
		this.master = master;
	}

	public Integer getOrders() {
		return this.orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public BigDecimal getCostAmount() {
		return costAmount;
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

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public Long getStockDetailID() {
		return stockDetailID;
	}

	public void setStockDetailID(Long stockDetailID) {
		this.stockDetailID = stockDetailID;
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

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}