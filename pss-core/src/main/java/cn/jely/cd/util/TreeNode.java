/*
 * 捷利商业进销存管理系统
 * @(#)TreeNode.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName:TreeNode
 * @Description:tree树形模型,可保存树型结构的数据 easyUI也可用
 * @author 周义礼
 * @version 2013-1-6 下午9:28:57
 * 
 */
public class TreeNode<T> {
	/*
	 * •id: node id, 取得远程数据的主键
	 * 
	 * •text: node text 显示节点的文本
	 * 
	 * •state: node state, 'open' or 'closed', default is 'open'. When set to
	 * 'closed', the node have children nodes and will load them from remote
	 * site
	 * 
	 * •checked: Indicate whether the node is checked selected.
	 * 
	 * •attributes: custom attributes can be added to a node
	 * 
	 * •children: an array nodes defines some children nodes
	 */
	public static final String STATE_OPEN = "open";
	public static final String STATE_CLOSED = "closed";
	private T node;
	private Boolean chedcked;
	private String state = STATE_OPEN;
	private String attributes;
	private List<TreeNode<T>> children;

	public TreeNode() {
		this(null, STATE_OPEN);
	}

	public TreeNode(T node, String state) {
		this.node = node;
		if (null == state || (state.length()) == 0) {
			this.state = STATE_CLOSED;
		} else {
			this.state = state;
		}
		this.children = new ArrayList<TreeNode<T>>();
	}

	public void addChild(TreeNode<T> ctd) {
		this.children.add(ctd);
	}

	public T getNode() {
		return node;
	}

	public void setNode(T node) {
		this.node = node;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}

	public Boolean getChedcked() {
		return chedcked;
	}

	public void setChedcked(Boolean chedcked) {
		this.chedcked = chedcked;
	}
}
