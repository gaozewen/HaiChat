package net.web.haichat.push.bean.api.account;

import com.google.gson.annotations.Expose;
import net.web.haichat.push.bean.card.UserCard;
import net.web.haichat.push.bean.db.User;

/**
 * 账户部分 返回的 Model
 */
public class AccountRespModel {

    @Expose
    private UserCard user;  // 用户基本信息
    @Expose
    private String account; // 当前登录的账号
    @Expose
    private String token;   // 当前登录获取的 token，可以通过 Token 获取用户的 所有信息
    @Expose
    private boolean isBind; // 标识 是否 已经绑定到了 设备 PushId

    public AccountRespModel(User user){
        // 默认 未绑定 PushId
        this(user,false);
    }

    public AccountRespModel(User user,boolean isBind){
        this.user = new UserCard(user);
        this.account = user.getPhone();
        this.token = user.getToken();
        this.isBind = isBind;
    }

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
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
