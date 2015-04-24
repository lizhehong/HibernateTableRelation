package cn.ITHong.hibernate.base.hql.manyTomany;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.util.HibernateUtils;

public class ManyToManyTest extends HibernateUtils{
	static{
		url="cn/ITHong/hibernate/base/hql/manyTomany/hibernate.cfg.xml";
	}
	/**
	 * 多对多
	 * */
	@Test
	public void testQueryCourse_Student() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		String HQL = "from Student s inner join fetch s.courses c";
		List<Student> sList =  session.createQuery(HQL).list();
		transaction.commit();
	}
	/**
	 * 一对多	结合	多对多
	 * 最好从 中心点出发 也就是这个中心点 能直接联系到 其他部分的
	 * 这个中心点作为返回值 最直接 以至于能减少 遍历次数
	 * 
	 * 
	 * 对于重复的元素 可以用 消除重复元素的方法
	 * 消除重复前	第一个HQL 1,2,1,3,1	个数=5 		第二个HQL 1,1,1,2,3	个数=5
	 * 消除重复后	第一个HQL	1,3,2		个数=3 		第二个HQL 1,3,2,		个数=3
	 * */
	@Test
	public void testQUeryClasses_Student_Course(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		String HQL = "";
		//HQL = "from Student s  inner join fetch s.classes cs inner join fetch s.courses c";//1,2,1,1个数=4 不加去重
		HQL = "from Student s left outer join fetch s.classes cs left outer join fetch s.courses c";//1,1,1,2,3,个数=5 不加去重
		List<Student> cList = session.createQuery(HQL).list();
		//去重开始
		Set<Student> sSet = new HashSet<Student>(cList);
		cList = new ArrayList<Student>(sSet);
		//去重结束	第一个HQL	1,2,个数=2 第二个HQL 1,3,2,个数=3
		for(Student student:cList){
			System.out.println(student.getSid());
		}
		System.out.println("个数="+cList.size());
		transaction.commit();
	}
}
