package net.haichat.factory.net;

import net.haichat.factory.model.api.RespModel;
import net.haichat.factory.model.api.account.AccountRespModel;
import net.haichat.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 使用 Retrofit 发送的
 * 网络请求 的所有 接口
 */
public interface ApiService {

    /**
     * 注册接口
     * @param registerModel 注册所需参数
     * @return RespModel<AccountRespModel>
     */
    @POST("account/register")
    Call<RespModel<AccountRespModel>> accountRegister(@Body RegisterModel registerModel);

}
