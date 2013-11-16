package cn.jely.cd.domain;


/**
 * Productplandetail entity. @author MyEclipse Persistence Tools
 */

public class ProductPlanDetail implements java.io.Serializable {

	// Fields

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private ProductPlanMaster productPlanMaster;
	private Product product;
	
	/**	明细的排序号 */
	private Integer orders;
	/**	计划产品数量 */
	private Integer quantity;
	/**	实际完成数量 */
	private Integer completeQuantity;
	/**是否完成,运算过程中保持其状态,不需要映射保存到数据库*/
	private boolean complete;
	private String memos;

	// Constructors

	/** default constructor */
	public ProductPlanDetail() {
	}

	/** full constructor */
	public ProductPlanDetail(Product product, ProductPlanMaster productplanmaster, Integer orders,
			Integer quantity, String memos) {
		this.product = product;
		this.productPlanMaster = productplanmaster;
		this.orders = orders;
		this.quantity = quantity;
		this.memos = memos;
	}

	// Property accessors

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductPlanMaster getProductPlanMaster() {
		return productPlanMaster;
	}

	public void setProductPlanMaster(ProductPlanMaster productPlanMaster) {
		this.productPlanMaster = productPlanMaster;
	}

	public Integer getOrders() {
		return this.orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer planQuantity) {
		this.quantity = planQuantity;
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

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

}