/*
 * 捷利商业进销存管理系统
 * @(#)AccountingPeriod.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.sys.dao.impl;

import java.util.List;

import cn.jely.cd.dao.impl.BaseDaoImpl;
import cn.jely.cd.sys.dao.IAccountingPeriodDao;
import cn.jely.cd.sys.domain.AccountingPeriod;

/**
 * @ClassName:AccountingPeriodDaoImpl
 * @Description:DaoImpl
 * @author
 * @version 2013-04-11 17:30:51 
 *
 */

public class AccountingPeriodDaoImpl extends BaseDaoImpl<AccountingPeriod> implements IAccountingPeriodDao {
    @Override  
	public AccountingPeriod findValidPeriod(){
    	  List<AccountingPeriod> ret=findByNamedParam("from "+entityClass.getName()+" o where o.state=:state","state",AccountingPeriod.INUSE);
    	  int periodsSize = ret.size();
		if(ret!=null&&periodsSize>0){
    		  if(periodsSize==1){
    			  return ret.get(0);
    		  }else{
    			  
    		  }
    	  }
    	  return null;
      }
}
