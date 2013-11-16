/*
 * 捷利商业进销存管理系统
 * @(#)TreeOperateAction.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-2-20
 */
package cn.jely.cd.web;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.service.IBaseTreeService;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ITreeOperateAction;

/**
 * @ClassName:TreeOperateAction
 * @Description:树的基本操作,与具体的类无关
 * @author 周义礼
 * @version 2013-2-20 下午10:45:10
 *
 */
public abstract class TreeOperateAction<T> extends JQGridAction<T> implements
		ITreeOperateAction {

	protected abstract IBaseTreeService<T> getTreeService() ;
	public abstract T getModel();
	@Override
	public String movePre() {
		getTreeService().MovePre(Long.valueOf(id));
		actionJsonResult=new ActionJsonResult(true,"前移成功");
		return JSONLIST;
	}

	@Override
	public String moveNext() {
		getTreeService().MoveNext(Long.valueOf(id));
		actionJsonResult=new ActionJsonResult(true,"后移成功");
		return JSONLIST;
	}

	@Override
	public String moveFirst() {
		getTreeService().MoveFirst(Long.valueOf(id));
		actionJsonResult=new ActionJsonResult(true,"移至最前成功");
		return JSONLIST;
	}

	@Override
	public String moveLast() {
		getTreeService().MoveLast(Long.valueOf(id));
		actionJsonResult=new ActionJsonResult(true,"移至最后成功");
		return JSONLIST;
	}
	@Override
	public String updateTreeNode() {
		if(StringUtils.isNotBlank(pid)){
			getTreeService().update(getModel(), Long.valueOf(pid));
			actionJsonResult=new ActionJsonResult(true,"更新节点成功",getModel());
		}
		return JSONLIST;
	}
}
