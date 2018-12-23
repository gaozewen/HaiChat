package net.haichat.common.app;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;

public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在界面未初始化之前调用的初始化窗口
        initWindows();

        if(initArgs(getIntent().getExtras())) { // 初始化数据成功
            // 状态栏透明
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            // 得到界面 id 并设置到 界面中
            setContentView(getContentLayoutId());
            initWidget();
            initData();
        }else {
            finish();
        }

    }

    // 初始化窗口的一些数据
    protected void initWindows() {

    }


    // 默认返回true
    // 子类可重写 做相应的逻辑判断

    /**
     * 初始化相关参数
     * @param bundle 接收到的参数
     * @return 如果参数正确 返回 true，错误返回 false
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    // 子类必须重写此方法
    // 作用：获取布局id
    /**
     * 得到当前界面的资源文件 id
     * @return 资源文件 id
     */
    protected abstract int getContentLayoutId();

    // protected 只要继承这个类就能使用

    /**
     * 初始化控件
     */
    protected void initWidget(){
        // 写了这个就不用再 写 findViewById 这些东西啦
        ButterKnife.bind(this); // Activity 的绑定
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    // 重写返回事件

    // 点击导航上的 返回上一页
    @Override
    public boolean onSupportNavigateUp() {
        finish();// 当点击界面导航返回时，Finish 当前界面
        return super.onSupportNavigateUp();
    }

    // 按手机 back 键返回
    @Override
    public void onBackPressed() {
        // 情景：当此 Activity 中 有多个 Fragment，而我只想返回上一个 Fragment
        // 知识点：涉及到 Activity 和 Fragment 之间的通讯

        // 得到当前 Activity 下的所有 F
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断 Fs 是否为空
        if(fragments.size() > 0){
            for (Fragment fragment : fragments) {
                // 如果这个 Fragment 是我们自己写的 F
                if(fragment instanceof net.haichat.common.app.Fragment){
                    // F 自己处理了返回操作，拦截了返回按钮，直接 return
                    if(((net.haichat.common.app.Fragment) fragment).onBackPressed()) return;
                }
            }
        }

        super.onBackPressed();
        finish();
    }

    //**************** Android M Permission (Android 6.0权限控制代码封装)
    private int permissionRequestCode = 88;
    private PermissionCallback permissionRunnable;

    public interface PermissionCallback {
        void hasPermission();

        void noPermission();
    }

    /**
     * Android M运行时权限请求封装
     *
     * @param permissionDes 权限描述
     * @param runnable      请求权限回调
     * @param permissions   请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.WRITE_CONTACTS
     */
    public void performCodeWithPermission(@NonNull String permissionDes, PermissionCallback runnable, @NonNull String... permissions) {
        if (permissions.length == 0)
            return;
        // this.permissionrequestCode = requestCode;
        this.permissionRunnable = runnable;
        // Build.VERSION.SDK_INT 来自 build.gradle.
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || checkPermissionGranted(permissions)) {
            if (permissionRunnable != null) {
                permissionRunnable.hasPermission();
                permissionRunnable = null;
            }
        } else {
            //permission has not been granted.
            requestPermission(permissionDes, permissionRequestCode, permissions);
        }

    }

    private boolean checkPermissionGranted(String[] permissions) {
        boolean isGranted = true;
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }

    private void requestPermission(String permissionDes, final int requestCode, final String[] permissions) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            /*1. 第一次请求权限时，用户拒绝了，下一次：shouldShowRequestPermissionRationale()  返回 true，应该显示一些为什么需要这个权限的说明
            2.第二次请求权限时，用户拒绝了，并选择了“不在提醒”的选项时：shouldShowRequestPermissionRationale()  返回 false
            3. 设备的策略禁止当前应用获取这个权限的授权：shouldShowRequestPermissionRationale()  返回 false*/
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.

            //            Snackbar.make(getWindow().getDecorView(), requestName,
            //                    Snackbar.LENGTH_INDEFINITE)
            //                    .setAction(R.string.common_ok, new View.OnClickListener() {
            //                        @Override
            //                        public void onClick(View view) {
            //                            ActivityCompat.requestPermissions(BaseAppCompatActivity.this,
            //                                    permissions,
            //                                    requestCode);
            //                        }
            //                    })
            //                    .show();
            //如果用户之前拒绝过此权限，再提示一次准备授权相关权限
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(permissionDes)
                    .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Activity.this, permissions, requestCode);
                        }
                    }).show();

        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(Activity.this, permissions, requestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean isShow = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                isShow = true;
                break;
            }
        }
        return isShow;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == permissionRequestCode) {
            if (verifyPermissions(grantResults)) {
                if (permissionRunnable != null) {
                    permissionRunnable.hasPermission();
                    permissionRunnable = null;
                }
            } else {
                Toast.makeText(this, "暂无权限执行相关操作！", Toast.LENGTH_SHORT).show();
                if (permissionRunnable != null) {
                    permissionRunnable.noPermission();
                    permissionRunnable = null;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    //********************** END Android M Permission ****************************************
}
