package net.haichat.push;

import com.igexin.sdk.PushManager;

import net.haichat.common.app.Application;
import net.haichat.factory.Factory;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 调用 Factory 进行初始化
        Factory.setup();
        // 个推 初始化
        // PushService.class 为 我们 自定义推送服务
        PushManager.getInstance().initialize(this, PushService.class);
        // PushIntentService 为 我们 ⾃自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this,PushIntentService.class);
    }
}
