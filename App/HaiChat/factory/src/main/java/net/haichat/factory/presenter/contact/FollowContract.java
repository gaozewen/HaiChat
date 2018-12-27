package net.haichat.factory.presenter.contact;

import net.haichat.factory.model.card.UserCard;
import net.haichat.factory.presenter.BaseContract;

/**
 * 关注接口 定义
 */
public interface FollowContract {
    // 任务调度者
    interface Presenter extends BaseContract.Presenter {
        // 关注一个人
        void follow(String userId);
    }

    interface View extends BaseContract.View<FollowContract.Presenter> {
        void onFollowSucceed(UserCard userCard);
    }

}
