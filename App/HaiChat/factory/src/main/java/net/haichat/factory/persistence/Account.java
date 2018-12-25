package net.haichat.factory.persistence;

/**
 * 账户相关 持久化类
 */
public class Account {

    // 设备的推送id (设备号)
    private static String pushId = "test";

    public static String getPushId() {
        return pushId;
    }

    public static void setPushId(String pushId) {
        Account.pushId = pushId;
    }
}
