package net.haichat.factory.presenter;

import android.support.annotation.StringRes;

/**
 * MVP 模式中 的 基本契约
 */
public interface BaseContract {

    interface View<T extends Presenter> {
        // 公共的：显示一个字符串错误
        void showError(@StringRes int str);

        // 公共的：显示进度条
        void showLoading();

        // 支持设置一个 Presenter
        void setPresenter(T presenter);
    }

    interface Presenter{
        // 共用的：开始触发
        void start();

        // 共用的：销毁触发
        void destroy();
    }

}
