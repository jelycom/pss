/*
 * 捷利商业进销存管理系统
 * @(#)AdjacencyNode.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-11-14
 */
package cn.jely.cd.util;

import java.io.Serializable;

/**
 * Adjacency(id-pid)节点类
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-11-14 下午8:19:57
 */
public class AdjacencyNode<T> implements Serializable{
	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private T node;
	private T parent;
	
	public AdjacencyNode(T node, T parent) {
		this.node = node;
		this.parent = parent;
	}
	public T getNode() {
		return node;
	}
	public void setNode(T node) {
		this.node = node;
	}
	public T getParent() {
		return parent;
	}
	public void setParent(T parent) {
		this.parent = parent;
	}
	
}
