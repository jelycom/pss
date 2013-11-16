/*
 * 捷利商业进销存管理系统
 * @(#)Region.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IRegionDao;
import cn.jely.cd.domain.ProductType;
import cn.jely.cd.domain.Region;
import cn.jely.cd.service.IBaseTreeService;
import cn.jely.cd.service.IRegionService;
import cn.jely.cd.service.NestedTreeServiceImpl;
import cn.jely.cd.util.query.ObjectQuery;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @ClassName:RegionServiceImpl
 * @Description:ServiceImpl
 * @author
 * @version 2012-11-09 13:48:12
 * 
 */
@Service("regionService")
public class RegionServiceImpl extends NestedTreeServiceImpl<Region> implements IRegionService {

	private IRegionDao regionDao;

	@Resource(name = "regionDao")
	public void setRegionDao(IRegionDao regionDao) {
		this.regionDao = regionDao;
	}

	@Override
	public IBaseDao<Region> getBaseDao() {
		return regionDao;
	}

	@Override
	public Boolean save(Region t, Serializable pid) {
		t.setItem(generateItem(pid == null ? null : regionDao.getById(pid)));
		return super.save(t, pid);
	}


	@Override
	protected Boolean beforeSaveCheck(Region t) {
		return !regionDao.checkExist(t);
	}

	@Override
	public String generateItem(Region region) {
		if (region != null) {
			Region lastChild = (Region) regionDao.getLastChild(region);
			if (lastChild == null) {
				return region.getItem() + String.format("%03d", 1);
			} else {
				String lastItemString = lastChild.getItem().substring(region.getItem().length());
				if (StringUtils.isBlank(lastItemString)) {
					lastItemString = "0";
				}
				Integer newItemValue = Integer.valueOf(lastItemString) + 1;
				return region.getItem() + String.format("%03d", newItemValue);
			}
		} else if (regionDao.getCount(null) == 0) {
			return "1";
		}
		return null;
	}

	// @Override
	// public Boolean save(Region region,Serializable id){
	// if(null==id){
	// region.setLft(1l);
	// region.setRgt(2l);
	// region.setDepth(0);
	// }else{
	// Region parent=getById(id);
	// Long prgt=parent.getRgt();
	// region.setLft(prgt);
	// region.setRgt(prgt+1l);
	// region.setDepth(parent.getDepth()+1);
	// ObjectQuery objectQuery=new ObjectQuery();
	// objectQuery.addWhere(" rgt>=? ", prgt);
	// String hql="update Region set rgt=rgt+2 ";
	// getBaseDao().executeHql(hql, objectQuery);
	// objectQuery=new ObjectQuery();
	// objectQuery.addWhere(" lft>? ", prgt);
	// hql="update Region set lft=lft+2 ";
	// regionDao.executeHql(hql, objectQuery);
	// }
	// return getBaseDao().save(region)==null;
	// }

	// public List<Region> findTreeNodeWithDepth(Long lft, Long rgt) {
	// String hql;
	// List<Region> regions=null;
	// if(lft==null||rgt==null||lft.equals(rgt)){
	// hql=" from Region c  order by c.lft ";
	// regions=getBaseDao().findByHql(hql);
	// }else{
	// hql=" from Region c where c.lft>? and c.rgt<? order by c.lft ";
	// regions = getBaseDao().findByHql(hql,new Object[]{lft,rgt});
	// }
	// // String
	// hql="select new cn.jely.cd.domain.Region(c.id,c.name,c.item,c.lft,c.rgt,count(c)-1 as level) from Region p,Region c "
	// +
	// // "where c.lft>=p.lft and c.rgt<=p.rgt group by c.name order by c.lft ";
	// for (Region region : regions) {
	// if(region.getLft()+1==region.getRgt()){
	// region.setIsLeaf(true);
	// }else{
	// region.setIsLeaf(false);
	// }
	// }
	// return regions;
	// }

	// @Override
	// public Boolean MoveIn(Serializable cid, Serializable pid) {
	// Region child=getById(cid);
	// Region parent=getById(pid);
	// return child==null||parent==null?false:regionDao.MoveIn(child, parent);
	// }
	//
	// @Override
	// public Boolean ChangeOrder(Serializable id1, Serializable id2) {
	// Region region1=getById(id1);
	// Region region2=getById(id2);
	// return regionDao.ChangeOrder(region1, region2);
	// }

}
