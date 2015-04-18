package cn.ITHong.hibernate.base.oneToone;
/**
 * 相对于Hibernate_hong_base改进了 
 * 加入
 * private Classes classes;
 * */

public class Address {

	private Long aid;
	private String aname;
	private Person person;
	private String description;
	public Long getAid() {
		return aid;
	}
	public void setAid(Long aid) {
		this.aid = aid;
	}
	public String getAname() {
		return aname;
	}
	public void setAname(String aname) {
		this.aname = aname;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Address [aid=" + aid + ", aname=" + aname + ", person="
				+ person + ", description=" + description + "]";
	}
	
}
