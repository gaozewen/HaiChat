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
        PushManager.getInstance().initialize(this);
    }
}
