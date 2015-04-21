package cn.ITHong.hibernate.base.session.cache;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.optimize.session.cache.oneTomany.Classes;
import cn.ITHong.hibernate.base.optimize.session.cache.oneTomany.Student;
import cn.ITHong.hibernate.base.util.HibernateUtils;
import cn.ITHong.hibernate.itest.ICacheTest;
/**
 * session缓存研究
 * 总结：hibernate 的操作
 * 切记 有缓存的存在
 * 如果对其CRUD 首先是对缓存的CRUD 再次才是数据库的
 * */
public class SessionCacheTest extends HibernateUtils implements ICacheTest{
	static{
		url="cn/ITHong/hibernate/base/optimize/session/cache/oneTomany/copy/hibernate.cfg.xml";
	}
	
	/* (非 Javadoc) 
	* <p>Title: testGet</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testGet() 
	*/
	@Test
	public void testGet() {
		Session session  = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes) session.get(Classes.class, 1L);
		classes = (Classes) session.get(Classes.class, 1L);
		transaction.commit();
	}
	
	/* (非 Javadoc) 
	* <p>Title: testLoad</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testLoad() 
	*/
	@Test
	public void testLoad() {
		Session session  = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes) session.load(Classes.class, 1L);
		classes.getCname();
		classes = (Classes) session.load(Classes.class, 1L);
		classes.getCname();
		transaction.commit();
	}
	/* (非 Javadoc) 
	* <p>Title: testSave</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testSave() 
	*/
	@Test
	public void testSave() {
		Session session  = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = new Classes();
		classes.setDescription("ssss");
		classes.setCname("aass");
		session.save(classes);
		classes = (Classes) session.get(Classes.class, classes.getCid());
		transaction.commit();
	}
	/* (非 Javadoc) 
	* <p>Title: testUpdate</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testUpdate() 
	*/
	@Test
	public void testUpdate() {
		Session session  = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes) session.get(Classes.class, 1L);
		//清除指定持久化对象 清空了在一级缓存的指定对象
		session.evict(classes);
		session.update(classes);//把classes对象放进一级缓存中
		session.get(Classes.class, classes.getCid());
		transaction.commit();
	}
	/* (非 Javadoc) 
	* <p>Title: testClear</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testClear() 
	*/
	@Test
	public void testClear() {
		Session session  = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes) session.get(Classes.class, 1L);
		//把session一级缓存的所有对象清除
		session.clear();
		session.get(Classes.class, classes.getCid());
		transaction.commit();
	}
	/* (非 Javadoc) 
	* <p>Title: testClearTest</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testClearTest() 
	*/
	@Test
	public void testClearTest() {
		Session session  = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Classes classes = (Classes) session.get(Classes.class, 1L);
		
		Classes classes2 = new Classes();
		classes2.setCid(1L);
		classes2.setCname("dasdaf");
		session.update(classes2);
		transaction.commit();
	}
	/* (非 Javadoc) 
	* <p>Title: testClearTest_2</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testClearTest_2() 
	*/
	@Test
	public void testClearTest_2() {
		Session session  = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Classes classes = (Classes) session.get(Classes.class, 1L);
		
		Classes classes2 = new Classes();
		classes2.setCid(1L);
		classes2.setCname("dasdaf");
		session.evict(classes);
		session.update(classes2);
		transaction.commit();
	}
	/* (非 Javadoc) 
	* <p>Title: testRefresh</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testRefresh() 
	*/
	@Test
	public void testRefresh(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Classes classes = (Classes) session.get(Classes.class, 1L);
		classes.setCname("uuuuu");
		//把CID为1的classes从数据库刷新到缓存中，也就是同步
		session.refresh(classes);
		transaction.commit();
	}
	/* (非 Javadoc) 
	* <p>Title: testFlush</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testFlush() 
	*/
	@Test
	public void testFlush(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Classes classes = (Classes) session.get(Classes.class, 1L);
		classes.setCname("iiiiii");
		Set<Student>students = classes.getStudents();
		for(Student student:students){
			student.setDescription("班长被修改");
		}
		session.flush();
		transaction.commit();
	}
	/* (非 Javadoc) 
	* <p>Title: testSaveBatch</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testSaveBatch() 
	*/
	@Test
	public void testSaveBatch(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		long time = System.currentTimeMillis();
		for(int i=9;i<1000;i++ ){
			Classes classes = new Classes();
			classes.setCname(UUID.randomUUID().toString());
			classes.setDescription("百万条数据测试");
			session.save(classes);
		}
		transaction.commit();
	
	}
	/* (非 Javadoc) 
	* <p>Title: testSaveBatch_2</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testSaveBatch_2() 
	*/
	@Test
	public void testSaveBatch_2(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		long time = System.currentTimeMillis();
		for(int i=9;i<1000;i++ ){
			Classes classes = new Classes();
			classes.setCname(UUID.randomUUID().toString());
			classes.setDescription("百万条数据测试");
			session.save(classes);
			if(i%50==0){
				session.flush();
				session.clear();
			}
		}
		transaction.commit();
	
	}
	/* (非 Javadoc) 
	* <p>Title: testFlush_3</p> 
	* <p>Description: </p>  
	* @see cn.ITHong.hibernate.base.ICacheTest#testFlush_3() 
	*/
	@Test
	public void testFlush_3(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		long time = System.currentTimeMillis();
		
			Classes classes = new Classes();
			classes.setCname(UUID.randomUUID().toString());
			classes.setDescription("百万条数据测试");
			session.save(classes);
			session.flush();
			//如果有清空内存 则classes不是持久化对象 则get方法 会发出sql语句	缓存为空	
			//如果没有清空内存 则classes是持久化对象 则get方法不会发出SQL语句	缓存不为空 成立
			classes = (Classes) session.get(Classes.class, classes.getCid());
	
		transaction.commit();
	
	}
	
}
