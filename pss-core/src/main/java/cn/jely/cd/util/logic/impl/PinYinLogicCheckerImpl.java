/*
 * 捷利商业进销存管理系统
 * @(#)PinyinLogicCheckerImpl.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-2
 */
package cn.jely.cd.util.logic.impl;

import cn.jely.cd.util.PinYinUtils;
import cn.jely.cd.util.logic.ILogic;
import cn.jely.cd.util.logic.IPinYinLogic;
import cn.jely.cd.util.logic.IPinYinLogicChecker;

/**
 * @ClassName:PinyinLogicCheckerImpl
 * @author 周义礼
 * @version 2013-4-2 下午10:24:15
 * 
 */
public class PinYinLogicCheckerImpl implements IPinYinLogicChecker {

	@Override
	public void CheckLogic(ILogic entity) {
		if (entity instanceof IPinYinLogic) {
			IPinYinLogic pinyinEntity = (IPinYinLogic) entity;
			if(pinyinEntity.checkBlank()){
				pinyinEntity.setPy(PinYinUtils.getPinYinShengMu(pinyinEntity.pySource()));
			}
		}
	}

}
