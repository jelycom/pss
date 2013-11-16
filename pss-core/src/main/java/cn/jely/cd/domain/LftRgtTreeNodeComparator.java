/*
 * 捷利商业进销存管理系统
 * @(#)LftRgtTreeNodeComparator.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-9-12
 */
package cn.jely.cd.domain;

import java.util.Comparator;

/**
 *
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-9-12 下午5:25:07
 */
public class LftRgtTreeNodeComparator implements Comparator<LftRgtTreeNode> {

	@Override
	public int compare(LftRgtTreeNode o1, LftRgtTreeNode o2) {
			if(o1.getLft()>o2.getLft()){
				return 1;
			}else if(o1.getLft()<o2.getLft()){
				return -1;
			}else{
				return 0;
			}
		}
}
