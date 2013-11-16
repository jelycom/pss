package cn.jely.cd.service.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.service.IBaseService;
import cn.jely.cd.util.ArrayConverter;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.logic.IPinYinLogic;
import cn.jely.cd.util.logic.IPinYinLogicChecker;
import cn.jely.cd.util.logic.impl.PinYinLogicCheckerImpl;
import cn.jely.cd.util.query.ObjectQuery;

public abstract class BaseServiceImpl<T> implements IBaseService<T> {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected abstract IBaseDao<T> getBaseDao();

	@Override
	public Long save(T t) {
		// TODO:后期可改为装饰模式.或者用策略模式
		if(beforeSaveCheck(t)){
			Serializable saveId = getBaseDao().save(t);
			afterSave(t);
			return (Long) saveId;
		}else{
			throw new RuntimeException("检查实体未通过,不能保存");
		}
	}

	/**
	 * 实体保存后的动作
	 * @param t void
	 */
	protected void afterSave(T t) {
		
	}

	/**
	 * 对实体进行保存前的检查
	 * @Title:checkObj
	 * @param t
	 * @return true:通过检查,允许保存;false:未通过检查,不允许保存
	 */
	protected Boolean beforeSaveCheck(T t) {
		if (t instanceof IPinYinLogic) {
			IPinYinLogicChecker checker = new PinYinLogicCheckerImpl();
			checker.CheckLogic((IPinYinLogic) t);
		}
		return true;
	}

	/**
	 * 更新前的检查
	 * @param t
	 * @return Boolean true:检查成功,允许更新;false:失败,不允许更新
	 */
	protected Boolean beforeUpdateCheck(T t){
		return true;
	}
	@Override
	public void delete(Long id) {
		getBaseDao().delete(id);
	}

	@Override
	public void delete(String id) {
		Long[] longids = ArrayConverter.Strings2Longs(id.split(","));// 生成参数数组
		// getBaseDao().delete("id", pvs);
		for (Long longid : longids) {
			getBaseDao().delete(longid);
		}
	}

	
/*	@Override
	public void delete(Object obj) {
		getBaseDao().delete(obj);
	}*/

	@Override
	public void update(T t) {
		if(beforeUpdateCheck(t)){
			getBaseDao().update(t);
			afterUpdate(t);
		}
	}

	/**
	 * 更新实体后的相关动作
	 * @param t void
	 */
	protected void afterUpdate(T t) {
		
	}

	@Override
	public T getById(Serializable id) {// 这里传过来的应该为主键,需要转换为对应的实际类型
		return getBaseDao().getById(id);
	}

	@Override
	public T getById(String id) {
		Long longValue = null;
		try {
			longValue = Long.valueOf(id);
		} catch (Exception e) {
			throw new RuntimeException("解析id出错");
		}
		return getBaseDao().getById(longValue);
	}

	@Override
	public List<T> getAll() {
		return getBaseDao().getAll();
	}

	@Override
	public Pager<T> findPager(ObjectQuery objectQuery) {
		return getBaseDao().findPager(objectQuery);
	}

	@Override
	public List findQuery(String hql, String orders, Object... values) {
		if (StringUtils.isNotBlank(orders)) {
			hql += " order by " + orders;
		}
		return getBaseDao().findByHql(hql, values);
	}

	@Override
	public List findQuery(String hql) {
		return getBaseDao().findByHql(hql);
	}

	@Override
	public void delete(String prop, String propvalue) {
		if(StringUtils.isNotBlank(propvalue)){
			String[] props = propvalue.split(",");
			getBaseDao().delete(prop, props);
		}else{
			getBaseDao().delete(prop, null);
		}
	}

	@Override
	public List<T> findAll(ObjectQuery objectQuery) {
		return getBaseDao().findAll(objectQuery);
	}

	@Override
	public Timestamp getDBTime() {
		return getBaseDao().getDBTime();
	}

	@Override
	public T findQueryObject(ObjectQuery objectQuery) {
		if (objectQuery == null) {
			objectQuery = new ObjectQuery();
		}
		return getBaseDao().findObject(objectQuery);
	}

	@Override
	public T findQueryLastObject(ObjectQuery objectQuery) {
		if (objectQuery == null) {
			objectQuery = new ObjectQuery();
		}
		objectQuery.setOrderField("id");
		objectQuery.setOrderType(ObjectQuery.ORDERDESC);
		return getBaseDao().findObject(objectQuery);
	}
	
	@SuppressWarnings("unchecked")
	public void printReport(String hql,Map<String,Object> hqlparams) throws JRException{
		JasperReport compileReport = JasperCompileManager.compileReport("");
		List<Object> datas = getBaseDao().findByNamedParam(hql, hqlparams);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datas);
		Map<String,Object> reportParams = new HashMap<String,Object>();
		reportParams.put(JRParameter.REPORT_DATA_SOURCE, dataSource);
		reportParams.put(JRParameter.REPORT_LOCALE, Locale.CHINA);
		JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, reportParams,dataSource);
		JasperExportManager.exportReportToPdfFile(jasperPrint,"F:/TDDOWNLOAD/1.pdf");
	}
}
