package cn.ITHong.hibernate.base.hql.manyTomany;

import java.io.Serializable;
import java.util.Set;

public class Classes implements Serializable{
	private Long cid;
	private String cname;
	private String description;
	private Set<Student> students;
	public Classes() {
		super();
	}

	public Classes(String cname, String description) {
		super();
		this.cname = cname;
		this.description = description;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}


	@Override
	public String toString() {
		return "Classes [cid=" + cid + ", cname=" + cname + ", description="
				+ description + ", students=" + students + "]";
	}
	
}
