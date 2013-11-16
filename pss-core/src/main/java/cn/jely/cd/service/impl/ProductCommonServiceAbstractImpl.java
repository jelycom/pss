/*
 * 捷利商业进销存管理系统
 * @(#)ProductIOServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-21
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jely.cd.dao.IBusinessUnitsDao;
import cn.jely.cd.dao.IFundAccountDao;
import cn.jely.cd.dao.IProductDao;
import cn.jely.cd.dao.IProductStockDetailDao;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.ProductCommonDetail;
import cn.jely.cd.domain.ProductCommonMaster;
import cn.jely.cd.domain.ProductOrderBillMaster;
import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.domain.ProductQuantity;
import cn.jely.cd.service.IProductCommonService;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.math.SystemCalUtil;
import cn.jely.cd.util.state.IDoWithState;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:ProductIOServiceImpl Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-6-21 下午3:30:58
 * 
 */
public abstract class ProductCommonServiceAbstractImpl<T extends ProductCommonMaster> extends BillStateServiceImpl<T>
		implements IProductCommonService<T> {
	protected IBusinessUnitsDao businessUnitsDao;
	protected IFundAccountDao fundAccountDao;
	protected IProductStockDetailDao productStockDetailDao;
	protected IProductDao productDao;

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public void setProductStockDetailDao(IProductStockDetailDao productStockDetailDao) {
		this.productStockDetailDao = productStockDetailDao;
	}

	public void setFundAccountDao(IFundAccountDao fundAccountDao) {
		this.fundAccountDao = fundAccountDao;
	}

	public void setBusinessUnitsDao(IBusinessUnitsDao businessUnitsDao) {
		this.businessUnitsDao = businessUnitsDao;
	}

	// protected abstract BusinessUnits setBusinessUnitLastDate(BusinessUnits u,
	// Date date);
	/**
	 * 更新相关联的计划
	 * 
	 * @param t
	 * @return 所影响的计划单编号集合
	 */
	protected abstract Set<String> updatePlan(Map<Long, List<ProductQuantity>> planMap);

	/**
	 * 更新相关联的定单
	 * 
	 * @param planMap
	 *            void
	 * @return 所影响的定单编号集合
	 */
	protected abstract Set<String> updateOrderBill(Map<Long, List<ProductQuantity>> orderMap, BigDecimal paid);

	/**
	 * 取得数据库中对应订单所有的预收/付款
	 * 
	 * @param t
	 * @return BigDecimal
	 */
	protected abstract BigDecimal getDbPrepare(T t);

	/**
	 * 更新帐户的金额,不同的业务类型有不同的增/减
	 * 
	 * @param id
	 * @param paid
	 *            void
	 */
	protected abstract void updateFundAccount(Serializable id, BigDecimal paid);

	/**
	 * 更新往来单位的最近日期及相应的应收应付款
	 * 
	 * @param id
	 * @param ARAP
	 * @param lastDate
	 *            void
	 */
	protected abstract void updateBusinessUnit(Serializable id, BigDecimal ARAP, Date lastDate);

	/**
	 * 迭代明细文件,将每个商品的数量及金额添加到库存明细表中.(明细表可查出实时库存)
	 * 同时保存产品成本批次表,记录不同的批次的数量,给不同的成本计算方法用.
	 * 
	 * @Title:PersistProductStockDetail
	 * @param t
	 * @param detail
	 * @return Boolean
	 */
	protected abstract Boolean PersistProductStockDetail(T t);

	@Override
	protected T getOldDomain(T master) {
		return getById(master.getId());
	}

	protected void createUpdateMap(Map<Long, List<ProductQuantity>> pqmap, ProductCommonDetail detail, Long planId) {
		ProductQuantity productQuantity = new ProductQuantity(detail.getProduct(), detail.getQuantity());
		List<ProductQuantity> assoProducts = pqmap.get(planId);
		if (assoProducts == null) {
			assoProducts = new ArrayList<ProductQuantity>();
			pqmap.put(planId, assoProducts);
		}
		assoProducts.add(productQuantity);
	}

	@Override
	protected void prepareModel(T t) {
		if (t.getFundAccount() != null && (t.getFundAccount().getId() == -1L || t.getFundAccount().getId() == null)) {
			t.setFundAccount(null);
		}
		if (t.getContactor() != null && (t.getContactor().getId() == null)) {
			t.setContactor(null);
		}
		if (t.getInvoiceType() != null && (t.getInvoiceType().getId() == null || t.getInvoiceType().getId() == -1L)) {
			t.setInvoiceType(null);
		}
		if (t.getWarehouse() == null || t.getWarehouse().getId() == null || t.getWarehouse().getId() == -1L) {
			throw new EmptyException();
		}
		if (t.getBusinessUnit() == null || t.getBusinessUnit().getId() == null) {
			throw new EmptyException();
		}
		if (t.getEmployee() == null || t.getEmployee().getId() == null) {
			throw new EmptyException();
		}
		// List<? extends ProductOrderBillMaster> productOrderBills =
		// t.getProductOrderBills();
		// if(productOrderBills!=null&&productOrderBills.size()>0){
		// for(ProductOrderBillMaster orderBillMaster:productOrderBills){
		// if(orderBillMaster.getId()==null){
		// productOrderBills.remove(orderBillMaster);
		// }
		// }
		// if(productOrderBills.size()==0){
		// t.setProductOrderBills(null);
		// }
		// }
		// List<? extends ProductPlanMaster> productPlans = t.getProductPlans();
		// if(productPlans!=null&&productPlans.size()>0){
		// for(ProductPlanMaster planMaster:productPlans){
		// if(planMaster.getId()==null){
		// productPlans.remove(planMaster);
		// }
		// }
		// if(productPlans.size()==0){
		// t.setProductPlans(null);
		// }
		// }
		t.setAmount(SystemCalUtil.checkValue(t.getAmount()));
		t.setDiscount(SystemCalUtil.checkValue(t.getDiscount()));
		t.setPaid(SystemCalUtil.checkValue(t.getPaid()));
		t.setAdvance(SystemCalUtil.checkValue(t.getAdvance()));
		t.setArap(SystemCalUtil.checkValue(t.getArap()));
		t.setPaidArap(SystemCalUtil.checkValue(t.getPaidArap()));
		t.setSubTotal(SystemCalUtil.checkValue(t.getSubTotal()));
		t.setValueAddTax(SystemCalUtil.checkValue(t.getValueAddTax()));
		BigDecimal detailTotal = checkDetails(t);// 先检查明细
		if (t.getAmount() != null) {
			if (detailTotal.compareTo(t.getAmount()) != 0) {
				throw new RuntimeException("明细值合计与合计值不相等");
			}
		} else {
			t.setAmount(detailTotal);
		}
		BigDecimal amount = t.getAmount();
		BigDecimal prepare = t.getAdvance();
		BigDecimal paid = t.getPaid();// 付款
		BigDecimal discount = t.getDiscount();// 优惠/折扣
		// 将应收应付(总金额减去已付金额为应收应付金额)记入本单中
		t.setArap(amount.subtract(paid.add(prepare).add(discount)));
		if (t.getState() == null) {
			t.setState(StateManager.getDefaultState());
		}
	}

	private BigDecimal checkDetails(T t) {
		List<ProductCommonDetail> details = t.getDetails();
		if (details != null) {
			BigDecimal total = BigDecimal.ZERO;
			for (int i = 0; i < details.size(); i++) {
				ProductCommonDetail detail1 = details.get(i);
				if (detail1.getQuantity() == null || detail1.getQuantity() < 1) {
					details.remove(i);
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (ProductCommonDetail detail : details) {
					detail.setOrders(order++);
					detail.setMaster(t);// 维护主从关系
					if (detail.getOrderBillMaster() != null && detail.getOrderBillMaster().getId() == null) {
						detail.setOrderBillMaster(null);
					}
					if (detail.getPlanMaster() != null && detail.getPlanMaster().getId() == null) {
						detail.setPlanMaster(null);
					}
					total = total.add(SystemCalUtil.checkValue(detail.getAmount()));
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
	protected void performChange(T master) {
		// 如果是现款,直接减去当前帐户的当前金额.或者不减帐户金额,而是查询金额时再查询出相应的单据进行运算?并将相应的金额存入当期分析文件的付出总计中(则可校验因为实时金额=期初和当期额计算出来).
		FundAccount fd = master.getFundAccount();
		BigDecimal paid = master.getPaid();
		BigDecimal prepare = master.getAdvance();
		BigDecimal dbPrepare = getDbPrepare(master);// 数据库中记录的预付款金额(预付-已冲销部分)
		if (prepare.compareTo(dbPrepare) != 0) {
			throw new RuntimeException("前台传入的定单金额不正确!");
		}
		if (fd != null && fd.getId() > 0L && paid.compareTo(BigDecimal.ZERO) != 0) {// 如果有帐户并且付了款的.
			updateFundAccount(fd.getId(), paid);// 更新帐户金额
		}
		updateBusinessUnit(master.getBusinessUnit().getId(), master.getArap(), SystemSetting.getInstance()
				.getCurrentDay());// 更新往来单位
		PersistProductStockDetail(master);// 更新库存
		updateASSO(master);// 更新相关联的计划及定单
	}

	private void updateASSO(T t) {
		Map<Long, List<ProductQuantity>> planMap = new HashMap<Long, List<ProductQuantity>>();
		Map<Long, List<ProductQuantity>> orderMap = new HashMap<Long, List<ProductQuantity>>();
		List<ProductCommonDetail> details = t.getDetails();
		// Set<String> assoitems = new TreeSet<String>();
		for (ProductCommonDetail detail : details) {
			ProductPlanMaster planMaster = detail.getPlanMaster();
			ProductOrderBillMaster orderMaster = detail.getOrderBillMaster();
			if (planMaster != null && planMaster.getId() != null && planMaster.getId() > 0l) {// 添加计划更新项目
				Long planId = planMaster.getId();
				createUpdateMap(planMap, detail, planId);
			} else if (orderMaster != null && orderMaster.getId() != null && orderMaster.getId() > 0) {// 添加定单更新项
				Long orderId = orderMaster.getId();
				createUpdateMap(orderMap, detail, orderId);
			}
		}
		Set<String> orderBillItems = null;
		Set<String> planItems = null;
		if (!orderMap.isEmpty()) {
			orderBillItems = updateOrderBill(orderMap, t.getAdvance());
		}
		if (!planMap.isEmpty()) {
			planItems = updatePlan(planMap);
		}
		StringBuffer memos = new StringBuffer();
		if (t.getMemos() != null) {
			memos.append(t.getMemos());
		}
		if (orderBillItems != null && !t.getMemos().contains(orderBillItems.toArray().toString())) {
			memos.append(orderBillItems.toString());
		}
		if (planItems != null && !t.getMemos().contains(planItems.toArray().toString())) {
			memos.append(planItems.toString());
		}
		t.setMemos(memos.toString());
	}
}
