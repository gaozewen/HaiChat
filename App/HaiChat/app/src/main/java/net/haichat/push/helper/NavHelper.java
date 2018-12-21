package net.haichat.push.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * 解决对 Fragment 的调度与重用问题，
 * 达到最优的 Fragment 切换
 */
// 思考：
// 我们要接管的其实是 对 F 的调度，
// 那么对 F 的调度其实是对 FragmentManager 的使用
public class NavHelper<T> {

    // 所有的 Tab 集合
    private final SparseArray<Tab<T>> tabs = new SparseArray<>(); // ArrayList 和 LinkedList 开销太大

    // 用户初始化的 必要参数
    private final Context context; // F 若通过 clazz 去创建 还需要一个 context
    private final FragmentManager fragmentManager;
    private final int containerId; // 存放 F 的 容器 id
    private final OnTabChangedListener<T> listener;

    // 当前选中的 Tab
    private Tab<T> currentTab;

    public NavHelper(Context context, FragmentManager fragmentManager,
                     int containerId, OnTabChangedListener<T> listener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.listener = listener;
    }

    /**
     * 添加 Tab
     * @param menuId Tab 对应的菜单 id
     * @param tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab){
        tabs.put(menuId,tab);
        return this;
    }

    /**
     * 获取当前显示的 Tab
     * @return Tab<T>
     */
    public Tab<T> getCurrentTab(){
        return currentTab;
    }


    /**
     * 执行 点击 底部导航菜单按钮
     *
     * @param menuId 菜单 id
     * @return 是否能够处理
     */
    public boolean performClickMenu(int menuId) {
        Tab<T> tab = tabs.get(menuId);
        if(tab != null){
            this.doSelect(tab);
            return true;
        }
        return false; // 不能处理
    }

    /**
     * 进行真实的 Tab 选择操作
     * @param selectedTab 被选中的 Tab
     */
    private void doSelect(Tab<T> selectedTab){
        Tab<T> oldTab = null;

        if(currentTab != null){
            oldTab = currentTab;
            if(oldTab == selectedTab) { // 重复点击同一个 tab
                this.notifyTabReselect(selectedTab);
                return;
            }
        }

        // 赋值 并 调用切换方法
        currentTab = selectedTab;
        this.doTabChanged(currentTab,oldTab);
    }

    // 该方法中进行 复杂的 F 调度
    /**
     * 进行 Fragment 的 真实 调度操作
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab,Tab<T> oldTab){
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if(oldTab != null) {
            if(oldTab.fragment != null){
                // 从界面移除,但是还在 Fragment 的缓存空间中 (detach 分离)
                // 即 从 界面 移除到 缓存
                ft.detach(oldTab.fragment);
            }
        }

        if(newTab != null){
            if(newTab.fragment == null) { // 默认情况下 Tab 的 F 未赋值
                // 首次新建 缓存起来
                newTab.fragment = Fragment.instantiate(context, newTab.clazz.getName(), null);
                ft.add(containerId,newTab.fragment,newTab.clazz.getName());// 最后一个参数 是 tag
            }else {
                // 从 FragmentManager 的 缓存空间中 重新加载到界面中
                // 即 从 缓存 加载到 界面
                ft.attach(newTab.fragment);
            }
        }

        // 提交事务
        ft.commit();
        // 通知回调
        this.notifyTabSelect(newTab,oldTab);
    }

    /**
     * 回调我们的监听器
     * @param newTab
     * @param oldTab
     */
    private void notifyTabSelect(Tab<T> newTab,Tab<T> oldTab){
        if(listener != null){
            listener.onTabChanged(newTab,oldTab);
        }
    }

    // 通知界面 刷新
    private void notifyTabReselect(Tab<T> tab){
        // TODO: 重复点击 同一个 tab 做的操作
    }

    // 为什么写成 static : 为了不让 NavHelper 循环引用
    // T：我们不知道有哪些东西需要在 切换 tab 时替换(title...)
    /**
     * 我们的所有的 Tab 基础属性
     *
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {
        // 菜单对应的 F 是什么,我们不希望直接初始化实例
        // 而是希望内部完成调度，所以传 Class
        public Class<? extends Fragment> clazz;
        public T extra; // 额外的字段，用户自己设定需要使用什么

        // 内部缓存的 tab 菜单 对应的 F
        // Package 权限
        Fragment fragment;

        public Tab(Class<? extends Fragment> clazz, T extra) {
            this.clazz = clazz;
            this.extra = extra;
        }
    }

    // 建立 事件机制, 处理完之后 抛出事件
    /**
     * 定义事件处理完成后的 回调 接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }

}
