package net.haichat.common.app;

import android.content.Context;

import net.haichat.factory.presenter.BaseContract;

/**
 * MVP 模式 使用
 *
 * @param <Presenter>
 */
public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 在界面 onAttach 之后就触发初始化 Presenter
        initPresenter();
    }

    /**
     * 初始化 Presenter
     *
     * @return Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        // 显示错误信息
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) mPlaceHolderView.triggerLoading();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        // View 中 赋值 Presenter
        mPresenter = presenter;
    }
}
