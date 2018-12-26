package net.haichat.factory.data.helper;

import android.text.TextUtils;

import net.haichat.factory.Factory;
import net.haichat.factory.R;
import net.haichat.factory.data.ApiCallback;
import net.haichat.factory.model.api.RespModel;
import net.haichat.factory.model.api.account.AccountRespModel;
import net.haichat.factory.model.api.account.LoginModel;
import net.haichat.factory.model.api.account.RegisterModel;
import net.haichat.factory.model.db.User;
import net.haichat.factory.net.Network;
import net.haichat.factory.net.ApiService;
import net.haichat.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Account 相关 Restful API
 */
public class AccountHelper {

    /**
     * 注册
     *
     * @param registerModel 注册接口所需参数 model
     * @param callback      请求成功与失败的回调
     */
    public static void register(
            final RegisterModel registerModel, final ApiCallback.Callback<User> callback) {
        ApiService api = Network.getApi();  // 调用 Retrofit 对我们的网络请求接口做代理
        Call<RespModel<AccountRespModel>> call = api.accountRegister(registerModel);
        call.enqueue(new AccountRespCallback(callback)); // 异步的请求
    }

    /**
     * 绑定设备 id
     *
     * @param callback ApiCallback.Callback<User>
     */
    public static void bindPushId(final ApiCallback.Callback<User> callback) {
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) return;

        ApiService api = Network.getApi(); // 使用 Retrofit 对 Restful 接口做代理
        Call<RespModel<AccountRespModel>> call = api.accountBind(pushId);
        call.enqueue(new AccountRespCallback(callback));
    }

    /**
     * 登录
     *
     * @param loginModel LoginModel
     * @param callback   ApiCallback.Callback<User>
     */
    public static void login(
            final LoginModel loginModel, final ApiCallback.Callback<User> callback) {
        ApiService api = Network.getApi();
        Call<RespModel<AccountRespModel>> call = api.accountLogin(loginModel);
        call.enqueue(new AccountRespCallback(callback));
    }


    /**
     * Retrofit 请求 回调封装
     */
    private static class AccountRespCallback implements Callback<RespModel<AccountRespModel>> {

        final ApiCallback.Callback<User> callback;

        public AccountRespCallback(ApiCallback.Callback<User> callback) {
            this.callback = callback;
        }

        @Override // 服务器正常返回
        public void onResponse(Call<RespModel<AccountRespModel>> call,
                               Response<RespModel<AccountRespModel>> response) {
            // 从 response 的 body 部分获取我们的 RespModel (使用 Gson 解析得到的)
            RespModel<AccountRespModel> respModel = response.body();
            if (respModel != null && respModel.success()) { // 操作成功
                // 拿到实体
                AccountRespModel accountRespModel = respModel.getResult();

                User user = accountRespModel.getUser();
                user.save(); // 保存进 DBFlow

                // 使用 DBFlow 进行数据库写入 和 缓存绑定
                // 1. 直接保存 ==> 只能保存自己
                // user.syncLoginInfo();
                // 2. 通过 ModelAdapter 保存 ==> 可以进行 集合存储
                // FlowManager.getModelAdapter(User.class).syncLoginInfo(user);
                // 3. 放在事务中
                // DatabaseDefinition dd = FlowManager.getDatabase(AppDatabase.class);
                // dd.beginTransactionAsync(new ITransaction() {
                //     @Override
                //     public void execute(DatabaseWrapper databaseWrapper) {
                //         FlowManager.getModelAdapter(User.class).syncLoginInfo(user);
                //     }
                // }).build().execute();// 异步

                Account.syncLoginInfo(accountRespModel); // (保存到 sp 中)同步到 XML 进行持久化保存

                // 判断绑定状态
                if (accountRespModel.isBind()) { // 已绑定 pushId
                    Account.setIsBind(true); // 缓存 绑定状态 到 Sp
                    // 执行 Presenter 逻辑
                    if (callback != null) callback.onDataLoadedSuccess(user);
                } else {
                    bindPushId(callback); // 绑定设备 id
                }
            } else { // 操作失败，原因解析
                Factory.decodeRespCode(respModel, callback);
            }
        }

        @Override // 未获取 服务器 返回(网络错误)
        public void onFailure(Call<RespModel<AccountRespModel>> call, Throwable t) {
            if (callback != null) callback.onDataNotAvailable(R.string.data_network_error);
        }
    }

}
