package net.haichat.factory.model.api.account;

import net.haichat.factory.model.db.User;

public class AccountRespModel {

    private User user;  // 用户基本信息

    private String account; // 当前登录的账号

    private String token;   // 当前登录获取的 token，可以通过 Token 获取用户的 所有信息

    private boolean isBind; // 标识 是否 已经绑定到了 设备 PushId

    @Override
    public String toString() {
        return "AccountRespModel{" +
                "user=" + user +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind=" + isBind +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
