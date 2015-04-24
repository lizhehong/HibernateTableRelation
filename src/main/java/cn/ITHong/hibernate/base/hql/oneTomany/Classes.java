package cn.ITHong.hibernate.base.hql.oneTomany;

import java.util.Set;

public class Classes {
	private Long cid;
	private String cname;
	private String description;
	/**
	 * 为什么要有这个构造器
	 * 2015/4/23 17:11	当时的想法就是为了适应Hernate中HQL 使用 new 的方法来使得
	 * 返回list的值是对象 而不是Object[]
	 * */
	public Classes(long cid,String cname) {
		super();
		this.cname = cname;
		this.cid = cid;
	}

	public Classes() {
		super();
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

	private Set<Student> students;

	@Override
	public String toString() {
		return "Classes [cid=" + cid + ", cname=" + cname + ", description="
				+ description + ", students=" + students + "]";
	}
	
}
