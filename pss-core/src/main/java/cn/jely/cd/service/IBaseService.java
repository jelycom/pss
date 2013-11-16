package cn.jely.cd.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

public interface IBaseService<T> {
Long save(T t);
//void delete(Serializable id);
//void delete(Serializable[] ids);
void delete(Long id);
void delete(String id);
//void delete(Object obj);
void delete(String prop,String propvalue);
void update(T t);
T getById(Serializable id);
T getById(String id);
List<T> getAll();
Pager<T> findPager(ObjectQuery objectQuery);
List<T> findAll(ObjectQuery objectQuery);
List findQuery(String hql,String orders,Object... values);
List findQuery(String hql);
T findQueryObject(ObjectQuery objectQuery);
/**
 * 根据ID来取得最大值(即最后条)的记录
 * @Title:findQueryLastObject
 * @Description:TODO:如果值超过最大值后会返回使用初始值.会产生潜在的Bug?
 * @param objectQuery
 * @return T
 */
T findQueryLastObject(ObjectQuery objectQuery);
Timestamp getDBTime();
}
