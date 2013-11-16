/*
 * 捷利商业进销存管理系统
 * @(#)ITreeOperateAction.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-2-19
 */
package cn.jely.cd.util;

/**
 * @ClassName:ITreeOperateAction
 * @Description:树的前台Action动作接口
 * @author 周义礼
 * @version 2013-2-19 下午9:47:48
 *
 */
public interface ITreeOperateAction {

	/**@Title:movepre 将节点前移 */
	public String movePre();
	/**@Title:movepre 将节点后移 */
	public String moveNext();
	/**@Title:movepre 将节点移至最前 */
	public String moveFirst();
	/**@Title:movepre 将节点移至最后 */
	public String moveLast();
	/** @Title:updateTreeNode	更新节点 */
	public String updateTreeNode();
	/**@Title:movepre 将节点移入某一节点下 */
	//public String moveIn();
}
