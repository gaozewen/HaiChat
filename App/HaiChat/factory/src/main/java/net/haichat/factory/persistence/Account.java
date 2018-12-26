package net.haichat.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.haichat.factory.Factory;
import net.haichat.factory.model.api.account.AccountRespModel;
import net.haichat.factory.model.db.User;
import net.haichat.factory.model.db.User_Table;

/**
 * 账户相关 持久化类
 */
public class Account {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID"; // sp 存储时的 key
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    private static String pushId;  // 设备的推送id (设备号)
    private static boolean isBind; // PushId 是否已经绑定到了服务器
    private static String token;   // Token (登录状态下)
    private static String userId;  // UserId (登录状态下)
    private static String account; // Phone (登录状态下)

    /**
     * 返回当前账号是否登录
     *
     * @return True 已登录
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(token) && !TextUtils.isEmpty(userId);
    }

    /**
     * 是否完善用户信息
     *
     * @return
     */
    public static boolean isPerfectUserInfo() {
        if(isLogin()){
            User self = getLoginInfo();
            return !TextUtils.isEmpty(self.getDesc())
                    && !TextUtils.isEmpty(self.getPortrait())
                    && self.getSex() != 0;
        }
        return false;
    }

    /**
     * 登录账户 和 PushId 是否已经绑定到 服务器
     *
     * @return True 已绑定
     */
    public static boolean isBind() {
        return isBind;
    }

    /**
     * 设置 绑定 状态
     */
    public static void setIsBind(boolean isBind) {
        Account.isBind = isBind;
        Account.saveDataToSp(Factory.app());
    }

    /**
     * 获取推送 Id
     *
     * @return
     */
    public static String getPushId() {
        return pushId;
    }

    /**
     * 设置并存储设备的id
     *
     * @param pushId 设备的推送id
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.saveDataToSp(Factory.app());
    }

    /**
     * 存储数据到 XML 文件，进行 数据持久化
     */
    private static void saveDataToSp(Context context) {
        // 获取数据持久化 sp，主要存储 应用的 配置信息
        SharedPreferences sp = context.getSharedPreferences(
                Account.class.getName(),
                Context.MODE_PRIVATE
        );

        sp.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply(); // apply 异步操作 commit 同步操作
    }

    /**
     * 从 Sp 中 加载 应用配置信息(PushId)
     *
     * @param context
     */
    public static void loadDataFromSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                Account.class.getName(),
                Context.MODE_PRIVATE
        );

        Account.pushId = sp.getString(KEY_PUSH_ID, "");
        Account.isBind = sp.getBoolean(KEY_IS_BIND, false);
        Account.token = sp.getString(KEY_TOKEN, "");
        Account.userId = sp.getString(KEY_USER_ID, "");
        Account.account = sp.getString(KEY_ACCOUNT, "");

    }

    /**
     * 登录 登录信息(个人信息) 到持久化的 XML 中
     *
     * @param model AccountRespModel
     */
    public static void syncLoginInfo(AccountRespModel model) {
        // 存储用户的 token,userId,方便从数据库中查询我的信息
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        saveDataToSp(Factory.app());
    }


    /**
     * 从 数据库中 获取 登录信息(是通过 DBFlow Save 的)
     * @return User
     */
    public static User getLoginInfo() {
        // 如果为 null 返回一个 new 的 User ,否则从数据库查询
        return TextUtils.isEmpty(userId) ? new User() : SQLite
                .select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }

    /**
     * 获取当前 登录 的 token
     * @return String
     */
    public static String getToken(){
        return token;
    }

}
