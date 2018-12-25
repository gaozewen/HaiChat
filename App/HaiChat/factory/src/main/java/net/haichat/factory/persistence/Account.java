package net.haichat.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import net.haichat.factory.Factory;

/**
 * 账户相关 持久化类
 */
public class Account {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID"; // sp 存储时的 key
    private static final String KEY_IS_BIND = "KEY_IS_BIND";

    private static String pushId; // 设备的推送id (设备号)
    private static boolean isBind; // PushId 是否已经绑定到了服务器


    /**
     * 返回当前账号是否登录
     *
     * @return True 已登录
     */
    public static boolean isLogin() {
        // TODO: 判断当前 用户 是否登录
        return false;
    }

    /**
     * 登录账户 和 PushId 是否已经绑定到 服务器
     * @return True 已绑定
     */
    public static boolean isBind() {
        return isBind;
    }

    /**
     * 设置 绑定 状态
     */
    public static void setIsBind(boolean isBind){
        Account.isBind = isBind;
        Account.saveDataToSp(Factory.app());
    }

    /**
     * 获取推送 Id
     * @return
     */
    public static String getPushId() {
        return pushId;
    }

    /**
     * 设置并存储设备的id
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
                .putBoolean(KEY_IS_BIND,isBind)
                .apply(); // apply 异步操作 commit 同步操作
    }

    /**
     * 从 Sp 中 加载 应用配置信息(PushId)
     * @param context
     */
    public static void loadDataFromSp(Context context){
        SharedPreferences sp = context.getSharedPreferences(
                Account.class.getName(),
                Context.MODE_PRIVATE
        );

        Account.pushId = sp.getString(KEY_PUSH_ID,"gzw");
        Account.isBind = sp.getBoolean(KEY_IS_BIND,false);
    }

}
