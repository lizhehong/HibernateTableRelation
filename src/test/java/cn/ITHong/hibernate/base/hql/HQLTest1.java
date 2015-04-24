package cn.ITHong.hibernate.base.hql;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.hql.oneTomany.Classes;
import cn.ITHong.hibernate.base.util.HibernateUtils;
public class HQLTest1 extends HibernateUtils{
	static{
		url="cn/ITHong/hibernate/base/hql/oneTomany/hibernate.cfg.xml";
	}
	/**
	 * 需要Classes里面有带构造函数 且与SQL中 带的参数顺序一致
	 * */
	@Test
	public void test(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		List<Classes> cList = session.createQuery("select new cn.ITHong.hibernate.base.hql.oneTomany.Classes(cid,cname) from Classes").list();
		System.out.println(cList);//[[Ljava.lang.Object;@6c779568, [Ljava.lang.Object;@f381794, [Ljava.lang.Object;@2cdd0d4b]
		for(Classes classes:cList){
			System.out.println(classes);//无输出
		}
		transaction.commit();
	}
	/**
	 * 通过自定义标签查询
	 *  需要Classes里面有带构造函数 且与SQL中 带的参数顺序一致
	 * */
	@Test
	public void test_2(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("select new cn.ITHong.hibernate.base.hql.oneTomany.Classes(cid,cname) from Classes where cid=:cid");
		query.setLong("cid", 1L);
		Classes classes = (Classes) query.uniqueResult();
		System.out.println(classes);
		transaction.commit();
	}
	/**
	 * 通过通配符查询
	 *  需要Classes里面有带构造函数 且与SQL中 带的参数顺序一致
	 * */
	@Test
	public void test_3(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("select new cn.ITHong.hibernate.base.hql.oneTomany.Classes(cid,cname) from Classes where cid=?");
		query.setLong(0, 1L);
		Classes classes = (Classes) query.uniqueResult();
		System.out.println(classes);
		transaction.commit();
	}
	/**
	 * 子查询 
	 * */
	@Test
	public void test_4(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		List<Classes> classesList = session.createQuery("from Classes where cid in (select cid from Classes where cid in(1,2,3))").list();

		System.out.println(classesList);
		transaction.commit();
	}


}





		