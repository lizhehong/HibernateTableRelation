package cn.ITHong.hibernate.base.hql;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.optimize.secondlevelcache.oneTomany.Classes;
import cn.ITHong.hibernate.base.util.HibernateUtils;
import cn.ITHong.hibernate.itest.ICacheTest;

public class HQLTest extends HibernateUtils {
	static{
		url="cn/ITHong/hibernate/base/optimize/secondlevelcache/oneTomany/hibernate.cfg.xml";
	}
	/**
	 * cList 返回的是Class
	 * */
	@Test
	public void test(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		List<Classes> cList = session.createQuery("from Classes").list();
		System.out.println(cList);//正常数据
		transaction.commit();
	}
	/**
	 * cList里面是Object[]
	 * */
	@Test
	public void test_1(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		List<Object[]> cList = session.createQuery("select cid,cname from Classes").list();
		System.out.println(cList);//[[Ljava.lang.Object;@6c779568, [Ljava.lang.Object;@f381794, [Ljava.lang.Object;@2cdd0d4b]
		for(Object[] classes:cList){
			System.out.println(classes);//无输出
		}
		transaction.commit();
	}
	/**
	 * cList里面是Object[] 迭代出相当于classes的object[]
	 * */
	@Test
	public void test_1_1(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		List<Classes> cList = session.createQuery("select cid,cname from Classes").list();
		System.out.println(cList);//[[Ljava.lang.Object;@6c779568, [Ljava.lang.Object;@f381794, [Ljava.lang.Object;@2cdd0d4b]
		for(Object classes:cList){
			System.out.println(classes);//[[Ljava.lang.Object;@6c779568, [Ljava.lang.Object;@f381794, [Ljava.lang.Object;@2cdd0d4b]
		}
		transaction.commit();
	}
	/**
	 * cList里面是Object[] 
	 * 第一次迭代是 代表 迭代存放属性的Object[]
	 * 第二次迭代是 代表 迭代出属性
	 * */
	@Test
	public void test_1_2(){
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		
		List<Object[]> cList = session.createQuery("select cid,cname from Classes").list();
		
		for(Object[] object:cList){
			for(Object obj:object){
				System.out.println(obj);//打印出所有需要额属性	obj里面的属性 类型 与存入的一样
			}
		}
		transaction.commit();
	}
}
