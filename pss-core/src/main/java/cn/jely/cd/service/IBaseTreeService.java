/*
 * 捷利商业进销存管理系统
 * @(#)IBaseTreeService.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service;

import java.io.Serializable;
import java.util.List;

import cn.jely.cd.domain.LftRgtTreeNode;
import cn.jely.cd.domain.Region;
import cn.jely.cd.util.TreeNode;

/**
 * @ClassName:IBaseTreeService
 * @Description:对树的相关操作,主要包含换位,上移,下移,等相关操作
 * @author 周义礼
 * @version 2013-2-4 上午10:34:04
 *
 */
public interface IBaseTreeService<T> extends IBaseService<T>{
	/**
	 * @Title:将此实体移动到某一父节点下
	 * @Description:将此实体移动到此节点下,包括此节点实体的所有下级节点实体.
	 * @param  cid 作为子的实体节点id
	 * @param  pid 作为父的实体节点id
	 * @return Boolean true:成功 ;false:失败
	 */
	public Boolean MoveIn(Serializable cid,Serializable pid);

	/**
	 * @Title:将两个实体顺序对调
	 * @Description:将两个实体的顺序,如果任一实体为空则返回false
	 * @param  id1 需要调序的实体id
	 * @param  id2 被调的实体id
	 * @return Boolean true:对调成功;false:操作失败
	 */
	public Boolean ChangeOrder(Serializable id1 ,Serializable id2);
	/**
	 * @Title:同级内的顺序上移
	 * @Description:在同一节点下的移动,如果已经是第一条,则返回false.
	 * @param id 需移动的实体主键
	 * @return Boolean true:上移操作成功.false:上移操作失败; 
	 */
	public Boolean MovePre(Serializable id);
	/**
	 * @Title:同级内的顺序下移
	 * @Description:在同一节点下的排序移动,如果已经最后一条,则返回false.
	 * @param id 需移动的实体主键
	 * @return Boolean true:下移操作成功.false:下移操作失败; 
	 */
	public Boolean MoveNext(Serializable id);
	/**
	 * @Title:移动到同级内排序的第一条
	 * @Description:在同一节点下的移动,如果已经是第一条,则返回false.
	 * @param id 需移动的实体主键
	 * @return Boolean true:移到顺序第一条操作成功.false:移动第一条操作失败; 
	 */
	public Boolean MoveFirst(Serializable id);
	/**
	 * @Title:移动到同级内排序的最后一条
	 * @Description:在同一节点下的移动,如果已经是最后一条,则返回false.
	 * @param id 需移动的实体主键
	 * @return Boolean true:移到顺序最后一条操作成功.false:移动顺序最后一条操作失败; 
	 */
	public Boolean MoveLast(Serializable id);
	
	/**
	 * @Title:保存树节点
	 * @Description:如果是根结点,则将父节点id置为null,
	 * @param region 待保存的节点
	 * @param id  父节点的id,为Null为根节点.(大部分树都只能有一个根结点)
	 * @return Boolean true:保存成功,false:保存失败.
	 * @throws
	 */
	public Boolean save(T t,Serializable id);
	

	/**
	 * @Title:update 更新树的节点.
	 * @Description:更新节点,更新时需指定其父节点
	 * @param t 待更新的节点
	 * @param id 父节点的id
	 * @return Boolean true:更新成功,false:更新失败
	 */
	public Boolean update(T t,Serializable id);
	/**
	 * @Title:findTreeNodes 查找指定节点下的所有子节点
	 * @Description:如果id为空则返回所有节点列表
	 * @param includeself 是否包含自己
	 * @return  List<T> 节点列表
	 * @throws 
	 */
	public List<T> findTreeNodes(Serializable id,boolean includeself);
	
	/**
	 * 删除当前节点及其所有子节点
	 * @param treeNode
	 * @return true 删除成功,false 删除失败(删除前检查在子类中实现)
	 */
	public Boolean deleteCascade(Serializable ... ids);
	
	/**
	 * 将左右值模型转换为Node[Children]模型
	 * @param nodeList
	 * @return TreeNode<ActionResource>
	 */
	public TreeNode<T> toChildModel(List<T> nodeList);
}
