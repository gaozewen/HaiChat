package net.haichat.push.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import net.haichat.common.app.Activity;
import net.haichat.push.R;
import net.haichat.push.frags.user.UpdateInfoFragment;

public class UserActivity extends Activity {

    private Fragment mCurFragment; // 当前的 F

    /**
     * 用户信息 Activity 显示的入口
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,UserActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = new UpdateInfoFragment();

        // 显示更新用户信息界面
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mCurFragment)
                .commit();
    }

    // Activity 中接收到裁剪图片成功的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }

}
