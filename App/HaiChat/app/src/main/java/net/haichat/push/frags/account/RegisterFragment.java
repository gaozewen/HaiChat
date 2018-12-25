package net.haichat.push.frags.account;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import net.haichat.common.app.Fragment;
import net.haichat.common.app.PresenterFragment;
import net.haichat.factory.presenter.account.RegisterContract;
import net.haichat.factory.presenter.account.RegisterPresenter;
import net.haichat.push.R;
import net.haichat.push.activities.MainActivity;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册界面
 */
public class RegisterFragment
        extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {

    private AccountTrigger mAccountTrigger; // 用来 切换 登录/注册 界面

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Loading mSubmit;
    @BindView(R.id.txt_go_login)
    TextView mGoLogin;


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

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){

        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String name = mName.getText().toString();

        // 这里的 mPresenter 必定有值，在 BasePresenter 中 通过 mView.setPresenter(this) 已经赋值了
        // 调用 P 层 进行注册
        mPresenter.register(phone,password,name);
    }

    @OnClick(R.id.txt_go_login)
    void onGoToLoginClick(){
        // 让 AccountActivity 进行界面切换
        mAccountTrigger.toggleView();
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        // 当需要显示错误的时候触发, 一定是 逻辑操作 被迫终止了

        mLoading.stop(); // 停止 Loading
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mName.setEnabled(true);
        mSubmit.setEnabled(true);
        mGoLogin.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        // 业务逻辑 正在 执行时，界面不可操作

        mLoading.start();
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mName.setEnabled(false);
        mSubmit.setEnabled(false);
        mGoLogin.setEnabled(false);
    }

    @Override
    public void registerSuccess() {
        // 注册成功，账户已经登录，跳转到 MainActivity
        MainActivity.show(getContext()); // 因为是 F 所以需要 通过 getContext 获取 Activity
        // 关闭当前界面
        getActivity().finish();
    }
}
