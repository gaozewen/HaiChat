package net.haichat.factory.net;

import net.haichat.factory.model.api.RespModel;
import net.haichat.factory.model.api.account.AccountRespModel;
import net.haichat.factory.model.api.account.LoginModel;
import net.haichat.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 使用 Retrofit 发送的
 * 网络请求 的所有 接口
 */
public interface ApiService {

    /**
     * 注册
     *
     * @param registerModel 注册所需参数
     * @return RespModel<AccountRespModel>
     */
    @POST("account/register")
    Call<RespModel<AccountRespModel>> accountRegister(@Body RegisterModel registerModel);

    /**
     * 登录
     *
     * @param loginModel LoginModel
     * @return RespModel<AccountRespModel>
     */
    @POST("account/login")
    Call<RespModel<AccountRespModel>> accountLogin(@Body LoginModel loginModel);

    /**
     * 绑定 设备 Id
     *
     * @param pushId
     * @return RespModel<AccountRespModel>
     */
    @POST("account/bind/{pushId}")
    Call<RespModel<AccountRespModel>> accountBind(
            @Path(encoded = true, value = "pushId") String pushId);
}
