package net.haichat.push.frags.account;

import net.haichat.common.app.Fragment;
import net.haichat.common.widget.PortraitView;
import net.haichat.push.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更新 用户信息的 界面
 */
public class UpdateInfoFragment extends Fragment {

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick
    void onPortraitClick(){
        // 点击头像时，显示 剪切界面 F
    }

}
