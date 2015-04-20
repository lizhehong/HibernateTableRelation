package cn.ITHong.hibernate.base;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.ITHong.hibernate.base.optimize.session.cache.oneTomany.copy.Classes;
import cn.ITHong.hibernate.base.optimize.session.cache.oneTomany.copy.Student;
import cn.ITHong.hibernate.base.util.HibernateUtils;
/**
 * session缓存研究
 * 总结：hibernate 的操作
 * 切记 有缓存的存在
 * 如果对其CRUD 首先是对缓存的CRUD 再次才是数据库的
 * */
public class SessionCacheTest extends HibernateUtils{
	static{
		url="cn/ITHong/hibernate/base/optimize/session/cache/oneTomany/copy/hibernate.cfg.xml";
	}
	
	/**
	 * session.get方法把数据存放到一级缓存中了
	 * */
	@Test
	public void testGet() {
		Session session  = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Classes classes = (Classes) session.get(Classes.class, 1L);
		classes = (Classes) session.get(Classes.class, 1L);
		transaction.commit();
	}
	
	/**
	 * session.loa方法把数据存放到一级缓存中了
	 * 因为只发出一次SQL语句
	 * */
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
	/**
	 * 在transaction.commit();前的
	 * classes = (Classes) session.get(Classes.class, classes.getCid());
	 * 是不发出SQL语句的 所以说明 这个语句 直接在内存中拿数据 不用去数据库拿
	 * 
	 * session.save方法把数据保存在一级缓存中
	 * */
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
	/**
	 * session.update(classes);//把classes对象放进一级缓存中
	 * */
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
	/**
	 * 肯定发出两次SQL语句
	 * */
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
	/**
	 * 异常
	 * org.hibernate.NonUniqueObjectException: 
	 * a different object with the same identifier value was already associated with the session: 
	 * [cn.ITHong.hibernate.base.optimize.session.cache.oneTomany.copy.Classes#1]
	 * 因为两个持久化对象标识符相同
	 *
	 * */
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
	/**
	 *解决testClearTest异常 但是 因为本来存在的数据1l 就会被
	 *新new的classes对象取代
	 *强调：
	 *		hibernate 持久化对象	不能存在同一样标识符
	 *		数据库	不能存在同一样的标识符	或联合主键
	 *		所以	不清除session缓存而执行的	操作时优先于数据库操作的
	 *		接着才是数据库操作
	 * */
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
	/**
	 * 把数据库中的数据刷新到缓存中
	 * classes.setCname("uuuuu");无效果
	 * */
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
	/**
	 * session.flush();
	 * 只管 save.update()	session.save()	session.delete()
	 * 不管 save.get	save.load
	 * 把缓存中的数据刷新到数据库中
	 * */
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
	/**
	 * 批量查询 (导致内存溢出)
	 * */
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
	/**
	 * 批量查询 (防止内存溢出)
	 * */
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
	/**
	 * 测试flush 是否清空缓存
	 * 实际测试 没有清空缓存
	 * */
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
