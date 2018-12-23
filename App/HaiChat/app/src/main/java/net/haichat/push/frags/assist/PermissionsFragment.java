package net.haichat.push.frags.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.haichat.common.app.Application;
import net.haichat.push.R;
import net.haichat.push.frags.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请弹出框
 */
public class PermissionsFragment extends BottomSheetDialogFragment
        implements EasyPermissions.PermissionCallbacks {

    private PermissionsFragment instance;

    public PermissionsFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 我们先使用默认的
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取布局
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        // 获取授权按钮
        root.findViewById(R.id.btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPerm();// 发起 申请权限 操作
                    }
                });
        return root;
    }

    // 当授权界面 操作结束，当前 F 重新 获得焦点的时候
    @Override
    public void onResume() {
        super.onResume();
        // 界面显示的收进行刷新 授权状态信息
        refreshState(getView());
    }

    /**
     * 刷新我们的布局中的图片的状态
     *
     * @param root 根布局
     */
    private void refreshState(View root) {
        if (root == null) return;
        Context context = getContext();
        root.findViewById(R.id.im_state_permission_network)
                .setVisibility(haveNetworkPerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_read)
                .setVisibility(haveReadPerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_write)
                .setVisibility(haveWritePerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_record_audio)
                .setVisibility(haveRecordAudioPerm(context) ? View.VISIBLE : View.GONE);
    }

    /**
     * 判断 是否有 网络权限
     */
    private static boolean haveNetworkPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 判断 是否有 外部存储读取权限
     */
    private static boolean haveReadPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 判断 是否有 外部存储写权限
     */
    private static boolean haveWritePerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 判断 是否有 录音权限
     */
    private static boolean haveRecordAudioPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 进入应用的时候检查 是否具有所有权限
     *
     * @param context Context
     * @param manager FragmentManager
     * @return true：已拥有所有权限
     */
    public static boolean haveAll(Context context, FragmentManager manager) {
        // 检查是否具有所有的权限
        return haveNetworkPerm(context)
                && haveReadPerm(context)
                && haveWritePerm(context)
                && haveRecordAudioPerm(context);
    }

    /**
     * show 方法
     *
     * @param manager
     */
    public static void show(FragmentManager manager) {
        // 调用 BottomSheetDialogFragment 已经准备好的 显示方法
        new PermissionsFragment()
                .show(manager, PermissionsFragment.class.getName());
    }

    // 权限回调的标识
    private static final int RC_ALL = 0x0100;

    // 这个注解表示，请求授权 RC_ALL 结束后，会回调 带有 RC_ALL 标识 的 方法
    @AfterPermissionGranted(RC_ALL)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
        };

        if (EasyPermissions.hasPermissions(getContext(), perms)) { // 有所有权限
            Application.showToast(R.string.label_permission_ok); // 授权成功
            // Fragment 中调用 getView 可以直接 获取 根布局，
            // 前提是 必须在 onCreateView 方法之后
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(
                    this, getString(R.string.title_assist_permissions),
                    RC_ALL, perms
            );
        }
    }

    // 权限申请成功
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    // 权限申请失败
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 如果有没有申请成功的权限存在，则弹出弹出框，用户点击后去到 设置界面 自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    /**
     * 获取权限申请的结果，将结果 交给 EasyPermission 框架来处理
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应的参数， 权限处理者 是 实现了 EasyPermissions.PermissionCallbacks 的当前类 也就是 this
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
