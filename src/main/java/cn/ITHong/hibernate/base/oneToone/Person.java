package cn.ITHong.hibernate.base.oneToone;

import java.util.Set;

public class Person {
	private Long pid;
	private String pname;
	private String description;
	private Address address;
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "Person [pid=" + pid + ", pname=" + pname + ", description="
				+ description + ", address=" + address + "]";
	}


}
