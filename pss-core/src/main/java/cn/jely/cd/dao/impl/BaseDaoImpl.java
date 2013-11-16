package cn.jely.cd.dao.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.engine.SessionImplementor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.ObjectQueryTool;
import cn.jely.cd.util.query.QueryGroup;
import cn.jely.cd.util.query.QueryRule;

public class BaseDaoImpl<T> extends HibernateDaoSupport implements IBaseDao<T> {
	protected Class<T> entityClass;

	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		Type type = this.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			entityClass = (Class<T>) pt.getActualTypeArguments()[0];
		}
	}

	@Resource
	// 采用注解注入,在父类中没有采用注解,故需要自写一个方法.
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Serializable save(T t) {
		return getHibernateTemplate().save(t);
	}

	@Override
	public void delete(Serializable id) {
		T t = getById(id);
		if (t == null) {
			throw new RuntimeException("删除错误!");
		}
		getHibernateTemplate().delete(t);
	}

	// @Override
	// public void delete(Object obj) {
	// getHibernateTemplate().delete(obj);
	// }

	public void clearCache() {
		getHibernateTemplate().clear();
	}
 
	@Override
	public void evict(T t) {
		getHibernateTemplate().evict(t);
	}

	@Override
	public void delete(final String prop, final Object[] ids) {

		getHibernateTemplate().execute(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException, SQLException {
				if (StringUtils.isNotBlank(prop) && ids != null && ids.length > 0) {
					delete(entityClass.getName(), prop, ids);
				} else {
					delete(entityClass.getName(), null, null);
				}
				return null;
			}
		});

	}

	// @Override
	// public void delete(final String prop, final Object[] ids) {
	// if (ids != null) {
	// getHibernateTemplate().execute(new HibernateCallback<T>() {
	// @Override
	// public T doInHibernate(Session session) throws HibernateException,
	// SQLException {
	// ArrayConverter.Longs2Strings((Long)ids);
	// deleteByProp(entityClass.getName(), prop, ids);
	// return null;
	// }
	// });
	// }
	// }

	@Override
	public void update(T t) {
		// getHibernateTemplate().update(t);//在一对多/多对多的实际情况中容易报错.要不就在前面Session.clear
		getHibernateTemplate().merge(t);
	}

	@Override
	public T getById(Serializable id) {
		return getHibernateTemplate().get(entityClass, id);
	}

	@Override
	public List<T> getAll() {
		return getHibernateTemplate().loadAll(entityClass);
	}

	@Override
	public Pager<T> findPager(ObjectQuery objectQuery) {
		objectQuery = prepareObjectQuery(objectQuery);
		final Pager<T> pager = new Pager<T>(objectQuery.getPageNo(), objectQuery.getPageSize(), getCount(objectQuery));
		final String hql = objectQuery.getFullHql();
		final Map<String, Object> paramValue = objectQuery.getParamValueMap();
		@SuppressWarnings("unchecked")
		List<T> rows = getHibernateTemplate().executeFind(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				setQueryParamMap(query, paramValue).setFirstResult((pager.getPageNo() - 1) * pager.getPageSize())
						.setMaxResults(pager.getPageSize());
				return query.list();
			}
		});
		pager.setRows(rows);
		return pager;
	}

	protected ObjectQuery prepareObjectQuery(ObjectQuery objectQuery) {
		if (objectQuery != null) {
			QueryGroup queryGroup = objectQuery.getQueryGroup();
			if(objectQuery.getQueryClass()==null){
				objectQuery.setQueryClass(entityClass);
			}
			try {
				queryGroup = ObjectQueryTool.toRealQueryGroup(queryGroup, objectQuery.getQueryClass());
				objectQuery.setQueryGroup(queryGroup);
			} catch (IntrospectionException e1) {
				e1.printStackTrace();
				logger.error("转换出错");
			}
//			List<QueryRule> allRule = ObjectQueryTool.getAllRule(queryGroup);
//			for (QueryRule queryRule : allRule) {
//				if(!queryRule.isRealType()){
//					if(queryRule.getRootClass()==null){
//						queryRule.setRootClass(objectQuery.getQueryClass());
//					}
//					try {
//						queryRule = ObjectQueryTool.toRealQueryRule(queryRule);
//					} catch (IntrospectionException e) {
//						e.printStackTrace();
//					}
//				}
//			}
			return objectQuery;
		}else{
			ObjectQuery newObjectQuery = new ObjectQuery();
			newObjectQuery.setQueryClass(entityClass);
			return newObjectQuery;
			
		}
//			final Map<String, Object> paramValue = objectQuery.getParamValueMap();
//			if (!paramValue.isEmpty()) {
//				DateConverter dateconverter = new DateConverter();
//				dateconverter.setPatterns(new String[] { "yyyy-MM-dd", "yyyy/MM/dd", "MM/dd/yyyy" });
//				ConvertUtils.register(dateconverter, java.util.Date.class);
//				for (String key : paramValue.keySet()) {
//					Class<?> fieldType = null;
//					try {
//						// key=key.replaceAll("o\\.", "");
//						fieldType = findFieldType(objectQuery.getQueryClass(), key);
//					} catch (IntrospectionException e) {
//						e.printStackTrace();
//					}
//					paramValue.put(key, ConvertUtils.convert(paramValue.get(key), fieldType));
//				}
//				objectQuery.setJqGridCondition(false);
//			}
	}

	@Override
	public Pager<T> findPager(final ObjectQuery objectQuery, String condition) {
		final Pager<T> pager = new Pager<T>(objectQuery.getPageNo(), objectQuery.getPageSize(), getCount(objectQuery));
		final StringBuilder builder = new StringBuilder(100);
		prepareWhere(objectQuery, builder);
		prepareOrder(objectQuery, builder);
		if (builder.indexOf("where") > 0) {
			builder.append(" and ").append(condition);
		} else {
			builder.append(" where ").append(condition);
		}

		@SuppressWarnings("unchecked")
		List<T> rows = getHibernateTemplate().executeFind(new HibernateCallback<List<T>>() {

			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = setQueryParams(objectQuery, builder, session).setFirstResult(
						(pager.getPageNo() - 1) * pager.getPageSize()).setMaxResults(pager.getPageSize());
				return query.list();
			}
		});
		pager.setRows(rows);
		return pager;
	}

	private void prepareOrder(ObjectQuery objectQuery, StringBuilder builder) {
		if (objectQuery != null) {
			builder.append(objectQuery.getOrder());
		}
	}

	private void prepareWhere(final ObjectQuery objectQuery, final StringBuilder builder) {
		builder.append("SELECT o FROM ").append(entityClass.getName()).append(" o");
		if (objectQuery != null && StringUtils.isNotBlank(objectQuery.getWhere())) {
			builder.append(" where ").append(objectQuery.getWhere());
		}
	}

	public Long getCount(ObjectQuery objectQuery) {
		// Session session = getSession();
		// session.createQuery("").setCacheable(true).list();
		// getHibernateTemplate()
		// .getSessionFactory()
		// .getCurrentSession() //这个是绑定到当前线程的。而下面的回调是有hibernate给的，不用关心session。
		// .createQuery(
		// "SELECT COUNT(o) from " + entityClass.getName()
		// + " o where 1=1 ");
		objectQuery = prepareObjectQuery(objectQuery);
		final String countHql = objectQuery.getCountHql();
		final Map<String, Object> paramValueMap = objectQuery.getParamValueMap();
		Long totalRecord = getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(countHql);
				query = setQueryParamMap(query, paramValueMap);
				return (Long) query.uniqueResult();
			}
		});
		return totalRecord == null ? 0 : totalRecord;
	}

	/**
	 * 根据objectQuery来拼接查询条件(Where 部分)
	 * 
	 * @param objectQuery
	 *            中的params保存了查询条件的属性值和属性类型
	 * @param builder
	 *            包含了前半部分语句
	 * @param session
	 *            Hibernate的Session
	 * @return Query Hibernate的Query
	 */
	private Query setQueryParams(final String hql, final Map<String, Object> paramValueMap, Session session) {
		Query query = session.createQuery(hql);
		setQueryParamMap(query, paramValueMap);
		return query;
		// int paramSize = paramValueMap.size();
		// if (paramSize > 0) {
		// for (int i = 0; i < paramSize; i++) {
		// if (objectQuery.isJqGridCondition()) {
		// JqGridFieldValue jqfv = (JqGridFieldValue)
		// objectQuery.getParams().get(i);
		// Class<?> fieldType = null;
		// try {
		// fieldType = findFieldType(entityClass, jqfv.getFieldName());
		// } catch (IntrospectionException e) {
		// e.printStackTrace();
		// }
		// if (fieldType.getName().contains("Date")) { // 注册util.date转换器,其它也可注册
		// DateConverter dateconverter = new DateConverter();
		// dateconverter.setPatterns(new String[] { "yyyy-MM-dd", "yyyy/MM/dd",
		// "MM/dd/yyyy" });
		// ConvertUtils.register(dateconverter, fieldType);
		// }
		// // Converter converter=ConvertUtils.lookup(fieldType);
		// // converter.convert(type, value);
		// // 这是ConvertUtils.convert用到的代码
		// query.setParameter(i, ConvertUtils.convert(jqfv.getFieldValue(),
		// fieldType));
		// } else {
		// query.setParameter(i, objectQuery.getParams().get(i));
		// }
		// }
		// }
		// return query;
	}

	private Query setQueryParams(final ObjectQuery objectQuery, final StringBuilder builder, Session session) {
		Query query = session.createQuery(builder.toString());
		int paramSize = objectQuery == null ? 0 : objectQuery.getParams().size();
		if (paramSize > 0) {
			for (int i = 0; i < paramSize; i++) {
				query.setParameter(i, objectQuery.getParams().get(i));
			}
		}
		return query;
	}

	/**
	 * 根据实体类和属性名,得到属性的类型
	 * 
	 * @param entityClass
	 * @param fieldName
	 * @return the last fieldName's type
	 * @throws IntrospectionException
	 */
	private Class<?> findFieldType(Class<?> entityClass, String fieldName) throws IntrospectionException {
		System.out.println("findfieldtype.....");
		if (entityClass == null || fieldName == null) {
			return null;
		}
		if (fieldName.contains("$")) {// 去掉jqGrid的前台传递的范围查询添加的后缀
			fieldName = fieldName.replaceAll("\\$", "");
		}
		int pos = fieldName.indexOf(".");// 如果有.号就一个一个查询它的类别
		if (pos < 0) {
			pos = fieldName.indexOf("_"); // 如果是jqgrid的字段名,因为hql的命名参数里不能有.,故前面已经替换成_,这里就需要按_来一一分别查找..
		}
		String firstName = fieldName;
		Class<?> tmpClass = entityClass;
		if (pos > 0) {
			firstName = fieldName.substring(0, pos);
			fieldName = fieldName.substring(pos + 1);
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(entityClass, Object.class);
		PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptor propertyDescriptor = properties[i];
			if (firstName.equals(propertyDescriptor.getName())) {
				tmpClass = (Class<?>) propertyDescriptor.getPropertyType();
				if (pos > 0) {
					tmpClass = findFieldType(tmpClass, fieldName);
				}
				return tmpClass;
			}
		}
		throw new RuntimeException("Get" + fieldName + "'s Type Error! Please make sure the " + entityClass
				+ "has this filed");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByHql(String hql, Object... values) {
		return getHibernateTemplate().find(hql, values);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByHql(String hql) {
		return getHibernateTemplate().find(hql);
	}

	@Override
	public void executeHql(final String hql, final String[] paramNames, final Object[] paramValues) {
		if (paramNames != null && paramValues != null && paramNames.length != paramValues.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}
		getHibernateTemplate().executeWithNativeSession(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException, SQLException {
				session.flush();
				Query query = session.createQuery(hql);
				for (int i = 0; i < paramValues.length; i++) {
					setQueryParamValue(query, paramNames[i], paramValues[i]);
				}
				query.executeUpdate();
				return null;
			}
		});
	}

	@Override
	public void executeHql(final String hql, final Map<String, Object> paramValue) {
		getHibernateTemplate().executeFind(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException, SQLException {
				session.flush();
				Query query = session.createQuery(hql);
				setQueryParamMap(query, paramValue);
				query.executeUpdate();
				return null;
			}
		});
	}

	@Override
	public void executeHql(final ObjectQuery objectQuery) {
		prepareObjectQuery(objectQuery);
		getHibernateTemplate().execute(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException, SQLException {
				session.flush();
				Query query = session.createQuery(objectQuery.getFullHql());
				setQueryParamMap(query, objectQuery.getParamValueMap());
				query.executeUpdate();
				return null;
			}
		});
	}

	public void executeHql(final String hql) {
		executeHql(hql, null);
	}

	@Override
	public void delete(final String className, final String prop, final Object[] propvalue) {
		getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("DELETE FROM ").append(className);
				if (StringUtils.isNotBlank(prop) && propvalue != null && propvalue.length > 0) {
					hql.append(" WHERE ").append(prop).append(" IN (:values)");
					return session.createQuery(hql.toString()).setParameterList("values", propvalue).executeUpdate();
				} else {
					return session.createQuery(hql.toString()).executeUpdate();
				}
			}
		});
	}

	// private void deleteByProp(final String className, final String prop,
	// Session session, Object... pvs)
	// throws HibernateException {
	// StringBuffer hql = new
	// StringBuffer("DELETE FROM ").append(className).append(" WHERE ").append(prop)
	// .append(" IN (:values)");
	// session.createQuery(hql.toString()).setParameterList("values",
	// pvs).executeUpdate();
	// }

	@Override
	public List<T> findAll(ObjectQuery objectQuery) {
		objectQuery = prepareObjectQuery(objectQuery);
		final String hql = objectQuery.getFullHql();
		final Map<String, Object> paramValueMap = objectQuery.getParamValueMap();
		@SuppressWarnings("unchecked")
		List<T> rows = getHibernateTemplate().executeFind(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query = setQueryParamMap(query, paramValueMap);
				return query.list();
			}
		});
		return rows;
	}

	@Override
	public List findAll(ObjectQuery objectQuery, boolean isJQGrid) {
		objectQuery = prepareObjectQuery(objectQuery);
		final String hql = objectQuery.getFullHql();
		final Map<String, Object> paramValueMap = objectQuery.getParamValueMap();
		@SuppressWarnings("unchecked")
		List<T> rows = getHibernateTemplate().executeFind(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query = setQueryParamMap(query, paramValueMap);
				return query.list();
			}
		});
		return rows;
	}

	@Override
	public Timestamp getDBTime() {// TODO:需要采用Hibernate来实现,暂时用SQL
		return getHibernateTemplate().execute(new HibernateCallback<Timestamp>() {
			@Override
			public Timestamp doInHibernate(Session session) throws HibernateException, SQLException {
				return (Timestamp) session.createSQLQuery(
						((SessionImplementor) session).getFactory().getDialect().getCurrentTimestampSelectString())
						.uniqueResult();
			}
		});
	}

	@Override
	public T findObject(ObjectQuery objectQuery) {
		objectQuery = prepareObjectQuery(objectQuery);
		final String hql = objectQuery.getFullHql();
		final Map<String, Object> paramValue = objectQuery.getParamValueMap();
		@SuppressWarnings("unchecked")
		List<T> rows = getHibernateTemplate().executeFind(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				setQueryParamMap(query, paramValue).setFirstResult(0).setMaxResults(1);
				return query.list();
			}
		});
		if (rows != null && rows.size() > 0) {
			return rows.get(0);
		}
		return null;
	}

	@Override
	public List findByNamedParam(String queryString, String paramName, Object value) {
		return getHibernateTemplate().findByNamedParam(queryString, paramName, value);
	}

	@Override
	public List findByNamedParam(String queryString, String[] paramNames, Object[] values) {
		return getHibernateTemplate().findByNamedParam(queryString, paramNames, values);
	}

	@Override
	public List findByNamedParam(final String queryString, final Map<String, Object> paramValueMap) {
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryString);
		setQueryParamMap(query, paramValueMap);
		return query.list();
	}

	private Query setQueryParamMap(Query query, Map<String, Object> param) {
		if (query != null ) {
			if(param != null){
				for (String key : param.keySet()) {
					if(StringUtils.isNotBlank(key)){
						Object value = param.get(key);
						setQueryParamValue(query, key, value);
					}
				}
			}
			return query;
		}
		return null;
	}

	private void setQueryParamValue(Query query, String key, Object value) {
		if (value instanceof Collection) {
			query.setParameterList(key, (Collection) value);
		} else if (value instanceof Object[]) {
			query.setParameterList(key, (Object[]) value);
		} else {
			query.setParameter(key, value);
		}
	}

	@Override
	public Long countByHql(final String hql, final Map<String, Object> variables) {
		return getHibernateTemplate().execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query = setQueryParamMap(query, variables);
				return (Long) query.iterate().next();
			}
		});
	}

	@Override
	public Long countByHql(String hql, String key, Object value) {
		Map<String, Object> paramValue = new HashMap<String, Object>();
		paramValue.put(key, value);
		return countByHql(hql, paramValue);
	}

	@Override
	public List<T> findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public T findByCriteriaObj(DetachedCriteria criteria) {
		List<T> results = getHibernateTemplate().findByCriteria(criteria, 0, 1);
		if (results != null && results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}
