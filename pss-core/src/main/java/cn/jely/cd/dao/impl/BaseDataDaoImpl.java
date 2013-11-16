package cn.jely.cd.dao.impl;

import org.springframework.stereotype.Repository;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBaseDataDao;
import cn.jely.cd.domain.BaseData;

@Repository("baseDataDao")
public class BaseDataDaoImpl extends BaseDaoImpl<BaseData> implements IBaseDataDao {

}
