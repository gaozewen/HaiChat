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

import java.util.List;

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
                    Factory.decodeRespCode(respModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RespModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    // 搜索的方法
    public static Call search(String name, final ApiCallback.Callback<List<UserCard>> callback) {
        ApiService api = Network.getApi();
        Call<RespModel<List<UserCard>>> call = api.searchUser(name);

        call.enqueue(new Callback<RespModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RespModel<List<UserCard>>> call, Response<RespModel<List<UserCard>>> response) {
                RespModel<List<UserCard>> respModel = response.body();
                if (respModel != null && respModel.success()) {
                    callback.onDataLoadedSuccess(respModel.getResult());
                } else {
                    Factory.decodeRespCode(respModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RespModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });

        // 把当前的调度者返回
        return call;
    }

    // 关注的方法
    public static void follow(String targetId, final ApiCallback.Callback<UserCard> callback) {
        ApiService api = Network.getApi();
        Call<RespModel<UserCard>> call = api.follow(targetId);

        call.enqueue(new Callback<RespModel<UserCard>>() {

            @Override
            public void onResponse(Call<RespModel<UserCard>> call, Response<RespModel<UserCard>> response) {
                RespModel<UserCard> respModel = response.body();
                if (respModel != null && respModel.success()) {
                    UserCard card = respModel.getResult();
                    // 通过 DBFlow 保存到 本地数据库
                    User user = card.convertToUser();
                    user.save();
                    //todo: 通知 联系人界面刷新
                    callback.onDataLoadedSuccess(card);
                } else {
                    Factory.decodeRespCode(respModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RespModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    // 刷新 联系人 列表
    public static void refreshContacts(final ApiCallback.Callback<List<UserCard>> callback) {
        ApiService api = Network.getApi();
        api.contacts().enqueue(new Callback<RespModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RespModel<List<UserCard>>> call, Response<RespModel<List<UserCard>>> response) {
                RespModel<List<UserCard>> respModel = response.body();
                if (respModel != null && respModel.success()) {
                    callback.onDataLoadedSuccess(respModel.getResult());
                } else {
                    Factory.decodeRespCode(respModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RespModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
