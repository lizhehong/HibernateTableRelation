package cn.ITHong.hibernate.itest;

import org.junit.Test;

public interface ICacheTest {

	/**
	 * session.get方法把数据存放到一级缓存中了
	 * */
	public abstract void testGet();

	/**
	 * session.loa方法把数据存放到一级缓存中了
	 * 因为只发出一次SQL语句
	 * */
	public abstract void testLoad();

	/**
	 * 在transaction.commit();前的
	 * classes = (Classes) session.get(Classes.class, classes.getCid());
	 * 是不发出SQL语句的 所以说明 这个语句 直接在内存中拿数据 不用去数据库拿
	 * 
	 * session.save方法把数据保存在一级缓存中
	 * */
	public abstract void testSave();

	/**
	 * session.update(classes);//把classes对象放进一级缓存中
	 * */
	public abstract void testUpdate();

	/**
	 * 肯定发出两次SQL语句
	 * */
	public abstract void testClear();

	/**
	 * 异常
	 * org.hibernate.NonUniqueObjectException: 
	 * a different object with the same identifier value was already associated with the session: 
	 * [cn.ITHong.hibernate.base.optimize.session.cache.oneTomany.copy.Classes#1]
	 * 因为两个持久化对象标识符相同
	 *
	 * */
	public abstract void testClearTest();

	/**
	 *解决testClearTest异常 但是 因为本来存在的数据1l 就会被
	 *新new的classes对象取代
	 *强调：
	 *		hibernate 持久化对象	不能存在同一样标识符
	 *		数据库	不能存在同一样的标识符	或联合主键
	 *		所以	不清除session缓存而执行的	操作时优先于数据库操作的
	 *		接着才是数据库操作
	 * */
	public abstract void testClearTest_2();

	/**
	 * 把数据库中的数据刷新到缓存中
	 * classes.setCname("uuuuu");无效果
	 * */
	public abstract void testRefresh();

	/**
	 * session.flush();
	 * 只管 save.update()	session.save()	session.delete()
	 * 不管 save.get	save.load
	 * 把缓存中的数据刷新到数据库中
	 * */
	public abstract void testFlush();

	/**
	 * 批量查询 (导致内存溢出)
	 * */
	public abstract void testSaveBatch();

	/**
	 * 批量查询 (防止内存溢出)
	 * */
	public abstract void testSaveBatch_2();

	/**
	 * 测试flush 是否清空缓存
	 * 实际测试 没有清空缓存
	 * */
	public abstract void testFlush_3();

}