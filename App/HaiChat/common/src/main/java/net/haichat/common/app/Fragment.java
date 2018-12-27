package net.haichat.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.haichat.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class Fragment extends android.support.v4.app.Fragment {

    protected View mRoot; // 因为 root 可能被复用
    protected Unbinder mRootUnBinder;
    protected PlaceHolderView mPlaceHolderView; // 自定义的占位布局
    // 为什么定义这个变量，否则每次 切换 tab 页 都要重新初始化
    protected boolean mIsFirstInitData = true; // 是否是第一次初始化数据


    // 当 F 添加到 Activity 的时候，最先调用的方法是 onAttach
    // 而 Activity 就是 context
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 初始化参数
        initArgs(getArguments());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前的根布局， 但是不在创建时就 添加到 container 里边去(所以 attachToRoot 是 false)
            View root = inflater.inflate(layId, container, false);
            initWidget(root); // root 根布局
            mRoot = root;
        } else {
            // 因为 上面 false 不是将 F 立即添加到父控件中，所以 父控件中可能已有 F，且未被回收
            // 所以我们需要 先将原来的 mRoot 先从父控件中 移除，
            // 这样 当 此方法 返回 mRoot 后，mRoot 才会被添加到 父控件中
            if (mRoot.getParent() != null) {
                // 把当前 root 从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 触发一次以后就不会触发
        if (mIsFirstInitData) {
            mIsFirstInitData = false;
            onFirstInit();
        }
        // 当 View 创建完成后，初始化数据
        initData();
    }

    /**
     * 初始化相关参数
     *
     * @param bundle 接收到的参数
     */
    protected void initArgs(Bundle bundle) {
    }

    /**
     * 得到当前界面的资源文件 id
     *
     * @return 资源文件 id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     *
     * @param root 根布局
     */
    protected void initWidget(View root) {
        mRootUnBinder = ButterKnife.bind(this, root); // 绑定 F 到 root(A) 中
    }

    /**
     * 只有 首次 初始化的时候调用
     */
    protected void onFirstInit() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 返回按键触发时调用
     *
     * @return 返回 true 代表我已经处理返回逻辑，Activity 不用自己 finish。返回 false 代表我没有处理，Activity 自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }
}
