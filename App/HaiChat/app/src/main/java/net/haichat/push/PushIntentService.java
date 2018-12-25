package net.haichat.push;


import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import net.haichat.factory.Factory;
import net.haichat.factory.callback.helper.AccountHelper;
import net.haichat.factory.persistence.Account;

public class PushIntentService extends GTIntentService {

    // 打印日志 使用的 Tag
    private static final String TAG = PushIntentService.class.getSimpleName();

    public PushIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        // 透传消息的处理
        byte[] payload = msg.getPayload();
        if (payload != null) {
            String message = new String(payload);
            Log.i(TAG, "onReceiveMessageData -> " + message);
            onMessageArrived(message);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.i(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        onInitClientId(clientid); // 初始化 PushId
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
    }

    /**
     * 当 id 初始化的时候
     *
     * @param cid 设备 Id
     */
    private void onInitClientId(String cid) {
        // 设置设备 Id
        Account.setPushId(cid);
        // 只有 登陆状态，才能 进行 pushId 绑定
        if(Account.isLogin()) AccountHelper.bindPushId(null);
    }

    /**
     * 消息到达时
     *
     * @param message 新消息
     */
    private void onMessageArrived(String message) {
        // 将 消息 交给 Factory 处理
        Factory.dispatchPushMessage(message);
    }
}
