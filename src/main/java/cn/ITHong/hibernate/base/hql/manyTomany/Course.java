package cn.ITHong.hibernate.base.hql.manyTomany;

import java.io.Serializable;
import java.util.Set;

public class Course implements Serializable {
	private Long cid;
	private String cname;
	private String desciprtion;
	private Set<Student> students;

	public Course() {

	}

	public Course(String cname, String desciprtion) {
		this.cname = cname;
		this.desciprtion = desciprtion;
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

	public String getDesciprtion() {
		return desciprtion;
	}

	public void setDesciprtion(String desciprtion) {
		this.desciprtion = desciprtion;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

}
