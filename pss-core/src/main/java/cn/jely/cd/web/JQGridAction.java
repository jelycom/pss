package cn.jely.cd.web;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.ObjectQueryTool;
import cn.jely.cd.util.query.QueryGroup;

/**
 * @ClassName:JQGridAction
 * @Description:使用Jquery插件jqGrid对应的Action抽象
 * @author {周义礼}
 * @date 2012-11-9 上午9:43:36
 * 
 * @param <T>
 */
public abstract class JQGridAction<T> extends CRUDAction<T> {

	private static final long serialVersionUID = 1L;
	// _search true
	//
	// nd 1348736044111
	// page 1
	// rows 5
	// sidx id
	// sord asc
	/** 排序的方式(ASC/DESC) */
	private String sord;
	/** 排序的字段名称(根据前台设置的json名称传回来) */
	private String sidx;
	/** 取哪页的数据 */
	private int page;
	/** 每页记录数 */
	private int rows;
	// 当multiSearch为true时
	/**
	 * 查询的条件(字段名,操作,值都有),封装为JSON格式</br> filters
	 * {"groupOp":"AND","rules":[{"field":"id","op":"eq","data":"2"}]} *
	 */
	private String filters;
	// 当multiSearch为false时
	/** 单个查询的字段名 */
	private String searchField;
	/** 单个查询的操作(大于,小于,等于,Like....) */
	private String searchOper;
	/** 单个查询的值 */
	private String searchString;
	/** 返回数据时表示总页数 */
	private int total;// "totalPages", // json中代表页码总数的数据

	/** 返回数据时的总记录数 */
	private int records; // "totalRows", // json中代表数据行总数的数据

	protected ObjectQuery objectQuery = null;

	/**
	 * 解析filters字符串,转化为查询对象
	 * 
	 * @param filters
	 * @return
	 */
	protected ObjectQuery parseCondition() {
		objectQuery = new ObjectQuery();
		QueryGroup qg = null;
		if (StringUtils.isNotBlank(searchField) && StringUtils.isNotBlank(searchOper)) {
			qg = ObjectQueryTool.QueryGroupPaser.parseJqgridSingle(searchField, searchOper, searchString);
		} else {
			if ("-1".equals(filters)) {
				filters = "";
			}
			qg = ObjectQueryTool.QueryGroupPaser.parseJqgridJson(filters);
		}
		objectQuery.setQueryGroup(qg);
		if (StringUtils.isNotBlank(this.sidx)) {
			objectQuery.setOrderField(this.sidx);
		}
		if (StringUtils.isNotBlank(sord)) {
			objectQuery.setOrderType(sord);
		}
		if (this.page > 0 && this.rows > 0) {
			objectQuery.setPageNo(this.page);
			objectQuery.setPageSize(rows);
		}
//		objectQuery.setJqGridCondition(true);
		// logger.debug(objectQuery.getOrder());
		return objectQuery;
	}

	public void prepareListjson() {
		parseCondition();
	}

	public void setSord(String sord) {
		this.sord = sord;
		// objectQuery.setOrderType(sord);
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
		// objectQuery.setOrderField(sidx);
	}

	public void setPage(int page) {
		this.page = page;
		// objectQuery.setPageNo(page);
	}

	public void setRows(int rows) {
		this.rows = rows;
		// objectQuery.setPageSize(rows);
	}

	public void setFilters(String filters) {
		this.filters = filters;
		// if("-1".equals(filters)){
		// filters="";
		// }
		// logger.debug(filters);
		// parseFilters(filters);
		// objectQuery.setFilterString(filters);
		// logger.debug("setFitlers:"+objectQuery.getWhere());
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
		// objectQuery.setSearchField(searchField);
	}

	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
		// objectQuery.setSearchOper(searchOper);
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
		// objectQuery.setSearchString(searchString);
	}

	public int getTotal() {
		return total;
	}

	public int getRecords() {
		return records;
	}

	public ObjectQuery getObjectQuery() {
		return objectQuery;
	}
	
	
}
