package net.haichat.factory.presenter.account;

import android.text.TextUtils;

import net.haichat.common.Common;
import net.haichat.factory.data.helper.AccountHelper;
import net.haichat.factory.model.api.RegisterModel;
import net.haichat.factory.presenter.BasePresenter;

import java.util.regex.Pattern;

public class RegisterPresenter
        extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String password, String name) {
        start(); // 开始执行业务，界面显示 Loading 并 锁定界面(禁止操作)

        // 手机号不合法提示
        if (!checkMobile(phone)) {
            // 提示
        } else if (password.length() < 6) {
            // 密码必须大于 6 位
        } else if (TextUtils.isEmpty(name)) {
            // 昵称 不能为空
        }else {
            // 进行网络请求
            AccountHelper.register(new RegisterModel(phone, password, name));
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

}
