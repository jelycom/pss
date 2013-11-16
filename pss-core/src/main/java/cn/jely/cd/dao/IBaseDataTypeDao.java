package cn.jely.cd.dao;

import cn.jely.cd.domain.BaseDataType;

public interface IBaseDataTypeDao extends IBaseTreeDao<BaseDataType> {

	/**
	 * 检查是否已经存在相同的基础数据类型,
	 * @param t
	 * @return boolean true:存在,false:不存在
	 */
	public boolean checkExist(BaseDataType t);
	
	/**
	 * 根据SN查询相应的类型数据,如果没有找到返回null.
	 * @param SN
	 * @return BaseDataType
	 */
	public BaseDataType findBySN(String SN) ;
}
