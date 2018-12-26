package net.haichat.factory.presenter.user;

import net.haichat.factory.presenter.BaseContract;

/**
 * 更新用户信息的 基本契约
 */
public interface UpdateInfoContract  {


    interface View extends BaseContract.View<Presenter>{
        void updateSucceed(); // P 层处理成功后 刷新界面
    }

    interface Presenter extends BaseContract.Presenter {
        // P 层 处理更新逻辑
        void update(String portraitPath,boolean isMan,String desc);
    }
}
