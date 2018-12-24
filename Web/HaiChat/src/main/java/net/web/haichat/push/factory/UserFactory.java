package net.web.haichat.push.factory;

import net.web.haichat.push.bean.db.User;
import net.web.haichat.push.utils.Hib;
import net.web.haichat.push.utils.TextUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserFactory {

    /**
     * 用户注册
     * 注册的操作需要写入数据库，并返回数据库中的 User 信息
     * @param account 账户
     * @param password 密码
     * @param name 用户名
     * @return User
     */
    public static User register(String account,String password,String name){
        account = account.trim(); // 去除账户中的首尾空格
        password = encodePassword(password); // 将密码转换成密文

        User user = new User();

        user.setName(name);
        user.setPassword(password);
        user.setPhone(account); // 账户就是手机号

        // 进行数据库操作
        Session session = Hib.session();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(user);
            transaction.commit();
            return user;
        }catch (Exception e){
            transaction.rollback(); // 失败回滚
            return null;
        }
    }

    private static String encodePassword(String password){
        // 进行 MD5 非对称加密，加盐会更安全，盐也要存储
        password = TextUtil.getMD5(password.trim());
        // 再进行 一次 对称 的 Base64 加密，当然可以采取加盐的方案
        return TextUtil.encodeBase64(password);
    }

}
