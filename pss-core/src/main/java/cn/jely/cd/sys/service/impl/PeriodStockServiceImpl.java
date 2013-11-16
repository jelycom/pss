/*
 * 捷利商业进销存管理系统
 * @(#)PeriodStock.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.domain.Product;
import cn.jely.cd.sys.dao.IPeriodStockDao;
import cn.jely.cd.sys.domain.PeriodStock;
import cn.jely.cd.sys.service.IPeriodStockService;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.ProjectConfig;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:PeriodStockServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-06-08 15:45:12 
 *
 */
public class PeriodStockServiceImpl extends PeriodAbstractServiceImpl<PeriodStock> implements
		IPeriodStockService {

	private IPeriodStockDao periodStockDao;
	private IProductDao productDao;
	
	public void setPeriodStockDao(IPeriodStockDao periodStockDao) {
		this.periodStockDao = periodStockDao;
	}
	
	
	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}


	@Override
	public IBaseDao<PeriodStock> getBaseDao() {
		return periodStockDao;
	}

	@Override
	protected Boolean beforeSaveCheck(PeriodStock t) {
		//默认允许添加批次记录.如果不允许添加批次记录,则同一库房,同一产品不能有1条以上期初库存记录
//		if(!ProjectConfig.getInstance().getBoolean(ConstValue.SYS_BATCHINITSTOCK,true)){
//		}
		List<PeriodStock> duplicates=periodStockDao.findDuplicate(t);
		if(duplicates!=null&&duplicates.size()>0){
			throw new AttrConflictException("同一库房已有此商品");
		}
		return super.beforeSaveCheck(t);
	}

	@Override
	public Pager<PeriodStock> findPager(ObjectQuery objectQuery) {
		Pager<Product> productPager = productDao.findPager(objectQuery);
		List<Product> products=productPager.getRows();
		ObjectQuery psQuery=new ObjectQuery();
		psQuery.setGroup("product");
		List<PeriodStock> periodStocks=periodStockDao.findAll(objectQuery);
		List<PeriodStock> ret=new ArrayList<PeriodStock>();
		for (Product product : products) {
			boolean added=false;
			for(PeriodStock ps:periodStocks){
				if(ps.getProduct().equals(product)){
					ret.add(ps);
					added=true;
					break;
				}
			}
			if(!added){
				PeriodStock tmpStock=new PeriodStock(product, 0, BigDecimal.ZERO);
				ret.add(tmpStock);
			}
		}
//	,case when o is null then 0 else o.amount end
//		objectQuery.setBaseHql("select new cn.jely.cd.sys.domain.PeriodStock(o.product,case when o is null then 0 else o.quantity end) from "+getBaseDao().getEntityClass().getName()+" o right join o.product p");
		return new Pager<PeriodStock>(productPager.getPageNo(), productPager.getPageSize(), productPager.getTotalRows(), productPager.getTotalPages(), ret);
	}


	@Override
	public List<PeriodStock> findAllStock(ObjectQuery objectQuery, Long warehouseId) {
		
//		Pager<Product> pPager=productDao.findPager(objectQuery);
//		List<Product> products=pPager.getRows();
//		List<Product> products = productDao.findAll(objectQuery,warehouseId);
		List<PeriodStock> pss=periodStockDao.findAllStock(objectQuery, warehouseId);
//		List<PeriodStock> pStocks=new ArrayList<PeriodStock>();
//		for (Product product : products) {
//			boolean exists = false;
//			for (PeriodStock periodStock : pss) {
//				if(periodStock.getProduct().equals(product)){//如果库存记录中有此记录
//					pStocks.add(periodStock);
//					exists = true;
//					break;
//				}
//			}
//			if(!exists){
//				pStocks.add(new PeriodStock(product, 0, BigDecimal.ZERO));
//			}
//		}
		return pss;
	}
	
	
}
