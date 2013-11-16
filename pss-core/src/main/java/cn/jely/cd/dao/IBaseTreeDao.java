/*
 * 捷利商业进销存管理系统
 * @(#)IBaseTreeDao.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.dao;

import java.io.Serializable;
import java.util.List;

import cn.jely.cd.domain.LftRgtTreeNode;

/**
 * @ClassName:IBaseTreeDao
 * @author 周义礼
 * @version 2013-2-4 上午10:17:27
 *
 */
public interface IBaseTreeDao<T> extends IBaseDao<T>{

	/**
	 * @Title:将此实体移动到某一父节点下
	 * @Description:将此实体移动到此节点下,包括此节点实体的所有下级节点实体.
	 * @param  LftRgtTreeNode  需移动的实体
	 * @param  pt 父节点实体
	 * @return Boolean true:成功 ;false:失败
	 */
	public Boolean MoveIn(LftRgtTreeNode treeNode,LftRgtTreeNode pTreeNode);

	/**
	 * @Title:将两个实体顺序对调
	 * @Description:将同层级的后一个实体的放到前一个实体前面,如果任一实体为空则返回false
	 * @param  t1 需要调序的实体
	 * @param  t2 被调的实体
	 * @return Boolean true:对调成功;false:操作失败
	 */
	public Boolean ChangeOrder(LftRgtTreeNode treeNode1 ,LftRgtTreeNode treeNode2);
	/**
	 * @Title:同级内的顺序上移
	 * @Description:在同一节点下的移动,如果已经是第一条,则返回false.
	 * @param LftRgtTreeNode 需移动的实体
	 * @return Boolean true:上移操作成功.false:上移操作失败; 
	 */
	public Boolean MovePre(LftRgtTreeNode treeNode);
	/**
	 * @Title:同级内的顺序下移
	 * @Description:在同一节点下的排序移动,如果已经最后一条,则返回false.
	 * @param LftRgtTreeNode 需移动的实体
	 * @return Boolean true:下移操作成功.false:下移操作失败; 
	 */
	public Boolean MoveNext(LftRgtTreeNode treeNode);
	/**
	 * @Title:移动到同级内排序的第一条
	 * @Description:在同一节点下的移动,如果已经是第一条,则返回false.
	 * @param LftRgtTreeNode 需移动的实体
	 * @return Boolean true:移到顺序第一条操作成功.false:移动第一条操作失败; 
	 */
	public Boolean MoveFirst(LftRgtTreeNode treeNode);
	/**
	 * @Title:移动到同级内排序的最后一条
	 * @Description:在同一节点下的移动,如果已经是最后一条,则返回false.
	 * @param LftRgtTreeNode 需移动的实体
	 * @return Boolean true:移到顺序最后一条操作成功.false:移动顺序最后一条操作失败; 
	 */
	public Boolean MoveLast(LftRgtTreeNode treeNode);
	/**
	 * @Title:保存树节点
	 * @Description:如果父节点为空,则保存为根节点.
	 * @param t 待保存的节点
	 * @param id  父节点的id
	 * @return Boolean true:保存成功,false:保存失败.
	 * @throws
	 */
	public Boolean save(T t,Serializable pid);
	
	/**
	 * @Title:getParentNode取得第一个父节点 从父点点列表中取得第一个
	 * @Description:如果节点为根节点,则父节点为Null
	 * @param treeNode
	 * @return LftRgtTreeNode 第一个父节点
	 */
	public LftRgtTreeNode getParentNode(LftRgtTreeNode treeNode);
	/**
	 * @Title:getPrevNode取得此节点同一层的上一个节点
	 * @param treeNode
	 * @return LftRgtTreeNode
	 */
	public LftRgtTreeNode getPrevNode(LftRgtTreeNode treeNode);
	/**
	 * @Title:getNextNode取得此节点同一层的下一个节点
	 * @param treeNode 当前节点
	 * @return LftRgtTreeNode 同层的下一节点
	 */
	public LftRgtTreeNode getNextNode(LftRgtTreeNode treeNode);
	/**
	 * 取得树节点的最后一个子节点,如果没有子节点返回null
	 * @Title:getLastChild
	 * @param treeNode
	 * @return LftRgtTreeNode
	 */
	public LftRgtTreeNode getLastChild(LftRgtTreeNode treeNode);
	/**
	 * @Title:findTreeNodes 返回此节点下的所有子节点
	 * @Description:如果id为空,则返回所有的节点
	 * @param id 此实体的节点标识符
	 * @param depth 所取的子节点深度
	 * @param includeself 是否包含自己
	 * @return
	 * List<T>
	 * @throws 
	 */
	public List<T> findTreeNodes(Serializable id,boolean includeself/*,Integer depth*/);
	
	/**
	 * 删除当前节点及其所有子节点
	 * @param treeNode
	 * @return true 删除成功,false 删除失败(删除前检查在子类中实现)
	 */
	public Boolean deleteCascade(LftRgtTreeNode treeNode);
}
