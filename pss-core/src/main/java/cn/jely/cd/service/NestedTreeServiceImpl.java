/*
 * 捷利商业进销存管理系统
 * @(#)BaseTreeServiceImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import cn.jely.cd.dao.IBaseTreeDao;
import cn.jely.cd.domain.LftRgtTreeNode;
import cn.jely.cd.domain.LftRgtTreeNodeComparator;
import cn.jely.cd.service.impl.BaseServiceImpl;
import cn.jely.cd.util.TreeNode;

/**
 * @ClassName:NestedTreeServiceImpl
 * @Description:嵌套的左右树实现
 * @author 周义礼
 * @version 2013-2-4 上午11:59:39
 * 
 */
public abstract class NestedTreeServiceImpl<T extends LftRgtTreeNode> extends BaseServiceImpl<T> implements IBaseTreeService<T> {

	@Override
	public Boolean MovePre(Serializable id) {
		// T t=getById(id);
		return ((IBaseTreeDao<T>) getBaseDao()).MovePre((LftRgtTreeNode) getById(id));
	}

	@Override
	public Boolean MoveNext(Serializable id) {

		return ((IBaseTreeDao<T>) getBaseDao()).MoveNext((LftRgtTreeNode) getById(id));
	}

	@Override
	public Boolean MoveFirst(Serializable id) {
		return ((IBaseTreeDao<T>) getBaseDao()).MoveFirst((LftRgtTreeNode) getById(id));
	}

	@Override
	public Boolean MoveLast(Serializable id) {
		return ((IBaseTreeDao<T>) getBaseDao()).MoveLast((LftRgtTreeNode) getById(id));
	}

	@Override
	public Boolean MoveIn(Serializable cid, Serializable pid) {
		if(cid==null||pid==null){
			return false;
		}
		LftRgtTreeNode treeNode = (LftRgtTreeNode) getById(cid);
		LftRgtTreeNode pTreeNode = (LftRgtTreeNode) getById(pid);
		return ((IBaseTreeDao<T>) getBaseDao()).MoveIn(treeNode, pTreeNode);
	}

	@Override
	public Boolean ChangeOrder(Serializable id1, Serializable id2) {
		LftRgtTreeNode treeNode1 = (LftRgtTreeNode) getById(id1);
		LftRgtTreeNode treeNode2 = (LftRgtTreeNode) getById(id2);
		return ((IBaseTreeDao<T>) getBaseDao()).ChangeOrder(treeNode1, treeNode2);
	}

	@Override
	public Boolean save(T t, Serializable pid) {
//		if (t instanceof IUnique) {
//			IUnique iUnique = (IUnique) t;
//			String uniqueField = iUnique.getUniqueField();
//			ObjectQuery objectQuery = new ObjectQuery();
//			if (StringUtils.isNotBlank(uniqueField)) {
//				objectQuery.setSearchField(uniqueField);
//				objectQuery.setSearchOper(FieldOperation.eq.toString());
//				objectQuery.setSearchString(iUnique.getUniqueValue());
//				List<T> exists = getBaseDao().findAll(objectQuery);
//				if (exists != null && exists.size() > 0) {
//					return false;
		
//				}
//			}
//		}
		if(pid==null){
			Long exists=getBaseDao().getCount(null);
			if(exists>0){
				throw new RuntimeException("根节点只能有一个.");
			}
		}
		if(beforeSaveCheck(t)){
			((IBaseTreeDao<T>) getBaseDao()).save(t, pid);
		}else{
			throw new RuntimeException("检查实体未通过,不能保存");
		}
		return true;
	}


	/**
	 * 检查是否存在此实体,如果不检查则直接返回false
	 * @Title:checkExist
	 * @param t
	 * @return Boolean true,存在;false,不存在
	 */
//	protected abstract Boolean checkExist(T t) ;
	
	@Override
	public Boolean update(T t, Serializable pid) {
		T parent = getById(pid);
		if (t.equals(parent)) { // 如果父节点是自己,则不能保存
			return false;
		}
		getBaseDao().update(t);
		LftRgtTreeNode pt = (LftRgtTreeNode) parent;
		LftRgtTreeNode treeNode = (LftRgtTreeNode) t;
		LftRgtTreeNode origalpt = ((IBaseTreeDao<T>) getBaseDao()).getParentNode(treeNode);
		if (pt != null && origalpt != null
				&& (!origalpt.getLft().equals(pt.getLft()) || !origalpt.getRgt().equals(pt.getRgt()))) { // 如果更新前后父节点不为空并且不同
			return ((IBaseTreeDao<T>) getBaseDao()).MoveIn(treeNode, pt);
		}
		return true;
	}

	@Override
	public List<T> findTreeNodes(Serializable id,boolean includeself) {
		return ((IBaseTreeDao<T>) getBaseDao()).findTreeNodes(id,includeself);
	}

	@Override
	public Boolean deleteCascade(Serializable... ids) {
		for(Serializable id:ids){
			getBaseDao().clearCache();//TODO:改进清空缓存作法
			//清空缓存,因为如果前面用get过,则缓存中有,而用hql进行修改或删除不会反映到缓存中,就会出现缓存与数据库不一致的情况而报错
			//如:先前已经get出,再hql删除,则现在get直接从缓存中取出.并判断时不为null,但下面操作时因为数据库中已经不存在而报:No row with the given identifier exists
			T t=getById(id);
			if(t != null){
				LftRgtTreeNode node=(LftRgtTreeNode)t;
				((IBaseTreeDao<T>) getBaseDao()).deleteCascade(node);
			}
		}
		return true;
	}

	/**
	 * 将左右值模型转换为Node[Children]模型
	 * @param resources
	 * @return TreeNode<T>
	 */
	public TreeNode<T> toChildModel(List<T> resources) {
		List<T> sortedList = new ArrayList<T>(resources);
		Collections.sort(sortedList, new LftRgtTreeNodeComparator());
		Stack<TreeNode<T>> nodeStack = new Stack<TreeNode<T>>();
		boolean isRoot = true;
		TreeNode<T> nestMenu = null;
		TreeNode<T> root = null;
		for (T resource : resources) {
				TreeNode<T> node = new TreeNode<T>(resource, null);
				if (isRoot) {
					root = node;
					nestMenu = node;
					isRoot = false;
				} else {
					nestMenu = nodeStack.peek();
					while (resource.getLft() < nestMenu.getNode().getLft()
							|| resource.getRgt() > nestMenu.getNode().getRgt()) {
						nodeStack.pop();
						nestMenu = nodeStack.peek();
					}
					nestMenu.getChildren().add(node);
				}
				nodeStack.push(node);
		}
		return root;
	}

}
