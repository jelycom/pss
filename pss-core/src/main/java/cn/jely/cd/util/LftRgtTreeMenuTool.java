/*
 * 捷利商业进销存管理系统
 * @(#)LftRgtTreeMenuTool.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-17
 */
package cn.jely.cd.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import cn.jely.cd.domain.LftRgtTreeNode;
import cn.jely.cd.domain.LftRgtTreeNodeComparator;
import cn.jely.cd.sys.domain.ActionResource;
import cn.jely.cd.vo.MenuTree;

/**
 * 
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-9-17 下午4:32:58
 */
public class LftRgtTreeMenuTool<T  extends LftRgtTreeNode > {

	List<MenuTree> generateMenu(Set<ActionResource> resources, Integer beginDepth) {
		ArrayList<ActionResource> sortedList = new ArrayList<ActionResource>(resources);
		Collections.sort(sortedList, new LftRgtTreeNodeComparator());
		// 将左右值进行相应的重算。
		int size = sortedList.size();
		
		if (beginDepth == null || beginDepth < 0) {
			beginDepth = 0;
		}
		List<MenuTree> menus = new ArrayList<MenuTree>();
		for (ActionResource resource : resources) {

		}
		return menus;
	}
	
	/**
	 * 将左右值模型转换为NODE -PARENTNODE 模型
	 * @param records
	 * @return List<AdjacencyNode<T>>
	 */
	public List<AdjacencyNode<T>> toAdjacencyNode(List<T> records){
		Stack<T> resourceStack = new Stack<T>();
		List<AdjacencyNode<T>> adjacencyNodes = new ArrayList<AdjacencyNode<T>>();
		T nestParent = null;
		AdjacencyNode<T> root = null;
		for (T resource : records) {
				if (nestParent == null) {
					nestParent = resource;
					root = new AdjacencyNode<T>(resource, null);
				} else {
					nestParent = resourceStack.peek();
					while (resource.getLft() < nestParent.getLft() || resource.getRgt() > nestParent.getRgt()) {
						resourceStack.pop();
						nestParent = resourceStack.peek();
					}
					root = new AdjacencyNode<T>(resource, nestParent);
				}
				resourceStack.push(resource);
				adjacencyNodes.add(root);
		}
		return adjacencyNodes;
	}
}
