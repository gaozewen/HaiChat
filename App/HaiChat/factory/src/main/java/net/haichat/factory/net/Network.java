package net.haichat.factory.net;

import android.text.TextUtils;

import net.haichat.common.Common;
import net.haichat.factory.Factory;
import net.haichat.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 网络请求的封装
 */
public class Network {
    // 为什么这样写,因为 这样就只在 静态区 开辟了 instance 一块空间
    //
    private static Network instance;
    private Retrofit retrofit;

    static {
        instance = new Network();
    }

    private Network() {

    }

    // 构建一个 Retrofit
    public static Retrofit getRetrofit() {
        if (instance.retrofit != null) return instance.retrofit;

        OkHttpClient client = new OkHttpClient.Builder()
                // 给所有请求添加拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // 获取 原始 请求
                        Request original = chain.request();
                        // 重新进行 build
                        Request.Builder builder = original.newBuilder();
                        // Header 中 注入 token
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            builder.addHeader("token", Account.getToken());
                        }
                        // 构建 新的 请求
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        instance.retrofit = new Retrofit.Builder()
                // 设置 baseUrl
                .baseUrl(Common.Constance.API_URL)
                // 实质是 对 OKHttp 的封装,所以需要传入 Ok 的 client
                .client(client)
                // 设置 Json 解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

        return instance.retrofit;
    }

    /**
     * 获取 Api 接口
     *
     * @return
     */
    public static ApiService getApi() {
        return getRetrofit().create(ApiService.class);
    }

}
