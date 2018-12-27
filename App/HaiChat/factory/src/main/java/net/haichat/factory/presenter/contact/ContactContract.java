package net.haichat.factory.presenter.contact;

import net.haichat.factory.model.db.User;
import net.haichat.factory.presenter.BaseContract;


public interface ContactContract {


    // 什么都不需要额外定义，直接调用 start 即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类里面完成了
    interface View extends BaseContract.RecyclerView<Presenter, User> {

    }

}
