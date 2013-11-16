package cn.jely.cd.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.dao.IBaseDataTypeDao;
import cn.jely.cd.domain.BaseDataType;
import cn.jely.cd.service.IBaseDataTypeService;
import cn.jely.cd.service.NestedTreeServiceImpl;

@Service("baseDataTypeService")
public class BaseDataTypeServiceImpl extends NestedTreeServiceImpl<BaseDataType> implements
			IBaseDataTypeService {

	private IBaseDataTypeDao baseDataTypeDao;
	@Resource(name="baseDataTypeDao")
	public void setBaseDataTypeDao(IBaseDataTypeDao baseDataTypeDao) {
		this.baseDataTypeDao = baseDataTypeDao;
	}

	@Override
	public IBaseDao<BaseDataType> getBaseDao() {
		return baseDataTypeDao;
	}

	@Override
	protected Boolean beforeSaveCheck(BaseDataType t) {
		return !baseDataTypeDao.checkExist(t);
	}

	@Override
	public BaseDataType findBySN(String SN) {
		return baseDataTypeDao.findBySN(SN);
	}
}
