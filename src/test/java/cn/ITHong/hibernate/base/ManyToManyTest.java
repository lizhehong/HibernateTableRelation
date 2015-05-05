package cn.ITHong.hibernate.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.manyTomany.Course;
import cn.ITHong.hibernate.base.manyTomany.Student;
import cn.ITHong.hibernate.base.util.HibernateUtils;

/**
 * 1、新建一个课程 2、新建一个学生 3、新建课程的同时新建学生 级联 4、已经存在一个课程，新建一个学生，建立课程和学生之间的关系
 * 5、已经存在一个学生，新建一个课程，建立课程和学生之间的关系 6、已经存在一个课程，已经存在一个学生，建立关联
 * 7、把已经存在的一些学生加入到已经存在的一个课程中 8、把一个学生加入到一些课程中 9、把多个学生加入到多个课程中
 * 10、把一个已经存在的学生从一个已经的课程中移除 11、把一些学生从一个已经存在的课程中移除 12、把一个学生从一个课程转向到另外一个课程 13、删除课程
 * 14、删除学生
 * 
 * @author Think
 *
 */

public class ManyToManyTest extends HibernateUtils {
	static {
		url = "cn/ITHong/hibernate/base/manyTomany/hibernate.cfg.xml";
	}

	/**
	 * 1、新建一个课程
	 * */

	 
	public void testSave1() {

		Course course = new Course("数学", "普通班");
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);
		HibernateUtils.Create(courses);
	}

	/**
	 * 2、新建一个学生
	 * */
	 
	public void testSave() {
		Student student = new Student("李哲弘", "打篮球");
		List<Student> students = new ArrayList<Student>();
		students.add(student);
		HibernateUtils.Create(students);

	}

	/**
	 * 3、新建课程的同时新建学生 级联
	 * */
	 
	public void testSave2() {
		Student student = new Student("李哲弘", "打篮球");
		Course course = new Course("数学", "普通班");
		List<Course> CourseList = new ArrayList<Course>();
		Set<Student> studentsSet = new HashSet<Student>();
		studentsSet.add(student);
		course.setStudents(studentsSet);
		CourseList.add(course);
		HibernateUtils.Create(CourseList);
	}

	/**
	 * 4、已经存在一个课程，新建一个学生，建立课程和学生之间的关系
	 * */
	 
	public void testSave3() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Course course = (Course) session.get(Course.class, 3L);
		System.out.println(course);
		Student student = new Student("红红", "大人");
		Set<Student> students = course.getStudents();
		students.add(student);

		course.setStudents(students);
		session.save(course);

		transaction.commit();
		session.close();

	}
	/**
	 * 把一个学生加入到一些课程中 
	 * */
	  
	public void testSave4(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		//请用类名哦
		List<Course> courses = session.createQuery("from Course where CID in(1,2,3)").list();
		Student student = (Student) session.get(Student.class, 1L);
		student.getCourses().addAll(courses);
		transaction.commit();
		session.close();
	}
	/**
	 * 把一个学生从一个课程转移到另一个课程
	 * 先删除 后添加
	 * */
	 
	public void testSave5(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		List<Course> courses = session.createQuery("from Course where CID in (1,4)").list();
		Student student =  (Student) session.get(Student.class, 1L);
		//学生离开课程1
		student.getCourses().remove(courses.get(0));
		//学生前往课程4
		student.getCourses().add(courses.get(1));
		transaction.commit();
		session.close();
	
	
	
	}
}
