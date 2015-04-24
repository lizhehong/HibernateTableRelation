package cn.ITHong.hibernate.base.hql;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.hql.constructor.oneTomany.Classes;
import cn.ITHong.hibernate.base.util.HibernateUtils;

/**
 * 带指定列名 查询 并返回对象的测试 也就是带select 查询
 * */
public class OneToManyByConstructorTest extends HibernateUtils {
	static {
		url = "cn/ITHong/hibernate/base/hql/oneTomany/hibernate.cfg.xml";
	}

	@Test
	public void queryClasses_Student_Select() {
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		String HQL = "select new cn.ITHong.hibernate.base.hql.constructor.oneTomany.CLassView (c.cname,s.sname) from Student s left outer join fetch s.classes c";
		List<Classes> cList = session.createQuery(HQL).list();
		transaction.commit();

	}

}

 