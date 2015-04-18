package cn.ITHong.hibernate.base.optimize.oneTomany;
/**
 * 相对于Hibernate_hong_base改进了 
 * 加入
 * private Classes classes;
 * */

public class Student {

	private Long sid;
	private String sname;
	private Classes classes;

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
	}

	public Long getSid() {
		return sid;
	}
	
	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Student [sid=" + sid + ", sname=" + sname + ", classes="
				+ classes + ", description=" + description + "]";
	}

	private String description;
}
