package net.haichat.factory.callback.helper;

import net.haichat.factory.Factory;
import net.haichat.factory.R;
import net.haichat.factory.callback.ApiCallback;
import net.haichat.factory.model.api.RespModel;
import net.haichat.factory.model.api.account.AccountRespModel;
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
     * 注册 API，异步调用
     *
     * @param registerModel 注册接口所需参数 model
     * @param callback      请求成功与失败的回调
     */
    public static void register(
            final RegisterModel registerModel, final ApiCallback.Callback<User> callback) {

        // 调用 Retrofit 对我们的网络请求接口做代理
        ApiService api = Network.getRetrofit().create(ApiService.class);
        // 得到一个 call
        Call<RespModel<AccountRespModel>> call = api.accountRegister(registerModel);
        // 异步的请求
        call.enqueue(new Callback<RespModel<AccountRespModel>>() {
            @Override // api 请求成功返回
            public void onResponse(Call<RespModel<AccountRespModel>> call,
                                   Response<RespModel<AccountRespModel>> response) {
                // 从 response 的 body 部分获取我们的 RespModel (使用 Gson 解析得到的)
                RespModel<AccountRespModel> respModel = response.body();
                if (respModel != null && respModel.success()) { // 注册成功
                    // 拿到实体
                    AccountRespModel accountRespModel = respModel.getResult();
                    // 判断绑定状态
                    if(accountRespModel.isBind()){ // 已绑定 pushId
                        User user = accountRespModel.getUser();
                        // todo: 进行数据库写入 和 缓存绑定
                        // 执行 注册成功 Presenter 逻辑
                        callback.onDataLoadedSuccess(user);
                    }else { // 未绑定 设备 id
                        // todo: 绑定 pushId(设备号)
                        bindPushId(callback);
                    }
                } else { // 注册失败，原因解析
                    Factory.decodeRespCode(respModel,callback);
                }
            }

            @Override // api 请求失败
            public void onFailure(Call<RespModel<AccountRespModel>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 绑定设备 id
     * @param callback ApiCallback.Callback<User>
     */
    public static void bindPushId(final ApiCallback.Callback<User> callback){
        // todo: 绑定 pushId 编写
        Account.setIsBind(true);
    }

}
