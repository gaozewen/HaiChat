package net.haichat.push.frags.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.haichat.common.app.Application;
import net.haichat.common.app.Fragment;
import net.haichat.common.widget.PortraitView;
import net.haichat.factory.Factory;
import net.haichat.factory.net.UploadHelper;
import net.haichat.push.R;
import net.haichat.push.frags.media.GalleryFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

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
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 将 选中的头像图片 在 界面上显示
     * @param uri
     */
    private void loadPortrait(Uri uri) {
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

        // 获取 本地文件地址
        String localPath = uri.getPath();
        Log.e("gzw","localPath: "+localPath);

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = UploadHelper.uploadPortrait(localPath);
                Log.e("gzw","callbackUrl: "+url);
            }
        });
    }
}
