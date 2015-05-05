package cn.ITHong.hibernate.base;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.junit.Test;




import cn.ITHong.hibernate.base.optimize.fetch.oneTomany.Classes;
import cn.ITHong.hibernate.base.optimize.fetch.oneTomany.Student;
import cn.ITHong.hibernate.base.util.HibernateUtils;
/**
 * 抓取策略测试
 * 在hibernate 中 由一个对象怎么样查询关联对象
 * 怎么对关联对象发出SQL 语句 这就是抓取策略
 * */
public class FetchTest extends HibernateUtils {
	static{
		url = "cn/ITHong/hibernate/base/optimize/fetch/oneTomany/hibernate.cfg.xml";
		
	}
	/**
	 * n+1的问题 因为 学生有几条 + 1条班级
	 * 这种查询方式 是效率最低的
	 * 在Classes.hbm.xml
	 * <set name="students"  cascade="save-update" inverse="false" lazy="true">
	 * */
	 
	public void test() {
		Session session = sessionFactory.openSession();
		List<Classes> cList  = session.createQuery("from Classes").list();
		for(Classes classes:cList){
			//便利所有的班级
			Set<Student> students = classes.getStudents();
			//便利每一个班级的所有学生
			for(Student student:students){
				System.out.println(student.getSname());
			}
		}
		session.close();
	}
	/**
	 * 获取一些班级
	 * 在Classes.hbm.xml
	 * <set name="students"  cascade="save-update" inverse="false" lazy="true">
	 * Hibernate: select classes0_.CID as CID0_, classes0_.CNAME as CNAME0_, classes0_.DESCRIPTION as DESCRIPT3_0_ from CLASSES classes0_ where CID in (1 , 2 , 3)
	   Hibernate: select students0_.CID as CID0_1_, students0_.SID as SID1_, students0_.SID as SID1_0_, students0_.SNAME as SNAME1_0_, students0_.DESCRIPTION as DESCRIPT3_1_0_, students0_.CID as CID1_0_ from STUDENT students0_ where students0_.CID=?
	   Hibernate: select students0_.CID as CID0_1_, students0_.SID as SID1_, students0_.SID as SID1_0_, students0_.SNAME as SNAME1_0_, students0_.DESCRIPTION as DESCRIPT3_1_0_, students0_.CID as CID1_0_ from STUDENT students0_ where students0_.CID=?
	   Hibernate: select students0_.CID as CID0_1_, students0_.SID as SID1_, students0_.SID as SID1_0_, students0_.SNAME as SNAME1_0_, students0_.DESCRIPTION as DESCRIPT3_1_0_, students0_.CID as CID1_0_ from STUDENT students0_ where students0_.CID=?
	 * 
	 * */
	 
	public void test1() {
		Session session = sessionFactory.openSession();
		List<Classes> cList  = session.createQuery("from Classes where CID in (1,2,3)").list();
		for(Classes classes:cList){
			//便利所有的班级
			Set<Student> students = classes.getStudents();
			//便利每一个班级的所有学生
			for(Student student:students){
				System.out.println(student.getSname());
			}
		}
		session.close();
	}
	//------------------------------以上用于没有设置 配置文件的fetch属性
	//------------------------------以下是设置了fetch属性，用于对比
	/**
	 * 获取一些班级
	 * 在Classes.hbm.xml
	 * <set name="students"  cascade="save-update" inverse="false" lazy="true" fetch="subselect">
	 *	Hibernate: 
	    select
	        classes0_.CID as CID0_,
	        classes0_.CNAME as CNAME0_,
	        classes0_.DESCRIPTION as DESCRIPT3_0_ 
	    from
	        CLASSES classes0_ 
	    where
	        CID in (
	            1 , 2 , 3
	        )
		Hibernate: 
	    select
	        students0_.CID as CID0_1_,
	        students0_.SID as SID1_,
	        students0_.SID as SID1_0_,
	        students0_.SNAME as SNAME1_0_,
	        students0_.DESCRIPTION as DESCRIPT3_1_0_,
	        students0_.CID as CID1_0_ 
	    from
	        STUDENT students0_ 
	    where
	        students0_.CID in (
	            select
	                classes0_.CID 
	            from
	                CLASSES classes0_ 
	            where
	                CID in (
	                    1 , 2 , 3
	                )
	        )
	 * 效果比较结论：
	 * 对比没有设置fetch属性
	 * SQL 在fetch属性设置为 subselect后 数量就只有两条
	 * 但是 具有子查询SQL 使用fetch="join" 是不起作用的
	 * */
	 
	public void test3() {
		Session session = sessionFactory.openSession();
		List<Classes> cList  = session.createQuery("from Classes where CID in (1,2,3)").list();
		for(Classes classes:cList){
			//便利所有的班级
			Set<Student> students = classes.getStudents();
			//便利每一个班级的所有学生
			for(Student student:students){
				System.out.println(student.getSname());
			}
		}
		session.close();
	}
	/**
	 * 在Classes.hbm.xml中
	 *  <set name="students"  cascade="save-update" inverse="false" lazy="true" fetch="join">
		Hibernate: 
	    select
	        classes0_.CID as CID0_1_,
	        classes0_.CNAME as CNAME0_1_,
	        classes0_.DESCRIPTION as DESCRIPT3_0_1_,
	        students1_.CID as CID0_3_,
	        students1_.SID as SID3_,
	        students1_.SID as SID1_0_,
	        students1_.SNAME as SNAME1_0_,
	        students1_.DESCRIPTION as DESCRIPT3_1_0_,
	        students1_.CID as CID1_0_ 
	    from
	        CLASSES classes0_ 
	    left outer join
	        STUDENT students1_ 
	            on classes0_.CID=students1_.CID 
	    where
	        classes0_.CID=?
	 * */
	 
	public void test4() {
		Session session = sessionFactory.openSession();
		Classes classes = (Classes) session.get(Classes.class, 1L);
		Set<Student> students = classes.getStudents();
		for(Student student:students){
			System.out.println(student.getSname()+":"+classes.getCid()+"班");
		}
		session.close();
	}

}
