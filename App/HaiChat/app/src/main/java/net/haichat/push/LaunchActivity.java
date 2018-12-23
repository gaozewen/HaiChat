package net.haichat.push;

import android.Manifest;

import net.haichat.common.app.Activity;
import net.haichat.push.activities.MainActivity;

public class LaunchActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }


    @Override
    protected void onResume() {
        System.out.println('2');
        super.onResume();
        // 老师写的授权方法
//        if(PermissionsFragment.haveAll(this,getSupportFragmentManager())){
//            MainActivity.show(this);
//            finish();// 跳转之后销毁此界面，这样 就不能再 back 到此界面了
//        }else{
//            PermissionsFragment.show(getSupportFragmentManager());
//        }


        performCodeWithPermission("获取应用所需权限", new PermissionCallback() {
                    @Override
                    public void hasPermission() {
                        MainActivity.show(LaunchActivity.this);
                        finish();
                        System.out.println("gzw success");
                    }

                    @Override
                    public void noPermission() {
                        System.out.println("gzw fail");
                        finish();

                    }
                },
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO);
    }

}
