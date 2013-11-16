/*
 * 捷利商业进销存管理系统
 * @(#)BaseTreeDaoImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.dao.IBaseTreeDao;
import cn.jely.cd.domain.LftRgtTreeNode;
import cn.jely.cd.domain.Region;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.query.ObjectQuery;

/**
 * @ClassName:BaseTreeDaoImpl
 * @author 周义礼
 * @version 2013-2-4 下午2:42:03
 * 
 */
public class NestedTreeDaoImpl<T> extends BaseDaoImpl<T> implements IBaseTreeDao<T> {
	String className = entityClass.getName();

	@SuppressWarnings("unchecked")
	@Override
	public Boolean MoveIn(LftRgtTreeNode t, LftRgtTreeNode pt) {
		LftRgtTreeNode ptree = (LftRgtTreeNode) pt;
		Long plft = ptree.getLft();
		Long prgt = ptree.getRgt();
		Integer pdepth = ptree.getDepth();// 父节点相关属性
		LftRgtTreeNode tree = (LftRgtTreeNode) t;
		Long lft = tree.getLft();
		Long rgt = tree.getRgt();
		Long margin = rgt - lft + 1;
		Integer depth = tree.getDepth();// 待移动根节点的相关属性
		StringBuilder addCValueBuilder = new StringBuilder(100);
		int newDepth = pdepth - depth + 1;
		if (2 * rgt + 2 > Long.MAX_VALUE)
			return false;
		long maxmargin = Long.MAX_VALUE - rgt;
		addCValueBuilder.append("update ").append(className)
				.append(" set lft=lft+:margin,rgt=rgt+:margin,depth=depth+:depth where lft>=:lft and rgt<=:rgt");
		Map<String, Object> paramValue=new HashMap<String, Object>();
		ObjectQuery objectQuery = new ObjectQuery(addCValueBuilder.toString(), paramValue);
		paramValue.put("margin", maxmargin);
		paramValue.put("depth", 0);
		paramValue.put("lft", lft);
		paramValue.put("rgt", rgt);
		//updateOwnChild(maxmargin, 0, lft, rgt, addCValueBuilder.toString()); // 将待移动节点及子节点左右值按最大范围进行操作,避免更新时更新到
		executeHql(objectQuery);
		StringBuilder rgtHqlBuilder = new StringBuilder(100);// 更新右值hql
		rgtHqlBuilder.append("update ").append(className).append(" set rgt=rgt-:margin where rgt>:rgt").append(" and rgt<:prgt ");
		;
		StringBuilder lftHqlBuilder = new StringBuilder(100);// 更新左值hql
		lftHqlBuilder.append("update ").append(className).append(" set lft=lft-:margin where lft>:rgt").append(" and lft<:prgt ");
		long move;
		long recoverValue;
		Map<String, Object> rgtParamValue=new HashMap<String, Object>();
//		Map<String, Object> lftParamValue=new HashMap<String, Object>();
		if (rgt < prgt) {// 如果是从前往后移
			rgtParamValue.put("margin", margin);
			rgtParamValue.put("rgt", rgt);
			rgtParamValue.put("prgt", prgt);
			executeHql(lftHqlBuilder.toString(), rgtParamValue);
			executeHql(rgtHqlBuilder.toString(), rgtParamValue);
//			updateLftRgt(margin, rgt, prgt, rgtHqlBuilder.toString());// 从前往后应先减
//			updateLftRgt(margin, rgt, prgt, lftHqlBuilder.toString()); // 先减去此节点及其子孙节点影响的节点左右值.
			move = prgt - rgt - 1; // plft
									// prgt?使用prgt因为父节点的左值和右值可能差很多,所以用最大值,保证范围
			recoverValue = move - maxmargin;// 去掉避免重复而人为增加的部分,得到真实的修改值.
		} else { // 从后往前移
			rgtParamValue.put("margin", 0-margin);
			rgtParamValue.put("rgt", prgt-1);
			rgtParamValue.put("prgt", rgt);
			executeHql(rgtHqlBuilder.toString(), rgtParamValue);
			rgtParamValue.put("rgt", prgt);
			executeHql(lftHqlBuilder.toString(), rgtParamValue);
//			updateLftRgt(0 - margin, prgt - 1, rgt, rgtHqlBuilder.toString());// 从后往前时须将移入的父节点右值加,而从后往前其受影响的应加
//			updateLftRgt(0 - margin, prgt, rgt, lftHqlBuilder.toString()); // 先减去此节点及其子孙节点影响的节点左右值.
			move = prgt - rgt + 1; // plft
									// prgt?使用prgt因为父节点的左值和右值可能差很多,所以用最大值,保证范围
			recoverValue = move - maxmargin;// 去掉避免重复而人为增加的部分,得到真实的修改值.
		}
		paramValue.put("margin", recoverValue);
		paramValue.put("depth",newDepth);
		paramValue.put("lft", maxmargin);
		paramValue.put("rgt", Long.MAX_VALUE);
		executeHql(addCValueBuilder.toString(), paramValue);
		updateOwnChild(recoverValue, newDepth, maxmargin, Long.MAX_VALUE, addCValueBuilder.toString());
		return true;
	}

	/**
	 * @Title:updateOwnChild
	 * @Description:更新节点及子节点的属性
	 * @param prgt
	 * @param rgt
	 * @param margin
	 * @param className
	 * @param newDepth
	 */
	private void updateOwnChild(Long margin, Integer newDepth, Long lft, Long rgt, String hql) {
//		ObjectQuery objectQuery = new ObjectQuery();
//		objectQuery.getParams().add(margin);
//		objectQuery.getParams().add(margin);
//		objectQuery.getParams().add(newDepth);
//		objectQuery.getParams().add(lft);
//		objectQuery.getParams().add(rgt);
//		executeHql(hql, objectQuery);// 更新此树及子孙节点
	}

	/**
	 * @Title:更新移动后的相关记录左右值
	 */
	@SuppressWarnings("unchecked")
	private void updateLftRgt(Long margin, Long rgt, Long plft, String lftrgthql) {
		ObjectQuery objectQuery = new ObjectQuery();
		objectQuery.getParams().add(margin);
		objectQuery.getParams().add(rgt);
		if (rgt < plft) { // 如果节点往后移
			objectQuery.getParams().add(plft);
		}
//		executeHql(lftrgthql, objectQuery);
	}

	@Override
	public Boolean ChangeOrder(LftRgtTreeNode treeNode1, LftRgtTreeNode treeNode2) {
		LftRgtTreeNode tree = null;
		LftRgtTreeNode ptree = null;
		if (treeNode2.getRgt() < treeNode1.getRgt()) { // 让左值小的在前面
			tree = treeNode2;
			ptree = treeNode1;
		} else {
			tree = treeNode1;
			ptree = treeNode2;
		}
		Long plft = ptree.getLft();
		Long prgt = ptree.getRgt();
		Integer pdepth = ptree.getDepth();// 父节点相关属性
		Long lft = tree.getLft();
		Long rgt = tree.getRgt();
		Integer depth = tree.getDepth();// 待移动根节点的相关属性
		StringBuilder addCValueBuilder = new StringBuilder(100);
		int newDepth = pdepth - depth;
		if (2 * rgt + 2 > Long.MAX_VALUE)
			return false;
		long maxmargin = Long.MAX_VALUE - prgt + 1;
		addCValueBuilder.append("update ").append(className)
				.append(" set lft=lft+:margin,rgt=rgt+:margin,depth=depth+:depth where lft>=:lft and rgt<=:rgt");
		executeHql(addCValueBuilder.toString(), new String[]{"margin","depth","lft","rgt"}, new Object[]{maxmargin,0,lft,plft});
//		updateOwnChild(maxmargin, 0, lft, plft, addCValueBuilder.toString()); // 将待移动节点及子节点左右值按最大范围进行操作,避免更新时更新到

		StringBuilder rgtHqlBuilder = new StringBuilder(100);// 更新左右值hql,因其不是放在节点下,故可同时更新左右值.
		rgtHqlBuilder.append("update ").append(className)
				.append(" set lft=lft-:margin, rgt=rgt-:margin, depth=depth-:depth where rgt>:plft").append(" and rgt<=:prgt ");
		long swapmargin = plft - lft;
		executeHql(rgtHqlBuilder.toString(), new String[]{"margin","depth","plft","prgt"},new Object[]{swapmargin, 0, plft, prgt});
//		updateOwnChild(swapmargin, 0, plft, prgt, rgtHqlBuilder.toString());// 先减去此节点及其子孙节点影响的节点左右值.
		long move = prgt - plft + 1; // plft
										// prgt?使用prgt因为父节点的左值和右值可能差很多,所以用最大值,保证范围
		long recoverValue = move - maxmargin;// 去掉避免重复而人为增加的部分,得到真实的修改值.
		executeHql(addCValueBuilder.toString(), new String[]{"margin","depth","lft","rgt"}, new Object[]{recoverValue, newDepth, maxmargin, Long.MAX_VALUE});
//		updateOwnChild(recoverValue, newDepth, maxmargin, Long.MAX_VALUE, addCValueBuilder.toString());
		return true;
	}

	public Boolean ChangeOrderDesc(LftRgtTreeNode treeNode1, LftRgtTreeNode treeNode2) {
		LftRgtTreeNode tree = null;
		LftRgtTreeNode ptree = null;
		if (treeNode2.getRgt() < treeNode1.getRgt()) { // 让左值小的在前面
			tree = treeNode2;
			ptree = treeNode1;
		} else {
			tree = treeNode1;
			ptree = treeNode2;
		}
		Long prgt = ptree.getRgt();
		Integer pdepth = ptree.getDepth();// 父节点相关属性
		Long lft = tree.getLft();
		Long rgt = tree.getRgt();
		Integer depth = tree.getDepth();// 待移动根节点的相关属性
		StringBuilder addCValueBuilder = new StringBuilder(100);
		int newDepth = pdepth - depth;
		if (2 * rgt + 2 > Long.MAX_VALUE) {
			return false;
		}
		long maxmargin = Long.MAX_VALUE - prgt;
		addCValueBuilder.append("update ").append(className)
				.append(" set lft=lft+:margin,rgt=rgt+:margin,depth=depth+:depth where lft>=:lft and rgt<=:rgt");
		executeHql(addCValueBuilder.toString(), new String[]{"margin","depth","lft","rgt"}, new Object[]{maxmargin,0,lft,rgt});
//		updateOwnChild(maxmargin, 0, lft, rgt, addCValueBuilder.toString()); // 将待移动节点及子节点左右值按最大范围进行操作,避免更新时更新到

		StringBuilder rgtHqlBuilder = new StringBuilder(100);// 更新左右值hql,因其不是放在节点下,故可同时更新左右值.
		rgtHqlBuilder.append("update ").append(className)
				.append(" set lft=lft-:margin, rgt=rgt-:margin, depth=depth-:depth where lft>:lft").append(" and rgt<=:rgt ");
		long swapmargin = rgt - lft + 1;
		executeHql(rgtHqlBuilder.toString(), new String[]{"margin","depth","plft","prgt"},new Object[]{swapmargin, 0, rgt, prgt});
//		updateOwnChild(swapmargin, 0, rgt, prgt, rgtHqlBuilder.toString());// 先减去此节点及其子孙节点影响的节点左右值.
		long move = prgt - rgt; // plft prgt?使用prgt因为父节点的左值和右值可能差很多,所以用最大值,保证范围
		long recoverValue = move - maxmargin;// 去掉避免重复而人为增加的部分,得到真实的修改值.
		executeHql(addCValueBuilder.toString(), new String[]{"margin","depth","lft","rgt"}, new Object[]{recoverValue, newDepth, maxmargin, Long.MAX_VALUE});
//		updateOwnChild(recoverValue, newDepth, maxmargin, Long.MAX_VALUE, addCValueBuilder.toString());
		return true;
	}

	@Override
	public Boolean MovePre(LftRgtTreeNode t) {
		LftRgtTreeNode treeNode = (LftRgtTreeNode) t;
		Long lft = treeNode.getLft();
		Long rgt = treeNode.getRgt();
		Integer depth = treeNode.getDepth();
		LftRgtTreeNode brotherTreeNode = null;
		List<LftRgtTreeNode> parentTreeNodeList = getParentTreeNodeList(lft, rgt);
		if (parentTreeNodeList != null && parentTreeNodeList.size() > 0) {
			LftRgtTreeNode ptree = parentTreeNodeList.get(0);// 取得第一个父节点
			if (ptree.getLft() + 1 < lft) {// 如果不是第一个子节点,意味着可以前移
				List<LftRgtTreeNode> brotherList = getBrotherTreeNodeList(depth, ptree.getLft(), ptree.getRgt());
				if (brotherList != null && brotherList.size() > 0) {// 此判断应该可以去掉
					int bListSize = brotherList.size();
					for (int i = 0; i < bListSize; i++) {
						if (brotherList.get(i).getLft().equals(lft)) {
							brotherTreeNode = brotherList.get(i - 1);
							break;
						}
					}
					ChangeOrder(treeNode, brotherTreeNode);
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * 取得节点下指定深度的所有同层子(孙)节点
	 * 
	 * @Title:getBrotherTreeNodeList
	 * @param lft
	 * @param rgt
	 * @param depth
	 * @param lft2
	 * @param rgt2
	 * @param @return
	 * @return List<LftRgtTreeNode>
	 */
	private List<LftRgtTreeNode> getBrotherTreeNodeList(Integer depth, Long plft, Long prgt) {
//		StringBuilder bTreeNodeHqlBuilder = new StringBuilder(100);
//		bTreeNodeHqlBuilder.append("from  ").append(className).append(" where lft>? and rgt<? and depth=? ")
//				.append(" order by lft asc");
//		(List<LftRgtTreeNode>) findByHql(bTreeNodeHqlBuilder.toString(), new Object[] { plft, prgt, depth });
		return (List<LftRgtTreeNode>) findByNamedParam("from "+className+" where lft>:lft and rgt<:rgt and depth=:depth order  by lft asc", new String[]{"lft","rgt","depth"}, new Object[] { plft, prgt, depth });
	}

	/**
	 * @Title:getParentTreeNodeList 取得节点的父节点列表
	 * @param lft
	 * @param rgt
	 * @return List<LftRgtTreeNode>
	 */
	private List<LftRgtTreeNode> getParentTreeNodeList(Long lft, Long rgt) {
//		StringBuilder pTreeNodeHqlBuilder = new StringBuilder(100);
//		pTreeNodeHqlBuilder.append("from  ").append(className).append(" where lft<? ").append(" and rgt>? ")
//				.append(" order by lft desc");
//		(List<LftRgtTreeNode>) findByHql(pTreeNodeHqlBuilder.toString(), new Object[] { lft, rgt });
		return (List<LftRgtTreeNode>) findByNamedParam("from "+className+" where lft<:lft and rgt>:rgt order by lft desc", new String[]{"lft", "rgt"},new Object[]{lft,rgt});
	}

	@Override
	public Boolean MoveNext(LftRgtTreeNode t) {
		LftRgtTreeNode treeNode = t;
		Long lft = treeNode.getLft();
		Long rgt = treeNode.getRgt();
		Integer depth = treeNode.getDepth();
		LftRgtTreeNode brotherTreeNode = null;
		List<LftRgtTreeNode> parentTreeNodeList = getParentTreeNodeList(lft, rgt);
		if (parentTreeNodeList != null && parentTreeNodeList.size() > 0) {
			LftRgtTreeNode ptree = parentTreeNodeList.get(0);// 取得第一个父节点
			if (ptree.getRgt() - 1 > rgt) {// 如果不是最后一个子节点,意味着可以后移
				List<LftRgtTreeNode> brotherList = getBrotherTreeNodeList(depth, ptree.getLft(), ptree.getRgt());
				if (brotherList != null && brotherList.size() > 0) {// 此判断应该可以去掉
					int bListSize = brotherList.size();
					for (int i = 0; i < bListSize - 1; i++) {
						if (brotherList.get(i).getLft().equals(lft)) {
							brotherTreeNode = brotherList.get(i + 1);
							break;
						}
					}
					ChangeOrder(brotherTreeNode, treeNode);
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean MoveFirst(LftRgtTreeNode t) {
		LftRgtTreeNode treeNode = (LftRgtTreeNode) t;
		Long lft = treeNode.getLft();
		Long rgt = treeNode.getRgt();
		Integer depth = treeNode.getDepth();
		LftRgtTreeNode brotherTreeNode = null;
		List<LftRgtTreeNode> parentTreeNodeList = getParentTreeNodeList(lft, rgt);
		if (parentTreeNodeList != null && parentTreeNodeList.size() > 0) {
			LftRgtTreeNode ptree = parentTreeNodeList.get(0);// 取得第一个父节点
			if (ptree.getLft() + 1 < lft) {// 如果不是第一个子节点,意味着可以前移
				List<LftRgtTreeNode> brotherList = getBrotherTreeNodeList(depth, ptree.getLft(), ptree.getRgt());
				if (brotherList != null && brotherList.size() > 0) {// 此判断应该可以去掉
					brotherTreeNode = brotherList.get(0);
					ChangeOrder(treeNode, brotherTreeNode);
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.jely.cd.dao.IBaseTreeDao#MoveBottom(java.lang.Object)
	 */
	@Override
	public Boolean MoveLast(LftRgtTreeNode t) {
		LftRgtTreeNode treeNode = t;
		Long lft = treeNode.getLft();
		Long rgt = treeNode.getRgt();
		Integer depth = treeNode.getDepth();
		LftRgtTreeNode brotherTreeNode = null;
		List<LftRgtTreeNode> parentTreeNodeList = getParentTreeNodeList(lft, rgt);
		if (parentTreeNodeList != null && parentTreeNodeList.size() > 0) {
			LftRgtTreeNode ptree = parentTreeNodeList.get(0);// 取得第一个父节点
			if (ptree.getRgt() - 1 > rgt) {// 如果不是最后一个子节点,意味着可以后移
				List<LftRgtTreeNode> brotherList = getBrotherTreeNodeList(depth, ptree.getLft(), ptree.getRgt());
				if (brotherList != null && brotherList.size() > 0) {// 此判断应该可以去掉
					int bListSize = brotherList.size();
					brotherTreeNode = brotherList.get(bListSize - 1);
					ChangeOrderDesc(treeNode, brotherTreeNode);
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean save(T t, Serializable pid) {
		if (!(t instanceof LftRgtTreeNode))
			return false;
		LftRgtTreeNode treeNode = (LftRgtTreeNode) t;
		if (null == pid || StringUtils.isBlank(pid.toString())) {
			if (getCount(null) > 0) {
				throw new AttrConflictException("只能有一个根记录,请指定父id");
			}
			treeNode.setLft(1l);
			treeNode.setRgt(2l);
			treeNode.setDepth(0);
		} else {
			T pt = getById(pid);
			LftRgtTreeNode parent = (LftRgtTreeNode) pt;
			Long prgt = parent.getRgt();
			treeNode.setLft(prgt);
			treeNode.setRgt(prgt + 1l);
			treeNode.setDepth(parent.getDepth() + 1);
			if (2 * prgt + 2 > Long.MAX_VALUE) {// 大于最大值将不能再移动等操作,故不再保存
				return false;
			}
//			ObjectQuery objectQuery = new ObjectQuery();
//			objectQuery.addWhere(" rgt>=:rgt ", "rgt", prgt);
//			String hql = "update " + className + " set rgt=rgt+2 ";// 更新此父节点及它的兄弟\父节点的右值.
//			executeHql(hql, objectQuery);
			executeHql("update "+className+" set rgt=rgt+2 where rgt>=:rgt", new String[]{"rgt"}, new Object[]{prgt});
//			objectQuery = new ObjectQuery();
//			objectQuery.addWhere(" lft>:lft ", "lft", prgt);// 更新此父节点后面(左值大于此父节点的)的兄弟\父节点的左值.
//			hql = "update " + className + " set lft=lft+2 ";
//			executeHql(hql, objectQuery);
			executeHql("update "+className+" set lft=lft+2 where lft>:lft",new String[]{"lft"},new Object[]{prgt});
			// getHibernateTemplate().refresh(pt);//在测试事务中解决更新问题,也可不用事务即可.
			getHibernateTemplate().evict(pt);// 在测试事务中解决更新问题,也可不用事务即可.
		}
		Boolean ret = (save(t) != null);
		return ret;
	}

	@Override
	public List<T> findTreeNodes(Serializable id,boolean includeself) {
		StringBuilder hqlBuilder = new StringBuilder(100);
		hqlBuilder.append(" from ").append(className);
		List<T> list = null;
		if (id == null) {
			hqlBuilder.append(" order by lft");
			// " from Region c  order by c.lft ";
			list = findByHql(hqlBuilder.toString());
		} else {
			if(includeself){
				hqlBuilder.append(" where lft>=? and rgt<=? order by lft");//如果要包含自己
			}else{
				hqlBuilder.append(" where lft>? and rgt<? order by lft");//如果不包含自己
			}
			T pt = getById(id);
			LftRgtTreeNode treeNode = (LftRgtTreeNode) pt;
			list = findByHql(hqlBuilder.toString(), new Object[] { treeNode.getLft(), treeNode.getRgt() });
			// hql=" from Region c where c.lft>? and c.rgt<? order by c.lft ";
		}
		// String
		// hql="select new cn.jely.cd.domain.Region(c.id,c.name,c.item,c.lft,c.rgt,count(c)-1 as level) from Region p,Region c "
		// +
		// "where c.lft>=p.lft and c.rgt<=p.rgt group by c.name order by c.lft ";
		setLeafProperty(list);
		return list;
	}

	/**
	 * @Title:setLeafProperty
	 * @param list
	 *            void
	 */
	private void setLeafProperty(List<T> list) {
		for (T t : list) {
			LftRgtTreeNode tn = (LftRgtTreeNode) t;
			if (tn.getLft() + 1 == tn.getRgt()) {
				tn.setIsLeaf(true);
			} else {
				tn.setIsLeaf(false);
			}
		}
	}

	@Override
	public LftRgtTreeNode getParentNode(LftRgtTreeNode treeNode) {
		if (treeNode != null) {
			List<LftRgtTreeNode> parentTreeNodeList = getParentTreeNodeList(treeNode.getLft(), treeNode.getRgt());
			if (parentTreeNodeList != null && parentTreeNodeList.size() > 0) {
				return parentTreeNodeList.get(0);// 取得第一个父节点
			}
		}
		return null;
	}

	@Override
	public LftRgtTreeNode getPrevNode(LftRgtTreeNode treeNode) {
		if (treeNode != null) {
			ObjectQuery objectQuery = new ObjectQuery().addWhere(" o.rgt <:rgt", "rgt", treeNode.getLft());
			objectQuery.setOrderField("o.rgt");
			objectQuery.setOrderType(ObjectQuery.ORDERDESC);
			return (LftRgtTreeNode) findObject(objectQuery);
		}
		return null;
	}

	@Override
	public LftRgtTreeNode getNextNode(LftRgtTreeNode treeNode) {
		if (treeNode != null) {
			ObjectQuery objectQuery = new ObjectQuery().addWhere(" o.lft >:lft ", "lft", treeNode.getRgt());
			objectQuery.setOrderField("o.rgt");
			objectQuery.setOrderType(ObjectQuery.ORDERDESC);
			return (LftRgtTreeNode) findObject(objectQuery);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public LftRgtTreeNode getLastChild(LftRgtTreeNode treeNode) {
		if (treeNode != null) {
			StringBuilder hqlBuilder = new StringBuilder(100);
			hqlBuilder.append(" from ").append(className);
			List<T> list = null;
			hqlBuilder.append(" where lft>:lft and rgt<:rgt and depth=:depth order by lft desc");
			list = findByNamedParam(hqlBuilder.toString(),new String[]{"lft","rgt","depth"},
					new Object[] { treeNode.getLft(), treeNode.getRgt(), treeNode.getDepth() + 1 });
			if (list != null && list.size() > 0) {
				return (LftRgtTreeNode) list.get(0);
			}
		}
		return null;
	}

	@Override
	public Boolean deleteCascade(LftRgtTreeNode treeNode) {
		getHibernateTemplate().refresh(treeNode);// 如果在缓存中,则需要更新一下,因为可能上次hql操作已经影响它的左右值
		Long rgt = treeNode.getRgt();
		Long margin = rgt - treeNode.getLft() + 1;
//		StringBuilder hqlBuilder = new StringBuilder();
		// hqlBuilder.append("delete from ").append(className).append(" where lft>=? and rgt <=?");
//		ObjectQuery objectQuery = new ObjectQuery("delete from " + className);
//		Map<String, Object> paramValueMap = new HashMap<String, Object>();
//		paramValueMap.put("lft", treeNode.getLft());
//		paramValueMap.put("rgt", treeNode.getRgt());
//		objectQuery.addWhere("where lft>=:lft and rgt<=:rgt", paramValueMap);
//		executeHql(hqlBuilder.toString(), new ObjectQuery("", treeNode.getLft(), treeNode.getRgt()));
		executeHql("delete from "+className+" where lft>=:lft and rgt<=:rgt",new String[]{"lft","rgt"},new Object[]{treeNode.getLft(),treeNode.getRgt()});
		executeHql("update "+className+" set rgt=rgt-:margin where rgt>:rgt", new String[]{"margin","rgt"},new Object[]{margin,rgt});// 减去受影响部分的右值(这样可以更新到父及root节点.
		executeHql("update "+className+" set lft=lft-:margin where lft>:lft",new String[]{"margin","lft"},new Object[]{margin,rgt});// 减去受影响部分的左值
//		executeHql(new StringBuilder("update ").append(className).append(" set rgt=rgt-? where rgt>?").toString(),
//				new ObjectQuery("", margin, rgt));
//		executeHql(new StringBuilder("update ").append(className).append(" set lft=lft-? where lft>?").toString(),
//				new ObjectQuery("", margin, rgt));// 减去受影响部分的左值
		return true;
	}

}
