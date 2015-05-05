package cn.ITHong.hibernate.base;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.oneTomany.Classes;
import cn.ITHong.hibernate.base.oneTomany.Student;
import cn.ITHong.hibernate.base.util.HibernateUtils;




/**
 * 比较
 * testSaveStudent_Cascade_Classes_Save() 	4条语句  一对多中  多的一端 效率高
 * testSaveStudent_Cascade_Classes_Save_1() 5条语句 一对多中  少的一端 效率低
 * 
 * 
 * 
 * 
 * */
public class OneToManyTest extends HibernateUtils {

	static{
		url = "cn/ITHong/hibernate/base/oneTomany/hibernate.cfg.xml";
		
	}
	
	
	 
	/**
	 * 即 学生保存 班级 
	 * 也可以
	 * 班级保存学生
	 * 以下没有发出update
	 Hibernate: select max(SID) from STUDENT
	 Hibernate: select max(CID) from CLASSES
	 Hibernate: insert into CLASSES (CNAME, DESCRIPTION, CID) values (?, ?, ?)
	 Hibernate: insert into STUDENT (SNAME, DESCRIPTION, CID, SID) values (?, ?, ?, ?)
	 */
	public void testSaveStudent_Cascade_Classes_Save() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		Student student = new Student();
		student.setSname("banzhang");
		student.setDescription("haoren");
		
		Classes classes = new Classes();
		classes.setCname("老毕亲子班");
		classes.setDescription("都是老毕的亲戚");
		student.setClasses(classes);
		
		
		session.save(student);
		
		
		transaction.commit();
		session.close();
	
	}
	/**
	 * 情况1：
	 *  <set name="students"  cascade="all" inverse="false">
	 *  则：
	 *  对应的student 有CID
	 *  情况2：
	 *  <set name="students"  cascade="all" inverse="true">
	 *  则：
	 *  对应的Student CID 为空
	 *  以下是 情况1：
			Hibernate: select student0_.SID as SID1_0_, student0_.SNAME as SNAME1_0_, student0_.DESCRIPTION as DESCRIPT3_1_0_, student0_.CID as CID1_0_ from STUDENT student0_ where student0_.SID=?
			Hibernate: select max(CID) from CLASSES
			Hibernate: insert into CLASSES (CNAME, DESCRIPTION, CID) values (?, ?, ?)
			Hibernate: update STUDENT set SNAME=?, DESCRIPTION=?, CID=? where SID=?
			Hibernate: update STUDENT set CID=? where SID=?
	 * */
	 
	public void testSaveStudent_Cascade_Classes_Save_1() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		Student student = new Student();
		student.setSname("banzhang5X");
		student.setDescription("haoren");
		
		Classes classes = new Classes();
		classes.setCname("老毕亲子班5X");
		classes.setDescription("都是老毕的亲戚");
		
		Set<Student> students = new HashSet<Student>();
		students.add(student);
		classes.setStudents(students);
		
		
		session.save(classes);
		
		
		transaction.commit();
		session.close();
	
	}
	/**
	 * save-update 只删除了学生 班级没哟影响
	 * 在Student.hbm.xml中
	 * <many-to-one class="cn.ITHong.hibernate.Base.Classes"
			name="classes" cascade="save-update" column="CID">
		</many-to-one>
	 * */
	 
	public void testSaveStudent_Cascade_Classes_DeleteStudent() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		Student student = (Student) session.get(Student.class, 1L);
		session.delete(student);
		
		transaction.commit();
		session.close();
	
	}
	/**
	 * save-update 删除班级的同时 删除了学生
	 * 在Classes.hbm.xml中
	 *  <set name="students"  cascade="all">
            <key>
                <column name="CID" />
            </key>
            <one-to-many class="cn.ITHong.hibernate.Base.Student"/>
        </set>
	 * */
	 
	public void testSaveStudent_Cascade_Classes_DeleteClasses() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		Classes classes = (Classes) session.get(Classes.class, 1L);
		session.delete(classes);
		
		transaction.commit();
		session.close();
	
	}
	/**
	 * 删除对应班级的2 的学生
	 * 维护关系
	 * <set name="students"  cascade="all" inverse="true">
            <key>
                <column name="CID" />
            </key>
            <one-to-many class="cn.ITHong.hibernate.Base.Student"/>
        </set>
     *删除有有缺点 发出 根据SID 查student  而不是 根据 CID  删除student 所以语句臃肿 缺点 sql不可控 
	 * */
	 
	public void testDeleteClasses_cascade() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		Classes classes = (Classes) session.get(Classes.class, 2L);
		session.delete(classes);
		
		transaction.commit();
		session.close();
	
	}
	/**
	 * CLasses.hbm.xml
	 *  <set name="students"  cascade="all" inverse="false">
	 *  Student.hbm.xml
	 *  <many-to-one class="cn.ITHong.hibernate.Base.Classes"
			name="classes" cascade="save-update" column="CID">
		</many-to-one>
		出现 
	*Hibernate: select classes0_.CID as CID0_0_, classes0_.CNAME as CNAME0_0_, classes0_.DESCRIPTION as DESCRIPT3_0_0_ from CLASSES classes0_ where classes0_.CID=?
	 Hibernate: select students0_.CID as CID0_1_, students0_.SID as SID1_, students0_.SID as SID1_0_, students0_.SNAME as SNAME1_0_, students0_.DESCRIPTION as DESCRIPT3_1_0_, students0_.CID as CID1_0_ from STUDENT students0_ where students0_.CID=?
	 Hibernate: update STUDENT set CID=null where CID=?
	 Hibernate: delete from STUDENT where SID=?
	 Hibernate: delete from CLASSES where CID=?	
	 *  */
	 
	public void testDeleteClasses_allINClasses() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		Classes classes = (Classes) session.get(Classes.class, 1L);
		session.delete(classes);
		
		transaction.commit();
		session.close();
	
	}
	/**
	 * 个人觉得这种设置 符合 班级 -学生 模型
	 * CLasses.hbm.xml
	 *  <set name="students"  cascade="save-update" inverse="false">
	 *  Student.hbm.xml
	 *  <many-to-one class="cn.ITHong.hibernate.Base.Classes"
			name="classes" cascade="save-update" column="CID">
		</many-to-one>
		出现 
		Hibernate: select classes0_.CID as CID0_0_, classes0_.CNAME as CNAME0_0_, classes0_.DESCRIPTION as DESCRIPT3_0_0_ from CLASSES classes0_ where classes0_.CID=?
		Hibernate: update STUDENT set CID=null where CID=?
		Hibernate: delete from CLASSES where CID=?
		删除班级 保留学生 删除学生的班级号
	 *  */
	 
	public void testDeleteClasses_SaveUpdateINClasses() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		Classes classes = (Classes) session.get(Classes.class, 1L);
		session.delete(classes);
		
		transaction.commit();
		session.close();
		System.out.println("个人觉得这种设置 符合 班级 -学生 模型");
	
	}
	 
	public void testDeleteClasses_Student(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
	
		Classes classes = (Classes) session.get(Classes.class, 1L);
		Set<Student> students = classes.getStudents();
		classes.setStudents(null);//解除关系--->不然会出现下面错误额
		//org.hibernate.ObjectDeletedException:
		//deleted object would be re-saved by cascade (remove deleted object from associations): 
		//[cn.ITHong.hibernate.base.oneTomany.Student#3]
		//必须让student和classes 解除关系
		for(Student student:students){
			session.delete(student);
		}
		
		
		transaction.commit();
		session.close();
	}
}
