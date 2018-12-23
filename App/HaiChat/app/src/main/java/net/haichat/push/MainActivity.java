package net.haichat.push;


import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.haichat.common.app.Activity;
import net.haichat.common.widget.PortraitView;
import net.haichat.push.activities.AccountActivity;
import net.haichat.push.frags.main.ActiveFragment;
import net.haichat.push.frags.main.ContactFragment;
import net.haichat.push.frags.main.GroupFragment;
import net.haichat.push.helper.NavHelper;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {

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

    @BindView(R.id.btn_action)
    FloatActionButton mAction;        // 容器右下角 浮动按钮

    private NavHelper<Integer> mNavHelper; // 工具类变量

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        performCodeWithPermission("获取读写权限", new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        System.out.println("gzw success");
                    }

                    @Override
                    public void noPermission() {
                        System.out.println("gzw fail");

                    }
                },
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // 初始化 BottomNavigation 工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);
        mNavHelper
                .add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));

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

    @Override
    protected void initData() {
        super.initData();

        // 从底部导航中接管我们的 Menu，然后 进行手动额触发第一次点击
        Menu menu = mNavigation.getMenu();
        // 触发首次选中 Home，即相当于 触发 下面的 onNavigationItemSelected 方法
        menu.performIdentifierAction(R.id.action_home, 0);
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
        AccountActivity.show(this);
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

    /**
     * 实现 NavHelper 事件处理完成 回调
     *
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        // 从 额外字段中 取出 我们的 title 资源 id
        mTitle.setText(newTab.extra);

        // 对 中间容器右下角 浮动按钮 进行 隐藏 与 显示 的 动画
        float transY = 0;
        float rotation = 0;

        if (Objects.equals(newTab.extra, R.string.title_home)) {
            // 主界面 时 隐藏
            transY = Ui.dipToPx(getResources(), 76); // Y 轴向下为 正， 隐藏
        } else {
            // transY 默认为 0 则 显示
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }

        // 开始动画
        mAction.animate()
                .rotation(rotation) // 旋转
                .translationY(transY) // Y 轴位移
                .setInterpolator(new AnticipateOvershootInterpolator(1)) // 有点弹性效果的
                .setDuration(480) // 480ms
                .start();
    }
}
