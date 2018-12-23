package net.haichat.push.activities;


import android.content.Context;
import android.content.Intent;

import net.haichat.common.app.Activity;
import net.haichat.push.R;
import net.haichat.push.frags.account.UpdateInfoFragment;

public class AccountActivity extends Activity {

    /**
     * 账户 Activity 显示的入口
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // 显示更新用户信息界面
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,new UpdateInfoFragment())
                .commit();
    }
}
