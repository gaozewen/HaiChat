package net.haichat.push.frags.account;

import android.content.Context;

import net.haichat.common.app.Fragment;
import net.haichat.push.R;

/**
 * 注册界面
 */
public class RegisterFragment extends Fragment {

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
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }
}
