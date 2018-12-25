package net.haichat.push.frags.account;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import net.haichat.common.app.Fragment;
import net.haichat.common.app.PresenterFragment;
import net.haichat.factory.presenter.account.LoginContract;
import net.haichat.factory.presenter.account.LoginPresenter;
import net.haichat.push.R;
import net.haichat.push.activities.MainActivity;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录界面
 */
public class LoginFragment
        extends PresenterFragment<LoginContract.Presenter>
        implements LoginContract.View{

    private AccountTrigger mAccountTrigger;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        mPresenter.login(phone,password);
    }

    @OnClick(R.id.txt_go_register)
    void onGoToRegisterClick(){
        mAccountTrigger.toggleView();
    }

    @Override
    public void showError(int str) {
        super.showError(str);

        mLoading.stop();
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
