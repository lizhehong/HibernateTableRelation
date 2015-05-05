package cn.ITHong.hibernate.base.hql;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.hql.oneTomany.Classes;
import cn.ITHong.hibernate.base.hql.oneTomany.Student;
import cn.ITHong.hibernate.base.util.HibernateUtils;

/**
 * 一对多 等值连接 内连接  迫切内连接 左外连接 迫切左外连接
 * */
public class OneToManyTest extends HibernateUtils {
	static {
		url = "cn/ITHong/hibernate/base/hql/oneTomany/hibernate.cfg.xml";
	}

	/**
	 * 等值连接测试 cList里面是Object[]
	 * 下列的情况 的产生 所以等值连接在hibernate很少用
	 * */
	 
	public void queryClasses_Student_EQ() {
		Session session = sessionFactory.getCurrentSession();
		Student student = null;
		Classes classes = null;
		Transaction transaction = session.beginTransaction();
		List<Object[]> cList = session.createQuery(
				"from Classes c,Student s where c.cid=s.classes.cid").list();
		for (Object[] objs : cList) {
			System.out.println(objs);
			for (Object obj : objs) {
				if (obj.getClass().equals(Student.class)){
					student = (Student) obj;
					System.out.println(student.getSid()+":"+"student");
				}
				else if (obj.getClass().equals(Classes.class)){
					classes = (Classes) obj;
					System.out.println(classes.getCid()+":"+"classes");
				}
					
			}
		}
		transaction.commit();
	}
	/**
	 * 内连接	与等值连接的效果一样
	 * 以下的是内连接	记得标准的SQL内连接是需要加on条件的
	 * 但是HQL中内连接是不需要加入条件语句的
	 * 但是内连接所等到的觉得 cList是Object[]
	 * */
	 
	public void queryClasses_Student_INNER() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		List<Classes> cList = session.createQuery(
				"from Classes c inner join c.students ").list();
		
		transaction.commit();

	}
	/**
	 * 迫切内连接 记住并不是抓取策略
	 * 为了解决内连接和等值连接的返回Object[]问题
	 * 所有有了
	 * 迫切内连接
	 * 它的返回结果就是以对象的形式的
	 * */
	 
	public void queryClasses_Student_INNER_Fetch() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		List<Classes> cList = session.createQuery(
				"from Classes c inner join fetch c.students ").list();
		
		transaction.commit();

	}
	/**
	 * 左外连接也是返回数组结果
	 * */
	 
	public void queryClasses_Student_Left_Outer_Join() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		List<Classes> cList = session.createQuery(
				" from Classes c left outer join  c.students  ").list();
		
		transaction.commit();

	}
	/**
	 * 迫切左外连接也是返回对象结果
	 * 记住返回list里面的是Classes
	 * */
	 
	public void queryClasses_Student_Left_Outer_Join_Fetch() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		List<Classes> cList = session.createQuery(
				" from Classes c left outer join fetch c.students  ").list();	
		transaction.commit();

	}
	/**
	 * 迫切左外连接也是返回对象结果
	 * 记住返回list里面是Student
	 * */
	 
	public void queryClasses_Student_Left_Outer_Join_Fetch_1() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		List<Student> cList = session.createQuery(
				" from Student s left outer join fetch s.classes ").list();	
		transaction.commit();

	}
	
}
