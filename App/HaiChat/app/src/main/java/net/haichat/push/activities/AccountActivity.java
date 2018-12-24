package net.haichat.push.activities;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import net.haichat.common.app.Activity;
import net.haichat.push.R;
import net.haichat.push.frags.account.AccountTrigger;
import net.haichat.push.frags.account.LoginFragment;
import net.haichat.push.frags.account.RegisterFragment;

public class AccountActivity extends Activity
        implements AccountTrigger {

    private Fragment mCurFragment; // 当前的 F
    private Fragment mLoginFragment; // 登录 F
    private Fragment mRegisterFragment; // 注册 F

    /**
     * 账户 Activity 显示的入口
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 默认情况下 CurF == LoginF
        mCurFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    // Activity 中受到裁剪图片成功的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void toggleView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment) {
            // 默认情况下为空， 第一次 需要初始化
            if (mRegisterFragment == null) mRegisterFragment = new RegisterFragment();
            fragment = mRegisterFragment;
        } else {
            // 因为 当前 F 默认就是
            fragment = mLoginFragment;
        }

        // 重新 赋值 当前 显示的 F
        mCurFragment = fragment;
        // 切换显示
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container, mCurFragment)
                .commit();
    }
}
