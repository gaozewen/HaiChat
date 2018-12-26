package net.haichat.factory.net;

import net.haichat.common.Common;
import net.haichat.factory.Factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 网络请求的封装
 */
public class Network {

    // 构建一个 Retrofit
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                // 设置 baseUrl
                .baseUrl(Common.Constance.API_URL)
                // 实质是 对 OKHttp 的封装,所以需要传入 Ok 的 client
                .client(new OkHttpClient.Builder().build())
                // 设置 Json 解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
    }

    /**
     * 获取 Api 接口
     * @return
     */
    public static ApiService getApi() {
        return getRetrofit().create(ApiService.class);
    }

}
