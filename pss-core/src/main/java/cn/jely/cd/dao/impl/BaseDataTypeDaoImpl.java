package cn.jely.cd.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBaseDataTypeDao;
import cn.jely.cd.domain.BaseDataType;
import cn.jely.cd.util.query.ObjectQuery;

@Repository("baseDataTypeDao")
public class BaseDataTypeDaoImpl extends NestedTreeDaoImpl<BaseDataType> implements IBaseDataTypeDao {

	@Override
	public boolean checkExist(BaseDataType t) {
		return getCount(new ObjectQuery().addWhere("name=:name", "name", t.getName()))>0;
	}

	@Override
	public BaseDataType findBySN(String SN) {
		List<BaseDataType> types = findByNamedParam("from "+entityClass.getName()+" where sn=:sn", "sn", SN);
		if(types!=null&&types.size()>0){
			return types.get(0);
		}
		return null;
	}

}
