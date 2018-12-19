package net.haichat.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在界面未初始化之前调用的初始化窗口
        initWindows();

        if(initArgs(getIntent().getExtras())) { // 初始化数据成功
            getContentLayoutId();
            initWidget();
            initData();
        }else {
            finish();
        }

    }

    // 初始化窗口的一些数据
    protected void initWindows() {

    }


    // 默认返回true
    // 子类可重写 做相应的逻辑判断

    /**
     * 初始化相关参数
     * @param bundle 接收到的参数
     * @return 如果参数正确 返回 true，错误返回 false
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    // 子类必须重写此方法
    // 作用：获取布局id
    /**
     * 得到当前界面的资源文件 id
     * @return 资源文件 id
     */
    protected abstract int getContentLayoutId();

    // protected 只要继承这个类就能使用

    /**
     * 初始化控件
     */
    protected void initWidget(){

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    // 重写返回事件

    // 点击导航上的 返回上一页
    @Override
    public boolean onSupportNavigateUp() {
        finish();// 当点击界面导航返回时，Finish 当前界面
        return super.onSupportNavigateUp();
    }

    // 按手机 back 键返回
    @Override
    public void onBackPressed() {
        // 情景：当此 Activity 中 有多个 Fragment，而我只想返回上一个 Fragment
        // 知识点：涉及到 Activity 和 Fragment 之间的通讯
        super.onBackPressed();
        finish();
    }
}
