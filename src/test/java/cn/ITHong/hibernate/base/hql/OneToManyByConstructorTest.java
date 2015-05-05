package cn.ITHong.hibernate.base.hql;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.hql.constructor.oneTomany.CLassView;
import cn.ITHong.hibernate.base.hql.constructor.oneTomany.Classes;
import cn.ITHong.hibernate.base.hql.constructor.oneTomany.Student;
import cn.ITHong.hibernate.base.util.HibernateUtils;

/**
 * 带指定列名 查询 并返回对象的测试 也就是带select 查询
 * */
public class OneToManyByConstructorTest extends HibernateUtils {
	static {
		url = "cn/ITHong/hibernate/base/hql/oneTomany/hibernate.cfg.xml";
	}
	/**
	 * 1.fech目标：
	 * 在Classes含有Student或者在Studeng中含有Classes
	 * 为了使其返回结构好	Classes带有Student 或者 Student含有Classes
	 * 2.加构造函数目标：
	 * 也是为了结构好 但是构造函数的结构肯定不是带fetch的结构
	 * 为了一个JavaBean把所有的属性整理到一起
	 * 
	 * 由上所得
	 * 	1.fetch目标,2加构造函数目标,两个目标的期望返回结构是违背的
	 * 总结：
	 * 	1,2只能选择其中一种
	 * 
	 * */
	 
	public void queryClasses_Student_Select_Error() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		String HQL = "select new cn.ITHong.hibernate.base.hql.constructor.oneTomany.CLassView (c.cname,s.sname) "
				+ "from Student s left outer join fetch s.classes c";
		List<Classes> cList = session.createQuery(HQL).list();
		transaction.commit();

	}
	/**
	 * 返回指定的列名数据到JavaBean中 也就是ClassesView
	 * */
	 
	public void queryClasses_Student_Select_New() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		String HQL = "select new cn.ITHong.hibernate.base.hql.constructor.oneTomany.CLassView (c.cname,s.sname) "
				+ "from Student s left outer join  s.classes c";
		List<CLassView> cList = session.createQuery(HQL).list();
		transaction.commit();

	}
	/**
	 * 返回有包含结构的
	 * */
	 
	public void queryClasses_Student_Select_Fecth() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		String HQL = "from Student s left outer join fetch s.classes c";
		List<Student> cList = session.createQuery(HQL).list();
		transaction.commit();

	}

}
