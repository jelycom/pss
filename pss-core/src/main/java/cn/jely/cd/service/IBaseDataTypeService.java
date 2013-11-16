package cn.jely.cd.service;

import cn.jely.cd.domain.BaseDataType;

public interface IBaseDataTypeService extends IBaseTreeService<BaseDataType> {
//	public List findChild(String string, StringBuffer id);
	public BaseDataType findBySN(String SN);
}
