package cn.ITHong.hibernate.base;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.Test;

import cn.ITHong.hibernate.base.util.HibernateUtils;

public class OneToOne extends HibernateUtils{
	
	static{
		url="cn/ITHong/hibernate/base/oneToone/hibernate.cfg.xml";
	}
	/**
	 * 尝试创建表
	 * */
	 
	public void test() {
		Session session = sessionFactory.openSession();
		
		session.close();
	}

}
