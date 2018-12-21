package net.haichat.push;


import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.haichat.common.app.Activity;
import net.haichat.common.widget.PortraitView;
import net.haichat.push.frags.main.ActiveFragment;
import net.haichat.push.frags.main.GroupFragment;
import net.haichat.push.helper.NavHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.appbar)
    AppBarLayout mAppbar;       // 顶部导航条

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;     // 头像控件

    @BindView(R.id.txt_title)
    TextView mTitle;            // 顶部导航条(标题)

    @BindView(R.id.lay_container)
    FrameLayout mContainer;     // 中间容器部分

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation; // 底部导航栏

    private NavHelper mNavHelper; // 工具类变量

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // 初始化 BottomNavigation 工具类
        mNavHelper = new NavHelper();

        // 设置 BottomNavigation 切换事件监听
        mNavigation.setOnNavigationItemSelectedListener(this);

        // 设置 appbar 背景图片
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop() // 居中剪切
                .into(new ViewTarget<View, GlideDrawable>(mAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        // 这个 this 是 new ViewTarget
                        // resource.getCurrent() 当前这一帧的图片
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    /**
     * 为 appbar 搜索按钮绑定点击事件
     */
    @OnClick(R.id.im_search)
    void onSearchMenuClick() {

    }

    /**
     * 为右下角浮动的快捷操作按钮绑定单击事件
     */
    @OnClick(R.id.btn_action)
    void onActionClick() {

    }

    boolean isFirst = true; // 第一次添加，不需要移除原来的 Fragment

    /**
     * 实现 底部导航栏 切换监听
     *
     * @param menuItem
     * @return true 能够处理，且处理了 false 不能处理，且未处理
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // 转接 事件流 到工具类中
        return mNavHelper.performClickMenu(menuItem.getItemId()); // 返回 true 表示正常处理
    }
}
