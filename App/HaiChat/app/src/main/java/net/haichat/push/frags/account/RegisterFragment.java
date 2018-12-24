package net.haichat.push.frags.account;

import android.content.Context;

import net.haichat.common.app.Fragment;
import net.haichat.common.app.PresenterFragment;
import net.haichat.factory.presenter.account.RegisterContract;
import net.haichat.factory.presenter.account.RegisterPresenter;
import net.haichat.push.R;

/**
 * 注册界面
 */
public class RegisterFragment
        extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {

    private AccountTrigger mAccountTrigger;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void registerSuccess() {

    }
}
