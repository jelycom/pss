package cn.jely.cd.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;

import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

public interface IBaseDao<T> {
/** 清空缓存 */
public void clearCache();
/** 清除缓存的实体 */
public void evict(T t);
//void delete(Object obj);
void delete(Serializable id);
void delete(String prop,Object [] ids);
//void delete(String prop, String ids);
/**
 * @Title:删除指定实体,指定属性的记录
 * @Description:
 * @param  clazz Model类
 * @param  prop 在Model中的属性名
 * @param  propvalue 属性的值
 */
void delete(String className,String prop,Object[] propvalue);
void executeHql(String hql);
/**
*
* @param hql
* @param paramValue void
*/
void executeHql(String hql, Map<String, Object> paramValue);

/**
 * 
 * @Title:使用条件来执行hql查询语句
 * @param hql 需要执行的hql语句
 * @param objectQuery  hql语句对应的条件的封装
 */
void executeHql(ObjectQuery objectQuery);
List<T> findAll(ObjectQuery objectQuery);
List<T> findByCriteria(DetachedCriteria criteria);
T findByCriteriaObj(DetachedCriteria criteria);
List findByHql(String hql);
//List<T> findByHql(String hql, Object...values);
List findByHql(String hql,Object...values);
Long countByHql(String hql,Map<String,Object> variables);
Long countByHql(String hql,String key,Object value);
/**
 * 根据命名参数进行查询
 * @param queryString
 * @param paramNames
 * @param values
 * @return
 */
List findByNamedParam(final String queryString, final String paramName, final Object value);

List findByNamedParam(final String queryString, final String[] paramNames, final Object[] values);
List findByNamedParam(final String queryString, final Map<String,Object> paramValueMap);
T findObject(ObjectQuery objectQuery);
Pager<T> findPager(ObjectQuery objectQuery);
Pager<T> findPager(ObjectQuery objectQuery,String condition);

List<T> getAll();
T getById(Serializable id);
Long getCount(final ObjectQuery objectQuery);
Timestamp getDBTime();
Serializable save(T t);
void update(T t);
/**
 * 得到当前操作的实体类
 * @return Class<T>
 */
Class<T> getEntityClass();
/**
 * 按照命名参数执行hql,参数名称和参数值要一一对应
 * @param hql
 * @param paramNames 
 * @param paramValues void
 */
void executeHql(String hql, String[] paramNames, Object[] paramValues);
/**
 * 查询所有,使用objectQuery实体包装查询条件,
 * @param objectQuery
 * @param isJQGrid 是否为jqGrid发送的条件,如果true:则会进行参数的类型转换,如果false,则不会进行参数类型转换
 * @return List
 */
List findAll(ObjectQuery objectQuery, boolean isJQGrid);
}
