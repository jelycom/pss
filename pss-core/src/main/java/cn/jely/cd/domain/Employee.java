package cn.jely.cd.domain;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import cn.jely.cd.sys.domain.User;

/**
 * Employee entity. @author MyEclipse Persistence Tools
 */

public class Employee implements java.io.Serializable {

	// Fields

	/**long:serialVersionUID:*/
	private static final long serialVersionUID = 1L;
	private Long id;
	private Department department;
	private String name;
	private String py;
	/**Long:manager:主管主键*/
	private Long manager;
	/**Date:birthday:出生日期*/
	private Date birthday;
	/**Date:hireDate:入职日期*/
	private Date hireDate;
	/**String:job:工种*/
	private String job;
	/**Double:salary:工资*/
	private Double salary;
	/**Double:bonus:资金*/
	private Double bonus;
	private String mobilePhone;
	private String telePhone;
	private String address;
	private String nickName;
	private User user;
	/**Date:leaveDate:离职日期*/
	private Date leaveDate;
	private String memo;


	// Constructors

	/** default constructor */
	public Employee() {
	}

	/** minimal constructor */
	public Employee(String name) {
		this.name = name;
	}

	public Employee(Department department, String name) {
		this.department = department;
		this.name = name;
	}

	/** full constructor */
	public Employee(Department department, String name, String py, Long manager, Date birthday, Date hireDate,
			String job, Double salary, Double bonus, String mobilePhone, String telephone, String address,
			String nickName, String loginName, String password, Date leaveDate,  Date expireDate,String memo) {
		this.department = department;
		this.name = name;
		this.py = py;
		this.manager = manager;
		this.birthday = birthday;
		this.hireDate = hireDate;
		this.job = job;
		this.salary = salary;
		this.bonus = bonus;
		this.mobilePhone = mobilePhone;
		this.telePhone = telephone;
		this.address = address;
		this.nickName = nickName;
		this.leaveDate = leaveDate;
		this.memo = memo;
	}

	
	
	// Property accessors
	public Employee(long id) {
		this.id = id;
	}

	public Employee(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPy() {
		return this.py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public Long getManager() {
		return this.manager;
	}

	public void setManager(Long manager) {
		this.manager = manager;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getJob() {
		return this.job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Double getSalary() {
		return this.salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Double getBonus() {
		return this.bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@JSON(format = "yyyy-MM-dd")
	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	
}