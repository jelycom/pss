/*
 * 捷利商业进销存管理系统
 * @(#)LftRgtTree.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.domain;

import org.apache.struts2.json.annotations.JSON;

/**
 * @ClassName:LftRgtTree
 * @Description:左右值树模型组件
 * @author 周义礼
 * @version 2013-2-4 下午2:14:48
 *
 */
public class LftRgtTreeNode {
	
	/**@Fields lft:节点左值*/
	private Long lft;
	/**@Fields rgt:节点右值*/
	private Long rgt;
	/**@Fields depth:节点深度*/
	private Integer depth;
	/**@Fields isLeaf:是否叶子*/
	private Boolean isLeaf;
	
	public LftRgtTreeNode() {
		super();
	}
	
	public LftRgtTreeNode(Long lft, Long rgt, Integer depth, Boolean isLeaf) {
		super();
		this.lft = lft;
		this.rgt = rgt;
		this.depth = depth;
		this.isLeaf = isLeaf;
	}
	public Long getLft() {
		return lft;
	}
	public void setLft(Long lft) {
		this.lft = lft;
	}
	public Long getRgt() {
		return rgt;
	}
	public void setRgt(Long rgt) {
		this.rgt = rgt;
	}
	@JSON(name="level")
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lft == null) ? 0 : lft.hashCode());
		result = prime * result + ((rgt == null) ? 0 : rgt.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LftRgtTreeNode other = (LftRgtTreeNode) obj;
		if (lft == null) {
			if (other.lft != null)
				return false;
		} else if (!lft.equals(other.lft))
			return false;
		if (rgt == null) {
			if (other.rgt != null)
				return false;
		} else if (!rgt.equals(other.rgt))
			return false;
		return true;
	}

}
