package net.haichat.factory.presenter.account;

import android.text.TextUtils;

import net.haichat.common.Common;
import net.haichat.factory.R;
import net.haichat.factory.data.DataSource;
import net.haichat.factory.data.helper.AccountHelper;
import net.haichat.factory.model.api.account.RegisterModel;
import net.haichat.factory.model.db.User;
import net.haichat.factory.presenter.BasePresenter;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

public class RegisterPresenter
        extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.CallBack<User> {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String password, String name) {
        start(); // 开始执行业务，界面显示 Loading 并 锁定界面(禁止操作)

        RegisterContract.View view = getView(); // View 接口

        if (!checkMobile(phone)) { // 手机号不合法提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (password.length() < 6) { // 密码必须大于 6 位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else if (TextUtils.isEmpty(name)) { // 昵称 不能为空
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else { // 进行网络请求,并设置回调
            AccountHelper.register(new RegisterModel(phone, password, name),this);
        }
    }

    /**
     * 检查手机号码是否合法
     *
     * @param phone 手机号码
     * @return 合法为 True
     */
    @Override
    public boolean checkMobile(String phone) {
        // 手机号不为空，并且满足格式
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoadedSuccess(User user) { // 注册成功
        final RegisterContract.View view = getView();
        if(view == null) return; // 界面已经销毁

        // 此时是从网络会使你刚回来的，并不保证处于 主线程 状态
        Run.onUiAsync(new Action() { // 强制执行在主线程中
            @Override
            public void call() {
                view.registerSuccess(); // 注册成功，跳转到 MainActivity
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) { // 注册失败
        final RegisterContract.View view = getView();
        if(view == null) return; // 界面已经销毁

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes); // 提示注册失败
            }
        });
    }
}
