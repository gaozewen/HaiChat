package net.haichat.push.frags.account;

import android.content.Context;

import net.haichat.common.app.Fragment;
import net.haichat.push.R;

/**
 * 登录界面
 */
public class LoginFragment extends Fragment {

    private AccountTrigger mAccountTrigger;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context); // 因为 context = AccountActivity
        mAccountTrigger = (AccountTrigger) context; // 所以直接可获取引用
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

}
