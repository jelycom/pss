/*
 * 捷利商业进销存管理系统
 * @(#)PeriodStock.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.domain.Product;
import cn.jely.cd.sys.dao.IPeriodStockDao;
import cn.jely.cd.sys.domain.PeriodStock;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.ObjectQueryTool;
import cn.jely.cd.util.query.QueryRule;

/**
 * @ClassName:PeriodStockDaoImpl
 * @Description:DaoImpl
 * @author 周义礼
 * @version 2013-06-08 15:45:12 
 *
 */

public class PeriodStockDaoImpl extends PeriodAbstractDaoImpl<PeriodStock> implements IPeriodStockDao {
	@Override
	public List<PeriodStock> findDuplicate(PeriodStock t) {
		ObjectQuery objectQuery=new ObjectQuery();
		objectQuery.addWhere("o.warehouse=:warehouse", "warehouse", t.getWarehouse());
		objectQuery.addWhere("o.product=:product", "product", t.getProduct());
		return findAll(objectQuery);
	}

	@Override
	public List<PeriodStock> findAllStock(ObjectQuery objectQuery, Long warehouseId) {
//		objectQuery.setJqGridCondition(true);
//		objectQuery.setQueryClass(Product.class);
		//这个查询语句不能完成未选择仓库id时显示所有库存的功能
//		String baseHql = "select new PeriodStock(o,case when ps is null or ps.warehouse.id!="+warehouseId+" then 0 else ps.quantity end,case when ps is null or ps.warehouse.id!="+warehouseId+" then 0.00 else ps.amount end) from PeriodStock ps right join ps.product o ";
//		String baseHql = "select new PeriodStock(o,case when ps is null  then 0 else ps.quantity end,case when ps is null then 0.00 else ps.amount end) from PeriodStock ps right join ps.product o ";
		String baseHql =null;
		if(warehouseId!=null){
			baseHql = "select new PeriodStock(o,case when ps is null  then 0 else sum(ps.quantity) end,case when ps is null then 0.00 else sum(ps.amount) end) from PeriodStock ps join ps.warehouse wh with wh.id= "+warehouseId+" right join ps.product o ";
		}else{
			baseHql = "select new PeriodStock(o,case when ps is null  then 0 else sum(ps.quantity) end,case when ps is null then 0.00 else sum(ps.amount) end) from PeriodStock ps right join ps.product o ";
		}
		String orderField=objectQuery.getOrderField();
		if(StringUtils.isNotBlank(orderField)){
			if("quantity".equals(orderField)||"amount".equals(orderField)){
				objectQuery.setOrderField("sum(ps."+orderField+")");
			}else{
				objectQuery.setOrderField("o."+orderField);
			}
		}else{
			objectQuery.setOrderField("o.id");
		}
		objectQuery.setGroup("o");
//		findAll(objectQuery, false);
		objectQuery.setBaseHql(baseHql);
		List<QueryRule> rules=ObjectQueryTool.getAllRule(objectQuery.getQueryGroup());
		for (QueryRule queryRule : rules) {
			String fieldName = queryRule.getField();
			if(fieldName.contains("quantity")||fieldName.contains("amount")){
				queryRule.setRootAlia("ps");
			}else{
				queryRule.setRootClass(Product.class);
				queryRule.setRootAlia("o");
			}
		}
		List<PeriodStock> allpsp = findAll(objectQuery);
//		System.out.println("----------------");
//		for (PeriodStock periodStock : allpsp) {
//			System.out.println(periodStock);
//		}
//		for (Object[] objects : allpsp) {
//			for (Object object : objects) {
//				System.out.println(object);
//			}
//		}
//		for (Object[] objects : allpsp) {
//			Product p=(Product) objects[0];
//			System.out.println(p);
//			if(objects[1]!=null){
//				PeriodStock ps=(PeriodStock) objects[1];
//				System.out.println(ps);
//			}
//		}
//		baseHql="select p.id,ps.quantity from PeriodStock ps right join ps.product p left join ps.warehouse wh where p.id = ? and (wh.id = ? or wh is null)";
//		if (warehouseId != null && warehouseId > 0) {
//			if(StringUtils.trim(objectQuery.getWhere()).length()>0){
//				objectQuery.addWhere(" and ps.warehouse is null or ps.warehouse.id="+warehouseId, null, null);
//			}else{
//				objectQuery.addWhere("where ps.warehouse is null or ps.warehouse.id="+warehouseId, null, null);
//			}
//			baseHql += " and ( ps.warehouse.id="+warehouseId+")";
//		}
//		objectQuery.setBaseHql(baseHql);
		return allpsp;
	}

	@Override
	public List<PeriodStock> findAllStock(List<Product> products, Long warehouseId) {
		ObjectQuery pssQuery=new ObjectQuery();
		pssQuery.addWhere("o.product in (:products)", "products", products);
		if(warehouseId==null){
			pssQuery.setGroup("product");//按产品分组后就可以将不同仓库的合计起来
			pssQuery.setBaseHql("select new List(o.product as product,sum(o.quantity) as quantity,sum(o.amount) as amount) from PeriodStock o");
			List<List> findAll = findAll(pssQuery,false);
			ArrayList<PeriodStock> periodStocks = new ArrayList<PeriodStock>();
			for (List list : findAll) {
				Product product = (Product)list.get(0);
				Integer quantity = ((Long)list.get(1)).intValue();
				BigDecimal amount = (BigDecimal)list.get(2);
				PeriodStock ps=new PeriodStock(product, quantity, amount);
				periodStocks.add(ps);
			}
			return periodStocks;
		}else{
			pssQuery.addWhere("and o.warehouse.id=:warehouseId", "warehouseId", warehouseId);
			return findAll(pssQuery);
		}
	}
	
}
