package net.haichat.factory;

import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.haichat.common.app.Application;
import net.haichat.factory.callback.ApiCallback;
import net.haichat.factory.model.api.RespModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Factory {

    private static final Factory instance; // 单例模式
    private final Executor executor; // 全局的 线程池
    private final Gson gson; // 全局的 Gson

    static {
        instance = new Factory();
    }

    private Factory() {
        // 新建一个 4 个线程的 线程池
        executor = Executors.newFixedThreadPool(4);
        // 进行 Gson 的初始化
        gson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // todo： 设置一个 过滤器，数据库级别的 Model 不进行 json 转换
                //.setExclusionStrategies()
                .create();
    }

    /**
     * 收到 账户退出 的消息 需要进行账户，重新登录
     */
    private void logout() {

    }

    /**
     * 返回全局的Application
     *
     * @return Application
     */
    public static Application app() {
        return Application.getInstance();
    }

    /**
     * 异步运行的方法
     *
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable) {
        // 拿到单例，拿到线程池，然后异步执行
        instance.executor.execute(runnable);
    }

    /**
     * 返回一个 全局的 Gson
     *
     * @return
     */
    public static Gson getGson() {
        return instance.gson;
    }

    /**
     * 统一处理 API 请求 错误信息 返回码
     *
     * @param respModel RespModel
     * @param callback  ApiCallback.FailedCallback
     */
    public static void decodeRespCode(RespModel respModel, ApiCallback.FailedCallback callback) {
        if (respModel == null) return;
        switch (respModel.getCode()) {
            case RespModel.SUCCEED:
                return;
            case RespModel.ERROR_SERVICE:
                decodeRespCode(R.string.data_rsp_error_service, callback);
                break;
            case RespModel.ERROR_NOT_FOUND_USER:
                decodeRespCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RespModel.ERROR_NOT_FOUND_GROUP:
                decodeRespCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RespModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRespCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RespModel.ERROR_CREATE_USER:
                decodeRespCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RespModel.ERROR_CREATE_GROUP:
                decodeRespCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RespModel.ERROR_CREATE_MESSAGE:
                decodeRespCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RespModel.ERROR_PARAMETERS:
                decodeRespCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RespModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRespCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RespModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRespCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RespModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RespModel.ERROR_ACCOUNT_LOGIN:
                decodeRespCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RespModel.ERROR_ACCOUNT_REGISTER:
                decodeRespCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RespModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRespCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RespModel.ERROR_UNKNOWN:
            default:
                decodeRespCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRespCode(@StringRes final int resId, final ApiCallback.FailedCallback failedCallback) {
        if (failedCallback != null) failedCallback.onDataNotAvailable(resId);
    }

}
