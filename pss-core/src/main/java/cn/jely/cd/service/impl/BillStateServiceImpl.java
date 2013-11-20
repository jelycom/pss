/*
 * 捷利商业进销存管理系统
 * @(#)BillStateServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-17
 */
package cn.jely.cd.service.impl;

import java.util.Date;

import cn.jely.cd.dao.IBillStateDao;
import cn.jely.cd.util.code.IItemAble;
import cn.jely.cd.util.code.ItemGenAble;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.state.IDoWithState;
import cn.jely.cd.util.state.IStateAble;
import cn.jely.cd.util.state.State;
import cn.jely.cd.util.state.StateManager;

/**
 * @ClassName:BillStateServiceImpl Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-17 下午5:36:37
 * 
 */
public abstract class BillStateServiceImpl<T> extends BaseServiceImpl<T> implements IDoWithState<T>, ItemGenAble {

	/**
	 * 根据新实体取得数据库中的实体数据
	 * 
	 * @param master
	 * @return T
	 */
	protected abstract T getOldDomain(T master);

	/**
	 * 对模型进行检查,保存和更新前检查
	 * 
	 * @param master
	 *            void
	 */
	protected abstract void prepareModel(T master);

	/**
	 * 执行更改,需要关联更新的部分放入此处
	 * 
	 * @param master
	 *            void
	 */
	protected abstract void performChange(T master);

	@Override
	public String generateItem(Date billDate) {
		return ((IBillStateDao<T>)getBaseDao()).generateItem(billDate);
	}

	@Override
	protected Boolean beforeUpdateCheck(T master) {
		boolean result = false;
		if (master != null) {
			State newState = ((IStateAble) master).getState();
			T oldMaster = getOldDomain(master);
//			if (StateManager.canEditContent((IStateAble) master)) {
			if (StateManager.canEditContent((IStateAble) oldMaster)) {//如果原来的状态是允许修改内容的。
				// 必须保证从表中的数据多于1条
				prepareModel(master);//检查模型是否有问题
				result = true;
			}  
			if(!((IStateAble) oldMaster).getState().equals(newState)&&StateManager.canChangeTo((IStateAble) oldMaster, newState)){
				result = true;
			}
		}
		return result;
	}

	@Override
	protected Boolean beforeSaveCheck(T master) {
		if (master != null) {
			checkItem(master);
			prepareModel(master);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查单据编号
	 * @param master
	 *            void
	 */
	private void checkItem(T master) {
		IItemAble domain = (IItemAble) master;
		ObjectQuery objectQuery=new ObjectQuery();
		objectQuery.addWhere("item=:item", "item", domain.getItem());
		T duplicate = getBaseDao().findObject(objectQuery);
		if(duplicate!=null){
			throw new AttrConflictException("编号");
		}
	}

	@Override
	protected void afterSave(T master) {
		doWithState((IStateAble) master);
	}

	@Override
	protected void afterUpdate(T master) {
		doWithState((IStateAble) master);
	}

	@Override
	public T doWithState(IStateAble domain) {
		if (StateManager.canPost(domain)) {
			performChange((T) domain);
		}
		return null;
	}

}
