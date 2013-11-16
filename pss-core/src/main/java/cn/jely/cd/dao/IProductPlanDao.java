package cn.jely.cd.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.domain.ProductQuantity;

public interface IProductPlanDao<T extends ProductPlanMaster> extends IBillStateDao<T>{

	/**
	 * 检查实体相应的属性是否已经存在
	 * @param t
	 * @return boolean
	 */
	public boolean checkExist(T t);
	/**
	 *	用List中的项目更新id=键值的记录
	 * @param planMap void
	 * @return Set<String> 所影响的计划单号,如果没有则返回null
	 */
	public Set<String> update(Map<Long, List<ProductQuantity>> planMap);
}
