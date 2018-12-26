package net.web.haichat.push.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Hibernate 工具类
 */
public class Hib {
    // 全局SessionFactory
    private static SessionFactory sessionFactory;

    static {
        // 静态初始化sessionFactory
        init();
    }

    private static void init() {
        // 从hibernate.cfg.xml文件初始化
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            // build 一个sessionFactory
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // 错误则打印输出，并销毁
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * 获取全局的SessionFactory
     *
     * @return SessionFactory
     */
    public static SessionFactory sessionFactory() {
        return sessionFactory;
    }

    /**
     * 从SessionFactory中得到一个Session会话
     *
     * @return Session
     */
    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 关闭sessionFactory
     */
    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    // 用于 用户实际操作的 一个接口
    // 具有 返回值 T
    public interface Handler<T> {
        T execute(Session session);
    }

    // 简化 Session 操作的 工具方法
    // 具有一个返回值
    public static<T> T execute(Handler<T> handler){
        // 重开一个 session
        Session session = sessionFactory.openSession();
        // 开启事务
        Transaction transaction = session.beginTransaction();

        T t = null;
        try{
            // 调用 传递进来的接口，并调用接口的方法把 session 传递进去
            t = handler.execute(session);
            transaction.commit(); // 提交
        }catch (Exception e){
            e.printStackTrace();
            try{
                transaction.rollback(); // 回滚
            }catch (RuntimeException re){
                re.printStackTrace();
            }
        }finally {
            // 无论成功失败，都要关闭 Session
            session.close();
        }
        return t;
    }
}
