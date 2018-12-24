package net.haichat.factory.presenter.account;

import net.haichat.factory.presenter.BaseContract;

/**
 * MVP 注册契约
 */
public interface RegisterContract {

    interface View extends BaseContract.View<Presenter>{
        // 注册成功
        void registerSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        // 发起一个注册
        void register(String phone, String password, String name);

        // 检查手机号是否正确
        boolean checkMobile(String phone);
    }

}
