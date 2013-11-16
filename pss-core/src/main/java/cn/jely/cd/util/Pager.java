package cn.jely.cd.util;

import java.util.ArrayList;
import java.util.List;

public class Pager<T> {
	private int pageNo = 1;
	private int pageSize = 5;
	private long totalRows;
	private long totalPages;
	private List<T> rows = new ArrayList<T>();

	public Pager() {
	}

	public Pager(int pageNo, int pageSize, long totalRows) {
		this.totalRows = totalRows;
		this.pageNo = pageNo < 1 ? 1 : pageNo;
		this.pageSize = pageSize < 1 ? 1 : pageSize;
		this.totalPages = (totalRows + pageSize - 1) / pageSize;
	}

	/**
	 * <p>
	 * Title:如果只是为了将数据封装为已知的分页数据用此方法
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param totalRows
	 * @param totalPages
	 * @param rows
	 */
	public Pager(int pageNo, int pageSize, long totalRows, long totalPages, List<T> rows) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalRows = totalRows;
		this.totalPages = totalPages;
		this.rows = rows;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
