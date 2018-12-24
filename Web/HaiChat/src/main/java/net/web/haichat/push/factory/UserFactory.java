package net.web.haichat.push.factory;

import com.google.common.base.Strings;
import net.web.haichat.push.bean.db.User;
import net.web.haichat.push.utils.Hib;
import net.web.haichat.push.utils.TextUtil;

import java.util.List;
import java.util.UUID;

/**
 * 用户相关 业务逻辑
 */
public class UserFactory {

    /**
     * 用户注册
     * 注册的操作需要写入数据库，并返回数据库中的 User 信息
     *
     * @param account  账户
     * @param password 密码
     * @param name     用户名
     * @return User
     */
    public static User register(String account, String password, String name) {
        // 创建用户
        User user = createUser(account, password, name);
        // 用户创建成功，刷新 token
        if (user != null) user = refreshToken(user);

        return user;
    }

    /**
     * 用户登录
     * 使用账户 和 密码进行登录
     *
     * @param account  用户手机号
     * @param password 密码
     * @return
     */
    public static User login(String account, String password) {
        String queryAccount = account.trim();
        String queryPassword = encodePassword(password);
        // 查询用户是否存在
        User user = Hib.execute(session -> (User) session
                .createQuery("from User where phone=:account and password=:password")
                .setParameter("account", queryAccount)
                .setParameter("password", queryPassword)
                .uniqueResult());
        // 用户存在 刷新 token
        if (user != null) user = refreshToken(user);
        return user;
    }

    /**
     * 为 当前用户 绑定 pushId
     *
     * @param user   当前用户
     * @param pushId 设备id
     * @return
     */
    public static User bindPushId(User user, String pushId) {
        // 查询 这个设备 是否 绑定(登录) 了其他 用户
        // 取消 原始 绑定，避免推送混乱
        // 查询的列表不能包括自己
        Hib.execute(session -> {
            List<User> userList = (List<User>)session
                    .createQuery("from User where pushId=:pushId and id!=:userId")
                    .setParameter("pushId", pushId)
                    .setParameter("userId", user.getId())
                    .list();

            for (User u : userList) { // 解绑
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
            return null;
        });

        // 已绑定过 则 无需再次绑定
        if(pushId.equals(user.getPushId())) return user;

        // 若当前账户绑定了 其他 pushId(设备id),
        // 为了实现单点登录，需要让 之前的 设备推出账户
        // 方案：为 之前的 设备 推送一条 推出指令
        if(!Strings.isNullOrEmpty(user.getPushId())){
            // todo: 为原来设备 推送一条 退出指令
        }

        // 更新 新的 设备id
        user.setPushId(pushId);
        return update(user);
    }

    /**
     * 更新用户信息 到 数据库
     * @param user User
     * @return User
     */
    public static User update(User user){
        return Hib.execute(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    // 通过 Phone 找到 User
    public static User findByPhone(String phone) {
        return Hib.execute(session -> (User) session
                .createQuery("from User where phone =:phone")
                .setParameter("phone", phone)
                .uniqueResult());
    }

    // 通过 Name 找到 User
    public static User findByName(String name) {
        return Hib.execute(session -> (User) session
                .createQuery("from User where name =:name")
                .setParameter("name", name)
                .uniqueResult());
    }

    // 通过 Token 找到 User
    // 只能自己使用，查询的是个人信息，非他人信息
    public static User findByToken(String token) {
        return Hib.execute(session -> (User) session
                .createQuery("from User where token =:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    /**
     * 对密码进行加密操作
     *
     * @param password 明文
     * @return String 密文
     */
    private static String encodePassword(String password) {
        // 进行 MD5 非对称加密，加盐会更安全，盐也要存储
        password = TextUtil.getMD5(password.trim());
        // 再进行 一次 对称 的 Base64 加密，当然可以采取加盐的方案
        return TextUtil.encodeBase64(password);
    }

    /**
     * 注册部分新建用户逻辑
     *
     * @param account  手机号
     * @param password 密码(明文)
     * @param name     用户名
     * @return User
     */
    private static User createUser(String account, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setPassword(encodePassword(password)); // 将密码转换成密文
        user.setPhone(account.trim()); // 去除账户中的首尾空格 账户就是手机号
        // 数据库存储
        return Hib.execute(session -> (User) session.save(user));
    }

    /**
     * 更新 token 用于
     *
     * @param user User
     * @return User
     */
    private static User refreshToken(User user) {
        String newToken = UUID.randomUUID().toString(); // 使用 随机的 UUID 值 充当 token
        newToken = TextUtil.encodeBase64(newToken); // 进行 Base64 格式化
        user.setToken(newToken); // 为 token 赋予新值
        return update(user);
    }


}
