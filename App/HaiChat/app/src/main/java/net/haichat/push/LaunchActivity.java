package net.haichat.push;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import net.haichat.common.app.Activity;
import net.haichat.factory.persistence.Account;
import net.haichat.push.activities.AccountActivity;
import net.haichat.push.activities.MainActivity;
import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;


public class LaunchActivity extends Activity {

    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // 获取根布局
        View root = findViewById(R.id.activity_launch);
        // 获取颜色
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        // 创建一个 Drawable
        ColorDrawable colorDrawable = new ColorDrawable(color);
        // 设置给背景
        root.setBackground(colorDrawable);
        mBgDrawable = colorDrawable;
    }

    @Override
    protected void initData() {
        super.initData();
        // 动画进入到 50% 开始 开始等待 PushId 获取
        startAnim(0.5f, this::waitSetupPushId);
    }

    /**
     * 等待 个推框架 对我们的 PushId 设置好值
     */
    private void waitSetupPushId() {

        if (Account.isLogin()) { // 已登录
            if (Account.isBind()) { // 已绑定，直接跳转
                skipToMainActivity();
                return;
            }
        } else { // 未登录
            if (!TextUtils.isEmpty(Account.getPushId())) { // 初始化完成(已获取 pushId)
                startAnim(1f, this::skipToMainActivity); // 在跳转之前完成剩下的 50% 动画
                return;
            }
        }

        getWindow() // 循环等待
                .getDecorView()
                .postDelayed(this::waitSetupPushId, 500);
    }


    /**
     * 给背景设置一个 (属性)动画
     *
     * @param endProgress 动画的 结束 进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress, final Runnable endCallback) {
        // 结束时颜色
        int endColor = Resource.Color.WHITE;
        // 运算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int curColor = (int) evaluator.evaluate(
                endProgress,
                mBgDrawable.getColor(),
                endColor
        );
        // 构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, curColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(), curColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 结束时触发
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private final Property<LaunchActivity, Object> property
            = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }
    };

    private void skipToMainActivity() {
        performCodeWithPermission("获取应用所需权限", new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        if (Account.isLogin()) {// 已登录 --> MainActivity
                            MainActivity.show(LaunchActivity.this);
                        } else { // 未登录 --> AccountActivity
                            AccountActivity.show(LaunchActivity.this);
                        }
                        finish();
                    }

                    @Override
                    public void noPermission() {
                        finish();

                    }
                },
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO);
    }
}
