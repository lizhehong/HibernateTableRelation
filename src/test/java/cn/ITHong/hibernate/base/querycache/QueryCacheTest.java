package cn.ITHong.hibernate.base.querycache;

import static org.junit.Assert.*;

import java.util.List;



import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;



import cn.ITHong.hibernate.base.optimize.querycache.oneTomany.Classes;
import cn.ITHong.hibernate.base.util.HibernateUtils;
import cn.ITHong.hibernate.itest.ICacheTest;

public class QueryCacheTest extends HibernateUtils {
	static{
		url="cn/ITHong/hibernate/base/optimize/querycache/oneTomany/hibernate.cfg.xml";
	}
	/**
	 * 只有一次SQL
	 * 说明第二次的classesList = query.list();不会发出SQL语句，有了查询缓存了
	 * Hibernate: 
		    select
		        classes0_.CID as CID0_,
		        classes0_.CNAME as CNAME0_,
		        classes0_.DESCRIPTION as DESCRIPT3_0_ 
		    from
		        CLASSES classes0_
	 * */
	 
	public void testQuery(){
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery("from Classes");
		query.setCacheable(true);//classes里的所有数据要完查询缓存存放了
		List<Classes> classesList = query.list();
		
		query = session.createQuery("from Classes");
		query.setCacheable(true);//开启优先查询 查询缓存
		classesList = query.list();
		
		
		session.close();
	}
	/**
	 * 两条SQL 似乎没有查询缓存
	 * */
	 
	public void testQuery_1(){
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery("from Classes ");
		query.setCacheable(true);//classes里的所有数据要完查询缓存存放了
		List<Classes> classesList = query.list();
		
		query = session.createQuery("from Classes where CID in (1,2)");
		query.setCacheable(true);//开启优先查询 查询缓存
		classesList = query.list();
		
		
		session.close();
	}
	/**
	 * 两条SQL语句
	 * */
	 
	public void testQuery_2(){
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery("from Classes ");
		query.setCacheable(true);//classes里的所有数据要完查询缓存存放了
		List<Classes> classesList = query.list();
		
		query = session.createQuery("select cname from Classes ");
		query.setCacheable(true);//开启优先查询 查询缓存
		classesList = query.list();
		
		
		session.close();
	}
	/**
	 * 两条SQL语句，没有查询缓存
	 * */
	 
	public void testQuery_3(){
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery("from Classes ");
		query.setCacheable(true);//classes里的所有数据要完查询缓存存放了
		List<Classes> classesList = query.list();
		
		query = session.createQuery(" from Classes where CID=1");
		query.setCacheable(true);//开启优先查询 查询缓存
		classesList = query.list();
		
		
		session.close();
	}
}
