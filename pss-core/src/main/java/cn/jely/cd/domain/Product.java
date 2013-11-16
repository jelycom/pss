package cn.jely.cd.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.logic.IPinYinLogic;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */

public class Product implements java.io.Serializable, IPinYinLogic, Cloneable {

	// Fields

	private Long id;
	private ProductType productType;
	private CostMethod costMethod;
	private String item;
	private String shortName;
	private String py;
	private String fullName;
	private String barCode;
	private String unit;
	private String marque;
	private String specification;
	private String color;
	private Integer safeStock;
	/**Date:lastPurchaseDate:最近进货日*/
	private Date lastPurchaseDate;
	/**BigDecimal:lastPurchasePrice:最近进货单价*/
	private BigDecimal lastPurchasePrice;
	/**Date:lastDeliveryDate:最近出货日*/
	private Date lastDeliveryDate;
	/**BigDecimal:lastDeliveryPrice:最近出货单价*/
	private BigDecimal lastDeliveryPrice;
	private Integer maxStock;
	private Integer minStock;
	private Integer allocationCanStock;
	private Double salePrice1;
	private Double salePrice2;
	private Double salePrice3;
	private Boolean invalid;
	private Boolean deleted;
	/** @Fields current:当前库存量 */
	private Integer currentQuanlity;
	/** @Fields currentAmount:当前库存总金额 */
	private BigDecimal currentAmount;

	// Constructors

	/** default constructor */
	public Product() {
	}

	public Product(Long id) {
		this.id = id;
	}

	public Product(ProductType productType, CostMethod costMethod, String item, String shortName, String fullName) {
		this.productType = productType;
		this.costMethod = costMethod;
		this.item = item;
		this.shortName = shortName;
		this.fullName = fullName;
	}

	public Product(Long id, ProductType productType, CostMethod costMethod, String item, String shortName, String py,
			String fullName) {
		this.id = id;
		this.productType = productType;
		this.costMethod = costMethod;
		this.item = item;
		this.shortName = shortName;
		this.py = py;
		this.fullName = fullName;
	}

	/** minimal constructor */
	public Product(ProductType producttype, String item, String shortName, String shortPy, Integer safeStock,
			Date lastPurchaseDate, Date lastDeliveryDate, boolean invalid) {
		this.productType = producttype;
		this.item = item;
		this.shortName = shortName;
		this.py = shortPy;
		this.safeStock = safeStock;
		this.lastPurchaseDate = lastPurchaseDate;
		this.lastDeliveryDate = lastDeliveryDate;
		this.invalid = invalid;
	}

	/** full constructor */
	public Product(ProductType producttype, CostMethod costMethod, String item, String shortName, String shortPy,
			String fullName, String fullPy, String barCode, String unit, String marque, String specification,
			String color, Integer safeStock, Date lastPurchaseDate, Date lastDeliveryDate, Integer maxStock,
			Integer minStock, Integer allocationCanStock, Double salePrice1, Double salePrice2, Double salePrice3,
			boolean invalid, boolean deleted) {
		this.productType = producttype;
		this.costMethod = costMethod;
		this.item = item;
		this.shortName = shortName;
		this.py = shortPy;
		this.fullName = fullName;
		this.barCode = barCode;
		this.unit = unit;
		this.marque = marque;
		this.specification = specification;
		this.color = color;
		this.safeStock = safeStock;
		this.lastPurchaseDate = lastPurchaseDate;
		this.lastDeliveryDate = lastDeliveryDate;
		this.maxStock = maxStock;
		this.minStock = minStock;
		this.allocationCanStock = allocationCanStock;
		this.salePrice1 = salePrice1;
		this.salePrice2 = salePrice2;
		this.salePrice3 = salePrice3;
		this.invalid = invalid;
		this.deleted = deleted;
	}

	// Property accessors

	public Product(Long id, String fullName,String item, String specification, String unit, String color) {
		this.id = id;
		this.item = item;
		this.fullName = fullName;
		this.specification = specification;
		this.unit = unit;
		this.color = color;
	}

	public Product(Long id, String item, String fullName, String specification, String unit, String color, String marque) {
		super();
		this.item = item;
		this.id = id;
		this.fullName = fullName;
		this.unit = unit;
		this.marque = marque;
		this.specification = specification;
		this.color = color;
	}

	public String getItem() {
		return this.item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public CostMethod getCostMethod() {
		return this.costMethod;
	}

	public void setCostMethod(CostMethod costMethod) {
		this.costMethod = costMethod;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMarque() {
		return this.marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public String getSpecification() {
		return this.specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getSafeStock() {
		return this.safeStock;
	}

	public void setSafeStock(Integer safeStock) {
		this.safeStock = safeStock;
	}

	public Integer getMaxStock() {
		return this.maxStock;
	}

	public void setMaxStock(Integer maxStock) {
		this.maxStock = maxStock;
	}

	public Integer getMinStock() {
		return this.minStock;
	}

	public void setMinStock(Integer minStock) {
		this.minStock = minStock;
	}

	public Integer getAllocationCanStock() {
		return this.allocationCanStock;
	}

	public void setAllocationCanStock(Integer allocationCanStock) {
		this.allocationCanStock = allocationCanStock;
	}

	public Double getSalePrice1() {
		return this.salePrice1;
	}

	public void setSalePrice1(Double salePrice1) {
		this.salePrice1 = salePrice1;
	}

	public Double getSalePrice2() {
		return this.salePrice2;
	}

	public void setSalePrice2(Double salePrice2) {
		this.salePrice2 = salePrice2;
	}

	public Double getSalePrice3() {
		return this.salePrice3;
	}

	public void setSalePrice3(Double salePrice3) {
		this.salePrice3 = salePrice3;
	}

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType producttype) {
		this.productType = producttype;
	}

	public Date getLastPurchaseDate() {
		return lastPurchaseDate;
	}

	public void setLastPurchaseDate(Date lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}

	public BigDecimal getLastPurchasePrice() {
		return lastPurchasePrice;
	}

	public void setLastPurchasePrice(BigDecimal lastPurchasePrice) {
		this.lastPurchasePrice = lastPurchasePrice;
	}

	public BigDecimal getLastDeliveryPrice() {
		return lastDeliveryPrice;
	}

	public void setLastDeliveryPrice(BigDecimal lastDeliveryPrice) {
		this.lastDeliveryPrice = lastDeliveryPrice;
	}

	public Date getLastDeliveryDate() {
		return lastDeliveryDate;
	}

	public void setLastDeliveryDate(Date lastDeliveryDate) {
		this.lastDeliveryDate = lastDeliveryDate;
	}

	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}

	public Integer getCurrentQuanlity() {
		return currentQuanlity;
	}

	public void setCurrentQuanlity(Integer currentQuanlity) {
		this.currentQuanlity = currentQuanlity;
	}

	@Override
	public String pySource() {
		return shortName;
	}

	@Override
	public boolean checkBlank() {
		return StringUtils.isBlank(py);
	}

	public Product toDisp(boolean withDetail) {
		Product product = null;
		try {
			product = (Product) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (withDetail) {
			product.setProductType(new ProductType(product.getProductType().getName(), product.getProductType()
					.getItem(), product.getProductType().getPy()));
		} else {
			product.setProductType(null);
		}
		return product;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}