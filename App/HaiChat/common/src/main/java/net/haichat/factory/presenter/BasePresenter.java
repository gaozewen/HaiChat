package net.haichat.factory.presenter;

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    protected T mView;

    public BasePresenter(T view) { // 当前的布局
        setView(view);
    }

    /**
     * 设置一个 View
     * 子类可以覆写
     *
     * @param view
     */
    protected void setView(T view) {
        this.mView = view;
    }

    /**
     * 给子类使用的 获取 View 方法
     * 不允许覆写
     *
     * @return
     */
    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        T view = mView;
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void destroy() {
        T view = mView;
        if (view != null) {
            // 把 Presenter 设置为 NULL
            view.setPresenter(null);
        }
        mView = null;
    }
}
