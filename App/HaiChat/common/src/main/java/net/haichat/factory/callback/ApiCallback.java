package net.haichat.factory.callback;

import android.support.annotation.StringRes;

/**
 * Restful API 接口 请求结果回调
 */
public interface ApiCallback {

    // 同时包含了 成功 和 失败 的 回调接口
    interface Callback<T> extends SucceedCallback<T>,FailedCallback{

    }

    // 只关注 成功 的接口
    interface SucceedCallback<T>{
        // 数据加载成功
        void onDataLoadedSuccess(T t);
    }

    // 只关注 失败 的接口
    interface FailedCallback{
        // 数据加载失败
        void onDataNotAvailable(@StringRes int strRes);
    }

}
