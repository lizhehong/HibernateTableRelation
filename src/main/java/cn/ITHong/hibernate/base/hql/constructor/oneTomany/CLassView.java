package cn.ITHong.hibernate.base.hql.constructor.oneTomany;

public class CLassView {
	private String cname;
	private String sname;
	
	public CLassView() {
		super();
	}
	/**
	 * 为了适应HQL查询指定列名而创建构造器
	 * */
	public CLassView(String cname, String sname) {
		super();
		this.cname = cname;
		this.sname = sname;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	
}
