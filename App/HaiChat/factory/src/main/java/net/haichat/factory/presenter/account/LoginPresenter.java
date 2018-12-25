package net.haichat.factory.presenter.account;

import net.haichat.factory.presenter.BasePresenter;

/**
 * 登录界面的逻辑实现
 */
public class LoginPresenter
        extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }
}
