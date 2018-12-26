package net.haichat.factory.data.helper;

import net.haichat.factory.Factory;
import net.haichat.factory.R;
import net.haichat.factory.data.ApiCallback;
import net.haichat.factory.model.api.RespModel;
import net.haichat.factory.model.api.user.UserUpdateModel;
import net.haichat.factory.model.card.UserCard;
import net.haichat.factory.model.db.User;
import net.haichat.factory.net.ApiService;
import net.haichat.factory.net.Network;
import net.haichat.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User 相关 Restful API
 */
public class UserHelper {

    /**
     * 更新用户信息(异步)
     */
    public static void update(
            final UserUpdateModel userUpdateModel, final ApiCallback.Callback<UserCard> callback) {
        ApiService api = Network.getApi(); // 调用 Retrofit 对我们的网络请求做代理
        Call<RespModel<UserCard>> call = api.updateInfo(userUpdateModel);
        // 发起请求 接收回调
        call.enqueue(new Callback<RespModel<UserCard>>() {
            @Override
            public void onResponse(Call<RespModel<UserCard>> call, Response<RespModel<UserCard>> response) {
                RespModel<UserCard> respModel = response.body();
                if (respModel != null && respModel.success()) {
                    UserCard card = respModel.getResult();
                    // 通过 DBFlow 保存到 本地数据库
                    User user = card.convertToUser();
                    user.save();
                    callback.onDataLoadedSuccess(card); // 请求成功 回调
                } else { // 解析错误码,进行提示
                    Factory.decodeRespCode(respModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RespModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
