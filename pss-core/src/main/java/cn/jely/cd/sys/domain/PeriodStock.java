/*
 * 捷利商业进销存管理系统
 * @(#)PeriodARAP.java
 * @Description:TODO
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-8
 */
package cn.jely.cd.sys.domain;

import java.math.BigDecimal;

import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductOrderBillDeliveryMaster;
import cn.jely.cd.domain.Warehouse;

/**
 * 期初库存
 * 
 * @ClassName:PeriodARAP Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-6-8 下午2:58:41
 * 
 */
public class PeriodStock implements Cloneable {

	private Long id;
	/** @Fields accountingPeriod:结帐期间(期初或某一期间) */
	private AccountingPeriod accountingPeriod;
	/** @Fields warehouse:所属仓库 */
	private Warehouse warehouse;
	/** @Fields businessUnits:库存产品 */
	private Product product;
	/** @Fields quanlity:数量 */
	private Integer quantity;
	/** @Fields price:平均单价(通过amount/quanlity得出) */
	private BigDecimal price;
	/** @Fields amount:总金额 */
	private BigDecimal amount;

	public PeriodStock() {
	}

	public PeriodStock(Long id) {
		this.id = id;
	}

	public PeriodStock(Product product) {
		super();
		this.product = product;
	}

	public PeriodStock(Product product, Integer quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}
	
	public PeriodStock(Product product, Long quantity,Double amount) {
		this(product, quantity.intValue(), amount);
	}
	
	public PeriodStock(Product product, Integer quantity,Double amount) {
		super();
		this.product = product;
		this.quantity = quantity;
		this.amount = new BigDecimal(amount.toString());
		if (quantity != null && quantity > 0) {
			this.price = this.amount.divide(new BigDecimal(quantity.toString()), 4, BigDecimal.ROUND_HALF_UP);
		} else {
			this.price = this.amount;
		}
	}

	public PeriodStock(Product product, Integer quantity, BigDecimal amount) {
		this.product = product;
		this.quantity = quantity;
		if (quantity != null && quantity > 0) {
			this.price = amount.divide(new BigDecimal(quantity.toString()), 4, BigDecimal.ROUND_HALF_UP);
		} else {
			this.price = amount;
		}
		this.amount = amount;
	}

	public PeriodStock(Product product, Integer quanlity, BigDecimal price, BigDecimal amount) {
		this.product = product;
		this.quantity = quanlity;
		this.price = price;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AccountingPeriod getAccountingPeriod() {
		return accountingPeriod;
	}

	public void setAccountingPeriod(AccountingPeriod accountingPeriod) {
		this.accountingPeriod = accountingPeriod;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		if(this.price==null&&this.amount!=null){
			this.price = amount.divide(new BigDecimal(quantity.toString()), 4, BigDecimal.ROUND_HALF_UP);
		}
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

	public PeriodStock toDisp(boolean withDetail){
		PeriodStock master=null;
		try {
			master=(PeriodStock) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		Product product=master.getProduct();
		master.setProduct(new Product(product.getId(), product.getFullName(),product.getItem(), product.getSpecification(), product.getUnit(), product.getColor()));
		return master;
	}
	@Override
	public String toString() {
		return "PeriodStock [id=" + id + ", accountingPeriod=" + accountingPeriod + ", warehouse=" + warehouse
				+ ", product=" + product + ", quantity=" + quantity + ", price=" + price + ", amount=" + amount + "]";
	}

}
