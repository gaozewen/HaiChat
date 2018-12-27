package net.haichat.factory.presenter;

import android.support.annotation.StringRes;

import net.haichat.common.widget.recycler.RecyclerAdapter;

/**
 * MVP 模式中 的 基本契约
 */
public interface BaseContract {
    // 共用的 界面 职责
    interface View<T extends Presenter> {
        // 公共的：显示一个字符串错误
        void showError(@StringRes int str);

        // 公共的：显示进度条
        void showLoading();

        // 支持设置一个 Presenter
        void setPresenter(T presenter);
    }

    // 公用的 P层 职责
    interface Presenter {
        // 共用的：开始触发
        void start();

        // 共用的：销毁触发
        void destroy();
    }

    // 公用的 界面(列表) 职责
    interface RecyclerView<T extends Presenter, DataModel> extends View<T> {
        // 界面端只能刷新整个数据集合，不能精确到每一条数据更新
        // void onDone(List<User> users); // 每次刷新全部，这样的体验是极差的

        // 因为 RecyclerView 本身就可以做到增量刷新
        // 拿到一个适配器，然后 自动进行 局部刷新
        RecyclerAdapter<DataModel> getRecyclerAdapter();

        // 当 Adapter 数据更改了 的时候触发
        void onAdapterDataChanged();
    }

}
