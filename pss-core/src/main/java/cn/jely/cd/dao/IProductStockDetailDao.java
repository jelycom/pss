/*
 * 捷利商业进销存管理系统
 * @(#)IProductStockDetailDao.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-26
 */
package cn.jely.cd.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockDetail;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.vo.RealStockVO;

/**
 * @ClassName:IProductStockDetailDao
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-26 下午3:16:45
 *
 */
public interface IProductStockDetailDao extends IBaseDao<ProductStockDetail>{

	/**统计指定仓库中某产品的数量
	 * @Title:countRealStock
	 * @param warehouse
	 * @param product
	 * @return Long 产品数量
	 */
	Long sumRealStockQuantity(Warehouse warehouse, Product product);

	List<RealStockVO> sumRealStockQuantity(Warehouse warehouse, List<ProductType> productTypes);
	/**
	 * 按先后顺序查询批次记录
	 * @Title:findDetailASC
	 * @param warehouse
	 * @param product
	 * @return List<ProductStockDetail>
	 */
	List<ProductStockDetail> findDetailASC(Warehouse warehouse, Product product);

	/**按后先顺序查询批次记录
	 * @Title:findDetailDESC
	 * @param warehouse
	 * @param product
	 * @return List<ProductStockDetail>
	 */
	List<ProductStockDetail> findDetailDESC(Warehouse warehouse, Product product);

	/**
	 * 更新指定的仓库和指定产品的库存明细
	 * @Title:updateOut
	 * @param warehouse 出货仓
	 * @param product 出货的产品
	 * @param outQuanlity 出货数量
	 * @param detailId 指定的批次id
	 * @return BigDecimal 本次出货的出库成本
	 * 可再加一个金额字段,表示按指定的出库成本出库,以应对指定退某张单据
	 * 或者在ProductStockDetail加入一个ProductCommonDetail关联,在选择时建立关联,即可在
	 * ProductStockDetail中查询关联ProductCommonDetail 的id的记录,找到并足够则出此批次
	 */
	BigDecimal updateOut(Warehouse warehouse, Product product, int outQuanlity, Long detailId);

	/**根据统计出指定的仓库产品的数量
	 * @Title:sumRealStock
	 * @param warehouses
	 * @return List
	 */
	List<RealStockVO> sumRealStock(List<Warehouse> warehouses);

	/**根据统计出指定的仓库产品的数量
	 * @Title:sumRealStock
	 * @param objectQuery
	 * @return List
	 */
	List<RealStockVO> sumRealStock(Serializable warehouseId,List<Serializable> productTypeIds,ObjectQuery objectQuery);

}
