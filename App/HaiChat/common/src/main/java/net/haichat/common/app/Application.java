package net.haichat.common.app;

import android.os.SystemClock;

import java.io.File;

public class Application extends android.app.Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 当前 APP 的缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    public static File getPortraitTmpFile() {
        // 获取 缓存头像 的 目录
        File dir = new File(getCacheDirFile(), "portrait");
        // 创建所有对应的文件夹
        dir.mkdirs();

        // 清空缓存目录下的 缓存文件
        File[] files = dir.listFiles();
        if(files != null && files.length>0){
            for (File file : files) {
                file.delete();
            }
        }

        // 目标：我们需要保证 文件名 随机可变，且 能随时被清理掉
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        // 返回一个 当前时间戳 的 头像文件地址
        return path.getAbsoluteFile();
    }
}
