package net.haichat.factory.presenter.contact;


import net.haichat.factory.data.ApiCallback;
import net.haichat.factory.data.helper.UserHelper;
import net.haichat.factory.model.card.UserCard;
import net.haichat.factory.presenter.BasePresenter;
import net.haichat.factory.presenter.search.SearchContract;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 * 关注 的逻辑实现
 */
public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter, ApiCallback.Callback<UserCard> {
    public FollowPresenter(FollowContract.View view) {
        super(view);
    }


    @Override
    public void follow(String targetId) {
        start();
        UserHelper.follow(targetId, this);
    }


    @Override
    public void onDataLoadedSuccess(UserCard userCard) {
        // 关注成功
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(userCard);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(int strRes) {
        // 关注失败
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
