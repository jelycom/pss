/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockDetailDaoImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-26
 */
package cn.jely.cd.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.util.CostMethod;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.ObjectQueryTool;
import cn.jely.cd.util.query.QueryRule;
import cn.jely.cd.vo.RealStockVO;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * @ClassName:ProductStockDetailDaoImpl Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-6-26 下午3:18:14
 * 
 */
public class ProductStockDetailDaoImpl extends BaseDaoImpl<ProductStockDetail> implements IProductStockDetailDao {

	@Override
	public Long sumRealStockQuantity(Warehouse warehouse, Product product) {
		HashMap<String, Object> variables = new HashMap<String, Object>();
		variables.put("product", product);
		String hql = null;
		if (warehouse == null || warehouse.getId() == null) {
			hql = "select (sum(o.inquantity)-sum(o.outquantity)) from ProductStockDetail o where o.product=:product and (o.inquantity <> o.outquantity or o.outquantity is null)";
		} else {
			variables.put("warehouse", warehouse);
			hql = "select (sum(o.inquantity)-sum(o.outquantity)) from ProductStockDetail o where o.warehouse=:warehouse and o.product=:product and (o.inquantity <> o.outquantity or o.outquantity is null)";
		}
		Long realQuantity = countByHql(
		// TODO:outquantity如果为null则sum为null导致整个返回为null造成错误
				hql, variables);
		if (realQuantity == null) {
			realQuantity = 0l;
		}
		return realQuantity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RealStockVO> sumRealStockQuantity(Warehouse warehouse, List<ProductType> productTypes) {
		String hql = "select new cn.jely.cd.vo.RealStockVO(o.warehouse,o.product,sum(o.inquantity)-sum(o.outquantity)) from ProductStockDetail o join o.product p where o.warehouse=:warehouse and p.productType in (:productTypes) and (o.inquantity <> o.outquantity or o.outquantity is null) group by p order by p.item";
		List<RealStockVO> realStocks = findByNamedParam(hql, new String[] { "warehouse", "productTypes" },
				new Object[] { warehouse, productTypes });
		return realStocks;
	}

	@Override
	public List<RealStockVO> sumRealStock(Serializable warehouseId, List<Serializable> productTypeIds,ObjectQuery objectQuery) {// TODO:根据前台自定义条件进行查询库存
		if (warehouseId != null) {
			objectQuery.getQueryGroup().getRules().add(new QueryRule("o.warehouse.id", FieldOperation.eq, warehouseId));
		}
		if (null != productTypeIds && 0 < productTypeIds.size()) {
			objectQuery.getQueryGroup().getRules().add(new QueryRule("o.product.productType.id", FieldOperation.in, productTypeIds));
		}
		objectQuery.setBaseHql("select new cn.jely.cd.vo.RealStockVO(o.warehouse as warehouse,o.product as product ,(sum(o.inquantity)-sum(o.outquantity)) as quantity,sum(o.amount) as amount) from ProductStockDetail o");
		if (StringUtils.isBlank(objectQuery.getGroup())) {
			objectQuery.setGroup("o.product");
		}
		List<QueryRule> allRule = ObjectQueryTool.getAllRule(objectQuery.getQueryGroup());
		for (QueryRule queryRule : allRule) {
			queryRule.setRootAlia("o");
		}
		objectQuery = prepareObjectQuery(objectQuery);
		return findByNamedParam(objectQuery.getFullHql(), objectQuery.getParamValueMap());
	}

	@Override
	public List<RealStockVO> sumRealStock(List<Warehouse> warehouses) {
		HashMap<String, Object> variables = new HashMap<String, Object>();
		variables.put("warehouses", warehouses);
		return (List<RealStockVO>) findByNamedParam(
				"select new cn.jely.cd.vo.RealStockVO(o.warehouse as warehouse,o.product as product ,(sum(o.inquantity)-sum(o.outquantity)) as quantity,sum(o.amount) as amount) from ProductStockDetail o where o.warehouse in (:warehouses) group by o.product",
				variables);// TODO:outquantity如果为null则sum为null导致整个返回为null造成错误
	}

	/**
	 * 统计实际的库存金额
	 * 
	 * @Title:sumRealAmount
	 * @param warehouse
	 * @param product
	 * @return BigDecimal
	 */
	private BigDecimal sumRealAmount(Warehouse warehouse, Product product) {
		// getHibernateTemplate();
		return null;
	}

	@Override
	public List<ProductStockDetail> findDetailASC(Warehouse warehouse, Product product) {
		return findByNamedParam(
				"from ProductStockDetail o where o.warehouse=:warehouse and o.product=:product and o.inquantity>o.outquantity order by o.id",
				new String[] { "warehouse", "product" }, new Object[] { warehouse, product });
	}

	@Override
	public List<ProductStockDetail> findDetailDESC(Warehouse warehouse, Product product) {
		// findByNamedParam(
		// "from ProductStockDetail o where o.warehouse=:warehouse and o.product=:product and o.inquantity>o.outquantity order by o.id desc",
		// new String[] { "warehouse", "product" }, new Object[] { warehouse,
		// product })
		List<ProductStockDetail> details = findDetailASC(warehouse, product);
		Collections.reverse(details);
		return details;
	}

	private List<ProductStockDetail> findDetailMOAV(Warehouse warehouse, Product product) {
		return findByNamedParam(
				"from ProductStockDetail o where o.warehouse=:warehouse and o.product=:product and o.amount>:amount order by o.id",
				new String[] { "warehouse", "product", "amount" }, new Object[] { warehouse, product, BigDecimal.ZERO });
	}

	/**
	 * 按顺序依次出库指定的库房及产品
	 * 
	 * @Title:updateBySequence
	 * @param quantity
	 * @param psdetails
	 * @return BigDecimal
	 */
	private BigDecimal updateBySequence(Integer quantity, List<ProductStockDetail> psdetails) {
		BigDecimal cost = BigDecimal.ZERO;
		for (ProductStockDetail psDetail : psdetails) {
			Integer subQuantity = psDetail.getInquantity() - psDetail.getOutquantity();// 这记录的可用数量
			if (subQuantity < quantity) {
				psDetail.setOutquantity(psDetail.getInquantity());
				cost = cost.add(psDetail.getAmount());
				psDetail.setAmount(BigDecimal.ZERO);
			} else if (subQuantity > quantity) {
				// 当前的库存金额=原来的库存金额-现在出货的金额
				BigDecimal costAmount = psDetail.getPrice().multiply(new BigDecimal(quantity.toString()));// 成本单价*出货数量=出货的金额
				psDetail.setOutquantity(psDetail.getOutquantity() + quantity);// 此句必须在上一句下,因为上句需要的是原来的数量,而这是重新设置了出库的数量
				psDetail.setAmount(psDetail.getAmount().subtract(costAmount));
				return cost.add(costAmount);
			} else {
				// 如果数量相等,则明细中的成本就是剩余的成本.
				cost = cost.add(psDetail.getAmount());
				psDetail.setOutquantity(psDetail.getOutquantity() + quantity);
				psDetail.setAmount(BigDecimal.ZERO);
			}
			update(psDetail);
			quantity = quantity - subQuantity;// 减去此单扣减的数量
			if (quantity <= 0) {// 如果数量已经为负,则退出循环
				break;
			}
		}
		return cost;
	}

	/**
	 * 出库时更新库存
	 * 
	 * @param warhouse
	 * @param product
	 * @param outQuantity
	 * @param detailId
	 *            手工指定时的批次所在记录号
	 * @return BigDecimal 合计出库成本 如果采用策略模式,可以通过定义一个算法接口interface
	 *         A及对应的方法:updateOut和此方法一样,并实现不同的方法类.
	 *         在updateOut中使用接口的A.updateOut()来实现算法的切换;
	 */
	@Override
	public BigDecimal updateOut(Warehouse warehouse, Product product, int outQuantity, Long detailId) {
		if (warehouse != null && product != null) {
			// 检查数量是否足够
			Long sumRealStock = sumRealStockQuantity(warehouse, product);
			if (outQuantity > sumRealStock) {
				throw new RuntimeException(warehouse.getName() + ":" + product.getFullName() + "数量不足");
			}
			CostMethod costMethod = product.getCostMethod();
			if (costMethod != null) {
				List<ProductStockDetail> psdetails = null;
				switch (costMethod) {
				case FIFO:// 先进先出
					// 为每一条明细记录成本,而成本需要按相应的计算方法来,成本计算:
					// 先按不同的成本计价方式(除手工指定外)取出所有inquantity不等于outquantity的记录.再按相应的方式进行数量金额的修改
					psdetails = findDetailASC(warehouse, product);
					return updateBySequence(outQuantity, psdetails);
					// 将成本金额记入明细的成本属性中.
				case LIFO:// 后进先出
					psdetails = findDetailDESC(warehouse, product);
					return updateBySequence(outQuantity, psdetails);
				case MOAV:// 移动平均
					psdetails = findDetailMOAV(warehouse, product);
					return updateByMOAV(outQuantity, psdetails);
				case ASSI:// 手工指定
					if (detailId == null) {
						throw new RuntimeException("使用手动指定成本方法,但未指定");
					} else {
						// 扣减指定的id数量及成本,如果库存数量不足,则产生错误.
						return updateByASSI(outQuantity, detailId);
					}
				default:
					break;
				}
			}
		}
		return null;

	}

	/**
	 * 根据指定的批次主键扣减出库
	 * 
	 * @Title:updateByASSI
	 * @param outQuantity
	 * @param detailId
	 * @return BigDecimal 出库成本
	 */
	private BigDecimal updateByASSI(int outQuantity, Long detailId) {
		ProductStockDetail detail = getById(detailId);
		BigDecimal cost = BigDecimal.ZERO;
		if (detail != null) {
			int subQuantity = detail.getInquantity() - detail.getOutquantity();
			BigDecimal oldAmount = detail.getAmount();
			BigDecimal price = oldAmount.divide(new BigDecimal(subQuantity));
			if (subQuantity > 0 && subQuantity >= outQuantity) {
				detail.setOutquantity(detail.getOutquantity() + outQuantity);
				if (subQuantity > outQuantity) {
					cost = cost.add(price.multiply(new BigDecimal(outQuantity)));
				} else {
					cost = oldAmount;
				}
				update(detail);
			} else {
				// 库存不足
			}
		}
		return cost;
	}

	/**
	 * 移动平均来计算出库成本
	 * 
	 * @param outQuantity
	 * @param psdetails
	 * @return BigDecimal 出库成本
	 */
	private BigDecimal updateByMOAV(int outQuantity, List<ProductStockDetail> psdetails) {
		BigDecimal cost = BigDecimal.ZERO;
		BigDecimal price = null;
		BigDecimal amount = BigDecimal.ZERO;
		Integer quantity = 0;
		for (ProductStockDetail psDetail : psdetails) {
			quantity = quantity + psDetail.getInquantity() - psDetail.getOutquantity();
			amount = amount.add(psDetail.getAmount());
		}
		// amount.divide(new BigDecimal(quantity));会出错,因为除不尽
		price = SystemCalUtil.dividePrice(amount, new BigDecimal(quantity));
		BigDecimal outAmount = BigDecimal.ZERO;
		if (outQuantity == quantity) {// 如果出货数量和库存数量相等,则出库金额为库存金额.
			outAmount = amount;
		} else {
			outAmount = price.multiply(new BigDecimal(outQuantity));// 本次出货金额
		}
		cost = outAmount;
		for (ProductStockDetail psDetail : psdetails) {
			Integer subQuantity = psDetail.getInquantity() - psDetail.getOutquantity();// 这记录的可用数量
			// 分别减去数量和金额
			if (subQuantity > 0 && outQuantity > 0) {// 如果有可出数量并且现出货数量都大于0
				if (outQuantity > subQuantity) {
					psDetail.setOutquantity(psDetail.getInquantity());
				} else {
					psDetail.setOutquantity(psDetail.getOutquantity() + outQuantity);
				}
				outQuantity = outQuantity - subQuantity;// 减去此单扣减的数量
			}
			BigDecimal oldAmount = psDetail.getAmount();
			if (outAmount.compareTo(BigDecimal.ZERO) > 0 && oldAmount.compareTo(BigDecimal.ZERO) > 0) {
				if (outAmount.compareTo(oldAmount) > 0) {
					psDetail.setAmount(BigDecimal.ZERO);
				} else {
					psDetail.setAmount(oldAmount.subtract(outAmount));
				}
				outAmount = outAmount.subtract(oldAmount);
			}
			update(psDetail);
			if (outQuantity <= 0 && outAmount.compareTo(BigDecimal.ZERO) <= 0) {// 如果数量已经为负,则退出循环
				break;
			}
		}
		return cost;
	}

}
