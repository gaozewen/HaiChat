package net.haichat.factory.data.helper;

import net.haichat.factory.R;
import net.haichat.factory.data.DataSource;
import net.haichat.factory.model.api.account.RegisterModel;
import net.haichat.factory.model.db.User;

/**
 * Account 相关 Restful API
 */
public class AccountHelper {

    /**
     * 注册 API，异步调用
     *
     * @param model    注册接口所需参数 model
     * @param callBack 请求成功与失败的回调
     */
    public static void register(RegisterModel model, final DataSource.CallBack<User> callBack) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callBack.onDataNotAvailable(R.string.data_rsp_error_parameters);
            }
        }.start();
    }

}
