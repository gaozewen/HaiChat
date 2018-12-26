package net.haichat.push.frags.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.haichat.common.app.Application;
import net.haichat.common.app.Fragment;
import net.haichat.common.app.PresenterFragment;
import net.haichat.common.widget.PortraitView;
import net.haichat.factory.Factory;
import net.haichat.factory.net.UploadHelper;
import net.haichat.factory.presenter.user.UpdateInfoContract;
import net.haichat.factory.presenter.user.UpdateInfoPresenter;
import net.haichat.push.R;
import net.haichat.push.activities.MainActivity;
import net.haichat.push.frags.media.GalleryFragment;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新 用户信息的 界面
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View {

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.im_sex)
    ImageView mSex;
    @BindView(R.id.edit_desc)
    EditText mDesc;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    private String mPortraitPath;
    private boolean isMan = true;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }
    // initPresenter
    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        // 点击头像时，显示 图片选择 GalleryFragment

        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        // 设置图片处理的格式为 JPEG
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        // 设置压缩后的图片精度
                        options.setCompressionQuality(96);
                        // 设置组件颜色
                        options.setStatusBarColor(0x801572FC);
                        options.setToolbarColor(0x801572FC);
                        options.setActiveWidgetColor(0x801572FC);
                        // 获取头像缓存地址
                        File cachePath = Application.getPortraitTmpFile();
                        // 打开 UCrop 裁剪框架的 Activity
                        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(cachePath))
                                .withAspectRatio(1, 1) // 1:1 的比例，保证正方形
                                .withMaxResultSize(520, 520)// 520x520 px
                                .withOptions(options)
                                .start(getActivity());
                    }
                })
                // show 的时候建议使用 getChildFragmentManager()
                // 不然会用到 Activity 的 Manager，引起一些不必要的麻烦
                // tag GalleryFragment class 名字
                .show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 裁剪框架 Activity 结束回调
        // 该回调 是从 AccountActivity 中传入进 此 Fragment 的
        // 然后取出结果值，用来进行 图片加载
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                this.loadPortrait(resultUri);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown); // 提示未知错误
            // final Throwable cropError = UCrop.getError(data); // 不处理错误
        }
    }

    // portrait 将 选中的头像图片 在 界面上显示
    private void loadPortrait(Uri uri) {
        mPortraitPath = uri.getPath(); // 本地缓存地址
        Glide.with(this).load(uri).asBitmap().centerCrop().into(mPortrait);
    }

    // sex
    @OnClick(R.id.im_sex)
    void onSexClick() {
        isMan = !isMan;
        Drawable drawable = getResources().getDrawable(isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);

        mSex.setImageDrawable(drawable);
        mSex.getBackground().setLevel(isMan ? 0 : 1); // 设置背景的层级,切换颜色
    }

    // submit
    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String desc = mDesc.getText().toString();
        mPresenter.update(mPortraitPath, isMan, desc);
    }

    @Override
    public void showError(int str) {
        super.showError(str);

        mLoading.stop();
        mPortrait.setEnabled(true);
        mDesc.setEnabled(true);
        mSex.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();
        mPortrait.setEnabled(false);
        mDesc.setEnabled(false);
        mSex.setEnabled(false);
        mSubmit.setEnabled(false);
    }


    @Override
    public void updateSucceed() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
