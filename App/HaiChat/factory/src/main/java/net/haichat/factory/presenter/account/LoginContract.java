package net.haichat.factory.presenter.account;

import net.haichat.factory.presenter.BaseContract;

/**
 * MVP 登录契约
 */
public interface LoginContract {

    interface View extends BaseContract.View<Presenter>{
        // 登录成功
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        // 发起一个登录
        void login(String phone, String password);
    }
}
