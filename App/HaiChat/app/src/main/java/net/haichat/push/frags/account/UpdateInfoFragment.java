package net.haichat.push.frags.account;

import net.haichat.common.app.Fragment;
import net.haichat.common.widget.PortraitView;
import net.haichat.push.R;
import net.haichat.push.frags.media.GalleryFragment;

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

    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
        // 点击头像时，显示 图片选择 GalleryFragment

        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {

                    }
                })
                // show 的时候建议使用 getChildFragmentManager()
                // 不然会用到 Activity 的 Manager，引起一些不必要的麻烦
                // tag GalleryFragment class 名字
                .show(getChildFragmentManager(),GalleryFragment.class.getName());
    }

}
