package net.web.haichat.push.factory;

import com.google.common.base.Strings;
import net.web.haichat.push.bean.db.User;
import net.web.haichat.push.bean.db.UserFollow;
import net.web.haichat.push.utils.Hib;
import net.web.haichat.push.utils.TextUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户相关 业务逻辑
 */
public class UserFactory {

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

    // 通过 Id 找到 User
    public static User findById(String id) {
        return Hib.execute(session -> session.get(User.class, id));
    }

    /**
     * 对密码进行加密操作
     *
     * @param password 明文
     * @return String 密文
     */
    private static String encodePassword(String password) {
        // 进行 MD5 非对称加密，加盐会更安全，盐也要存储
        password = TextUtil.getMD5(password); // password 不能 trim() 空格也是字符 可作为密码
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
        return Hib.execute(session -> {
            session.save(user);
            return user;
        });
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
            List<User> userList = (List<User>) session
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
        if (pushId.equals(user.getPushId())) return user;

        // 若当前账户绑定了 其他 pushId(设备id),
        // 为了实现单点登录，需要让 之前的 设备推出账户
        // 方案：为 之前的 设备 推送一条 推出指令
        if (!Strings.isNullOrEmpty(user.getPushId())) {
            // todo: 为原来设备 推送一条 退出指令
        }

        // 更新 新的 设备id
        user.setPushId(pushId);
        return update(user);
    }

    /**
     * 更新用户信息 到 数据库
     *
     * @param user User
     * @return User
     */
    public static User update(User user) {
        return Hib.execute(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * 获取 我的 联系人列表
     *
     * @param self User
     * @return List<User>
     */
    public static List<User> contacts(User self) {
        return Hib.execute(session -> {
            // 重新加载一次用户信息 到 self 中，和当前的 session 绑定
            session.load(self, self.getId());
            // 获取我关注的人
            Set<UserFollow> list = self.getFollowing();
            // java8 流式 操作 (原来用 for 遍历赋值)
            return list.stream()
                    .map(UserFollow::getTarget)
                    .collect(Collectors.toList());
        });
    }

    /**
     * 关注 某人
     *
     * @param self   发起者
     * @param target 被关注人
     * @param alias  备注名
     * @return 被关注人的信息
     */
    public static User follow(User self, User target, String alias) {
        // 查询 2 者关系
        UserFollow userFollow = getRelationship(self, target);
        // 已关注，直接返回 被关注的人
        if (userFollow != null) return userFollow.getTarget();

        return Hib.execute(session -> {
            // 想要操作懒加载的数据，需要重新 load 一次绑定 session
            session.load(self, self.getId());
            session.load(target, target.getId());

            // 我关注 他的同时，让他 也自动关注我
            UserFollow meToHim = new UserFollow();
            meToHim.setOrigin(self);
            meToHim.setTarget(target);
            meToHim.setAlias(alias);

            UserFollow himToMe = new UserFollow();
            himToMe.setOrigin(target);
            himToMe.setTarget(self);

            // 保存到数据库
            session.save(meToHim);
            session.save(himToMe);

            return target;
        });
    }

    /**
     * 查询 2 这个关系( self 是否 已关注 target)
     *
     * @param self   发起者
     * @param target 被关注人
     * @return UserFollow
     */
    public static UserFollow getRelationship(User self, User target) {
        return Hib.execute(session -> (UserFollow) session
                .createQuery("from UserFollow where originId=:originId and targetId=:targetId")
                .setParameter("originId", self.getId())
                .setParameter("targetId", target.getId())
                .uniqueResult());
    }

    /**
     * 模糊搜索用户
     *
     * @param name 模糊查询，允许为空
     * @return
     */
    public static List<User> search(String name) {
        // name 忽略大小写，使用 like 模糊查询，头像 和 描述 必须完善才能被查询到
        return Hib.execute(session -> (List<User>) session
                .createQuery("from User where lower(name) like :name and portrait is not null and description is not null")
                .setParameter("name", "%" + (name == null ? "" : name) + "%")
                .setMaxResults(20) // 至多返回 20 条
                .list());
    }
}
