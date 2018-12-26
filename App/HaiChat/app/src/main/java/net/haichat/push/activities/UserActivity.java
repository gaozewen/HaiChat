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
import net.haichat.push.frags.user.UpdateInfoFragment;
import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class UserActivity extends Activity {

    private Fragment mCurFragment; // 当前的 F

    @BindView(R.id.im_bg)
    ImageView mBg;

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

        // 初始化背景
        Glide.with(this)
                .load(R.drawable.bg_src_hai)
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
                                UiCompat.getColor(getResources(), R.color.white_alpha_160),
                                PorterDuff.Mode.SCREEN
                        );
                        this.view.setImageDrawable(drawable);
                    }
                });
    }

    // Activity 中接收到裁剪图片成功的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }

}
