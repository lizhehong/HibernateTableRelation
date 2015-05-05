package cn.ITHong.hibernate.base.session;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.optimize.session.oneTomany.Classes;
import cn.ITHong.hibernate.base.util.HibernateUtils;

public class SessionTest extends HibernateUtils{

	static{
		url="cn/ITHong/hibernate/base/optimize/session/oneTomany/hibernate.cfg.xml";
	}
	
	/**
	 * 	当前线程的session
	 * 	org.hibernate.HibernateException: No CurrentSessionContext configured!
	 *	解决方案 	第一步
	 *	必须在hibernate.cfg.xml中配置
	 *	<property name="current_session_context_class">thread</property>
	 *	但是配置了还是会有下列异常
	 *	org.hibernate.HibernateException: get is not valid without active transaction
	 * 	解决方案	第二步
	 * 	CRUD操作必须在事务的环境下运行
	 * 	session和事务绑定在一起了 所以必须加入 事务
	 * 	但是又出现异常
	 * 	org.hibernate.SessionException: Session was already closed
	 * 	解决方案	第三步
	 * 	不要写 session.close();
	 * 
	 * 总结：session和transaction绑定在一起了 
	 * */
	 
	public void testGetCurrentSession(){
		Session session = sessionFactory.getCurrentSession();		
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes) session.get(Classes.class, 1L);
		transaction.commit();
		//session.close();
	}
}
