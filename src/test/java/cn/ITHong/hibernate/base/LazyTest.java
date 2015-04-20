package cn.ITHong.hibernate.base;

import static org.junit.Assert.*;

import java.util.Set;

import org.hibernate.Session;
import org.junit.Test;

import cn.ITHong.hibernate.base.optimize.oneTomany.Classes;
import cn.ITHong.hibernate.base.optimize.oneTomany.Student;
import cn.ITHong.hibernate.base.util.HibernateUtils;

public class LazyTest extends HibernateUtils {
	static {
		url = "cn/ITHong/hibernate/base/optimize/oneTomany/hibernate.cfg.xml";
	}

	/**
	 * 测试作用 类的懒加载 除了session.get 还有 session.load 懒加载
	 * 1、利用session.load方法可以产生代理对象
	 * 2、在session.load方法执行的时候并不发出sql语句 
	 * 3、在得到其一般属性的时候发出sql语句(请注意哦！！！！！)
	 * 4、只针对一般属性有效，针对标识符属性是无效的 5、默认情况就是懒加载
	 * */
	@Test
	public void test() {
		Session session = sessionFactory.openSession();
		Classes classes = (Classes) session.load(Classes.class, 1L);

		System.out.println(classes);// 发出sql语句
		// System.out.println(classes.getCid());//不发出sql语句额
		session.close();
	}

	/**
	 * 请对比test()
	 * */
	@Test
	public void test1() {
		Session session = sessionFactory.openSession();
		Classes classes = (Classes) session.get(Classes.class, 1L);

		System.out.println(classes.getCid());// 发出sql语句
		session.close();
	}

	/**
	 * 如果在session.close();后发出 System.out.println(classes.getCname()); 则会报告这个错误
	 * org.hibernate.LazyInitializationException: could not initialize proxy -
	 * no Session
	 * */
	@Test
	public void test3() {
		Session session = sessionFactory.openSession();
		Classes classes = (Classes) session.load(Classes.class, 1L);

		session.close();
		// System.out.println(classes.getCid());//还是可以得到的
		System.out.println(classes.getCname());
	}

	/**
	 * 测试条件： <set name="students" cascade="save-update" inverse="false"
	 * lazy="true"> 集合延时加载 得出结论：load 执行后 只有当 要得到集合时才发sql 且 要便利之前
	 * */
	@Test
	public void test4() {
		Session session = sessionFactory.openSession();
		Classes classes = (Classes) session.load(Classes.class, 1L);

		// Hibernate: select classes0_.CID as CID0_0_, classes0_.CNAME as
		// CNAME0_0_, classes0_.DESCRIPTION as DESCRIPT3_0_0_ from CLASSES
		// classes0_ where classes0_.CID=?
		Set<Student> students = classes.getStudents();

		for (Student student : students) {// Hibernate: select students0_.CID as
											// CID0_1_, students0_.SID as SID1_,
											// students0_.SID as SID1_0_,
											// students0_.SNAME as SNAME1_0_,
											// students0_.DESCRIPTION as
											// DESCRIPT3_1_0_, students0_.CID as
											// CID1_0_ from STUDENT students0_
											// where students0_.CID=?
			System.out.println(student.getSname());
		}
		session.close();
	}

	/**
	 * 测试条件： <set name="students" cascade="save-update" inverse="false"
	 * lazy="true"> 比较test4() 得出结论 ：get一执行 就sql 且 持久化 一get集合 就发出sql
	 * */
	@Test
	public void test5() {
		Session session = sessionFactory.openSession();

		Classes classes = (Classes) session.get(Classes.class, 1L);// Hibernate:
																	// select
																	// classes0_.CID
																	// as
																	// CID0_0_,
																	// classes0_.CNAME
																	// as
																	// CNAME0_0_,
																	// classes0_.DESCRIPTION
																	// as
																	// DESCRIPT3_0_0_
																	// from
																	// CLASSES
																	// classes0_
																	// where
																	// classes0_.CID=?

		Set<Student> students = classes.getStudents();
		for (Student student : students) {// Hibernate: select students0_.CID as
											// CID0_1_, students0_.SID as SID1_,
											// students0_.SID as SID1_0_,
											// students0_.SNAME as SNAME1_0_,
											// students0_.DESCRIPTION as
											// DESCRIPT3_1_0_, students0_.CID as
											// CID1_0_ from STUDENT students0_
											// where students0_.CID=?
			System.out.println(student.getSname());
		}
		session.close();
	}

	/**
	 * 测试条件： <set name="students" cascade="save-update" inverse="false"
	 * lazy="false"> 比较test4() 得出结论 ：只有到get集合的时候 才一起发出sql语句
	 * */
	@Test
	public void test6() {
		Session session = sessionFactory.openSession();

		Classes classes = (Classes) session.load(Classes.class, 1L);

		Set<Student> students = classes.getStudents();
		// --------------------sql在这里发出
		// Hibernate: select classes0_.CID as CID0_0_, classes0_.CNAME as
		// CNAME0_0_, classes0_.DESCRIPTION as DESCRIPT3_0_0_ from CLASSES
		// classes0_ where classes0_.CID=?
		// Hibernate: select students0_.CID as CID0_1_, students0_.SID as SID1_,
		// students0_.SID as SID1_0_, students0_.SNAME as SNAME1_0_,
		// students0_.DESCRIPTION as DESCRIPT3_1_0_, students0_.CID as CID1_0_
		// from STUDENT students0_ where students0_.CID=?
		// --------------------sql发出结束
		for (Student student : students) {
			System.out.println(student.getSname());
		}
		session.close();
	}

	/**
	 * 测试条件： <set name="students" cascade="save-update" inverse="false"
	 * lazy="false"> 比较test4() 得出结论 ：只有到get的时候 才一起发出sql语句
	 * */
	@Test
	public void test7() {
		Session session = sessionFactory.openSession();

		Classes classes = (Classes) session.get(Classes.class, 1L);
		// --------------------sql在这里发出
		// Hibernate: select classes0_.CID as CID0_0_, classes0_.CNAME as
		// CNAME0_0_, classes0_.DESCRIPTION as DESCRIPT3_0_0_ from CLASSES
		// classes0_ where classes0_.CID=?
		// Hibernate: select students0_.CID as CID0_1_, students0_.SID as SID1_,
		// students0_.SID as SID1_0_, students0_.SNAME as SNAME1_0_,
		// students0_.DESCRIPTION as DESCRIPT3_1_0_, students0_.CID as CID1_0_
		// from STUDENT students0_ where students0_.CID=?
		// --------------------sql发出结束
		Set<Student> students = classes.getStudents();

		for (Student student : students) {
			System.out.println(student.getSname());
		}
		session.close();
	}

	/**
	 * 测试条件：<set name="students" cascade="save-update" inverse="false"
	 * lazy="extra"> 
	 * 
	 * */
	@Test
	public void test8() {
		Session session = sessionFactory.openSession();

		Classes classes = (Classes) session.load(Classes.class, 1L);

		Set<Student> students = classes.getStudents();// Hibernate: select
														// classes0_.CID as
														// CID0_0_,
														// classes0_.CNAME as
														// CNAME0_0_,
														// classes0_.DESCRIPTION
														// as DESCRIPT3_0_0_
														// from CLASSES
														// classes0_ where
														// classes0_.CID=?

		for (Student student : students) {// Hibernate: select students0_.CID as
											// CID0_1_, students0_.SID as SID1_,
											// students0_.SID as SID1_0_,
											// students0_.SNAME as SNAME1_0_,
											// students0_.DESCRIPTION as
											// DESCRIPT3_1_0_, students0_.CID as
											// CID1_0_ from STUDENT students0_
											// where students0_.CID=?

			System.out.println(student.getSname());
		}
		session.close();
	}
	/**
	 * 测试条件：<set name="students" cascade="save-update" inverse="false"
	 * lazy="extra"> 
	 * 进一步的懒加载
	 * 打印东东为：
	 * Hibernate: select classes0_.CID as CID0_0_, classes0_.CNAME as CNAME0_0_, classes0_.DESCRIPTION as DESCRIPT3_0_0_ from CLASSES classes0_ where classes0_.CID=?
	 * Hibernate: select count(SID) from STUDENT where CID =?
	 * 3
	 * */
	@Test
	public void test9() {
		Session session = sessionFactory.openSession();

		Classes classes = (Classes) session.load(Classes.class, 1L);

		Set<Student> students = classes.getStudents();

		System.out.println(students.size());
		session.close();
	}
	/**
	 * 测试条件：<set name="students" cascade="save-update" inverse="false"
	 * lazy="true"> 
	 * 懒加载
	 * 打印东东为：
	 *Hibernate: select classes0_.CID as CID0_0_, classes0_.CNAME as CNAME0_0_, classes0_.DESCRIPTION as DESCRIPT3_0_0_ from CLASSES classes0_ where classes0_.CID=?
	 *Hibernate: select students0_.CID as CID0_1_, students0_.SID as SID1_, students0_.SID as SID1_0_, students0_.SNAME as SNAME1_0_, students0_.DESCRIPTION as DESCRIPT3_1_0_, students0_.CID as CID1_0_ from STUDENT students0_ where students0_.CID=?
	 *3
	 * */
	@Test
	public void test10() {
		Session session = sessionFactory.openSession();

		Classes classes = (Classes) session.load(Classes.class, 1L);

		Set<Student> students = classes.getStudents();

		System.out.println(students.size());
		session.close();
	}
	/**
	 * 测试条件：<set name="students" cascade="save-update" inverse="false"
	 * lazy="false"> 
	 * 不进行懒加载
	 * 打印东东为：
	 *Hibernate: select classes0_.CID as CID0_0_, classes0_.CNAME as CNAME0_0_, classes0_.DESCRIPTION as DESCRIPT3_0_0_ from CLASSES classes0_ where classes0_.CID=?
	 *Hibernate: select students0_.CID as CID0_1_, students0_.SID as SID1_, students0_.SID as SID1_0_, students0_.SNAME as SNAME1_0_, students0_.DESCRIPTION as DESCRIPT3_1_0_, students0_.CID as CID1_0_ from STUDENT students0_ where students0_.CID=?
	 *3
	 * */
	@Test
	public void test11() {
		Session session = sessionFactory.openSession();

		Classes classes = (Classes) session.load(Classes.class, 1L);

		Set<Student> students = classes.getStudents();

		System.out.println(students.size());
		session.close();
	}
	
}
