/*
 * 捷利商业进销存管理系统
 * @(#)BillStateDaoImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-15
 */
package cn.jely.cd.dao.impl;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.jely.cd.dao.IBillStateDao;
import cn.jely.cd.util.DateUtils;
import cn.jely.cd.util.FieldOperation;
import cn.jely.cd.util.code.ICodeGenerator;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.code.impl.CodeGeneratorUtil;
import cn.jely.cd.util.code.impl.DayGenerator;
import cn.jely.cd.util.code.impl.MonthGenerator;
import cn.jely.cd.util.code.impl.YearGenerator;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.QueryRule;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:BillStateDaoImpl Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-15 上午9:49:18
 * 
 */
public class BillStateDaoImpl<T> extends BaseDaoImpl<T> implements IBillStateDao<T> {

	protected List<T> findByState(String entityName, List<State> states) {
		return findByNamedParam("from " + entityName + " o where o.state in (:states)", "states", states);
	}

	@Override
	public List<T> findAllUnFinished(ObjectQuery objectQuery) {
		objectQuery.getQueryGroup().getRules().add(new QueryRule("state", FieldOperation.in, StateManager.getUnCompleteStates()));
//		if (StringUtils.isNotBlank(objectQuery.getWhere())) {
//			objectQuery.addWhere("and state in (:state)", "state", StateManager.getUnCompleteStates());
//		} else {
//			objectQuery.addWhere(" state in (:state)", "state", StateManager.getUnCompleteStates());
//		}
		return findAll(objectQuery);
	}

	@Override
	public List<T> findAllFinished(ObjectQuery objectQuery) {
		objectQuery.getQueryGroup().getRules().add(new QueryRule("state", FieldOperation.in, StateManager.getCompleteStates()));
//		if (StringUtils.isNotBlank(objectQuery.getWhere())) {
//			objectQuery.addWhere(" and state in (:state)", "state", StateManager.getCompleteStates());
//		} else {
//			objectQuery.addWhere(" state in (:state)", "state", StateManager.getCompleteStates());
//		}
		return findAll(objectQuery);
	}

	@Override
	public Long countByStates(State... states) {
		// getCount(new ObjectQuery("o.state=? or o.state=? or o.state=?",
		// State.COMPLETE,State.DISCARD,State.SUSPEND));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("states", states);
		return countByHql("select count(o) from " + getEntityClass().getName() + " o where o.state in (:states)", param);
	}

	@Override
	public Long countAllFinished() {
		return countByStates(StateManager.getCompleteStates());
	}

	@Override
	public Long countAllUnFinished() {
		return countByStates(StateManager.getUnCompleteStates());
	}

	@Override
	public String findLasteItem(ObjectQuery objectQuery) {
		T t = findObject(objectQuery);
		if (t == null) { // || !(t instanceof IItemAble)
			return null;
		} else {
			return ((IItemAble) t).getItem();
		}
	}

	@Override
	public String generateItem(Date billDate) {
		ObjectQuery objectQuery = new ObjectQuery();
		objectQuery.setOrderField("item");
		objectQuery.setOrderType(ObjectQuery.ORDERDESC);
		CodeGeneratorUtil codeGenUtil = CodeGeneratorUtil.getInstance();
		ICodeGenerator generator = codeGenUtil.getGenerator(entityClass.getName());
		Calendar calendar = new GregorianCalendar();
		billDate = DateUtils.getDayBegin(billDate);
		calendar.setTime(billDate);
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
		format.setCalendar(calendar);
		if (generator instanceof YearGenerator) {// 按年生成
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			Date startDate = calendar.getTime();
			calendar.add(Calendar.MONTH, 12);
			Date endDate = calendar.getTime();
			objectQuery.addWhere("billDate>=:startDate", "startDate", startDate);
			objectQuery.addWhere("and billDate<:endDate", "endDate", endDate);
		} else if (generator instanceof MonthGenerator) {// 按月生成
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			Date startDate = calendar.getTime();
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			Date endDate = calendar.getTime();
			objectQuery.addWhere("billDate >= :startDate", "startDate", startDate);
			objectQuery.addWhere("and billDate <:endDate", "endDate", endDate);
		} else if (generator instanceof DayGenerator) {// 按日生成
			objectQuery.addWhere("billDate=:billDate", "billDate", billDate);
		}
		String lastItem = findLasteItem(objectQuery);
		return codeGenUtil.genItem(entityClass.getName(), lastItem, billDate);
	}
}
