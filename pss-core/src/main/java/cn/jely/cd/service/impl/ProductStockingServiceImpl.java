/*
 * 捷利商业进销存管理系统
 * @(#)ProductStockingMaster.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IInventoryLossDao;
import cn.jely.cd.dao.IInventoryProfitDao;
import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.dao.IProductStockingDao;
import cn.jely.cd.domain.InventoryCommonDetail;
import cn.jely.cd.domain.InventoryLossMaster;
import cn.jely.cd.domain.InventoryProfitMaster;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductStockingDetail;
import cn.jely.cd.domain.ProductStockingMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IProductStockingService;
import cn.jely.cd.util.ArrayConverter;
import cn.jely.cd.util.DateUtils;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;
import cn.jely.cd.vo.RealStockVO;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 库存盘点服务层,如果在明细中加入单价字段,则可以直接完成盘盈和盘亏的业务逻辑, 盘盈:按增加的数量*单价即为此次入库的金额 相符:不需要操作
 * 盘亏:按减少的数量进行0价格出库操作.其costAmount即为亏损的金额
 * 
 * @ClassName:ProductStockingMasterServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2013-08-16 10:54:17
 * 
 */
public class ProductStockingServiceImpl extends BillStateServiceImpl<ProductStockingMaster> implements IProductStockingService {

	private IProductStockingDao productStockingDao;
	private IProductStockDetailDao productStockDetailDao;
	private IProductDao productDao;
	private IInventoryProfitDao inventoryProfitDao;
	private IInventoryLossDao inventoryLossDao;

	
	public void setProductStockingDao(IProductStockingDao productStockingDao) {
		this.productStockingDao = productStockingDao;
	}

	public void setProductStockDetailDao(IProductStockDetailDao productStockDetailDao) {
		this.productStockDetailDao = productStockDetailDao;
	}

	public void setInventoryProfitDao(IInventoryProfitDao inventoryProfitDao) {
		this.inventoryProfitDao = inventoryProfitDao;
	}

	public void setInventoryLossDao(IInventoryLossDao inventoryLossDao) {
		this.inventoryLossDao = inventoryLossDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public void setProductStockDao(IProductStockDetailDao productStockDao) {
		this.productStockDetailDao = productStockDao;
	}

	public void setProductStockingMasterDao(IProductStockingDao productStockingMasterDao) {
		this.productStockingDao = productStockingMasterDao;
	}

	@Override
	public IBaseDao<ProductStockingMaster> getBaseDao() {
		return productStockingDao;
	}

	@Override
	protected ProductStockingMaster getOldDomain(ProductStockingMaster master) {
		return productStockingDao.getById(master.getId());
	}
	@Override
	public ProductStockingMaster continueStocking(String id) {
		ProductStockingMaster continueMaster=new ProductStockingMaster();
		if(StringUtils.isNotBlank(id)){//为空则表示没有之前的单据.暂时返回空对象
			Long[] longids = ArrayConverter.Strings2Longs(id.split(","));// 生成参数数组
			List<ProductStockingDetail> stocked = new ArrayList<ProductStockingDetail>();//已经生成盘点表的产品数据
			Warehouse warehouse = null;
			for (Long longid : longids) {
				ProductStockingMaster master = productStockingDao.getById(longid);
				if(!StateManager.canPost(master)){
					throw new RuntimeException("单据不是生效状态，不能继续盘点！单号："+master.getItem());
				}
				if (warehouse == null) {
					warehouse = master.getWarehouse();
				}
				if (!warehouse.equals(master.getWarehouse())) {
					throw new RuntimeException("不能操作多个仓库!");
				}
//				if (!Arrays.asList(StateManager.getCompleteStates()).contains(master.getState())) {
//					throw new RuntimeException("单据不是完成状态,不能继续!单据号:" + master.getItem());
//				}
				stocked.addAll(master.getDetails());
			}
			List<RealStockVO> realStocks = productStockDetailDao.sumRealStock(Arrays.asList(new Warehouse[] { warehouse }));//当前库房的所有产品库存
			List<Product> stockedProducts = new ArrayList<Product>();
			for (ProductStockingDetail detail : stocked) {
				stockedProducts.add(detail.getProduct());
			}
			continueMaster.setWarehouse(warehouse);
			for (RealStockVO detail : realStocks) {
				Product product = detail.getProduct();
				if (!stockedProducts.contains(product)) {
					ProductStockingDetail psDetail = new ProductStockingDetail();
					psDetail.setProduct(product);
					continueMaster.getDetails().add(psDetail);
				}
			}
		}
		return continueMaster;
	}

	@Override
	protected void performChange(ProductStockingMaster master) {// 生成盘盈盘亏表或者直接进行出入库操作
		List<ProductStockingDetail> details = master.getDetails();
		List<InventoryCommonDetail> profits = new ArrayList<InventoryCommonDetail>();
		List<InventoryCommonDetail> losses = new ArrayList<InventoryCommonDetail>();
		for (ProductStockingDetail detail : details) {
			if (detail.isComplete()) {// 必须是盘点后的状态,每盘点一条即将完成状态置为true
				InventoryCommonDetail idetail=new InventoryCommonDetail();
				idetail.setProduct(detail.getProduct());
				Integer addQuantity = detail.getQuantity();
				if (addQuantity > 0) {// 盘盈
					idetail.setQuantity(addQuantity);
					profits.add(idetail);
				} else if (addQuantity < 0) {// 盘亏
					idetail.setQuantity(Math.abs(addQuantity));
					losses.add(idetail);
				}
			}
		}
		Date billDate = DateUtils.getDayBegin(getDBTime());
		if(profits.size()>0){
			InventoryProfitMaster pmaster=new InventoryProfitMaster();
			pmaster.setBillDate(billDate);
			pmaster.setItem(inventoryProfitDao.generateItem(billDate));
			pmaster.setEmployee(master.getEmployee());
			pmaster.setWarehouse(master.getWarehouse());
			pmaster.setDetails(profits);
			pmaster.setState(State.NEW);
			pmaster.setMemos(master.getItem());
			inventoryProfitDao.save(pmaster);
		}
		if(losses.size()>0){
			InventoryLossMaster lmaster = new InventoryLossMaster();
			lmaster.setBillDate(billDate);
			lmaster.setItem(inventoryLossDao.generateItem(billDate));
			lmaster.setEmployee(master.getEmployee());
			lmaster.setWarehouse(master.getWarehouse());
			lmaster.setDetails(losses);
			lmaster.setState(State.NEW);
			lmaster.setMemos(master.getItem());
			inventoryLossDao.save(lmaster);
		}
	}

	@Override
	protected void prepareModel(ProductStockingMaster master) {
		if (master.getWarehouse() == null || master.getWarehouse().getId() == null
				|| master.getWarehouse().getId() == -1L) {
			throw new EmptyException();
		}
		if (master.getEmployee() == null || master.getEmployee().getId() == null) {
			throw new EmptyException();
		}
		checkDetails(master);
	}

	private BigDecimal checkDetails(ProductStockingMaster t) {
		List<ProductStockingDetail> details = t.getDetails();
		if (details != null) {
			BigDecimal total = BigDecimal.ZERO;
			for (int i = 0; i < details.size(); i++) {
				ProductStockingDetail detail1 = details.get(i);
				if (detail1.getQuantity() == null && detail1.isComplete()) {
					details.remove(i);
				}
				if(!detail1.isComplete()&&!State.NEW.equals(t.getState())){
					t.setState(State.NEW);
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (ProductStockingDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(t);// 维护主从关系
				}
			} else {
				throw new EmptyException("无有效的明细.");
			}
			return total;
		} else {
			throw new EmptyException();
		}
	}

	@Override
	public void audit(String id) {
		if(StringUtils.isNotBlank(id)){//为空则表示没有之前的单据.暂时返回空对象
			Long[] ids = ArrayConverter.Strings2Longs(id.split(","));// 生成参数数组
			for (Long masterid : ids) {
				ProductStockingMaster master=productStockingDao.getById(masterid);
				master.getDetails();
				productStockingDao.evict(master);
				master.setState(StateManager.getDefaultState());
				update(master);
			}
		}else{
			throw new RuntimeException("未选择相应的单据");
		}
	}
}
