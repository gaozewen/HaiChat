package net.haichat.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

import net.haichat.factory.Factory;
import net.haichat.factory.data.helper.AccountHelper;
import net.haichat.factory.persistence.Account;

/**
 * 个推 的 消息接收器
 */
public class MessageReceiver extends BroadcastReceiver {

    // 打印日志 使用的 Tag
    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        Bundle bundle = intent.getExtras();

        // 判断当前消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                Log.i(TAG, "GET_CLIENTID: " + bundle.toString());
                // 当 id 初始化的时候
                onClientInit(bundle.getString("clientid"));
                break;
            case PushConsts.GET_MSG_DATA:
                // 常规消息送达
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String message = new String(payload);
                    Log.i(TAG, "GET_MSG_DATA: " + message);
                    onMessageArrived(message);
                }
                break;
            default:
                Log.i(TAG, "OTHER: " + bundle.toString());
                break;
        }
    }

    /**
     * 当 id 初始化的时候
     *
     * @param cid 设备 Id
     */
    private void onClientInit(String cid) {
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
