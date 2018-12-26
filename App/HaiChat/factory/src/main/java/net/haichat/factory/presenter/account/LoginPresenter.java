package net.haichat.factory.presenter.account;

import android.text.TextUtils;

import net.haichat.factory.R;
import net.haichat.factory.data.ApiCallback;
import net.haichat.factory.data.helper.AccountHelper;
import net.haichat.factory.model.api.account.LoginModel;
import net.haichat.factory.model.db.User;
import net.haichat.factory.persistence.Account;
import net.haichat.factory.presenter.BasePresenter;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 登录界面的逻辑实现
 */
public class LoginPresenter
        extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, ApiCallback.Callback<User> {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    // 发起登录 请求
    @Override
    public void login(String phone, String password) {
        start(); // 界面 显示 Loading
        LoginContract.View view = getView(); // 获取 布局

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            LoginModel loginModel = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(loginModel,this);
        }
    }

    @Override
    public void onDataLoadedSuccess(User user) {
        final LoginContract.View view = getView();
        if(view == null) return; // 界面已经销毁

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess(); // LoginFragment 中 方法
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final LoginContract.View view = getView();
        if(view == null) return; // 界面已经销毁

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes); // 显示 失败 原因
            }
        });
    }
}
