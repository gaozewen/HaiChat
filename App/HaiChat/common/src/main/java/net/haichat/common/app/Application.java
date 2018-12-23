package net.haichat.common.app;

import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

public class Application extends android.app.Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 清空该目录下所有文件
     * @param dir
     */
    private static void clearDir(File dir) {
        File[] files = dir.listFiles();
        if(files != null && files.length>0){
            for (File file : files) {
                file.delete();
            }
        }
    }

    /**
     * 外部获取单例
     * @return
     */
    public static Application getInstance(){
        return instance;
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 当前 APP 的缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }


    /**
     * 获取 头像 的 临时存储文件地址
     * @return File
     */
    public static File getPortraitTmpFile() {
        // 获取 缓存头像 的 目录
        File dir = new File(getCacheDirFile(), "portrait");
        // 创建所有对应的文件夹
        dir.mkdirs();
        // 清空缓存目录下的 缓存文件
        clearDir(dir);
        // 目标：我们需要保证 文件名 随机可变，且 能随时被清理掉
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        // 返回一个 当前时间戳 的 头像文件地址
        return path.getAbsoluteFile();
    }

    /**
     * 获取声音文件的本地地址
     * @param isTmp 是否是缓存文件，
     *              True， 每次返回的文件地址都是一样的
     *              False，返回的是用来上传 语音文件名
     * @return File 录音文件地址
     */
    public static File getAudioTmpFile(boolean isTmp){
        File dir = new File(getCacheDirFile(), "audio");
        dir.mkdirs();
        clearDir(dir);
        File path = new File(getCacheDirFile(),isTmp?"tmp.mp3":SystemClock.uptimeMillis()+".mp3");
        return path.getAbsoluteFile();
    }

    /**
     * 显示一个 Toast
     * @param msg 字符串
     */
    public static void showToast(final String msg){
        // Toast 只能在 主线程中显示，所以需要进行 线程转换
        // Toast.makeText(instance,msg,Toast.LENGTH_SHORT).show();

        // 这个库 实现了对 Handler 的封装，
        // 同时解决了 常规情况下 切换到主线程 对 Handler 的一些处理
        // 对 handler 资源的释放，防止内存泄漏
        // 这里对 Handler 进行了一定的封装，同时加上了一些线程调度
        // onUiAsync 方法 是 把 Action 方法 放到 UI 线程中去执行，同时 相对于我当前的 调度方法是异步的
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示一个 Toast
     * @param msgId 字符串资源 id
     */
    public static void showToast(@StringRes int msgId){
        showToast(instance.getString(msgId));
    }

}
