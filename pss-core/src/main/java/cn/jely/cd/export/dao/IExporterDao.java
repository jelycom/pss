/*
 * 捷利商业进销存管理系统
 * @(#)IExporterDao.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-10-24
 */
package cn.jely.cd.export.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import cn.jely.cd.dao.IBaseDao;
import cn.jely.cd.export.ro.RealStockRO;


/**
 * 导出相关操作类（包括导出各种格式及不同的设备等）。
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-10-24 下午5:58:57
 */
public interface IExporterDao  extends IBaseDao<Object>{

//	/**
//	 * 将符合条件的数据导出到指定的流中。
//	 * @param objectQuery
//	 * @param design
//	 * @param setting TODO
//	 * @param outStream void
//	 * @throws JRException 
//	 * @throws IOException 
//	 */
//	void ExportPdf(ObjectQuery objectQuery,JasperDesign design,Map<String,Object> setting, OutputStream outStream) throws JRException, IOException;
//	void ExportPdf(ObjectQuery objectQuery,JasperReport report,Map<String,Object> setting, OutputStream outStream) throws JRException, IOException;
//	void ExportXls(ObjectQuery objectQuery,JasperDesign design,Map<JRExporterParameter,Object> setting, OutputStream outStream) throws JRException, IOException;
//	void ExportXls(ObjectQuery objectQuery,JasperReport report,Map<JRExporterParameter,Object> setting, OutputStream outStream) throws JRException, IOException;

	/**
	 *
	 * @param datas
	 * @param design
	 * @param reportParams
	 * @return
	 * @throws JRException
	 * @throws IOException JasperPrint
	 */
	public JasperPrint fillData(List<?> datas, JasperDesign design, Map<String, Object> reportParams) throws JRException,
	IOException;
	/**
	 *
	 * @param datas
	 * @param report
	 * @param reportParams
	 * @return
	 * @throws JRException
	 * @throws IOException JasperPrint
	 */
	public JasperPrint fillData(List<?> datas, JasperReport report, Map<String, Object> reportParams) throws JRException,
			IOException;

	/**
	 *
	 * @param outStream
	 * @param jrprint
	 * @throws IOException
	 * @throws JRException void
	 */
	public void toPdf(OutputStream outStream, JasperPrint jrprint) throws IOException, JRException;

	/**
	 *
	 * @param outStream
	 * @param jrprint
	 * @param setting
	 * @throws JRException void
	 */
	public void toXls(OutputStream outStream, JasperPrint jrprint, Map<JRExporterParameter, Object> setting)
			throws JRException;
	/**
	 *
	 * @param warehouseId
	 * @param productTypeId
	 * @param productId
	 * @return List<Object[]>
	 */
	public List<RealStockRO> findRealStock(Long warehouseId, List<Serializable> TypeIds);

}
