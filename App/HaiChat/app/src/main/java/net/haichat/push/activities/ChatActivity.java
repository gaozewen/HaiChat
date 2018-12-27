package net.haichat.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import net.haichat.factory.model.IUser;
import net.haichat.push.R;

public class ChatActivity extends Activity {

    /**
     * 显示 和 target 的聊天界面
     * @param context
     * @param target IUser
     */
    public static void show(Context context, IUser target) {
        // todo
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

}
