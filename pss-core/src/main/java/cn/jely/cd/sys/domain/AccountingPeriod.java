package cn.jely.cd.sys.domain;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;


/**
 * 会计期间实体,year和month留给以后用于财务期间用,结帐期间只需要开始时间和结束时间即可
 * @author 周义礼
 * @version 2013-4-16 下午5:26:49
 *
 */
public class AccountingPeriod implements java.io.Serializable {
	public static final int UNUSE=0;
	public static final int INUSE=1;
	public static final int USED=2;
	public static final String INITPERIOD="initPeriod";
	// Fields

	private Long id;
	/**@Fields name:结帐期间名称*/
	private String name;
	/**@Fields year:会计期间年度*/
	private String year;
	/**@Fields month:会计期间月度*/
	private String month;
	/**@Fields start:开始日期*/
	private Date start;
	/**@Fields end:结束日期*/
	private Date end;
	/**@Fields state:暂不使用 会计期间状态0:未进入,1:当前,2:已结帐*/
	private int state;
	// Constructors

	/** default constructor */
	public AccountingPeriod() {
	}

	
	public AccountingPeriod(String name, Date start, Date end) {
		this.name = name;
		this.start = start;
		this.end = end;
	}


	/** full constructor */
	public AccountingPeriod(String year, String month, Date start, Date end) {
		this.year = year;
		this.month = month;
		this.start = start;
		this.end = end;
	}

	// Property accessors

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getStart() {
		return this.start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getEnd() {
		return this.end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}