package net.haichat.push.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.haichat.common.app.Activity;
import net.haichat.push.R;
import net.haichat.push.frags.account.AccountTrigger;
import net.haichat.push.frags.account.LoginFragment;
import net.haichat.push.frags.account.RegisterFragment;
import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class AccountActivity extends Activity
        implements AccountTrigger {

    private Fragment mCurFragment; // 当前的 F
    private Fragment mLoginFragment; // 登录 F
    private Fragment mRegisterFragment; // 注册 F

    @BindView(R.id.im_bg)
    ImageView mBg;

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
        // 初始化 到 登录界面
        // 默认情况下 CurF == LoginF
        mCurFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();

        // 初始化背景
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop()
                .into(new ViewTarget<ImageView, GlideDrawable>(mBg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        // 获取 glide 的 Drawable
                        Drawable drawable = resource.getCurrent();
                        // 使用 适配类 进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        // 设置 着色的效果 和 蒙版模式
                        drawable.setColorFilter(
                                UiCompat.getColor(getResources(), R.color.colorAccent),
                                PorterDuff.Mode.SCREEN
                        );
                        this.view.setImageDrawable(drawable);
                    }
                });
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
